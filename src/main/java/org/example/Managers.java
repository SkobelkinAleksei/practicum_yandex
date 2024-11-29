package org.example;

public class Managers {

    public static InMemoryHistoryManagerImpl getDefaultHistory() {
        return new InMemoryHistoryManagerImpl();
    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
