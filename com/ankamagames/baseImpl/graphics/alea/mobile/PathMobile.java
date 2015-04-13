package com.ankamagames.baseImpl.graphics.alea.mobile;

import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.framework.kernel.core.maths.motion.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;

public class PathMobile extends Mobile implements StyleMobile
{
    private static final byte SLOPE_CONTINUE = 0;
    private static final byte SLOPE_DISCONTINUE = 1;
    private static final byte SLOPE_FORCE_DISCONTINUE = 2;
    protected final List<MobileEndPathListener> m_endPathListeners;
    protected final Set<MobileEndPathListener> m_endPathListenersToRemove;
    public static final int[][] EIGHT_DIRECTION_SHIFT;
    public static final int[][] FOUR_DIRECTION_SHIFT;
    public static final short DEFAULT_MOBILE_JUMP_HEIGHT = 4;
    public static final short DEFAULT_MOBILE_MAX_JUMP_DESCENDING_HEIGHT = 8;
    public int[][] m_directionShift;
    protected short m_jumpCapacity;
    private MovementSelector m_movementSelector;
    private PathMovementStyle m_movementStyle;
    private boolean m_isInSpecialJump;
    private float m_movementMultiplier;
    protected PathFindResult m_currentPath;
    protected int m_currentPathCell;
    protected int m_lastAirImpulsion;
    protected int m_lastSpecialJump;
    protected static boolean DEBUG_TRAJECTORIES;
    public static boolean GRAPHICAL_DEBUG_TRAJECTORIES;
    public static boolean GRAPHICAL_DEBUG_PATHFIND;
    protected TIntHashSet m_debugParticleSystemsIds;
    protected static final float MAX_LINEAR_TRAJECTORY_LENGTH = 2.0f;
    protected Spline m_trajectories;
    protected Vector3 m_previousPosition;
    private int m_trajectoryTime;
    protected byte m_availableDirections;
    protected boolean m_jumpAnimAvailable;
    private static final boolean DEBUG_MODE = false;
    private static final CellPathData[] m_cellPathData;
    private final SlopeProperties m_currentSlope;
    private final SlopeProperties m_nextSlope;
    
    public PathMobile(final long id) {
        super(id);
        this.m_endPathListeners = new ArrayList<MobileEndPathListener>(5);
        this.m_endPathListenersToRemove = new HashSet<MobileEndPathListener>(5);
        this.m_directionShift = PathMobile.EIGHT_DIRECTION_SHIFT;
        this.m_jumpCapacity = 4;
        this.m_movementSelector = SimpleMovementSelector.getInstance();
        this.m_movementStyle = WalkMovementStyle.getInstance();
        this.m_movementMultiplier = 1.0f;
        this.m_debugParticleSystemsIds = new TIntHashSet();
        this.m_availableDirections = 8;
        this.m_jumpAnimAvailable = true;
        this.m_currentSlope = new SlopeProperties();
        this.m_nextSlope = new SlopeProperties();
    }
    
    public PathMobile(final long id, final float worldX, final float worldY, final float altitude) {
        super(id, worldX, worldY, altitude);
        this.m_endPathListeners = new ArrayList<MobileEndPathListener>(5);
        this.m_endPathListenersToRemove = new HashSet<MobileEndPathListener>(5);
        this.m_directionShift = PathMobile.EIGHT_DIRECTION_SHIFT;
        this.m_jumpCapacity = 4;
        this.m_movementSelector = SimpleMovementSelector.getInstance();
        this.m_movementStyle = WalkMovementStyle.getInstance();
        this.m_movementMultiplier = 1.0f;
        this.m_debugParticleSystemsIds = new TIntHashSet();
        this.m_availableDirections = 8;
        this.m_jumpAnimAvailable = true;
        this.m_currentSlope = new SlopeProperties();
        this.m_nextSlope = new SlopeProperties();
    }
    
    public PathMobile(final long id, final float worldX, final float worldY) {
        super(id, worldX, worldY);
        this.m_endPathListeners = new ArrayList<MobileEndPathListener>(5);
        this.m_endPathListenersToRemove = new HashSet<MobileEndPathListener>(5);
        this.m_directionShift = PathMobile.EIGHT_DIRECTION_SHIFT;
        this.m_jumpCapacity = 4;
        this.m_movementSelector = SimpleMovementSelector.getInstance();
        this.m_movementStyle = WalkMovementStyle.getInstance();
        this.m_movementMultiplier = 1.0f;
        this.m_debugParticleSystemsIds = new TIntHashSet();
        this.m_availableDirections = 8;
        this.m_jumpAnimAvailable = true;
        this.m_currentSlope = new SlopeProperties();
        this.m_nextSlope = new SlopeProperties();
    }
    
