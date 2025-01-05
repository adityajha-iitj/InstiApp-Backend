package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;

public enum StudentBranchData {
    STUDENT_BRANCH1("BRANCH1" , 2008 , null ),
    STUDENT_BRANCH2("BRANCH2" , 2008 , 2018 ),
    STUDENT_BRANCH3("BRANCH3" , 2008 , 2019 ),
    STUDENT_BRANCH4("BRANCH4", 2007,2020);

    public final String name;
    public  final Integer openingYear;
    public  final Integer closingYear;

    StudentBranchData(String name, Integer openingYear, Integer closingYear) {
        this.name = name;
        this.openingYear = openingYear;
        this.closingYear = closingYear;
    }

    public StudentBranch toEntity() {
        return new StudentBranch(this.name , this.openingYear , this.closingYear, null);
    }
}
