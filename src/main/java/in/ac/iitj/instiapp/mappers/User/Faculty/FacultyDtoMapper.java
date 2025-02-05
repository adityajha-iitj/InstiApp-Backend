package in.ac.iitj.instiapp.mappers.User.Faculty;

import org.mapstruct.*;

import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;



@Mapper(componentModel = "spring")
public interface FacultyDtoMapper {


    @Mapping(source="user",target="user")
    @Mapping(source="organisation",target="organisation")
    @Mapping(source="description",target="description")
    @Mapping(source="websiteUrl",target="websiteUrl")
    Faculty toFaculty(FacultyBaseDto facultyBaseDto);

    @Mapping(source="user",target="user")
    @Mapping(source="organisation",target="organisation")
    @Mapping(source="description",target="description")
    @Mapping(source="websiteUrl",target="websiteUrl")
    Faculty toFaculty(FacultyDetailedDto facultyDetailedDto);

    /*@Mapping(source="user",target="user")
    @Mapping(source="organisation",target="organisation")
    @Mapping(source="description",target="description")
    @Mapping(source="websiteUrl",target="websiteUrl")
    FacultyBaseDto toFacultyBaseDto(Faculty faculty);

    @Mapping(source="user",target="user")
    @Mapping(source="organisation",target="organisation")
    @Mapping(source="description",target="description")
    @Mapping(source="websiteUrl",target="websiteUrl")
    FacultyDetailedDto toFacultyDetailedDto(Faculty faculty);*/
}
