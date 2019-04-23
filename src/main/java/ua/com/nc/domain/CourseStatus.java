package ua.com.nc.domain;


public enum  CourseStatus {
    PLANNED("The registration has not started yet"),
    REGISTRATION("The registrion is open. No schedule yet"),
    SCHEDULED("Registration is closed. Schedule is formed. The lessons have not started yet"),
    ONGOING("The lessons have started"),
    ENDED("The lessons have ended");
    private String description;
    private CourseStatus(String description){
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name();
    }
}
