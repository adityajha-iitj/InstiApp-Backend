package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import lombok.Getter;

@Getter
public enum LostnFoundData {
    LOST_N_FOUND1("PUBLIC_ID1" , "EXTRA_INFO1" , true),
    LOST_N_FOUND2("PUBLIC_ID2" , "EXTRA_INFO2" , false),
    LOST_N_FOUND3("PUBLIC_ID3" , "EXTRA_INFO3" , false),
    LOST_N_FOUND4("PUBLIC_ID4" , "EXTRA_INFO4" , true);

    public final String publicId;
    public final String extraInfo;
    public final Boolean status;

    LostnFoundData(String publicId, String extraInfo, boolean status) {
        this.publicId = publicId;
        this.extraInfo = extraInfo;
        this.status = status;
    }

    public LostnFound toEntity() {
        return new LostnFound(null ,this.publicId , null , null , null , this.extraInfo , this.status , null);
    }
}
