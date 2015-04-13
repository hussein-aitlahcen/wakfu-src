package com.ankamagames.wakfu.client.alea.highlightingCells;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;

public class XelorSpecificRangeDisplayer implements CharacterSpecificRangeDisplayer
{
    public static final Logger m_logger;
    private static final XelorSpecificRangeDisplayer m_instance;
    
    public static XelorSpecificRangeDisplayer getInstance() {
        return XelorSpecificRangeDisplayer.m_instance;
    }
    
    @Override
    public void addCellsForCharacter(final CharacterInfo characterInfo, final ElementSelection range, final ElementSelection rangeWithConstraint, final ElementSelection rangeWithoutLOS) {
        if (characterInfo == null) {
            return;
        }
        final Fight currentFight = characterInfo.getCurrentFight();
        if (currentFight == null) {
            return;
        }
        final Collection<BasicEffectArea> activeEffectAreas = currentFight.getEffectAreaManager().getActiveEffectAreas();
        if (activeEffectAreas == null) {
            return;
        }
        for (final BasicEffectArea area : activeEffectAreas) {
            if (area.getOwner() != characterInfo) {
                continue;
            }
            if (area.getType() != EffectAreaType.HOUR.getTypeId()) {
                continue;
            }
            range.add(area.getWorldCellX(), area.getWorldCellY(), area.getWorldCellAltitude());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)XelorSpecificRangeDisplayer.class);
        m_instance = new XelorSpecificRangeDisplayer();
    }
}
