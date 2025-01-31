package HandlerTest;

import org.example.InMemoryTaskManager;
import org.example.http.HttpTaskServer;
import org.example.http.PrioritiHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

public class PrioritiHandlerTest {
    private PrioritiHandler prioritiHandler;

    @BeforeEach
    public void setUp() {
        prioritiHandler = new PrioritiHandler();
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException {
        TestHttpExchange exchange = new TestHttpExchange("GET", "", "");
        prioritiHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Список приоритетных задач пока не реализован."));
    }

    @Test
    public void testMethodNotAllowed() throws IOException {
        TestHttpExchange exchange = new TestHttpExchange("POST", "", "");
        prioritiHandler.handle(exchange);

        assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
        assertTrue(exchange.getResponse().contains("Method Not Allowed")); // Проверяем ответ
    }
}