    public void clearMovement() {
        this.m_currentPath = null;
        this.m_currentPathCell = -1;
        this.m_lastAirImpulsion = -1;
        this.m_lastSpecialJump = -1;
        this.m_trajectories = null;
        this.m_endPathListeners.clear();
        this.m_movementSelector.onMovementEnded(this);
        if (this.shouldNotifyMovementStyleAfterClearing()) {
            this.m_movementStyle.onMovingOnGround(this, 0);
            this.m_movementStyle.onStandingOnLastCell(this);
        }
    }
    
    protected boolean shouldNotifyMovementStyleAfterClearing() {
        return this.m_movementStyle != null && !this.isCarried();
    }
    
    public boolean containsEndPositionListerner(final MobileEndPathListener listener) {
        return this.m_endPathListeners.contains(listener);
    }
    
    public final void addEndPositionListener(final MobileEndPathListener listener) {
        if (this.m_endPathListeners.contains(listener)) {
            this.m_endPathListenersToRemove.remove(listener);
            return;
        }
        this.m_endPathListeners.add(listener);
    }
    
    public final void removeEndPositionListener(final MobileEndPathListener listener) {
        if (listener != null) {
            this.m_endPathListenersToRemove.add(listener);
        }
    }
    
    public final void removeAllEndPositionListener() {
        this.m_endPathListenersToRemove.addAll(this.m_endPathListeners);
    }
    
    @Override
    public void setMaskKey(final int key, final short layerId) {
        super.setMaskKey(key, layerId);
        final Mobile carriedMobile = this.getCarriedMobile();
        if (carriedMobile != null) {
            carriedMobile.setMaskKey(key, layerId);
        }
    }
    
    private static void getSlopeProperties(final int cellX, final int cellY, final int cellZ, final SlopeProperties properties) {
        if (properties.positionEquals(cellX, cellY, cellZ)) {
            return;
        }
        properties.reset();
        final TopologyMapInstance mapInstance = TopologyMapManager.getMapFromCell(cellX, cellY);
        if (mapInstance == null) {
            return;
        }
        final int numZ = mapInstance.getTopologyMap().getPathData(cellX, cellY, PathMobile.m_cellPathData, 0);
        if (numZ == 0) {
            return;
        }
        for (int i = 0; i < numZ; ++i) {
            final CellPathData cellPathData = PathMobile.m_cellPathData[i];
            if (cellPathData.m_z == cellZ) {
                final DisplayedScreenElement screenElement = DisplayedScreenWorld.getInstance().getWalkableElementAtTop(cellPathData.m_x, cellPathData.m_y, cellPathData.m_z, ElementFilter.NOT_EMPTY);
                if (screenElement != null) {
                    final ElementProperties elementProperties = screenElement.getElement().getCommonProperties();
                    properties.slope = elementProperties.getSlope();
                    if (properties.slope != 0) {
                        properties.height = (byte)elementProperties.getVisualHeight();
                    }
                    else {
                        properties.height = 0;
                    }
                    properties.setPosition(cellX, cellY, cellZ);
                    break;
                }
            }
        }
        if (properties.cellZ == Integer.MAX_VALUE) {
            properties.slope = 0;
            properties.height = 0;
            properties.setPosition(cellX, cellY, cellZ);
        }
    }
    
    protected final void finalizeCurrentTrajectory() {
        final Vector3 finalPosition = this.m_trajectories.getFinalPosition();
        super.setWorldPosition(finalPosition.getX(), finalPosition.getY(), finalPosition.getZ());
        if (this.getCarriedMobile() != null) {
            this.getCarriedMobile().setWorldPosition(this.m_worldX, this.m_worldY, this.m_altitude + this.getHeight());
        }
        final ArrayList<Mobile> childrenMobile = this.getLinkedChildrenMobile();
        if (childrenMobile != null) {
            for (int i = 0, size = childrenMobile.size(); i < size; ++i) {
                childrenMobile.get(i).setWorldPosition(this.m_worldX, this.m_worldY, this.m_altitude);
            }
        }
        this.m_trajectories = null;
        this.m_currentPath = null;
        this.m_currentPathCell = -1;
        this.m_lastAirImpulsion = -1;
        this.m_lastSpecialJump = -1;
        this.m_movementStyle.onMovingOnGround(this, 0);
        this.m_movementStyle.onStandingOnLastCell(this);
        this.m_movementSelector.onMovementEnded(this);
        this.m_endPathListeners.removeAll(this.m_endPathListenersToRemove);
        this.m_endPathListenersToRemove.clear();
        for (int i = 0, size = this.m_endPathListeners.size(); i < size; ++i) {
            this.m_endPathListeners.get(i).pathEnded(this, (int)this.m_worldX, (int)this.m_worldY, (short)this.m_altitude);
        }
        this.m_endPathListeners.removeAll(this.m_endPathListenersToRemove);
        this.m_endPathListenersToRemove.clear();
        MaskableHelper.setUndefined(this);
    }
    
