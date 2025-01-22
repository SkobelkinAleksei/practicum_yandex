package manager;

import org.example.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSOutput;
import test8.TaskManagerTest;

import java.time.Duration;
import java.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    File file;
    FileBackedTaskManager taskManager;
    private static final String filePath = "src/main/resources/test_tasks.csv";

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
        Epic epic = new Epic("name", "descr", Status.NEW, 1, Duration.ofHours(2),
                LocalDateTime.of(2023, 10, 1, 12, 10));

        taskManager = FileBackedTaskManager.loadFromFile(file);

        taskManager.addEpic(epic);

        assertEquals(epic, taskManager.getEpicsById(1));
        assertEquals(1, taskManager.getAllEpics().size());

    }

    @Test
    public void saveAndLoadTest() {
        var epic = new Epic("Epic 1", "Description", Status.NEW, 0, Duration.ofHours(2),
                LocalDateTime.of(2023, 10, 1, 12, 10));
        taskManager.createEpics(epic);

        var subTask1 = new SubTask("SubTask 1", "Description", Status.NEW, 1, Duration.ofHours(2),
                LocalDateTime.of(2023, 10, 1, 15, 10), epic.getId());
        taskManager.createSubTask(subTask1);
        taskManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, loadedManager.getAllEpics().size(), "Должен быть загружен один эпик.");
        assertEquals(1, loadedManager.getAllSubTasks().size(), "Должна быть загружена одна подзадача.");
        assertEquals(1, epic.getSubTaskList().size(), "Должна быть загружена одна подзадача.");
    }

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return null;
    }

    @Override
    public void setUp() throws IOException {

    }

    @Test
    public void testFileNotFoundException() {
        File nonExistentFile = new File("non_existent_file.csv");
        assertThrows(IllegalArgumentException.class, () -> {
            FileBackedTaskManager.loadFromFile(nonExistentFile);
        }, "Файл не должен существовать.");
    }

    @Test
    public void testExceptionOnSave() {
        // Проверка на исключение при попытке сохранить в недоступный файл
        File readOnlyFile = new File("/path/to/read-only-file.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(readOnlyFile);
        assertThrows(ManagerSaveException.class, manager::save, "Сохранение должно выбрасывать исключение.");
    }
}
