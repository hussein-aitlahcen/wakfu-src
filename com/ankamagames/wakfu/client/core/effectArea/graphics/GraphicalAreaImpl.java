package com.ankamagames.wakfu.client.core.effectArea.graphics;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;

final class GraphicalAreaImpl implements GraphicalArea
{
    private static final Logger m_logger;
    private String m_gfx;
    private String m_cellGfx;
    private String m_aps;
    private String m_apsSpecificForCaster;
    private String m_apsSpecificForAllies;
    private int m_apsLevel;
    private String m_cellAps;
    private short m_visualHeight;
    private AnimatedInteractiveElement m_animatedElement;
    private boolean m_shouldPlayDeathAnimation;
    private IsoWorldTarget m_apsSpecificTarget;
    @NotNull
    private final AbstractEffectArea m_linkedArea;
    private List<GraphicalAreaAnimatedElementObserver> m_animatedElementObservers;
    
    GraphicalAreaImpl(@NotNull final AbstractEffectArea linkedArea) {
        super();
        this.m_apsLevel = ParticleSystemFactory.SYSTEM_WITHOUT_LEVEL;
        this.m_shouldPlayDeathAnimation = true;
        this.m_linkedArea = linkedArea;
    }
    
    @Override
    public long getId() {
        return this.m_linkedArea.getId();
    }
    
