package com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;

public final class PathComputer
{
    protected static Logger m_logger;
    private static CellPathData[] m_sourceCellPathData;
    private static CellPathData[] m_destCellPathData;
    private PathChecker m_pathChecker;
    private FightMap m_fightMap;
    private TopologyMap m_topologyMap;
    private final MovementEffectUser m_mover;
    private final Point3 m_targetCell;
    private final int m_distance;
    private final EffectContext<WakfuEffect> m_context;
    private int m_numSourceZ;
    private int m_sourceIndex;
    private boolean m_oppositeMovement;
    private int m_minAltitudeDiffStoppingMovement;
    
    public PathComputer(final MovementEffectUser mover, final Point3 targetCell, final int distanceMaxToReach, final EffectContext<WakfuEffect> context) {
        super();
        this.m_minAltitudeDiffStoppingMovement = 0;
        this.m_mover = mover;
        this.m_targetCell = targetCell;
        this.m_distance = distanceMaxToReach;
        this.m_context = context;
        if (this.m_context != null) {
            this.m_fightMap = this.m_context.getFightMap();
        }
    }
    
    private boolean init() {
        this.initPathChecker();
        return this.initTopologyMap() && this.initSource();
    }
    
    private void initPathChecker() {
        (this.m_pathChecker = new PathChecker()).setMoverCaracteristics(this.m_mover.getHeight(), this.m_mover.getPhysicalRadius(), this.m_mover.getJumpCapacity());
    }
    
    private boolean initSource() {
        final int x = this.m_mover.getWorldCellX();
        final int y = this.m_mover.getWorldCellY();
        final short z = this.m_mover.getWorldCellAltitude();
        final int sourceNumZ = this.m_topologyMap.getPathData(x, y, PathComputer.m_sourceCellPathData, 0);
        final int sourceIndex = TopologyChecker.getIndexFromZ(0, sourceNumZ, PathComputer.m_sourceCellPathData, z);
        if (sourceIndex == -32768) {
            PathComputer.m_logger.error((Object)("Unable to find the cell (" + x + "; " + y + ") with z value = " + z));
            return false;
        }
        return true;
    }
    
    private boolean initTopologyMap() {
        final int x = this.m_mover.getWorldCellX();
        final int y = this.m_mover.getWorldCellY();
        this.m_topologyMap = this.m_fightMap.getTopologyMapFromCell(x, y);
        if (this.m_topologyMap == null) {
            PathComputer.m_logger.error((Object)("The cell (" + x + "; " + y + ") is not in the fightMap"));
            return false;
        }
        return true;
    }
    
    public PathComputationResult computeMovement() {
        if (!this.init()) {
            PathComputer.m_logger.error((Object)"Impossible d'initialiser correctement le computer");
            return null;
        }
        int x = this.m_mover.getWorldCellX();
        int y = this.m_mover.getWorldCellY();
        short z = this.m_mover.getWorldCellAltitude();
        int fallHeight = 0;
        final Direction8 dir = this.getDirection();
        final PathComputationResult pathRes = new PathComputationResult();
        if (dir.m_x == 0 && dir.m_y == 0) {
            pathRes.addNextCell(x, y, z);
            return pathRes;
        }
        this.m_numSourceZ = this.m_topologyMap.getPathData(x, y, PathComputer.m_destCellPathData, 0);
        for (int i = 0; i < this.m_distance; ++i) {
            x += dir.m_x;
            y += dir.m_y;
            final CellValidationResult res = this.isCellValidForMovement(x, y, z);
            if (res.isError()) {
                break;
            }
            if (res.isStopped()) {
                x -= dir.m_x;
                y -= dir.m_y;
                pathRes.setBlocked(true);
                pathRes.setObstacle(res.getObstacle());
                break;
            }
            final int heightDiff = res.getArrivalAltitude() - z;
            if (heightDiff < 0) {
                fallHeight -= heightDiff;
            }
            pathRes.setNbCellsCovered(i + 1);
            pathRes.setFallHeight(fallHeight);
            z = res.getArrivalAltitude();
            pathRes.addNextCell(x, y, z);
            if (this.isOnFinalCell(x, y)) {
                break;
            }
            if (this.stoppedByGrippedArea()) {
                break;
            }
        }
        return pathRes;
    }
    
    private Direction8 getDirection() {
        if (!this.m_oppositeMovement) {
            return new Vector3i(this.m_mover.getWorldCellX(), this.m_mover.getWorldCellY(), this.m_mover.getWorldCellAltitude(), this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ()).toDirection4();
        }
        return new Vector3i(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_mover.getWorldCellX(), this.m_mover.getWorldCellY(), this.m_mover.getWorldCellAltitude()).toDirection4();
    }
    
