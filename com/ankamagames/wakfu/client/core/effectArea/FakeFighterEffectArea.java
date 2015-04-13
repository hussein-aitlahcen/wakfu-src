package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.IsoWorldTarget.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class FakeFighterEffectArea extends AbstractFakeFighterEffectArea implements GraphicalAreaProvider, ScriptProvider, InteractiveIsoWorldTarget
{
    private static final short FAKE_FIGHTER_VISUAL_HEIGHT = 6;
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    private CellParticleSystem m_cellParticleSystem;
    
    private FakeFighterEffectArea() {
        super();
        (this.m_graphicalArea = new GraphicalAreaImplBuilder(this).withVisualHeight((short)6).build()).addAnimatedElementObserver(new FakeFighterEffectAreaAnimatedElementObserver());
    }
    
    public FakeFighterEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx).withVisualHeight((short)6);
        (this.m_graphicalArea = builder.build()).addAnimatedElementObserver(new FakeFighterEffectAreaAnimatedElementObserver());
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
        super.setPosition(x, y, alt);
        if (this.m_graphicalArea.getAnimatedElement() != null) {
            this.m_graphicalArea.getAnimatedElement().setWorldPosition(x, y, alt);
        }
    }
    
    public BasicEffectArea instanceNew() {
        return new FakeFighterEffectArea();
    }
    
    @Override
    public FakeFighterEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final FakeFighterEffectArea area = (FakeFighterEffectArea)super.instanceAnother(parameters);
        area.m_scriptId = this.m_scriptId;
        area.m_graphicalArea.copy(this.m_graphicalArea);
        area.m_userDefinedId = this.m_userDefinedId;
        area.initialize();
        return area;
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
        return "AnimMort".equals(animationName) || "AnimAttaque".equals(animationName) || ("AnimHit".equals(animationName) && this.hasCharacteristic(FighterCharacteristicType.HP));
    }
    
    @Override
    public void onSelectionChanged(final boolean selected) {
        super.onSelectionChanged(selected);
        switch (this.getUserDefinedId()) {
            case 1: {
                if (this.getOwner() == WakfuGameEntity.getInstance().getLocalPlayer()) {
                    break;
                }
                final FightInfo fight = StaticEffectAreaDisplayer.getInstance().getFight();
                if (fight == null) {
                    return;
                }
                final BasicEffectAreaManager effectAreaManager = fight.getEffectAreaManager();
                if (effectAreaManager == null) {
                    return;
                }
                final ArrayList<AbstractHourEffectArea> areasToUpdate = new ArrayList<AbstractHourEffectArea>();
                for (final BasicEffectArea basicEffectArea : effectAreaManager.getActiveEffectAreas()) {
                    if (basicEffectArea.getType() != EffectAreaType.HOUR.getTypeId()) {
                        continue;
                    }
                    final AbstractHourEffectArea hourEffectArea = (AbstractHourEffectArea)basicEffectArea;
                    if (hourEffectArea.getOwner() != this.getOwner()) {
                        continue;
                    }
                    areasToUpdate.add(hourEffectArea);
                }
                for (int i = 0; i < areasToUpdate.size(); ++i) {
                    if (selected) {
                        StaticEffectAreaDisplayer.getInstance().pushStaticEffectArea(areasToUpdate.get(i), fight);
                    }
                    else {
                        StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(areasToUpdate.get(i));
                    }
                }
                StaticEffectAreaDisplayer.getInstance().update();
                break;
            }
        }
    }
    
    @Override
    public void onEffectAreaAddedToManager() {
        super.onEffectAreaAddedToManager();
        InteractiveIsoWorldTargetManager.getInstance().addInteractiveIsoWorldTarget(this.getId(), this);
    }
    
    @Override
    public void onEffectAreaRemovedFromManager() {
        super.onEffectAreaRemovedFromManager();
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY) {
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float worldZ) {
    }
    
    @Override
    public float getEntityRenderRadius() {
        return 0.0f;
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    
    @Override
    public String getName() {
        if (this.m_graphicalArea != null) {
            return this.m_graphicalArea.getName();
        }
        return "";
    }
}
