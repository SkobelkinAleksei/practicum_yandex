package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.http.DurationTypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final InMemoryTaskManager taskManager = new InMemoryTaskManager();


    public static void main(String[] args) {

        // Регистрируем адаптер для Duration
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();


        Task task = new Task("Test Task", "This is a test task", Status.NEW, 1, Duration.ofHours(4), LocalDateTime.of(2023, 10, 1, 12, 10));

        // Сериализация
        String taskJson = gson.toJson(task);
        System.out.println("Serialized JSON: " + taskJson);

        // Десериализация
        Task deserializedTask = gson.fromJson(taskJson, Task.class);
        System.out.println("Deserialized Task: " + deserializedTask);
    }

}
