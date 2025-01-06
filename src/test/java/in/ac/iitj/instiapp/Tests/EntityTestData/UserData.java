package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.User;

public enum UserData {

    USER1("John Doe", "jdoe", "jdoe@example.com", "123-456-7890", "AVATAR1.com"),
    USER2("Jane Smith", "jsmith", "jsmith@example.com", "987-654-3210","AVATAR2.com"),
    USER3("Alice Johnson", "ajohnson", "alicej@example.com", "555-123-4567","AVATAR3.com"),
    USER4("Bob Brown", "bbrown", "bob@example.com", "444-987-6543","AVATAR4.com"),
    USER5("tanmay", "tdaga", "tdaga@gmail.com","999-888-8888","AVATAR5.com"),
    USER6("nayan", "nkute","nkute@gmail.com","888-9999-9999-1111","AVATAR6.com"),
    USER7("adityajha","ajha","ajha@gmail.com","111-666-8899-09887","AVATAR7.com"),
    USER8("rahul", "rsharma", "rsharma@gmail.com", "777-555-4444", "AVATAR8.com"),
    USER9("sneha", "skulkarni", "skulkarni@gmail.com", "666-777-3333", "AVATAR9.com"),
    USER10("ankita", "agupta", "agupta@gmail.com", "555-666-2222", "AVATAR10.com"),
    USER11("Danie", "DanieGJ", "daniegeorgejohn@gmail.com", "555-123-2222", "AVATAR11.com"),
    USER12("George", "GeorgeDJ", "george@gmail.com", "555-345-2222", "AVATAR12.com"),
    USER13("John", "JohnDG", "john@gmail.com", "555-567-2222", "AVATAR13.com"),
    USER14("Alice", "AliceWD", "alice@gmail.com", "555-789-1234", "AVATAR14.com"),
    USER15("Bob", "BobXZ", "bob@gmail.com", "555-234-5678", "AVATAR15.com"),
    USER16("Eve", "EveQK", "eve@gmail.com", "555-876-4321", "AVATAR16.com");



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
