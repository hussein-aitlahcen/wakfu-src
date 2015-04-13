package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class FecaGlyphEffectArea extends AbstractFecaGlyphEffectArea implements GraphicalAreaProvider, ScriptProvider
{
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    
    private FecaGlyphEffectArea() {
        super();
        (this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build()).addAnimatedElementObserver(new EffectAreaAnimatedElementObserver());
    }
    
    public FecaGlyphEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        (this.m_graphicalArea = builder.build()).addAnimatedElementObserver(new EffectAreaAnimatedElementObserver());
    }
    
    public BasicEffectArea instanceNew() {
        return new FecaGlyphEffectArea();
    }
    
    public FecaGlyphEffectArea instanceAnother(final short level) {
        final FecaGlyphEffectArea fecaGlyphEffectArea = new FecaGlyphEffectArea();
        return fecaGlyphEffectArea;
    }
    
    @Override
    public FecaGlyphEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final FecaGlyphEffectArea glyphEffectArea = (FecaGlyphEffectArea)super.instanceAnother(parameters);
        glyphEffectArea.m_scriptId = this.m_scriptId;
        glyphEffectArea.m_graphicalArea.copy(this.m_graphicalArea);
        glyphEffectArea.initialize();
        return glyphEffectArea;
    }
    
    @Override
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    @Override
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
        return "AnimMort".equals(animationName) || "AnimAttaque".equals(animationName) || "AnimHit".equals(animationName);
    }
    
    @Override
    public String getName() {
        if (this.m_graphicalArea != null) {
            return this.m_graphicalArea.getName();
        }
        return "";
    }
}
