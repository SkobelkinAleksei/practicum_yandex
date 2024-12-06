package manager;

import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    private static TaskManager taskManager;
    private static InMemoryHistoryManagerImpl historyManager;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void getHistoryLastOf10Tasks() {
        for (int i = 0; i < 15; i++) {
            taskManager.createTasks(new Task("Name", "description"));
        }
        ArrayList<Task> allTasks = taskManager.getAllTasks();
        for (Task allTask : allTasks) {
            taskManager.getTaskById(allTask.getId());
        }
        List<Task> historyTask = taskManager.getHistory();
        assertEquals(15, historyTask.size(), "Неверное количество элементов");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task = new Task("One", "Desription");
        taskManager.createTasks(task);
        taskManager.getTaskById(task.getId());
        taskManager.updateTask(new Task("Two", "Desription", Status.DONE, task.getId()));
        List<Task> taskHistory = taskManager.getHistory();
        Task firstTask = taskHistory.getFirst();
        assertEquals(task.getName(), firstTask.getName(), "Не сохранилась старая версия задачи");
        assertEquals(task.getDescription(), firstTask.getDescription(), "Не сохранилась старая версия задачи");
    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic epic = new Epic("One", "Desription");
        taskManager.createEpics(epic);
        taskManager.getEpicsById(epic.getId());
        taskManager.updateEpic(new Epic("Two", "Desription", Status.DONE, epic.getId()));
        List<Task> epicHistory = taskManager.getHistory();
        Epic firstEpic = (Epic) epicHistory.getFirst();
        assertEquals(epic.getName(), firstEpic.getName(), "Не сохранилась старая версия задачи");
        assertEquals(epic.getDescription(), firstEpic.getDescription(), "Не сохранилась старая версия задачи");

    }

    @Test
    public void getHistoryShouldReturnOldSubTaskAfterUpdate() {
        Epic epic = new Epic("One", "Desription");
        taskManager.createEpics(epic);

        SubTask subTask = new SubTask("OneSub", "TwoSub", epic.getId());
        taskManager.createSubTask(subTask);
        taskManager.getSubTaskById(subTask.getId());
        taskManager.updateSubTask(new SubTask(subTask.getId(), "Two", "Desription", Status.DONE, epic.getId()));
        List<Task> epicHistory = taskManager.getHistory();
        SubTask firstSubTask = (SubTask) epicHistory.getFirst();
        assertEquals(subTask.getName(), firstSubTask.getName(), "Не сохранилась старая версия задачи");
        assertEquals(subTask.getDescription(), firstSubTask.getDescription(), "Не сохранилась старая версия задачи");

    }

    @Test
    void add() {

        Task task = new Task("One", "Desription");
        taskManager.createTasks(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }


}
