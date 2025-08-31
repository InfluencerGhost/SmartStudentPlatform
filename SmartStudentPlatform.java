
import smartstudentplatform.Student;
import smartstudentplatform.Result;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class SmartStudentPlatform extends JFrame implements ActionListener {

    // Data structures
    private ArrayList<Student> students = new ArrayList<>();
    private HashMap<Integer, Student> studentMap = new HashMap<>();

    // GUI components
    private JTable studentTable;
    private StudentTableModel tableModel;
    private JLabel totalLabel;
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

    inputPanel.add(new JLabel("Courses:"));
    courseField = new JTextField();
    inputPanel.add(courseField);

    inputPanel.add(new JLabel("CGPA:"));
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


    // Display area (center) with JTable
    tableModel = new StudentTableModel();
    studentTable = new JTable(tableModel);
    studentTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
    studentTable.setRowHeight(22);
    JScrollPane scrollPane = new JScrollPane(studentTable);
    add(scrollPane, BorderLayout.CENTER);

    // Total students label (bottom)
    totalLabel = new JLabel("Total Students: 0");
    totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
    add(totalLabel, BorderLayout.SOUTH);

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
            updateTable();
        } else if (e.getSource() == sortNameButton) {
            sortByName();
            updateTable();
        } else if (e.getSource() == sortIdButton) {
            sortById();
            updateTable();
        } else if (e.getSource() == sortCgpaButton) {
            sortByCgpa();
            updateTable();
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

            String courses = courseField.getText().trim();
            if (courses.isEmpty()) {
                errorLabel.setText("Courses cannot be empty!");
                return;
            }

            String cgpaStr = gradeField.getText().trim();
            if (cgpaStr.isEmpty()) {
                errorLabel.setText("CGPA cannot be empty!");
                return;
            }
            double cgpa = Double.parseDouble(cgpaStr);
            if (cgpa < 0.0 || cgpa > 5.0) {
                errorLabel.setText("CGPA must be between 0.0 and 5.0!");
                return;
            }

            Student student = new Student(id, name);
            student.getResults().clear();
            // Add each course with the same CGPA (since only one CGPA is provided)
            String[] courseArr = courses.split(",");
            for (String course : courseArr) {
                student.addResult(course.trim(), cgpa);
            }
            students.add(student);
            studentMap.put(id, student);

            errorLabel.setText("Student added successfully!");
            clearFields();
            updateTable();
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid ID or CGPA! Use numbers.");
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

            String courses = courseField.getText().trim();
            String cgpaStr = gradeField.getText().trim();
            if (!courses.isEmpty() && !cgpaStr.isEmpty()) {
                double cgpa = Double.parseDouble(cgpaStr);
                if (cgpa < 0.0 || cgpa > 5.0) {
                    errorLabel.setText("CGPA must be between 0.0 and 5.0!");
                    return;
                }
                student.getResults().clear();
                String[] courseArr = courses.split(",");
                for (String course : courseArr) {
                    student.addResult(course.trim(), cgpa);
                }
            }

            errorLabel.setText("Student updated successfully!");
            clearFields();
            updateTable();
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid ID or CGPA! Use numbers.");
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


    private void updateTable() {
        tableModel.setStudents(students);
        totalLabel.setText("Total Students: " + students.size());
    }

    // Quicksort by name
    private void sortByName() {
        String[] options = {"Ascending", "Descending"};
        int choice = JOptionPane.showOptionDialog(this, "Sort by Name:", "Sort Order",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        boolean ascending = (choice == 0);
        quickSortByName(0, students.size() - 1, ascending);
    }

    private void quickSortByName(int low, int high, boolean ascending) {
        if (low < high) {
            int pi = partitionByName(low, high, ascending);
            quickSortByName(low, pi - 1, ascending);
            quickSortByName(pi + 1, high, ascending);
        }
    }

    private int partitionByName(int low, int high, boolean ascending) {
        String pivot = students.get(high).getName();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            int cmp = students.get(j).getName().compareToIgnoreCase(pivot);
            if ((ascending && cmp <= 0) || (!ascending && cmp >= 0)) {
                i++;
                Collections.swap(students, i, j);
            }
        }
        Collections.swap(students, i + 1, high);
        return i + 1;
    }

    // Bubble sort by ID
    private void sortById() {
        String[] options = {"Ascending", "Descending"};
        int choice = JOptionPane.showOptionDialog(this, "Sort by ID:", "Sort Order",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        boolean ascending = (choice == 0);
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = 0; j < students.size() - i - 1; j++) {
                if ((ascending && students.get(j).getId() > students.get(j + 1).getId()) ||
                    (!ascending && students.get(j).getId() < students.get(j + 1).getId())) {
                    Student temp = students.get(j);
                    students.set(j, students.get(j + 1));
                    students.set(j + 1, temp);
                }
            }
        }
    }

    // Bubble sort by CGPA
    private void sortByCgpa() {
        String[] options = {"Ascending", "Descending"};
        int choice = JOptionPane.showOptionDialog(this, "Sort by CGPA:", "Sort Order",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        boolean ascending = (choice == 0);
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = 0; j < students.size() - i - 1; j++) {
                if ((ascending && students.get(j).getCGPA() > students.get(j + 1).getCGPA()) ||
                    (!ascending && students.get(j).getCGPA() < students.get(j + 1).getCGPA())) {
                    Collections.swap(students, j, j + 1);
                }
            }
        }
    }

    private void searchStudent() {
        try {
            String[] options = {"Linear (Name)", "Binary (ID)"};
            String choice = (String) JOptionPane.showInputDialog(this, "Search by:", "Search Student",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == null) return;

            ArrayList<Student> foundStudents = new ArrayList<>();
            if (choice.equals("Linear (Name)")) {
                String name = JOptionPane.showInputDialog("Enter name to search:");
                if (name == null) return;
                for (Student s : students) {
                    if (s.getName().equalsIgnoreCase(name)) {
                        foundStudents.add(s);
                    }
                }
                if (foundStudents.isEmpty()) {
                    errorLabel.setText("Student not found!");
                }
            } else if (choice.equals("Binary (ID)")) {
                sortById();
                String idStr = JOptionPane.showInputDialog("Enter ID to search:");
                if (idStr == null) return;
                int id = Integer.parseInt(idStr);
                int low = 0;
                int high = students.size() - 1;
                while (low <= high) {
                    int mid = (low + high) / 2;
                    Student midStudent = students.get(mid);
                    if (midStudent.getId() == id) {
                        foundStudents.add(midStudent);
                        break;
                    } else if (midStudent.getId() < id) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                if (foundStudents.isEmpty()) {
                    errorLabel.setText("Student not found!");
                }
            }
            tableModel.setStudents(foundStudents);
            totalLabel.setText("Total Students: " + foundStudents.size());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid ID! Use numbers.");
        }
    }

    private void showSummaries() {
        if (students.isEmpty()) {
            tableModel.setStudents(new ArrayList<>());
            totalLabel.setText("No students!");
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
    tableModel.setStudents(new ArrayList<>(Arrays.asList(top)));
        totalLabel.setText(String.format("Class Average CGPA: %.2f | Top Performer: %s (ID: %d, CGPA: %.2f)", average, top.getName(), top.getId(), top.getCGPA()));
    }

    // Table model for JTable
    class StudentTableModel extends AbstractTableModel {
    private String[] columns = {"ID", "Name", "Courses", "CGPA"};
        private ArrayList<Student> data = new ArrayList<>();

        public void setStudents(ArrayList<Student> students) {
            data = new ArrayList<>(students);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Student s = data.get(row);
            switch (col) {
                case 0: return s.getId();
                case 1: return s.getName();
                case 2:
                    StringBuilder sb = new StringBuilder();
                    for (Result r : s.getResults()) {
                        sb.append(r.getCourse()).append(", ");
                    }
                    if (sb.length() > 2) sb.setLength(sb.length() - 2); // remove last comma
                    return sb.toString();
                case 3: return String.format("%.2f", s.getCGPA());
                default: return "";
            }
        }
    }
}