    @Override
    public void process(final AleaWorldScene scene, final int deltaTime) {
        this.m_trajectoryTime += deltaTime;
        if (this.m_trajectories == null) {
            return;
        }
        if (this.m_trajectoryTime >= this.m_trajectories.getFinalTime()) {
            this.finalizeCurrentTrajectory();
            return;
        }
        this.m_endPathListeners.removeAll(this.m_endPathListenersToRemove);
        this.m_endPathListenersToRemove.clear();
        if (PathMobile.GRAPHICAL_DEBUG_TRAJECTORIES) {
            this.debugTrajectory();
        }
        final float remainingPercentTrajectory = getRemainingPercent(this.m_trajectories, this.m_trajectoryTime);
        final Vector3 currentPosition = this.m_trajectories.getPosition(this.m_trajectoryTime);
        final Vector3 nextPosition = this.m_trajectories.getNextPositionInDifferentCell(this.m_trajectoryTime);
        final float newPositionX = currentPosition.getX();
        final float newPositionY = currentPosition.getY();
        final float newPositionZ = currentPosition.getZ();
        final Direction8 direction = this.getDirectionFrom(currentPosition, this.m_previousPosition);
        final int currentCellXi = MathHelper.fastRound(newPositionX);
        final int nextCellXi = MathHelper.fastRound(nextPosition.getX());
        final int currentCellYi = MathHelper.fastRound(newPositionY);
        final int nextCellYi = MathHelper.fastRound(nextPosition.getY());
        int currentCellZi = MathHelper.fastRound(newPositionZ);
        int nextCellZi = MathHelper.fastRound(nextPosition.getZ());
        currentCellZi = this.correctZ(currentCellXi, currentCellYi, currentCellZi);
        nextCellZi = this.correctZ(nextCellXi, nextCellYi, nextCellZi);
        final int minI = this.getNearestPathCellIndexFromCurrentCell(currentCellXi, currentCellYi);
        this.cellTransitions(currentCellXi, currentCellYi, minI);
        if (nextCellXi != currentCellXi || nextCellYi != currentCellYi) {
            getSlopeProperties(currentCellXi, currentCellYi, currentCellZi, this.m_currentSlope);
            getSlopeProperties(nextCellXi, nextCellYi, nextCellZi, this.m_nextSlope);
        }
        this.m_previousPosition = currentPosition;
        assert !Float.isNaN(newPositionX);
        assert !Float.isNaN(newPositionY);
        if (currentCellXi != this.getWorldCellX() || currentCellYi != this.getWorldCellY() || currentCellZi != this.getWorldCellAltitude()) {
            MaskableHelper.setUndefined(this);
        }
        final float[] newAltitude = { this.m_altitude };
        if (this.processAltitude(currentPosition, nextCellXi, nextCellYi, newAltitude)) {
            this.m_lastAirImpulsion = this.m_currentPathCell;
        }
        else if (this.processSpecialJump(currentPosition, nextCellXi, nextCellYi, newAltitude)) {
            this.m_lastSpecialJump = this.m_currentPathCell;
        }
        else if (this.m_currentPath != null) {
            this.m_movementStyle.onMovingOnGround(this, (int)(remainingPercentTrajectory * this.m_currentPath.getPathLength()));
        }
        if (direction != null) {
            this.m_movementStyle.onDirectionChanged(this, direction);
        }
        super.setWorldPosition(newPositionX, newPositionY, newAltitude[0]);
        this.processChildren();
    }
    
    @Nullable
    private Direction8 getDirectionFrom(final Vector3 currentPosition, final Vector3 previousPosition) {
        final float dx = currentPosition.m_x - previousPosition.m_x;
        final float dy = currentPosition.m_y - previousPosition.m_y;
        if (dx == 0.0f && dy == 0.0f) {
            return null;
        }
        if (this.getAvailableDirections() == 8) {
            return Vector3.getDirection8FromVector(dx, dy);
        }
        return Vector3.getDirection4FromVector(dx, dy);
    }
    
    private void processChildren() {
        if (this.getCarriedMobile() != null) {
            this.getCarriedMobile().setWorldPosition(this.m_worldX, this.m_worldY, this.m_altitude + this.getHeight());
        }
        final ArrayList<Mobile> childrenMobile = this.getLinkedChildrenMobile();
        if (childrenMobile != null) {
            for (int i = 0, size = childrenMobile.size(); i < size; ++i) {
                childrenMobile.get(i).setWorldPosition(this.m_worldX, this.m_worldY, this.m_altitude);
            }
        }
    }
    
