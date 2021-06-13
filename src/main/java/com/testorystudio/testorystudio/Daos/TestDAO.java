package com.testorystudio.testorystudio.Daos;

import com.testorystudio.testorystudio.Entities.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TestDAO {
    public String name;
    public List<String> testFoldersIds;
    private Long parentId;

    public TestDAO() { }

    public TestDAO(Test test){
        this.name = test.getName();
        this.testFoldersIds = test.getTestFolders().stream().
                map((t -> t.getId().toString())).collect(Collectors.toList());
        this.parentId = test.getParentId();
    }

    public TestDAO (String name, List<String> testFoldersIds,Long parentId) {
        this.name = name;
        this.testFoldersIds = testFoldersIds;
        this.parentId = parentId;

    }
    public String getName() {
        return name;
    }

    public List<String> getTestFoldersIds() {
        return testFoldersIds;
    }

    public Long getParentId() {
        return parentId;
    }
}
