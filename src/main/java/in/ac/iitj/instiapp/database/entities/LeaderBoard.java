package in.ac.iitj.instiapp.database.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leaderboard")

public class LeaderBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String type;

    @Column(nullable = false)
    String event_name;

    @Column( nullable = false)
    String points;

    @Column( nullable = false)
    String team;

    @Column( nullable = false)
    Integer position;

    @Column( nullable = false)
    String chief_guest;
}