    protected void cellTransitions(final int currentCellXi, final int currentCellYi, final int minI) {
        if (minI > this.m_currentPathCell) {
            final int previousCurrentPathCell = this.m_currentPathCell;
            for (int i = this.m_currentPathCell + 1; i < minI; ++i) {
                this.onCellTransition(this.m_currentPath.getPathStep(i), this.m_currentPath.getPathStep(i - 1));
            }
            if (this.m_currentPathCell != previousCurrentPathCell) {
                this.m_currentPathCell = this.getNearestPathCellIndexFromCurrentCell(currentCellXi, currentCellYi);
            }
            else {
                this.m_currentPathCell = minI;
            }
            if (PathMobile.GRAPHICAL_DEBUG_PATHFIND) {
                this.debugPathFind();
            }
        }
    }
    
    private int getNearestPathCellIndexFromCurrentCell(final int currentCellXi, final int currentCellYi) {
        int minDist = Integer.MAX_VALUE;
        int minI = 0;
        for (int i = this.m_currentPathCell; i < this.m_currentPath.getPathLength(); ++i) {
            final int[] step = this.m_currentPath.getPathStep(i);
            final int dist = (step[0] - currentCellXi) * (step[0] - currentCellXi) + (step[1] - currentCellYi) * (step[1] - currentCellYi);
            if (dist < minDist) {
                minDist = dist;
                minI = i;
            }
        }
        return minI;
    }
    
    private static float getRemainingPercent(final Spline trajectories, final int trajectoryTime) {
        final long finalTime = trajectories.getFinalTime();
        return (finalTime - trajectoryTime) / (finalTime - trajectories.getInitialTime());
    }
    
    private boolean processSpecialJump(final Vector3 currentPosition, final int nextCellX, final int nextCellY, final float[] newAltitude) {
        if (this.m_currentPathCell == this.m_lastAirImpulsion + 1 && this.m_lastAirImpulsion != -1) {
            return true;
        }
        final int currentX = MathHelper.fastRound(currentPosition.getX());
        final int currentY = MathHelper.fastRound(currentPosition.getY());
        if (TopologyMapManager.isGap(currentX, currentY)) {
            return true;
        }
        if (TopologyMapManager.isJump(currentX, currentY) && TopologyMapManager.isGap(nextCellX, nextCellY)) {
            this.m_movementStyle.onMovingSpecialJump(this);
            return true;
        }
        return false;
    }
    
    private boolean processAltitude(final Vector3 currentPosition, final int nextCellX, final int nextCellY, final float[] newAltitude) {
        if (!this.m_currentSlope.initialized() || !this.m_nextSlope.initialized()) {
            return false;
        }
        final float dx = nextCellX - currentPosition.getX();
        final float dy = nextCellY - currentPosition.getY();
        final float distanceToNextCell = Vector2.sqrLength(dx, dy);
        final int totalDx = this.m_nextSlope.cellX - this.m_currentSlope.cellX;
        final int totalDy = this.m_nextSlope.cellY - this.m_currentSlope.cellY;
        final int deltaZ = this.m_nextSlope.cellZ - this.m_currentSlope.cellZ;
        final float totalDistanceBetweenCells = Vector2.sqrLength(totalDx, totalDy);
        final float cellPositionPercent = (totalDistanceBetweenCells == 0.0f) ? 0.0f : MathHelper.clamp(1.0f - MathHelper.sqrt(distanceToNextCell / totalDistanceBetweenCells), 0.0f, 1.0f);
        if (distanceToNextCell == 0.0) {
            newAltitude[0] = this.m_currentSlope.cellZ + cellPositionPercent * deltaZ;
            return false;
        }
        final boolean needImpulsion = this.m_movementStyle.isAirImpulsionNeeded(this, deltaZ);
        final byte slopeContinuity = canFollowSlope(this.m_currentSlope, this.m_nextSlope, 1);
        if (!needImpulsion || (slopeContinuity != 1 && slopeContinuity != 2)) {
            newAltitude[0] = this.computeAltitudeOnSlope(cellPositionPercent);
            return false;
        }
        boolean inAir = false;
        final JumpTrajectory jumpTrajectory = (deltaZ >= 0) ? this.m_movementStyle.getJumpUp() : this.m_movementStyle.getJumpDown();
        final Phase jumpPhase = jumpTrajectory.getPhase(cellPositionPercent);
        switch (jumpPhase) {
            case AFTER: {
                newAltitude[0] = this.m_nextSlope.cellZ;
                break;
            }
            case ASCENDING:
            case STABLE:
            case DESCENDING: {
                inAir = true;
                newAltitude[0] = jumpTrajectory.getAltitude(this.m_currentSlope.cellZ, this.m_nextSlope.cellZ, cellPositionPercent, jumpPhase);
                break;
            }
        }
        if (this.m_jumpAnimAvailable && inAir && slopeContinuity != 2) {
            this.m_movementStyle.onMovingOnAir(this, deltaZ, jumpPhase);
            if (cellPositionPercent <= 0.5 && this.m_currentPathCell == this.m_lastAirImpulsion + 1) {
                this.forceReloadAnimation();
            }
            return true;
        }
        return false;
    }
    
