package in.ac.iitj.instiapp.mappers.User.Student;

import in.ac.iitj.instiapp.database.entities.User.Student.StudentBranch;
import in.ac.iitj.instiapp.payload.User.Student.StudentBranchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentBranchDtoMapper {

    StudentBranchDtoMapper INSTANCE = Mappers.getMapper(StudentBranchDtoMapper.class);

    @Mapping(source = "name" , target = "name")
    @Mapping(source = "organisation" , target = "organisation")
    @Mapping(source = "openingYear" , target = "openingYear")
    @Mapping(source = "closingYear" , target = "closingYear")
    StudentBranchDto toDto(StudentBranch studentBranch);



    @Mapping(source = "name" , target = "name")
    @Mapping(source = "organisation" , target = "organisation")
    @Mapping(source = "openingYear" , target = "openingYear")
    @Mapping(source = "closingYear" , target = "closingYear")
    @Mapping(target = "id", ignore = true) // Ignoring Id field during mapping from DTO to Entity
    StudentBranch toEntity(StudentBranchDto studentBranchDto);
}
