package com.testorystudio.testorystudio.Entities;
//
import com.testorystudio.testorystudio.Daos.StoryDAO;
import com.testorystudio.testorystudio.Daos.TreeNodeDao;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StoryType;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StudioEntity;

import javax.persistence.*;
import java.util.ArrayList;


@Entity
@Table(name = "stories")
public class Story extends StudioEntity {
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column(columnDefinition = "TEXT")
    private String translatedBody;
    @Enumerated(EnumType.STRING)
    public StoryType type;

    public Story(String name, String body, StoryType type, String translatedBody) {
        super(name);
        this.body = body;
        this.type = type;
        this.translatedBody = translatedBody;
    }

    public Story(StoryDAO storyDAO){
        super(storyDAO.getName());
        this.body = storyDAO.getBody();
        this.type = StoryType.valueOf(storyDAO.getType());
        this.translatedBody = storyDAO.getTranslatedBody();
    }


    public Story() {}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public TreeNodeDao toTreeDao() {
        return new TreeNodeDao(getId(),getName(),EntityConsts.STORY,new ArrayList<>());
    }

    public StoryDAO toStoryDao(){
        return new StoryDAO(this);
    }

    public String getTranslatedBody() {
        return translatedBody;
    }

    public void setTranslatedBody(String translatedBody) {
        this.translatedBody = translatedBody;
    }

    public StoryType getType() {
        return type;
    }

    public void setType(StoryType type) {
        this.type = type;
    }

}