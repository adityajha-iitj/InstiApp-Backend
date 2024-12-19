package in.ac.iitj.instiapp.payload.Scheduling.MessMenu;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link MenuOverride}
 */
@Value
public class MenuOverrideDto implements Serializable {
    Date date;
    String menuItemBreakfast;
    String menuItemLunch;
    String menuItemSnacks;
    String menuItemDinner;
}