    @Override
    public String getName() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final EffectUser effectUser = this.m_linkedArea.getOwner();
        if (effectUser instanceof CharacterInfo) {
            final Citizen owner = (Citizen)effectUser;
            sb.append("[").append(owner.getName()).append("] ");
        }
        sb.append(WakfuTranslator.getInstance().getString(6, (int)this.m_linkedArea.getBaseId(), new Object[0]));
        return sb.finishAndToString();
    }
    
    @Override
    public boolean hasAPS() {
        return this.m_aps != null && !this.m_aps.isEmpty();
    }
    
    @Override
    public String getAPS() {
        return this.m_aps;
    }
    
    @Override
    public String getSpecificAPSForCaster() {
        if (!this.hasSpecificAPSForCaster()) {
            return this.getAPS();
        }
        return this.m_apsSpecificForCaster;
    }
    
    @Override
    public boolean hasSpecificAPSForCaster() {
        return this.m_apsSpecificForCaster != null;
    }
    
    @Override
    public String getSpecificAPSForAllies() {
        if (!this.hasSpecificAPSForAllies()) {
            return this.getAPS();
        }
        return this.m_apsSpecificForAllies;
    }
    
    @Override
    public boolean hasSpecificAPSForAllies() {
        return this.m_apsSpecificForAllies != null;
    }
    
    @Override
    public void setAps(final String aps) {
        this.m_aps = null;
        this.m_apsSpecificForCaster = null;
        this.m_apsSpecificForAllies = null;
        if (aps == null) {
            return;
        }
        final String[] apsArray = StringUtils.split(aps, '|');
        if (apsArray.length >= 1) {
            this.m_aps = apsArray[0];
        }
        if (apsArray.length >= 2) {
            this.m_apsSpecificForCaster = apsArray[1];
        }
        if (apsArray.length >= 3) {
            this.m_apsSpecificForAllies = apsArray[2];
        }
    }
    
    void setGfx(final String gfx) {
        this.m_gfx = gfx;
    }
    
    void setCellGfx(final String cellGfx) {
        this.m_cellGfx = cellGfx;
    }
    
    @Override
    public void setCellAps(final String cellAps) {
        this.m_cellAps = cellAps;
    }
    
    @Override
    public void setAPSLevel(final int cellApsLevel) {
        this.m_apsLevel = cellApsLevel;
    }
    
    @Override
    public void setApsSpecificTarget(final IsoWorldTarget apsSpecificTarget) {
        this.m_apsSpecificTarget = apsSpecificTarget;
    }
    
    @Override
    public IsoWorldTarget getAPSSpecificTarget() {
        return this.m_apsSpecificTarget;
    }
    
    @Override
    public boolean hasCellAPS() {
        return !StringUtils.isEmptyOrNull(this.m_cellAps);
    }
    
    @Override
    public String getCellAPS() {
        return this.m_cellAps;
    }
    
    @Override
    public int getAPSLevel() {
        return this.m_apsLevel;
    }
    
    @Override
    public boolean hasCellTexture() {
        return !StringUtils.isEmptyOrNull(this.m_cellGfx);
    }
    
    @Override
    public String getCellTextureFile() {
        return this.m_cellGfx;
    }
    
    @Override
    public boolean hasAnimation() {
        return !StringUtils.isEmptyOrNull(this.m_gfx);
    }
    
    @Override
    public String getAnimationFile() {
        return this.m_gfx;
    }
    
    public void setVisualHeight(final short visualHeight) {
        this.m_visualHeight = visualHeight;
    }
    
    @Override
    public short getVisualHeight() {
        return this.m_visualHeight;
    }
    
    @Override
    public void setShouldPlayDeathAnimation(final boolean shouldPlayDeathAnimation) {
        this.m_shouldPlayDeathAnimation = shouldPlayDeathAnimation;
    }
    
    @Override
    public boolean shouldPlayDeathAnimation() {
        return this.m_shouldPlayDeathAnimation;
    }
    
    @Override
    public void setAnimatedElement(final AnimatedInteractiveElement element) {
        (this.m_animatedElement = element).setDeltaZ(LayerOrder.EFFECT_AREA_IE.getDeltaZ());
        try {
            this.m_animatedElement.setDirection(this.m_linkedArea.getDirection());
            this.setAnimation("AnimStatique");
            if (this.m_animatedElementObservers == null || this.m_animatedElementObservers.isEmpty()) {
                return;
            }
            for (int i = 0; i < this.m_animatedElementObservers.size(); ++i) {
                this.m_animatedElementObservers.get(i).onAnimatedElementChanged(this);
            }
        }
        catch (Exception e) {
            GraphicalAreaImpl.m_logger.error((Object)e);
        }
    }
    
    @Override
    public long setAnimation(final String animation) {
        if (this.m_animatedElement == null) {
            return 0L;
        }
        this.m_animatedElement.setAnimation(animation);
        return this.m_animatedElement.getAnimationDuration(animation);
    }
    
    @Override
    public AnimatedInteractiveElement getAnimatedElement() {
        return this.m_animatedElement;
    }
    
    @NotNull
    @Override
    public AbstractEffectArea getLinkedArea() {
        return this.m_linkedArea;
    }
    
    @Override
    public void addAnimatedElementObserver(final GraphicalAreaAnimatedElementObserver observer) {
        if (this.m_animatedElementObservers == null) {
            this.m_animatedElementObservers = new ArrayList<GraphicalAreaAnimatedElementObserver>();
        }
        this.m_animatedElementObservers.add(observer);
    }
    
    @Override
    public void removeAnimatedElementObserver(final GraphicalAreaAnimatedElementObserver observer) {
        if (this.m_animatedElementObservers != null) {
            this.m_animatedElementObservers.remove(observer);
        }
    }
    
    @Override
    public void copy(final GraphicalArea model) {
        if (model == null) {
            return;
        }
        this.m_aps = model.getAPS();
        if (model.hasSpecificAPSForCaster()) {
            this.m_apsSpecificForCaster = model.getSpecificAPSForCaster();
        }
        else {
            this.m_apsSpecificForCaster = null;
        }
        if (model.hasSpecificAPSForAllies()) {
            this.m_apsSpecificForAllies = model.getSpecificAPSForAllies();
        }
        else {
            this.m_apsSpecificForAllies = null;
        }
        this.m_cellAps = model.getCellAPS();
        this.m_apsLevel = model.getAPSLevel();
        this.m_cellGfx = model.getCellTextureFile();
        this.m_gfx = model.getAnimationFile();
        this.m_animatedElement = model.getAnimatedElement();
        this.m_apsSpecificTarget = model.getAPSSpecificTarget();
        this.m_visualHeight = model.getVisualHeight();
    }
    
    static {
        m_logger = Logger.getLogger((Class)GraphicalAreaImpl.class);
    }
}
