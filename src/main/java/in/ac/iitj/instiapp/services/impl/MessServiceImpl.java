package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.MessRepository;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import in.ac.iitj.instiapp.mappers.Scheduling.MessMenu.MessDtoMapper;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;
import in.ac.iitj.instiapp.services.MessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessServiceImpl implements MessService {

    private final MessRepository messRepository;

    @Autowired
    public MessServiceImpl(MessRepository messRepository) {
        this.messRepository = messRepository;
    }

    @Override
    public void saveMessMenu(MessMenuDto menu) {
        if(menu.getYear() == null || menu.getMonth() == null || menu.getDay() == null) {
            throw new DataIntegrityViolationException(" year month and day are mandatory fields");
        }
        else if (menu.getMenuItemBreakfast() == null || menu.getMenuItemLunch() == null || menu.getMenuItemSnacks() == null || menu.getMenuItemDinner() == null) {
            throw new DataIntegrityViolationException("Data for all the breakfast lunch snacks and dinner should be given");

        }
        else {
            MessMenu menuEntity = MessDtoMapper.INSTANCE.dtoToMessMenu(menu);
            messRepository.saveMessMenu(menuEntity);
        }

    }

    @Override
    public List<MessMenuDto> getMessMenu(Integer year, Integer month) {
        if(year == null || month == null){
            throw new DataIntegrityViolationException("year and month are mandatory fields");
        }
        else {
            return messRepository.getMessMenu(year, month);
        }
    }

    @Override
    public boolean messMenuExists(Integer year, Integer month, Integer day) {
        return messRepository.messMenuExists(year, month, day);
    }

    @Override
    public void updateMessMenu(MessMenuDto menu) {
        if(menu.getYear() == null || menu.getMonth() == null || menu.getDay() == null) {
            throw new DataIntegrityViolationException(" year month and day are mandatory fields");
        }
        else if (menu.getMenuItemBreakfast() == null || menu.getMenuItemLunch() == null || menu.getMenuItemSnacks() == null || menu.getMenuItemDinner() == null) {
            throw new DataIntegrityViolationException("Data for all the breakfast lunch snacks and dinner should be given");

        }
        else {
            messRepository.updateMessMenu(menu.getYear(), menu.getMonth(), menu.getDay(), new MenuItem(menu.getMenuItemBreakfast(), menu.getMenuItemLunch(), menu.getMenuItemSnacks() ,menu.getMenuItemDinner()));
        }
    }

    @Override
    public void deleteMessMenu(Integer year, Integer month, Integer day) {
        if(year == null || month == null || day == null) {
            throw new DataIntegrityViolationException("year and month are mandatory fields");
        }
        else {
            messRepository.deleteMessMenu(year, month, day);
        }
    }

    @Override
    public void saveOverrideMessMenu(MenuOverrideDto menuOverride) {
        if(menuOverride.getDate() == null) {
            throw new DataIntegrityViolationException("date is required");
        }
        else if(menuOverride.getMenuItemBreakfast() == null && menuOverride.getMenuItemLunch() == null && menuOverride.getMenuItemSnacks() == null && menuOverride.getMenuItemDinner() == null)  {
            throw new DataIntegrityViolationException("At least one of the meal menu is required");
        }
        else {
            MenuOverride menu = MessDtoMapper.INSTANCE.menuOverrideDtoToMenuOverride(menuOverride);
            messRepository.saveOverrideMessMenu(menu);
        }
    }

    @Override
    public MenuOverrideDto getOverrideMessMenu(Date date) {
        if( date == null){
            throw new DataIntegrityViolationException("Date Must Not be Null");
        }
        else {
            return messRepository.getOverrideMessMenu(date);
        }
    }

    @Override
    public boolean menuOverrideExists(Date date) {
        return messRepository.menuOverrideExists(date);
    }

    @Override
    public void deleteOverrideMessMenu(Date date) {
        if(date == null){
            throw new DataIntegrityViolationException("Date Must Not be Null");
        }
        else {
            messRepository.deleteOverrideMessMenu(date);
        }
    }

    @Override
    public void updateOverrideMessMenu(MenuOverrideDto menuOverride) {
        if (menuOverride.getDate() == null) {
            throw new DataIntegrityViolationException("date is required");
        } else if (menuOverride.getMenuItemBreakfast() == null && menuOverride.getMenuItemLunch() == null && menuOverride.getMenuItemSnacks() == null && menuOverride.getMenuItemDinner() == null) {
            throw new DataIntegrityViolationException("At least one of the meal menu is required");
        } else {
            messRepository.updateOverrideMessMenu(new MenuItem(menuOverride.getMenuItemBreakfast(), menuOverride.getMenuItemLunch(), menuOverride.getMenuItemSnacks(), menuOverride.getMenuItemDinner()), menuOverride.getDate());
        }
    }
}