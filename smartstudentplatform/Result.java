package smartstudentplatform;

public class Result {
    private String course;
    private double grade;

    public Result(String course, double grade) {
        this.course = course;
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public double getGrade() {
        return grade;
    }
}
