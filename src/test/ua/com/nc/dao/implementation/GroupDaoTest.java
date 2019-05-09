package ua.com.nc.dao.implementation;

import org.junit.*;
import ua.com.nc.dao.interfaces.GroupDao;
import ua.com.nc.domain.Group;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class GroupDaoTest {

    static private GroupDao groupDao;
    private static Group newGroup;
    private static Integer id;

    @BeforeClass
    public static void before() {
        SqlQueriesProperties sqlQueriesProperties = new SqlQueriesProperties();
        Properties properties = new Properties();
        InputStream input;
        try {
            input = GroupDaoTest.class.getClassLoader().getResourceAsStream("sql_queries.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlQueriesProperties.setGroupDelete(properties.getProperty("group.delete"));
        sqlQueriesProperties.setGroupInsert(properties.getProperty("group.insert"));
        sqlQueriesProperties.setGroupSelectAll(properties.getProperty("group.select-all"));
        sqlQueriesProperties.setGroupSelectById(properties.getProperty("group.select-by-id"));
        sqlQueriesProperties.setGroupUpdate(properties.getProperty("group.update"));
        groupDao = new GroupDaoImpl("jdbc:postgresql://45.66.10.81:5432/nc_training_center", "ncpostgres", "nc2019");
        newGroup = new Group(1, "groupName");
    }

    @AfterClass
    public static void after() {
        try {
            groupDao.rollback();
            groupDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        newGroup.setId(null);
        groupDao.insert(newGroup);
        id = newGroup.getId();
    }

    @After
    public void tearDown() {
        if (groupDao.getEntityById(id) != null){
            groupDao.delete(id);
        }
    }


    @Test
    public void getAll() {
        assertTrue(groupDao.getAll().size() > 0);
    }

    @Test
    public void getEntityById() {
        Group group = groupDao.getEntityById(id);
        assertNotNull(group);
        assertEquals(newGroup, group);
    }

    @Test
    public void update() {
        Group savedGroup = groupDao.getEntityById(id);
        assertNotNull(savedGroup);
        assertEquals(newGroup, savedGroup);

        Group newGroup = new Group(id,2, "new groupName");
        groupDao.update(newGroup);

        Group updatedRetrievedGroup = groupDao.getEntityById(id);
        assertNotNull(updatedRetrievedGroup);
        assertEquals(newGroup, updatedRetrievedGroup);
    }

    @Test
    public void delete() {
        List<Group> groups = groupDao.getAll();
        assertTrue(groups.size() > 0);
        int before = groups.size();
        groupDao.delete(id);
        assertEquals(before - 1, groupDao.getAll().size());
        assertNull(groupDao.getEntityById(id));
    }

    @Test
    public void insert() {
        List<Group> groups = groupDao.getAll();
        int before = groups.size();
        assertTrue(groups.size() > 0);
        Group group = new Group(2, "new groupName");
        groupDao.insert(group);
        assertNotNull(group.getId());
        Integer newID = group.getId();
        assertTrue(newID != 0);
        assertEquals(before + 1, groupDao.getAll().size());
        Group insertedGroup = groupDao.getEntityById(newID);
        assertNotNull(insertedGroup);
        assertEquals(group, insertedGroup);
    }





}