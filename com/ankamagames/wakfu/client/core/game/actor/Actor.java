package com.ankamagames.wakfu.client.core.game.actor;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.IsoWorldTarget.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import gnu.trove.*;

public abstract class Actor extends PathMobile implements InventoryObserver, InteractiveIsoWorldTarget
{
    private static final Logger m_logger;
    public static final int NON_STOP_MOVEMENT_SEND_INTERVAL = 2000;
    public static final int STOP_MOVEMENT_SEND_INTERVAL = 1500;
    public final ActionInProgress m_actionInProgress;
    private boolean m_emitter;
    private Direction8Path m_lastPathSent;
    private Direction8Path m_bufferedPath;
    private long m_lastPathSentTime;
    private static int TOKEN_GENERATOR;
    private int m_scheduledToken;
    private boolean m_waitEndAnimation;
    private final List<MobileStartPathListener> m_startListener;
    private final TShortObjectHashMap<Anm> m_equipedPositions;
    protected static final PathFinderParameters PATHFINDER_PARAMETERS;
    private static final TopologyMapInstanceSet PATHFINDER_MAPINSTANCESET;
    
    public Actor(final long id) {
        super(id);
        this.m_actionInProgress = new ActionInProgress();
        this.m_startListener = new ArrayList<MobileStartPathListener>();
        this.m_equipedPositions = new TShortObjectHashMap<Anm>(EquipmentType.values().length);
        this.m_jumpCapacity = 4;
    }
    
    @Override
    public void addSelectionChangedListener(final InteractiveElementSelectionChangeListener listener) {
        super.addSelectionChangedListener(listener);
    }
    
    public void applyEquipment(final int gfxId, final short position, final boolean hasGfx) {
        final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(position);
        if (equipmentType == null) {
            return;
        }
        this.unapplyEquipment(position);
        if (!hasGfx) {
            return;
        }
        try {
            final String equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
            final Anm equipment = AnimatedElement.loadEquipment(String.format(equipmentFileName, gfxId));
            this.applyParts(equipment, equipmentType.m_linkageCrc);
            this.m_equipedPositions.put(position, equipment);
        }
        catch (Exception e) {
            Actor.m_logger.error((Object)("Erreur au chargement de l'\u00e9quipment : " + gfxId), (Throwable)e);
        }
    }
    
    public void applyEquipment(final AbstractReferenceItem item, final short position, final byte sex) {
        final int gfxId = (sex == 0) ? item.getGfxId() : item.getFemaleGfxId();
        this.applyEquipment(gfxId, position, item.getItemType().isVisibleInAnimations());
    }
    
    public void applyPartsEquipment(final int gfxId) {
        try {
            final String equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
            final Anm equipment = AnimatedElement.loadEquipment(String.format(equipmentFileName, gfxId));
            this.applyParts(equipment, new int[0]);
        }
        catch (Exception e) {
            Actor.m_logger.error((Object)("Erreur au chargement de l'\u00e9quipment : " + gfxId), (Throwable)e);
        }
    }
    
    public void unApplyAllPartsEquipment() {
        if (this.getAnmInstance() == null) {
            return;
        }
        this.getAnmInstance().unapplyAllParts();
        this.forceUpdateEquipment();
    }
    
    @Override
    public void stopMoving() {
        if (this.m_currentPath == null) {
            return;
        }
        if (this.m_currentPathCell + 1 > this.m_currentPath.getPathLength()) {
            return;
        }
        final PathFindResult newPath = this.m_currentPath.subPath(this.m_currentPathCell, this.m_currentPathCell + 1);
        this.m_currentPathCell = 0;
        this.m_lastAirImpulsion = -1;
        this.applyPathResult(newPath, true);
        if (this.m_trajectories != null) {
            this.process(null, (int)this.m_trajectories.getFinalTime());
        }
    }
    
