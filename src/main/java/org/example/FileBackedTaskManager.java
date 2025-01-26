package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String CSV_FILE = "id,type,name,status,description,epic";
    private static final String[] keys = {"id", "type", "name", "status", "description", "startTime", "duration", "epicId"};

    private File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public TaskType getType(Task task) {
        if (task instanceof SubTask) {
            return TaskType.SUBTASK;
        } else if (task instanceof Epic) {
            return TaskType.EPIC;
        }
        return TaskType.TASK;
    }

    public String toString(Task task) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("id", Integer.toString(task.getId()));
        params.put("type", getType(task).toString());
        params.put("name", task.getName());
        params.put("status", task.getStatus().toString());
        params.put("description", task.getDescription());
        params.put("epicId", getEpicId(task));

        return String.join(",", params.values());
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");

        Map<String, String> params = new LinkedHashMap<>();

        for (int i = 0; i < keys.length && i < value.length(); i++) {
            String currentValue = (i < values.length) ? values[i] : null;
            params.put(keys[i], currentValue);
        }

        int id = Integer.parseInt(params.get("id"));
        String type = params.get("type");
        String name = params.get("name");
        Status status = Status.valueOf(params.get("status"));
        String description = params.get("description");
        int epicId = 0;
        LocalDateTime startTime = null;
        Duration duration = null;

        if (params.containsKey("startTime") && params.get("startTime") != null) {
            startTime = LocalDateTime.parse(params.get("startTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        if (params.containsKey("duration") && params.get("duration") != null) {
            duration = Duration.parse(params.get("duration"));
        }

        if (type.equals("SubTask") && params.containsKey("epicId")) {
            epicId = Integer.parseInt(params.get("epicId"));
        }

        if (type.equals("Task")) {
            return new Task(name, description, status, id, duration, startTime);
        } else if (type.equals("SubTask")) {
            return new SubTask(name, description, status, id, duration, startTime, epicId);
        } else {
            return new Epic(name, description, status, id, duration, startTime);
        }

    }

    public String getEpicId(Task task) {
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getEpicId());
        }

        return "";
    }

    public void save() {
        try (Writer writer = new FileWriter(file)) {
            // Первая строка заголовка
            writer.write("id,type,title,status,description,startTime,duration,epicId\n");

            // Используем TreeMap для сортировки по ключам
            Map<Integer, String> allTasks = new TreeMap<>();

            // Сохраняем задачи в allTasks, убирая кавычки из строк
            Map<Integer, Task> tasks = super.getTasks();
            for (Integer id : tasks.keySet()) {
                allTasks.put(id, formatTask(tasks.get(id)));
            }

            Map<Integer, Epic> epics = super.getEpics();
            for (Integer id : epics.keySet()) {
                allTasks.put(id, formatTask(epics.get(id)));
            }

            Map<Integer, SubTask> subtasks = super.getSubTasks();
            for (Integer id : subtasks.keySet()) {
                allTasks.put(id, formatTask(subtasks.get(id)));
            }

            // Пишем все задачи
            for (String value : allTasks.values()) {
                writer.write(value + "\n");
            }
            writer.write("\n");

            // История
            for (Task task : super.getHistory()) {
                writer.write(task.getId() + ",");
            }

        } catch (IOException exception) {
            throw new ManagerSaveException("Unable to write file");
        }
    }

    // Метод для форматирования задачи без кавычек
    private String formatTask(Task task) {
        String epicPart = task instanceof SubTask subTask ? String.valueOf(subTask.getEpicId()) : "";
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                task.getId(),
                task.getClass().getSimpleName(), // Тип задачи
                task.getName(), // Название без кавычек
                task.getStatus(),
                task.getDescription(), // Описание без кавычек
                task.getStartTime(),
                task.getDuration(),
                epicPart // ID эпика для SubTask
        );
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Файл не найден: " + file.getPath());
        }

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;

            // Пропускаем первую строку (заголовок файла CSV)
            line = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    // Пустая строка может сигнализировать о конце блока задач
                    break;
                }

                // Конвертируем строку в задачу
                Task task = fromString(line);

                // Добавляем задачу в соответствующий список
                if (task instanceof SubTask subTask) {
                    manager.addSubTask(subTask);
                }

                if (task instanceof Epic epic) {
                    manager.addEpic(epic);
                } else {
                    manager.addTask(task);
                }
            }

            // Восстановление истории просмотра задач
            if ((line = bufferedReader.readLine()) != null && !line.trim().isEmpty()) {
                List<Integer> historyIds = parseHistory(line);

                for (Integer id : historyIds) {
                    Task task = manager.getTaskById(id); // Получение задачи по ID
                    if (task != null) {
                        manager.addToHistory(task); // Добавляем задачу в историю просмотров
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла: " + e.getMessage(), e);
        }

        return manager;
    }


    private static List<Integer> parseHistory(String line) {
        if (line.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String[] ids = line.split(",");
        List<Integer> history = new ArrayList<>();
        for (String id : ids) {
            try {
                history.add(Integer.parseInt(id.trim())); // Преобразуем только валидные числа
            } catch (NumberFormatException e) {
                // Пропускаем невалидные числа
            }
        }
        return history;
    }

    public void addToHistory(Task task) {
        if (task != null) {
            historyManager.add(task);
        }
    }


    private void uploadHistory(List<Integer> ids) {
        for (Integer id : ids) {

            if (tasks.containsKey(id)) {
                historyManager.add(getTaskById(id));

            } else if (subTasks.containsKey(id)) {
                historyManager.add(getSubTaskById(id));

            } else {
                historyManager.add(getEpicsById(id));
            }
        }
    }

    private static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        List<Integer> history = new ArrayList<>(split.length);

        for (String string : split) {
            history.add(Integer.parseInt(string));
        }

        return history;
    }

    private static String historyToString(HistoryManager manager) {
        String[] historyIds = new String[manager.getHistory().size()];

        for (int i = 0; i < manager.getHistory().size(); i++) {
            int id = manager.getHistory().get(i).getId();
            historyIds[i] = Integer.toString(id);
        }

        return String.join(",", historyIds);
    }

    public void addTask(Task task) {
        super.createTasks(task);
        save();
    }

    public void addEpic(Epic epic) {
        super.createEpics(epic);
        save();
    }

    public void addSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    public void getEpic(int id) {
        super.getEpicsById(id);
        save();
    }

    public Task getTask(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    public SubTask getSubTask(int subTaskId) {
        SubTask task = super.getSubTaskById(subTaskId);
        save();
        return task;
    }

    public void updateEpics(Epic epic) {
        super.updateEpic(epic);
        save();
    }


    public void updateSubTasks(SubTask task) {
        super.updateSubTask(task);
        save();
    }

    public void updateTasks(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = super.getAllTasks();
        save();
        return allTasks;
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> allSubTasks = super.getAllSubTasks();
        save();
        return allSubTasks;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpics = super.getAllEpics();
        save();
        return allEpics;
    }


    public void removeAllTasks() {
        super.deleteTasks();
        save();
    }


    public void removeAllEpics() {
        super.deleteEpics();
        save();
    }


    public void removeAllSubTasks() {
        super.deleteSubTasks();
        save();
    }


    public void removeTask(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }


    public void removeEpic(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }


    public void removeSubTask(int subTaskId) {
        super.deleteTaskById(subTaskId);
        save();
    }


}











