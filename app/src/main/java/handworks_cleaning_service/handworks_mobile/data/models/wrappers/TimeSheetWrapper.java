package handworks_cleaning_service.handworks_mobile.data.models.wrappers;

import handworks_cleaning_service.handworks_mobile.data.models.users.TimeSheet;

public class TimeSheetWrapper {
    private TimeSheet timesheet;

    public TimeSheetWrapper(TimeSheet timeSheet) {
        this.timesheet = timeSheet;
    }

    public TimeSheet getTimeSheet() {
        return timesheet;
    }

    public void setTimeSheet(TimeSheet timesheet) {
        this.timesheet = timesheet;
    }
}
