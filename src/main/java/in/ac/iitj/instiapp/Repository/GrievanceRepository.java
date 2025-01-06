package in.ac.iitj.instiapp.Repository;

//post grievance with form data
//get grievance with the parameter username
//delete grievance
//update grievance
// username title object
import in.ac.iitj.instiapp.database.entities.Grievance;
import in.ac.iitj.instiapp.payload.GrievanceDto;


import java.util.List;


public interface GrievanceRepository {

    public boolean checkGrievance(String title , String user_from_id);
    public void addGrievance(Grievance grievance);
    public List<GrievanceDto> getGrievances(String username);
    public void deleteGrievance(String userName , String grievanceTitle);
    public void updateGrievance(String userName, String grievanceTitle, Boolean resolved);
    public GrievanceDto getGrievance(String userName , String grievanceTitle);
}
