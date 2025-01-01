package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;

public enum StudentData {
    STUDENT_DATA1(2023, null, null, null),
    STUDENT_DATA2(2022, null, null, null);

    private final Integer admissionYear;
    private User user;
    private StudentProgram program;
    private StudentBranch branch;

    private StudentData(Integer admissionYear, User user, StudentProgram program, StudentBranch branch) {
        this.admissionYear = admissionYear;
        this.user = user;
        this.program = program;
        this.branch = branch;
    }

    public Student toEntity() {
        return new Student(null, user, program, branch, admissionYear);
    }

    // Setters for relationships that need to be set before creating the entity
    public StudentData setUser(User user) {
        this.user = user;
        return this;
    }

    public StudentData setProgram(StudentProgram program) {
        this.program = program;
        return this;
    }

    public StudentData setBranch(StudentBranch branch) {
        this.branch = branch;
        return this;
    }

    // Getters for testing purposes
    public Integer getAdmissionYear() {
        return admissionYear;
    }

    public User getUser() {
        return user;
    }

    public StudentProgram getProgram() {
        return program;
    }

    public StudentBranch getBranch() {
        return branch;
    }
}