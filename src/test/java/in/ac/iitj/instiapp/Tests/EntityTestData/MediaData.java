package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Media.Media;

public enum MediaData {
    MEDIA1("PUBLICID1" , "PUBLICURL1" , "ASSETID1" , MediaTypeData.MEDIA_TYPE1),
    MEDIA2("PUBLICID2" , "PUBLICURL2" , "ASSETID1" , MediaTypeData.MEDIA_TYPE2),
    MEDIA3("PUBLICID3" , "PUBLICURL3" , "ASSETID3" , MediaTypeData.MEDIA_TYPE3),
    MEDIA4("PUBLICID4", "PUBLICURL4", "ASSETID4",MediaTypeData.MEDIA_TYPE1),
    MEDIA5("PUBLICID5", "PUBLICURL5", "ASSETID5", MediaTypeData.MEDIA_TYPE2),
    MEDIA6("PUBLICID6", "PUBLICURL6", "ASSETID6", MediaTypeData.MEDIA_TYPE3),
    MEDIA7("PUBLICID7", "PUBLICURL7", "ASSETID7", MediaTypeData.MEDIA_TYPE1),
    MEDIA8("PUBLICID8", "PUBLICURL8", "ASSETID8", MediaTypeData.MEDIA_TYPE2),
    MEDIA9("PUBLICID9", "PUBLICURL9", "ASSETID9", MediaTypeData.MEDIA_TYPE3),
    MEDIA10("PUBLICID10", "PUBLICURL10", "ASSETID10", MediaTypeData.MEDIA_TYPE1),
    MEDIA11("PUBLICID11", "PUBLICURL11", "ASSETID11", MediaTypeData.MEDIA_TYPE2),
    MEDIA12("PUBLICID12", "PUBLICURL12", "ASSETID12", MediaTypeData.MEDIA_TYPE3),
    MEDIA13("PUBLICID13", "PUBLICURL13", "ASSETID13", MediaTypeData.MEDIA_TYPE1);

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

