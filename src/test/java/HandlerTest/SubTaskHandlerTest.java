package HandlerTest;

import org.example.*;
import org.example.http.HttpTaskServer;
import org.example.http.SubtaskHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskHandlerTest {
    private SubtaskHandler subtaskHandler;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager); // Создание экземпляра
        subtaskHandler = new SubtaskHandler();
    }

    @Test
    public void testGetSubtasks() throws IOException {
        Epic epic = new Epic("Epic 1", "Epic description", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        SubTask subtask = new SubTask("SubTask 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        taskManager.createEpics(epic);
        taskManager.createSubTask(subtask);
        System.out.println(subtask);

        TestHttpExchange exchange = new TestHttpExchange("GET", "", "");
        subtaskHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("SubTask 1"));
    }

    @Test
    public void testCreateSubtask() throws IOException {
        Epic epic = new Epic("Epic 1", "Epic description", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic); // Добавление эпика в менеджер задач

        // Создание запроса на создание подзадачи
        String requestBody = "{\"name\":\"Subtask\", \"description\":\"Description\", \"status\":\"NEW\", \"id\":\"1\", \"duration\":\"0\", \"startTime\":\"2025-01-30T12:00:00\", \"epicId\":\"" + epic.getId() + "\"}";
        TestHttpExchange exchange = new TestHttpExchange("POST", requestBody, "");
        subtaskHandler.handle(exchange);
        
        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Subtask добавлен успешно"));
    }

    @Test
    public void testDeleteSubtask() throws IOException {
        Epic epic = new Epic("Epic 1", "Epic description", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        SubTask subtask = new SubTask("SubTask 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10), epic.getId());
        taskManager.createEpics(epic);
        taskManager.createSubTask(subtask);

        TestHttpExchange exchange = new TestHttpExchange("DELETE", "", "id=" + subtask.getId());
        subtaskHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Subtask удален успешно"));
    }
}
