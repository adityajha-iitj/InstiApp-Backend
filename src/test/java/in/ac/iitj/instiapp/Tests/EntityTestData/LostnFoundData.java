package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFoundType;
import lombok.Getter;

@Getter
public enum LostnFoundData {
    LOST_N_FOUND1("PUBLIC_ID1" , "EXTRA_INFO1" , true, LostnFoundType.LOST),
    LOST_N_FOUND2("PUBLIC_ID2" , "EXTRA_INFO2" , false, LostnFoundType.LOST),
    LOST_N_FOUND3("PUBLIC_ID3" , "EXTRA_INFO3" , false, LostnFoundType.FOUND),
    LOST_N_FOUND4("PUBLIC_ID4" , "EXTRA_INFO4" , true, LostnFoundType.FOUND),;

    public final String publicId;
    public final String extraInfo;
    public final Boolean status;
    public final LostnFoundType lostnFoundType;

    LostnFoundData(String publicId, String extraInfo, boolean status, LostnFoundType lostnFoundType) {
        this.publicId = publicId;
        this.extraInfo = extraInfo;
        this.status = status;
        this.lostnFoundType = lostnFoundType;
    }

    public LostnFound toEntity() {
        return new LostnFound(null ,this.publicId , null , null , null ,this.lostnFoundType, this.extraInfo , this.status , null);
    }
}
