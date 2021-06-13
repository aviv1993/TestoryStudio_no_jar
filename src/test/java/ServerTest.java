import com.testorystudio.testorystudio.Daos.*;
import com.testorystudio.testorystudio.Entities.EntitiesInterface.StoryType;
import com.testorystudio.testorystudio.Entities.Event;
import com.testorystudio.testorystudio.Entities.Folder;
import com.testorystudio.testorystudio.Entities.Story;
import com.testorystudio.testorystudio.Repo.EventRepository;
import com.testorystudio.testorystudio.Repo.FolderRepository;
import com.testorystudio.testorystudio.Repo.StoryRepository;
import com.testorystudio.testorystudio.Repo.TestRepository;
import com.testorystudio.testorystudio.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(MockitoExtension.class)
public class ServerTest {

    Server server;

    @Mock
    EventRepository eventRepository;

    @Mock
    StoryRepository storyRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    TestRepository testRepository;

    @BeforeEach
    void init (){
        server = new Server(storyRepository, eventRepository, folderRepository, testRepository);
    }

    @Test
    void addEventShouldAdd(){
        /**
         * Asserting for null because usually the db will auto generate the event id.
         */
        long folderId = 1;
        EventDAO eventDAO = new EventDAO("name", "body", folderId);
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(new Folder("folder"));
        assertNull(server.addEvent(eventDAO));
    }

    @Test
    void addEventShouldFail(){
        long folderId = -1;
        EventDAO eventDAO = new EventDAO("name", "body", folderId);
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(null);
        assertEquals(-1, server.addEvent(eventDAO));
    }

    @Test
    void updateEventShouldUpdate()
    {
        long eventId = 1;
        String newName = "new name";
        String newBody = "new body";
        Event returnedEvent = new Event("name", "body");
        EventDAO updatedEvent = new EventDAO(newName, newBody, 0);
        Mockito.when(eventRepository.getEventById(eventId)).thenReturn(returnedEvent);
        assertTrue(server.updateEvent(eventId, updatedEvent));
        assertEquals(newBody, returnedEvent.getBody());
        assertEquals(newName, returnedEvent.getName());
    }

    @Test
    void updateEventNameShouldUpdate()
    {
        long eventId = 1;
        String newName = "new name";
        Event returnedEvent = new Event("name", "body");
        EventDAO updatedEvent = new EventDAO(newName, "body", 0);
        Mockito.when(eventRepository.getEventById(eventId)).thenReturn(returnedEvent);
        assertTrue(server.updateEvent(eventId, updatedEvent));
        assertEquals(newName, returnedEvent.getName());
    }

    @Test
    void DeleteEvent_ShouldPass() {
        Long eventId = 100L;
        Event returnedEvent = new Event("name", "body");
        Mockito.when(eventRepository.getEventById(eventId)).thenReturn(returnedEvent);
        final boolean result = server.deleteEvent(eventId);
        int num = eventRepository.getAllEvents().size();
        assertEquals(num, 0);
        Mockito.verify(eventRepository, times(1)).deleteEvent(returnedEvent);
        assertEquals(result, true);


    }

    @Test
    void DeleteEvent_ShouldFail_WhenIdDoesNotExists() {
        Long eventId = 100L;
        Event returnedEvent = new Event("name", "body");
        Mockito.when(eventRepository.getEventById(eventId)).thenReturn(null);
        final boolean result = server.deleteEvent(eventId);
        int num = eventRepository.getAllEvents().size();
        assertEquals(num, 0);
        Mockito.verify(eventRepository, times(0)).deleteEvent(returnedEvent);
        assertEquals(result, false);


    }


    @Test
    void addNotTopLevelFolder_shouldNotAddAnyChildrens() {
        final ArgumentCaptor<Folder> captor = ArgumentCaptor.forClass(Folder.class);
        FolderDAO folderToAddDao = new FolderDAO();
        folderToAddDao.setName("test");
        Long folderId = 100L;
        folderToAddDao.setParentId(folderId);
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(new Folder("Parent"));
        server.addFolder(folderToAddDao);
        Mockito.verify(folderRepository).addFolder(captor.capture()); // capture the folder passed
        final Folder passedFolder = captor.getValue();
        assertEquals(passedFolder.getFolders().size(), 0);
    }

