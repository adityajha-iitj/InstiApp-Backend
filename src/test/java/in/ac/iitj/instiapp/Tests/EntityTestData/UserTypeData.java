package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Usertype;
import org.hibernate.usertype.UserType;

public enum UserTypeData {

    USER_TYPE1("Student"),
    USER_TYPE2("Faculty"),
    USER_TYPE3("Staff"),
    USER_TYPE4("Organization"), // not in database
    USER_TYPE5("Department"),
    USER_TYPE6("Office"),
    USER_TYPE7("Hostel")
    ; // Not in database





    public final String name;



    UserTypeData(String name) {
        this.name = name;
    }


    public Usertype toEntity() {
        return  new Usertype(null, this.name);
    }
}
