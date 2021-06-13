package com.testorystudio.testorystudio.Entities;

import com.testorystudio.testorystudio.Daos.FolderDAO;
import com.testorystudio.testorystudio.Daos.TreeNodeDao;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StudioEntity;
import com.testorystudio.testorystudio.Models.EventDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "folders")
public class Folder extends StudioEntity {

    //@ElementCollection
    //@CollectionTable(name="events_in_project", joinColumns=@JoinColumn(name="projects_id"))
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="parent_folder_id")
    private List<Event> events;

    //@ElementCollection
    //@CollectionTable(name="stories_in_project", joinColumns=@JoinColumn(name="projects_id"))
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="parent_folder_id")
    private List<Story> stories;

    //@ElementCollection
    //@CollectionTable(name="folders_in_project", joinColumns=@JoinColumn(name="projects_id"))
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="parent_folder_id")
    private List<Folder> folders;

    public Folder(String name) {
        super(name);
        this.stories = new ArrayList<>();
        this.events = new ArrayList<>();
        this.folders = new ArrayList<>();
    }

    public Folder(FolderDAO folderDAO){
        super(folderDAO.getName());
        this.stories = new ArrayList<>();
        this.events = new ArrayList<>();
        this.folders = new ArrayList<>();
    }

    public Folder(){}

    public void addStory(Story story){
        story.setParentId(getId());
        stories.add(story);
    }

    public void addEvent(Event event){
        event.setParentId(getId());
        events.add(event);
    }

    public void addChildFolder(Folder folder){
        folder.setParentId(this.getId());
        folders.add(folder);
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Story> getStories() {
        return stories;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    @Override
    public TreeNodeDao toTreeDao() {
        List<TreeNodeDao> children = stories.stream().map(s -> s.toTreeDao()).collect(Collectors.toList());
        children.addAll(events.stream().map(e -> e.toTreeDao()).collect(Collectors.toList()));
        if(folders==null)
            folders=new ArrayList<>();
        children.addAll(folders.stream().map(f -> f.toTreeDao()).collect(Collectors.toList()));
        return new TreeNodeDao(getId(), getName(), EntityConsts.FOLDER, children);
    }

    public FolderDAO toFolderDAO(){
        return new FolderDAO(this);
    }

    public void updateFolder(FolderDAO FolderDAO){
        this.setName(FolderDAO.getName());
    }

    public List<EventDef> collectEventsDef(){
        List<Event> events = getEvents();
        List<EventDef> res = new ArrayList<>();
        for (Event event: events){
            EventDef eDef = event.getEventDefinition();
            if (eDef != null) {
                res.add(eDef);
            }
        }
        for (Folder childFolder : this.getFolders()){
            res.addAll(childFolder.collectEventsDef());
        }
        return res;
    }
}
