package in.ac.iitj.instiapp.database.entities.User.Organisation;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import in.ac.iitj.instiapp.database.entities.Scheduling.Calendar.Events;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import in.ac.iitj.instiapp.database.entities.Announcements;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organisation")
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL) // <-- ADD THIS LINE
    Organisation parentOrganisation;

    @ManyToOne
    OrganisationType type;

    @Column(nullable = false)
    String Description;

    @OneToMany
    List<Media> media;

    @Column(nullable = true)
    String Website;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Events> events;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Announcements> announcements;

    public Organisation(Long organisationId) {
        this.Id = organisationId;
    }

    public String getTypeName() {
        return type.getName();
    }
}
