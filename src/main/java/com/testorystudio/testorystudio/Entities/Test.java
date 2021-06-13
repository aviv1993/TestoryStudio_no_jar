package com.testorystudio.testorystudio.Entities;

import com.testorystudio.testorystudio.Daos.TestDAO;
import com.testorystudio.testorystudio.Daos.TreeNodeDao;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StudioEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "tests")
public class Test extends StudioEntity {

    @ManyToMany
    private List<Folder> testFolders;

    public Test(){}

    public Test(String name, List<Folder> testFoldersIds) {
        super(name);
        this.testFolders = testFoldersIds;
    }

    public Test(TestDAO testDAO){
        super(testDAO.getName());
        this.testFolders = new ArrayList<>();
    }

    public List<Folder> getTestFolders() {
        return testFolders;
    }

    @Override
    public TreeNodeDao toTreeDao() {
        List<TreeNodeDao> childs = testFolders.stream().map(f -> f.toTreeDao()).collect(Collectors.toList());
        return new TreeNodeDao(getId(), getName(), EntityConsts.TEST, childs);
    }

    public TestDAO toTestDao(){
        return new TestDAO(this);
    }
}
