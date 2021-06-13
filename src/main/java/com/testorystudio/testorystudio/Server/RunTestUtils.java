package com.testorystudio.testorystudio.Server;

import com.testorystudio.testorystudio.Daos.EventDAO;
import com.testorystudio.testorystudio.Daos.StoryDAO;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StoryType;
import com.testorystudio.testorystudio.Entities.Folder;
import com.testorystudio.testorystudio.Entities.Severity;
import com.testorystudio.testorystudio.Entities.WebsocketMessage;
import com.testorystudio.testorystudio.controllers.StreamGobbler;
import com.testorystudio.testorystudio.controllers.WebSocketController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class RunTestUtils {
    public final ServerFacade serverFacade;
    public final WebSocketController webSocketController;

    public RunTestUtils(ServerFacade serverFacade, WebSocketController webSocketController) {
        this.serverFacade = serverFacade;
        this.webSocketController = webSocketController;
    }

    public void writeFilesToFolder(Long folderId, StringBuilder eventDefBuilder, StringBuilder testsBuilder) {
        FileOutputStream writer;
        try {
            String folderPath = "folder" + folderId;
            File dir = new File(folderPath);
            if (!dir.exists()) {
                // create it
                dir.mkdir();
            }
            String testFilePath = "folder" + folderId + "\\tests.js";
            String eventFilePath = "folder" + folderId + "\\eventDefs.js";
            writer = new FileOutputStream(modifyStringByOs(testFilePath), false);
            writer.write(testsBuilder.toString().getBytes(StandardCharsets.UTF_8));
            writer.close();
            writer = new FileOutputStream(modifyStringByOs(eventFilePath), false);
            writer.write(eventDefBuilder.toString().getBytes(StandardCharsets.UTF_8));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringBuilder getTestsStringBuilder(Set<Long> folders) {
        StringBuilder testsBuilder = new StringBuilder();

        testsBuilder.append("testSuite('Test test suite')");
        testsBuilder.append('\n');
        for (StoryDAO story : serverFacade.getAllStories()) {
            if (story.getParentId() != null && folders.contains(story.getParentId())) {
                boolean isBlocky = StoryType.valueOf(story.getType()) == StoryType.BLOCKLY;
                testsBuilder.append(isBlocky ? story.getTranslatedBody() : story.getBody());
                testsBuilder.append('\n');
            }
        }
        return testsBuilder;
    }

    public StringBuilder getEventDefStringBuilder(Set<Long> folders) {
        StringBuilder eventDefBuilder = new StringBuilder();

        for (EventDAO event : serverFacade.getAllEvents()) {
            if (event.getParentId() != null && folders.contains(event.getParentId())) {
                eventDefBuilder.append(event.getBody());
                eventDefBuilder.append('\n');
            }
        }
        return eventDefBuilder;
    }

    public Set<Long> getSubFolders(Long folderId) {
        List<Folder> allFolders = serverFacade.getAllFoldersObjects();
        Set<Long> initFolders = new HashSet<Long>();
        initFolders.add(folderId);
        return getAllFolders(initFolders, allFolders);
    }

    public Set<Long> getAllFolders(Set<Long> folders, List<Folder> allFolders) {
        int setLen = folders.size();
        List<Folder> temp = new LinkedList<>();
        for (Folder folder : allFolders) {
            if (folders.contains(folder.getParentId())) {
                folders.add(folder.getId());
                temp.add(folder);
            }
        }
        allFolders.removeAll(temp);
        if (setLen != folders.size()) {
            return getAllFolders(folders, allFolders);
        }
        return folders;
    }

    public Process startSelenium() throws IOException {
        String command = modifyStringByOs("cmd.exe /c java -jar TestoryEngine\\selenium-server-standalone-3.141.59.jar");
        return Runtime.getRuntime().exec(command);
    }

    public String modifyStringByOs(String s) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            // the commands are already windows syntax
        } else if (osName.contains("mac")) {
            // we are on Mac, alter the command
            s = s.replace("cmd.exe /c", "bash");
            s = s.replace('\\', '/');
            s = s.replace("testory.bat", "testory-mac.sh");
            if (s.contains("selenium")) {
                s = s.replace("bash", "");
            }
        } else if (osName.contains("nux")) {
            // we are on unix
            s = s.replace("cmd.exe /c", "bash");
            s = s.replace('\\', '/');
            s = s.replace("testory.bat", "testory.sh");
        }

        return s;
    }

    public void exec(String command, String socketId) throws IOException, InterruptedException {
        command = modifyStringByOs(command);
        Process process = Runtime.getRuntime().exec(command);


        Consumer<String> reportBack = value -> {
            System.out.println(value);
            WebsocketMessage websocketMessage = new WebsocketMessage(value, new Timestamp(System.currentTimeMillis()), Severity.Info);
            webSocketController.sendUpdate(socketId, websocketMessage);
        };

        Consumer<String> reportBackError = value -> {
            System.out.println(value);
            WebsocketMessage websocketMessage = new WebsocketMessage(value, new Timestamp(System.currentTimeMillis()), Severity.Error);
            webSocketController.sendUpdate(socketId, websocketMessage);
        };

        System.out.println("process is running on id " + process.pid());
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), reportBack);
        StreamGobbler errorsStreamGobbler = new StreamGobbler(process.getErrorStream(), reportBackError);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        Executors.newSingleThreadExecutor().submit(errorsStreamGobbler);

        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}