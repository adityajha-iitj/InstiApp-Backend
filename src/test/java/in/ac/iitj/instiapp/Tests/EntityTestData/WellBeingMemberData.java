package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;

public enum WellBeingMemberData {
    WELL_BEING_MEMBER_DATA1("M.Phil Clinical Psychology", "mondays", UserData.USER1),
    WELL_BEING_MEMBER_DATA2("M.A. Clinical Psychology", "all time", UserData.USER2),;

    private final String qualification;
    private final String availability;
    private final UserData userData;

    private WellBeingMemberData(String qualification, String availability, UserData userData) {
        this.qualification = qualification;
        this.availability = availability;
        this.userData = userData;
    }

    public WellBeingMember toEntity() {
        WellBeingMember member = new WellBeingMember(this.qualification, this.availability);
        member.setUser(this.userData.toEntity());
        return member;
    }

}

