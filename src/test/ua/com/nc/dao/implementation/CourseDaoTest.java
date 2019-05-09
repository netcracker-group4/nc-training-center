package ua.com.nc.dao.implementation;

import org.junit.*;
import ua.com.nc.dao.interfaces.CourseDao;
import ua.com.nc.domain.Course;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class CourseDaoTest {

    static private CourseDao courseDao;
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
        courseDao = new CourseDaoImpl("jdbc:postgresql://45.66.10.81:5432/nc_training_center", "ncpostgres", "nc2019");
        newCourse = new Course("courseName", 1, 1, 1, "url", new Date(65465465L), new Date(654L), true, "desc");
    }

    @AfterClass
    public static void after() {
        try {
            courseDao.rollback();
            courseDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        newCourse.setId(null);
        courseDao.insert(newCourse);
        id = newCourse.getId();
    }

    @After
    public void tearDown() {
        if (courseDao.getEntityById(id) != null){
            courseDao.delete(id);
        }
    }


    @Test
    public void getAll() {
        assertTrue(courseDao.getAll().size() > 0);
    }

    @Test
    public void getEntityById() {
        Course course = courseDao.getEntityById(id);
        assertNotNull(course);
        assertEquals(newCourse, course);
    }

    @Test
    public void update() {
        Course savedCourse = courseDao.getEntityById(id);
        assertNotNull(savedCourse);
        assertEquals(newCourse, savedCourse);

        Course newCourse = new Course(id,"new courseName", 2, 2, 2, "new url", new Date(55555L), new Date(6666L), false, "new desc");
        courseDao.update(newCourse);

        Course updatedRetrievedCourse = courseDao.getEntityById(id);
        assertNotNull(updatedRetrievedCourse);
        assertEquals(newCourse, updatedRetrievedCourse);
    }

    @Test
    public void delete() {
        List<Course> courses = courseDao.getAll();
        assertTrue(courses.size() > 0);
        int before = courses.size();
        courseDao.delete(id);
        assertEquals(before - 1, courseDao.getAll().size());
        assertNull(courseDao.getEntityById(id));
    }

    @Test
    public void insert() {
        List<Course> courses = courseDao.getAll();
        int before = courses.size();
        assertTrue(courses.size() > 0);
        Course course = new Course("new courseName", 2, 2, 2, "new url", new Date(55555), new Date(6666), false, "new desc");
        courseDao.insert(course);
        assertNotNull(course.getId());
        Integer newID = course.getId();
        assertTrue(newID != 0);
        assertEquals(before + 1, courseDao.getAll().size());
        Course insertedCourse = courseDao.getEntityById(newID);
        assertNotNull(insertedCourse);
        assertEquals(course, insertedCourse);
    }


    @Test
    public void getAllByLevel() {
        int levelId = 1;
        List<Course> courses = courseDao.getAllByLevel(levelId);
        for (Course course : courses) {
            assertEquals(levelId, course.getLevel());
        }
    }
}