    protected PathFindResult checkDimensionalBagPath(final DimensionalBagView dimensionalBagView, final PathFindResult path) {
        return dimensionalBagView.checkPath(this.getId(), path);
    }
    
    public boolean applyPathResult(PathFindResult pathResult, final boolean stop) {
        for (final MobileStartPathListener mobileStartPathListener : new ArrayList(this.m_startListener)) {
            mobileStartPathListener.pathStarted(this, pathResult);
        }
        pathResult = this.alterPathInDimensionnalBag(pathResult);
        if (pathResult == null || !pathResult.isPathFound()) {
            if (Actor.DEBUG_TRAJECTORIES) {
                Actor.m_logger.warn((Object)"Aucun chemin n'a \u00e9t\u00e9 trouv\u00e9 pour le d\u00e9placement local.");
            }
            return false;
        }
        if (pathResult.getPathLength() > 1) {
            this.updateEmitter(pathResult, stop);
            if (Actor.DEBUG_TRAJECTORIES) {
                Actor.m_logger.trace((Object)("setPath: " + pathResult));
            }
            this.setPath(pathResult, false, true);
        }
        else {
            if (pathResult.getPathLength() != 1 || this.getCurrentPath() == null || this.getCurrentPath().getFirstStep() == null) {
                return false;
            }
            int[] previous = null;
            for (final int[] step : this.getCurrentPath()) {
                if (step[0] == pathResult.getFirstStep()[0] && step[1] == pathResult.getFirstStep()[1] && step[2] == pathResult.getFirstStep()[2]) {
                    final PathFindResult newResult = new PathFindResult(2);
                    if (previous != null) {
                        newResult.setStep(0, previous);
                        newResult.setStep(1, step);
                        this.updateEmitter(newResult, true);
                        this.setWorldPosition(step[0], step[1], step[2]);
                        Actor.m_logger.trace((Object)"Envoi d'un chemin 'pseudo-stop' au serveur");
                        break;
                    }
                    break;
                }
                else {
                    previous = step;
                }
            }
        }
        return true;
    }
    
    private PathFindResult alterPathInDimensionnalBag(final PathFindResult pathResult) {
        if (pathResult == null || !pathResult.isPathFound()) {
            return pathResult;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return null;
        }
        final DimensionalBagView dimensionalBag = localPlayer.getVisitingDimentionalBag();
        if (dimensionalBag == null) {
            return pathResult;
        }
        return this.checkDimensionalBagPath(dimensionalBag, pathResult);
    }
    
    public PathFindResult getPathResult(final Point3 to, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        return this.getPathResult(to.getX(), to.getY(), to.getZ(), stopBeforeEndCell, useDiagonal);
    }
    
    public PathFindResult getPathResult(final int toX, final int toY, final short toZ, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        final int worldX = this.getCurrentWorldX();
        final int worldY = this.getCurrentWorldY();
        this.prepareParameters(stopBeforeEndCell, useDiagonal);
        TopologyMapManager.getTopologyMapInstances(worldX, worldY, toX, toY, Actor.PATHFINDER_MAPINSTANCESET);
        final PathFinder pathFinder = PathFinder.checkOut();
        pathFinder.setMoverCaracteristics(this.getHeight(), this.getPhysicalRadius(), this.getJumpCapacity());
        pathFinder.setParameters(Actor.PATHFINDER_PARAMETERS);
        pathFinder.setStopCell(toX, toY, toZ);
        pathFinder.setTopologyMapInstanceSet(Actor.PATHFINDER_MAPINSTANCESET);
        pathFinder.addStartCell(worldX, worldY, this.getCurrentAltitude());
        pathFinder.findPath();
        final PathFindResult result = pathFinder.getPathResult();
        pathFinder.release();
        return result;
    }
    
