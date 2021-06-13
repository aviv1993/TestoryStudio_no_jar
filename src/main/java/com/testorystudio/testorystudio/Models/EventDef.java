package com.testorystudio.testorystudio.Models;

import java.util.List;

public class EventDef {
    private String eventName;
    private List<String> eventVarDefinitions;

    public EventDef(){}

    public EventDef(String eventName, List<String> eventVarDefinitions) {
        this.eventName = eventName;
        this.eventVarDefinitions = eventVarDefinitions;
    }

    public String getEventName() {
        return eventName;
    }

    public List<String> getEventVarDefinitions() {
        return eventVarDefinitions;
    }
}
