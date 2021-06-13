package com.testorystudio.testorystudio.Repo;

import com.testorystudio.testorystudio.Entities.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long>{

    default List<Event> getAllEvents() {
        return (List<Event>)this.findAll();
    }

    default Event getEventById(Long id) {
        try {
            return this.findById(id).get();
        }
        catch (Exception e){
            return null;
        }
    }

    default Event addEvent(Event event) {
        return this.save(event);
    }

    default Event updateEvent(Event newEvent) {
        return this.save(newEvent);
    }

    default void deleteEvent(Event event){
        this.delete(event);
    }

}
