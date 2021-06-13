package com.testorystudio.testorystudio.Daos;

import com.testorystudio.testorystudio.Entities.Event;

public class EventDAO {
    private String name;
    private String body;
    private Long parentId;

    public EventDAO() { }

    public EventDAO(Event event){
        this.name = event.getName();
        this.body = event.getBody();
        this.parentId = event.getParentId();
    }

    public EventDAO(String name, String body, long parentId){
        this.name = name;
        this.body = body;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
