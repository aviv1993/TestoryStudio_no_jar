package com.testorystudio.testorystudio.Repo;

import com.testorystudio.testorystudio.Entities.Folder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FolderRepository extends CrudRepository<Folder, Long> {

    default List<Folder> getAllFolders(){
        return (List<Folder>)this.findAll();
    }

    default Folder getFolderById(Long id){
        try {
            return this.findById(id).get();
        }
        catch (Exception e){
            return null;
        }
    }

    default Folder addFolder(Folder folder) {
        return this.save(folder);
    }

    default Folder updateFolder(Folder newFolder) {
        return this.save(newFolder);
    }

    default void deleteFolder(Folder folder){
        this.delete(folder);
    }

}
