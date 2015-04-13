package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.personalSpace.data.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.dimensionalBag.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.fileFormat.properties.*;
import java.io.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class UIDimensionalBagAppearanceManagerFrame implements MessageFrame
{
    private static UIDimensionalBagAppearanceManagerFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    protected static final Logger m_logger;
    private ArrayList<DimensionalBagCustomView> m_views;
    private int m_currentIndex;
    private int m_previousView;
    
    public UIDimensionalBagAppearanceManagerFrame() {
        super();
        this.m_views = new ArrayList<DimensionalBagCustomView>();
        this.m_currentIndex = 0;
    }
    
    public static UIDimensionalBagAppearanceManagerFrame getInstance() {
        return UIDimensionalBagAppearanceManagerFrame.m_instance;
    }
    
    public void next() {
        if (this.m_views.size() != 0) {
            this.m_currentIndex = (this.m_currentIndex + 1) % this.m_views.size();
            this.updateProperties();
        }
    }
    
    public void previous() {
        if (this.m_views.size() != 0) {
            this.m_currentIndex = (this.m_currentIndex - 1 + this.m_views.size()) % this.m_views.size();
            this.updateProperties();
        }
    }
    
    private void updateProperties() {
        if (this.m_views.size() != 0) {
            final DimensionalBagCustomView value = this.m_views.get(this.m_currentIndex);
            final String text = this.m_currentIndex + 1 + "/" + this.m_views.size();
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", text, "dimensionalBagAppearanceManagerDialog");
            PropertiesProvider.getInstance().setPropertyValue("dimensionalBag.selected.appearance", value);
            this.setMapBackground(value);
        }
    }
    
    private void setMapBackground(final DimensionalBagCustomView value) {
        final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo((short)9);
        final int backgroundMapId = DimensionalBagModelViewManager.INSTANCE.getBackgroundMapId(value.getViewModelId());
        info.setParallax(backgroundMapId);
        NetWorldFrame.setParallax(WakfuClientInstance.getInstance().getBackgroundWorldScene(), info);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17012: {
                final UIDimensionalBagSelectAppearanceMessage msg = (UIDimensionalBagSelectAppearanceMessage)message;
                PropertiesProvider.getInstance().setPropertyValue("dimensionalBag.selected.appearance", msg.getView());
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void sendMessage() {
        if (this.m_views.size() != 0) {
            final DimensionalBagCustomView view = this.m_views.get(this.m_currentIndex);
            final int modelId = view.getViewModelId();
            if (modelId != this.m_previousView) {
                this.m_previousView = modelId;
                final DimensionalBagChangeViewRequest msg = new DimensionalBagChangeViewRequest();
                msg.setViewModelId(modelId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
            }
        }
    }
    
    private ArrayList<DimensionalBagCustomView> createViews() {
        this.m_currentIndex = -1;
        boolean isOnMarket = false;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final DimensionalBagView ownedBag = localPlayer.getOwnedDimensionalBag();
        if (ownedBag != null) {
            isOnMarket = ownedBag.isOnMarket();
            this.m_previousView = ownedBag.getCustomModelView();
        }
        final int playerNationId = getPlayerNationId(localPlayer);
        int index = 0;
        final ArrayList<DimensionalBagCustomView> list = new ArrayList<DimensionalBagCustomView>();
        final TIntIntHashMap map = ((InteractiveElementFactory<T, WakfuClientInteractiveElementFactoryConfiguration>)WakfuClientInteractiveElementFactory.getInstance()).getConfiguration().getGfxWithViewType(WakfuClientInteractiveElementViewTypes.DimensionalBagIsoView.getViewFactoryId());
        final TIntIntIterator it = map.iterator();
        while (it.hasNext()) {
            it.advance();
            final int modelId = it.key();
            boolean valid = localPlayer.getPersonalSpaceHandler().knowView(modelId);
            if (!valid) {
                continue;
            }
            if (!DimensionalBagPersonalSkin.isForNation(playerNationId, modelId)) {
                continue;
            }
            if (isOnMarket) {
                valid = DimensionalBagModelViewManager.INSTANCE.isAllowedOnMarket(modelId);
            }
            else {
                valid = DimensionalBagModelViewManager.INSTANCE.isAllowedInWorld(modelId);
            }
            if (!valid) {
                continue;
            }
            if (modelId == this.m_previousView) {
                this.m_currentIndex = index;
            }
            list.add(new DimensionalBagCustomView(modelId, it.value()));
            ++index;
        }
        if (this.m_currentIndex == -1) {
            this.m_currentIndex = 0;
            this.m_previousView = list.get(this.m_currentIndex).getViewModelId();
        }
        return list;
    }
    
    private static int getPlayerNationId(final LocalPlayerCharacter localPlayer) {
        final CitizenComportment citizenComportment = localPlayer.getCitizenComportment();
        return (citizenComportment == null) ? 0 : citizenComportment.getNationId();
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("dimensionalBagAppearanceManagerDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagAppearanceManagerFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("dimensionalBagAppearanceManagerDialog", Dialogs.getDialogPath("dimensionalBagAppearanceManagerDialog"), 32768L, (short)10000);
            this.m_currentIndex = 0;
            this.m_views = this.createViews();
            this.updateProperties();
            Xulor.getInstance().putActionClass("wakfu.appearanceManager", DimensionalBagAppearanceManagerDialogActions.class);
            WakfuSoundManager.getInstance().windowFadeIn();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.sendMessage();
            final DimensionalBagView ownedBag = WakfuGameEntity.getInstance().getLocalPlayer().getOwnedDimensionalBag();
            if (ownedBag != null) {
                ownedBag.setCustomModelView(this.m_previousView);
            }
            this.m_views = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("editableDimensionalBag");
            Xulor.getInstance().unload("dimensionalBagAppearanceManagerDialog");
            Xulor.getInstance().removeActionClass("wakfu.appearanceManager");
            WakfuSoundManager.getInstance().windowFadeOut();
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        UIDimensionalBagAppearanceManagerFrame.m_instance = new UIDimensionalBagAppearanceManagerFrame();
        m_logger = Logger.getLogger((Class)UIDimensionalBagAppearanceManagerFrame.class);
    }
    
    public static class DimensionalBagCustomView extends ImmutableFieldProvider
    {
        public static final String NAME = "name";
        public static final String ACTOR_DESCRIPTOR_LIBRARY = "actorDescriptorLibrary";
        private final AnimatedElement m_animatedElement;
        private final int m_viewModelId;
        
        public DimensionalBagCustomView(final int viewModelId, final int gfxId) {
            super();
            String path = null;
            try {
                path = String.format(WakfuConfiguration.getInstance().getString("ANMInteractiveElementPath"), gfxId);
            }
            catch (PropertyException e) {
                UIDimensionalBagAppearanceManagerFrame.m_logger.warn((Object)"Probl\u00e8me au chargement de la propri\u00e9t\u00e9 ANMInteractiveElementPath");
            }
            this.m_animatedElement = new AnimatedElement();
            try {
                this.m_animatedElement.load(path, true);
            }
            catch (IOException e2) {
                UIDimensionalBagAppearanceManagerFrame.m_logger.error((Object)("Probl\u00e8me au chargement de " + path));
            }
            this.m_animatedElement.setGfxId(String.valueOf(gfxId));
            this.m_viewModelId = viewModelId;
        }
        
        public int getViewModelId() {
            return this.m_viewModelId;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("name")) {
                return WakfuTranslator.getInstance().getString(99, this.m_viewModelId, new Object[0]);
            }
            if (fieldName.equals("actorDescriptorLibrary")) {
                return this.m_animatedElement;
            }
            return null;
        }
    }
}
