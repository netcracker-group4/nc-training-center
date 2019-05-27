package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.ChatDao;
import ua.com.nc.dao.interfaces.ProblemDao;
import ua.com.nc.dao.interfaces.ProblemStatusDao;
import ua.com.nc.dao.interfaces.UserDao;
import ua.com.nc.domain.Chat;
import ua.com.nc.domain.Problem;
import ua.com.nc.domain.ProblemStatus;
import ua.com.nc.domain.User;
import ua.com.nc.service.ChatService;
import ua.com.nc.service.EmailReminderService;
import ua.com.nc.service.InfodeskRequestService;

import java.util.List;

@Log4j2
@Service
public class InfodeskRequestServiceImpl implements InfodeskRequestService {

    @Autowired
    private ProblemDao problemDao;

    @Autowired
    private ProblemStatusDao problemStatusDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChatDao chatDao;

    @Autowired
    private EmailReminderService emailReminderService;

    @Autowired
    private ChatService chatService;


    @Override
    public void createRequest(Integer userId, String description, String message, String requestType) {

        User admin = userDao.getAdmin();
        chatService.addMessage(message, userId, admin.getId(), null);

        Chat chat = chatDao.getChatBySenderIdAndReceiverId(userId, admin.getId());

        ProblemStatus ps = problemStatusDao.getProblemStatusByString(requestType);
        Problem problem = new Problem(userId, description, message, ps.getId(), chat.getId());

        problemDao.insert(problem);

        if (!requestType.equals("draft")) emailReminderService.sendInfodeskNotification();
    }

    @Override
    public List<Problem> getAllRequests() {
        return problemDao.getAll();
    }

    @Override
    public void updateRequestType(Integer requestId, String requestType) {

        Problem problem = problemDao.getEntityById(requestId);
        ProblemStatus ps = problemStatusDao.getProblemStatusByString(requestType);

        problem.setStatus(ps.getId());
        problemDao.update(problem);

    }

    @Override
    public List<ProblemStatus> getStatuses() {
        return problemStatusDao.getAll();
    }

    @Override
    public List<Problem> getRequestsByUserId(Integer userId) {
        return problemDao.getRequestsByUserId(userId);
    }

}
