package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.framework.script.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class WakfuClientInteractiveAnimatedElementSceneView extends ClientInteractiveAnimatedElementSceneView
{
    private static final String VISIBLE_PART_NAME = "part";
    protected boolean m_highLightable;
    protected boolean m_overHeadable;
    private byte m_visibleContent;
    protected boolean m_selectable;
    protected String m_anmPath;
    protected int m_particleSystemId;
    protected int m_particleSystemUID;
    private static final JavaFunctionsLibrary[] m_libraries;
    
    public WakfuClientInteractiveAnimatedElementSceneView() {
        super();
        this.m_visibleContent = -1;
        this.onCheckOut();
    }
    
    @Override
    public void setInteractiveElement(@NotNull final ClientMapInteractiveElement element) {
        super.setInteractiveElement(element);
        this.m_visibleContent = (byte)~element.getVisibleContent();
        this.update();
    }
    
    @Override
    public void onInteractiveElementChanges(final MapInteractiveElement element) {
        this.update();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_anmPath = "ANMInteractiveElementPath";
        this.m_highLightable = true;
        this.addSelectionChangedListener(new InteractiveElementSelectionChangeListener<WakfuClientInteractiveAnimatedElementSceneView>() {
            @Override
            public void selectionChanged(final WakfuClientInteractiveAnimatedElementSceneView element, final boolean selected) {
                if (!element.isVisible() || !element.isSelectable()) {
                    return;
                }
                if (selected) {
                    if (WakfuClientInteractiveAnimatedElementSceneView.this.m_overHeadable) {
                        final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                        if (widget != null && widget != MasterRootContainer.getInstance()) {
                            return;
                        }
                        String text = ((WakfuClientMapInteractiveElement)element.getInteractiveElement()).getName();
                        final String addedText = WakfuClientInteractiveAnimatedElementSceneView.this.getOverheadText();
                        if (addedText != null) {
                            text += addedText;
                        }
                        if (text == null) {
                            text = "erreur le texte est null";
                            WakfuClientInteractiveAnimatedElementSceneView.m_logger.error((Object)"Le text est null, ce qui veut certainement dire que l initialisation de l'objet n a pas eu lieu ou s est mal pass\u00e9.");
                        }
                        final UIShowOverHeadInfosMessage msg = new UIShowOverHeadInfosMessage(element, ((WakfuClientMapInteractiveElement)element.getInteractiveElement()).getOverheadOffset(), WakfuClientInteractiveAnimatedElementSceneView.this.getOverheadDelayPreference());
                        if (element.getInteractiveElement() instanceof DimensionalBagInteractiveElement) {
                            TextWidgetFormater twf = new TextWidgetFormater();
                            text = twf.b().append(text)._b().finishAndToString();
                            msg.addInfo(text, null);
                            final ArrayList<MerchantInventoryItem> merchantInventoryItems = ((DimensionalBagInteractiveElement)element.getInteractiveElement()).getInfoProvider().getShelfMerchantInventoryItems();
                            for (final MerchantInventoryItem merchantInventoryItem : merchantInventoryItems) {
                                twf = new TextWidgetFormater();
                                twf.append(merchantInventoryItem.getName()).append(" x").append(merchantInventoryItem.getPackType().qty);
                                twf.newLine();
                                twf.append(merchantInventoryItem.getPrice()).append(" §");
                                msg.addInfo(twf.finishAndToString(), merchantInventoryItem.getItem().getIconUrl());
                            }
                        }
                        else {
                            msg.addInfo(text, null);
                        }
                        Worker.getInstance().pushMessage(msg);
                    }
                    if (WakfuClientInteractiveAnimatedElementSceneView.this.isHighlightable()) {
                        MobileColorizeHelper.onHover(element);
                    }
                }
                else {
                    UIOverHeadInfosFrame.getInstance().hideOverHead(element);
                    MobileColorizeHelper.onLeave(element);
                }
            }
        });
        this.m_visibleContent = -1;
        this.m_soundValidator = SoundValidatorAll.INSTANCE;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_particleSystemUID);
        this.m_particleSystemId = -1;
        this.m_particleSystemUID = -1;
        UIOverHeadInfosFrame.getInstance().hideOverHead(this);
        this.m_anmPath = "ANMInteractiveElementPath";
        this.removeAllSelectionChangedListener();
        this.dispose();
    }
    
    @Override
    public boolean isHighlightable() {
        return super.isHighlightable() && this.getInteractiveElement().isUsable();
    }
    
    @Override
    public void update() {
        final WakfuClientMapInteractiveElement element = (WakfuClientMapInteractiveElement)this.m_interactiveElement;
        if (element != null) {
            final DragInfo dragInfo = element.getDragInfo();
            if (dragInfo.isBeingDragged()) {
                final Point3 dragPoint = dragInfo.getDragPoint();
                this.setWorldPosition(dragPoint.getX(), dragPoint.getY(), dragPoint.getZ());
            }
            else {
                this.setWorldPosition(element.getWorldCellX(), element.getWorldCellY(), element.getWorldCellAltitude());
            }
            if (this.m_state == -32768) {
                this.m_state = (short)(element.isUsingSpecificOldState() ? element.getOldState() : -32768);
            }
            final byte newState = (byte)element.getState();
            if (this.m_state == -1 && newState == 0) {
                final FreeParticleSystem freeParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800220);
                if (freeParticleSystem != null) {
                    freeParticleSystem.setPosition(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude());
                    IsoParticleSystemManager.getInstance().addParticleSystem(freeParticleSystem);
                }
            }
            this.selectAnimation(newState, element.isUseSpecificAnimTransition(), element.getTransitionModel(), element.getDirection());
            this.m_state = newState;
            this.setVisible(element.isVisible());
            this.setSelectable(element.isSelectable());
            this.setPartVisible(element.getVisibleContent());
            final IsoParticleSystem particleSystem = IsoParticleSystemManager.getInstance().getParticleSystem(this.m_particleSystemUID);
            if (particleSystem != null) {
                particleSystem.setVisible(element.isVisible() && element.isUsable());
            }
            final boolean wasOverheadable = this.m_overHeadable;
            this.m_overHeadable = element.isOverHeadable();
            if (wasOverheadable && !this.m_overHeadable) {
                UIOverHeadInfosFrame.getInstance().hideOverHead(this);
            }
        }
        else {
            WakfuClientInteractiveAnimatedElementSceneView.m_logger.error((Object)"Cet ClientInteractiveElementView n'a pas de mod\u00e8le associ\u00e9.");
        }
    }
    
    private void setPartVisible(final byte visibleContent) {
        if (this.m_visibleContent == visibleContent) {
            return;
        }
        for (int i = 0; i < 8; ++i) {
            final int mask = 1 << i;
            final boolean partVisible = (visibleContent & mask) == mask;
            final boolean wasPartVisible = (this.m_visibleContent & mask) == mask;
            if (wasPartVisible != partVisible) {
                this.setLinkageVisible("part" + i, partVisible);
            }
        }
        this.m_visibleContent = visibleContent;
    }
    
    @Override
    public void setViewGfxId(final int id) {
        super.setViewGfxId(id);
        String interactiveElementPath = "";
        try {
            interactiveElementPath = WakfuConfiguration.getInstance().getString(this.m_anmPath);
            interactiveElementPath = String.format(interactiveElementPath, id);
            this.setGfxId(Integer.toString(id));
            if (id != 0) {
                this.load(interactiveElementPath, true);
            }
        }
        catch (Exception e) {
            WakfuClientInteractiveAnimatedElementSceneView.m_logger.error((Object)"impossible de r\u00e9cup\u00e9rer le path depuis la config ", (Throwable)e);
        }
    }
    
    @Override
    public void setParticleSystemId(final int particleSystemId, final int zOffset) {
        this.m_particleSystemId = particleSystemId;
        if (this.m_particleSystemId > 0) {
            final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(this.m_particleSystemId);
            system.setTarget(this, 0.0f, zOffset);
            IsoParticleSystemManager.getInstance().addParticleSystem(system);
            this.m_particleSystemUID = system.getId();
        }
    }
    
    public boolean isSelectable() {
        return this.m_selectable;
    }
    
    public void setSelectable(final boolean selectable) {
        this.m_selectable = selectable;
    }
    
    @Override
    public JavaFunctionsLibrary[] getLibraries() {
        return WakfuClientInteractiveAnimatedElementSceneView.m_libraries;
    }
    
    public void setAnmPath(final String anmPath) {
        this.m_anmPath = anmPath;
    }
    
    public String getOverheadText() {
        if (this.m_interactiveElement != null && this.m_interactiveElement instanceof WakfuClientMapInteractiveElement) {
            return ((WakfuClientMapInteractiveElement)this.m_interactiveElement).getOverHeadText();
        }
        return null;
    }
    
    protected int getOverheadDelayPreference() {
        if (this.m_interactiveElement != null && this.m_interactiveElement instanceof WakfuClientMapInteractiveElement) {
            return ((WakfuClientMapInteractiveElement)this.m_interactiveElement).getOverheadDelayPreference();
        }
        return -1;
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float altitude) {
        if (this.getWorldX() != worldX || this.getWorldY() != worldY || this.getAltitude() != altitude) {
            AnimatedElementSceneViewManager.getInstance().onElementMove(this.getId(), (int)worldX, (int)worldY);
            super.setWorldPosition(worldX, worldY, altitude);
        }
    }
    
    @Override
    public int getCurrentFightId() {
        if (this.m_interactiveElement == null) {
            return super.getCurrentFightId();
        }
        return ((WakfuClientMapInteractiveElement)this.m_interactiveElement).getCurrentFightId();
    }
    
    static {
        m_libraries = new JavaFunctionsLibrary[] { ParticleSystemFunctionsLibrary.getInstance(), MobileFunctionsLibrary.getInstance(), SoundFunctionsLibrary.getInstance(), InteractiveElementFunctionsLibrary.getInstance(), ResourceFunctionsLibrary.INSTANCE };
    }
    
    public static class WakfuInteractiveAnimatedElementSceneViewFactory extends ObjectFactory<ClientInteractiveElementView>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientInteractiveAnimatedElementSceneView makeObject() {
            WakfuClientInteractiveAnimatedElementSceneView wakfuInteractiveAnimatedElementSceneView;
            try {
                wakfuInteractiveAnimatedElementSceneView = (WakfuClientInteractiveAnimatedElementSceneView)WakfuInteractiveAnimatedElementSceneViewFactory.m_pool.borrowObject();
                wakfuInteractiveAnimatedElementSceneView.setPool(WakfuInteractiveAnimatedElementSceneViewFactory.m_pool);
            }
            catch (Exception e) {
                WakfuClientInteractiveAnimatedElementSceneView.m_logger.error((Object)"Erreur lors de l'extraction d'un WakfuClientInteractiveAnimatedElementSceneView du pool", (Throwable)e);
                wakfuInteractiveAnimatedElementSceneView = new WakfuClientInteractiveAnimatedElementSceneView();
            }
            return wakfuInteractiveAnimatedElementSceneView;
        }
        
        static {
            WakfuInteractiveAnimatedElementSceneViewFactory.m_pool = new MonitoredPool(new ObjectFactory<WakfuClientInteractiveAnimatedElementSceneView>() {
                @Override
                public WakfuClientInteractiveAnimatedElementSceneView makeObject() {
                    return new WakfuClientInteractiveAnimatedElementSceneView();
                }
            });
        }
    }
}
