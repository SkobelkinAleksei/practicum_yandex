package org.example.http;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetTasks(exchange);
                break;
            case "POST":
                handleCreateTask(exchange);
                break;
            case "DELETE":
                handleDeleteTask(exchange);
                break;
            default:
                sendText(exchange, "Method Not Allowed");
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = HttpTaskServer.taskManager.getAllTasks();
        String jsonResponse = HttpTaskServer.gson.toJson(tasks);
        sendText(exchange, jsonResponse);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        try {
            Task task = HttpTaskServer.gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Task.class);
            HttpTaskServer.taskManager.createTasks(task);
            sendText(exchange, "{\"message\":\"Task добавлен успешно\"}");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_CREATED, -1);
        } catch (Exception e) {
            sendInternalServerError(exchange, e.getMessage());
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            HttpTaskServer.taskManager.deleteTaskById(id);
            sendText(exchange, "{\"message\":\"Task удален успешно\"}");
        } else {
            sendNotFound(exchange);
        }
    }
}