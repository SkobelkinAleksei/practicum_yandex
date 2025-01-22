package org.example;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status;
    private int id;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;

    }

    public Task(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task() {

    }

    // Если дата не указана, то выброс исключения
    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        } else {
            throw new ManagerSaveException("Вы не установили дату начала и время на выполнение задачи или продолжительность");
        }
    }

    // Метод для проверки пересечений --------------------------------------------------------------
    public boolean overlapsWith(Task other) {
        if (this.startTime != null && other.startTime != null) {
            LocalDateTime firstTask = this.getEndTime();
            LocalDateTime secondTask = other.startTime;
            return firstTask.isAfter(secondTask);
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }
}
