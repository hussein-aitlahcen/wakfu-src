package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.xulor2.core.*;

public class UIMapFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final UIMapFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private long m_lastProtectorUpdate;
    private Runnable m_protectorUpdateScheduler;
    private PopupElement m_popup;
    private PopupElement m_popup2;
    private PopupElement m_completeMapPopup;
    private PopupClientImpl m_popupClientImpl;
    private MapZone m_currentZone;
    
    public UIMapFrame() {
        super();
        this.m_popupClientImpl = new PopupClientImpl();
    }
    
    public static UIMapFrame getInstance() {
        return UIMapFrame.m_instance;
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
                final float scale = MapManager.getInstance().getZoomScale() + 0.1f;
                MapManager.getInstance().setZoomScale(Math.max(scale, 1.0f));
                return false;
            }
            case 16582: {
                final float scale = MapManager.getInstance().getZoomScale() - 0.1f;
                MapManager.getInstance().setZoomScale(Math.min(0.0f, scale));
                return false;
            }
            case 16583: {
                final UIMessage msg2 = (UIMessage)message;
                MapManager.getInstance().setZoomScale((float)msg2.getDoubleValue());
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public final void displayPopup(final MapEvent e) {
        final MapWidget mapWidget = e.getTarget();
        final MapZone mapZone = mapWidget.getSelectedMapZone();
        if (this.m_currentZone == mapZone) {
            return;
        }
        this.m_currentZone = mapZone;
        if (this.m_currentZone == null) {
            MasterRootContainer.getInstance().getPopupContainer().hide();
            return;
        }
        final DefaultMapZoneDescription zoneDescription = (DefaultMapZoneDescription)mapZone.getMapZoneDescription();
        final String desc = zoneDescription.getTextDescription();
        if (desc != null && desc.length() > 0) {
            PropertiesProvider.getInstance().setPropertyValue("mapPopupDescription", desc);
            try {
                PropertiesProvider.getInstance().setPropertyValue("currentMapScrollDecoratorPath", String.format(WakfuConfiguration.getInstance().getString("mapScrollDecoratorPath"), zoneDescription.getScrollDecorator()));
            }
            catch (PropertyException e2) {
                UIMapFrame.m_logger.warn((Object)e2.getMessage());
            }
            final int x = mapZone.getMinScreenX();
            final int y = mapZone.getMinScreenY();
            final int x2 = mapZone.getMaxScreenX();
            final int y2 = mapZone.getMaxScreenY();
            final int zoneIsoX = (int)(x / 86.0f - y / 43.0f);
            final int zoneIsoY = (int)(-(x / 86.0f + y / 43.0f));
            final int zoneIsoX2 = (int)(x2 / 86.0f - y2 / 43.0f);
            final int zoneIsoY2 = (int)(-(x2 / 86.0f + y2 / 43.0f));
            final Point2i screen = mapWidget.isoToScreen(zoneIsoX, zoneIsoY, true);
            final Point2i screen2 = mapWidget.isoToScreen(zoneIsoX2, zoneIsoY2, true);
            final int width = screen2.getX() - screen.getX();
            final int height = screen2.getY() - screen.getY();
            this.m_popupClientImpl.setDisplayX(screen.getX() + mapWidget.getScreenX() + mapWidget.getAppearance().getContentWidth() / 2 - width / 2);
            this.m_popupClientImpl.setDisplayY(screen.getY() + mapWidget.getScreenY());
            this.m_popupClientImpl.setWidth(width);
            this.m_popupClientImpl.setHeight(height);
            this.m_completeMapPopup.show(this.m_popupClientImpl);
        }
    }
    
    public final void displayPopup(final MapItemEvent e) {
        final String name = e.getDisplayableMapPoint().getName();
        if (!e.getDisplayableMapPoint().isEditable() && (name == null || name.length() == 0)) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("mapPopupDescription", name);
        final MapOverlay map = e.getTarget();
        this.m_popupClientImpl.setDisplayX(e.getMeshX() + map.getScreenX() - e.getMeshWidth() / 2);
        this.m_popupClientImpl.setDisplayY(e.getMeshY() + map.getScreenY() - e.getMeshHeight() / 2);
        this.m_popupClientImpl.setWidth(e.getMeshWidth());
        this.m_popupClientImpl.setHeight(e.getMeshHeight());
        if (this.m_popupClientImpl.getDisplayX() < MasterRootContainer.getInstance().getWidth() / 2) {
            this.m_popup.show(this.m_popupClientImpl);
        }
        else {
            this.m_popup2.show(this.m_popupClientImpl);
        }
    }
    
    public final boolean isPopupEditing() {
        return PropertiesProvider.getInstance().getBooleanProperty("mapPopupIsEditing");
    }
    
    public final void setPopupEditing(final boolean editing) {
        PropertiesProvider.getInstance().setPropertyValue("mapPopupIsEditing", editing);
        if (editing) {
            final ElementMap elementMap = MasterRootContainer.getInstance().getPopupContainer().getElementMap();
            if (elementMap != null) {
                final TextEditor textEditor = (TextEditor)elementMap.getElement("textEditor");
            }
        }
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
    
    public static void applyButtonZoom(final Button button, final float zoom) {
        final Dimension prefSize = button.getPrefSize();
        if (prefSize == null) {
            return;
        }
        button.setSize((int)(prefSize.width * zoom), (int)(prefSize.height * zoom));
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("mapDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIMapFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("landMarkGfxs", WakfuGameEntity.getInstance().getLocalPlayer().getMapHandler().getGfxs());
            PropertiesProvider.getInstance().setPropertyValue("compassGfx", WakfuGameEntity.getInstance().getLocalPlayer().getMapHandler().getCompassGfx());
            final LandMarkHandler landMarkHandler = MapManager.getInstance().getLandMarkHandler();
            landMarkHandler.setFilters(new LandMarkFilter());
            landMarkHandler.setMapPointProcessor(DisplayableMapPointProcessor.DEFAULT);
            landMarkHandler.updateFiltersFromPreferences();
            MapManager.getInstance().initProperty();
            MapManager.getInstance().setDisplayTerritories(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_TERRITORIES_KEY));
            this.m_popup = this.loadPopup("mapEditablePopup");
            this.m_popup2 = this.loadPopup("mapEditablePopup2");
            this.m_completeMapPopup = this.loadPopup("completeMapPopup");
            final Widget mapDialog = (Widget)Xulor.getInstance().load("mapDialog", Dialogs.getDialogPath("mapDialog"), 1L, (short)10000);
            if (mapDialog == null) {
                return;
            }
            mapDialog.setVisible(false);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("mapDialog");
            if (map != null) {
                final MapWidget mapWidget = (MapWidget)map.getElement("map");
                mapWidget.addMapListener(new MapWidget.MapListener() {
                    @Override
                    public boolean onTexturesLoaded() {
                        mapDialog.setVisible(true);
                        return false;
                    }
                    
                    @Override
                    public boolean onResized() {
                        return false;
                    }
                });
                MapManager.getInstance().setWidget(mapWidget);
            }
            MapManagerHelper.loadMap();
            this.m_popupClientImpl.setMap(map);
            MapDialogActions.reset();
            Xulor.getInstance().putActionClass("wakfu.map", MapDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600021L);
            this.updateProtectorsIfPossible();
            this.setPopupEditing(false);
            this.m_protectorUpdateScheduler = new Runnable() {
                @Override
                public void run() {
                    UIMapFrame.this.updateProtectorsIfPossible();
                }
            };
            ProcessScheduler.getInstance().schedule(this.m_protectorUpdateScheduler, 30000L, -1);
            WakfuSoundManager.getInstance().fadeMusic(0.1f, 1000);
            WakfuSoundManager.getInstance().fadeAmbiance(0.1f, 1000);
        }
    }
    
    private PopupElement loadPopup(final String dialogID) {
        Xulor.getInstance().load(dialogID, Dialogs.getDialogPath(dialogID), 8200L, (short)10000);
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogID);
        if (map != null) {
            return (PopupElement)map.getElement("popup");
        }
        return null;
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("mapDialog");
            Xulor.getInstance().unload("mapEditablePopup");
            Xulor.getInstance().unload("mapEditablePopup2");
            Xulor.getInstance().unload("completeMapPopup");
            this.m_popup = null;
            this.m_popup2 = null;
            this.m_completeMapPopup = null;
            this.m_popupClientImpl.setMap(null);
            PropertiesProvider.getInstance().removeProperty("landMarkGfxs");
            MapManager.getInstance().setWidget(null);
            LandMarkNoteManager.getInstance().saveNotes();
            MapDialogActions.setCurrentWorldPositionMarker(null);
            MapDialogActions.setCurrentNote(null);
            Xulor.getInstance().removeActionClass("wakfu.map");
            WakfuSoundManager.getInstance().playGUISound(600022L);
            CursorFactory.getInstance().unlock();
            ProcessScheduler.getInstance().remove(this.m_protectorUpdateScheduler);
            this.m_protectorUpdateScheduler = null;
            MapDialogActions.reset();
            WakfuSoundManager.getInstance().fadeMusic(1.0f, 1000);
            WakfuSoundManager.getInstance().fadeAmbiance(1.0f, 1000);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIMapFrame.class);
        m_instance = new UIMapFrame();
    }
}
