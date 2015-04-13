package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.console.command.admin.commands.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class DragoMapDialogActions
{
    public static final String PACKAGE = "wakfu.dragoMap";
    private static final Logger m_logger;
    private static LandMarkNote m_currentNote;
    private static DefaultMapPositionMarker m_currentMapPositionMarker;
    private static MapZone m_currentMapZone;
    private static DisplayableMapPoint m_overPoint;
    private static boolean m_mouseIsPressed;
    private static boolean m_mouseInMap;
    private static boolean m_mouseOverLandMark;
    private static Alignment9 m_mousePosition;
    public static final int NONE = 0;
    public static final int ADDING_NOTE = 1;
    public static final int ADDING_MARKER = 2;
    public static final int MOVING_NOTE = 3;
    public static int m_state;
    private static String m_noteText;
    private static DisplayableMapPoint m_editedPoint;
    private static long m_uid;
    private static ClientMapHandler.LandMarkGfx m_currentGfx;
    
    public static void switchToAddNote(final Event e) {
        DragoMapDialogActions.m_state = 1;
        CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
    }
    
    public static void switchToAddPositionMarker(final Event e) {
        DragoMapDialogActions.m_state = 2;
        CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
    }
    
    public static void mapZoomIn(final Event e) {
        MapManager.getInstance().zoomIn();
    }
    
    public static void mapZoomOut(final Event e) {
        if (MapManager.getInstance().getZoneDescription() instanceof InstanceParentMapZoneDescription) {
            return;
        }
        MapManager.getInstance().zoomOut();
    }
    
    public static void adminTP(final MapEvent event) {
        if (!event.hasAlt() && !event.hasCtrl() && !event.hasShift() && !event.hasMeta()) {
            new TeleportToCoordsCommand(new ObjectPair<Integer, Integer>((int)event.getIsoX(), (int)event.getIsoY())).execute();
        }
    }
    
    public static void onMapClick(final MapEvent e, final MapWidget map) {
        if (!e.hasAlt() && !e.hasCtrl() && !e.hasShift() && !e.hasMeta()) {
            switch (DragoMapDialogActions.m_state) {
                case 1: {
                    if (e.getButton() == 1 && DragoMapDialogActions.m_currentGfx != null) {
                        final LandMarkNote note = MapManager.getInstance().getLandMarkHandler().addNote((int)e.getIsoX(), (int)e.getIsoY(), DragoMapDialogActions.m_currentGfx.getGfxId());
                        setCurrentNote(note);
                        WakfuSoundManager.getInstance().playGUISound(600119L);
                    }
                    DragoMapDialogActions.m_currentGfx = null;
                    DragoMapDialogActions.m_state = 0;
                    GraphicalMouseManager.getInstance().hide();
                    CursorFactory.getInstance().unlock();
                    break;
                }
                case 2: {
                    if (DragoMapDialogActions.m_currentGfx != null) {
                        final DefaultMapPositionMarker dmpm = new DefaultMapPositionMarker();
                        final String desc = WakfuTranslator.getInstance().getString("map.worldPositionMarker.destination", (int)e.getIsoX(), (int)e.getIsoY());
                        MapManager.getInstance().addCompassPointAndPositionMarker(dmpm.m_id, 2, e.getIsoX(), e.getIsoY(), 0.0f, MapManager.getInstance().getMap(), desc, dmpm, DisplayableMapPointIconFactory.COMPASS_POINT_ICON, WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT, false);
                        WakfuSoundManager.getInstance().playGUISound(600119L);
                        GraphicalMouseManager.getInstance().hide();
                        CursorFactory.getInstance().unlock();
                    }
                    DragoMapDialogActions.m_state = 0;
                    break;
                }
            }
        }
    }
    
    public static void setTypeFilter(final SelectionChangedEvent e, final Byte type) {
        final UIMessage msg = new UIMessage();
        msg.setId(16584);
        msg.setBooleanValue(e.isSelected());
        msg.setByteValue(type);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void setCurrentNote(final LandMarkNote note) {
        DragoMapDialogActions.m_currentNote = note;
        PropertiesProvider.getInstance().setPropertyValue("landMark.currentNote", note);
        PropertiesProvider.getInstance().setPropertyValue("landMark.currentNote.text", (note != null) ? note.getNote() : null);
    }
    
    public static void setCurrentWorldPositionMarker(final DefaultMapPositionMarker marker) {
        DragoMapDialogActions.m_currentMapPositionMarker = marker;
        PropertiesProvider.getInstance().setPropertyValue("landMark.currentWorldPositionMarker", marker);
    }
    
    public static void onMapMove(final MapEvent e) {
        MapManager.getInstance().setDisplayedPosition((int)(e.getIsoX() / 18.0f), (int)(e.getIsoY() / 18.0f));
        final MapOverlay map = e.getTarget();
        final int width = map.getWidth();
        final int height = map.getHeight();
        final int x = e.getX(map);
        final int y = e.getY(map);
        if (x < width / 2) {
            if (y < height / 2) {
                DragoMapDialogActions.m_mousePosition = Alignment9.SOUTH_WEST;
            }
            else {
                DragoMapDialogActions.m_mousePosition = Alignment9.NORTH_WEST;
            }
        }
        else if (y < height / 2) {
            DragoMapDialogActions.m_mousePosition = Alignment9.SOUTH_EAST;
        }
        else {
            DragoMapDialogActions.m_mousePosition = Alignment9.NORTH_EAST;
        }
        final MapWidget mapWidget = e.getTarget();
        DragoMapDialogActions.m_currentMapZone = mapWidget.getSelectedMapZone();
        updateCursor();
    }
    
    public static void onMousePress(final Event e) {
        if (DragoMapDialogActions.m_overPoint != null && DragoMapDialogActions.m_overPoint.isDndropable()) {
            DragoMapDialogActions.m_mouseIsPressed = true;
            updateCursor();
        }
    }
    
    public static void onMouseRelease(final Event e) {
        DragoMapDialogActions.m_mouseIsPressed = false;
        updateCursor();
    }
    
    public static void onMouseExit(final Event e) {
        DragoMapDialogActions.m_currentMapZone = null;
        DragoMapDialogActions.m_overPoint = null;
        DragoMapDialogActions.m_mouseInMap = false;
        updateCursor();
    }
    
    public static void onMouseEnter(final Event e) {
        DragoMapDialogActions.m_mouseInMap = true;
        updateCursor();
    }
    
    private static void updateCursor() {
        CursorFactory.getInstance().unlock();
    }
    
    public static void onMapItemClick(final MapItemEvent e) {
        UIDragoMapFrame.getInstance().hidePopup();
        final DisplayableMapPoint point = e.getDisplayableMapPoint();
        if (!(point.getValue() instanceof InteractiveElementDef)) {
            return;
        }
        final InteractiveElementDef def = (InteractiveElementDef)point.getValue();
        if (def.m_type == 47) {
            final UIMessage msg = new UIMessage();
            msg.setId(19312);
            msg.setLongValue(def.m_id);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void onMapItemOver(final MapItemEvent e) {
        UIDragoMapFrame.getInstance().displayPopup(e);
        if (MapManager.getInstance().getZoneDescription() instanceof InstanceParentMapZoneDescription) {
            return;
        }
        DragoMapDialogActions.m_overPoint = e.getDisplayableMapPoint();
        updateCursor();
    }
    
    public static void onMapItemOut(final MapItemEvent e) {
        UIDragoMapFrame.getInstance().hidePopup();
        DragoMapDialogActions.m_overPoint = null;
        updateCursor();
    }
    
    public static void setDisplayTerritories(final SelectionChangedEvent e) {
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.DISPLAY_TERRITORIES_KEY, e.isSelected());
        MapManager.getInstance().setDisplayTerritories(e.isSelected());
    }
    
    public static void openNoteContainer(final Event e, final Container c) {
        c.setVisible(true);
        reset();
    }
    
    private static void dropLandMark(final int isoX, final int isoY, final LandMarkNote note) {
        note.setX(isoX);
        note.setY(isoY);
        MapManager.getInstance().getLandMarkHandler().addPersonalNotePoint(note);
        DragoMapDialogActions.m_currentNote = null;
        DragoMapDialogActions.m_state = 0;
    }
    
    public static void onLandMarkItemOver(final ItemEvent e) {
        final Object value = e.getItemValue();
        if (value != null) {
            DragoMapDialogActions.m_mouseOverLandMark = true;
            updateCursor();
        }
    }
    
    public static void onLandMarkItemOut(final ItemEvent e) {
        DragoMapDialogActions.m_mouseOverLandMark = false;
        updateCursor();
    }
    
    public static void reset() {
        DragoMapDialogActions.m_state = 0;
        GraphicalMouseManager.getInstance().hide();
        if (DragoMapDialogActions.m_editedPoint != null) {
            DragoMapDialogActions.m_editedPoint.setBeingEdited(false);
        }
        DragoMapDialogActions.m_overPoint = null;
        DragoMapDialogActions.m_currentMapZone = null;
        DragoMapDialogActions.m_mouseInMap = false;
        DragoMapDialogActions.m_mouseIsPressed = false;
        DragoMapDialogActions.m_mouseOverLandMark = false;
        CursorFactory.getInstance().unlock();
    }
    
    static {
        m_logger = Logger.getLogger((Class)DragoMapDialogActions.class);
        DragoMapDialogActions.m_currentNote = null;
        DragoMapDialogActions.m_currentMapPositionMarker = null;
        DragoMapDialogActions.m_state = 0;
        DragoMapDialogActions.m_uid = 0L;
        DragoMapDialogActions.m_currentGfx = null;
    }
    
    private enum MapCursorMode
    {
        ZOOM, 
        SELECT_TERRITORY, 
        HAND_OVER, 
        HAND_SELECT, 
        HAND_DELETE;
        
        CursorFactory.CursorType m_cursorType;
    }
    
    private static class DefaultMapPositionMarker
    {
        private final long m_id;
        
        private DefaultMapPositionMarker() {
            super();
            this.m_id = DragoMapDialogActions.m_uid++;
        }
    }
}
