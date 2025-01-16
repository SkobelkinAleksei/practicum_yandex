package org.example;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    private List<SubTask> subTaskList = new ArrayList<>();


    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
    }

    public Epic() {

    }

    public Epic(String name, String description) {
        super(name, description);
    }


    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void addSubTask(SubTask subTask) {
        subTaskList.add(subTask);
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }


}
