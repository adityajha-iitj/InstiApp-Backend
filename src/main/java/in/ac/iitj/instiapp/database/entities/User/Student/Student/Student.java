package in.ac.iitj.instiapp.database.entities.User.Student.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.database.entities.User.Student.StudentProgram;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @OneToOne
    User user;

    @ManyToOne
    StudentProgram program;

    @ManyToOne
    StudentBranch branch;

    @Column( nullable = false)
    Integer admissionYear;


}
