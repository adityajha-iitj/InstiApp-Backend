package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;

public enum AlumniData {

    ALUMNI1( 1998, 2003),
    ALUMNI2( 1999, 2003),
    ALUMNI3( 2000, 2004),
    ALUMNI4( 2004, 2008);


    public final Integer admissionYear;
    public final Integer passOutYear;


    AlumniData(Integer admissionYear, Integer passOutYear){

        this.admissionYear = admissionYear;
        this.passOutYear = passOutYear;
    }

    public Alumni toEntity(){
        return  new Alumni(null, null,null, null, this.admissionYear,this.passOutYear);
    }


}
