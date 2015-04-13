package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class HourEffectArea extends AbstractHourEffectArea implements GraphicalAreaProvider, ScriptProvider
{
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    
    public HourEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        this.m_graphicalArea = builder.build();
    }
    
    private HourEffectArea() {
        super();
        this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.onGoesOffPlay();
    }
    
    public BasicEffectArea instanceNew() {
        return new HourEffectArea();
    }
    
    @Override
    public HourEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final HourEffectArea hourEffectArea = (HourEffectArea)super.instanceAnother(parameters);
        hourEffectArea.m_graphicalArea.copy(this.m_graphicalArea);
        hourEffectArea.initialize();
        return hourEffectArea;
    }
    
    @Override
    public void setHour(final byte hour) {
        super.setHour(hour);
        if (this.m_graphicalArea.getAPS() != null && this.m_graphicalArea.getAPS().equals("[#1]")) {
            this.m_graphicalArea.setAps(Integer.toString(WakfuClientConstants.HOUR_EFFECT_APS[hour - 1]));
        }
    }
    
    @Override
    public void setAsCurrentHour(final boolean currentHour) {
        super.setAsCurrentHour(currentHour);
        if (currentHour) {
            this.m_graphicalArea.setAPSLevel(100);
        }
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
