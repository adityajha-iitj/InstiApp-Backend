package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.User;

public enum UserData {

    USER1("John Doe", "jdoe", "jdoe@example.com", "123-456-7890"),
    USER2("Jane Smith", "jsmith", "jsmith@example.com", "987-654-3210"),
    USER3("Alice Johnson", "ajohnson", "alicej@example.com", "555-123-4567"),
    USER4("Bob Brown", "bbrown", "bob@example.com", "444-987-6543");

    public final String name;
    public final String userName;
    public final String email;
    public final String phoneNumber;




    UserData(String name, String userName, String email, String phoneNumber) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public User toEntity(){
        return  new User(this.name,this.userName,this.email,this.phoneNumber);
    }


}
