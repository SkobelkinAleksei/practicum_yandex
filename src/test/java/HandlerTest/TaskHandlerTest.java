package HandlerTest;

import org.example.InMemoryTaskManager;
import org.example.Status;
import org.example.Task;
import org.example.TaskManager;
import org.example.http.HttpTaskServer;
import org.example.http.TaskHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {
    private TaskHandler taskHandler;
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        taskHandler = new TaskHandler();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    }

    @Test
    public void testGetTasks() throws IOException {
        Task task = new Task("Task 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);

        TestHttpExchange exchange = new TestHttpExchange("GET", "", "");
        taskHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Task 1"));
    }

    @Test
    public void testCreateTask() throws IOException {
        String requestBody = "{\"name\":\"Test\", \"description\":\"Description\", \"status\":\"NEW\", \"id\":\"1\", \"duration\":\"0\", \"startTime\":\"2023-10-01T12:10\"}";
        TestHttpExchange exchange = new TestHttpExchange("POST", requestBody, "");
        taskHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_CREATED, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Task добавлен успешно"));
    }

    @Test
    public void testDeleteTask() throws IOException {
        Task task = new Task("Task 1", "Description", Status.NEW, 0, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);

        TestHttpExchange exchange = new TestHttpExchange("DELETE", "", "id=" + task.getId());
        taskHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Task удален успешно"));
    }
}
