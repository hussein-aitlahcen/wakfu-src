package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class GateEffectAreaEffectArea extends AbstractGateEffectArea implements GraphicalAreaProvider, ScriptProvider, OverHead
{
    private int m_scriptId;
    private String m_aps;
    private final GraphicalArea m_graphicalArea;
    
    private GateEffectAreaEffectArea() {
        super();
        this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build();
    }
    
    public GateEffectAreaEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        this.m_aps = aps;
        this.m_graphicalArea = builder.build();
    }
    
    @Override
    protected BasicEffectArea instanceNew() {
        return new GateEffectAreaEffectArea();
    }
    
    @Override
    public GateEffectAreaEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final GateEffectAreaEffectArea effectArea = (GateEffectAreaEffectArea)super.instanceAnother(parameters);
        effectArea.m_scriptId = this.m_scriptId;
        effectArea.m_graphicalArea.copy(this.m_graphicalArea);
        effectArea.m_aps = this.m_graphicalArea.getAPS();
        effectArea.initialize();
        return effectArea;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        this.initAps(this.getTeamId());
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
    public void setTeamId(final byte teamId) {
        super.setTeamId(teamId);
        this.initAps(teamId);
    }
    
    private void initAps(final byte teamId) {
        if (this.m_aps == null) {
            return;
        }
        final String[] cellAps = StringUtils.split(this.m_aps, ';');
        if (cellAps.length <= teamId) {
            return;
        }
        this.m_graphicalArea.setAps(cellAps[teamId]);
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
    
    @Override
    public int getIconId() {
        return 0;
    }
    
    @Override
    public String getFormatedOverheadText() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(this.getName());
        final ArrayList<String> effects = CastableDescriptionGenerator.generateDescription(new EffectAreaWriter(this, this.m_level, 0));
        for (int i = 0, size = effects.size(); i < size; ++i) {
            sb.newLine().append(effects.get(i));
        }
        return sb.finishAndToString();
    }
    
    @Override
    public Color getOverHeadborderColor() {
        return Color.WHITE;
    }
}
