package ua.com.nc.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.nc.dao.interfaces.AbsenceReasonDao;
import ua.com.nc.domain.AbsenceReason;
import ua.com.nc.service.AbsenceReasonService;


@Log4j
@Service
public class AbsenceReasonServiceImpl implements AbsenceReasonService {
    @Autowired
    private AbsenceReasonDao absenceReasonDao;

    @Override
    public Integer add(AbsenceReason absenceReason) {
        absenceReasonDao.insert(absenceReason);
        return absenceReason.getId();
    }
}
