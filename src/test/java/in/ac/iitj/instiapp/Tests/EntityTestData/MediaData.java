package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Media.Media;

public enum MediaData {
    MEDIA_DATA1("PUBLICID1" , "PUBLICURL1" , "ASSETID1" , MediaTypeData.MEDIA_TYPE1),
    MEDIA_DATA2("PUBLICID2" , "PUBLICURL2" , "ASSETID1" , MediaTypeData.MEDIA_TYPE2),
    MEDIA_DATA3("PUBLICID3" , "PUBLICURL3" , "ASSETID3" , MediaTypeData.MEDIA_TYPE3),
    MEDIA_DATA4("PUBLICID4", "PUBLICURL4", "ASSETID4",MediaTypeData.MEDIA_TYPE1),
    MEDIA_DATA7("PUBLICID7", "PUBLICURL7", "ASSETID7", MediaTypeData.MEDIA_TYPE1),
    MEDIA_DATA8("PUBLICID8", "PUBLICURL8", "ASSETID8", MediaTypeData.MEDIA_TYPE2),
    MEDIA_DATA9("PUBLICID9", "PUBLICURL9", "ASSETID9", MediaTypeData.MEDIA_TYPE3),
    MEDIA_DATA10("PUBLICID10", "PUBLICURL10", "ASSETID10", MediaTypeData.MEDIA_TYPE1),
    MEDIA_DATA11("PUBLICID11", "PUBLICURL11", "ASSETID11", MediaTypeData.MEDIA_TYPE2),
    MEDIA_DATA12("PUBLICID12", "PUBLICURL12", "ASSETID12", MediaTypeData.MEDIA_TYPE3),
    MEDIA_DATA13("PUBLICID13", "PUBLICURL13", "ASSETID13", MediaTypeData.MEDIA_TYPE1);

    public final String publicId;
    public final String publicUrl;
    public final String assetId;
    public final MediaTypeData mediaType;

    MediaData(String publicId, String publicUrl, String assetId, MediaTypeData mediaType) {
        this.publicId = publicId;
        this.publicUrl = publicUrl;
        this.assetId = assetId;
        this.mediaType = mediaType;
    }

    public Media toEntity(){
        return new Media(this.publicId , this.publicUrl , this.assetId , this.mediaType.toEntity());
    }
}

