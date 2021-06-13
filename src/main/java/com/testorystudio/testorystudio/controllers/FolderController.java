package com.testorystudio.testorystudio.controllers;

import com.testorystudio.testorystudio.Daos.FolderDAO;
import com.testorystudio.testorystudio.Models.EventDef;
import com.testorystudio.testorystudio.Server.ServerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
@CrossOrigin(origins = "http://localhost:8081")
public class FolderController {

    @Autowired
    private ServerFacade serverFacade;

    @GetMapping()
    public List<FolderDAO> getAllFolders()
    {
        return serverFacade.getAllFolders();
    }

    @GetMapping("/{id}")
    public FolderDAO getFolderByID(@PathVariable("id") Long id){
        return serverFacade.getFolderByID(id);
    }

    @PostMapping()
    public Long addFolder(@RequestBody FolderDAO folderDAO){
        return serverFacade.addFolder(folderDAO);
    }

    @PutMapping("/{id}")
    public boolean updateFolder(@PathVariable("id") Long id, @RequestBody FolderDAO folderDAO) {
        return serverFacade.updateFolder(id, folderDAO);
    }

    @PutMapping("/rename_{id}")
    public boolean updateFolderName(@PathVariable("id") Long id, @RequestBody String newName){
        return serverFacade.updateFolderName(id, newName);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFolder(@PathVariable("id") Long id){
        return serverFacade.deleteFolder(id);
    }


    @GetMapping("/defs/{id}")
    public List<EventDef> getFolderEventsDefinitions(@PathVariable("id") Long id) {
        return serverFacade.getFolderEventsDefinitions(id);
    }
}
