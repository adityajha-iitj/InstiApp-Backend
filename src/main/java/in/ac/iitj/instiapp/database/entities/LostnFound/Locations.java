package in.ac.iitj.instiapp.database.entities.LostnFound;

import jakarta.persistence.*;


@Entity
public class Locations {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long Id;

    @Column(nullable = false)
    Long Name;
}
