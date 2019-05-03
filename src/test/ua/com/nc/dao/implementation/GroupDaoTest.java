package ua.com.nc.dao.implementation;

import org.junit.*;
import ua.com.nc.dao.interfaces.IGroupDao;
import ua.com.nc.domain.Group;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class GroupDaoTest {

    static private IGroupDao iGroupDao;
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
        iGroupDao = new GroupDao("jdbc:postgresql://45.66.10.81:5432/nc_training_center", "ncpostgres", "nc2019");
        newGroup = new Group(1, "groupName");
    }

    @AfterClass
    public static void after() {
        try {
            iGroupDao.rollback();
            iGroupDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        newGroup.setId(null);
        iGroupDao.insert(newGroup);
        id = newGroup.getId();
    }

    @After
    public void tearDown() {
        if (iGroupDao.getEntityById(id) != null){
            iGroupDao.delete(id);
        }
    }


    @Test
    public void getAll() {
        assertTrue(iGroupDao.getAll().size() > 0);
    }

    @Test
    public void getEntityById() {
        Group group = iGroupDao.getEntityById(id);
        assertNotNull(group);
        assertEquals(newGroup, group);
    }

    @Test
    public void update() {
        Group savedGroup = iGroupDao.getEntityById(id);
        assertNotNull(savedGroup);
        assertEquals(newGroup, savedGroup);

        Group newGroup = new Group(id,2, "new groupName");
        iGroupDao.update(newGroup);

        Group updatedRetrievedGroup = iGroupDao.getEntityById(id);
        assertNotNull(updatedRetrievedGroup);
        assertEquals(newGroup, updatedRetrievedGroup);
    }

    @Test
    public void delete() {
        List<Group> groups = iGroupDao.getAll();
        assertTrue(groups.size() > 0);
        int before = groups.size();
        iGroupDao.delete(id);
        assertEquals(before - 1, iGroupDao.getAll().size());
        assertNull(iGroupDao.getEntityById(id));
    }

    @Test
    public void insert() {
        List<Group> groups = iGroupDao.getAll();
        int before = groups.size();
        assertTrue(groups.size() > 0);
        Group group = new Group(2, "new groupName");
        iGroupDao.insert(group);
        assertNotNull(group.getId());
        Integer newID = group.getId();
        assertTrue(newID != 0);
        assertEquals(before + 1, iGroupDao.getAll().size());
        Group insertedGroup = iGroupDao.getEntityById(newID);
        assertNotNull(insertedGroup);
        assertEquals(group, insertedGroup);
    }





}