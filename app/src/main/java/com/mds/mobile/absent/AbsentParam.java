package com.mds.mobile.absent;

import java.io.Serializable;
import java.util.Date;

public class AbsentParam implements Serializable {

    Date workStart = new Date();
    Date workEnd = new Date();
    double longitudeOrigin = 0;
    double latitudeOrigin = 0;
    int radius = 0;
    AbsentStatus absentStatus = AbsentStatus.NO_ABSENT;
    String absentStatusValue = "";
    String absentPosition= "";
    String address = "";
    boolean inOffice = false;

    double longitudeLive = 0;
    double latitudeLive = 0;

}
