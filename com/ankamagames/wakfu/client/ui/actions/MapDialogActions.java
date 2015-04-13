package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.console.command.admin.commands.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class MapDialogActions
{
    public static final String PACKAGE = "wakfu.map";
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
    public static final int MOVING_MARKER = 4;
    public static int m_state;
    private static String m_noteText;
    private static DisplayableMapPoint m_editedPoint;
    private static long m_uid;
    private static ClientMapHandler.LandMarkGfx m_currentGfx;
    
    public static void switchToAddNote(final Event e) {
        MapDialogActions.m_state = 1;
        CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
    }
    
    public static void switchToAddPositionMarker(final Event e) {
        MapDialogActions.m_state = 2;
        CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
    }
    
    public static void radarZoom(final Event event) {
    }
    
    public static void radarZoomIn(final Event event) {
        UIMessage.send((short)16581);
    }
    
    public static void radarZoomOut(final Event event) {
        UIMessage.send((short)16582);
    }
    
    public static void mapZoomIn(final Event e) {
        MapManager.getInstance().zoomIn();
    }
    
    public static void mapZoomOut(final Event e) {
        MapManager.getInstance().zoomOut();
    }
    
    public static void adminTP(final MapEvent event) {
        if (!event.hasAlt() && !event.hasCtrl() && !event.hasMeta() && !event.hasShift()) {
            new TeleportToInstanceCommand(new ObjectPair<Integer, Integer>((int)event.getIsoX(), (int)event.getIsoY()), MapManager.getInstance().getMap()).execute();
        }
    }
    
    public static void onMapClick(final MapEvent e, final MapWidget map) {
        if (!e.hasAlt() && !e.hasCtrl() && !e.hasMeta() && !e.hasShift()) {
            switch (MapDialogActions.m_state) {
                case 1: {
                    if (e.getButton() == 1 && MapDialogActions.m_currentGfx != null) {
                        final LandMarkNote note = MapManager.getInstance().getLandMarkHandler().addNote((int)e.getIsoX(), (int)e.getIsoY(), MapDialogActions.m_currentGfx.getGfxId());
                        setCurrentNote(note);
                        WakfuSoundManager.getInstance().playGUISound(600119L);
                    }
                    MapDialogActions.m_currentGfx = null;
                    MapDialogActions.m_state = 0;
                    GraphicalMouseManager.getInstance().hide();
                    CursorFactory.getInstance().unlock();
                    break;
                }
                case 2: {
                    if (MapDialogActions.m_currentGfx != null) {
                        final DefaultMapPositionMarker dmpm = new DefaultMapPositionMarker();
                        final String desc = WakfuTranslator.getInstance().getString("map.worldPositionMarker.destination", (int)e.getIsoX(), (int)e.getIsoY());
                        MapManager.getInstance().addCompassPointAndPositionMarker(dmpm.m_id, 2, e.getIsoX(), e.getIsoY(), 0.0f, MapManager.getInstance().getMap(), desc, dmpm, DisplayableMapPointIconFactory.COMPASS_POINT_ICON, WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT, false);
                        WakfuSoundManager.getInstance().playGUISound(600119L);
                        GraphicalMouseManager.getInstance().hide();
                        CursorFactory.getInstance().unlock();
                    }
                    MapDialogActions.m_state = 0;
                    break;
                }
                case 3: {
                    if (e.getButton() == 1) {
                        dropLandMark((int)e.getIsoX(), (int)e.getIsoY(), MapDialogActions.m_currentNote);
                    }
                    else {
                        deleteNote(MapDialogActions.m_currentNote);
                    }
                    GraphicalMouseManager.getInstance().hide();
                    CursorFactory.getInstance().unlock();
                    break;
                }
                case 0: {
                    if (e.getValue() instanceof LandMarkNote && e.getButton() == 3) {
                        return;
                    }
                    if (e.getButton() == 3) {
                        mapZoomOut(e);
                    }
                    else if (e.getButton() == 1) {
                        MapManager.getInstance().setSecondaryMapToSelectedMapZone();
                    }
                    cancelNoteModification(e);
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
        MapDialogActions.m_currentNote = note;
    }
    
    public static void setCurrentWorldPositionMarker(final DefaultMapPositionMarker marker) {
        MapDialogActions.m_currentMapPositionMarker = marker;
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
                MapDialogActions.m_mousePosition = Alignment9.SOUTH_WEST;
            }
            else {
                MapDialogActions.m_mousePosition = Alignment9.NORTH_WEST;
            }
        }
        else if (y < height / 2) {
            MapDialogActions.m_mousePosition = Alignment9.SOUTH_EAST;
        }
        else {
            MapDialogActions.m_mousePosition = Alignment9.NORTH_EAST;
        }
        final MapWidget mapWidget = e.getTarget();
        MapDialogActions.m_currentMapZone = mapWidget.getSelectedMapZone();
        UIMapFrame.getInstance().displayPopup(e);
        updateCursor();
    }
    
    public static void onMousePress(final Event e) {
        if (MapDialogActions.m_overPoint != null && MapDialogActions.m_overPoint.isDndropable()) {
            MapDialogActions.m_mouseIsPressed = true;
            updateCursor();
        }
    }
    
    public static void onMouseRelease(final Event e) {
        MapDialogActions.m_mouseIsPressed = false;
        updateCursor();
    }
    
    public static void onMouseExit(final Event e) {
        MapDialogActions.m_currentMapZone = null;
        MapDialogActions.m_overPoint = null;
        MapDialogActions.m_mouseInMap = false;
        updateCursor();
    }
    
    public static void onMouseEnter(final Event e) {
        MapDialogActions.m_mouseInMap = true;
        updateCursor();
    }
    
    private static boolean canDragNDrop() {
        return !canZoomIn() && MapManager.getInstance().isDisplayingCurrentMap();
    }
    
    private static boolean canZoomIn() {
        return MapManager.getInstance().canZoomIn();
    }
    
    private static void updateCursor() {
        final boolean canZoomIn = canZoomIn();
        final boolean canDragNDrop = canDragNDrop();
        CursorFactory.CursorType cursor = null;
        if (MapDialogActions.m_mouseIsPressed || MapDialogActions.m_state == 1 || MapDialogActions.m_state == 3 || MapDialogActions.m_state == 4) {
            if (!canDragNDrop || !MapDialogActions.m_mouseInMap) {
                cursor = CursorFactory.CursorType.CUSTOM11;
            }
            else {
                cursor = CursorFactory.CursorType.CUSTOM10;
            }
        }
        else if (MapDialogActions.m_mouseOverLandMark || (MapDialogActions.m_overPoint != null && canDragNDrop && MapDialogActions.m_overPoint.isDndropable())) {
            cursor = CursorFactory.CursorType.CUSTOM9;
        }
        else if (MapDialogActions.m_currentMapZone != null) {
            final DefaultMapZoneDescription zoneDescription = (DefaultMapZoneDescription)MapDialogActions.m_currentMapZone.getMapZoneDescription();
            if (MapManager.getInstance().getZoneDescription() instanceof SubInstanceParentMapZoneDescription && zoneDescription instanceof TerritoryMapZoneDescription) {
                final TerritoryMapZoneDescription mapZoneDesc = (TerritoryMapZoneDescription)zoneDescription;
                final int territoryId = mapZoneDesc.getTerritory().getId();
                if (territoryId != MapManager.getInstance().getDisplayedTerritoryId()) {
                    switch (MapDialogActions.m_mousePosition) {
                        case NORTH_WEST: {
                            cursor = CursorFactory.CursorType.CUSTOM12;
                            break;
                        }
                        case NORTH_EAST: {
                            cursor = CursorFactory.CursorType.CUSTOM13;
                            break;
                        }
                        case SOUTH_WEST: {
                            cursor = CursorFactory.CursorType.CUSTOM14;
                            break;
                        }
                        case SOUTH_EAST: {
                            cursor = CursorFactory.CursorType.CUSTOM15;
                            break;
                        }
                    }
                }
            }
            else if (zoneDescription.canZoomIn()) {
                cursor = CursorFactory.CursorType.CUSTOM8;
            }
            else if (MapDialogActions.m_currentMapZone.getMapZoneDescription() instanceof SplitMapMapzoneDescription) {
                final SplitMapMapzoneDescription mapZoneDesc2 = (SplitMapMapzoneDescription)MapDialogActions.m_currentMapZone.getMapZoneDescription();
            }
        }
        if (cursor != null) {
            CursorFactory.getInstance().show(cursor, true);
        }
        else {
            CursorFactory.getInstance().unlock();
        }
    }
    
    public static void onMapItemClick(final MapItemEvent e) {
        if (canZoomIn() || e.getButton() != 3) {
            UIMapFrame.getInstance().hidePopup();
            return;
        }
        final DisplayableMapPoint point = e.getDisplayableMapPoint();
        if (point.getValue() instanceof Item) {
            final Item item = (Item)point.getValue();
            MapManager.getInstance().removeCompassPointAndPositionMarker(4, item.getReferenceId());
            return;
        }
        if (!point.isEditable()) {
            return;
        }
        UIMapFrame.getInstance().displayPopup(e);
        (MapDialogActions.m_editedPoint = point).setBeingEdited(true);
        MapDialogActions.m_currentNote = (LandMarkNote)point.getValue();
        MapDialogActions.m_noteText = PropertiesProvider.getInstance().getStringProperty("mapPopupDescription");
        UIMapFrame.getInstance().setPopupEditing(true);
    }
    
    public static void onMapItemOver(final MapItemEvent e) {
        if (!UIMapFrame.getInstance().isPopupEditing()) {
            UIMapFrame.getInstance().displayPopup(e);
        }
        if (canZoomIn()) {
            return;
        }
        MapDialogActions.m_overPoint = e.getDisplayableMapPoint();
        updateCursor();
    }
    
    public static void onMapItemOut(final MapItemEvent e) {
        if (!UIMapFrame.getInstance().isPopupEditing()) {
            UIMapFrame.getInstance().hidePopup();
        }
        MapDialogActions.m_overPoint = null;
        updateCursor();
    }
    
    public static void onKeyPress(final KeyEvent e) {
        if (e.getKeyCode() == 127) {
            deleteNote(e);
            deleteWorldPositionMarker(e);
        }
    }
    
    public static void onTextEditorChange(final KeyEvent e) {
        final TextEditor te = e.getTarget();
        MapDialogActions.m_noteText = te.getText();
    }
    
    public static void onTextEditorKeyPress(final KeyEvent e) {
        if (e.getKeyCode() == 10) {
            applyNote(e);
        }
    }
    
    public static void applyNote(final Event e) {
        if (MapDialogActions.m_currentNote == null) {
            return;
        }
        final String moderatedString = WakfuWordsModerator.makeValidSentence(MapDialogActions.m_noteText);
        if (moderatedString.length() == 0 && MapDialogActions.m_noteText.length() != 0) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.censoredSentence"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
            setCurrentNote(null);
            hideLandMarkEditablePopup();
            return;
        }
        MapDialogActions.m_currentNote.setNote(moderatedString);
        LandMarkNoteManager.getInstance().saveNotes();
        MapManager.getInstance().getLandMarkHandler().updatePersonnalNotePoint(MapDialogActions.m_currentNote);
        setCurrentNote(null);
        hideLandMarkEditablePopup();
    }
    
    public static boolean deleteNote(final Event e) {
        if (MapDialogActions.m_currentNote == null) {
            return true;
        }
        deleteNote(MapDialogActions.m_currentNote);
        hideLandMarkEditablePopup();
        return true;
    }
    
    private static void hideLandMarkEditablePopup() {
        if (MapDialogActions.m_editedPoint != null) {
            MapDialogActions.m_editedPoint.setBeingEdited(false);
            MapDialogActions.m_editedPoint = null;
        }
        UIMapFrame.getInstance().setPopupEditing(false);
        UIMapFrame.getInstance().hidePopup();
    }
    
    public static boolean cancelNoteModification(final Event e) {
        hideLandMarkEditablePopup();
        return true;
    }
    
    public static void deleteNote(final LandMarkNote note) {
        MapManager.getInstance().getLandMarkHandler().removeNote(note);
        setCurrentNote(null);
        MapDialogActions.m_state = 0;
    }
    
    public static void deleteWorldPositionMarker(final Event e) {
        if (MapDialogActions.m_currentMapPositionMarker != null) {
            MapManager.getInstance().removeCompassPointAndPositionMarker(2, MapDialogActions.m_currentMapPositionMarker.m_id);
            setCurrentWorldPositionMarker(null);
        }
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
        MapDialogActions.m_currentNote = null;
        MapDialogActions.m_state = 0;
    }
    
    public static void onMapDrag(final DragEvent e) {
        final DisplayableMapPoint point = (DisplayableMapPoint)e.getValue();
        if (point.getValue() instanceof LandMarkNote) {
            MapDialogActions.m_currentNote = (LandMarkNote)point.getValue();
            MapManager.getInstance().getLandMarkHandler().removePersonalNotePoint(MapDialogActions.m_currentNote, true);
            MapDialogActions.m_state = 3;
        }
        else {
            MapDialogActions.m_state = 4;
            MapManager.getInstance().removeCompassPointAndPositionMarker();
        }
    }
    
    public static void onLandMarkMousePress(final Event e) {
        if (MapDialogActions.m_mouseOverLandMark) {
            MapDialogActions.m_mouseIsPressed = true;
            updateCursor();
        }
    }
    
    public static void onLandMarkMouseRelease(final Event e) {
        MapDialogActions.m_mouseIsPressed = false;
        updateCursor();
    }
    
    public static void onLandMarkDrag(final DragEvent e) {
        MapDialogActions.m_currentGfx = (ClientMapHandler.LandMarkGfx)e.getValue();
        MapDialogActions.m_state = 1;
        updateCursor();
    }
    
    public static void onLandMarkDropOut(final Event e) {
        MapDialogActions.m_state = 0;
        MapDialogActions.m_mouseIsPressed = false;
        updateCursor();
    }
    
    public static void onLandMarkItemOver(final ItemEvent e) {
        final Object value = e.getItemValue();
        if (value != null) {
            MapDialogActions.m_mouseOverLandMark = true;
            updateCursor();
        }
    }
    
    public static void onCompassMousePress(final Event e) {
        if (MapDialogActions.m_mouseOverLandMark) {
            MapDialogActions.m_mouseIsPressed = true;
            updateCursor();
        }
    }
    
    public static void onCompassMouseRelease(final Event e) {
        MapDialogActions.m_mouseIsPressed = false;
        updateCursor();
    }
    
    public static void onCompassItemOut(final ItemEvent e) {
        MapDialogActions.m_mouseOverLandMark = false;
        updateCursor();
    }
    
    public static void onCompassDrag(final DragEvent e) {
        MapDialogActions.m_currentGfx = (ClientMapHandler.LandMarkGfx)e.getValue();
        MapDialogActions.m_state = 2;
        updateCursor();
    }
    
    public static void onCompassDropOut(final Event e) {
        MapDialogActions.m_state = 0;
        MapDialogActions.m_mouseIsPressed = false;
        updateCursor();
    }
    
    public static void onCompassItemOver(final ItemEvent e) {
        final Object value = e.getItemValue();
        if (value != null) {
            MapDialogActions.m_mouseOverLandMark = true;
            updateCursor();
        }
    }
    
    public static void onLandMarkItemOut(final ItemEvent e) {
        MapDialogActions.m_mouseOverLandMark = false;
        updateCursor();
    }
    
    public static void onDropOut(final DropOutEvent e) {
        final DisplayableMapPoint point = (DisplayableMapPoint)e.getValue();
        if (point.getValue() instanceof LandMarkNote) {
            deleteNote((LandMarkNote)point.getValue());
        }
        MapDialogActions.m_state = 0;
        MapDialogActions.m_mouseIsPressed = false;
        updateCursor();
    }
    
    public static void onDrop(final DropEvent e) {
        final MapOverlay map = (MapOverlay)e.getDroppedInto();
        final Point2i pos = map.getIsoMousePosition();
        switch (MapDialogActions.m_state) {
            case 1: {
                final LandMarkNote note = MapManager.getInstance().getLandMarkHandler().addNote(pos.getX(), pos.getY(), MapDialogActions.m_currentGfx.getGfxId());
                setCurrentNote(note);
                WakfuSoundManager.getInstance().playGUISound(600119L);
                MapManager.getInstance().setSecondaryMapToSelectedMapZone();
                break;
            }
            case 3: {
                dropLandMark(pos.getX(), pos.getY(), MapDialogActions.m_currentNote);
                break;
            }
            case 2: {
                MapManager.getInstance().addCompassPointAndPositionMarker(pos.getX(), pos.getY(), 0.0f, MapManager.getInstance().getMap(), null, false);
                break;
            }
            case 4: {
                MapManager.getInstance().addCompassPointAndPositionMarker(pos.getX(), pos.getY(), 0.0f, MapManager.getInstance().getMap(), null, false);
                break;
            }
        }
        MapDialogActions.m_state = 0;
    }
    
    public static boolean mapDropValidate(final DragNDropContainer sourceContainer, final Object sourceValue, final DragNDropContainer destContainer, final Object destValue, final Object value) {
        return canDragNDrop();
    }
    
    public static void reset() {
        MapDialogActions.m_state = 0;
        GraphicalMouseManager.getInstance().hide();
        if (MapDialogActions.m_editedPoint != null) {
            MapDialogActions.m_editedPoint.setBeingEdited(false);
        }
        MapDialogActions.m_overPoint = null;
        MapDialogActions.m_currentMapZone = null;
        MapDialogActions.m_mouseInMap = false;
        MapDialogActions.m_mouseIsPressed = false;
        MapDialogActions.m_mouseOverLandMark = false;
        CursorFactory.getInstance().unlock();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapDialogActions.class);
        MapDialogActions.m_currentNote = null;
        MapDialogActions.m_currentMapPositionMarker = null;
        MapDialogActions.m_state = 0;
        MapDialogActions.m_uid = 0L;
        MapDialogActions.m_currentGfx = null;
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
            this.m_id = MapDialogActions.m_uid++;
        }
    }
}
