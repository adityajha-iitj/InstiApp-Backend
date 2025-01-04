package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.User;

public enum UserData {

    USER1("John Doe", "jdoe", "jdoe@example.com", "123-456-7890", "AVATAR1.com"),
    USER2("Jane Smith", "jsmith", "jsmith@example.com", "987-654-3210","AVATAR2.com"),
    USER3("Alice Johnson", "ajohnson", "alicej@example.com", "555-123-4567","AVATAR3.com"),
    USER4("Bob Brown", "bbrown", "bob@example.com", "444-987-6543","AVATAR4.com");

    public final String name;
    public final String userName;
    public final String email;
    public final String phoneNumber;
    public final String avatarUrl;




    UserData(String name, String userName, String email, String phoneNumber, String avatarUrl) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
    }


    public User toEntity(){
        return  new User(this.name,this.userName,this.email,this.phoneNumber, this.avatarUrl);
    }


}
