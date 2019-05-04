package ua.com.nc.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.IAttachmentDao;
import ua.com.nc.dao.interfaces.ILessonAttachmentDao;
import ua.com.nc.dao.interfaces.ILessonDao;
import ua.com.nc.domain.Lesson;
import ua.com.nc.domain.LessonAttachment;
import ua.com.nc.dto.DtoLesson;
import ua.com.nc.service.LessonsService;

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

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();

    @Override
    public String getAllForGroup(int groupId) {
        List<DtoLesson> dtoLessons = new ArrayList<>();
        List<Lesson> lessons = iLessonDao.getByGroupId(groupId);
        System.out.println(lessons);
        for (Lesson lesson : lessons) {
            DtoLesson dtoLesson = new DtoLesson(lesson, iAttachmentDao.getByLessonId(lesson.getId()));
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
        for (LessonAttachment attachment : toAdd.getAttachments()) {
            iLessonAttachmentDao.insertAttachment(attachment);
        }
        iLessonAttachmentDao.commit();
        return Integer.toString(domainLesson.getId());
    }

    @Override
    public String deleteLesson(int toDelete) {
        iLessonDao.delete(toDelete);
        iLessonDao.commit();
        return "Lesson deleted";
    }
}