    private CellValidationResult isCellValidForMovement(final int destX, final int destY, final short z) {
        final CellValidationResult res = new CellValidationResult();
        res.setValid(false);
        res.setError(false);
        if (!this.hasTopologyMapForCell(destX, destY)) {
            PathComputer.m_logger.error((Object)("The cell (" + destX + "; " + destY + ") is not in the fightMap"));
            res.setError(true);
            return res;
        }
        if (!this.m_fightMap.isInsideOrBorder(destX, destY)) {
            res.setStopped(true);
            return res;
        }
        final int destNumZ = this.m_topologyMap.getPathData(destX, destY, PathComputer.m_destCellPathData, 0);
        if (!this.nextAltitudeReachable(destNumZ, z)) {
            res.setStopped(true);
            return res;
        }
        res.setArrivalAltitude(this.getLowestAltitudeReachableFromNextCell());
        this.updatePathData();
        this.m_numSourceZ = destNumZ;
        if (this.m_mover.isBlockingMovement() && this.m_fightMap.isMovementBlocked(destX, destY, z)) {
            final FightObstacle obs = this.m_fightMap.getObstacle(destX, destY);
            if (obs instanceof EffectUser) {
                res.setObstacle(obs);
            }
            res.setStopped(true);
            return res;
        }
        if (this.m_mover instanceof AbstractBombEffectArea) {
            final List<BasicEffectArea> areasOnPos = this.m_context.getEffectAreaManager().getTargetableEffectAreasListOnPositionOfType(destX, destY, EffectAreaType.BOMB.getTypeId());
            if (!areasOnPos.isEmpty()) {
                res.setStopped(true);
                return res;
            }
        }
        res.setValid(true);
        return res;
    }
    
    private boolean nextAltitudeReachable(final int destNumZ, final short previousZ) {
        final int currentIndex = TopologyChecker.getIndexFromZ(this.m_sourceIndex, this.m_numSourceZ, PathComputer.m_sourceCellPathData, previousZ);
        if (currentIndex == -32768) {
            PathComputer.m_logger.error((Object)"Pas d'altitude valide trouv\u00e9e pour la prochaine cellule");
            return false;
        }
        final int validIndexesCount = this.m_pathChecker.getValidIndexesOnNextCell(currentIndex + this.m_sourceIndex, this.m_sourceIndex, this.m_numSourceZ, PathComputer.m_sourceCellPathData, 0, destNumZ, PathComputer.m_destCellPathData);
        if (validIndexesCount <= 0) {
            return false;
        }
        final short nextZ = PathComputer.m_destCellPathData[this.m_pathChecker.m_validIndexes[0]].m_z;
        return this.m_minAltitudeDiffStoppingMovement <= 0 || nextZ - previousZ <= this.m_minAltitudeDiffStoppingMovement;
    }
    
    private short getLowestAltitudeReachableFromNextCell() {
        return PathComputer.m_destCellPathData[this.m_pathChecker.m_validIndexes[0]].m_z;
    }
    
    private void updatePathData() {
        final CellPathData[] tmp = PathComputer.m_sourceCellPathData;
        PathComputer.m_sourceCellPathData = PathComputer.m_destCellPathData;
        PathComputer.m_destCellPathData = tmp;
        this.m_sourceIndex = this.m_pathChecker.m_validIndexes[0];
    }
    
    private boolean hasTopologyMapForCell(final int destX, final int destY) {
        if (this.m_topologyMap.isInMap(destX, destY)) {
            return true;
        }
        this.m_topologyMap = this.m_fightMap.getTopologyMapFromCell(destX, destY);
        return this.m_topologyMap != null;
    }
    
    private boolean isOnFinalCell(final int x, final int y) {
        return this.m_targetCell.equals(x, y);
    }
    
    private boolean stoppedByGrippedArea() {
        if (this.m_context == null) {
            return false;
        }
        final Iterable<BasicEffectArea> activeEffectAreas = this.m_context.getEffectAreaManager().getActiveEffectAreas();
        if (this.m_context.getEffectAreaManager() == null) {
            return false;
        }
        if (activeEffectAreas != null) {
            final Iterator<BasicEffectArea> effectAreaIterator = (Iterator<BasicEffectArea>)activeEffectAreas.iterator();
            boolean grip = false;
            while (effectAreaIterator.hasNext()) {
                final AbstractEffectArea basicEffectArea = effectAreaIterator.next();
                if (basicEffectArea.isActiveProperty(EffectAreaPropertyType.GRIP)) {
                    grip = true;
                    break;
                }
            }
            if (grip) {
                return true;
            }
        }
        return false;
    }
    
    public void setOppositeMovement(final boolean oppositeMovement) {
        this.m_oppositeMovement = oppositeMovement;
    }
    
    public void setMinAltitudeDiffStoppingMovement(final int minAltitudeDiffStoppingMovement) {
        this.m_minAltitudeDiffStoppingMovement = minAltitudeDiffStoppingMovement;
    }
    
    static {
        PathComputer.m_logger = Logger.getLogger((Class)PathComputer.class);
        PathComputer.m_sourceCellPathData = new CellPathData[32];
        for (int i = 0; i < PathComputer.m_sourceCellPathData.length; ++i) {
            PathComputer.m_sourceCellPathData[i] = new CellPathData();
        }
        PathComputer.m_destCellPathData = new CellPathData[32];
        for (int i = 0; i < PathComputer.m_destCellPathData.length; ++i) {
            PathComputer.m_destCellPathData[i] = new CellPathData();
        }
    }
}
