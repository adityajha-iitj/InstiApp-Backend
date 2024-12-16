package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.Media.Mediatype;

public enum UserAvatarData {

    USER_AVATAR1(Mediatype.Image, "publicID1","assetID1","publicURL1"),
    USER_AVATAR2(Mediatype.Image, "publicID2","assetID2","publicURL2"),
    USER_AVATAR3(Mediatype.Image, "publicID3","assetID3","publicURL3");
    ;





    public final Mediatype mediatype;
    public final String publicId;
    public final String assetId;
    public final String publicUrl;


    UserAvatarData(Mediatype mediatype, String publicId, String assetId, String publicUrl) {
        this.mediatype = mediatype;
        this.publicId = publicId;
        this.assetId = assetId;
        this.publicUrl = publicUrl;
    }

    public in.ac.iitj.instiapp.database.entities.Media.UserAvatar toEntity(){
        return  new in.ac.iitj.instiapp.database.entities.Media.UserAvatar(null, mediatype,publicId,assetId, publicUrl);
    }



}
