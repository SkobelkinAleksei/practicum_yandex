package org.example;

public enum TaskType {
    TASK("Task"),
    EPIC("Epic"),
    SUBTASK("SubTask");

    private final String typeName;

    TaskType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}

