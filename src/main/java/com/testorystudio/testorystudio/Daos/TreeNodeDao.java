package com.testorystudio.testorystudio.Daos;

import java.util.List;

public class TreeNodeDao {
    public Long id;
    public String name;
    public String file;
    public List<TreeNodeDao> children;

    public TreeNodeDao(Long id, String name, String file, List<TreeNodeDao> children) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.children = children;
    }

    public List<TreeNodeDao> getChildren() {
        return children;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }

}
