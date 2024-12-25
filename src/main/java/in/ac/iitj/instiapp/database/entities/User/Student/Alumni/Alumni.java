package in.ac.iitj.instiapp.database.entities.User.Student.Alumni;

import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alumni")
public class Alumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @OneToOne
    User user;

    @ManyToOne
    StudentProgram program;

    @ManyToOne
    StudentBranch branch;

    @Column(nullable = false)
    Integer admissionYear;

    @Column(nullable = false)
    Integer passOutYear;
}
