package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.IsoWorldTarget.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class BeaconEffectArea extends AbstractBeaconEffectArea implements GraphicalAreaProvider, InteractiveIsoWorldTarget, CharacteristicUpdateListener, ScriptProvider
{
    private static final short BEACON_VISUAL_HEIGHT = 9;
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    
    private BeaconEffectArea() {
        super();
        (this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build()).addAnimatedElementObserver(new EffectAreaAnimatedElementObserver());
    }
    
    public BeaconEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        builder.withVisualHeight((short)9);
        (this.m_graphicalArea = builder.build()).addAnimatedElementObserver(new EffectAreaAnimatedElementObserver());
    }
    
    @Override
    public void initialize() {
        super.initialize();
        this.m_characteristics.setListener(this);
    }
    
    public BasicEffectArea instanceNew() {
        return new BeaconEffectArea();
    }
    
    @Override
    public BeaconEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final BeaconEffectArea beacon = (BeaconEffectArea)super.instanceAnother(parameters);
        beacon.m_scriptId = this.m_scriptId;
        beacon.m_graphicalArea.copy(this.m_graphicalArea);
        beacon.initialize();
        return beacon;
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
    public void onEffectAreaAddedToManager() {
        InteractiveIsoWorldTargetManager.getInstance().addInteractiveIsoWorldTarget(this.getId(), this);
    }
    
    @Override
    public void onEffectAreaRemovedFromManager() {
        InteractiveIsoWorldTargetManager.getInstance().removeInteractiveIsoWorldTarget(this.getId());
    }
    
    @Override
    public GraphicalArea getGraphicalArea() {
        return this.m_graphicalArea;
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        if (charac.getType() == FighterCharacteristicType.HP && this.hasAnimation("AnimHit")) {
            this.m_graphicalArea.setAnimation("AnimHit");
        }
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
        super.setPosition(x, y, alt);
        if (this.m_graphicalArea.getAnimatedElement() != null) {
            this.m_graphicalArea.getAnimatedElement().setWorldPosition(x, y, alt);
        }
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    
    @Override
    public float getAltitude() {
        return this.getWorldCellAltitude();
    }
    
    @Override
    public float getWorldX() {
        return this.getWorldCellX();
    }
    
    @Override
    public float getWorldY() {
        return this.getWorldCellY();
    }
    
    @Override
    public float getEntityRenderRadius() {
        return 0.0f;
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY) {
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float worldZ) {
    }
    
    @Override
    public void onSelectionChanged(final boolean selected) {
        if (selected) {
            UIFightFrame.getInstance();
            UIFightFrame.refreshBeaconDisplay(this);
        }
        else {
            UIFightFrame.getInstance();
            UIFightFrame.refreshBeaconDisplay(null);
        }
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
