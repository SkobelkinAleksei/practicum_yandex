package org.example.http;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import org.example.Epic;
import org.example.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetEpics(exchange);
                break;
            case "POST":
                handleCreateEpic(exchange);
                break;
            case "DELETE":
                handleDeleteEpic(exchange);
                break;
            default:
                sendText(exchange, "Method Not Allowed");
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = HttpTaskServer.taskManager.getAllEpics();
        String jsonResponse = HttpTaskServer.gson.toJson(epics);
        sendText(exchange, jsonResponse);
    }

    private void handleCreateEpic(HttpExchange exchange) throws IOException {
        Epic epic = HttpTaskServer.gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Epic.class);
        HttpTaskServer.taskManager.createEpics(epic);
        sendText(exchange, "{\"message\":\"Epic created successfully\"}");
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            HttpTaskServer.taskManager.deleteEpicById(id);
            sendText(exchange, "{\"message\":\"Epic deleted successfully\"}");
        } else {
            sendNotFound(exchange);
        }
    }
}