    public PathFindResult getPathResult(final boolean stopBeforeEndCell, final boolean useDiagonal, final List<Point3> destinations) {
        if (destinations.isEmpty()) {
            return PathFindResult.EMPTY;
        }
        this.prepareParameters(stopBeforeEndCell, useDiagonal);
        final int worldX = this.getCurrentWorldX();
        final int worldY = this.getCurrentWorldY();
        final Point3 dest = destinations.get(0);
        TopologyMapManager.getTopologyMapInstances(worldX, worldY, dest.getX(), dest.getY(), Actor.PATHFINDER_MAPINSTANCESET);
        final PathFinder pathFinder = PathFinder.checkOut();
        final THashSet<Point3> done = new THashSet<Point3>(destinations.size());
        for (int i = 0; i < destinations.size(); ++i) {
            final Point3 pos = destinations.get(i);
            final short realZ = TopologyMapManager.getNearestWalkableZ(pos.getX(), pos.getY(), pos.getZ());
            if (realZ != -32768) {
                if (TopologyMapManager.isStoppableUpon(pos.getX(), pos.getY(), pos.getZ())) {
                    short destZ;
                    if (Math.abs(realZ - pos.getZ()) <= 2) {
                        destZ = realZ;
                    }
                    else {
                        destZ = pos.getZ();
                    }
                    if (done.add(new Point3(pos.getX(), pos.getY(), destZ))) {
                        pathFinder.addStartCell(pos.getX(), pos.getY(), destZ);
                    }
                }
            }
        }
        if (done.isEmpty()) {
            pathFinder.release();
            return PathFindResult.EMPTY;
        }
        pathFinder.setMoverCaracteristics(this.getHeight(), this.getPhysicalRadius(), this.getJumpCapacity());
        pathFinder.setParameters(Actor.PATHFINDER_PARAMETERS);
        pathFinder.setStopCell(worldX, worldY, this.getCurrentAltitude());
        pathFinder.setTopologyMapInstanceSet(Actor.PATHFINDER_MAPINSTANCESET);
        pathFinder.findPath();
        final PathFindResult result = pathFinder.getReversePathResult();
        pathFinder.release();
        return result;
    }
    
    private void prepareParameters(final boolean stopBeforeEndCell, final boolean useDiagonal) {
        Actor.PATHFINDER_PARAMETERS.m_stopBeforeEndCell = stopBeforeEndCell;
        Actor.PATHFINDER_PARAMETERS.m_limitTo4Directions = !useDiagonal;
        Actor.PATHFINDER_PARAMETERS.m_punishJump = true;
        Actor.PATHFINDER_PARAMETERS.m_punishDirectionChangeIn4D = Actor.PATHFINDER_PARAMETERS.m_limitTo4Directions;
        Actor.PATHFINDER_PARAMETERS.m_allowMoboSterile = this.allowMoboSterileMovement();
        Actor.PATHFINDER_PARAMETERS.m_allowGap = this.allowGaps();
    }
    
