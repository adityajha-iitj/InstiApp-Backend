package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.MessRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import in.ac.iitj.instiapp.services.MessService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void saveMessMenu(MessMenu menu) {
        messRepository.saveMessMenu(menu);
    }

    @Override
    public void saveOverrideMessMenu(MenuOverride menuOverride) {
        messRepository.saveOverrideMessMenu(menuOverride);
    }

    @Override
    public List<MessMenu> getMessMenu(int year, int month) {
        return messRepository.getMessMenu(year, month);
    }

    @Override
    public MenuOverride getOverrideMessMenu(Date date) {
        return messRepository.getOverrideMessMenu(date);
    }

    @Override
    public boolean messMenuExists(int year, int month, int day) {
        return messRepository.messMenuExists(year, month, day);
    }

    @Override
    public boolean menuOverrideExists(Date date) {
        return messRepository.menuOverrideExists(date);
    }

    @Override
    public void deleteMessMenu(int year, int month, int day) {
        messRepository.deleteMessMenu(year, month, day);
    }

    @Override
    public void deleteOverrideMessMenu(Date date) {
        messRepository.deleteOverrideMessMenu(date);
    }

    @Override
    public void updateMessMenu(int year, int month, int day, MenuItem menuItem) {
        messRepository.updateMessMenu(year, month, day, menuItem);
    }

    @Override
    public void updateOverrideMessMenu(MenuItem menuItem, Date date) {
        messRepository.updateOverrideMessMenu(menuItem, date);
    }
}