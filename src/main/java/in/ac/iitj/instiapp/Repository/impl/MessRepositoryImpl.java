package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.*;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MessRepositoryImpl implements in.ac.iitj.instiapp.Repository.MessRepository  {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;


    public MessRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }


    @Override
    public boolean messMenuExists(int year , int month , int day) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM mess_menu  WHERE year = ? AND month = ? AND day = ?)", Boolean.class, year , month , day));
    }

    @Override
    public boolean menuOverrideExists(Date date) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM menu_override  WHERE date = ? )", Boolean.class, date));
    }

    @Transactional
    @Override
    public void saveMessMenu(MessMenu messMenu) {

        if (messMenuExists(messMenu.getYear(), messMenu.getMonth(), messMenu.getDay())) {
            throw new DataIntegrityViolationException(" Mess Menu for " + messMenu.getYear() + "-" + messMenu.getMonth() + "-" + messMenu.getDay() + " already exists");
        }
            entityManager.persist(messMenu);


    }

    /*Would return empty list if no mess menu exist for specific year and month */
    @Override
    public List<MessMenuDto> getMessMenu(int year, int month) {
        List<MessMenuDto> menuDtos = entityManager.createQuery("select "+
                "new in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto"+
                "(me.year,me.month,me.day,me.menuItem.breakfast ,me.menuItem.lunch,me.menuItem.snacks,me.menuItem.dinner)"+
                "from MessMenu me where me.year = :year and me.month = :month" , MessMenuDto.class)
                .setParameter("year" , year)
                .setParameter("month" , month)
                .getResultList();
                ;
                return menuDtos;
    }


    @Transactional
    @Override
    public void saveOverrideMessMenu(MenuOverride menuOverride) {
        if(menuOverrideExists(menuOverride.getDate()) ) {
            throw new DataIntegrityViolationException(" Mess Menu for this override already exists");
        }
        entityManager.persist(menuOverride);
    }

    @Override
    public MenuOverrideDto getOverrideMessMenu(Date date) {
        if (!menuOverrideExists(date)) {
            throw new EmptyResultDataAccessException("Menu Override not found for date " + date.toString(), 1);
        }
        return (MenuOverrideDto) entityManager.createQuery("select "+
                "new in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto"+
                "(mo.date , mo.menuItem.breakfast , mo.menuItem.lunch,mo.menuItem.snacks,mo.menuItem.dinner)" +
                "from MenuOverride mo where mo.date = :date")
                .setParameter("date" , date)
                .getSingleResult();

    }

    @Transactional
    @Override
    public void deleteMessMenu(int year, int month , int day) {
        jdbcTemplate.update("delete from mess_menu where year=? and month=? and day= ?", year , month ,day);
    }

    @Override
    public void deleteOverrideMessMenu(Date date) {
        jdbcTemplate.update("delete from menu_override where date = ?", date);
    }

    @Transactional
    @Override
    public void updateMessMenu(int year, int month , int day , MenuItem menuItem) {


        if(Boolean.FALSE.equals(messMenuExists(year, month, day))) {
            throw new DataIntegrityViolationException("mess menu for year" + year + "month" + month + "day" + day + " doesn't exists");
        }

        String sql2 = "SELECT menu.id FROM mess_menu menu WHERE menu.year = ? AND menu.month = ? AND menu.day = ?";

        Long id = jdbcTemplate.queryForObject(
                sql2,
                Long.class,
                year, month, day
        );

        MessMenu menu_new= entityManager.getReference(MessMenu.class, id);
        menu_new.setMenuItem(menuItem);
        entityManager.merge(menu_new);
    }

    @Override
    public void updateOverrideMessMenu(MenuItem menuItem , Date date) {


        if (Boolean.FALSE.equals(menuOverrideExists(date))) {
            throw new DataIntegrityViolationException("Override menu for date " + date + " doesn't exist");
        } else {

            String menuIdSql = "SELECT menu.id FROM menu_override menu WHERE menu.date = ?";

            Long id = jdbcTemplate.queryForObject(
                    menuIdSql,
                    Long.class,
                    date
            );
            MenuOverride menuOverride = entityManager.getReference(MenuOverride.class, id);
            menuOverride.setMenuItem(menuItem);
            entityManager.merge(menuOverride);
        }
    }

}