    public boolean moveNearTarget(final Target t, final boolean useDiagonal, final boolean stopOnAxisCell) {
        this.prepareParameters(false, useDiagonal);
        Actor.PATHFINDER_PARAMETERS.m_permissiveStartCellAltitude = true;
        final int worldX = this.getCurrentWorldX();
        final int worldY = this.getCurrentWorldY();
        TopologyMapManager.getTopologyMapInstances(worldX, worldY, t.getWorldCellX(), t.getWorldCellY(), Actor.PATHFINDER_MAPINSTANCESET);
        final PathFinder pathFinder = PathFinder.checkOut();
        pathFinder.setStopCell(worldX, worldY, this.getCurrentAltitude());
        final int xMin = t.getWorldCellX() - t.getPhysicalRadius() - 1;
        final int xMax = t.getWorldCellX() + t.getPhysicalRadius() + 1;
        final int yMin = t.getWorldCellY() - t.getPhysicalRadius() - 1;
        final int yMax = t.getWorldCellY() + t.getPhysicalRadius() + 1;
        for (int x = xMin; x <= xMax; ++x) {
            for (int y = yMin; y <= yMax; ++y) {
                if (stopOnAxisCell && (x == xMin || x == xMax)) {
                    if (y == yMin) {
                        continue;
                    }
                    if (y == yMax) {
                        continue;
                    }
                }
                final TopologyMap map = Actor.PATHFINDER_MAPINSTANCESET.getTopologyMapFromCell(x, y);
                if (map != null) {
                    if (TopologyMapManager.isWalkable(x, y, t.getWorldCellAltitude())) {
                        if (TopologyMapManager.isStoppableUpon(x, y, t.getWorldCellAltitude())) {
                            pathFinder.addStartCell(x, y, t.getWorldCellAltitude());
                        }
                    }
                }
            }
        }
        pathFinder.setMoverCaracteristics(this.getHeight(), this.getPhysicalRadius(), this.getJumpCapacity());
        pathFinder.setParameters(Actor.PATHFINDER_PARAMETERS);
        pathFinder.setTopologyMapInstanceSet(Actor.PATHFINDER_MAPINSTANCESET);
        pathFinder.findPath();
        final PathFindResult result = pathFinder.getReversePathResult();
        pathFinder.release();
        Actor.PATHFINDER_PARAMETERS.m_permissiveStartCellAltitude = false;
        return result.isPathFound() && this.applyPathResult(result, true);
    }
    
    protected boolean allowMoboSterileMovement() {
        return true;
    }
    
    protected boolean allowGaps() {
        return false;
    }
    
    public boolean moveTo(final Point3 to, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        return this.moveTo(to.getX(), to.getY(), to.getZ(), stopBeforeEndCell, useDiagonal);
    }
    
    public boolean moveTo(final int toX, final int toY, final short toZ, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        final PathFindResult result = this.getPathResult(toX, toY, toZ, stopBeforeEndCell, useDiagonal);
        return result.isPathFound() && this.applyPathResult(result, true);
    }
    
    public boolean moveTo(final boolean stopBeforeEndCell, final boolean useDiagonal, final List<Point3> destinations) {
        final PathFindResult result = this.getPathResult(stopBeforeEndCell, useDiagonal, destinations);
        return result.isPathFound() && this.applyPathResult(result, true);
    }
    
    public boolean teleportTo(final Point3 to, final boolean tryToMove, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        return this.teleportTo(to.getX(), to.getY(), to.getZ(), tryToMove, stopBeforeEndCell, useDiagonal);
    }
    
    public boolean teleportTo(final int toX, final int toY, final short toZ, final boolean tryToMove, final boolean stopBeforeEndCell, final boolean useDiagonal) {
        if (tryToMove) {
            final PathFindResult result = this.getPathResult(toX, toY, toZ, stopBeforeEndCell, useDiagonal);
            if (result != null && result.getPathLength() != 0) {
                this.setPath(result, false, true);
                return true;
            }
        }
        this.setWorldPosition(toX, toY, toZ);
        return true;
    }
    
    Direction8Path getBufferedPath() {
        return this.m_bufferedPath;
    }
    
    public void sendBufferedPathToServer() {
        if (this.m_bufferedPath == null) {
            return;
        }
        final long now = System.currentTimeMillis();
        if (this.m_lastPathSent != null && this.m_lastPathSent.contains(this.m_bufferedPath) && this.m_lastPathSent.getEndingPosition().equals(this.m_bufferedPath.getEndingPosition())) {
            if (Actor.DEBUG_TRAJECTORIES) {
                Actor.m_logger.info((Object)"Pas d'envoi du chemin (contenu dans le pr\u00e9c\u00e9dent envoy\u00e9)");
            }
        }
        else {
            final ActorPathRequestMessage request = new ActorPathRequestMessage();
            request.setPath(this.m_bufferedPath);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
            this.m_lastPathSent = this.m_bufferedPath;
            this.m_lastPathSentTime = now;
            if (Actor.DEBUG_TRAJECTORIES) {
                Actor.m_logger.info((Object)("Sent : " + this.m_lastPathSent + " (" + this.m_lastPathSent.steps() + " steps)"));
            }
        }
        this.m_bufferedPath = null;
    }
    
