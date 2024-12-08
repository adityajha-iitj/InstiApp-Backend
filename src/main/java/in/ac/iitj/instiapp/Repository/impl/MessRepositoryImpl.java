package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuItem;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MenuOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MessRepositoryImpl implements in.ac.iitj.instiapp.Repository.MessRepository {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;


    public MessRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    public boolean messMenuExists(int year, int month, int day) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM mess_menu  WHERE year = ? AND month = ? AND day = ?)", Boolean.class, year, month, day));
    }

    public boolean menuOverrideExists(Date date) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM menu_override  WHERE date = ? )", Boolean.class, date));
    }

    @Transactional
    public void saveMessMenu(MessMenu messMenu) {

        if (messMenuExists(messMenu.getYear(), messMenu.getMonth(), messMenu.getDay())) {
            throw new DataIntegrityViolationException(" Mess Menu for " + messMenu.getYear() + "-" + messMenu.getMonth() + "-" + messMenu.getDay() + " already exists");
        }
        entityManager.persist(messMenu);
    }

    /*Would return empty list if no mess menu exist for specific year and month */
    public List<MessMenu> getMessMenu(int year, int month) {
        return entityManager.createQuery("SELECT new MessMenu( mm.year, mm.month, mm.day, mm.menuItem) from MessMenu mm where mm.year = :year and mm.month = :month", MessMenu.class)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Transactional
    public void saveOverrideMessMenu(MenuOverride menuOverride) {

        Date menuOverrideDate = menuOverride.getDate();

        if (menuOverrideExists(menuOverrideDate)) {
            throw new DataIntegrityViolationException(" Mess Menu Override already  exists for date " + menuOverrideDate + ". Please delete that to insert new one");
        }
        try {
            Long id = jdbcTemplate.queryForObject("select id from mess_menu where year = EXTRACT(YEAR FROM ? :: DATE) and month = EXTRACT(MONTH FROM ? :: DATE) and day = EXTRACT(DOW from ? :: DATE)", Long.class, menuOverrideDate, menuOverrideDate, menuOverrideDate);
            MessMenu menu = entityManager.getReference(MessMenu.class, id);
            menuOverride.setMessMenu(menu);
            entityManager.persist(menuOverride);

        } catch (DataAccessException e) {
            throw new EmptyResultDataAccessException("Please first insert MessMenu for specified date then try to insert Override Menu", 1);
        }
    }


    public MenuOverride getOverrideMessMenu(Date date) {

        if (!menuOverrideExists(date)) {
            throw new EmptyResultDataAccessException("Menu Override not found for date " + date.toString(), 1);
        }

        return entityManager.createQuery("select new MenuOverride(mo.date, mo.menuItem) from MenuOverride mo where mo.date = :date", MenuOverride.class)
                .setParameter("date", date)
                .getSingleResult();
    }


    @Transactional
    public void deleteMessMenu(int year, int month, int day) {
//        Removing all the menuovveride before removing mess_menu to respect constraints
        jdbcTemplate.update("DELETE FROM menu_override mo USING mess_menu mm WHERE mo.mess_menu_id = mm.id AND mm.year = ? and  mm.month = ? AND mm.day = ?;", year, month, day);
        jdbcTemplate.update("delete from mess_menu where year=? and month=? and day= ?", year, month, day);
    }


    public void deleteOverrideMessMenu(Date date) {
        jdbcTemplate.update("delete from menu_override where date = ?", date);
    }

    /*Only Updates MenuItem*/
    @Transactional
    public void updateMessMenu(int year, int month, int day, MenuItem menuItem) {

        if (Boolean.FALSE.equals(messMenuExists(year, month, day))) {
            throw new DataIntegrityViolationException("mess menu for year" + year + "month" + month + "day" + day + " doesn't exists");
        }

        String sql2 = "SELECT menu.id FROM mess_menu menu WHERE menu.year = ? AND menu.month = ? AND menu.day = ?";

        Long id = jdbcTemplate.queryForObject(
                sql2,
                Long.class,
                year, month, day
        );

        MessMenu menu_new = entityManager.getReference(MessMenu.class, id);
        menu_new.setMenuItem(menuItem);
        entityManager.merge(menu_new);
    }


    /*Only Updates MenuItem*/
    public void updateOverrideMessMenu(MenuItem menuItem, Date date) {


        if (Boolean.FALSE.equals(menuOverrideExists(date))) {
            throw new DataIntegrityViolationException("Override menu for date " + date + " doesn't exist");
        }

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





