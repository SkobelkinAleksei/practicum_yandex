package org.example.http;

import com.sun.net.httpserver.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.Managers;
import org.example.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT = 8080;
    public static TaskManager taskManager;
    public static Gson gson;
    private HttpServer server;

    public HttpTaskServer(TaskManager manager) {
        taskManager = manager;
        gson = new GsonBuilder().registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            System.out.println("Сервер был запущен на порту " + PORT);

            server.createContext("/tasks", new TaskHandler());
            server.createContext("/subtasks", new SubtaskHandler());
            server.createContext("/epics", new EpicHandler());
            server.createContext("/history", new HistoryHandler());
            server.createContext("/prioriti", new PrioritiHandler());

            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Сервер был остановлен.");
        }
    }
}
