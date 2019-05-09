package ua.com.nc.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.*;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.domain.User;
import ua.com.nc.dto.DtoLesson;
import ua.com.nc.service.LessonsService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonsServiceImpl implements LessonsService {
    @Autowired
    ILessonDao iLessonDao;
    @Autowired
    IAttachmentDao iAttachmentDao;
    @Autowired
    ILessonAttachmentDao iLessonAttachmentDao;

    @Autowired
    ICourseDao iCourseDao;
    @Autowired
    IUserDao iUserDao;

    private Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, (JsonSerializer<Timestamp>)
            (timestamp, type, jsonSerializationContext) -> new JsonPrimitive(timestamp.toString())).create();

    @Override
    public String getAllForGroup(int groupId) {
        List<Lesson> lessons = iLessonDao.getByGroupId(groupId);
        return createDtoForLessons(lessons);
    }

    private String createDtoForLessons(List<Lesson> lessons) {
        List<DtoLesson> dtoLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            User trainer = iUserDao.getEntityById(lesson.getTrainerId());
            String trainerName = trainer.getFirstName() + " " + trainer.getLastName();

            DtoLesson dtoLesson = new DtoLesson(lesson, trainerName,
                    iAttachmentDao.getByLessonId(lesson.getId()));
            dtoLessons.add(dtoLesson);
        }
        return gson.toJson(dtoLessons);
    }

    @Override
    public String updateLesson(DtoLesson toUpdate) {
        Lesson domainLesson = toUpdate.getDomainLesson();
        iLessonDao.update(domainLesson);
        iLessonDao.commit();
        iLessonAttachmentDao.deleteByLessonId(toUpdate.getId());
        for (LessonAttachment attachment : toUpdate.getAttachments()) {
            iLessonAttachmentDao.insertAttachment(attachment);
        }
        iLessonAttachmentDao.commit();
        return Integer.toString(toUpdate.getId());
    }

    @Override
    public String addLesson(DtoLesson toAdd) {
        Lesson domainLesson = toAdd.getDomainLesson();
        iLessonDao.insert(domainLesson);
        iLessonDao.commit();
        for (LessonAttachment attachment : toAdd.getAttachmentsForNewLesson(domainLesson.getId())) {
            iLessonAttachmentDao.insertAttachment(attachment);
        }
        iLessonAttachmentDao.commit();
        return Integer.toString(domainLesson.getId());
    }

    @Override
    public String deleteLesson(int toArchive) {
        iLessonAttachmentDao.deleteByLessonId(toArchive);
        iLessonAttachmentDao.commit();
        iLessonDao.archiveLesson(toArchive);
        iLessonDao.commit();
        return "Lesson deleted";
    }

    @Override
    public String cancelLesson(int lessonId) {
        Lesson lesson = iLessonDao.getEntityById(lessonId);
        boolean newCanceled = !lesson.isCanceled();
        lesson.setCanceled(newCanceled);
        iLessonDao.update(lesson);
        iLessonDao.commit();
        return Boolean.toString(newCanceled);
    }

    @Override
    public String getAllForUser(int userId) {
        List<Lesson> lessons = iLessonDao.getByUser(userId);
        return createDtoForLessons(lessons);
    }
}
