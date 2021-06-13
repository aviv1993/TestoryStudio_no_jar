package com.testorystudio.testorystudio.Daos;

import com.testorystudio.testorystudio.Entities.EntitiesInterface.StoryType;
import com.testorystudio.testorystudio.Entities.Story;

public class StoryDAO {
    private String name;
    private String body;
    private Long parentId;
    private String type;
    private String translatedBody;

    public StoryDAO() { }

    public StoryDAO(String name, String body,  StoryType type) {
        this.name = name;
        this.body = body;
        this.type = type.name();
    }

    public StoryDAO(Story story){
        this.name = story.getName();
        this.body = story.getBody();
        this.parentId = story.getParentId();
        this.type = story.getType().name();
        this.translatedBody = story.getTranslatedBody();
    }

    public StoryDAO(String name, Long parentId, StoryType type) {
        this.name = name;
        this.parentId = parentId;
        this.type = type.name();
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getType() {
        return type;
    }

    public String getTranslatedBody() {
        return translatedBody;
    }
}
