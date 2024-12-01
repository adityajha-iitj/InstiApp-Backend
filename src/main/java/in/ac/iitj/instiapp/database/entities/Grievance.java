package in.ac.iitj.instiapp.database.entities;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String Title;

    @Column(nullable = false)
    String Description;

    @ManyToOne
    User userFrom;


    //To be implemented using organisation
    @ManyToOne
    User userTo;


    @ManyToOne
    @JoinColumn(name = "media_id", nullable = true)
    Media media;
}
