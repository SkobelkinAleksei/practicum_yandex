package org.example.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.SubTask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetSubtasks(exchange);
                break;
            case "POST":
                handleCreateSubtask(exchange);
                break;
            case "DELETE":
                handleDeleteSubtask(exchange);
                break;
            default:
                sendText(exchange, "Method Not Allowed");
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<SubTask> subtasks = HttpTaskServer.taskManager.getAllSubTasks();
        System.out.println(subtasks);
        String jsonResponse = HttpTaskServer.gson.toJson(subtasks);
        sendText(exchange, jsonResponse);
    }

    private void handleCreateSubtask(HttpExchange exchange) throws IOException {
        SubTask subtask = HttpTaskServer.gson.fromJson(new InputStreamReader(exchange.getRequestBody()), SubTask.class);
        HttpTaskServer.taskManager.createSubTask(subtask);
        sendText(exchange, "{\"message\":\"Subtask добавлен успешно\"}");
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            HttpTaskServer.taskManager.deleteSubTaskById(id);
            sendText(exchange, "{\"message\":\"Subtask удален успешно\"}");
        } else {
            sendNotFound(exchange);
        }
    }
}