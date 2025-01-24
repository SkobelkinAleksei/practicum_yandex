package manager;

import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void addNewTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        final Task taskId = taskManager.createTasks(task);

        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpicsAndSubTasks() {

        Epic epic = taskManager.createEpics(new Epic("Epic 1", "Epic description", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN));
        SubTask subTask = taskManager.createSubTask(new SubTask("Пройти 5 тем", "По учебнику от Галины Семеновны", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId()));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Изучить вопросы экзамена", "Спросить у Саши", Status.NEW, 2, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Пройти 2 тем", "По учебнику от Семена", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId()));

        Epic savedEpic = taskManager.getEpicsById(epic.getId());
        SubTask subTaskById = taskManager.getSubTaskById(subTask.getId());
        SubTask subTaskByIdSecond = taskManager.getSubTaskById(subTask1.getId());
        SubTask subTaskById3 = taskManager.getSubTaskById(subTask2.getId());

        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(subTask, subTaskById, "Подзадачи не совпадают");
        assertEquals(subTask1, subTaskByIdSecond, "Подзадачи не совпадают");
        assertEquals(subTask2, subTaskById3, "Подзадачи не совпадают");
        assertNotNull(savedEpic, "Эпик не найден");
        assertNotNull(subTaskById3, "Подзадача не найдена");

        List<SubTask> subTaskList = savedEpic.getSubTaskList();
        assertNotNull(subTaskList, "В списке нет Подзадач");
        assertEquals(subTaskById, subTaskList.getFirst(), "Подзадачи не совпадают");
        assertEquals(3, subTaskList.size(), "Неверное количество Подзадач");

        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "В списке нет Эпиков");
        assertEquals(epic, epics.getFirst(), "Эпики не совпадают");
        assertEquals(1, epics.size(), "Неверное количество Эпиков");
    }

    @Test
    public void updateTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);
        Task updatedTask = new Task("Task 2", "Description", Status.NEW, task.getId(), Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        Task actualTask = taskManager.updateTask(updatedTask);
        assertEquals(task.getId(), actualTask.getId(), "Не совпадает айди задач");
    }

    @Test
    public void updateEpic() {
        Epic epic = new Epic("Epic 1", "Epic description", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic);
        Epic updatedEpic = new Epic("Test 2", "descriptoin", Status.DONE, epic.getId(), Duration.ZERO, LocalDateTime.MIN);
        Epic actualEpic = taskManager.updateEpic(updatedEpic);
        assertEquals(epic.getId(), actualEpic.getId(), "Не совпадает айди Эпиков");
    }

    @Test
    public void updateSubTask() {
        Epic epic = new Epic("Выучить Английский", "Успеть за пол года", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic);

        SubTask subTask = new SubTask("SubTask 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        taskManager.createSubTask(subTask);
        SubTask updatedSubTask = new SubTask("Test 2", "descriptoin", Status.DONE, subTask.getId(), Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        SubTask actualSubTask = taskManager.updateSubTask(updatedSubTask);
        assertEquals(subTask.getId(), actualSubTask.getId(), "Не совпадает айди Подзадач");
    }

    @Test
    public void deleteTasks() {
        Task task = new Task("Task 1", "Description", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);
        taskManager.deleteTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список задач должен быть пуст");
    }

    @Test
    public void deleteEpics() {
        Epic epic = new Epic("Выучить Английский", "Успеть за пол года", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic);
        taskManager.deleteEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Список Эпиков должен быть пуст");
    }

    @Test
    public void deleteSubTasks() {

        Epic epic = new Epic("Выучить Английский", "Успеть за пол года", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic);

        SubTask subTask = new SubTask("SubTask 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty(), "Список подзадач должен быть пуст");
    }

    @Test
    public void createdTaskAndGenerateTaskShouldNotConflicts() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        Task task2 = new Task("Task 2", "Description", Status.NEW, 2, Duration.ofHours(2), LocalDateTime.of(2023, 10, 2, 12, 10));
        taskManager.createTasks(task1);
        taskManager.createTasks(task2);
        assertNotEquals(task1.getId(), task2.getId(), "ID должны быть уникальные");
        assertTrue(taskManager.getTasks().containsKey(task1.getId()));
        assertTrue(taskManager.getTasks().containsKey(task2.getId()));

    }

    @Test
    public void deleteTaskById() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        Task task2 = new Task("Task 2", "Description", Status.NEW, 2, Duration.ofHours(2), LocalDateTime.of(2023, 10, 2, 12, 10));
        taskManager.createTasks(task1);
        taskManager.createTasks(task2);
        assertFalse(taskManager.deleteTaskById(3), "Ключа нет в списке");

    }

    @Test
    public void deleteEpicById() {
        Epic epic1 = new Epic("Выучить Английский", "Успеть за пол года", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        Epic epic2 = new Epic("Выучить Английский", "Успеть за пол года", Status.NEW, 2, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic1);
        taskManager.createEpics(epic2);
        assertFalse(taskManager.deleteEpicById(3), "Ключа нет в списке");

    }

    @Test
    public void deleteSubTaskById() {

        Epic epic = new Epic("Выучить Английский", "Успеть за пол года", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        SubTask subTask2 = new SubTask("SubTask 1", "Description", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        assertFalse(taskManager.deleteSubTaskById(4), "Ключа нет в списке");

    }

}
