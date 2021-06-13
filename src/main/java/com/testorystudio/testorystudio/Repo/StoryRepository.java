package com.testorystudio.testorystudio.Repo;

import java.util.List;

import com.testorystudio.testorystudio.Entities.Story;

import org.springframework.data.repository.CrudRepository;

public interface StoryRepository extends CrudRepository<Story,Long>{


    default List<Story> getAllStories(){
        return (List<Story>)this.findAll();
    }

    default Story getStoryById(Long id) {
        try {
            return this.findById(id).get();
        }
        catch (Exception e){
            return null;
        }
    }

    default Story addStory(Story story) {
        return this.save(story);
    }

    default Story updateStory(Story newStory) {
        return this.save(newStory);
    }

    default void deleteStory(Story storyToDel) {
        this.delete(storyToDel);
    }

}
