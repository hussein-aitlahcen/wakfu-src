package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;

public class ChallengeGoalData
{
    private final int m_id;
    private int m_challengeId;
    private final Point3 m_goalPos;
    private final String m_jaugeVarName;
    private final int m_jaugeMaxValue;
    private final boolean m_isJaugeCountdown;
    
    public ChallengeGoalData(final int id, final int challengeId, final Point3 pos, final String jaugeVarName, final int jaugeMaxValue, final boolean isJaugeCountdown) {
        super();
        this.m_id = id;
        this.m_goalPos = pos;
        this.m_jaugeVarName = jaugeVarName;
        this.m_jaugeMaxValue = jaugeMaxValue;
        this.m_isJaugeCountdown = isJaugeCountdown;
        this.m_challengeId = challengeId;
    }
    
    protected String getFormattedString() {
        return WakfuTranslator.getInstance().getString(25, this.m_id, new Object[0]);
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    public Point3 getGoalPos() {
        return this.m_goalPos;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getJaugeVarName() {
        return this.m_jaugeVarName;
    }
    
    public int getJaugeMaxValue() {
        return this.m_jaugeMaxValue;
    }
    
    public boolean isJaugeCountdown() {
        return this.m_isJaugeCountdown;
    }
}
