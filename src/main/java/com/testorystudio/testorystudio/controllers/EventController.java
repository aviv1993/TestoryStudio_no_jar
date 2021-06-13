package com.testorystudio.testorystudio.controllers;

import java.util.List;

import com.testorystudio.testorystudio.Daos.EventDAO;
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
@RequestMapping("/events")
@CrossOrigin(origins = "http://localhost:8081")
public class EventController {

    @Autowired
    private ServerFacade serverFacade;

    @GetMapping()
    public List<EventDAO> getAllEvents(){
        return serverFacade.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventDAO getEventByID(@PathVariable("id") Long id){
        return serverFacade.getEventById(id);
    }

    @PostMapping()
    public Long addEvent(@RequestBody EventDAO event){
        return serverFacade.addEvent(event);
    }

    @PutMapping("/{id}")
    public boolean updateEvent(@PathVariable("id") Long id, @RequestBody EventDAO event){
        return serverFacade.updateEvent(id, event);
    }

    @PutMapping("/rename_{id}")
    public boolean updateEventName(@PathVariable("id") Long id, @RequestBody String newName){
        return serverFacade.updateEventName(id, newName);
    }

    @DeleteMapping("/{id}")
    public boolean deleteEvent(@PathVariable("id") Long id){
        return serverFacade.deleteEvent(id);
    }

}
