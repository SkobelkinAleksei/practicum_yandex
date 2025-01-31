package HandlerTest;

import org.example.InMemoryTaskManager;
import org.example.Status;
import org.example.Task;
import org.example.TaskManager;
import org.example.http.HistoryHandler;
import org.example.http.HttpTaskServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryHandlerTest {
    private HistoryHandler historyHandler;
    private TaskManager taskManager;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager); // Инициализируйте HttpTaskServer
        historyHandler = new HistoryHandler();
        HttpTaskServer.taskManager = taskManager; // Установите taskManager в HttpTaskServer
    }

    @Test
    public void testGetHistory() throws IOException {
        Task task = new Task("Test", "Description", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createTasks(task);
        TestHttpExchange exchange = new TestHttpExchange("GET", "", "");
        historyHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        System.out.println("Response: " + exchange.getResponse());
        assertTrue(exchange.getResponse().contains("Test"));
    }
}