    @Test
    void addTopLevelFolder_shouldAddItWithStoriesAndEventsSubFolders() {
        final ArgumentCaptor<Folder> captor = ArgumentCaptor.forClass(Folder.class);
        FolderDAO folderToAddDao = new FolderDAO();
        folderToAddDao.setName("test");
        folderToAddDao.setParentId(null); // top level folders have no parent ids
        server.addFolder(folderToAddDao);
        Mockito.verify(folderRepository).updateFolder(captor.capture()); // capture the folder passed
        final Folder passedFolder = captor.getValue();
        assertEquals(passedFolder.getFolders().size(), 2);
    }

    @Test
    void updateFolder_ReturnTrue_WhenFolderExists() {
        Folder returnedFolder = new Folder();
        returnedFolder.setName("test");
        returnedFolder.setParentId(null);
        FolderDAO updateFolder = new FolderDAO();
        updateFolder.setName("test2");
        updateFolder.setParentId(null);
        long folderId = 1L;
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(returnedFolder);
        assertTrue(server.updateFolder(folderId, updateFolder));
        assertEquals("test2", returnedFolder.getName());

    }

    @Test
    void updateFolder_ReturnFalse_WhenIdDoesNotExists() {
        long folderId = 101L;
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(null);
        assertFalse(server.updateFolder(folderId, new FolderDAO()));
    }

    @Test
    void updateFolderName_ReturnFalse_WhenIdDoesNotExists() {
        long folderId = 101L;
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(null);
        assertFalse(server.updateFolderName(folderId, "some name"));
    }

    @Test
    void updateFolderName_ReturnTrue_WhenStoryExists() {
        long folderId = 1L;
        String newName = "new name";
        Folder returnedFolder = new Folder();
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(returnedFolder);
        assertTrue(server.updateFolderName(folderId, newName));
        assertEquals(newName, returnedFolder.getName());
    }


    @Test
    void DeleteFolder_ShouldPass() {
        Folder folderToAdd = new Folder();
        folderToAdd.setName("test");
        folderToAdd.setParentId(null);
        Long folderId = 100L;
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(folderToAdd);
        final boolean result = server.deleteFolder(folderId);
        int num = folderRepository.getAllFolders().size();
        assertEquals(num, 0);
        Mockito.verify(folderRepository, times(1)).deleteFolder(folderToAdd);
        assertEquals(result, true);


    }

    @Test
    void DeleteFolder_ShouldFail_WhenIdDoesNotExists() {
        Folder folderToAdd = new Folder();
        folderToAdd.setName("test");
        folderToAdd.setParentId(null);
        Long folderId = 100L;
        Mockito.when(folderRepository.getFolderById(folderId)).thenReturn(null);
        final boolean result = server.deleteFolder(folderId);
        int num = folderRepository.getAllFolders().size();
        assertEquals(num, 0);
        Mockito.verify(folderRepository, times(0)).deleteFolder(folderToAdd);
        assertEquals(result, false);


    }


    @Test
    void addStoryShouldAdd_andReturnNullId(){
        long parentId = 1;
        Folder parentFolder = new Folder("folder");
        StoryDAO storyDAO = new StoryDAO("Story", parentId, StoryType.JS);
        Mockito.when(folderRepository.getFolderById(parentId)).thenReturn(parentFolder);
        Long result = server.addStory(storyDAO);
        assertEquals(parentFolder.getStories().size(), 1);
        assertNull(result); // real db will generate some id
    }

    @Test
    void addStoryShouldFail_andReturnMinus1_whenNoParentId(){
        StoryDAO storyDAO = new StoryDAO("Story", (Long) null, StoryType.JS);
        Long result = server.addStory(storyDAO);
        assertEquals(result, -1);
    }

    @Test
    void updateStory_ReturnFalse_WhenIdDoesNotExists() {
        long storyId = 101L;
        Mockito.when(storyRepository.getStoryById(storyId)).thenReturn(null);
        assertFalse(server.updateStory(storyId, new StoryDAO("Story", (Long) null, StoryType.JS)));
    }

    @Test
    void updateStory_ReturnTrue_WhenStoryExists() {
        long storyId = 1L;
        String newName = "new name";
        String newBody = "new body";
        Story returnedStory = new Story("name", "body", StoryType.BLOCKLY, "");
        StoryDAO updateStory = new StoryDAO(newName, newBody, StoryType.BLOCKLY);
        Mockito.when(storyRepository.getStoryById(storyId)).thenReturn(returnedStory);
        assertTrue(server.updateStory(storyId, updateStory));
        assertEquals(newBody, returnedStory.getBody());
        assertEquals(newName, returnedStory.getName());
    }

