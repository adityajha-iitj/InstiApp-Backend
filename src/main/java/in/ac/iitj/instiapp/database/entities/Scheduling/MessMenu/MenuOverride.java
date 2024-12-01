package in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public class MenuOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private MessMenu messMenu;

    private Integer day;

    private MenuItem menuItem;
}
