package com.testorystudio.testorystudio.controllers;

import java.util.List;

import com.testorystudio.testorystudio.Daos.StoryDAO;
import com.testorystudio.testorystudio.Server.ServerFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stories")
@CrossOrigin(origins = "http://localhost:8081")
public class StoryController {

    @Autowired
    private ServerFacade serverFacade;

    @GetMapping()
    public List<StoryDAO> getAllStories()
    {
        return serverFacade.getAllStories();
    }

    @GetMapping("/{id}")
    public StoryDAO getStoryByID(@PathVariable("id") Long id){
        return serverFacade.getStoryById(id);
    }

    @PostMapping()
    public Long addStory(@RequestBody StoryDAO story){
        return serverFacade.addStory(story);
    }

    @PutMapping("/{id}")
    public boolean updateStory(@PathVariable("id") Long id, @RequestBody StoryDAO newStory){
        return serverFacade.updateStory(id, newStory);
    }

    @PutMapping("/rename_{id}")
    public boolean updateStoryName(@PathVariable("id") Long id, @RequestBody String newName){
        return serverFacade.updateStoryName(id, newName);
    }

    @DeleteMapping("/{id}")
    public boolean deleteStory(@PathVariable("id") Long id){
        return serverFacade.deleteStory(id);
    }

}
