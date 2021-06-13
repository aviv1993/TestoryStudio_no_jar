package com.testorystudio.testorystudio.Daos;

import com.testorystudio.testorystudio.Entities.Folder;

public class FolderDAO {
    private String name;
    private Long parentId;

    public FolderDAO(Folder folder) {
        this.name = folder.getName();
        this.parentId = folder.getParentId();
    }

    public FolderDAO() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
