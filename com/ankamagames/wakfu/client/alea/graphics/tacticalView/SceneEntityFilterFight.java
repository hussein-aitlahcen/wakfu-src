package com.ankamagames.wakfu.client.alea.graphics.tacticalView;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.util.*;

class SceneEntityFilterFight extends SceneEntityFilter
{
    private final FightMap m_fightMap;
    private final int m_fightId;
    private final TacticalView m_tacticalView;
    
    SceneEntityFilterFight(final Fight fight) {
        super();
        this.m_fightMap = fight.getFightMap();
        this.m_fightId = fight.getId();
        this.m_tacticalView = new TacticalView(this.m_fightMap);
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_tacticalView.clear();
    }
    
    @Override
    public boolean acceptEntity(final Entity entity, final boolean sorted) {
        return this.m_fightMap.isInsideOrBorder(MathHelper.fastRound(entity.m_cellX), MathHelper.fastRound(entity.m_cellY));
    }
    
    @Override
    public boolean accept(final AnimatedElement animatedElement) {
        return animatedElement.getCurrentFightId() == this.m_fightId;
    }
    
    @Override
    public void addEntities(final IsoWorldScene scene, final ArrayList<DisplayedScreenElement> elements) {
        this.m_tacticalView.prepare(elements);
        for (final Entity entity : this.m_tacticalView.getEntities()) {
            scene.forceAddEntity(entity, true);
        }
    }
}
