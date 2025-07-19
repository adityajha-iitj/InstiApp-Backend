package in.ac.iitj.instiapp.database.entities.User;


import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Calendar;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User{

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String userName;

    @Column(nullable = false)
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;

    @Column(nullable = true)
    String phoneNumber;

    @ManyToOne
    Usertype userType;

    @OneToOne
    @JoinColumn(nullable = true)
    Calendar calendar;

    @Column(nullable = true)
    String avatarUrl;

    @OneToMany
    Set<OrganisationRole> organisationRoleSet;

    @Column(unique = true)
    private String googleId;

    public  User(String name, String userName, String email, String phoneNumber, String avatarUrl){
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
    }

    public User(Long userId) {
        this.Id = userId;
    }

    public User(String userName) {
        this.userName = userName;
    }
}

