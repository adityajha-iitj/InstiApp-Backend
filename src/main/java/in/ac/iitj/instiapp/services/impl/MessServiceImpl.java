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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void saveMessMenu(MessMenuDto menu) {
        MessMenu menuEntity = MessDtoMapper.INSTANCE.dtoToMessMenu(menu);
        messRepository.saveMessMenu( menuEntity );

    }

    @Override
    public List<MessMenuDto> getMessMenu(int year, int month) {
        return messRepository.getMessMenu(year, month);
    }

    @Override
    public boolean messMenuExists(int year, int month, int day) {
        return messRepository.messMenuExists(year, month, day);
    }

    @Override
    public void updateMessMenu(MessMenuDto messMenuDto) {
        messRepository.updateMessMenu(messMenuDto.getYear(),
                messMenuDto.getMonth(),
                messMenuDto.getDay(),
                new MenuItem(
                        messMenuDto.getMenuItemBreakfast(),
                        messMenuDto.getMenuItemLunch(),
                        messMenuDto.getMenuItemSnacks(),
                        messMenuDto.getMenuItemDinner()
                )
                );

    }

    @Override
    public void deleteMessMenu(int year, int month, int day) {
        messRepository.deleteMessMenu(year, month, day);
    }

    @Override
    public void saveOverrideMessMenu(MenuOverrideDto menuOverride) {
        MenuOverride menu = MessDtoMapper.INSTANCE.menuOverrideDtoToMenuOverride(menuOverride);
        messRepository.saveOverrideMessMenu( menu );
    }

    @Override
    public MenuOverrideDto getOverrideMessMenu(Date date) {
        return messRepository.getOverrideMessMenu(date);
    }

    @Override
    public boolean menuOverrideExists(Date date) {
        return messRepository.menuOverrideExists(date);
    }

    @Override
    public void deleteOverrideMessMenu(Date date) {
        messRepository.deleteOverrideMessMenu(date);
    }

    @Override
    public void updateOverrideMessMenu(MenuOverrideDto menuOverrideDto) {
        messRepository.updateOverrideMessMenu(
                new MenuItem(menuOverrideDto.getMenuItemBreakfast(),
                            menuOverrideDto.getMenuItemLunch(),
                            menuOverrideDto.getMenuItemSnacks(),
                            menuOverrideDto.getMenuItemDinner()
                        )
                , menuOverrideDto.getDate());
    }
}