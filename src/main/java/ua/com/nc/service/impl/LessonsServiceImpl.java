package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Attachment;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoLesson;
import ua.com.nc.dto.DtoUser;
import ua.com.nc.exceptions.LogicException;
import ua.com.nc.service.LessonsService;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class LessonsServiceImpl implements LessonsService {
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private LessonAttachmentDao lessonAttachmentDao;

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<DtoLesson> getAllForGroup(Integer groupId) {
        List<Lesson> lessons = lessonDao.getByGroupId(groupId);
        return createDtoForLessons(lessons);
    }

    private List<DtoLesson> createDtoForLessons(List<Lesson> lessons) {
        List<DtoLesson> dtoLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            DtoLesson dtoLesson = convertToDtoLesson(lesson);
            dtoLessons.add(dtoLesson);
        }
        return dtoLessons;
    }

    private DtoLesson convertToDtoLesson(Lesson lesson) {
        User trainer = userDao.getEntityById(lesson.getTrainerId());
        String trainerName = trainer.getFirstName() + " " + trainer.getLastName();
        List<Attachment> attachmentsForLesson = attachmentDao.getByLessonId(lesson.getId());
        return new DtoLesson(lesson, trainerName, attachmentsForLesson);
    }

    @Override
    public String updateLesson(DtoLesson toUpdate) {
        Lesson domainLesson = toUpdate.getDomainLesson();
        lessonDao.update(domainLesson);
        lessonAttachmentDao.deleteByLessonId(toUpdate.getId());
        for (LessonAttachment attachment : toUpdate.getDomainAttachments()) {
            lessonAttachmentDao.insertAttachment(attachment);
        }
        return Integer.toString(toUpdate.getId());
    }

    @Override
    public String addLesson(DtoLesson lessonToInsert) {
        Lesson domainLesson = lessonToInsert.getDomainLesson();
        lessonDao.insert(domainLesson);
        Integer newLessonId = domainLesson.getId();
        for (Attachment attachment : lessonToInsert.getAttachments()) {
            LessonAttachment lessonAttachment = new LessonAttachment(attachment.getId(), newLessonId);
            lessonAttachmentDao.insertAttachment(lessonAttachment);
        }
        return Integer.toString(newLessonId);
    }


    @Override
    public void deleteLesson(int toArchive) {
        Lesson lessonToArchive = lessonDao.getEntityById(toArchive);
        lessonToArchive.setArchived(true);
        lessonDao.update(lessonToArchive);
        lessonAttachmentDao.deleteByLessonId(toArchive);
    }

    @Override
    public String invertIsCanceledForLesson(Integer lessonId) {
        Lesson lesson = lessonDao.getEntityById(lessonId);
        boolean newCanceled = !lesson.isCanceled();
        lesson.setCanceled(newCanceled);
        lessonDao.update(lesson);
        return Boolean.toString(newCanceled);
    }

    @Override
    public List<DtoLesson> getAllForEmployee(Integer userId) {
        List<Lesson> lessons = lessonDao.getByEmployee(userId);
        return createDtoForLessons(lessons);
    }

    @Override
    public List<DtoLesson> getAllForTrainer(Integer userId) {
        List<Lesson> lessons = lessonDao.getByTrainer(userId);
        return createDtoForLessons(lessons);
    }

    @Override
    public Lesson getLessonById(Integer lessonId) {
        Lesson lesson = lessonDao.getEntityById(lessonId);
        if(lesson == null){
            throw new LogicException("There is no such lesson");
        }
        return lesson;
    }

    @Override
    public List<DtoUser> getStudentsIdsByLessonId(Integer lessonId) {
        List<DtoUser> dtoUsers = new ArrayList<>();
        List<User> users = userDao.getStudentsByLessonId(lessonId);
        if (!users.isEmpty()) {
            for (User user : users) {
                dtoUsers.add(new DtoUser(user.getId()));
            }
        }
        return dtoUsers;
    }

    @Override
    public String invertIsPerformedForLesson(Integer lessonId) {
        Lesson lesson = lessonDao.getEntityById(lessonId);
        boolean newPerformed = !lesson.isPerformed();
        lesson.setPerformed(newPerformed);
        lessonDao.update(lesson);
        return Boolean.toString(newPerformed);
    }
}
