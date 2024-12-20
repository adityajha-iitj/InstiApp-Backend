package in.ac.iitj.instiapp.Tests.EntityTestData;

import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;

public enum WellBeingMemberData {
    WELL_BEING_MEMBER_DATA1("M.Phil Clinical Psychology", "mondays"),
    WELL_BEING_MEMBER_DATA2("M.A. Clinical Psychology", "all time");

    private final String qualification;
    private final String availability;

    private WellBeingMemberData(String qualification, String availability) {
        this.qualification = qualification;
        this.availability = availability;
    }

    public WellBeingMember toEntity() {
        return new WellBeingMember(this.qualification, this.availability);
    }

}

