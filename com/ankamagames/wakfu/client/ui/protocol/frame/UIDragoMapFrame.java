package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.travel.provider.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.xulor2.core.*;

public class UIDragoMapFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static UIDragoMapFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private long m_lastProtectorUpdate;
    private Runnable m_protectorUpdateScheduler;
    private PopupElement m_popup;
    private PopupClientImpl m_popupClientImpl;
    private TravelMachine m_currentDrago;
    
    public UIDragoMapFrame() {
        super();
        this.m_popupClientImpl = new PopupClientImpl();
    }
    
    public static UIDragoMapFrame getInstance() {
        return UIDragoMapFrame.m_instance;
    }
    
    public void initialize(final TravelMachine currentDrago) {
        this.m_currentDrago = currentDrago;
        DisplayableMapPointProcessor.DRAGO_MAP_PROCESSOR.setCurrentDragoId(currentDrago.getId());
        WakfuGameEntity.getInstance().pushFrame(this);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16584: {
                final UIMessage msg = (UIMessage)message;
                MapManager.getInstance().getLandMarkHandler().setFilterTypeSelected(msg.getByteValue(), msg.getBooleanValue());
                return false;
            }
            case 16581: {
                float zoomScale = MapManager.getInstance().getZoomScale();
                final float scale = zoomScale + 0.1f;
                zoomScale = ((1.0f < scale) ? 1.0f : scale);
                MapManager.getInstance().setZoomScale(zoomScale);
                return false;
            }
            case 16582: {
                float zoomScale = MapManager.getInstance().getZoomScale();
                final float scale = zoomScale - 0.1f;
                zoomScale = ((0.0f > scale) ? 0.0f : scale);
                MapManager.getInstance().setZoomScale(zoomScale);
                return false;
            }
            case 16583: {
                final UIMessage msg2 = (UIMessage)message;
                MapManager.getInstance().setZoomScale((float)msg2.getDoubleValue());
                return false;
            }
            case 19312: {
                final UIMessage msg2 = (UIMessage)message;
                final long dragoId = msg2.getLongValue();
                final DragoInfo dragoInfo = TravelInfoManager.INSTANCE.getDragoInfo(dragoId);
                if (dragoInfo == null) {
                    UIDragoMapFrame.m_logger.warn((Object)"On essaye de se t\u00e9l\u00e9porter sur une drago d'id inconnue ?");
                    return false;
                }
                final TravelHandler travelHandler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
                if (!travelHandler.canUseDrago((int)dragoId)) {
                    return false;
                }
                final LocalPlayerCharacter user = WakfuGameEntity.getInstance().getLocalPlayer();
                final SubscribeWorldAccess worldSubscription = WorldInfoManager.getInstance().getInfo(user.getInstanceId()).m_subscriberWorld;
                final int cost = (worldSubscription == SubscribeWorldAccess.NON_ABO_FREE_ACCESS) ? 0 : 0;
                if (user.getKamasCount() < cost) {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.notEnoughKamas"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 3L, 102, 1);
                }
                else {
                    final MessageBoxControler controller = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("drago.confirmTravel", cost), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    if (controller != null) {
                        controller.addEventListener(new MessageBoxEventListener() {
                            @Override
                            public void messageBoxClosed(final int type, final String userEntry) {
                                if (type == 8) {
                                    final DragoTravelProvider provider = TravelHelper.getProvider(TravelType.DRAGO);
                                    provider.travel(user, UIDragoMapFrame.this.m_currentDrago, dragoId);
                                    WakfuGameEntity.getInstance().removeFrame(UIDragoMapFrame.this);
                                }
                            }
                        });
                        return false;
                    }
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public final void displayPopup(final MapItemEvent e) {
        PropertiesProvider.getInstance().setPropertyValue("mapPopupDescription", e.getDisplayableMapPoint().getName());
        final MapOverlay map = e.getTarget();
        this.m_popupClientImpl.setDisplayX(e.getMeshX() + map.getScreenX() - e.getMeshWidth() / 2);
        this.m_popupClientImpl.setDisplayY(e.getMeshY() + map.getScreenY() - e.getMeshHeight() / 2);
        this.m_popupClientImpl.setWidth(e.getMeshWidth());
        this.m_popupClientImpl.setHeight(e.getMeshHeight());
        this.m_popup.show(this.m_popupClientImpl);
    }
    
    public final void hidePopup() {
        MasterRootContainer.getInstance().getPopupContainer().hide();
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private void updateProtectorsIfPossible() {
        final long time = System.currentTimeMillis();
        if (time - this.m_lastProtectorUpdate > 30000L) {
            this.m_lastProtectorUpdate = time;
            final InstanceProtectorsUpdateRequestMessage msg = new InstanceProtectorsUpdateRequestMessage();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (WakfuGameEntity.getInstance().hasFrame(UIMapFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIMapFrame.getInstance());
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("dragoMapDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIDragoMapFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("landMarkGfxs", WakfuGameEntity.getInstance().getLocalPlayer().getMapHandler().getGfxs());
            MapManager.getInstance().getLandMarkHandler().setMapPointProcessor(DisplayableMapPointProcessor.DRAGO_MAP_PROCESSOR);
            MapManager.getInstance().getLandMarkHandler().setFilters(new DragoMapLandMarkFilter());
            MapManager.getInstance().initProperty();
            MapManager.getInstance().setDisplayTerritories(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_TERRITORIES_KEY));
            MapManager.getInstance().fireMapIsAvaiable();
            MapManagerHelper.loadMap(false);
            Xulor.getInstance().load("dragoMapPopup", Dialogs.getDialogPath("dragoMapPopup"), 8200L, (short)10000);
            ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("dragoMapPopup");
            if (map != null) {
                this.m_popup = (PopupElement)map.getElement("popup");
            }
            final Widget mapDialog = (Widget)Xulor.getInstance().load("dragoMapDialog", Dialogs.getDialogPath("dragoMapDialog"), 1L, (short)10000);
            map = Xulor.getInstance().getEnvironment().getElementMap("dragoMapDialog");
            if (map != null) {
                final Button closeButton = (Button)map.getElement("closeMapButton");
                final MapWidget mapWidget = (MapWidget)map.getElement("map");
                mapWidget.addMapListener(new MapWidget.MapListener() {
                    @Override
                    public boolean onTexturesLoaded() {
                        mapDialog.setVisible(true);
                        return false;
                    }
                    
                    @Override
                    public boolean onResized() {
                        final int mapBackgroundStartX = mapWidget.getMapBackgroundStartX();
                        final int mapBackgroundEndX = mapWidget.getMapBackgroundEndX();
                        final int mapBackgroundStartY = mapWidget.getMapBackgroundStartY();
                        final int mapBackgroundEndY = mapWidget.getMapBackgroundEndY();
                        final Pixmap mapBackgroundPixmap = mapWidget.getMapBackgroundPixmap();
                        final float pixmapMarginWidthRatio = (mapBackgroundEndX - mapBackgroundStartX) / mapBackgroundPixmap.getWidth();
                        final float pixmapMarginHeightRatio = (mapBackgroundEndY - mapBackgroundStartY) / mapBackgroundPixmap.getHeight();
                        final float mapVisualWidth = mapWidget.getWidth() / pixmapMarginWidthRatio;
                        final float mapVisualHeight = mapWidget.getHeight() / pixmapMarginHeightRatio;
                        final float pixmapScaleWidthRatio = mapVisualWidth / mapBackgroundPixmap.getWidth();
                        final float pixmapScaleHeightRatio = mapVisualHeight / mapBackgroundPixmap.getHeight();
                        final float zoom = pixmapScaleWidthRatio * 0.8f;
                        UIMapFrame.applyButtonZoom(closeButton, zoom);
                        final int mapLeft = (int)(mapBackgroundStartX * pixmapScaleWidthRatio);
                        final int mapBottom = (int)(mapBackgroundStartY * pixmapScaleHeightRatio);
                        closeButton.setPosition((int)(mapVisualWidth * 0.933f) - mapLeft, (int)(mapVisualHeight * 0.433f) - mapBottom);
                        return false;
                    }
                });
                MapManager.getInstance().setWidget(mapWidget);
            }
            this.m_popupClientImpl.setMap(map);
            DragoMapDialogActions.reset();
            Xulor.getInstance().putActionClass("wakfu.dragoMap", DragoMapDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600021L);
            this.updateProtectorsIfPossible();
            this.m_protectorUpdateScheduler = new Runnable() {
                @Override
                public void run() {
                    UIDragoMapFrame.this.updateProtectorsIfPossible();
                }
            };
            ProcessScheduler.getInstance().schedule(this.m_protectorUpdateScheduler, 30000L, -1);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("dragoMapDialog");
            Xulor.getInstance().unload("dragoMapPopup");
            this.m_popup = null;
            this.m_popupClientImpl.setMap(null);
            PropertiesProvider.getInstance().removeProperty("landMarkGfxs");
            MapManager.getInstance().setWidget(null);
            LandMarkNoteManager.getInstance().saveNotes();
            DragoMapDialogActions.setCurrentWorldPositionMarker(null);
            DragoMapDialogActions.setCurrentNote(null);
            Xulor.getInstance().removeActionClass("wakfu.dragoMap");
            WakfuSoundManager.getInstance().playGUISound(600022L);
            CursorFactory.getInstance().unlock();
            ProcessScheduler.getInstance().remove(this.m_protectorUpdateScheduler);
            this.m_protectorUpdateScheduler = null;
            DragoMapDialogActions.reset();
            MapManager.getInstance().fireMapIsAvaiable();
            this.m_currentDrago = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIDragoMapFrame.class);
        UIDragoMapFrame.m_instance = new UIDragoMapFrame();
    }
}
