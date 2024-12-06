package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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


    public boolean messMenuExists(int year , int month) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM mess_menu  WHERE year = ? AND month = ?)", Boolean.class, year , month));
    }

    public boolean menuOverrideExists(Date date) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM menu_override  WHERE date = ? )", Boolean.class, date));
    }

    @Transactional
    public void saveMessMenu(MessMenu messMenu) {

        if (!messMenuExists(messMenu.getYear(), messMenu.getMonth())) {
            throw new DataIntegrityViolationException(" Mess Menu for this month already exists");
        }
        entityManager.persist(messMenu);

    }


    public List<MessMenu> getMessMenu(int year, int month) {
        // Define the native SQL query
        String sql = "SELECT * FROM mess_menu WHERE year = :year AND month = :month";
            // Create the query using EntityManager
            Query query = entityManager.createNativeQuery(sql, MessMenu.class);
            query.setParameter("year", year);
            query.setParameter("month", month);

        return  query.getResultList();
    }


    public void saveOverrideMessMenu(MenuOverride menuOverride) {
        if( menuOverrideExists(menuOverride.getDate()) ) {
            throw new DataIntegrityViolationException(" Mess Menu for this override already exists");
        }
        entityManager.persist(menuOverride);
    }


    public MenuOverride getOverrideMessMenu(Date date) {
        String sql = "SELECT * FROM override_menu WHERE date = :date";
        Query query = entityManager.createNativeQuery(sql, MenuOverride.class);
        query.setParameter("date", date);
        return (MenuOverride) query.getSingleResult();
    }


    public void deleteMessMenu(int year, int month) {
        jdbcTemplate.update("delete from mess_menu where year=? and month=?", year , month);
    }


    public void deleteOverrideMessMenu(Date date) {
        jdbcTemplate.update("delete from override_menu where date = ?", date);
    }


    public void updateMessMenu(int year, int month , int day , MenuItem menuItem) {

        Boolean exists = entityManager.createQuery(
                        "SELECT (COUNT(m) > 0) " +
                                "FROM MessMenu m WHERE m.year = :year AND m.month = :month AND m.day = :day",
                        Boolean.class)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("day", day)
                .getSingleResult();

        if(!exists){
            throw new DataIntegrityViolationException("mess menu for year" + year + "month" + month + "day" + day + " doesn't exists");
        }

        Long Id = entityManager.createQuery("select menu.id from mess_menu  menu where menu.year = :year and menu.month = :month and menu.day = :day",Long.class)
                .setParameter("year",year)
                .setParameter("month",month)
                .setParameter("day", day).getSingleResult();

        MessMenu menu_new= entityManager.getReference(MessMenu.class, Id);
        menu_new.setMenuItem(menuItem);
        entityManager.merge(menu_new);
    }


    public void updateOverrideMessMenu(MenuOverride menuOverride) {
        Boolean exists = entityManager.createQuery("SELECT EXISTS(SELECT 1 FROM MenuOverride mo WHERE mo.date = :date)", Boolean.class)
                .setParameter("date", menuOverride.getDate())
                .getSingleResult();

        if (!exists) {
            throw new DataIntegrityViolationException("override menu for date" + menuOverride.getDate() + " doesn't exists");
        } else {
            Long Id = entityManager.createQuery("select menu.id from menu_override menu where menu.date = :date" , Long.class)
                    .setParameter("date" , menuOverride.getDate())
                    .getSingleResult();

            MenuOverride menu_new = entityManager.getReference(MenuOverride.class, Id);
            menu_new.setMenuItem(menuOverride.getMenuItem());
            entityManager.merge(menu_new);
        }
    }


}


