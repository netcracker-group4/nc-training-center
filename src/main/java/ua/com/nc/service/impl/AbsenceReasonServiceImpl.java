package ua.com.nc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.implementation.AbsenceReasonDao;
import ua.com.nc.domain.AbsenceReason;
import ua.com.nc.service.AbsenceReasonService;

@Service
public class AbsenceReasonServiceImpl implements AbsenceReasonService {
    @Autowired
    private AbsenceReasonDao absenceReasonDao;

    @Override
    public void add(AbsenceReason reason) {
        absenceReasonDao.insert(reason);
        absenceReasonDao.commit();
    }

    @Override
    public void add(String title) {
        AbsenceReason reason = new AbsenceReason(title);
        add(reason);
    }
}
