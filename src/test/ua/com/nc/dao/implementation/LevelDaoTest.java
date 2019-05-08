package ua.com.nc.dao.implementation;

import org.junit.*;
import ua.com.nc.dao.interfaces.ILevelDao;
import ua.com.nc.domain.Level;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class LevelDaoTest {
    static private ILevelDao iLevelDao;
    private static Level newLevel;
    private static Integer id;

    @BeforeClass
    public static void before() {
        SqlQueriesProperties sqlQueriesProperties = new SqlQueriesProperties();
        Properties properties = new Properties();
        InputStream input;
        try {
            input = LevelDaoTest.class.getClassLoader().getResourceAsStream("sql_queries.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlQueriesProperties.setLevelDelete(properties.getProperty("level.delete"));
        sqlQueriesProperties.setLevelInsert(properties.getProperty("level.insert"));
        sqlQueriesProperties.setLevelSelectAll(properties.getProperty("level.select-all"));
        sqlQueriesProperties.setLevelSelectById(properties.getProperty("level.select-by-id"));
        sqlQueriesProperties.setLevelUpdate(properties.getProperty("level.update"));
//        iLevelDao = new LevelDao("jdbc:postgresql://45.66.10.81:5432/nc_training_center", "ncpostgres", "nc2019", sqlQueriesProperties);
        System.out.println("instantiated COURSE DAO");
        newLevel = new Level("levelName");
    }

    @AfterClass
    public static void after() {
        try {
            iLevelDao.rollback();
            iLevelDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        newLevel.setId(null);
        iLevelDao.insert(newLevel);
        id = newLevel.getId();
    }

    @After
    public void tearDown() {
        if (iLevelDao.getEntityById(id) != null){
            iLevelDao.delete(id);
        }
    }


    @Test
    public void getAll() {
        assertTrue(iLevelDao.getAll().size() > 0);
    }

    @Test
    public void getEntityById() {
        Level level = iLevelDao.getEntityById(id);
        assertNotNull(level);
        assertEquals(newLevel, level);
    }

    @Test
    public void update() {
        Level savedLevel = iLevelDao.getEntityById(id);
        assertNotNull(savedLevel);
        assertEquals(newLevel, savedLevel);

        Level newLevel = new Level(id,"new levelName");
        iLevelDao.update(newLevel);

        Level updatedRetrievedLevel = iLevelDao.getEntityById(id);
        assertNotNull(updatedRetrievedLevel);
        assertEquals(newLevel, updatedRetrievedLevel);
    }

    @Test
    public void delete() {
        List<Level> levels = iLevelDao.getAll();
        assertTrue(levels.size() > 0);
        int before = levels.size();
        iLevelDao.delete(id);
        assertEquals(before - 1, iLevelDao.getAll().size());
        assertNull(iLevelDao.getEntityById(id));
    }

    @Test
    public void insert() {
        List<Level> levels = iLevelDao.getAll();
        int before = levels.size();
        assertTrue(levels.size() > 0);
        Level level = new Level("new level");
        iLevelDao.insert(level);
        assertNotNull(level.getId());
        Integer newID = level.getId();
        assertTrue(newID != 0);
        assertEquals(before + 1, iLevelDao.getAll().size());
        Level insertedLevel = iLevelDao.getEntityById(newID);
        assertNotNull(insertedLevel);
        assertEquals(level, insertedLevel);
    }



}