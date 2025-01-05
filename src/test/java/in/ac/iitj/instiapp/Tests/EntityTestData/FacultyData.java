package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;

public enum FacultyData {

    FACULTY1("Professor at the Dept of CSE", "cse@gmail.com"),
    FACULTY2("Professor at the Dept of EE", "ee@gmail.com"),
    FACULTY3("Professor at the Dept of ES", "es@gmail.com"),
    FACULTY4("Professor at the Dept of AIDE", "aide@gmail.com");


    public final String description;
    public final String websiteUrl;


    FacultyData(String description, String websiteUrl) {
        this.description = description;
        this.websiteUrl = websiteUrl;
    }

    public Faculty toEntity() {
         return new Faculty(null, null, this.description,this.websiteUrl, null);
    }

}
