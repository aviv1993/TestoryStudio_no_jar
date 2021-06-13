package com.testorystudio.testorystudio.controllers;

import com.testorystudio.testorystudio.Daos.TestDAO;
import com.testorystudio.testorystudio.Daos.TreeNodeDao;
import com.testorystudio.testorystudio.Server.ServerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@CrossOrigin(origins = "http://localhost:8081")
public class TestController {
    @Autowired
    private ServerFacade serverFacade;

    @PostMapping()
    public boolean addTest(@RequestBody TestDAO test){
        return serverFacade.addTest(test);
    }

    @GetMapping("/{id}")
    public TestDAO getTestByID(@PathVariable("id") Long id){
        return serverFacade.getTest(id);
    }

    @GetMapping()
    public List<TreeNodeDao> getTestsTree(){
        return serverFacade.getTestsTree();
    }

    @DeleteMapping("/{id}")
    public boolean deleteTest(@PathVariable("id") Long id){
        return serverFacade.deleteTest(id);
    }
}
