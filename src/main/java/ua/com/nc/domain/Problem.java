package ua.com.nc.domain;

import lombok.Data;
import java.util.Objects;

@Data
public class Problem extends Entity {

    private Integer studentId;
    private String description;
    private String message;
    private String status;

    public Integer getStudentId() { return studentId; }

    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Problem () {}

    public Problem (Integer id, Integer studentId, String description, String message, String status) {
        super(id);
        this.studentId = studentId ;
        this.description = description;
        this.message = message;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return Objects.equals(studentId, problem.studentId) &&
                Objects.equals(description, problem.description) &&
                Objects.equals(message, problem.message) &&
                Objects.equals(status, problem.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, description, message, status);
    }

    @Override
    public String toString() {
        return "Problem{" +
                "studentId=" + studentId +
                ", description='" + description + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
