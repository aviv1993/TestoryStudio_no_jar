package com.testorystudio.testorystudio.Entities.EntitiesInterface;

import com.testorystudio.testorystudio.Daos.TreeNodeDao;

import javax.persistence.*;

@MappedSuperclass
public abstract class StudioEntity {

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long parentId;

    public StudioEntity(String name){
        this.name = name;
    }

    public StudioEntity(){ }

    public Long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract TreeNodeDao toTreeDao();

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