    @Test
    void updateStoryName_ReturnFalse_WhenIdDoesNotExists() {
        long storyId = 101L;
        Mockito.when(storyRepository.getStoryById(storyId)).thenReturn(null);
        assertFalse(server.updateStoryName(storyId, "some name"));
    }

    @Test
    void updateStoryName_ReturnTrue_WhenStoryExists() {
        long storyId = 1L;
        String newName = "new name";
        Story returnedStory = new Story("name", "body", StoryType.JS, "");
        StoryDAO updateStory = new StoryDAO(newName, "body", StoryType.JS);
        Mockito.when(storyRepository.getStoryById(storyId)).thenReturn(returnedStory);
        assertTrue(server.updateStory(storyId, updateStory));
        assertEquals(newName, returnedStory.getName());
    }

    @Test
    void DeleteStory_ShouldPass() {
        Long storyId = 100L;
        Story returnedStory = new Story("name", "body", StoryType.JS, "");
        Mockito.when(storyRepository.getStoryById(storyId)).thenReturn(returnedStory);
        final boolean result = server.deleteStory(storyId);
        int num = storyRepository.getAllStories().size();
        assertEquals(num, 0);
        Mockito.verify(storyRepository, times(1)).deleteStory(returnedStory);
        assertEquals(result, true);


    }

    @Test
    void DeleteStory_ShouldFail_WhenIdDoesNotExists() {
        Long storyId = 100L;
        Story returnedStory = new Story("name", "body", StoryType.JS, "");
        Mockito.when(storyRepository.getStoryById(storyId)).thenReturn(null);
        final boolean result = server.deleteStory(storyId);
        int num = storyRepository.getAllStories().size();
        assertEquals(num, 0);
        Mockito.verify(storyRepository, times(0)).deleteStory(returnedStory);
        assertEquals(result, false);


    }


    @Test
    void getTree_shouldReturn_onlyRootFolder() {
        List<Folder> folderList = new LinkedList<>();
        Folder rootFolder = new Folder("root1");
        rootFolder.setParentId(null);
        Folder childFolder = new Folder("child1");
        childFolder.setParentId(1L);
        folderList.add(rootFolder);
        folderList.add(childFolder);
        Mockito.when(folderRepository.getAllFolders()).thenReturn(folderList);
        List<TreeNodeDao> roots = server.getTree();
        assertEquals(roots.size(), 1);
        assertEquals(roots.get(0).getName(), "root1");

    }

    @Test
    void addTestShouldAdd(){
        long parentId = 1;
        Folder parentFolder = new Folder("folder");
        List<String> foldersList = new ArrayList<>();
        foldersList.add("1");
        TestDAO TestDAO = new TestDAO("name", foldersList,parentId);
        Mockito.when(folderRepository.getFolderById(parentId)).thenReturn(parentFolder);
        boolean result = server.addTest(TestDAO);
        assertTrue(result); // real db will generate some id
    }

    @Test
    void DeleteTest_ShouldPass() {
        Long testId = 100L;
        Folder parentFolder = new Folder("folder");
        List<Folder> foldersList = new ArrayList<>();
        foldersList.add(parentFolder);
        com.testorystudio.testorystudio.Entities.Test returnedTest = new com.testorystudio.testorystudio.Entities.Test("name", foldersList);
        Mockito.when(testRepository.getTestById(testId)).thenReturn(returnedTest);
        final boolean result = server.deleteTest(testId);
        int num = testRepository.getAllTests().size();
        assertEquals(num, 0);
        Mockito.verify(testRepository, times(0)).deleteTest(returnedTest);
        assertEquals(result, true);


    }

    @Test
    void DeleteTest_ShouldFail_WhenIdDoesNotExists() {
        Long testId = 100L;
        Folder parentFolder = new Folder("folder");
        List<Folder> foldersList = new ArrayList<>();
        foldersList.add(parentFolder);
        com.testorystudio.testorystudio.Entities.Test returnedTest = new com.testorystudio.testorystudio.Entities.Test("name", foldersList);
        Mockito.when(testRepository.getTestById(testId)).thenReturn(null);
        final boolean result = server.deleteTest(testId);
        int num = testRepository.getAllTests().size();
        assertEquals(num, 0);
        Mockito.verify(testRepository, times(0)).deleteTest(returnedTest);
        assertEquals(result, false);

    }
}
