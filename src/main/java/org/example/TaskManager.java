package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    Task createTasks(Task task);

    Epic createEpics(Epic epic);

    SubTask createSubTask(SubTask subTask);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    SubTask updateSubTask(SubTask subTask);

    boolean deleteTaskById(int id);

    boolean deleteEpicById(int id);

    boolean deleteSubTaskById(int id);

    List<SubTask> getSubTaskListByEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, SubTask> getSubTasks();

    Map<Integer, Task> getTasks();

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    List<Task> getPrioritiTasks();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicsById(int id);

    List<Task> getHistory();
}
