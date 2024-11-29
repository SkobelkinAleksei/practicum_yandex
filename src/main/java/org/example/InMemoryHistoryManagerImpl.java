package org.example;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManagerImpl implements HistoryManager {

    private final List<Task> historyList = new ArrayList<>();
    private static final int MAX_HISTORY_COUNT = 10;

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (historyList.size() == MAX_HISTORY_COUNT) {
            historyList.removeFirst();
        }
        historyList.add(task);


    }


}
