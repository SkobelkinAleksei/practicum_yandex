package manager;

import org.example.InMemoryHistoryManagerImpl;
import org.example.InMemoryTaskManager;
import org.example.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ManagersTest {

    @Test
    public void getDefaultInMemoryTaskManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(), "Возвращаемый тип не соответсвует.");
    }

    @Test
    public void getDefaultInMemoryHistoryManager() {
        assertInstanceOf(InMemoryHistoryManagerImpl.class, Managers.getDefaultHistory(), "Возвращаемый тип не соответсвует.");
    }
}
