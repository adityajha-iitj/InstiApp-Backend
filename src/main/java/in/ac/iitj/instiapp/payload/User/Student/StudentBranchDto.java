package in.ac.iitj.instiapp.payload.User.Student;

import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch}
 */
@Value
public class StudentBranchDto implements Serializable {
    String name;
    OrganisationBaseDto organisation;
    Integer openingYear;
    Integer closingYear;


    public StudentBranchDto(String name){
        this.name = name;
        this.openingYear = null;
        this.closingYear = null;
        this.organisation = null;
    }

    public StudentBranchDto(String name, String organisationUsername, Integer openingYear, Integer closingYear){
        this.name = name;
        this.organisation = new OrganisationBaseDto(organisationUsername);
        this.openingYear = openingYear;
        this.closingYear = closingYear;

    }
}