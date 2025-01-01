package in.ac.iitj.instiapp.Repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.ac.iitj.instiapp.Repository.User.Student.Alumni.AlumniRepository;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniBaseDto;
import in.ac.iitj.instiapp.payload.User.Alumni.AlumniDetailedDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AlumniRepositoryImpl implements AlumniRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    public AlumniRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public void save(Alumni alumni) {
        if (alumni.getUser() == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (alumni.getBranch() == null) {
            throw new IllegalArgumentException("Branch cannot be null");
        }
        if (alumni.getProgram() == null) {
            throw new IllegalArgumentException("Program cannot be null");
        }

        User user = entityManager.getReference(User.class, alumni.getUser().getId());
        StudentBranch branch = entityManager.getReference(StudentBranch.class, alumni.getBranch().getId());
        StudentProgram program = entityManager.getReference(StudentProgram.class, alumni.getProgram().getId());

        alumni.setUser(user);
        alumni.setBranch(branch);
        alumni.setProgram(program);

        entityManager.persist(alumni);
    }

    @Override
    public AlumniBaseDto getAlumni(String username) {
        String sql = """
        SELECT 
            u.name, u.username, u.email, u.user_type_name, u.avatar_url,
            p.name as program_name,
            b.name as branch_name,
            o.user_id as org_user_id,
            po.username as parent_org_username,
            o.type_name as org_type,
            o.description as org_description,
            o.website as org_website,
            b.opening_year,
            b.closing_year,
            a.admission_year
        FROM alumni a
        JOIN users u ON a.user_id = u.id
        JOIN student_branch b ON a.branch_id = b.id
        JOIN student_program p ON a.program_id = p.id
        JOIN organisation o ON b.organisation_id = o.id
        LEFT JOIN users po ON o.parent_organisation_user_id = po.id
        WHERE u.username = ?
    """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new AlumniBaseDto(
                    new UserBaseDto(
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("user_type_name"),
                            rs.getString("avatar_url")
                    ),
                    rs.getString("program_name"),
                    rs.getString("branch_name"),
                    new OrganisationBaseDto(
                            new UserBaseDto(
                                    rs.getString("name"),
                                    rs.getString("username"),
                                    rs.getString("email"),
                                    rs.getString("user_type_name"),
                                    rs.getString("avatar_url")
                            ),
                            rs.getString("parent_org_username"),
                            rs.getString("org_type"),
                            rs.getString("org_description"),
                            rs.getString("org_website")
                    ),
                    rs.getInt("opening_year"),
                    rs.getInt("closing_year"),
                    rs.getInt("admission_year")
            ), username);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Alumni with username '" + username + "' not found", 1);
        }
    }


    @Override
    public AlumniDetailedDto getDetailedAlumni(String username) {
        String sql = """
    SELECT 
        u.name as user_name, 
        u.username as user_username, 
        u.email as user_email, 
        u.user_type_name as user_type_name, 
        u.avatar_url as user_avatar_url,
        u.phone_number as user_phone_number, 
        u.calendar_public_id as user_calendar_public_id,
        p.name as program_name,
        b.name as branch_name,
        o.user_id as org_user_id,
        o.name as org_name,
        po.username as parent_org_username,
        o.type_name as org_type_name,
        o.description as org_description,
        o.website as org_website,
        b.opening_year,
        b.closing_year,
        a.admission_year,
        a.pass_out_year,
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
    FROM alumni a
    JOIN users u ON a.user_id = u.id
    JOIN student_branch b ON a.branch_id = b.id
    JOIN student_program p ON a.program_id = p.id
    JOIN organisation o ON b.organisation_id = o.id
    LEFT JOIN users po ON o.parent_organisation_user_id = po.id
    LEFT JOIN user_organisation_role ur ON u.id = ur.user_id
    LEFT JOIN organisation_role r ON ur.role_id = r.id
    LEFT JOIN organisation org ON ur.organisation_id = org.id
    LEFT JOIN users orgu ON org.user_id = orgu.id
    WHERE u.username = ?
    """;

        try {
            Map<Long, OrganisationRoleDto> rolesMap = new HashMap<>();
            List<AlumniDetailedDto> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
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

                OrganisationBaseDto branchOrg = new OrganisationBaseDto(
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

                return new AlumniDetailedDto(
                        userDto,
                        getStringOrNull(rs, "program_name"),
                        getStringOrNull(rs, "branch_name"),
                        branchOrg,
                        getIntegerOrNull(rs, "opening_year"),
                        getIntegerOrNull(rs, "closing_year"),
                        getIntegerOrNull(rs, "admission_year"),
                        getIntegerOrNull(rs, "pass_out_year")
                );
            }, username);

            if (results.isEmpty()) {
                throw new EmptyResultDataAccessException("Alumni with username '" + username + "' not found", 1);
            }

            return results.get(0);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Alumni with username '" + username + "' not found", 1);
        }
    }

    private String getStringOrNull(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return rs.wasNull() ? null : value;
    }

    private Integer getIntegerOrNull(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }

    @Override
    public List<AlumniBaseDto> getAlumniByFilter(Optional<String> programName, Optional<String> branchName, Optional<Integer> admissionYear, Optional<Integer> passOutYear, Pageable pageable) {
        StringBuilder sql = new StringBuilder("""
        SELECT
            u.name, u.username, u.email, u.user_type_name, u.avatar_url,
            p.name as program_name,
            b.name as branch_name,
            o.user_id as org_user_id,
            po.username as parent_org_username,
            o.type_name as org_type,
            o.description as org_description,
            o.website as org_website,
            b.opening_year,
            b.closing_year,
            a.admission_year
        FROM alumni a
        JOIN users u ON a.user_id = u.id
        JOIN student_branch b ON a.branch_id = b.id
        JOIN student_program p ON a.program_id = p.id
        JOIN organisation o ON b.organisation_id = o.id
        LEFT JOIN users po ON o.parent_organisation_user_id = po.id
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // Add filters only if they are present (not empty)
        if (programName.isPresent()) {
            sql.append(" AND p.name = ?");
            params.add(programName.get());
        }

        if (branchName.isPresent()) {
            sql.append(" AND b.name = ?");
            params.add(branchName.get());
        }

        if (admissionYear.isPresent()) {
            sql.append(" AND a.admission_year = ?");
            params.add(admissionYear.get());
        }

        if (passOutYear.isPresent()) {
            sql.append(" AND a.pass_out_year = ?");
            params.add(passOutYear.get());
        }

        // Add ordering and pagination
        sql.append(" ORDER BY ").append(pageable.getSort().toString().replace(":", ""));
        sql.append(" LIMIT ").append(pageable.getPageSize());
        sql.append(" OFFSET ").append(pageable.getOffset());

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> new AlumniBaseDto(
                new UserBaseDto(
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("user_type_name"),
                        rs.getString("avatar_url")
                ),
                rs.getString("program_name"),
                rs.getString("branch_name"),
                new OrganisationBaseDto(
                        new UserBaseDto(
                                rs.getString("name"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("user_type_name"),
                                rs.getString("avatar_url")
                        ),
                        rs.getString("parent_org_username"),
                        rs.getString("org_type"),
                        rs.getString("org_description"),
                        rs.getString("org_website")
                ),
                rs.getInt("opening_year"),
                rs.getInt("closing_year"),
                rs.getInt("admission_year")
        ));
    }

    @Override
    public void updateAlumni(Alumni alumni) {
        // Validate userId is not null
        if (alumni.getUser() == null || alumni.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Get the existing alumni entity
        Alumni existingAlumni = entityManager.find(Alumni.class, alumni.getId());
        if (existingAlumni == null) {
            throw new NoSuchElementException("Alumni not found");
        }

        // Update only the allowed fields
        // Branch update (only ID needed as per comments)
        if (alumni.getBranch() != null && alumni.getBranch().getId() != null) {
            StudentBranch branchRef = entityManager.getReference(StudentBranch.class, alumni.getBranch().getId());
            existingAlumni.setBranch(branchRef);
        }

        // Program update (only ID needed as per comments)
        if (alumni.getProgram() != null && alumni.getProgram().getId() != null) {
            StudentProgram programRef = entityManager.getReference(StudentProgram.class, alumni.getProgram().getId());
            existingAlumni.setProgram(programRef);
        }

        // Update years (assuming validity already checked as per comments)
        if (alumni.getAdmissionYear() != null) {
            existingAlumni.setAdmissionYear(alumni.getAdmissionYear());
        }
        if (alumni.getPassOutYear() != null) {
            existingAlumni.setPassOutYear(alumni.getPassOutYear());
        }

        // Merge the updated entity
        entityManager.merge(existingAlumni);
    }

    @Override
    public Long deleteAlumni(String username) {
        // First get the user ID before deletion for returning
        String userIdQuery = """
        SELECT u.id 
        FROM alumni a 
        JOIN users u ON a.user_id = u.id 
        WHERE u.username = ?
    """;

        Long userId;
        try {
            userId = jdbcTemplate.queryForObject(userIdQuery, Long.class, username);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Alumni with username '" + username + "' not found");
        }

        // Delete the alumni record
        String deleteQuery = """
        DELETE FROM alumni a 
        WHERE a.user_id = (
            SELECT id 
            FROM users 
            WHERE username = ?
        )
    """;

        int deletedRows = jdbcTemplate.update(deleteQuery, username);

        if (deletedRows == 0) {
            throw new NoSuchElementException("Alumni with username '" + username + "' not found");
        }

        return userId;
    }







}