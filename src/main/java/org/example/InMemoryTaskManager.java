package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final InMemoryHistoryManagerImpl historyManager = new InMemoryHistoryManagerImpl();


    private int nexId = 1;


    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);

        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);

        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;
    }

    @Override
    public Epic getEpicsById(int id) {
        Epic epic = epics.get(id);

        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task createTasks(Task task) {
        task.setId(getNexId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpics(Epic epic) {
        epic.setId(getNexId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId((getNexId()));
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.replace(taskId, task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getId();

        if (epicId == null || !epics.containsKey(epicId)) {
            return null;
        }
        Epic oldEpic = epics.get(epicId);
        List<SubTask> subTaskList = oldEpic.getSubTaskList();

        if (!subTaskList.isEmpty()) {

            for (SubTask subTask : subTaskList) {
                subTasks.remove(subTask.getId());
            }
        }

        epics.replace(epicId, epic);

        List<SubTask> newSubTaskList = epic.getSubTaskList();
        if (!newSubTaskList.isEmpty()) {
            for (SubTask subTask : newSubTaskList) {
                subTasks.put(subTask.getId(), subTask);
            }
        }

        return epic;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        Integer subTaskId = subTask.getId();

        if (subTaskId == null || !subTasks.containsKey(subTaskId)) {
            return null;
        }

        int epicId = subTask.getEpicId();
        SubTask oldSubTask = subTasks.get(subTaskId);

        subTasks.replace(subTaskId, subTask);

        Epic epic = epics.get(epicId);
        List<SubTask> oldSubTaskList = epic.getSubTaskList();
        oldSubTaskList.remove(oldSubTask);
        oldSubTaskList.add(subTask);
        epic.setSubTaskList(oldSubTaskList);
        updateEpicStatus(epic);

        return subTask;
    }

    @Override
    public boolean deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            return false;
        }

        tasks.remove(id);
        return true;
    }

    @Override
    public boolean deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            return false;
        }

        epics.remove(id);
        return true;
    }

    @Override
    public boolean deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            return false;
        }

        SubTask subTask = subTasks.get(id);
        int epicId = subTask.getEpicId();

        Epic epic = epics.get(epicId);
        List<SubTask> subTaskList = epic.getSubTaskList();
        subTaskList.remove(subTask);
        epic.setSubTaskList(subTaskList);

        return true;
    }

    @Override
    public List<SubTask> getSubTaskListByEpic(Epic epic) {
        return epic.getSubTaskList();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        int allIsDoneCount = 0;
        int allIsNewCount = 0;
        List<SubTask> subList = epic.getSubTaskList();

        for (SubTask subTask : subList) {
            if (subTask.getStatus() == Status.DONE) {
                allIsDoneCount++;
            }
            if (subTask.getStatus() == Status.NEW) {
                allIsNewCount++;
            }
        }
        if (allIsDoneCount == subList.size()) {
            epic.setStatus(Status.DONE);
        } else if (allIsNewCount == subList.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        subTasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubTaskList().clear();
        }
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public int getNexId() {
        return nexId++;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }


    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                ", subTasks=" + subTasks +
                ", epics=" + epics +
                '}';
    }

}