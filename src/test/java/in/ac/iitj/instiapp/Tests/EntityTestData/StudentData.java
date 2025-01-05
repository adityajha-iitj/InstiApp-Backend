package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import lombok.Getter;


@Getter
public enum StudentData {
    STUDENT1(2023),
    STUDENT2(2022),
    STUDENT3(2021),
    STUDENT4(2020);

    public final Integer admissionYear;


     StudentData(Integer admissionYear) {
        this.admissionYear = admissionYear;

    }

    public Student toEntity() {
         return new Student(null,null,null,null,this.admissionYear);
    }


}