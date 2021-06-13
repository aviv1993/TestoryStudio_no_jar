package com.testorystudio.testorystudio.Entities;

import com.testorystudio.testorystudio.Daos.EventDAO;
import com.testorystudio.testorystudio.Daos.TreeNodeDao;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StudioEntity;
import com.testorystudio.testorystudio.Models.EventDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Entity
@Table(name = "events")
public class Event extends StudioEntity {
    @Column(columnDefinition = "TEXT")
    private String body;
    private static final String eventDefRegex =
            "define_event(?:\\s*)\\([\\\"\\'](.*)[\\\"\\'](?:\\s*),(?:\\s*)function(?:\\s*)\\((.*)\\)";
    private static final String eventVarsRegex = "[^a-zA-Z0-9_]%s\\.(.*?)[^a-zA-Z0-9_]";
    public Event(String name, String body) {
        super(name);
        this.body = body;
    }

    public Event(EventDAO eventDAO){
        super(eventDAO.getName());
        this.body = eventDAO.getBody();
    }

    public Event(){}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public TreeNodeDao toTreeDao() {
        return new TreeNodeDao(getId(),getName(),EntityConsts.EVENT,new ArrayList<>());
    }

    public EventDAO toEventDao(){
        return new EventDAO(this);
    }


    private List<String> parseEventVars(String eventVarName){
        final Pattern pattern = Pattern.compile(String.format(eventVarsRegex, eventVarName), Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(getBody());
        Set<String> res = new HashSet<>();
        while (matcher.find()){
            for (int i = 1; i <= matcher.groupCount(); i++){
                res.add(matcher.group(i));
            }
        }
        res.remove("s");
        return new ArrayList<>(res);
    }

    public EventDef getEventDefinition(){
        if (getBody() == null)
            return null;
        final Pattern pattern = Pattern.compile(eventDefRegex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(getBody());
        String eventBodyName = null;
        String eventVariableName = null;
        if (matcher.find()) {
            eventBodyName = matcher.group(1);
            eventVariableName = matcher.group(2);
        }
        return new EventDef(eventBodyName, parseEventVars(eventVariableName));
    }

}
