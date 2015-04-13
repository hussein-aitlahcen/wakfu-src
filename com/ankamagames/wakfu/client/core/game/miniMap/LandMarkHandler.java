package com.ankamagames.wakfu.client.core.game.miniMap;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import java.util.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.xulor2.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.landMarks.agtEnum.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.dungeon.*;
import com.ankamagames.wakfu.common.game.lock.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.basicDungeon.*;
import com.ankamagames.wakfu.client.core.landMarks.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class LandMarkHandler extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    private static final float[] COLOR;
    public static final String POINTS = "points";
    public static final String COMPASS = "compass";
    private LandMarkFilter m_filters;
    protected final IntObjectLightWeightMap<TLongObjectHashMap<DisplayableMapPoint>> m_points;
    protected final ArrayList<DisplayableMapPoint> m_pointsArray;
    protected final IntObjectLightWeightMap<TLongObjectHashMap<DisplayableMapPoint>> m_compasses;
    protected final ArrayList<DisplayableMapPoint> m_compassesArray;
    protected final TLongObjectHashMap<DisplayableMapPoint> m_landMarks;
    private final TByteObjectHashMap<ArrayList<DisplayableMapPoint>> m_allLandMarks;
    private final WakfuLandMarkMap m_landMarkMap;
    private DisplayableMapPointProcessor m_mapPointProcessor;
    private MapOverlay m_widget;
    private final TShortArrayList m_maps;
    
    public LandMarkHandler() {
        super();
        this.m_filters = new LandMarkFilter();
        this.m_points = new IntObjectLightWeightMap<TLongObjectHashMap<DisplayableMapPoint>>();
        this.m_pointsArray = new ArrayList<DisplayableMapPoint>();
        this.m_compasses = new IntObjectLightWeightMap<TLongObjectHashMap<DisplayableMapPoint>>();
        this.m_compassesArray = new ArrayList<DisplayableMapPoint>();
        this.m_landMarks = new TLongObjectHashMap<DisplayableMapPoint>();
        this.m_allLandMarks = new TByteObjectHashMap<ArrayList<DisplayableMapPoint>>();
        this.m_landMarkMap = new WakfuLandMarkMap();
        this.m_mapPointProcessor = DisplayableMapPointProcessor.DEFAULT;
        this.m_maps = new TShortArrayList();
    }
    
    @Override
    public String[] getFields() {
        return LandMarkHandler.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("compass")) {
            return this.m_compassesArray;
        }
        if (fieldName.equals("points")) {
            return this.m_pointsArray;
        }
        return null;
    }
    
    public void setWidget(final MapOverlay widget) {
        this.m_widget = widget;
        if (this.m_widget != null) {
            final TLongObjectIterator<DisplayableMapPoint> it = this.m_landMarks.iterator();
            while (it.hasNext()) {
                it.advance();
                this.m_widget.addLandMark(it.value());
            }
        }
    }
    
    public void setHavenWorld() {
        this.clearAllPoints();
        final HavenWorldTopology havenWorld = HavenWorldManager.INSTANCE.getHavenWorld();
        final ArrayList<DisplayableMapPoint> points = new ArrayList<DisplayableMapPoint>();
        this.m_allLandMarks.put(LandMarkEnum.HAVEN_WORLD_BUILDING.getType(), points);
        HavenWorldEntityCreator.addBuildingsDisplayPoints(havenWorld, points);
        this.loadPoints();
    }
    
    public DisplayableMapPointProcessor getMapPointProcessor() {
        return this.m_mapPointProcessor;
    }
    
    public void setMapPointProcessor(final DisplayableMapPointProcessor mapPointProcessor) {
        this.m_mapPointProcessor = mapPointProcessor;
    }
    
    public void loadMaps(final TShortArrayList mapList) {
        this.clearAllPoints();
        this.m_maps.add(mapList.toNativeArray());
        for (int i = 0, size = mapList.size(); i < size; ++i) {
            String path;
            try {
                path = String.format(WakfuConfiguration.getInstance().getString("mapsPoiPath"), mapList.get(i));
            }
            catch (PropertyException e) {
                LandMarkHandler.m_logger.error((Object)"Probl\u00e8me lors de la lecture de mapsPoiPath");
                return;
            }
            try {
                this.m_landMarkMap.loadFull(ExtendedDataInputStream.wrap(ContentFileHelper.readFile(path)), false);
            }
            catch (IOException e2) {
                LandMarkHandler.m_logger.error((Object)("Probl\u00e8me au chargement du fichier de LandMarks " + path));
                return;
            }
        }
        this.loadPoints();
    }
    
    public TShortArrayList getMaps() {
        return this.m_maps;
    }
    
    private String replaceDefaultInString(final String format, final CharSequence defaultValue) {
        return format.replace("[default]", defaultValue);
    }
    
    private LandMarkDescriptionDef selectDescription(final LandMarkDef def) {
        final ArrayList<LandMarkDescriptionDef> descs = def.m_descriptions;
        if (descs == null) {
            return null;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        for (int i = 0, size = descs.size(); i < size; ++i) {
            final LandMarkDescriptionDef desc = descs.get(i);
            if (desc.m_criterion == null || desc.m_criterion.isValid(player, null, null, player.getAppropriateContext())) {
                return desc;
            }
        }
        return null;
    }
    
    private void loadPoints() {
        try {
            this.addAllLandmarks();
            for (int i = 0, size = this.m_maps.size(); i < size; ++i) {
                this.addPersonnalNotes(this.m_maps.get(i));
            }
            this.populateLandMarksWithFilters();
        }
        catch (Exception e) {
            LandMarkHandler.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    public void clearAllPoints() {
        this.removeAllLandMarks();
        this.m_allLandMarks.clear();
        this.m_landMarkMap.clear();
        this.m_maps.clear();
    }
    
    private void addAllLandmarks() {
        final TIntObjectIterator<LandMarkDef> it = this.m_landMarkMap.getLandMarkDef().iterator();
        while (it.hasNext()) {
            it.advance();
            this.addLandMark(it.value());
        }
    }
    
    private void addLandMark(final LandMarkDef def) {
        final LandMarkDescriptionDef descriptionDef = this.selectDescription(def);
        if (descriptionDef == null) {
            return;
        }
        final int score = getPointScore(def);
        if (score > 0) {
            return;
        }
        DisplayableMapPoint point;
        if (def.m_exportType == LandMarkExportType.STATIC.getId()) {
            point = this.addStaticLandMark(def, descriptionDef);
        }
        else if (def.m_exportType == LandMarkExportType.IE.getId()) {
            point = this.addIELandMark(def, descriptionDef);
        }
        else if (def.m_exportType == LandMarkExportType.DUNGEONS.getId()) {
            point = this.addDungeonLandMark(def, descriptionDef);
        }
        else if (def.m_exportType == LandMarkExportType.PROTECTORS.getId()) {
            point = this.addProtectorLandMark(def, descriptionDef);
        }
        else {
            point = null;
        }
        if (point == null) {
            return;
        }
        if (score == 0) {
            point.setOverlayIcon(DisplayableMapPointIconFactory.NEW_ICON);
        }
    }
    
    private short getInstanceId() {
        return this.m_maps.get(0);
    }
    
    private DisplayableMapPoint addStaticLandMark(final LandMarkDef def, final LandMarkDescriptionDef descriptionDef) {
        final DisplayableMapPointIcon icon = DisplayableMapPointIconFactory.INSTANCE.getIcon(descriptionDef.m_iconId);
        final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(def.m_type, true);
        final DisplayableMapPointIcon alternateIcon = (descriptionDef.m_iconId == 46) ? icon : null;
        if (icon == null) {
            return null;
        }
        final DisplayableMapPoint point = new DisplayableMapPoint(def.m_x, def.m_y, def.m_z, this.getInstanceId(), WakfuTranslator.getInstance().getString(35, descriptionDef.m_id, new Object[0]), def, icon, alternateIcon, LandMarkHandler.COLOR, false, false);
        points.add(point);
        return point;
    }
    
    private DisplayableMapPoint addIELandMark(final LandMarkDef def, final LandMarkDescriptionDef descriptionDef) {
        final InteractiveElementDef iedef = this.m_landMarkMap.getIE(def.m_exportTypeLinkedId);
        if (iedef == null) {
            return null;
        }
        final WakfuClientMapInteractiveElement element = ((InteractiveElementFactory<WakfuClientMapInteractiveElement, C>)WakfuClientInteractiveElementFactory.getInstance()).createDummyInteractiveElement(iedef.m_id, iedef.m_type, iedef.m_data);
        if (element == null) {
            return null;
        }
        final DisplayableMapPointIcon icon = DisplayableMapPointIconFactory.INSTANCE.getIcon((descriptionDef.m_iconId == -1) ? iedef.m_landMarkType : descriptionDef.m_iconId);
        if (icon == null) {
            return null;
        }
        final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(WakfuClientInteractiveElementTypes.getFromId(iedef.m_type).getLandMarkType().getType(), true);
        final String name = this.replaceDefaultInString(WakfuTranslator.getInstance().getString(35, descriptionDef.m_id, new Object[0]), element.getName());
        final DisplayableMapPoint point = new DisplayableMapPoint(def.m_x, def.m_y, def.m_z, this.getInstanceId(), name, iedef, icon, LandMarkHandler.COLOR);
        points.add(point);
        element.release();
        return point;
    }
    
    private DisplayableMapPoint addProtectorLandMark(final LandMarkDef def, final LandMarkDescriptionDef desc) {
        Protector protector = ProtectorManager.INSTANCE.getProtector(def.m_exportTypeLinkedId);
        if (protector == null) {
            protector = ProtectorManager.INSTANCE.getStaticProtector(def.m_exportTypeLinkedId);
        }
        if (protector == null) {
            return null;
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        ProtectorDescriptionHelper.describeProtectorAndTerritory(sb, protector);
        final String name = this.replaceDefaultInString(WakfuTranslator.getInstance().getString(35, desc.m_id, new Object[0]), sb.finishAndToString());
        final DisplayableMapPointIcon icon = DisplayableMapPointIconFactory.INSTANCE.getIcon((desc.m_iconId == -1) ? LandMarkEnum.PROTECTORS.getType() : desc.m_iconId);
        if (icon == null) {
            return null;
        }
        final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(LandMarkEnum.PROTECTORS.getType(), true);
        final DisplayableMapPoint point = new DisplayableMapPoint(def.m_x, def.m_y, def.m_z, this.getInstanceId(), name, def, icon, LandMarkHandler.COLOR);
        points.add(point);
        return point;
    }
    
    @Nullable
    private DisplayableMapPoint addDungeonLandMark(final LandMarkDef def, final LandMarkDescriptionDef desc) {
        final DungeonDefinition dungeon = DungeonManager.INSTANCE.getDungeonByTpId(def.m_exportTypeLinkedId);
        if (dungeon == null) {
            return null;
        }
        final int lock = LockManager.INSTANCE.getLockByLockedItemId(dungeon.getId());
        if (LockManager.INSTANCE.isLocked(lock)) {
            return null;
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(WakfuTranslator.getInstance().getString(137, dungeon.getId(), new Object[0]));
        sb.newLine().append(WakfuTranslator.getInstance().getString("required.level.custom", dungeon.getMinLevel()));
        if (lock != -1) {
            final String lockText = LockHelper.getLockText(WakfuGameEntity.getInstance().getLocalPlayer(), lock);
            if (!lockText.isEmpty()) {
                sb.newLine().append(lockText);
            }
        }
        final String name = this.replaceDefaultInString(WakfuTranslator.getInstance().getString(35, desc.m_id, new Object[0]), sb.finishAndToString());
        final DisplayableMapPointIcon icon = DisplayableMapPointIconFactory.INSTANCE.getIcon((desc.m_iconId == -1) ? LandMarkEnum.DUNGEONS.getType() : desc.m_iconId);
        if (icon == null) {
            return null;
        }
        final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(LandMarkEnum.DUNGEONS.getType(), true);
        final DisplayableMapPoint point = new DisplayableMapPoint(def.m_x, def.m_y, def.m_z, this.getInstanceId(), name, def, icon, LandMarkHandler.COLOR);
        point.setUseGrayScale(WakfuClientInstance.getGameEntity().getLocalPlayer().getLevel() < dungeon.getMinLevel());
        points.add(point);
        return point;
    }
    
    private static int getPointScore(final LandMarkDef def) {
        if (def.m_versionMajor > 1) {
            return 1;
        }
        if (def.m_versionMajor == 1 && def.m_versionMinor > 39) {
            return 1;
        }
        if (def.m_versionMajor == 1 && def.m_versionMinor == 39) {
            return 0;
        }
        return -1;
    }
    
    private void displayInteractiveElements(final ArrayList<InteractiveElementDef> interactiveElementDefs) {
        final WakfuClientInteractiveElementFactory elementFactory = WakfuClientInteractiveElementFactory.getInstance();
        for (int i = 0, size = interactiveElementDefs.size(); i < size; ++i) {
            final InteractiveElementDef def = interactiveElementDefs.get(i);
            final WakfuClientMapInteractiveElement element = ((InteractiveElementFactory<WakfuClientMapInteractiveElement, C>)elementFactory).createDummyInteractiveElement(def.m_id, def.m_type, def.m_data);
            if (element != null) {
                final DisplayableMapPointIcon icon = DisplayableMapPointIconFactory.INSTANCE.getIcon(def.m_landMarkType);
                if (icon != null) {
                    final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(WakfuClientInteractiveElementTypes.getFromId(def.m_type).getLandMarkType().getType(), true);
                    points.add(new DisplayableMapPoint(element.getWorldCellX(), element.getWorldCellY(), element.getWorldCellAltitude(), this.getInstanceId(), element.getName(), def, icon, LandMarkHandler.COLOR));
                    element.release();
                }
            }
        }
    }
    
    private void addPersonnalNotes(final short map) {
        final TIntObjectIterator<LandMarkNote> it = LandMarkNoteManager.getInstance().getDisplayedNotes(map).iterator();
        while (it.hasNext()) {
            it.advance();
            this.addPersonalNotePoint(it.value(), false);
        }
    }
    
    public void populateLandMarksWithFilters(final LandMarkEnum type) {
        final LandMarkFilterElement filter = this.m_filters.getFilter(type.getType());
        if (filter == null) {
            return;
        }
        final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(filter.getId(), false);
        if (points == null) {
            return;
        }
        for (int i = 0, size = points.size(); i < size; ++i) {
            final DisplayableMapPoint mapPoint = points.get(i);
            this.m_mapPointProcessor.process(mapPoint);
            if (filter.isSelected(mapPoint)) {
                this.addLandMark(mapPoint);
            }
            else {
                this.removeLandMark(mapPoint);
            }
        }
    }
    
    public void populateLandMarksWithFilters() {
        for (final LandMarkEnum type : LandMarkEnum.values()) {
            this.populateLandMarksWithFilters(type);
        }
    }
    
    private void removeLandMark(final DisplayableMapPoint mapPoint) {
        if (this.m_landMarks.remove(mapPoint.getValue().hashCode()) != null && this.m_widget != null) {
            this.m_widget.removeLandMark(mapPoint);
        }
    }
    
    private void addLandMark(final DisplayableMapPoint mapPoint) {
        if (this.m_landMarks.put(mapPoint.getValue().hashCode(), mapPoint) == null && this.m_widget != null) {
            this.m_widget.addLandMark(mapPoint);
        }
    }
    
    private boolean hasLandmarks() {
        return !this.m_allLandMarks.isEmpty();
    }
    
    private ArrayList<DisplayableMapPoint> getLandMarksOfType(final byte type, final boolean createIfNeeded) {
        ArrayList<DisplayableMapPoint> points = this.m_allLandMarks.get(type);
        if (createIfNeeded && points == null) {
            points = new ArrayList<DisplayableMapPoint>();
            this.m_allLandMarks.put(type, points);
        }
        return points;
    }
    
    public void selectAllFilters() {
        try {
            this.m_filters.selectAllFilters();
            this.populateLandMarksWithFilters();
        }
        catch (Exception e) {
            LandMarkHandler.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    public void unselectAllFilters() {
        this.m_filters.unselectAllFilters();
        this.populateLandMarksWithFilters();
    }
    
    public void updateFiltersFromPreferences() {
        this.m_filters.updateFromPreferences();
    }
    
    public void setFilters(final LandMarkFilter filters) {
        this.m_filters = filters;
        this.populateLandMarksWithFilters();
    }
    
    public LandMarkFilter getFilters() {
        return this.m_filters;
    }
    
    public void setFilterTypeSelected(final byte filterType, final boolean selected) {
        this.m_filters.setSelected(filterType, selected);
        this.populateLandMarksWithFilters();
    }
    
    public void updateLandMarkType(final LandMarkEnum type) {
        final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(type.getType(), false);
        if (points == null) {
            return;
        }
        for (int i = 0, size = points.size(); i < size; ++i) {
            final DisplayableMapPoint mapPoint = points.remove(0);
            this.removeLandMark(mapPoint);
        }
        final TIntObjectHashMap<LandMarkDef> landMarkDefs = this.m_landMarkMap.getLandMarkDef();
        final TIntObjectIterator<LandMarkDef> it = landMarkDefs.iterator();
        while (it.hasNext()) {
            it.advance();
            final LandMarkDef def = it.value();
            if (def.m_type == type.getType()) {
                this.addLandMark(def);
            }
        }
        this.populateLandMarksWithFilters(type);
    }
    
    public void removePersonalNotePoint(final LandMarkNote note, final boolean updateFromFilters) {
        if (note == null) {
            return;
        }
        final ArrayList<DisplayableMapPoint> points = this.getLandMarksOfType(LandMarkEnum.PERSONAL_NOTE.getType(), false);
        if (points == null) {
            return;
        }
        for (int i = 0, size = points.size(); i < size; ++i) {
            final DisplayableMapPoint mapPoint = points.get(i);
            if (mapPoint.getValue() == note) {
                points.remove(i);
                this.removeLandMark(mapPoint);
                break;
            }
        }
        if (updateFromFilters) {
            this.populateLandMarksWithFilters();
        }
    }
    
    public void addPersonalNotePoint(final LandMarkNote note) {
        this.addPersonalNotePoint(note, true);
    }
    
    private void addPersonalNotePoint(final LandMarkNote note, final boolean updateFromFilters) {
        if (note == null) {
            return;
        }
        final DisplayableMapPointIcon icon = DisplayableMapPointIconFactory.INSTANCE.getIcon(note.getGfxId());
        if (icon == null) {
            return;
        }
        final ArrayList<DisplayableMapPoint> personalNotesPoints = this.getLandMarksOfType(LandMarkEnum.PERSONAL_NOTE.getType(), true);
        personalNotesPoints.add(new DisplayableMapPoint(note.getX(), note.getY(), 0.0f, this.getInstanceId(), note.getNote(), note, icon, DisplayableMapPointIconFactory.DEFAULT_SMALL_ICON, LandMarkHandler.COLOR, true, true));
        if (updateFromFilters) {
            this.populateLandMarksWithFilters();
        }
    }
    
    public LandMarkNote addNote(final int x, final int y, final int gfxId, final String text) {
        final LandMarkNote note = LandMarkNoteManager.getInstance().addNote(x, y, text, gfxId);
        this.addPersonalNotePoint(note);
        return note;
    }
    
    public LandMarkNote addNote(final int x, final int y, final int gfxId) {
        return this.addNote(x, y, gfxId, WakfuTranslator.getInstance().getString("desc.landMark.personalNote"));
    }
    
    public void removeNote(final LandMarkNote note) {
        if (!LandMarkNoteManager.getInstance().removeNote(note)) {
            return;
        }
        this.removePersonalNotePoint(note, true);
    }
    
    public void updatePersonnalNotePoint(final LandMarkNote note) {
        final ArrayList<DisplayableMapPoint> list = this.getLandMarksOfType(LandMarkEnum.PERSONAL_NOTE.getType(), false);
        if (list == null) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; --i) {
            final DisplayableMapPoint p = list.get(i);
            if (note == p.getValue()) {
                p.setName(note.getNote());
            }
        }
    }
    
    public void addPoint(final long referenceId, final int type, final float worldX, final float worldY, final float altitude, final String name, final Object value, final DisplayableMapPointIcon icon, final String particlePath, final float[] color, final short instanceId) {
        if (icon == null) {
            return;
        }
        TLongObjectHashMap<DisplayableMapPoint> points = this.m_points.get(type);
        if (points == null) {
            points = new TLongObjectHashMap<DisplayableMapPoint>();
            this.m_points.put(type, points);
        }
        final DisplayableMapPoint mapPoint = points.get(referenceId);
        if (mapPoint != null) {
            mapPoint.setDesiredIso(worldX, worldY, altitude);
            mapPoint.setIcon(icon);
            mapPoint.setColor(color);
            mapPoint.setParticlePath(particlePath);
        }
        else {
            points.put(referenceId, new DisplayableMapPoint(worldX, worldY, altitude, instanceId, name, value, icon, particlePath, color));
        }
        this.firePointsFieldChanged();
    }
    
    public void addCompassPoint(final long referenceId, final int type, final float worldX, final float worldY, final float altitude, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final float[] color) {
        if (icon == null) {
            return;
        }
        TLongObjectHashMap<DisplayableMapPoint> points = this.m_compasses.get(type);
        if (points == null) {
            points = new TLongObjectHashMap<DisplayableMapPoint>();
            this.m_compasses.put(type, points);
        }
        final DisplayableMapPoint mapPoint = points.get(referenceId);
        if (mapPoint != null) {
            mapPoint.setDesiredIso(worldX, worldY, altitude);
            mapPoint.setIcon(icon);
            mapPoint.setColor(color);
        }
        else {
            points.put(referenceId, new DisplayableMapPoint(worldX, worldY, altitude, instanceId, name, value, icon, color));
        }
        this.fireCompassPointsFieldChanged();
    }
    
    public void setUniqueCompassPoint(final float worldX, final float worldY, final float altitude, final short instanceId, final String name, final Object value, final DisplayableMapPointIcon icon, final float[] color) {
        final TLongObjectHashMap<DisplayableMapPoint> map = this.m_compasses.get(5);
        if (map != null) {
            map.clear();
        }
        this.addCompassPoint(0L, 5, worldX, worldY, altitude, instanceId, name, value, icon, color);
    }
    
    protected DisplayableMapPoint getUniqueCompassPoint() {
        final TLongObjectHashMap<DisplayableMapPoint> map = this.m_compasses.get(5);
        if (map == null) {
            return null;
        }
        return map.get(0L);
    }
    
    void updatePoint(final long referenceId, final int type, final float worldX, final float worldY, final float altitude, final short instanceId) {
        if (this.updatePoints(referenceId, type, worldX, worldY, altitude, instanceId, this.m_points)) {
            this.firePointsFieldChanged();
        }
        if (this.updatePoints(referenceId, type, worldX, worldY, altitude, instanceId, this.m_compasses)) {
            this.fireCompassPointsFieldChanged();
        }
    }
    
    private boolean updatePoints(final long referenceId, final int type, final float worldX, final float worldY, final float altitude, final short instanceId, final IntObjectLightWeightMap<TLongObjectHashMap<DisplayableMapPoint>> points) {
        final TLongObjectHashMap<DisplayableMapPoint> compasses = points.get(type);
        if (compasses == null) {
            return false;
        }
        final DisplayableMapPoint mapPoint = compasses.get(referenceId);
        if (mapPoint != null) {
            mapPoint.setDesiredIso(worldX, worldY, altitude);
            mapPoint.setInstanceId(instanceId);
            return true;
        }
        return false;
    }
    
    public void removePoint(final int type, final long referenceId) {
        final TLongObjectHashMap<DisplayableMapPoint> points = this.m_points.get(type);
        if (points == null) {
            return;
        }
        if (points.remove(referenceId) != null) {
            this.firePointsFieldChanged();
        }
    }
    
    public void removeCompass(final int type, final long referenceId) {
        final TLongObjectHashMap<DisplayableMapPoint> points = this.m_compasses.get(type);
        if (points == null) {
            return;
        }
        if (points.remove(referenceId) != null) {
            this.fireCompassPointsFieldChanged();
        }
    }
    
    public void removeUniqueCompass() {
        final TLongObjectHashMap<DisplayableMapPoint> points = this.m_compasses.get(5);
        if (points == null) {
            return;
        }
        if (points.remove(0L) != null) {
            this.fireCompassPointsFieldChanged();
        }
    }
    
    public void removeLandMark(final long referenceId) {
        final DisplayableMapPoint dmp = this.m_landMarks.remove(referenceId);
        this.removeLandMark(dmp);
    }
    
    public void removeAllLandMarks() {
        if (this.m_widget != null) {
            this.m_widget.clearLandMarks();
        }
        this.m_landMarks.clear();
    }
    
    private void firePointsFieldChanged() {
        this.m_pointsArray.clear();
        for (int i = 0, size = this.m_points.size(); i < size; ++i) {
            final TLongObjectHashMap<DisplayableMapPoint> points = this.m_points.getQuickValue(i);
            final TLongObjectIterator<DisplayableMapPoint> it = points.iterator();
            while (it.hasNext()) {
                it.advance();
                this.m_pointsArray.add(it.value());
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "points");
    }
    
    private void fireCompassPointsFieldChanged() {
        this.m_compassesArray.clear();
        for (int i = 0, size = this.m_compasses.size(); i < size; ++i) {
            final TLongObjectHashMap<DisplayableMapPoint> points = this.m_compasses.getQuickValue(i);
            final TLongObjectIterator<DisplayableMapPoint> it = points.iterator();
            while (it.hasNext()) {
                it.advance();
                this.m_compassesArray.add(it.value());
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "compass");
    }
    
    public void removeAllPointsOfType(final int type) {
        if (this.m_points.remove(type) != null) {
            this.firePointsFieldChanged();
        }
    }
    
    public void removeAll() {
        if (this.m_points != null) {
            final int len = this.m_points.size();
            this.m_points.clear();
            this.m_pointsArray.clear();
            LandMarkHandler.m_logger.info((Object)("Nettoyage des points contenus dans la map (" + this.m_points.size() + " restants sur " + len + ")"));
        }
        if (this.m_compasses != null) {
            final int len = this.m_compasses.size();
            this.m_compasses.clear();
            this.m_compassesArray.clear();
            LandMarkHandler.m_logger.info((Object)("Nettoyage des points contenus dans la map (" + this.m_compasses.size() + " restants sur " + len + ")"));
        }
        LandMarkHandler.m_logger.info((Object)"Nettoyage de la boussole contenue dans la map");
        if (this.m_landMarks != null) {
            final int len = this.m_landMarks.size();
            this.m_landMarks.clear();
            if (this.m_widget != null) {
                this.m_widget.clearLandMarks();
            }
            LandMarkHandler.m_logger.info((Object)("Nettoyage des rep\u00e8res contenus dans la map (" + this.m_landMarks.size() + " restants sur " + len + ")"));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LandMarkHandler.class);
        COLOR = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    }
}
