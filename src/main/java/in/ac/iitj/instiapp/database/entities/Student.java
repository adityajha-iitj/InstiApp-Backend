package in.ac.iitj.instiapp.database.entities;

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
    @JoinColumn(name= "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "program", nullable = false)
    StudentProgram program;

    @ManyToOne
    @JoinColumn(name = "branch", nullable = false)
    StudentBranch branch;

    @Column( nullable = false)
    Integer admissionYear;
}
