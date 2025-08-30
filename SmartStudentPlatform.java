import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SmartStudentPlatform extends JFrame implements ActionListener {
    // Inner classes for OOP
    static class Person {
        private String name;
        private int id;

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class Result {
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

    static class Student extends Person implements Comparable<Student> {
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

    // Data structures
    private ArrayList<Student> students = new ArrayList<>();
    private HashMap<Integer, Student> studentMap = new HashMap<>();

    // GUI components
    private DefaultListModel<String> listModel;
    private JList<String> displayList;
    private JTextField idField, nameField, courseField, gradeField;
    private JLabel errorLabel;
    private JButton addButton, updateButton, viewButton, sortNameButton, sortIdButton, sortCgpaButton, searchButton, summaryButton;

    public SmartStudentPlatform() {
        setTitle("Smart Student Platform");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel (left side)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Course:"));
        courseField = new JTextField();
        inputPanel.add(courseField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        inputPanel.add(addButton);
        inputPanel.add(updateButton);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        inputPanel.add(errorLabel);

        add(inputPanel, BorderLayout.WEST);

        // Button panel (top)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        viewButton = new JButton("View All");
        sortNameButton = new JButton("Sort by Name");
        sortIdButton = new JButton("Sort by ID");
        sortCgpaButton = new JButton("Sort by CGPA");
        searchButton = new JButton("Search Student");
        summaryButton = new JButton("Show Summaries");

        buttonPanel.add(viewButton);
        buttonPanel.add(sortNameButton);
        buttonPanel.add(sortIdButton);
        buttonPanel.add(sortCgpaButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(summaryButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Display area (center)
        listModel = new DefaultListModel<>();
        displayList = new JList<>(listModel);
        displayList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayList);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        viewButton.addActionListener(this);
        sortNameButton.addActionListener(this);
        sortIdButton.addActionListener(this);
        sortCgpaButton.addActionListener(this);
        searchButton.addActionListener(this);
        summaryButton.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        errorLabel.setText(" ");
        if (e.getSource() == addButton) {
            addStudent();
        } else if (e.getSource() == updateButton) {
            updateStudent();
        } else if (e.getSource() == viewButton) {
            viewAll();
        } else if (e.getSource() == sortNameButton) {
            sortByName();
            viewAll();
        } else if (e.getSource() == sortIdButton) {
            sortById();
            viewAll();
        } else if (e.getSource() == sortCgpaButton) {
            sortByCgpa();
            viewAll();
        } else if (e.getSource() == searchButton) {
            searchStudent();
        } else if (e.getSource() == summaryButton) {
            showSummaries();
        }
    }

    private void addStudent() {
        try {
            // Validate inputs
            String idStr = idField.getText().trim();
            if (idStr.isEmpty()) {
                errorLabel.setText("ID cannot be empty!");
                return;
            }
            int id = Integer.parseInt(idStr);
            if (studentMap.containsKey(id)) {
                errorLabel.setText("ID already exists!");
                return;
            }

            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("Name cannot be empty!");
                return;
            }

            String course = courseField.getText().trim();
            if (course.isEmpty()) {
                errorLabel.setText("Course cannot be empty!");
                return;
            }

            String gradeStr = gradeField.getText().trim();
            if (gradeStr.isEmpty()) {
                errorLabel.setText("Grade cannot be empty!");
                return;
            }
            double grade = Double.parseDouble(gradeStr);

            // Create and add student
            Student student = new Student(id, name);
            student.addResult(course, grade);
            students.add(student);
            studentMap.put(id, student);

            errorLabel.setText("Student added successfully!");
            clearFields();
            viewAll();
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid ID or grade! Use numbers.");
        } catch (Exception ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    private void updateStudent() {
        try {
            String idStr = idField.getText().trim();
            if (idStr.isEmpty()) {
                errorLabel.setText("Enter ID to update!");
                return;
            }
            int id = Integer.parseInt(idStr);
            Student student = studentMap.get(id);
            if (student == null) {
                errorLabel.setText("Student not found!");
                return;
            }

            String newName = nameField.getText().trim();
            if (!newName.isEmpty()) {
                student.setName(newName);
            }

            String course = courseField.getText().trim();
            String gradeStr = gradeField.getText().trim();
            if (!course.isEmpty() && !gradeStr.isEmpty()) {
                double grade = Double.parseDouble(gradeStr);
                student.getResults().clear();
                student.addResult(course, grade);
            }

            errorLabel.setText("Student updated successfully!");
            clearFields();
            viewAll();
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid ID or grade! Use numbers.");
        } catch (Exception ex) {
            errorLabel.setText("Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        gradeField.setText("");
    }

    private void viewAll() {
        listModel.clear();
        for (Student s : students) {
            listModel.addElement(String.format("ID: %-8d Name: %-20s CGPA: %.2f", s.getId(), s.getName(), s.getCGPA()));
            for (Result r : s.getResults()) {
                listModel.addElement(String.format("    Course: %-15s Grade: %.2f", r.getCourse(), r.getGrade()));
            }
        }
    }

    // Bubble sort by name
    private void sortByName() {
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = 0; j < students.size() - i - 1; j++) {
                if (students.get(j).getName().compareTo(students.get(j + 1).getName()) > 0) {
                    Student temp = students.get(j);
                    students.set(j, students.get(j + 1));
                    students.set(j + 1, temp);
                }
            }
        }
    }

    // Bubble sort by ID
    private void sortById() {
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = 0; j < students.size() - i - 1; j++) {
                if (students.get(j).getId() > students.get(j + 1).getId()) {
                    Student temp = students.get(j);
                    students.set(j, students.get(j + 1));
                    students.set(j + 1, temp);
                }
            }
        }
    }

    // Merge sort by CGPA
    private void sortByCgpa() {
        if (students.isEmpty()) return;
        ArrayList<Student> temp = new ArrayList<>(students.size());
        for (int i = 0; i < students.size(); i++) {
            temp.add(null);
        }
        mergeSort(0, students.size() - 1, temp);
    }

    private void mergeSort(int low, int high, ArrayList<Student> temp) {
        if (low >= high) return;
        int mid = (low + high) / 2;
        mergeSort(low, mid, temp);
        mergeSort(mid + 1, high, temp);
        merge(low, mid, high, temp);
    }

    private void merge(int low, int mid, int high, ArrayList<Student> temp) {
        for (int i = low; i <= high; i++) {
            temp.set(i, students.get(i));
        }
        int i = low;
        int j = mid + 1;
        int k = low;
        while (i <= mid && j <= high) {
            if (temp.get(i).getCGPA() <= temp.get(j).getCGPA()) {
                students.set(k++, temp.get(i++));
            } else {
                students.set(k++, temp.get(j++));
            }
        }
        while (i <= mid) {
            students.set(k++, temp.get(i++));
        }
    }

    private void searchStudent() {
        try {
            String[] options = {"Linear (Name)", "Binary (ID)"};
            String choice = (String) JOptionPane.showInputDialog(this, "Search by:", "Search Student",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == null) return;

            if (choice.equals("Linear (Name)")) {
                String name = JOptionPane.showInputDialog("Enter name to search:");
                if (name == null) return;
                listModel.clear();
                boolean found = false;
                for (Student s : students) {
                    if (s.getName().equalsIgnoreCase(name)) {
                        listModel.addElement(String.format("ID: %-8d Name: %-20s CGPA: %.2f", s.getId(), s.getName(), s.getCGPA()));
                        for (Result r : s.getResults()) {
                            listModel.addElement(String.format("    Course: %-15s Grade: %.2f", r.getCourse(), r.getGrade()));
                        }
                        found = true;
                    }
                }
                if (!found) {
                    errorLabel.setText("Student not found!");
                }
            } else if (choice.equals("Binary (ID)")) {
                sortById();
                String idStr = JOptionPane.showInputDialog("Enter ID to search:");
                if (idStr == null) return;
                int id = Integer.parseInt(idStr);

                int low = 0;
                int high = students.size() - 1;
                boolean found = false;
                listModel.clear();
                while (low <= high) {
                    int mid = (low + high) / 2;
                    Student midStudent = students.get(mid);
                    if (midStudent.getId() == id) {
                        listModel.addElement(String.format("ID: %-8d Name: %-20s CGPA: %.2f", midStudent.getId(), midStudent.getName(), midStudent.getCGPA()));
                        for (Result r : midStudent.getResults()) {
                            listModel.addElement(String.format("    Course: %-15s Grade: %.2f", r.getCourse(), r.getGrade()));
                        }
                        found = true;
                        break;
                    } else if (midStudent.getId() < id) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                if (!found) {
                    errorLabel.setText("Student not found!");
                }
            }
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid ID! Use numbers.");
        }
    }

    private void showSummaries() {
        if (students.isEmpty()) {
            listModel.clear();
            listModel.addElement("No students!");
            return;
        }
        double totalCgpa = 0.0;
        Student top = students.get(0);
        for (Student s : students) {
            totalCgpa += s.getCGPA();
            if (s.getCGPA() > top.getCGPA()) {
                top = s;
            }
        }
        double average = totalCgpa / students.size();

        listModel.clear();
        listModel.addElement(String.format("Class Average CGPA: %.2f", average));
        listModel.addElement(String.format("Top Performer: ID %-8d Name: %-20s CGPA: %.2f", top.getId(), top.getName(), top.getCGPA()));
    }

}