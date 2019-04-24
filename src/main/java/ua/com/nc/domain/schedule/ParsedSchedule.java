package ua.com.nc.domain.schedule;

import lombok.Getter;
import ua.com.nc.domain.DesiredSchedule;
import ua.com.nc.domain.Suitability;

import java.time.DayOfWeek;
import java.util.List;

@Getter
public class ParsedSchedule {
    private int userId;
    private DayOfWeek dayOfWeek;
    private int start;
    private int end;
    private String color;


    public ParsedSchedule(DesiredSchedule desiredSchedule, List<Suitability> priorityList) throws Exception {
        Suitability suitability = findSuitability(desiredSchedule.getSuitability(), priorityList);
        this.userId = desiredSchedule.getUserId();
        this.color = suitability.getColor();
        String cron = desiredSchedule.getCronInterval();
        String[] elem = cron.split("\\s+");
        start = Integer.parseInt(elem[1]);
        end = Integer.parseInt(elem[3]);
        dayOfWeek = DayOfWeek.of(Integer.parseInt(elem[4]));
    }

    private Suitability findSuitability(int suitabilityId, List<Suitability> suitabilityList) throws Exception {
        for (Suitability suitability : suitabilityList) {
            if (suitability.getId() == suitabilityId) {
                return suitability;
            }
        }
        throw new Exception("No such suitability");
    }

}
