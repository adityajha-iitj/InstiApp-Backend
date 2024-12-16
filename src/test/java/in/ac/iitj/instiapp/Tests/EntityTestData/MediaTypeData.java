package in.ac.iitj.instiapp.Tests.EntityTestData;


import in.ac.iitj.instiapp.database.entities.Media.Mediatype;



public enum MediaTypeData {

    MEDIA_TYPE1(Mediatype.Image),
    MEDIA_TYPE2(Mediatype.File),
    MEDIA_TYPE3(Mediatype.Video);
    ;

    public final Mediatype name;

    MediaTypeData(Mediatype name) {
        this.name = name;
    }

    public Mediatype toEntity(){
        return this.name;
    }
}
