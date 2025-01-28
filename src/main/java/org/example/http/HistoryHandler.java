package org.example.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Task> history = HttpTaskServer.taskManager.getHistory();
            String jsonResponse = HttpTaskServer.gson.toJson(history);
            sendText(exchange, jsonResponse);
        } else {
            sendText(exchange, "Method Not Allowed");
        }
    }
}