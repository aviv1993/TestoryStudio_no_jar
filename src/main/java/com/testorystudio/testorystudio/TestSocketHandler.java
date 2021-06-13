package com.testorystudio.testorystudio;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TestSocketHandler {
    private Map<String,String> idsToMessages = new HashMap<>();

    public String addNewClient(String name){
        String id = UUID.randomUUID().toString();
        idsToMessages.put(id, "Hello " + name);
        return id;
    }

    public Map<String,String> getMessages(){
        Map<String,String> messages = new HashMap<>(idsToMessages);
        for(String key: idsToMessages.keySet()){
            idsToMessages.put(key,null);
        }
        return messages;
    }

    public void randMessageFor(String socketId){
        if (idsToMessages.containsKey(socketId)){
            double random = Math.random()*10;
            idsToMessages.replace(socketId,"Random number: " + random);
        }
    }

    public void removeClient(String socketId){
        idsToMessages.remove(socketId);
    }




}
