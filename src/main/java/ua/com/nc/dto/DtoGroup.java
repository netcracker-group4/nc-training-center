package ua.com.nc.dto;

public class DtoGroup {
    private Integer id;
    private String title;

    public DtoGroup(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