    public void setGfx(@NotNull final String gfx) {
        try {
            if (gfx.equals(this.m_gfxId)) {
                return;
            }
            final int index = Integer.parseInt(gfx);
            final String gfxFile = getGfxFile(index);
            this.setGfxId(gfx);
            this.load(gfxFile, true);
        }
        catch (Exception e) {
            Actor.m_logger.error((Object)"Erreur lors de la cr\u00e9ation de la DescriptorLibrary : ", (Throwable)e);
        }
    }
    
    public static String getGfxFile(final int gfxId) throws PropertyException {
        final String pathForGfx = ActorUtils.getPathForGfx(gfxId);
        final String gfxFile = WakfuConfiguration.getInstance().getString(pathForGfx);
        return String.format(gfxFile, Integer.toString(gfxId));
    }
    
    public static String getGfxFile(final String dir, final int gfxId) throws PropertyException {
        if (StringUtils.isEmptyOrNull(dir)) {
            return getGfxFile(gfxId);
        }
        String gfxFile;
        if (dir.equals("equipment")) {
            gfxFile = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
        }
        else if (dir.equals("npc")) {
            gfxFile = WakfuConfiguration.getInstance().getString("npcGfxPath");
        }
        else {
            if (!dir.equals("player")) {
                return null;
            }
            gfxFile = WakfuConfiguration.getInstance().getString("playerGfxPath");
        }
        return String.format(gfxFile, Integer.toString(gfxId));
    }
    
    public void unapplyAllEquipments() {
        final TShortObjectIterator<Anm> iter = this.m_equipedPositions.iterator();
        while (iter.hasNext()) {
            iter.advance();
            final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(iter.key());
            this.unapplyEquipments(equipmentType, iter.value());
        }
        this.m_equipedPositions.clear();
    }
    
    public void unapplyEquipment(final short position) {
        final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(position);
        if (equipmentType == null) {
            return;
        }
        final Anm anm = this.m_equipedPositions.remove(position);
        if (anm != null) {
            this.unapplyEquipments(equipmentType, anm);
        }
    }
    
    private void unapplyEquipments(final EquipmentType equipmentType, final Anm value) {
        this.removeParts(value, equipmentType.m_linkageCrc);
    }
    
    public void updateActorPath(final Direction8Path path) {
        final PathFindResult pathResult = new PathFindResult(path.steps() + 1);
        pathResult.setStep(0, path.getStartingPosition().getX(), path.getStartingPosition().getY(), path.getStartingPosition().getZ());
        final Point3 pos = new Point3(path.getStartingPosition());
        for (int i = 0; i < path.steps(); ++i) {
            final Direction8Path.Step step = path.getStep(i);
            pos.shift(step.direction);
            pos.add(0, 0, step.heightDiff);
            pathResult.setStep(i + 1, pos.getX(), pos.getY(), pos.getZ());
        }
        this.setPath(pathResult, true, this.getAvailableDirections() != 4);
    }
    
