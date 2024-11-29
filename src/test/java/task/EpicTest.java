package task;

import org.example.Epic;
import org.example.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    @Test
    public void epicsShouldBeEqual() {
        Epic epic1 = new Epic("Выучить Английский", "Успеть за пол года", Status.NEW, 4);
        Epic epic2 = new Epic("Выучить Английский", "Успеть за пол года", Status.DONE, 4);
        assertEquals(epic1, epic2, "Наследники класса Task должны быть равны, если равен их id");
    }
}
