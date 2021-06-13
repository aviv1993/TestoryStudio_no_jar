package com.testorystudio.testorystudio.Server;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.testorystudio.testorystudio.Daos.*;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StoryType;
import com.testorystudio.testorystudio.Entities.Event;
import com.testorystudio.testorystudio.Entities.Folder;
import com.testorystudio.testorystudio.Entities.Story;
import com.testorystudio.testorystudio.Entities.Test;
import com.testorystudio.testorystudio.Models.EventDef;
import com.testorystudio.testorystudio.Repo.EventRepository;
import com.testorystudio.testorystudio.Repo.FolderRepository;
import com.testorystudio.testorystudio.Repo.StoryRepository;

import com.testorystudio.testorystudio.Repo.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Server implements ServerFacade {

    private final StoryRepository storyRepository;
    private final EventRepository eventRepository;
    private final FolderRepository folderRepository;
    private final TestRepository testRepository;

    @Autowired
    public Server(StoryRepository storyRepository,
                  EventRepository eventRepository,
                  FolderRepository folderRepository,
                  TestRepository testRepository) {
        this.storyRepository = storyRepository;
        this.eventRepository = eventRepository;
        this.folderRepository= folderRepository;
        this.testRepository = testRepository;
    }

    @Override
    public List<StoryDAO> getAllStories() {
        return storyRepository.getAllStories().stream().map(s -> s.toStoryDao()).collect(Collectors.toList());
    }

    @Override
    public StoryDAO getStoryById(Long id) {
        return storyRepository.getStoryById(id).toStoryDao();
    }

    @Override
    public Long addStory(StoryDAO storyDAO) {
        Story story = new Story(storyDAO);
        Folder folder = folderRepository.getFolderById(storyDAO.getParentId());
        if(folder!=null){
            storyRepository.addStory(story);
            folder.addStory(story);
            folderRepository.updateFolder(folder);
            System.out.println(story.getId());
            return story.getId();
        }
        return Long.valueOf(-1);
    }

    @Override
    public boolean updateStory(Long id, StoryDAO newStory) {
        Story story=storyRepository.getStoryById(id);
        if (story!=null){
            story.setBody(newStory.getBody());
            story.setName(newStory.getName());
            story.setType(StoryType.valueOf(newStory.getType()));
            story.setTranslatedBody(newStory.getTranslatedBody());
            storyRepository.updateStory(story);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStoryName(Long id, String name) {
        Story story=storyRepository.getStoryById(id);
        if (story!=null){
            story.setName(name);
            storyRepository.updateStory(story);
            return true;
        }
        return false;
    }

    @Override
    public List<EventDAO> getAllEvents() {
        return eventRepository.getAllEvents().stream().map(e -> e.toEventDao()).collect(Collectors.toList());
    }

    @Override
    public EventDAO getEventById(Long id) {
        return eventRepository.getEventById(id).toEventDao();
    }

    @Override
    public Long addEvent(EventDAO event) {
        Event newEvent = new Event(event);
        Folder folder=folderRepository.getFolderById(event.getParentId());
        if(folder!=null){
            eventRepository.addEvent(newEvent);
            folder.addEvent(newEvent);
            folderRepository.updateFolder(folder);
            return newEvent.getId();
        }
        return Long.valueOf(-1);
    }

    @Override
    public boolean updateEvent(Long id, EventDAO event) {
        Event eventToUpdate=eventRepository.getEventById(id);
        if (eventToUpdate!=null){
            eventToUpdate.setBody(event.getBody());
            eventToUpdate.setName(event.getName());
            eventRepository.updateEvent(eventToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEventName(Long id, String name) {
        Event eventToUpdate=eventRepository.getEventById(id);
        if (eventToUpdate!=null){
            eventToUpdate.setName(name);
            eventRepository.updateEvent(eventToUpdate);
            return true;
        }
        return false;
    }

    @Override
    public List<TreeNodeDao> getTree() {
        List<TreeNodeDao> roots = new ArrayList<>();
        for (Folder p : folderRepository.getAllFolders()){
            if(p.getParentId()==null)
                roots.add(p.toTreeDao());
        }
        return roots;
    }


    @Override
    public List<FolderDAO> getAllFolders() {
        return folderRepository.getAllFolders().stream().map(p -> p.toFolderDAO()).collect(Collectors.toList());
    }
    @Override
    public List<Folder> getAllFoldersObjects() {
        return folderRepository.getAllFolders();
    }

    @Override
    public FolderDAO getFolderByID(Long id) {
        return folderRepository.getFolderById(id).toFolderDAO();
    }


    @Override
    public boolean updateFolder(Long id, FolderDAO folderDAO) {
        Folder folder=folderRepository.getFolderById(id);
        if (folder!=null){
            folder.updateFolder(folderDAO);
            folderRepository.updateFolder(folder);

            return true;
        }
        return false;
    }

    @Override
    public boolean updateFolderName(Long id, String name) {
        Folder folder=folderRepository.getFolderById(id);
        if (folder!=null){
            folder.setName(name);;
            folderRepository.updateFolder(folder);

            return true;
        }
        return false;
    }

    @Override
    public Long addFolder(FolderDAO folderDAO) {
        Folder folder= new Folder(folderDAO);
        Long parentId = folderDAO.getParentId();
        if(parentId!=null){
            Folder parentFolder=folderRepository.getFolderById(parentId);
            parentFolder.addChildFolder(folder);
            folderRepository.updateFolder(parentFolder);
            folderRepository.addFolder(folder);
            return folder.getId();
        }
        folderRepository.addFolder(folder);
        Folder storiesFolder = new Folder("Stories");
        storiesFolder.setParentId(folder.getId());
        folder.addChildFolder(storiesFolder);
        Folder eventsFolder = new Folder("Events");
        eventsFolder.setParentId(folder.getId());
        folder.addChildFolder(eventsFolder);
        folderRepository.updateFolder(folder);
        return folder.getId();
    }

    @Override
    public boolean deleteStory(Long id) {
        Story story=storyRepository.getStoryById(id);
        if (story!=null){
            storyRepository.deleteStory(story);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFolder(Long id) {
        Folder folder=folderRepository.getFolderById(id);
        if (folder!=null){
            folderRepository.deleteFolder(folder);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteEvent(Long id) {
        Event event=eventRepository.getEventById(id);
        if (event!=null){
            eventRepository.deleteEvent(event);
            return true;
        }
        return false;
    }

    @Override
    public boolean addTest(TestDAO test) {
        Test newTest = new Test(test);
        List<Folder> foldersList = new ArrayList<>();
        for (String id : test.getTestFoldersIds()){
            Long longId = Long.parseLong(id);
            foldersList.add(folderRepository.getFolderById(longId));
        }
        newTest.getTestFolders().addAll(foldersList);
        testRepository.addTest(newTest);
        return true;
    }

    public TestDAO getTest(Long id) {
        return testRepository.getTestById(id).toTestDao();
    }


    public List<TreeNodeDao> getTestsTree(){
        List<TreeNodeDao> testDaos = new ArrayList<>();
        for(Test test: testRepository.getAllTests()){
            testDaos.add(test.toTreeDao());
        }
        return testDaos;
    }

    @Override
    public boolean deleteTest(Long id) {
        Test test = testRepository.getTestById(id);
        if (test != null)
        {
            testRepository.delete(test);
            return true;
        }
        return false;
    }

    @Override
    public List<EventDef> getFolderEventsDefinitions(Long id) {
        Folder folder = folderRepository.getFolderById(id);
        if (folder.getParentId() != null){
            folder = folderRepository.getFolderById(folder.getParentId());
        }
        return folder.collectEventsDef();
    }
}