    private void updateEmitter(final PathFindResult pathFind, final boolean stop) {
        if (!this.m_emitter) {
            return;
        }
        final long now = System.currentTimeMillis();
        final Direction8Path path = Direction8Path.fromPathFindResult(pathFind);
        if (path == null) {
            Actor.m_logger.error((Object)"Impossible d'\u00e9mettre un chemin null en direction du serveur");
            return;
        }
        if (this.m_bufferedPath == null) {
            this.m_bufferedPath = path;
        }
        else {
            this.m_bufferedPath = Direction8Path.fusionPaths(this.m_bufferedPath.getStartingPosition(), this.m_bufferedPath, path);
        }
        if (!stop) {
            if (now - this.m_lastPathSentTime < 2000L) {
                if (Actor.DEBUG_TRAJECTORIES) {
                    Actor.m_logger.info((Object)("Bufferisation des chemins non-stop... " + this.m_bufferedPath));
                }
            }
            else {
                if (Actor.DEBUG_TRAJECTORIES) {
                    Actor.m_logger.info((Object)("===> Envoi du chemin non-stop bufferis\u00e9 au serveur: " + this.m_bufferedPath));
                }
                this.sendBufferedPathToServer();
            }
        }
        else {
            final long sinceLastSent = now - this.m_lastPathSentTime;
            final long nextSchedule = 1500L - sinceLastSent;
            final int token = Actor.TOKEN_GENERATOR++;
            if (nextSchedule > 0L) {
                this.m_scheduledToken = token;
                if (Actor.DEBUG_TRAJECTORIES) {
                    Actor.m_logger.info((Object)("Schedule de l'envoi d'un chemin stop tokenis\u00e9 " + token + " dans " + nextSchedule + " ms"));
                }
                ProcessScheduler.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (Actor.this.m_scheduledToken == token) {
                            if (Actor.DEBUG_TRAJECTORIES) {
                                Actor.m_logger.info((Object)("==> Envoi du chemin stop tokenis\u00e9 " + token + " au serveur: " + Actor.this.m_bufferedPath));
                            }
                            Actor.this.sendBufferedPathToServer();
                        }
                        else if (Actor.DEBUG_TRAJECTORIES) {
                            Actor.m_logger.info((Object)("Chemin tokenis\u00e9 " + token + " annul\u00e9"));
                        }
                    }
                }, nextSchedule, 1);
            }
            else {
                if (Actor.DEBUG_TRAJECTORIES) {
                    Actor.m_logger.info((Object)("===> Envoi du chemin stop bufferis\u00e9 au serveur: " + this.m_bufferedPath));
                }
                this.sendBufferedPathToServer();
            }
        }
    }
    
    public ActionInProgress getActionInProgress() {
        return this.m_actionInProgress;
    }
    
    public boolean isEmitter() {
        return this.m_emitter;
    }
    
    public void setEmitter(final boolean emitter) {
        this.m_emitter = emitter;
    }
    
    public boolean isWaitEndAnimation() {
        return this.m_waitEndAnimation;
    }
    
    public void setWaitEndAnimation(final boolean waitEndAnimation) {
        this.m_waitEndAnimation = waitEndAnimation;
    }
    
    public void addStartPathListener(final MobileStartPathListener startListener) {
        this.m_startListener.add(startListener);
    }
    
    public void removeStartListener(final MobileStartPathListener listener) {
        this.m_startListener.remove(listener);
    }
    
    public void transferPropertiesTo(final Actor actor) {
        for (final MobileStartPathListener mobileStartPathListener : this.m_startListener) {
            actor.addStartPathListener(mobileStartPathListener);
        }
        for (final MobileEndPathListener endPathListener : this.m_endPathListeners) {
            actor.addEndPositionListener(endPathListener);
        }
    }
    
    public void enableDefaultPreferedAlphaMask(final boolean valueWhenNoPreference) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getActor() == this) {
            this.enableAlphaMask(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY));
        }
        else {
            this.enableAlphaMask(valueWhenNoPreference);
        }
    }
    
    @Override
    public String getFormatedOverheadText() {
        return "";
    }
    
    @Override
    protected void reset() {
        super.reset();
        this.m_equipedPositions.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Actor.class);
        Actor.TOKEN_GENERATOR = 1;
        PATHFINDER_PARAMETERS = new PathFinderParameters();
        Actor.PATHFINDER_PARAMETERS.m_searchLimit = 400;
        PATHFINDER_MAPINSTANCESET = new TopologyMapInstanceSet();
    }
}
