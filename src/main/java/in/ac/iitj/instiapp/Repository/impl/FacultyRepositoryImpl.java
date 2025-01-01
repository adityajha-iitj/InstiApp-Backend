package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class FacultyRepositoryImpl implements FacultyRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public FacultyRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public void save(Faculty faculty) {
        // Validate required fields
        if (faculty.getUser() == null || faculty.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (faculty.getOrganisation() == null || faculty.getOrganisation().getId() == null) {
            throw new IllegalArgumentException("Organisation ID cannot be null");
        }

        // Get references to ensure User and Organisation exist
        User user = entityManager.getReference(User.class, faculty.getUser().getId());
        Organisation organisation = entityManager.getReference(Organisation.class, faculty.getOrganisation().getId());

        // Set the references in faculty object
        faculty.setUser(user);
        faculty.setOrganisation(organisation);

        // Persist the faculty entity
        entityManager.persist(faculty);
    }

    @Override
    public FacultyBaseDto getFaculty(String username) {
        String sql = """
        SELECT 
            u.name, u.username, u.email, u.user_type_name, u.avatar_url,
            o.name as org_name,
            ou.name as org_user_name, ou.username as org_user_username, 
            ou.email as org_user_email, ou.user_type_name as org_user_type_name, 
            ou.avatar_url as org_user_avatar_url,
            po.username as parent_org_username,
            o.type_name as org_type,
            o.description as org_description,
            o.website as org_website
        FROM faculty f
        JOIN users u ON f.user_id = u.id
        JOIN organisation o ON f.organisation_id = o.id
        JOIN users ou ON o.user_id = ou.id
        LEFT JOIN users po ON o.parent_organisation_user_id = po.id
        WHERE u.username = ?
    """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new FacultyBaseDto(
                    // UserBaseDto for faculty
                    new UserBaseDto(
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("user_type_name"),
                            rs.getString("avatar_url")
                    ),
                    // OrganisationBaseDto with its nested UserBaseDto
                    new OrganisationBaseDto(
                            // UserBaseDto for organisation
                            new UserBaseDto(
                                    rs.getString("org_user_name"),
                                    rs.getString("org_user_username"),
                                    rs.getString("org_user_email"),
                                    rs.getString("org_user_type_name"),
                                    rs.getString("org_user_avatar_url")
                            ),
                            rs.getString("parent_org_username"),
                            rs.getString("org_type"),
                            rs.getString("org_description"),
                            rs.getString("org_website")
                    )
            ), username);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Faculty with username '" + username + "' not found", 1);
        }
    }

    @Override
    public FacultyDetailedDto getDetailedFaculty(String username) {
        String sql = """
    SELECT 
        u.name as user_name, 
        u.username as user_username, 
        u.email as user_email, 
        u.user_type_name as user_type_name, 
        u.avatar_url as user_avatar_url,
        u.phone_number as user_phone_number, 
        u.calendar_public_id as user_calendar_public_id,
        o.user_id as org_user_id,
        o.name as org_name,
        po.username as parent_org_username,
        o.type_name as org_type_name,
        o.description as org_description,
        o.website as org_website,
        ur.role_id,
        r.name as role_name,
        r.permission as role_permission,
        org.user_id as role_org_user_id,
        org.type_name as role_org_type_name,
        org.description as role_org_description,
        org.website as role_org_website,
        orgu.name as role_org_user_name,
        orgu.username as role_org_username,
        orgu.email as role_org_email,
        orgu.user_type_name as role_org_user_type_name,
        orgu.avatar_url as role_org_avatar_url,
        org.name as role_org_name
    FROM faculty f
    JOIN users u ON f.user_id = u.id
    JOIN organisation o ON f.organisation_id = o.id
    LEFT JOIN users po ON o.parent_organisation_user_id = po.id
    LEFT JOIN user_organisation_role ur ON u.id = ur.user_id
    LEFT JOIN organisation_role r ON ur.role_id = r.id
    LEFT JOIN organisation org ON ur.organisation_id = org.id
    LEFT JOIN users orgu ON org.user_id = orgu.id
    WHERE u.username = ?
    """;

        try {
            Map<Long, OrganisationRoleDto> rolesMap = new HashMap<>();
            List<FacultyDetailedDto> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Long roleId = rs.getObject("role_id", Long.class);
                Set<OrganisationRoleDto> roles = new HashSet<>();

                if (roleId != null && rs.getObject("role_org_user_id") != null) {
                    String rolePermissionStr = rs.getString("role_permission");
                    OrganisationPermission permission = rolePermissionStr != null ?
                            OrganisationPermission.valueOf(rolePermissionStr) : null;

                    String roleName = getStringOrNull(rs, "role_name");
                    String orgName = getStringOrNull(rs, "role_org_name");

                    if (!rolesMap.containsKey(roleId)) {
                        OrganisationRoleDto role = new OrganisationRoleDto(orgName, roleName, permission);
                        rolesMap.put(roleId, role);
                    }
                    roles.add(rolesMap.get(roleId));
                }

                UserDetailedDto userDto = new UserDetailedDto(
                        getStringOrNull(rs, "user_name"),
                        getStringOrNull(rs, "user_username"),
                        getStringOrNull(rs, "user_email"),
                        getStringOrNull(rs, "user_phone_number"),
                        getStringOrNull(rs, "user_type_name"),
                        getStringOrNull(rs, "user_calendar_public_id"),
                        getStringOrNull(rs, "user_avatar_url"),
                        roles
                );

                OrganisationBaseDto organisation = new OrganisationBaseDto(
                        new UserBaseDto(
                                getStringOrNull(rs, "org_name"),
                                getStringOrNull(rs, "user_username"),
                                getStringOrNull(rs, "user_email"),
                                getStringOrNull(rs, "user_type_name"),
                                getStringOrNull(rs, "user_avatar_url")
                        ),
                        getStringOrNull(rs, "parent_org_username"),
                        getStringOrNull(rs, "org_type_name"),
                        getStringOrNull(rs, "org_description"),
                        getStringOrNull(rs, "org_website")
                );

                return new FacultyDetailedDto(userDto, organisation);
            }, username);

            if (results.isEmpty()) {
                throw new EmptyResultDataAccessException("Faculty with username '" + username + "' not found", 1);
            }

            return results.get(0);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Faculty with username '" + username + "' not found", 1);
        }
    }

    private String getStringOrNull(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return rs.wasNull() ? null : value;
    }

    @Override
    public List<FacultyBaseDto> getFacultyByFilter(Optional<String> organisationName, Pageable pageable) {
        StringBuilder sql = new StringBuilder("""
        SELECT 
            u.name, u.username, u.email, u.user_type_name, u.avatar_url,
            o.name as org_name,
            ou.name as org_user_name, ou.username as org_user_username, 
            ou.email as org_user_email, ou.user_type_name as org_user_type_name, 
            ou.avatar_url as org_user_avatar_url,
            po.username as parent_org_username,
            o.type_name as org_type,
            o.description as org_description,
            o.website as org_website
        FROM faculty f
        JOIN users u ON f.user_id = u.id
        JOIN organisation o ON f.organisation_id = o.id
        JOIN users ou ON o.user_id = ou.id
        LEFT JOIN users po ON o.parent_organisation_user_id = po.id
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // Add filter conditions
        if (organisationName.isPresent()) {
            sql.append(" AND LOWER(o.name) LIKE LOWER(?)");
            params.add("%" + organisationName.get() + "%");
        }

        // Add ordering
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();

            sort.forEach(order -> {
                String property = order.getProperty();
                String direction = order.getDirection().name();

                // Map properties to actual column names
                String column = switch (property) {
                    case "name" -> "u.name";
                    case "email" -> "u.email";
                    case "organisation" -> "o.name";
                    default -> "u.name"; // default sorting by user name
                };

                orderClauses.add(column + " " + direction);
            });

            sql.append(String.join(", ", orderClauses));
        } else {
            // Default ordering if none specified
            sql.append(" ORDER BY u.name ASC");
        }

        // Add pagination
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        try {
            return jdbcTemplate.query(
                    sql.toString(),
                    ps -> {
                        int paramIndex = 1;
                        for (Object param : params) {
                            ps.setObject(paramIndex++, param);
                        }
                    },
                    (rs, rowNum) -> new FacultyBaseDto(
                            // UserBaseDto for faculty
                            new UserBaseDto(
                                    rs.getString("name"),
                                    rs.getString("username"),
                                    rs.getString("email"),
                                    rs.getString("user_type_name"),
                                    rs.getString("avatar_url")
                            ),
                            // OrganisationBaseDto with its nested UserBaseDto
                            new OrganisationBaseDto(
                                    // UserBaseDto for organisation
                                    new UserBaseDto(
                                            rs.getString("org_user_name"),
                                            rs.getString("org_user_username"),
                                            rs.getString("org_user_email"),
                                            rs.getString("org_user_type_name"),
                                            rs.getString("org_user_avatar_url")
                                    ),
                                    rs.getString("parent_org_username"),
                                    rs.getString("org_type"),
                                    rs.getString("org_description"),
                                    rs.getString("org_website")
                            )
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList(); // Return empty list instead of null
        }
    }

    @Override
    public void updateFaculty(Faculty faculty) {
        // Validate constraints
        if (faculty.getOrganisation() == null || faculty.getOrganisation().getId() == null) {
            throw new IllegalArgumentException("Organisation ID cannot be null");
        }
        if (faculty.getUser() == null || faculty.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        String sql = """
        UPDATE faculty 
        SET organisation_id = ?,
            updated_at = CURRENT_TIMESTAMP
        WHERE user_id = ?
    """;

        int rowsAffected = jdbcTemplate.update(
                sql,
                faculty.getOrganisation().getId(),
                faculty.getUser().getId()
        );

        if (rowsAffected == 0) {
            throw new EmptyResultDataAccessException(
                    String.format("No faculty found for user ID: %d", faculty.getUser().getId()),
                    1
            );
        }
    }

}