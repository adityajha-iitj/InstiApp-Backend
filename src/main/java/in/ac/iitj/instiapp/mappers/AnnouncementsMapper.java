package in.ac.iitj.instiapp.mappers;

import in.ac.iitj.instiapp.database.entities.Announcements;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.payload.AnnouncementsDto;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AnnouncementsMapper {

    @Mapping(
            target = "organisation",
            expression = "java(createOrganisation(announcementsDto.getOrganisationUserUserName()))"
    )
    Announcements toEntity(AnnouncementsDto announcementsDto);

    @Mapping(
            target = "mediaPublicUrls",
            expression = "java(mediaToMediaPublicUrls(announcements.getMedia()))"
    )
    @Mapping(
            source = "organisation.user.userName",
            target = "organisationUserUserName"
    )
    AnnouncementsDto toDto(Announcements announcements);

    // helper to build nested Organisation ← User from just a username
    default Organisation createOrganisation(String username) {
        if (username == null) {
            return null;
        }
        User user = new User();
        user.setUserName(username);
        Organisation org = new Organisation();
        org.setUser(user);
        return org;
    }

    // your existing media → URL extractor
    default List<String> mediaToMediaPublicUrls(Set<Media> media) {
        return media == null
                ? null
                : media.stream()
                .map(Media::getPublicUrl)
                .collect(Collectors.toList());
    }
}
