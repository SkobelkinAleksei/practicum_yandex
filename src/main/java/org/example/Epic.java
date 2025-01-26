package org.example;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Epic extends Task {


    private List<SubTask> subTaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
    }


    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void addSubTask(SubTask subTask) {
        subTaskList.add(subTask);
        calculateEpicProperties();
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }

    // Метод для расчета свойств эпика
    private void calculateEpicProperties() {
        if (subTaskList.isEmpty() || subTaskList == null) {
            this.startTime = null;
            this.duration = null;
            this.endTime = null;
            return;
        }

        // Расчет самой ранней даты начала
        this.startTime = subTaskList.stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull) // (start -> start != null)
                .min(LocalDateTime::compareTo)
                .orElse(null); // после фильтрации не пройдет null

        // Сумма продолжительности всех подзадач
        this.duration = subTaskList.stream()
                .map(SubTask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);


        // endTime будет последней задачей в списке
        LocalDateTime endTime = subTaskList.stream() // Расчет времени завершения эпика
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);  // после фильтрации не пройдет null
        setEndTime(endTime);
    }

    // Переопределение метода для получения времени завершения
    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskList, epic.subTaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskList);
    }
}
