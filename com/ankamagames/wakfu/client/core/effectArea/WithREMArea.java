package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class WithREMArea extends AbstractWithREMArea implements GraphicalAreaProvider
{
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    
    private WithREMArea() {
        super();
        this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build();
    }
    
    public WithREMArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        this.m_graphicalArea = builder.build();
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
        super.setPosition(x, y, alt);
        if (this.m_graphicalArea.getAnimatedElement() != null) {
            this.m_graphicalArea.getAnimatedElement().setWorldPosition(x, y, alt);
        }
    }
    
    public BasicEffectArea instanceNew() {
        return new WithREMArea();
    }
    
    @Override
    public WithREMArea instanceAnother(final EffectAreaParameters parameters) {
        final WithREMArea area = (WithREMArea)super.instanceAnother(parameters);
        area.m_scriptId = this.m_scriptId;
        area.m_graphicalArea.copy(this.m_graphicalArea);
        area.initialize();
        return area;
    }
    
    @Override
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        super.setDirection(direction);
        if (this.m_graphicalArea.getAnimatedElement() != null) {
            this.m_graphicalArea.getAnimatedElement().setDirection(this.getDirection());
        }
    }
    
    @Override
    public GraphicalArea getGraphicalArea() {
        return this.m_graphicalArea;
    }
    
    @Override
    public boolean hasAnimation(final String animationName) {
        return false;
    }
    
    @Override
    public String getName() {
        if (this.m_graphicalArea != null) {
            return this.m_graphicalArea.getName();
        }
        return "";
    }
}
