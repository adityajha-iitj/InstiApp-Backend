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


    @Override
    public boolean messMenuExists(int year , int month , int day) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT id FROM mess_menu  WHERE year = ? AND month = ? AND day = ?)", Boolean.class, year , month , day));
    }

    @Override
    public boolean menuOverrideExists(Date date) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM menu_override  WHERE date = ? )", Boolean.class, date));
    }

    @Transactional
    @Override
    public void saveMessMenu(MessMenu messMenu) {

        if (messMenuExists(messMenu.getYear(), messMenu.getMonth() , messMenu.getDay())) {
            throw new DataIntegrityViolationException(" Mess Menu for this month already exists");
                 }
        entityManager.persist(messMenu);

    }

    @Override
    public List<MessMenu> getMessMenu(int year, int month) {
        // Define the native SQL query
        String sql = "SELECT * FROM mess_menu WHERE year = :year AND month = :month";
            // Create the query using EntityManager
            Query query = entityManager.createNativeQuery(sql, MessMenu.class);
            query.setParameter("year", year);
            query.setParameter("month", month);

        return  query.getResultList();
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
    public MenuOverride getOverrideMessMenu(Date date) {
        String sql = "SELECT * FROM menu_override WHERE date = :date";
        Query query = entityManager.createNativeQuery(sql, MenuOverride.class);
        query.setParameter("date", date);
        return (MenuOverride) query.getSingleResult();
    }

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

        String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM mess_menu WHERE year = ? AND month = ? AND day = ?) THEN TRUE ELSE FALSE END";

        Boolean exists = jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                year, month, day
        );


        if(Boolean.FALSE.equals(exists)){
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

        String existsSql = "SELECT EXISTS (SELECT 1 FROM menu_override mo WHERE mo.date = ?)";

        Boolean exists = jdbcTemplate.queryForObject(
                existsSql,
                Boolean.class,
                date
        );

        if (Boolean.FALSE.equals(exists)) {
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