    private float computeAltitudeOnSlope(final float cellPositionPercent) {
        final SlopeProperties current = (cellPositionPercent <= 0.5f) ? this.m_currentSlope : this.m_nextSlope;
        final int dz = this.m_nextSlope.cellZ - this.m_currentSlope.cellZ;
        if (current.slope == 0) {
            if (dz == 0 || this.m_currentSlope.slope != this.m_nextSlope.slope) {
                return current.cellZ;
            }
            if (this.m_currentSlope.slope == this.m_nextSlope.slope) {
                final float p = 0.5f - cellPositionPercent;
                final float delta = 0.1f;
                if (Math.abs(p) > 0.1f) {
                    return current.cellZ;
                }
                return this.m_currentSlope.cellZ + (0.1f - p) / 0.1f * 0.5f * dz;
            }
        }
        float percent;
        if (this.m_currentSlope.slope == this.m_nextSlope.slope) {
            percent = cellPositionPercent;
        }
        else if (cellPositionPercent <= 0.5f) {
            percent = cellPositionPercent * 2.0f;
        }
        else {
            percent = cellPositionPercent * 2.0f - 1.0f;
        }
        return this.m_currentSlope.cellZ + percent * dz;
    }
    
    private int correctZ(final int cellX, final int cellY, final int cellZ) {
        for (int pathSize = this.m_currentPath.getPathLength(), i = this.m_currentPathCell; i < pathSize; ++i) {
            final int[] pathStep = this.m_currentPath.getPathStep(i);
            if (pathStep[0] == cellX && pathStep[1] == cellY && Math.abs(cellZ - pathStep[2]) < this.getJumpCapacity()) {
                return pathStep[2];
            }
        }
        final short z = TopologyMapManager.getNearestWalkableZ(cellX, cellY, (short)cellZ);
        if (z == -32768) {
            return cellZ;
        }
        return z;
    }
    
    public void stopMoving() {
        if (this.m_currentPath == null) {
            return;
        }
        final PathFindResult newPath = this.m_currentPath.subPath(this.m_currentPathCell, this.m_currentPathCell + 2);
        this.setPath(newPath, false, true);
    }
    
    protected void debugTrajectory() {
    }
    
    protected void debugPathFind() {
    }
    
