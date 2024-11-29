package task;

import org.example.SubTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {

    @Test
    public void subTaskShouldBeEqual() {
        SubTask subTask = new SubTask("Пройти 5 тем", "По учебнику от Галины Семеновны", 4);
        SubTask subTask1 = new SubTask("Изучить вопросы экзамена", "Спросить у Саши", 4);
        assertEquals(subTask, subTask1, "Наследники Task должны быть равны, если равен их id");

    }
}
