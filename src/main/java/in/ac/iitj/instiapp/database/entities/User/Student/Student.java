package in.ac.iitj.instiapp.database.entities.User.Student;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
