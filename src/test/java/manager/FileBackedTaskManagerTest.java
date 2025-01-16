package manager;

import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest<T extends TaskManager> {
    File file;
    FileBackedTaskManager taskManager;
    private static final String filePath = "src/main/resources/file.csv";


    @BeforeEach
    public void init() {
        file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать файл для тестов", e);
        }

        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void loadFromFileTest() {
        taskManager.save();

        taskManager = FileBackedTaskManager.loadFromFile(file);

        Assertions.assertTrue(taskManager.getAllTasks().isEmpty());
        Assertions.assertTrue(taskManager.getAllEpics().isEmpty());
        Assertions.assertTrue(taskManager.getAllSubTasks().isEmpty());
    }


    @Test
    public void loadFromFileEpicTest() {
        Epic epic = new Epic("name", "descr", Status.NEW, 1);

        taskManager = FileBackedTaskManager.loadFromFile(file);

        taskManager.addEpic(epic);


        assertEquals(epic, taskManager.getEpicsById(1));
        assertEquals(1, taskManager.getAllEpics().size());

    }

    @Test
    public void saveAndLoadTest() {
        Task task = new Task("name", "descr", Status.NEW, 1);
        Epic epic = new Epic("Выучить Английский", "Успеть за пол года");

        taskManager.createTasks(task);
        taskManager.createEpics(epic);

        FileBackedTaskManager.loadFromFile(file);
        assertEquals(List.of(task), taskManager.getAllTasks());
        assertEquals(List.of(epic), taskManager.getAllEpics());
    }


}
