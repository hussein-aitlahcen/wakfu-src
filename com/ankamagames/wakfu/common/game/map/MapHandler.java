package com.ankamagames.wakfu.common.game.map;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;

public class MapHandler
{
    protected static Logger m_logger;
    public static final byte DEFAULT_LANDMARK_ID = 30;
    protected final TByteHashSet m_lankMarksKnown;
    
    public MapHandler() {
        super();
        this.m_lankMarksKnown = new TByteHashSet();
    }
    
    public boolean learnLandMark(final byte landMarkId) {
        this.m_lankMarksKnown.add(landMarkId);
        return true;
    }
    
    public void clear() {
        this.m_lankMarksKnown.clear();
    }
    
    public void toRaw(final CharacterSerializedLandMarkInventory part) {
        final TByteIterator it = this.m_lankMarksKnown.iterator();
        while (it.hasNext()) {
            final CharacterSerializedLandMarkInventory.LandMark landmark = new CharacterSerializedLandMarkInventory.LandMark();
            landmark.landMarkId = it.next();
            part.landMarks.add(landmark);
        }
    }
    
    public final void fromRaw(final CharacterSerializedLandMarkInventory part) {
        for (int i = 0; i < part.landMarks.size(); ++i) {
            this.learnLandMark(part.landMarks.get(i).landMarkId);
        }
    }
    
    static {
        MapHandler.m_logger = Logger.getLogger((Class)MapHandler.class);
    }
}
