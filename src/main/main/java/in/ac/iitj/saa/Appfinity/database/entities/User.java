package in.ac.iitj.saa.Appfinity.database.entities;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String name;

    @Column(nullable = false)
    String userName;

    @Column( nullable = false)
    String email;

    @Column( nullable = false)
    String password;

    @Column( nullable = false)
    String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "User_type_id", nullable = false)
    Usertype userType;

    @OneToOne
    @JoinColumn(name = "Calendar_id", nullable = false)
    Calendar calendar;

    @OneToOne
    @JoinColumn(nullable = true)
    Media avatar;
}
