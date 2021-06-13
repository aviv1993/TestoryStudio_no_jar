package com.testorystudio.testorystudio.controllers;

import com.testorystudio.testorystudio.Daos.TreeNodeDao;
import com.testorystudio.testorystudio.Server.ServerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tree")
@CrossOrigin(origins = "http://localhost:8081")
public class TreeController {
    @Autowired
    public ServerFacade serverFacade;

    @GetMapping
    public List<TreeNodeDao> getTree(){
        return serverFacade.getTree();
    }
/*
    @PostMapping
    public boolean addFolder(@RequestBody FolderDAO folderDAO)
    {
        return serverFacade.addFolder(folderDAO);
    }
    */
}
