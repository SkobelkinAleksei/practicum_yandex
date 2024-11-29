package task;

import org.example.Status;
import org.example.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    public void taskShouldBeEqual() {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.DONE, 4);
        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, 4);
        assertEquals(task1, task2, "Такски должны быть равны, если равен их id");

    }
}
