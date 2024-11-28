package in.ac.iitj.saa.Appfinity.database.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_branch")
public class StudentBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String name;

    @ManyToOne
    Organisation   organisation;


    @Column(name = "opening_year",nullable = false,columnDefinition = "INTEGER CHECK (opening_year >= 2007 AND opening_year <= 2200)")
    Integer openingYear;


    @Column(name = "closing_year",nullable = true,columnDefinition = "INTEGER CHECK (closing_year >= 2007 AND closing_year <= 2200)")
    Integer closingYear;

}
