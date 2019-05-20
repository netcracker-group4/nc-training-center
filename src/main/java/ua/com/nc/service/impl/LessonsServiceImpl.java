package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoLesson;
import ua.com.nc.service.LessonsService;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class LessonsServiceImpl implements LessonsService {
    @Autowired
    LessonDao lessonDao;
    @Autowired
    AttachmentDao attachmentDao;
    @Autowired
    LessonAttachmentDao lessonAttachmentDao;

    @Autowired
    CourseDao courseDao;
    @Autowired
    UserDao userDao;

    @Override
    public List<DtoLesson> getAllForGroup(int groupId) {
        List<Lesson> lessons = lessonDao.getByGroupId(groupId);
        return createDtoForLessons(lessons);
    }

    private List<DtoLesson> createDtoForLessons(List<Lesson> lessons) {
        List<DtoLesson> dtoLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            User trainer = userDao.getEntityById(lesson.getTrainerId());
            String trainerName = trainer.getFirstName() + " " + trainer.getLastName();

            DtoLesson dtoLesson = new DtoLesson(lesson, trainerName,
                    attachmentDao.getByLessonId(lesson.getId()));
            dtoLessons.add(dtoLesson);
        }
        return dtoLessons;
    }

    @Override
    public String updateLesson(DtoLesson toUpdate) {
        Lesson domainLesson = toUpdate.getDomainLesson();
        lessonDao.update(domainLesson);
        lessonAttachmentDao.deleteByLessonId(toUpdate.getId());
        for (LessonAttachment attachment : toUpdate.getAttachments()) {
            lessonAttachmentDao.insertAttachment(attachment);
        }
        return Integer.toString(toUpdate.getId());
    }

    @Override
    public String addLesson(DtoLesson toAdd) {
        Lesson domainLesson = toAdd.getDomainLesson();
        lessonDao.insert(domainLesson);
        for (LessonAttachment attachment : toAdd.getAttachmentsForNewLesson(domainLesson.getId())) {
            lessonAttachmentDao.insertAttachment(attachment);
        }
        return Integer.toString(domainLesson.getId());
    }

    @Override
    public String deleteLesson(int toArchive) {
        lessonAttachmentDao.deleteByLessonId(toArchive);
        lessonDao.archiveLesson(toArchive);
        return "Lesson deleted";
    }

    @Override
    public String cancelLesson(int lessonId) {
        Lesson lesson = lessonDao.getEntityById(lessonId);
        boolean newCanceled = !lesson.isCanceled();
        lesson.setCanceled(newCanceled);
        lessonDao.update(lesson);
        return Boolean.toString(newCanceled);
    }

    @Override
    public List<DtoLesson> getAllForEmployee(int userId) {
        List<Lesson> lessons = lessonDao.getByEmployee(userId);
        return createDtoForLessons(lessons);
    }

    @Override
    public List<DtoLesson> getAllForETrainer(Integer userId) {
        List<Lesson> lessons = lessonDao.getByTrainer(userId);
        return createDtoForLessons(lessons);
    }
}