    private static byte canFollowSlope(final SlopeProperties currentSlopeProp, final SlopeProperties nextSlopeProp, final int maxDeltaZ) {
        final int dx = nextSlopeProp.cellX - currentSlopeProp.cellX;
        final int dy = nextSlopeProp.cellY - currentSlopeProp.cellY;
        final byte height = currentSlopeProp.height;
        final byte nextHeight = nextSlopeProp.height;
        final byte slope = currentSlopeProp.slope;
        final byte nextSlope = nextSlopeProp.slope;
        final int diffAltitude = nextSlopeProp.cellZ - ((nextSlope != 0) ? (nextHeight / 2) : 0) - (currentSlopeProp.cellZ - ((slope != 0) ? (height / 2) : 0));
        if (dx == 0 || dy == 0) {
            if (dx > 0) {
                if ((slope & 0xC) == 0xC && (nextSlope & 0x3) == 0x3) {
                    return 2;
                }
                final int right = ((slope & 0x4) == 0x4) ? height : 0;
                final int bottom = ((slope & 0x8) == 0x8) ? height : 0;
                final int leftNext = (nextSlope == 0 || (nextSlope & 0x1) == 0x1) ? nextHeight : 0;
                final int topNext = (nextSlope == 0 || (nextSlope & 0x2) == 0x2) ? nextHeight : 0;
                return (byte)((Math.abs(right - (topNext + diffAltitude)) > Math.abs(maxDeltaZ) || Math.abs(bottom - (leftNext + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
            }
            else if (dx < 0) {
                if ((slope & 0x3) == 0x3 && (nextSlope & 0xC) == 0xC) {
                    return 2;
                }
                final int left = ((slope & 0x1) == 0x1) ? height : 0;
                final int top = ((slope & 0x2) == 0x2) ? height : 0;
                final int bottomNext = (nextSlope == 0 || (nextSlope & 0x8) == 0x8) ? nextHeight : 0;
                final int rightNext = (nextSlope == 0 || (nextSlope & 0x4) == 0x4) ? nextHeight : 0;
                return (byte)((Math.abs(left - (bottomNext + diffAltitude)) > Math.abs(maxDeltaZ) || Math.abs(top - (rightNext + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
            }
            else if (dy > 0) {
                if ((slope & 0x9) == 0x9 && (nextSlope & 0x6) == 0x6) {
                    return 2;
                }
                final int left = ((slope & 0x1) == 0x1) ? height : 0;
                final int bottom = ((slope & 0x8) == 0x8) ? height : 0;
                final int topNext2 = (nextSlope == 0 || (nextSlope & 0x2) == 0x2) ? nextHeight : 0;
                final int rightNext = (nextSlope == 0 || (nextSlope & 0x4) == 0x4) ? nextHeight : 0;
                return (byte)((Math.abs(left - (topNext2 + diffAltitude)) > Math.abs(maxDeltaZ) || Math.abs(bottom - (rightNext + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
            }
            else {
                if ((slope & 0x6) == 0x6 && (nextSlope & 0x9) == 0x9) {
                    return 2;
                }
                final int top2 = ((slope & 0x2) == 0x2) ? height : 0;
                final int right2 = ((slope & 0x4) == 0x4) ? height : 0;
                final int leftNext = (nextSlope == 0 || (nextSlope & 0x1) == 0x1) ? nextHeight : 0;
                final int bottomNext2 = (nextSlope == 0 || (nextSlope & 0x8) == 0x8) ? nextHeight : 0;
                return (byte)((Math.abs(right2 - (bottomNext2 + diffAltitude)) > Math.abs(maxDeltaZ) || Math.abs(top2 - (leftNext + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
            }
        }
        else if (dx > 0) {
            if (dy > 0) {
                final int bottom2 = ((slope & 0x8) == 0x8) ? height : 0;
                final int topNext3 = (nextSlope == 0 || (nextSlope & 0x2) == 0x2) ? nextHeight : 0;
                return (byte)((Math.abs(bottom2 - (topNext3 + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
            }
            final int right = ((slope & 0x4) == 0x4) ? height : 0;
            final int leftNext2 = (nextSlope == 0 || (nextSlope & 0x1) == 0x1) ? nextHeight : 0;
            return (byte)((Math.abs(right - (leftNext2 + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
        }
        else {
            if (dy > 0) {
                final int left = ((slope & 0x1) == 0x1) ? height : 0;
                final int rightNext2 = (nextSlope == 0 || (nextSlope & 0x4) == 0x4) ? nextHeight : 0;
                return (byte)((Math.abs(left - (rightNext2 + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
            }
            final int top2 = ((slope & 0x2) == 0x2) ? height : 0;
            final int bottomNext3 = (nextSlope == 0 || (nextSlope & 0x8) == 0x8) ? nextHeight : 0;
            return (byte)((Math.abs(top2 - (bottomNext3 + diffAltitude)) > Math.abs(maxDeltaZ)) ? 1 : 0);
        }
    }
    
    public short getJumpCapacity() {
        return this.m_jumpCapacity;
    }
    
    public void setJumpCapacity(final short jumpCapacity) {
        this.m_jumpCapacity = jumpCapacity;
    }
    
    public final PathFindResult getCurrentPath() {
        return this.m_currentPath;
    }
    
    private void setMovementStyle(final int pathLength) {
        final PathMovementStyle movementStyle = this.m_movementSelector.selectMovementStyle(this, pathLength);
        assert movementStyle != null;
        this.m_movementStyle = movementStyle;
    }
    
    @Override
    public final PathMovementStyle getMovementStyle() {
        return this.m_movementStyle;
    }
    
    @Override
    public MovementSelector getMovementSelector() {
        return this.m_movementSelector;
    }
    
    @Override
    public void resetMovementSelector() {
        this.m_movementSelector.resetMovementSelector(this);
    }
    
    @Override
    public void setIsInSpecialJump(final boolean isInSpecialJump) {
        this.m_isInSpecialJump = isInSpecialJump;
    }
    
    @Override
    public boolean isInSpecialJump() {
        return this.m_isInSpecialJump;
    }
    
    public void setMovementSelector(final boolean justOnce, final String styleName) {
        final PathMovementStyle style = MovementStyleManager.getInstance().getMovementStyle(styleName);
        this.setMovementSelector(CustomMovementSelector.create(justOnce, this, style, style));
    }
    
    public void setMovementSelector(final boolean justOnce, final String walkStyle, final String runStyle) {
        final PathMovementStyle walk = MovementStyleManager.getInstance().getMovementStyle(walkStyle);
        final PathMovementStyle run = MovementStyleManager.getInstance().getMovementStyle(runStyle);
        this.setMovementSelector(CustomMovementSelector.create(justOnce, this, walk, run));
    }
    
    @Override
    public void setMovementSelector(final MovementSelector movementSelector) {
        assert this.m_movementSelector != null;
        this.m_movementSelector = movementSelector;
        this.setMovementStyle(0);
    }
    
    public void setMovementMultiplier(final float multiplier) {
        this.m_movementMultiplier = multiplier;
    }
    
    public void setPath(final PathFindResult node, final boolean uselessParam, final boolean useSpline) {
        if (node.getPathLength() < 2) {
            return;
        }
        if (node.getPathLength() == 2 && node.getFirstStep()[0] == node.getLastStep()[0] && node.getFirstStep()[1] == node.getLastStep()[1]) {
            this.setMovementStyle(0);
            return;
        }
        for (int i = 0, size = node.getPathLength(); i < size; ++i) {
            final int[] pathStep = node.getPathStep(i);
            if (TopologyMapManager.isGap(pathStep[0], pathStep[1]) && MovementStyleManager.WALK_STYLE.equals(this.m_movementStyle.getStyleName())) {
                this.setMovementSelector(true, MovementStyleManager.RUN_STYLE);
                break;
            }
        }
        this.setMovementStyle(node.getPathLength());
        final float speed = this.m_movementStyle.getCellSpeed(this) / this.m_movementMultiplier;
        ArrayList<LinearTrajectory> trajectories = node.toTrajectories((int)speed, true);
        final Vector3 currentMobilePosition = new Vector3(this.m_worldX, this.m_worldY, this.m_altitude);
        if (this.m_trajectories != null) {
            final LinearTrajectory traj0 = trajectories.get(0);
            traj0.setInitialPosition(currentMobilePosition);
            traj0.setInitialVelocity(traj0.getFinalPosition().sub(traj0.getInitialPosition()));
        }
        this.m_trajectoryTime = 0;
        if (useSpline && trajectories.size() > 1) {
            final ArrayList<LinearTrajectory> subTrajectories = new ArrayList<LinearTrajectory>(trajectories.size());
            for (final LinearTrajectory trajectory : trajectories) {
                subTrajectories.addAll(trajectory.split(2.0));
            }
            trajectories = subTrajectories;
            this.m_trajectories = new TimeUniformSpline(trajectories, this.m_trajectoryTime);
        }
        else {
            final int delayOnFirstCell = this.m_movementStyle.getDelayOnFirstCell();
            if (delayOnFirstCell >= 0) {
                this.m_movementStyle.onStandingOnFirstCell(this, node.getDirectionToStep(1));
            }
            this.m_trajectories = new LinearSpline(trajectories, this.m_trajectoryTime + Math.max(0, delayOnFirstCell));
        }
        this.m_previousPosition = new Vector3(this.m_worldX, this.m_worldY, this.m_altitude);
        this.m_currentPath = node;
        this.m_currentPathCell = 0;
        if (PathMobile.GRAPHICAL_DEBUG_TRAJECTORIES) {
            this.m_debugParticleSystemsIds.forEach(new TIntProcedure() {
                @Override
                public boolean execute(final int debugParticleId) {
                    IsoParticleSystemManager.getInstance().removeParticleSystem(debugParticleId);
                    return true;
                }
            });
            this.m_debugParticleSystemsIds.clear();
        }
    }
    
    public Spline getTrajectories() {
        return this.m_trajectories;
    }
    
    public int getCurrentWorldX() {
        if (this.m_currentPath != null) {
            return this.m_currentPath.getPathStep(this.m_currentPathCell)[0];
        }
        return (int)this.m_worldX;
    }
    
    public int getCurrentWorldY() {
        if (this.m_currentPath != null) {
            return this.m_currentPath.getPathStep(this.m_currentPathCell)[1];
        }
        return (int)this.m_worldY;
    }
    
    public short getCurrentAltitude() {
        if (this.isCarried()) {
            return (short)this.getCarrierMobile().getAltitude();
        }
        if (this.m_currentPath != null) {
            return (short)this.m_currentPath.getPathStep(this.m_currentPathCell)[2];
        }
        return (short)this.m_altitude;
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float altitude) {
        if (this.getMovementStyle().createPathOnSetPosition(this)) {
            PathFindResult path;
            if (this.m_currentPath == null) {
                path = new PathFindResult(2);
                path.setStep(0, (int)this.m_worldX, (int)this.m_worldY, (short)this.m_altitude);
                path.setStep(1, (int)worldX, (int)worldY, (short)altitude);
            }
            else {
                path = new PathFindResult(3);
                path.setStep(0, (int)this.m_worldX, (int)this.m_worldY, (short)this.m_altitude);
                path.setStep(1, this.m_currentPath.getLastStep());
                path.setStep(2, (int)worldX, (int)worldY, (short)altitude);
            }
            this.setPath(path, true, false);
            return;
        }
        super.setWorldPosition(worldX, worldY, altitude);
        this.clearMovement();
    }
    
    public void setAvailableDirections(final byte number) {
        this.m_availableDirections = number;
    }
    
    public byte getAvailableDirections() {
        return this.m_availableDirections;
    }
    
    public void setJumpAnimAvailable(final boolean animAvailable) {
        this.m_jumpAnimAvailable = animAvailable;
    }
    
    @Override
    public int getMaxWalkDistance() {
        return 5;
    }
    
    @Override
    public MovementSpeed getRunMovementSpeed() {
        return MovementSpeed.NORMAL_RUN_SPEED;
    }
    
    @Override
    public MovementSpeed getWalkMovementSpeed() {
        return MovementSpeed.NORMAL_WALK_SPEED;
    }
    
    @Override
    public String getMoveEndAnimationKey() {
        return this.getStaticAnimationKey();
    }
    
    public boolean isMoving() {
        return this.m_currentPath != null;
    }
    
    @Override
    protected void onCellTransition(final int[] nextCell, final int[] previousCell) {
        final int[] correctedNextCell = { nextCell[0], nextCell[1], nextCell[2] };
        if (this.m_currentPath != null) {
            final int length = this.m_currentPath.getPathLength();
            boolean cellFoundInPath = false;
            for (int i = 0; i < length; ++i) {
                final int[] cell = this.m_currentPath.getPathStep(i);
                if (cell[0] == nextCell[0] && cell[1] == nextCell[1]) {
                    correctedNextCell[2] = cell[2];
                    cellFoundInPath = true;
                    break;
                }
            }
            if (!cellFoundInPath) {
                return;
            }
        }
        if (this.m_positionListeners != null) {
            final TargetPositionListener[] arr$;
            final TargetPositionListener[] listeners = arr$ = this.m_positionListeners.toArray(new TargetPositionListener[this.m_positionListeners.size()]);
            for (final TargetPositionListener listener : arr$) {
                listener.cellPositionChanged(this, correctedNextCell[0], correctedNextCell[1], (short)correctedNextCell[2]);
            }
        }
        if (this.getCarriedMobile() != null) {
            this.getCarriedMobile().onCellTransition(correctedNextCell, previousCell);
        }
    }
    
    public boolean isLocalPlayer() {
        return false;
    }
    
    @Override
    public boolean hitTest(final float x, final float y) {
        if (this.isMoving()) {
            return this.hitTest(x, y, 2.0f);
        }
        return super.hitTest(x, y);
    }
    
    @Override
    protected void reset() {
        super.reset();
        this.m_endPathListeners.clear();
        this.m_endPathListenersToRemove.clear();
        this.m_currentPath = null;
        this.m_trajectories = null;
    }
    
    static {
        EIGHT_DIRECTION_SHIFT = new int[][] { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
        FOUR_DIRECTION_SHIFT = new int[][] { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
        PathMobile.DEBUG_TRAJECTORIES = false;
        PathMobile.GRAPHICAL_DEBUG_TRAJECTORIES = false;
        PathMobile.GRAPHICAL_DEBUG_PATHFIND = false;
        m_cellPathData = CellPathData.createCellPathDataTab();
    }
    
    private static final class SlopeProperties
    {
        byte slope;
        byte height;
        int cellX;
        int cellY;
        int cellZ;
        
        private SlopeProperties() {
            super();
            this.reset();
        }
        
        void reset() {
            this.slope = 0;
            this.height = 0;
            this.cellX = Integer.MAX_VALUE;
            this.cellY = Integer.MAX_VALUE;
            this.cellZ = Integer.MAX_VALUE;
        }
        
        public boolean initialized() {
            return this.cellZ != Integer.MAX_VALUE;
        }
        
        boolean positionEquals(final int x, final int y, final int z) {
            return this.cellX == x && this.cellY == y && this.cellZ == z;
        }
        
        public void setPosition(final int x, final int y, final int z) {
            this.cellX = x;
            this.cellY = y;
            this.cellZ = z;
        }
        
        @Override
        public String toString() {
            return "{" + this.cellX + "," + this.cellY + "," + this.cellZ + "}";
        }
    }
}
