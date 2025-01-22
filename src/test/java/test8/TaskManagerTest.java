package test8;

import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager = (T) new InMemoryTaskManager();

    protected abstract T createTaskManager();

    @BeforeEach
    public abstract void setUp() throws IOException;

    // Проверка создания задачи
    @Test
    public void testCreateTask() {
        Task task = new Task("Task 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);
        assertEquals(1, taskManager.getAllTasks().size(), "Задача должна быть создана.");
    }

    // Проверка получения задачи по ID
    @Test
    public void testGetTaskById() {
        Task task = new Task("Task 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);
        Task fetchedTask = taskManager.getTaskById(1);
        assertNotNull(fetchedTask, "Задача должна быть найдена по ID.");
        assertEquals("Task 1", fetchedTask.getName(), "Имя задачи должно совпадать.");
    }

    // Проверка удаления задачи
    @Test
    public void testDeleteTaskById() {
        Task task = new Task("Task 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);
        assertTrue(taskManager.deleteTaskById(1), "Задача должна быть удалена.");
        assertNull(taskManager.getTaskById(1), "Задача должна быть не найдена после удаления.");
    }

    // Проверка статуса Epic
    @Test
    public void testEpicStatusCalculation() {

        Epic epic = new Epic("Epic 1", "Epic description", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        SubTask subTask1 = new SubTask("SubTask 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description", Status.DONE, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 15, 10), epic.getId());
        taskManager.createEpics(epic);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, subTask1.getStatus(), "Статус подзадачи должен быть IN_PROGRESS.");

        // Установим все подзадачи в DONE
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateEpicStatus(epic);
        assertEquals(Status.DONE, subTask1.getStatus(), "Статус подзадачи должен быть DONE.");

        // Установим все подзадачи в NEW
        subTask1.setStatus(Status.NEW);
        taskManager.updateSubTask(subTask1);
        taskManager.updateEpicStatus(epic);
        assertEquals(Status.NEW, subTask1.getStatus(), "Статус подзадачи должен быть NEW.");

    }

    // Проверка пересечения задач
    @Test
    public void testTaskOverlap() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.createTasks(task1);

        Task task2 = new Task("Task 2", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 11, 0));


        assertThrows(ManagerSaveException.class, () -> {
            taskManager.createTasks(task2);
        }, "Задачи пересекаются по времени.");
    }

}
