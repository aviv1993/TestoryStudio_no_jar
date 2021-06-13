package com.testorystudio.testorystudio.controllers;

import com.testorystudio.testorystudio.Server.RunTestUtils;
import com.testorystudio.testorystudio.Server.ServerFacade;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/commands")
public class commandsController {
    private final RunTestUtils runTestUtils;

    public commandsController(WebSocketController webSocketController, ServerFacade serverFacade){
        this.runTestUtils =  new RunTestUtils(serverFacade, webSocketController);
    }

    @PostMapping("runTest")
    @ResponseStatus(value = HttpStatus.OK)
    public void runTest(@RequestParam Long folderId, @RequestParam String socketId) throws IOException, InterruptedException {
        Set<Long> folders = runTestUtils.getSubFolders(folderId);
        StringBuilder eventDefBuilder = runTestUtils.getEventDefStringBuilder(folders);
        StringBuilder testsBuilder = runTestUtils.getTestsStringBuilder(folders);

        runTestUtils.writeFilesToFolder(folderId, eventDefBuilder, testsBuilder);

        Process seleniumProcess = runTestUtils.startSelenium();
        runTestUtils.exec("cmd.exe /c TestoryEngine\\testory.bat folder" + folderId, socketId);
        seleniumProcess.destroyForcibly(); // kill the process
    }

}
