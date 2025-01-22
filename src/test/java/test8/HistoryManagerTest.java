package test8;

import org.example.HistoryManager;
import org.example.InMemoryHistoryManagerImpl;
import org.example.Status;
import org.example.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManagerImpl();
    }

    @Test
    public void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой.");
    }

    @Test
    public void testAddToHistory() {
        Task task = new Task("Task 1", "Description", Status.NEW, 1);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "История должна содержать одну задачу.");
    }

    @Test
    public void testDuplicateTaskInHistory() {
        Task task = new Task("Task 1", "Description", Status.NEW, 1);
        historyManager.add(task);
        historyManager.add(task); // Добавляем дубликат
        assertEquals(1, historyManager.getHistory().size(), "История не должна содержать дубликаты.");
    }

    @Test
    public void testRemoveFromHistoryStart() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, 1);
        Task task2 = new Task("Task 2", "Description", Status.NEW, 2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1); // Удаляем первую задачу
        assertEquals(1, historyManager.getHistory().size(), "История должна содержать одну задачу после удаления.");
    }

    @Test
    public void testRemoveFromHistoryMiddle() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, 1);
        Task task2 = new Task("Task 2", "Description", Status.NEW, 2);
        Task task3 = new Task("Task 3", "Description", Status.NEW, 3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2); // Удаляем вторую задачу
        assertEquals(2, historyManager.getHistory().size(), "История должна содержать две задачи после удаления.");
    }

    @Test
    public void testRemoveFromHistoryEnd() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, 1);
        Task task2 = new Task("Task 2", "Description", Status.NEW, 2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(2); // Удаляем последнюю задачу
        assertEquals(1, historyManager.getHistory().size(), "История должна содержать одну задачу после удаления.");
    }
}
