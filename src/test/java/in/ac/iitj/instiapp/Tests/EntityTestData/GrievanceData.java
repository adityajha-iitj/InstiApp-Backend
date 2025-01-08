package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Grievance;

public enum GrievanceData {

    GRIEVANCE1("100","Regarding the Water coolers","The water coolers are not properly functioning",false),
    GRIEVANCE2("101","Regarding the Door Knob","My door knob at O4 W314 is broken, kindly fix it",true),
    GRIEVANCE3("102","Regarding the AC in my room","The Air Conditioner in my room O4 W314 is not working properly",true),
    GRIEVANCE4("103","This is a Test","This is just a test grievance without any functionality",false);

    public final String publicId;
    public final String Title;
    public final String Description;
    public final boolean resolved;

    GrievanceData(String publicId, String Title, String Description, boolean resolved) {
        this.publicId = publicId;
        this.Title = Title;
        this.Description = Description;
        this.resolved = resolved;
    }

    public Grievance toEntity() {
        return new Grievance(null,this.publicId, this.Title,this.Description,null,null,this.resolved,null);
    }


}
