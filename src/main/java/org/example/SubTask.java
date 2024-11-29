package org.example;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(int id, String name, String description, Status status, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

}
