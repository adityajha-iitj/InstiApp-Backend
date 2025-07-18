package in.ac.iitj.instiapp.database.entities;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "announcements")
public class Announcements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(nullable = false, unique = true, updatable = false)
    private Long publicId;

    @ManyToOne
    @JoinColumn(name = "announcer", nullable = false)
    Organisation organisation;

    @Column(nullable = false)
    String title;

    @Column( nullable = true)
    String description;

    @Column(nullable = false)
    Date dateOfAnnouncement;

    @Column(nullable = false)
    Time timeOfAccouncement;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinTable(
            name = "announcement_media",
            joinColumns = @JoinColumn(name = "announcement_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<Media> media = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void ensurePublicId() {
        // if this is the very first save, Id will be null until after persist,
        // so we use a temporary UUID‐based long. After persist, Id != null,
        // so you could switch to Id if you prefer.
        if (publicId == null) {
            publicId = UUID.randomUUID()
                    .getMostSignificantBits() & Long.MAX_VALUE;
        }
    }
}
