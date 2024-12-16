package in.ac.iitj.instiapp.database.entities;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Groups;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "announcements")
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
    @JoinTable(name = "announcements_groups_list")
    Set<Groups> groupsList;

    @OneToMany
    Set<User> users;
}
