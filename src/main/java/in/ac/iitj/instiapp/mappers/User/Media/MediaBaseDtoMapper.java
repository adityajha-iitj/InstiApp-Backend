package in.ac.iitj.instiapp.mappers.User.Media;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface MediaBaseDtoMapper {

    @Mapping(source="publicUrl",target="publicUrl")
    MediaBaseDto toMediaBaseDto(Media media);

    @Mapping(source="publicUrl",target="publicUrl")
    Media toMedia(MediaBaseDto mediaDto);
}
