package com.testorystudio.testorystudio.Server;


import java.util.List;

import com.testorystudio.testorystudio.Daos.*;
import com.testorystudio.testorystudio.Entities.Folder;
import com.testorystudio.testorystudio.Models.EventDef;


public interface ServerFacade {

    List<FolderDAO> getAllFolders();
    List<Folder> getAllFoldersObjects();
    FolderDAO getFolderByID(Long id);
    boolean updateFolder(Long id, FolderDAO folderDAO);
    List<StoryDAO> getAllStories();
    StoryDAO getStoryById(Long id);
    Long addStory(StoryDAO story);
    boolean updateStory(Long id, StoryDAO newStory);
    boolean updateStoryName(Long id, String name);
    boolean updateEventName(Long id, String name);
    boolean updateFolderName(Long id, String name);
    List<EventDAO> getAllEvents();
    EventDAO getEventById(Long id);
    Long addEvent(EventDAO event);
    boolean updateEvent(Long id, EventDAO newEvent);
    List<TreeNodeDao> getTree();
    Long addFolder(FolderDAO folderDAO);
    boolean deleteStory(Long id);
    boolean deleteFolder(Long id);
    boolean deleteEvent(Long id);
    boolean addTest(TestDAO test);
    TestDAO getTest(Long id);
    List<TreeNodeDao> getTestsTree();
    boolean deleteTest(Long id);
    List<EventDef> getFolderEventsDefinitions(Long id);
}
