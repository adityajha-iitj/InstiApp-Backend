package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;

public enum StudentProgramData {
    STUDENT_PROGRAM1("PROGRAM1" , true),
    STUDENT_PROGRAM2("PROGRAM2" , false),
    STUDENT_PROGRAM3("PROGRAM3" , true),
    STUDENT_PROGRAM4("PROGRAM4" , false);
    public final String name;
    public final boolean isActive;

    StudentProgramData(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public StudentProgram toEntity() {
        return new StudentProgram(this.name , this.isActive);
    }
}
