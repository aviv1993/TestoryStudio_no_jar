package com.testorystudio.testorystudio.Repo;

import com.testorystudio.testorystudio.Entities.Event;
import com.testorystudio.testorystudio.Entities.Test;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestRepository extends CrudRepository<Test, Long> {

    default Test addTest(Test test) {
        return this.save(test);
    }

    default List<Test> getAllTests() {
        return (List<Test>)this.findAll();
    }

    default void deleteTest(Test testToDelete){
        this.delete(testToDelete);
    }

    default Test getTestById(Long id) {
        try {
            return this.findById(id).get();
        }
        catch (Exception e){
            return null;
        }
    }

}
