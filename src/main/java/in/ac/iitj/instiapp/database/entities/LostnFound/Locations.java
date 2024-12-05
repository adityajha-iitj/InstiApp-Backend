package in.ac.iitj.instiapp.database.entities.LostnFound;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Locations {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    Long Id;

    @Column(nullable = false)
    String Name;
}
