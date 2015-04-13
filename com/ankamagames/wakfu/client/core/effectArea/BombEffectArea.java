package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.ui.protocol.message.effectArea.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class BombEffectArea extends AbstractBombEffectArea implements GraphicalAreaProvider, MobileProvider, ScriptProvider
{
    private static final short BOMB_VISUAL_HEIGHT = 1;
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    private Mobile m_mobile;
    
    private BombEffectArea() {
        super();
        (this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build()).addAnimatedElementObserver(new EffectAreaAnimatedElementObserver());
    }
    
    public BombEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        builder.withVisualHeight((short)1);
        (this.m_graphicalArea = builder.build()).addAnimatedElementObserver(new EffectAreaAnimatedElementObserver());
    }
    
    @Override
    protected BombEffectArea instanceNew() {
        return new BombEffectArea();
    }
    
    @Override
    public BombEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final BombEffectArea bomb = (BombEffectArea)super.instanceAnother(parameters);
        bomb.m_scriptId = this.m_scriptId;
        bomb.m_graphicalArea.copy(this.m_graphicalArea);
        bomb.initialize();
        return bomb;
    }
    
    @Override
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    @Override
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    @Override
    public GraphicalArea getGraphicalArea() {
        return this.m_graphicalArea;
    }
    
    @Override
    public Mobile getMobile() {
        return this.m_mobile;
    }
    
    public void setMobile(final Mobile mobile) {
        this.m_mobile = mobile;
    }
    
    @Override
    public void onCarryEvent(final Carrier carrier) {
        super.onCarryEvent(carrier);
        final UIEffectAreaMessage effectAreaMsg2 = new UIEffectAreaMessage(this);
        effectAreaMsg2.setTarget(carrier);
        effectAreaMsg2.setId(18106);
        effectAreaMsg2.setBooleanValue(true);
        Worker.getInstance().pushMessage(effectAreaMsg2);
    }
    
    @Override
    public void onUncarryEvent() {
        super.onUncarryEvent();
        StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(this);
        if (this.m_context.getEffectAreaManager().containsActiveArea(this)) {
            if (this.getContext() instanceof WakfuFightEffectContextInterface) {
                final Fight currentOrObservedFight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFight();
                if (currentOrObservedFight != null && currentOrObservedFight.getId() == ((WakfuFightEffectContextInterface)this.getContext()).getFightId()) {
                    StaticEffectAreaDisplayer.getInstance().pushStaticEffectArea(this, currentOrObservedFight);
                }
            }
            ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).addHighlightCellProvidersToUpdate(StaticEffectAreaDisplayer.getInstance());
        }
    }
    
    @Override
    public void teleport(final int x, final int y, final short z) {
        super.teleport(x, y, z);
        StaticEffectAreaDisplayer.getInstance().update(this);
        ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).addHighlightCellProvidersToUpdate(StaticEffectAreaDisplayer.getInstance());
    }
    
    @Override
    public void onSelectionChanged(final boolean selected) {
        if (selected) {
            UIFightFrame.getInstance().refreshAreaDisplay(this);
        }
        else {
            UIFightFrame.getInstance().refreshAreaDisplay(null);
        }
    }
    
    @Override
    public boolean hasAnimation(final String animationName) {
        return "AnimMort".equals(animationName);
    }
    
    @Override
    public String getName() {
        if (this.m_graphicalArea != null) {
            return this.m_graphicalArea.getName();
        }
        return "";
    }
}
