package com.testorystudio.testorystudio.Server;

import com.testorystudio.testorystudio.Entities.Folder;
import com.testorystudio.testorystudio.controllers.WebSocketController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class RunTestUtilsTest {
    private RunTestUtils runTestUtils;

    @BeforeEach
    void setUp() {
        WebSocketController webSocketControllerMock = mock(WebSocketController.class);
        ServerFacade serverFacade = mock(Server.class);
        runTestUtils = new RunTestUtils(serverFacade, webSocketControllerMock);
    }

    @Test
    void modifyStringByOs_ShouldModifyCommandBasedOnOs() {
        String osName = System.getProperty("os.name").toLowerCase();
        String testString = "cmd.exe /c \\something";
        String result = runTestUtils.modifyStringByOs(testString);
        if (osName.startsWith("windows")) {
            assertEquals(testString, result);
        } else if (osName.contains("mac")) {
            assertEquals(result, "bash /something");
        }
    }

    @Test
    void getAllFoldersTest() {
        Folder folder1 = new Folder("folder1");
        folder1.setId(1L);
        folder1.setParentId(null);
        Folder folder2 = new Folder("folder2");
        folder2.setId(2L);
        folder2.setParentId(null);
        Folder folder3 = new Folder("folder3");
        folder3.setId(3L);
        folder3.setParentId(1L);
        Set<Long> initFolders = new HashSet<Long>();
        initFolders.add(folder1.getId());
        List<Folder> folders = new LinkedList<>();
        folders.add(folder1);
        folders.add(folder2);
        folders.add(folder3);
        Set<Long> result = runTestUtils.getAllFolders(initFolders, folders);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(1L));
        assertTrue(result.contains(3L));
    }
}