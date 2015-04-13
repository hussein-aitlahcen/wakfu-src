package com.ankamagames.wakfu.common.game.almanach;

import gnu.trove.*;
import com.ankamagames.wakfu.common.constants.*;

public class AlmanachEntry
{
    public static final AlmanachEntry NO_ENTRY;
    private final int m_id;
    private final int m_challengeId;
    private final int m_achievementId;
    private final TIntHashSet m_zoneIds;
    
    public AlmanachEntry(final int id, final int challengeId, final int achievementId, final int... zoneIds) {
        super();
        this.m_zoneIds = new TIntHashSet();
        this.m_id = id;
        this.m_challengeId = challengeId;
        this.m_achievementId = achievementId;
        this.m_zoneIds.addAll(zoneIds);
        this.m_zoneIds.removeAll(AlmanachConstantsAGT.ALMANACH_EXCLUDED_ZONE_IDS);
    }
    
    public int getAchievementId() {
        return this.m_achievementId;
    }
    
    public TIntHashSet getZoneIds() {
        return this.m_zoneIds;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean hasZone(final int zoneId) {
        return this.m_zoneIds.contains(zoneId);
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
    
    public boolean isNull() {
        return this.m_id == AlmanachEntry.NO_ENTRY.m_id;
    }
    
    static {
        NO_ENTRY = new AlmanachEntry(-1, -1, -1, new int[0]);
    }
}
