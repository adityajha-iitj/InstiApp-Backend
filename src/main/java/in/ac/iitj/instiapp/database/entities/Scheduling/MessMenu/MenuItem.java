package in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
@Getter
@Setter
@Embeddable
public class MenuItem {


    private String breakfast;
    private String lunch;
    private String snacks;
    private String dinner;


}
