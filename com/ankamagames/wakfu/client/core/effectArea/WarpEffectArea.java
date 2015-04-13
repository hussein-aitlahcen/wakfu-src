package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class WarpEffectArea extends AbstractWarpEffectArea implements GraphicalAreaProvider, ScriptProvider
{
    private int m_scriptId;
    private GraphicalArea m_graphicalArea;
    
    private WarpEffectArea() {
        super();
    }
    
    public WarpEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        this.m_graphicalArea = builder.build();
    }
    
    @Override
    protected WarpEffectArea instanceNew() {
        return new WarpEffectArea();
    }
    
    @Override
    public WarpEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final WarpEffectArea warp = (WarpEffectArea)super.instanceAnother(parameters);
        warp.m_scriptId = this.m_scriptId;
        (warp.m_graphicalArea = new GraphicalAreaImplBuilder(warp).build()).copy(this.m_graphicalArea);
        warp.initialize();
        return warp;
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
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    @Override
    public int getScriptId() {
        return this.m_scriptId;
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
