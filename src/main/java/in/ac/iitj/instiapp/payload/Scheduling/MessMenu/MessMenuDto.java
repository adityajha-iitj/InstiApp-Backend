package in.ac.iitj.instiapp.payload.Scheduling.MessMenu;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link MessMenu}
 */
@Data
@AllArgsConstructor
public class MessMenuDto implements Serializable {

    Integer year;
    Integer month;
    Integer day;
    String menuItemBreakfast;
    String menuItemLunch;
    String menuItemSnacks;
    String menuItemDinner;
}