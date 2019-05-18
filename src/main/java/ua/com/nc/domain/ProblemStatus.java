package ua.com.nc.domain;

public class ProblemStatus extends Entity {

    private String title;

    public ProblemStatus (String title) {
        this.title = title;
    }

    public ProblemStatus (Integer id, String title) {
        super (id);
        this.title = title;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

}
