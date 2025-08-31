package smartstudentplatform;

import java.util.ArrayList;

public class Student extends Person implements Comparable<Student> {
    private ArrayList<Result> results = new ArrayList<>();

    public Student(int id, String name) {
        super(id, name);
    }

    public void addResult(String course, double grade) {
        results.add(new Result(course, grade));
    }

    public double getCGPA() {
        if (results.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Result r : results) {
            sum += r.getGrade();
        }
        return sum / results.size();
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.getId(), other.getId());
    }
}
