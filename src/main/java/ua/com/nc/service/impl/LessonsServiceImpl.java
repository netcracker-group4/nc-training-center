package ua.com.nc.service.impl;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.IAttachmentDao;
import ua.com.nc.dao.interfaces.ILessonAttachmentDao;
import ua.com.nc.dao.interfaces.ILessonDao;
import ua.com.nc.dao.interfaces.IUserDao;
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
    IUserDao iUserDao;

    private Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, (JsonSerializer<Timestamp>)
            (timestamp, type, jsonSerializationContext) -> new JsonPrimitive(timestamp.toString())).create();

    @Override
    public String getAllForGroup(int groupId) {
        List<DtoLesson> dtoLessons = new ArrayList<>();
        List<Lesson> lessons = iLessonDao.getByGroupId(groupId);
        System.out.println(lessons);
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
        System.out.println("domain    to update    " + domainLesson);
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
        System.out.println("domain    to insert    " + domainLesson);
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
    public String cancelLesson(int parseInt) {
        Lesson lesson = iLessonDao.getEntityById(parseInt);
        System.out.println(lesson);
        boolean newCanceled = !lesson.isCanceled();
        lesson.setCanceled(newCanceled);
        System.out.println(lesson);
        iLessonDao.update(lesson);
        iLessonDao.commit();
        System.out.println("ok");
        return Boolean.toString(newCanceled);
    }
}
