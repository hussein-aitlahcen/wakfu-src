package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class SeasonCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 0;
    private Season m_season;
    
    public Season getSeason() {
        return this.m_season;
    }
    
    public void setSeason(final Season season) {
        this.m_season = season;
    }
    
    public boolean _isValid() {
        return ContainerCriterionParameterManager.getInstance().getSeason() == this.m_season;
    }
    
    @Override
    public byte getCriterionId() {
        return 0;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        this.m_season = Season.values()[is.readByte()];
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeByte((byte)this.m_season.ordinal());
    }
    
    public String _toString() {
        return "Saison : " + this.m_season.toString();
    }
    
    @Override
    public ContainerCriterion clone() {
        final SeasonCriterion sc = new SeasonCriterion();
        sc.setNegated(this.isNegated());
        sc.setSeason(this.m_season);
        return sc;
    }
}
