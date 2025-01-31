package HandlerTest;

import org.example.Epic;
import org.example.InMemoryTaskManager;
import org.example.Status;
import org.example.TaskManager;
import org.example.http.EpicHandler;
import org.example.http.HttpTaskServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EpicHandlerTest {

    private EpicHandler epicHandler;
    private TaskManager taskManager;
    private HttpTaskServer httpTaskServer;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        epicHandler = new EpicHandler();
        httpTaskServer = new HttpTaskServer(taskManager);
    }

    @Test
    public void testGetEpics() throws IOException {
        Epic epic = new Epic("Epic", "Description", Status.NEW, 1, Duration.ZERO, LocalDateTime.MIN);
        taskManager.createEpics(epic);

        TestHttpExchange exchange = new TestHttpExchange("GET", "", "");
        epicHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Epic")); //
    }

    @Test
    public void testCreateEpic() throws IOException {
        String requestBody = "{\"name\":\"Epic\", \"description\":\"Description\", \"status\":\"NEW\", \"id\":\"1\", \"duration\":\"0\", \"startTime\":\"2025-01-30T12:00:00\"}";
        System.out.println(requestBody);
        TestHttpExchange exchange = new TestHttpExchange("POST", requestBody, "");
        epicHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Эпик создан успешно"));
    }

    @Test
    public void testDeleteEpic() throws IOException {
        Epic epic = new Epic("Test Epic", "Description", Status.NEW, 1, Duration.ZERO, LocalDateTime.now());
        taskManager.createEpics(epic);

        TestHttpExchange exchange = new TestHttpExchange("DELETE", "", "id=" + epic.getId());
        epicHandler.handle(exchange);

        assertTrue(exchange.getResponse().contains("Эпик удален успешно"));
        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
    }
}
