package ua.com.nc.dao.implementation;

import org.junit.*;
import ua.com.nc.dao.interfaces.IUserDao;
import ua.com.nc.model.User;

import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTest {

    static private IUserDao iUserDao;
    private static User newUser;
    private static Integer id;

    @BeforeClass
    public static void before() {
        iUserDao = new UserDao();
        System.out.println("instantiated accountDao");
        newUser = new User("user@gmail.com", "kjdfdfshd", "test", "user", 0, true);
    }

    @AfterClass
    public static void after() {
        try {
            iUserDao.rollback();
            iUserDao.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        newUser.setId(null);
        iUserDao.insert(newUser);
        id = newUser.getId();
    }

    @After
    public void tearDown() {
        if (iUserDao.getEntityById(id) != null){
            iUserDao.delete(id);
        }
    }


    @Test
    public void getAll() {
        assertTrue(iUserDao.getAll().size() > 0);
    }

    @Test
    public void getEntityById() {
        User user = iUserDao.getEntityById(id);
        assertNotNull(user);
        assertEquals(newUser, user);
    }

    @Test
    public void update() {
        User savedUser = iUserDao.getEntityById(id);
        assertNotNull(savedUser);
        assertEquals(newUser, savedUser);

        User newUser = new User(id,"new@new.com", "new", "firstNew", "lastNew", 0, false);
        iUserDao.update(newUser);

        User updatedRetrievedUser = iUserDao.getEntityById(id);
        assertNotNull(updatedRetrievedUser);
        assertEquals(newUser, updatedRetrievedUser);
    }

    @Test
    public void delete() {
        List<User> users = iUserDao.getAll();
        assertTrue(users.size() > 0);
        int before = users.size();
        iUserDao.delete(id);
        assertEquals(before - 1, iUserDao.getAll().size());
        assertNull(iUserDao.getEntityById(id));
    }

    @Test
    public void insert() {
        List<User> users = iUserDao.getAll();
        int before = users.size();
        assertTrue(users.size() > 0);
        User user = new User("JustInsertedUser@gmail.com", "kjdfdfshd", "test", "user", 0, true);
        iUserDao.insert(user);
        assertNotNull(user.getId());
        Integer newID = user.getId();
        assertTrue(newID != 0);
        assertEquals(before + 1, iUserDao.getAll().size());
        User insertedUser = iUserDao.getEntityById(newID);
        assertNotNull(insertedUser);
        assertEquals(user, insertedUser);
    }

}