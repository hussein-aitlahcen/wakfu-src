package com.ankamagames.wakfu.common.game.wakfu;

import com.ankamagames.framework.external.*;
import gnu.trove.*;

public enum WakfuMonsterAlignment implements ExportableEnum
{
    NEUTRAL(0, 1, false, "monstre neutre"), 
    WAKFU(100, 3, true, "monstre wakfu"), 
    STASIS(200, 3, false, "monstre stasis");
    
    private static final TIntObjectHashMap<WakfuMonsterAlignment> m_alignementsById;
    private final int m_id;
    private final int m_multiplicator;
    private final boolean m_isWakfu;
    private final String m_description;
    
    private WakfuMonsterAlignment(final int id, final int multiplicator, final boolean isWakfu, final String description) {
        this.m_id = id;
        this.m_multiplicator = multiplicator;
        this.m_isWakfu = isWakfu;
        this.m_description = description;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getMultiplicator() {
        return this.m_multiplicator;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_description;
    }
    
    @Override
    public String getEnumComment() {
        return "multiplicateur x" + this.m_multiplicator;
    }
    
    public boolean isWakfu() {
        return this.m_isWakfu;
    }
    
    public static WakfuMonsterAlignment getAlignmentById(final int id) {
        return WakfuMonsterAlignment.m_alignementsById.get(id);
    }
    
    static {
        m_alignementsById = new TIntObjectHashMap<WakfuMonsterAlignment>();
        for (final WakfuMonsterAlignment alignment : values()) {
            WakfuMonsterAlignment.m_alignementsById.put(alignment.getId(), alignment);
        }
    }
}
