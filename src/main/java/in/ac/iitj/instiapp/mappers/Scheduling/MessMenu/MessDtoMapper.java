package in.ac.iitj.instiapp.mappers.Scheduling.MessMenu;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessDtoMapper {

    MessDtoMapper INSTANCE = Mappers.getMapper(MessDtoMapper.class);

    @Mapping(target = "menuItemBreakfast", expression = "java(messMenu.getMenuItem().getBreakfast())")
    @Mapping(target = "menuItemLunch", expression = "java(messMenu.getMenuItem().getLunch())")
    @Mapping(target = "menuItemSnacks", expression = "java(messMenu.getMenuItem().getSnacks())")
    @Mapping(target = "menuItemDinner", expression = "java(messMenu.getMenuItem().getDinner())")
    @Mapping( target = "year" , expression = "java(messMenu.getYear())")
    @Mapping( target = "month" , expression = "java(messMenu.getMonth())")
    @Mapping( target = "day" , expression = "java(messMenu.getDay())")
    MessMenuDto messMenuToDto(MessMenu messMenu);


    @Mapping(target = "menuItem", expression = "java(new in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem(dto.getMenuItemBreakfast(), dto.getMenuItemLunch(), dto.getMenuItemSnacks(), dto.getMenuItemDinner()))")
    MessMenu dtoToMessMenu(MessMenuDto dto);

    @Mapping( target = "menuItemBreakfast" ,expression = "java(menuOverride.getMenuItem().getBreakfast())")
    @Mapping(target = "menuItemLunch" ,expression = "java(menuOverride.getMenuItem().getLunch())")
    @Mapping( target = "menuItemSnacks" ,expression = "java(menuOverride.getMenuItem().getSnacks())")
    @Mapping(target = "menuItemDinner",expression = "java(menuOverride.getMenuItem().getDinner())")
    @Mapping(target = "date" ,expression = "java(menuOverride.getDate())")
    MenuOverrideDto menuOverrideToMenuOverrideDto(MenuOverride menuOverride);

    @Mapping(target = "menuItem", expression = "java(new in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem(menuOverrideDto.getMenuItemBreakfast(), menuOverrideDto.getMenuItemLunch(), menuOverrideDto.getMenuItemSnacks(), menuOverrideDto.getMenuItemDinner()))")
    MenuOverride menuOverrideDtoToMenuOverride(MenuOverrideDto menuOverrideDto);

}
