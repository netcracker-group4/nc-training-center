package ua.com.nc.dao.implementation;

import org.junit.*;
import ua.com.nc.dao.interfaces.ICourseDao;
import ua.com.nc.domain.Course;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class CourseDaoTest {

    static private ICourseDao iCourseDao;
    private static Course newCourse;
    private static Integer id;

    @BeforeClass
    public static void before() {
        SqlQueriesProperties sqlQueriesProperties = new SqlQueriesProperties();
        Properties properties = new Properties();
        InputStream input;
        try {
            input = CourseDaoTest.class.getClassLoader().getResourceAsStream("sql_queries.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlQueriesProperties.setCourseDelete(properties.getProperty("course.delete"));
        sqlQueriesProperties.setCourseInsert(properties.getProperty("course.insert"));
        sqlQueriesProperties.setCourseSelectAll(properties.getProperty("course.select-all"));
        sqlQueriesProperties.setCourseSelectById(properties.getProperty("course.select-by-id"));
        sqlQueriesProperties.setCourseUpdate(properties.getProperty("course.update"));
        sqlQueriesProperties.setCourseSelectByLevel(properties.getProperty("course.select-by-level"));
        iCourseDao = new CourseDao("jdbc:postgresql://45.66.10.81:5432/nc_training_center", "ncpostgres", "nc2019", sqlQueriesProperties);
        System.out.println("instantiated COURSE DAO");
        newCourse = new Course("courseName", 1, 1, 1, "url", new Date(65465465L), new Date(654L), true, "desc");
    }

    @AfterClass
    public static void after() {
        try {
            iCourseDao.rollback();
            iCourseDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        newCourse.setId(null);
        iCourseDao.insert(newCourse);
        id = newCourse.getId();
    }

    @After
    public void tearDown() {
        if (iCourseDao.getEntityById(id) != null){
            iCourseDao.delete(id);
        }
    }


    @Test
    public void getAll() {
        assertTrue(iCourseDao.getAll().size() > 0);
    }

    @Test
    public void getEntityById() {
        Course course = iCourseDao.getEntityById(id);
        assertNotNull(course);
        assertEquals(newCourse, course);
    }

    @Test
    public void update() {
        Course savedCourse = iCourseDao.getEntityById(id);
        assertNotNull(savedCourse);
        assertEquals(newCourse, savedCourse);

        Course newCourse = new Course(id,"new courseName", 2, 2, 2, "new url", new Date(55555L), new Date(6666L), false, "new desc");
        iCourseDao.update(newCourse);

        Course updatedRetrievedCourse = iCourseDao.getEntityById(id);
        assertNotNull(updatedRetrievedCourse);
        assertEquals(newCourse, updatedRetrievedCourse);
    }

    @Test
    public void delete() {
        List<Course> courses = iCourseDao.getAll();
        assertTrue(courses.size() > 0);
        int before = courses.size();
        iCourseDao.delete(id);
        assertEquals(before - 1, iCourseDao.getAll().size());
        assertNull(iCourseDao.getEntityById(id));
    }

    @Test
    public void insert() {
        List<Course> courses = iCourseDao.getAll();
        int before = courses.size();
        assertTrue(courses.size() > 0);
        Course course = new Course("new courseName", 2, 2, 2, "new url", new Date(55555), new Date(6666), false, "new desc");
        iCourseDao.insert(course);
        assertNotNull(course.getId());
        Integer newID = course.getId();
        assertTrue(newID != 0);
        assertEquals(before + 1, iCourseDao.getAll().size());
        Course insertedCourse = iCourseDao.getEntityById(newID);
        assertNotNull(insertedCourse);
        assertEquals(course, insertedCourse);
    }


    @Test
    public void getAllByLevel() {
        int levelId = 1;
        List<Course> courses = iCourseDao.getAllByLevel(levelId);
        for (Course course : courses) {
            assertEquals(levelId, course.getLevel());
        }
    }
}