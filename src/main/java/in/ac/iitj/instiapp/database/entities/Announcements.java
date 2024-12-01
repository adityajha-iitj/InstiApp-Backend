package in.ac.iitj.instiapp.database.entities;

import in.ac.iitj.instiapp.database.entities.User.Groups;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Announcements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    @JoinColumn(name = "announcer", nullable = false)
    User user;

    @Column(nullable = false)
    String Title;

    @Column( nullable = true)
    String Description;

    @Column(nullable = false)
    Date dateOfAnnouncement;

    @OneToMany
    Set<Media> media;

    @OneToMany
    List<Groups> groupsList;

    @OneToMany
    List<User> users;
}
