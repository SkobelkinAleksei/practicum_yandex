package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final InMemoryTaskManager taskManager = new InMemoryTaskManager();


    public static void main(String[] args) {

        extracted();

        printAllTasks();

        printHistory();
    }

    private static void extracted() {
        Task taskLesson = new Task("Домашняя работа", "Решить задачу 5");
        taskManager.createTasks(taskLesson);
        System.out.println("Это 1-ый : " + taskLesson.getId());

        Task toUpdateTaskLesson = new Task("Учебная работа", "Решить контрольную", Status.IN_PROGRESS, taskLesson.getId());
        taskManager.createTasks(toUpdateTaskLesson);
        System.out.println("Это 2-ый : " + toUpdateTaskLesson.getId());

        Epic epicEnglish = new Epic("Выучить Английский", "Успеть за пол года");
        Epic epicSpanish = new Epic("Выучить Испанский", "Успеть за пол года");
        taskManager.createEpics(epicEnglish);
        taskManager.createEpics(epicSpanish);

        SubTask epicEnglishSubTaskFirst = new SubTask("Пройти 5 тем", "По учебнику от Галины Семеновны", epicEnglish.getId());
        SubTask epicEnglishSubTaskSecond = new SubTask("Изучить вопросы экзамена", "Спросить у Саши", epicSpanish.getId());
        SubTask epicEnglishSubTask3 = new SubTask("Пройти 2 тем", "По учебнику от Семена", epicEnglish.getId());
        taskManager.createSubTask(epicEnglishSubTaskFirst);
        taskManager.createSubTask(epicEnglishSubTaskSecond);
        taskManager.createSubTask(epicEnglishSubTask3);
        System.out.println("САБТАСК: " + epicEnglishSubTaskSecond.getEpicId());

        System.out.println(taskManager.getAllTasks());
    }

    private static void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getAllEpics()) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getAllSubTasks()) {
            System.out.println(subtask);
        }

    }

    private static void printHistory() {
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        taskManager.getEpicsById(3);
        taskManager.getEpicsById(4);
        taskManager.getEpicsById(4);
        System.out.println("История просмотров: ");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

    }
}
