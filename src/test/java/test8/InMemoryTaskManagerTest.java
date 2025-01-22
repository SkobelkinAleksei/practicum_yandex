package test8;


import org.example.InMemoryTaskManager;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return null;
    }

    @Override
    public void setUp() throws IOException {

    }
}
