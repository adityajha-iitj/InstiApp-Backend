package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.User.Student.Student.StudentRepository;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationPermission;
import in.ac.iitj.instiapp.database.entities.User.Student.Student.Student;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationBaseDto;
import in.ac.iitj.instiapp.payload.User.Organisation.OrganisationRoleDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentDetailedDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private  final JdbcTemplate jdbcTemplate;
    private  final EntityManager entityManager;


    @Autowired
    public StudentRepositoryImpl(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Override
    public void save(Student student) {
        // Check if Branch ID is provided since it's required per comments
        if (student.getBranch() == null || student.getBranch().getId() == null) {
            throw new IllegalArgumentException("Branch ID cannot be null");
        }

        // Check if Program ID is provided since it's required per comments
        if (student.getProgram() == null || student.getProgram().getId() == null) {
            throw new IllegalArgumentException("Program ID cannot be null");
        }

        // Username can be null but userId must exist per assumptions
        if (student.getUser() == null || student.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Get references to related entities as mentioned in @implNote
        User user = entityManager.getReference(User.class, student.getUser().getId());
        StudentBranch branch = entityManager.getReference(StudentBranch.class, student.getBranch().getId());
        StudentProgram program = entityManager.getReference(StudentProgram.class, student.getProgram().getId());

        // Set the references back to the student object
        student.setUser(user);
        student.setBranch(branch);
        student.setProgram(program);

        // Persist the student entity
        entityManager.persist(student);
    }

    @Override
    public StudentBaseDto getStudent(String username) {
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
            s.admission_year
        FROM student s
        JOIN users u ON s.user_id = u.id
        JOIN student_branch b ON s.branch_id = b.id
        JOIN student_program p ON s.program_id = p.id
        JOIN organisation o ON b.organisation_id = o.id
        LEFT JOIN users po ON o.parent_organisation_user_id = po.id
        WHERE u.username = ?
    """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new StudentBaseDto(
                    new UserBaseDto(
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("user_type_name"),
                            rs.getString("avatar_url")
                    ),
                    rs.getString("program_name"),
                    new StudentBranchDto(
                            rs.getString("branch_name"),
                            new OrganisationBaseDto(
                                    null, // user field is READ_ONLY in OrganisationBaseDto
                                    rs.getString("parent_org_username"),
                                    rs.getString("org_type"),
                                    rs.getString("org_description"),
                                    rs.getString("org_website")
                            ),
                            rs.getInt("opening_year"),
                            rs.getInt("closing_year")
                    ),
                    rs.getInt("admission_year")
            ), username);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }
    }

    @Override
    public List<StudentBaseDto> getStudentByFilter(Optional<String> programName, Optional<String> branchName, Optional<Integer> admissionYear, Pageable pageable) {
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
            s.admission_year
        FROM student s
        JOIN users u ON s.user_id = u.id
        JOIN student_branch b ON s.branch_id = b.id
        JOIN student_program p ON s.program_id = p.id
        JOIN organisation o ON b.organisation_id = o.id
        LEFT JOIN users po ON o.parent_organisation_user_id = po.id
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // Handle Optional filters - using empty() instead of null checks
        if (programName.isPresent()) {
            sql.append(" AND p.name = ?");
            params.add(programName.get());
        }
        if (branchName.isPresent()) {
            sql.append(" AND b.name = ?");
            params.add(branchName.get());
        }
        if (admissionYear.isPresent()) {
            sql.append(" AND s.admission_year = ?");
            params.add(admissionYear.get());
        }

        // Add pagination
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> new StudentBaseDto(
                new UserBaseDto(
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("user_type_name"),
                        rs.getString("avatar_url")
                ),
                rs.getString("program_name"),
                new StudentBranchDto(
                        rs.getString("branch_name"),
                        new OrganisationBaseDto(
                                null,
                                rs.getString("parent_org_username"),
                                rs.getString("org_type"),
                                rs.getString("org_description"),
                                rs.getString("org_website")
                        ),
                        rs.getInt("opening_year"),
                        rs.getInt("closing_year")
                ),
                rs.getInt("admission_year")
        ));
    }

    @Override
    public boolean existStudent(String username) {
        String sql = "SELECT COUNT(s.id) FROM student s JOIN users u ON s.user_id = u.id WHERE u.username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;
    }

    @Override
    public void updateStudent(Student student) {
        // Verify the student exists
        if (!entityManager.contains(student) && entityManager.find(Student.class, student.getId()) == null) {
            throw new EntityNotFoundException("Student not found");
        }

        // Get references for the new branch and program IDs
        StudentBranch branch = entityManager.getReference(StudentBranch.class, student.getBranch().getId());
        StudentProgram program = entityManager.getReference(StudentProgram.class, student.getProgram().getId());

        // Get current student from database to preserve user details
        Student currentStudent = entityManager.find(Student.class, student.getId());

        // Update only allowed fields
        currentStudent.setBranch(branch);
        currentStudent.setProgram(program);
        currentStudent.setAdmissionYear(student.getAdmissionYear());

        // Merge the updates
        entityManager.merge(currentStudent);
    }

    @Override
    public Long deleteStudent(String username) {
        // First get the user ID for return value
        String userIdSql = "SELECT u.id FROM users u WHERE u.username = ?";
        Long userId = jdbcTemplate.queryForObject(userIdSql, Long.class, username);

        if (userId == null) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }

        // Delete the student entry
        String deleteSql = "DELETE FROM student s WHERE s.user_id = (SELECT id FROM users WHERE username = ?)";
        int rowsAffected = jdbcTemplate.update(deleteSql, username);

        if (rowsAffected == 0) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
        }

        return userId;
    }

    @Override
    public StudentDetailedDto getDetailedStudent(String username) {
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
        s.admission_year,
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
    FROM student s
    JOIN users u ON s.user_id = u.id
    JOIN student_branch b ON s.branch_id = b.id
    JOIN student_program p ON s.program_id = p.id
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
            List<StudentDetailedDto> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
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

                StudentBranchDto branchDto = new StudentBranchDto(
                        getStringOrNull(rs, "branch_name"),
                        branchOrg,
                        getIntegerOrNull(rs, "opening_year"),
                        getIntegerOrNull(rs, "closing_year")
                );

                return new StudentDetailedDto(
                        userDto,
                        getStringOrNull(rs, "program_name"),
                        branchDto,
                        getIntegerOrNull(rs, "admission_year")
                );
            }, username);

            if (results.isEmpty()) {
                throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
            }

            return results.get(0);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("Student with username '" + username + "' not found", 1);
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

}
