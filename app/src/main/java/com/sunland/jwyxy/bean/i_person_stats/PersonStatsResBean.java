package com.sunland.jwyxy.bean.i_person_stats;

import com.sunlandgroup.def.bean.result.ResultBase;

public class PersonStatsResBean extends ResultBase {

    private PersonalStat personalStats;

    public PersonalStat getPersonalStats() {
        return personalStats;
    }

    public void setPersonalStats(PersonalStat personalStats) {
        this.personalStats = personalStats;
    }
}
