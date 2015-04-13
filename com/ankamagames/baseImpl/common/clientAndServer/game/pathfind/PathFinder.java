package com.ankamagames.baseImpl.common.clientAndServer.game.pathfind;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.pathfinder.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PathFinder implements Releasable
{
    private static final Logger m_logger;
    private static final boolean DEBUG_PATHFINDER = false;
    private static final Logger m_debugLogger;
    public static final int PATH_NOT_FOUND = -1;
    public static final int MAX_PATH_LENGTH = 128;
    public static int MAX_NODES;
    public static final int MAGIC_NUMBER = 3;
    public static int MAX_CELL_PATH_DATA;
    private static final float DIAGONAL_COST = 1.4f;
    private static final float[] JUMP_COST_PER_HEIGHT_UNIT;
    private static final float DIRECTION_CHANGE_COST = 0.9f;
    private static final float DIRECTION_CHANGE_COST_4D = 10.0f;
    public static final PathFindResult PATH_NOT_FOUND_RESULT;
    private static final int[][] DIRECTIONS_DX_FIRST;
    private static final int[][] DIRECTIONS_DY_FIRST;
    private int m_moverHeight;
    private short m_moverJumpCapacity;
    protected byte m_moverPhysicalRadius;
    protected final TLongArrayList m_startCellsHashcodes;
    protected long m_stopCellHashcode;
    private TopologyMapInstanceSet m_topologyMapInstanceSet;
    private PathFinderParameters m_parameters;
    private final PathChecker m_pathChecker;
    protected short m_lastPathFoundSize;
    protected long[] m_lastPathFoundData;
    private final PathFinderNode[] m_nodes;
    private short m_lastNodeIndex;
    private final CellPathData[] m_cellPathData;
    private short m_nextCellPathDataIndex;
    private final PathFinderNode[] m_openNodes;
    private short m_firstOpenNodeIndex;
    private short m_lastOpenNodeIndex;
    private final TLongIntHashMap m_cellPathDataHashToIndexAndCount;
    private final TLongByteHashMap m_calculatedMovesValidity;
    private final TLongShortHashMap m_nodesHashToIndex;
    private static final ObjectPool m_staticPool;
    static final /* synthetic */ boolean $assertionsDisabled;
    
    protected PathFinder() {
        super();
        this.m_startCellsHashcodes = new TLongArrayList();
        this.m_pathChecker = new PathChecker();
        this.m_lastPathFoundData = new long[128];
        this.m_openNodes = new PathFinderNode[PathFinder.MAX_NODES];
        this.m_firstOpenNodeIndex = -1;
        this.m_lastOpenNodeIndex = -1;
        this.m_cellPathDataHashToIndexAndCount = new TLongIntHashMap();
        this.m_calculatedMovesValidity = new TLongByteHashMap();
        this.m_nodesHashToIndex = new TLongShortHashMap();
        this.m_nodes = new PathFinderNode[PathFinder.MAX_NODES];
        for (int i = 0; i < PathFinder.MAX_NODES; ++i) {
            this.m_nodes[i] = new PathFinderNode();
        }
        this.m_cellPathData = new CellPathData[PathFinder.MAX_CELL_PATH_DATA];
        for (int i = 0; i < PathFinder.MAX_CELL_PATH_DATA; ++i) {
            this.m_cellPathData[i] = new CellPathData();
        }
    }
    
    public static PathFinder checkOut(final int maxNodes) {
        PathFinder.MAX_NODES = maxNodes;
        PathFinder.MAX_CELL_PATH_DATA = PathFinder.MAX_NODES * 3;
        return checkOut();
    }
    
    public static PathFinder checkOut() {
        try {
            return (PathFinder)PathFinder.m_staticPool.borrowObject();
        }
        catch (Exception e) {
            PathFinder.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    @Override
    public void release() {
        try {
            PathFinder.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            PathFinder.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_moverHeight = 0;
        this.m_moverJumpCapacity = 0;
        this.m_moverPhysicalRadius = 0;
        this.m_lastNodeIndex = 0;
        this.m_nextCellPathDataIndex = 0;
        this.m_firstOpenNodeIndex = -1;
        this.m_lastOpenNodeIndex = -1;
        this.m_cellPathDataHashToIndexAndCount.clear();
        this.m_calculatedMovesValidity.clear();
        this.m_nodesHashToIndex.clear();
        this.m_startCellsHashcodes.reset();
        this.m_topologyMapInstanceSet = null;
        this.m_lastPathFoundSize = -1;
        this.m_stopCellHashcode = -1L;
        this.m_pathChecker.setMoverCaracteristics(0, (byte)(-1), -1);
        this.m_parameters = null;
    }
    
    public final void setTopologyMapInstanceSet(final TopologyMapInstanceSet set) {
        this.m_topologyMapInstanceSet = set;
    }
    
    public void setParameters(final PathFinderParameters parameters) {
        this.m_parameters = parameters;
    }
    
    public void setMoverCaracteristics(final int moverHeight, final byte moverPhysicalRadius, final short moverJumpCapacity) {
        this.m_moverHeight = moverHeight;
        this.m_moverJumpCapacity = moverJumpCapacity;
        this.m_moverPhysicalRadius = moverPhysicalRadius;
        this.m_pathChecker.setMoverCaracteristics(moverHeight, moverPhysicalRadius, moverJumpCapacity);
    }
    
    public void clearStartCells() {
        this.m_startCellsHashcodes.reset();
    }
    
    public void addStartCell(final int x, final int y, final short z) {
        this.m_startCellsHashcodes.add(get3DHashCode(x, y, z));
    }
    
    public void addStartCell(final Point3 startPosition) {
        this.m_startCellsHashcodes.add(get3DHashCode(startPosition.getX(), startPosition.getY(), startPosition.getZ()));
    }
    
    public void setStopCell(final int x, final int y, final short z) {
        this.m_stopCellHashcode = get3DHashCode(x, y, z);
    }
    
    public void setStopCell(final Point3 stopCell) {
        assert stopCell != null : "can't define a null cell as the destination";
        this.m_stopCellHashcode = get3DHashCode(stopCell.getX(), stopCell.getY(), stopCell.getZ());
    }
    
    public short getPathSize() {
        return this.m_lastPathFoundSize;
    }
    
    public long[] getPathData() {
        return this.m_lastPathFoundData;
    }
    
    public final PathFindResult getPathResult() {
        if (this.m_lastPathFoundSize == -1) {
            return PathFinder.PATH_NOT_FOUND_RESULT;
        }
        final PathFindResult result = new PathFindResult(this.m_lastPathFoundSize);
        for (int i = 0; i < this.m_lastPathFoundSize; ++i) {
            final long pathData = this.m_lastPathFoundData[this.m_lastPathFoundSize - 1 - i];
            final int x = getXFrom3DHashCode(pathData);
            final int y = getYFrom3DHashCode(pathData);
            final short z = getZFrom3DHashCode(pathData);
            result.setStep(i, x, y, z);
        }
        return result;
    }
    
    public final PathFindResult getReversePathResult() {
        if (this.m_lastPathFoundSize == -1) {
            return PathFinder.PATH_NOT_FOUND_RESULT;
        }
        final PathFindResult result = new PathFindResult(this.m_lastPathFoundSize);
        for (int i = 0; i < this.m_lastPathFoundSize; ++i) {
            final long pathData = this.m_lastPathFoundData[i];
            final int x = getXFrom3DHashCode(pathData);
            final int y = getYFrom3DHashCode(pathData);
            final short z = getZFrom3DHashCode(pathData);
            result.setStep(i, x, y, z);
        }
        return result;
    }
    
    public int findPath() {
        assert this.m_topologyMapInstanceSet != null : "no TopologyMapInstanceSet defined";
        assert this.m_moverHeight > 0 : "invalid m_moverHeight";
        assert this.m_moverJumpCapacity >= 0 : "invalid m_moverJumpCapacity";
        assert this.m_moverPhysicalRadius >= 0 : "invalid m_moverPhysicalRadius : " + this.m_moverPhysicalRadius;
        assert this.m_parameters != null : "no PathFinderParameters defined";
        assert this.m_stopCellHashcode != -1L : "stop cell not defined";
        assert !this.m_startCellsHashcodes.isEmpty() : "start cells not defined";
        assert this.m_parameters.m_searchLimit > 0 : "search limit not defined in the path find parameters";
        if (!PathFinder.$assertionsDisabled && !this.m_parameters.m_includeEndCell && this.m_parameters.m_stopBeforeEndCell) {
            throw new AssertionError((Object)"stopping before the end and asking for the end cell to be removed assumes the last cell of the path will be known");
        }
        this.m_nodesHashToIndex.clear();
        this.m_lastNodeIndex = 0;
        this.m_cellPathDataHashToIndexAndCount.clear();
        this.m_nextCellPathDataIndex = 0;
        this.m_firstOpenNodeIndex = -1;
        this.m_lastOpenNodeIndex = -1;
        final PathFinderNode endNode = this.getNode(this.m_stopCellHashcode);
        if (endNode == null) {
            return -1;
        }
        if (!TopologyChecker.checkHeightIndexValidity(endNode.m_firstElementIndex + endNode.m_nodeElementIndex, endNode.m_firstElementIndex, endNode.m_cellElementsCount, this.m_cellPathData, this.m_moverHeight)) {
            return -1;
        }
        final int stopCellX = getXFrom3DHashCode(this.m_stopCellHashcode);
        final int stopCellY = getYFrom3DHashCode(this.m_stopCellHashcode);
        final short stopCellZ = getZFrom3DHashCode(this.m_stopCellHashcode);
        if (!this.m_parameters.m_stopBeforeEndCell && this.isMovementBlockedFromTopology(stopCellX, stopCellY, stopCellZ, stopCellX, stopCellY, stopCellZ)) {
            return -1;
        }
        if (this.m_moverPhysicalRadius > 0) {
            for (int incX = -this.m_moverPhysicalRadius; incX <= this.m_moverPhysicalRadius; ++incX) {
                for (int incY = -this.m_moverPhysicalRadius; incY <= this.m_moverPhysicalRadius; ++incY) {
                    if (this.isMovementBlockedFromTopology(stopCellX + incX, stopCellY + incY, stopCellZ, stopCellX + incX, stopCellY + incY, stopCellZ)) {
                        return -1;
                    }
                }
            }
        }
        for (int i = this.m_startCellsHashcodes.size() - 1; i >= 0; --i) {
            final long hashCode = this.m_startCellsHashcodes.get(i);
            if (hashCode == this.m_stopCellHashcode) {
                return this.m_lastPathFoundSize = 0;
            }
            final PathFinderNode startNode = this.getNode(hashCode);
            if (startNode == null) {
                PathFinder.m_logger.info((Object)("Invalid start cell for pathfind search : doesn't exist. HASHCODE : " + hashCode));
            }
            else {
                final int startCellX = getXFrom3DHashCode(hashCode);
                final int startCellY = getYFrom3DHashCode(hashCode);
                short startCellZ = getZFrom3DHashCode(hashCode);
                if (this.m_moverPhysicalRadius > 0 && stopCellZ != startCellZ) {
                    if (!this.m_parameters.m_permissiveStartCellAltitude) {
                        continue;
                    }
                    startCellZ = stopCellZ;
                }
                if (!TopologyChecker.checkHeightIndexValidity(startNode.m_firstElementIndex + startNode.m_nodeElementIndex, startNode.m_firstElementIndex, startNode.m_cellElementsCount, this.m_cellPathData, this.m_moverHeight)) {
                    if (!this.m_parameters.m_permissiveStartCellAltitude) {
                        PathFinder.m_logger.info((Object)("Invalid start cell (" + startCellX + ", " + startCellY + ", " + startCellZ + ") for pathfind search : not a suitable position for the mover. "));
                        continue;
                    }
                    startCellZ = TopologyChecker.getHighestWalkableZ(startNode.m_firstElementIndex, startNode.m_cellElementsCount, this.m_cellPathData, (short)(startCellZ + this.m_moverJumpCapacity), this.m_moverHeight);
                    if (startCellZ == -32768) {
                        continue;
                    }
                }
                startNode.m_h = this.computeManhattanDistance(startCellX, startCellY, stopCellX, stopCellY);
                if (this.m_parameters.m_maxPathLength <= 0 || this.m_parameters.m_maxPathLength >= startNode.m_h) {
                    startNode.m_g = 0.0f;
                    startNode.m_f = startNode.m_h;
                    startNode.m_opened = true;
                    startNode.m_incomingDirection = -1;
                    this.insertOpenNode(startNode);
                }
            }
        }
        if (this.m_firstOpenNodeIndex == -1) {
            return -1;
        }
        final boolean initialDirectionParameters = this.m_parameters.m_limitTo4Directions;
        if (this.m_moverPhysicalRadius > 0 && !this.m_parameters.m_limitTo4Directions) {
            this.m_parameters.m_limitTo4Directions = true;
        }
        int result;
        if (this.m_moverPhysicalRadius == 0) {
            result = this.findPathForSingleCellMover(stopCellX, stopCellY, endNode);
        }
        else {
            result = this.findPathForMultiCellsMover(stopCellX, stopCellY, getZFrom3DHashCode(this.m_stopCellHashcode), endNode);
        }
        this.m_parameters.m_limitTo4Directions = initialDirectionParameters;
        return result;
    }
    
    public boolean checkMovementOnNextCell(final Point3 from, final Point3 to) {
        if (from.getDistance(to) != 1) {
            PathFinder.m_logger.error((Object)("Unable to checkMovementOnNextCell if cells are not adjacent :" + from + ", " + to));
            return false;
        }
        final int thisX = from.getX();
        final int thisY = from.getY();
        final short thisZ = from.getZ();
        final int nextX = to.getX();
        final int nextY = to.getY();
        final short nextZ = to.getZ();
        if (this.m_moverPhysicalRadius != 0) {
            for (int incX = -this.m_moverPhysicalRadius; incX <= this.m_moverPhysicalRadius; ++incX) {
                for (int incY = -this.m_moverPhysicalRadius; incY <= this.m_moverPhysicalRadius; ++incY) {
                    if (!this.m_topologyMapInstanceSet.isInMap(nextX + incX, nextY + incY)) {
                        return false;
                    }
                    if (this.isMovementBlockedFromTopology(nextX + incX, nextY + incY, nextZ, thisX + incX, thisY + incY, thisZ)) {
                        return false;
                    }
                    final boolean movementValid = this.checkMovementValidityForMultiCellsMovers(thisX + incX, thisY + incY, to.getZ(), nextX + incX, nextY + incY);
                    if (!movementValid) {
                        return false;
                    }
                }
            }
            return true;
        }
        if (this.isMovementBlockedFromTopology(nextX, nextY, nextZ, thisX, thisY, thisZ)) {
            return false;
        }
        final int thisCellIndexAndCount = this.retrieveCellPathData(thisX, thisY);
        if (thisCellIndexAndCount == 0) {
            return false;
        }
        final int thisCellStartIndex = getIndexFromIndexAndCountHashCode(thisCellIndexAndCount);
        final int thisCellElementsCount = getCountFromIndexAndCountHashCode(thisCellIndexAndCount);
        final int thisZIndex = TopologyChecker.getIndexFromZ(thisCellStartIndex, thisCellElementsCount, this.m_cellPathData, from.getZ());
        if (thisZIndex == -32768) {
            return false;
        }
        final int nextCellIndexAndCount = this.retrieveCellPathData(nextX, nextY);
        if (nextCellIndexAndCount == 0) {
            return false;
        }
        final int nextCellStartIndex = getIndexFromIndexAndCountHashCode(nextCellIndexAndCount);
        final int nextCellElementsCount = getCountFromIndexAndCountHashCode(nextCellIndexAndCount);
        final int nextZIndex = TopologyChecker.getIndexFromZ(nextCellStartIndex, nextCellElementsCount, this.m_cellPathData, to.getZ());
        return nextZIndex != -32768 && this.m_pathChecker.checkMoveOnNextCellValidity(thisCellStartIndex + thisZIndex, thisCellStartIndex, thisCellElementsCount, this.m_cellPathData, nextCellStartIndex + nextZIndex, nextCellStartIndex, nextCellElementsCount, this.m_cellPathData);
    }
    
    private int findPathForSingleCellMover(final int stopCellX, final int stopCellY, final PathFinderNode endNode) {
        int nodesTested = 0;
        PathFinderNode node;
        while ((node = this.popFirstNode()) != null) {
            ++nodesTested;
            if (this.m_parameters.m_searchLimit < nodesTested) {
                return -1;
            }
            if (node == endNode) {
                return this.computeResultPath(node);
            }
            final CellPathData nodeData = this.m_cellPathData[node.m_firstElementIndex + node.m_nodeElementIndex];
            final int[][] selectedDirections = this.selectDirectionIterationOrder(nodeData, stopCellX, stopCellY);
            byte validDirectionsBitset = 0;
            for (byte directionsCount = this.getDirectionCount(), dirIndex = 0; dirIndex < directionsCount; ++dirIndex) {
                final boolean isDiag = dirIndex > 3;
                final int[] directions = selectedDirections[dirIndex];
                if (!isDiag || (validDirectionsBitset & directions[2]) == directions[2]) {
                    final int nextX = nodeData.m_x + directions[0];
                    final int nextY = nodeData.m_y + directions[1];
                    final short nextZ = nodeData.m_z;
                    if (this.m_parameters.m_stopBeforeEndCell && !this.m_parameters.m_checkValidityIfStopBeforeEndCell && nextX == stopCellX && nextY == stopCellY) {
                        return this.computeResultPath(node);
                    }
                    final int nextCellIndexAndCount = this.retrieveCellPathData(nextX, nextY);
                    if (nextCellIndexAndCount != 0) {
                        final int nextCellStartIndex = getIndexFromIndexAndCountHashCode(nextCellIndexAndCount);
                        final int nextCellElementsCount = getCountFromIndexAndCountHashCode(nextCellIndexAndCount);
                        if (this.isMovementBlockedFromTopology(nextX, nextY, nextZ, nodeData.m_x, nodeData.m_y, nodeData.m_z)) {
                            if (!this.m_parameters.m_stopBeforeEndCell) {
                                continue;
                            }
                            if (nextX != stopCellX) {
                                continue;
                            }
                            if (nextY != stopCellY) {
                                continue;
                            }
                        }
                        final int validIndexesOnNextCell = this.m_pathChecker.getValidIndexesOnNextCell(node.m_firstElementIndex + node.m_nodeElementIndex, node.m_firstElementIndex, node.m_cellElementsCount, this.m_cellPathData, nextCellStartIndex, nextCellElementsCount, this.m_cellPathData);
                        if (validIndexesOnNextCell != 0) {
                            final TopologyMapInstance topologyMapInstance = this.m_topologyMapInstanceSet.getMap(nextX, nextY);
                            if (topologyMapInstance != null) {
                                for (int i = 0; i < validIndexesOnNextCell; ++i) {
                                    final CellPathData nextNodeData = this.m_cellPathData[this.m_pathChecker.m_validIndexes[i]];
                                    assert nextNodeData.m_x == nextX && nextNodeData.m_y == nextY : "Pathchecker.getValidIndexes returned a CellPathData not corresponding to the given bounds";
                                    if (topologyMapInstance.isBlocked(nextX, nextY, nextNodeData.m_z)) {
                                        if (!this.m_parameters.m_stopBeforeEndCell) {
                                            continue;
                                        }
                                        if (nextX != stopCellX) {
                                            continue;
                                        }
                                        if (nextY != stopCellY) {
                                            continue;
                                        }
                                    }
                                    final PathFinderNode nextNode = this.getNode(nextX, nextY, nextNodeData.m_z);
                                    if (nextNode == null) {
                                        System.out.println("Node inexistant ou trop de nodes. nodes testes : " + nodesTested + "/" + this.m_parameters.m_searchLimit);
                                    }
                                    else if (nextNode != node.m_parentNode) {
                                        if (this.m_parameters.m_allowMoboSterile || !nextNodeData.isMoboSteryl()) {
                                            if (this.m_parameters.m_allowGap || !nextNodeData.isGap()) {
                                                if (this.m_parameters.m_stopBeforeEndCell && nextNode == endNode) {
                                                    return this.computeResultPath(node);
                                                }
                                                if (isDiag) {
                                                    if (!this.checkNextCell(nextNode, nextX, nodeData.m_y)) {
                                                        continue;
                                                    }
                                                    if (!this.checkNextCell(nextNode, nodeData.m_x, nextY)) {
                                                        continue;
                                                    }
                                                }
                                                else {
                                                    validDirectionsBitset |= (byte)selectedDirections[dirIndex][2];
                                                }
                                                final float newG = node.m_g + this.computeNodeWeight(node, nodeData, nextNode, nextNodeData, dirIndex);
                                                final byte newPathLength = (byte)(node.m_currentPathLength + 1);
                                                if (nextNode.m_g >= newG) {
                                                    if (nextNode.m_g != newG || nextNode.m_incomingDirection >= dirIndex) {
                                                        if (this.m_parameters.m_maxPathLength <= 0 || newPathLength <= this.m_parameters.m_maxPathLength) {
                                                            nextNode.m_g = newG;
                                                            if (nextNode.m_h == 0.0f) {
                                                                nextNode.m_h = this.computeHeuristic(nextX, nextY, stopCellX, stopCellY);
                                                            }
                                                            nextNode.m_f = nextNode.m_g + nextNode.m_h;
                                                            nextNode.m_parentNode = node;
                                                            nextNode.m_incomingDirection = dirIndex;
                                                            nextNode.m_currentPathLength = newPathLength;
                                                            if (nextNode.m_opened) {
                                                                this.removeOpenNode(nextNode);
                                                            }
                                                            this.insertOpenNode(nextNode);
                                                            nextNode.m_opened = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            node.m_opened = false;
        }
        return -1;
    }
    
    private boolean checkNextCell(final PathFinderNode nextNode, final int nextX, final int nextY) {
        final int indexAndCount = this.retrieveCellPathData(nextX, nextY);
        if (indexAndCount == 0) {
            return false;
        }
        final int startIndex = getIndexFromIndexAndCountHashCode(indexAndCount);
        final int elementCount = getCountFromIndexAndCountHashCode(indexAndCount);
        final int validIndexes = this.m_pathChecker.getValidIndexesOnNextCell(nextNode.m_firstElementIndex + nextNode.m_nodeElementIndex, nextNode.m_firstElementIndex, nextNode.m_cellElementsCount, this.m_cellPathData, startIndex, elementCount, this.m_cellPathData);
        return validIndexes != 0;
    }
    
    private int[][] selectDirectionIterationOrder(final CellPathData currentNodeData, final int stopCellX, final int stopCellY) {
        if (!this.m_parameters.m_reduceGreaterAxisFirst) {
            return PathFinder.DIRECTIONS_DX_FIRST;
        }
        final int dX = Math.abs(stopCellX - currentNodeData.m_x);
        final int dY = Math.abs(stopCellY - currentNodeData.m_y);
        if (dX >= dY) {
            return PathFinder.DIRECTIONS_DX_FIRST;
        }
        return PathFinder.DIRECTIONS_DY_FIRST;
    }
    
    private int findPathForMultiCellsMover(final int stopCellX, final int stopCellY, final short stopCellZ, final PathFinderNode endNode) {
        this.m_calculatedMovesValidity.clear();
        int nodesTested = 0;
        PathFinderNode node;
        while ((node = this.popFirstNode()) != null) {
            ++nodesTested;
            if (this.m_parameters.m_searchLimit < nodesTested) {
                return -1;
            }
            if (node == endNode) {
                return this.computeResultPath(node);
            }
            final CellPathData nodeData = this.m_cellPathData[node.m_firstElementIndex + node.m_nodeElementIndex];
            final int[][] selectedDirections = this.selectDirectionIterationOrder(nodeData, stopCellX, stopCellY);
            final byte directionsCount = this.getDirectionCount();
            byte dirIndex = 0;
        Label_0622_Outer:
            while (dirIndex < directionsCount) {
                final int[] directions = selectedDirections[dirIndex];
                final int nextX = nodeData.m_x + directions[0];
                final int nextY = nodeData.m_y + directions[1];
                final short nextZ = nodeData.m_z;
                if (this.m_parameters.m_stopBeforeEndCell && !this.m_parameters.m_checkValidityIfStopBeforeEndCell && nextX == stopCellX && nextY == stopCellY) {
                    return this.computeResultPath(node);
                }
                int incX = -this.m_moverPhysicalRadius;
            Label_0622:
                while (true) {
                    while (incX <= this.m_moverPhysicalRadius) {
                        for (int incY = -this.m_moverPhysicalRadius; incY <= this.m_moverPhysicalRadius; ++incY) {
                            if (!this.m_topologyMapInstanceSet.isInMap(nextX + incX, nextY + incY)) {
                                break Label_0622;
                            }
                            if (this.isMovementBlockedFromTopology(nextX + incX, nextY + incY, nextZ, nextX + incX, nextY + incY, nextZ)) {
                                if (!this.m_parameters.m_stopBeforeEndCell) {
                                    break Label_0622;
                                }
                                if (nextX + incX != stopCellX) {
                                    break Label_0622;
                                }
                                if (nextY + incY != stopCellY) {
                                    break Label_0622;
                                }
                            }
                            final boolean movementValid = this.checkMovementValidityForMultiCellsMovers(nodeData.m_x + incX, nodeData.m_y + incY, stopCellZ, nextX + incX, nextY + incY);
                            if (!movementValid) {
                                break Label_0622;
                            }
                        }
                        ++incX;
                        continue Label_0622_Outer;
                        ++dirIndex;
                        continue Label_0622_Outer;
                    }
                    final PathFinderNode nextNode = this.getNode(nextX, nextY, stopCellZ);
                    if (nextNode == null) {
                        System.out.println("Node inexistant ou trop de nodes. nodes testes : " + nodesTested + "/" + this.m_parameters.m_searchLimit);
                        continue Label_0622;
                    }
                    if (nextNode == node.m_parentNode) {
                        continue Label_0622;
                    }
                    if (this.m_parameters.m_stopBeforeEndCell && nextNode == endNode) {
                        return this.computeResultPath(node);
                    }
                    final float newG = node.m_g + this.computeNodeWeight(node, nodeData, nextNode, this.m_cellPathData[node.m_firstElementIndex + node.m_nodeElementIndex], dirIndex);
                    final byte newPathLength = (byte)(node.m_currentPathLength + 1);
                    if (nextNode.m_g <= newG) {
                        continue Label_0622;
                    }
                    if (this.m_parameters.m_maxPathLength > 0 && newPathLength > this.m_parameters.m_maxPathLength) {
                        continue Label_0622;
                    }
                    nextNode.m_g = newG;
                    if (nextNode.m_h == 0.0f) {
                        nextNode.m_h = this.computeHeuristic(nextX, nextY, stopCellX, stopCellY);
                    }
                    nextNode.m_f = nextNode.m_g + nextNode.m_h;
                    nextNode.m_parentNode = node;
                    nextNode.m_incomingDirection = dirIndex;
                    nextNode.m_currentPathLength = newPathLength;
                    if (nextNode.m_opened) {
                        this.removeOpenNode(nextNode);
                    }
                    this.insertOpenNode(nextNode);
                    nextNode.m_opened = true;
                    continue Label_0622;
                }
            }
            node.m_opened = false;
        }
        return -1;
    }
    
    private boolean checkMovementValidityForMultiCellsMovers(final int currentX, final int currentY, final short movementZ, final int nextX, final int nextY) {
        final int currentCellIndexAndCount = this.retrieveCellPathData(currentX, currentY);
        if (currentCellIndexAndCount == 0) {
            return false;
        }
        final int currentCellStartIndex = getIndexFromIndexAndCountHashCode(currentCellIndexAndCount);
        final int currentCellElementsCount = getCountFromIndexAndCountHashCode(currentCellIndexAndCount);
        final short currentCellIndex = TopologyChecker.getIndexFromZ(currentCellStartIndex, currentCellElementsCount, this.m_cellPathData, movementZ);
        if (currentCellIndex == -32768) {
            return false;
        }
        final int nextCellIndexAndCount = this.retrieveCellPathData(nextX, nextY);
        if (nextCellIndexAndCount == 0) {
            return false;
        }
        final int nextCellStartIndex = getIndexFromIndexAndCountHashCode(nextCellIndexAndCount);
        final long movementHashCode = this.getMovementHashCode(currentCellStartIndex, nextCellStartIndex);
        final byte preCalculatedValidity = this.m_calculatedMovesValidity.get(movementHashCode);
        if (preCalculatedValidity != 0) {
            return preCalculatedValidity > 0;
        }
        final int nextCellElementsCount = getCountFromIndexAndCountHashCode(nextCellIndexAndCount);
        final int validIndexesOnNextCell = this.m_pathChecker.getValidIndexesOnNextCell(currentCellStartIndex + currentCellIndex, currentCellStartIndex, currentCellElementsCount, this.m_cellPathData, nextCellStartIndex, nextCellElementsCount, this.m_cellPathData);
        if (validIndexesOnNextCell == 0) {
            this.m_calculatedMovesValidity.put(movementHashCode, (byte)(-1));
            return false;
        }
        for (int i = 0; i < validIndexesOnNextCell; ++i) {
            final CellPathData nextNodeData = this.m_cellPathData[this.m_pathChecker.m_validIndexes[i]];
            if (nextNodeData.m_z == movementZ) {
                this.m_calculatedMovesValidity.put(movementHashCode, (byte)1);
                return true;
            }
        }
        this.m_calculatedMovesValidity.put(movementHashCode, (byte)(-1));
        return false;
    }
    
    private long getMovementHashCode(final int cellFirstIndex1, final int cellFirstIndex2) {
        if (cellFirstIndex1 < cellFirstIndex2) {
            return (cellFirstIndex1 << 32 & 0xFFFFFFFF00000000L) | (cellFirstIndex2 & 0xFFFFFFFFL);
        }
        return (cellFirstIndex2 << 32 & 0xFFFFFFFF00000000L) | (cellFirstIndex1 & 0xFFFFFFFFL);
    }
    
    private byte getDirectionCount() {
        return (byte)(this.m_parameters.m_limitTo4Directions ? 4 : 8);
    }
    
    protected boolean isMovementBlockedFromTopology(final int nextX, final int nextY, final short nextZ, final int currentX, final int currentY, final short currentZ) {
        if (this.m_topologyMapInstanceSet.isMovementBlocked(nextX, nextY, nextZ)) {
            return true;
        }
        if (TopologyMapManager.isGap(nextX, nextY)) {
            if (!TopologyMapManager.isJump(currentX, currentY)) {
                return true;
            }
            if (currentX != nextX && currentY != nextY) {
                return true;
            }
        }
        if (TopologyMapManager.isGap(currentX, currentY) && currentX != nextX && currentY != nextY) {
            return true;
        }
        final int stopCellX = getXFrom3DHashCode(this.m_stopCellHashcode);
        final int stopCellY = getYFrom3DHashCode(this.m_stopCellHashcode);
        final short stopCellZ = getZFrom3DHashCode(this.m_stopCellHashcode);
        return TopologyMapManager.isGap(nextX, nextY) && nextX == stopCellX && nextY == stopCellY && nextZ == stopCellZ;
    }
    
    protected float computeNodeWeight(final PathFinderNode currentNode, final CellPathData currentNodeData, final PathFinderNode nextNode, final CellPathData nextNodeData, final byte movementDirection) {
        float weight;
        if (movementDirection >= 4) {
            weight = 1.4f;
        }
        else {
            weight = 1.0f;
        }
        if (this.m_parameters.m_punishJump) {
            final int deltaZ = Math.abs(currentNodeData.m_z - nextNodeData.m_z);
            if (deltaZ >= PathFinder.JUMP_COST_PER_HEIGHT_UNIT.length) {
                weight += PathFinder.JUMP_COST_PER_HEIGHT_UNIT[PathFinder.JUMP_COST_PER_HEIGHT_UNIT.length - 1];
            }
            else {
                weight += PathFinder.JUMP_COST_PER_HEIGHT_UNIT[deltaZ];
            }
        }
        final boolean directionChanged = currentNode.m_incomingDirection != -1 && currentNode.m_incomingDirection != movementDirection;
        if (directionChanged) {
            if (!this.m_parameters.m_limitTo4Directions) {
                weight += 0.9f;
            }
            else if (this.m_parameters.m_punishDirectionChangeIn4D) {
                weight += 10.0f;
            }
        }
        return weight;
    }
    
    private float computeManhattanDistance(final int currentX, final int currentY, final int lastX, final int lastY) {
        final int diffX = Math.abs(currentX - lastX);
        final int diffY = Math.abs(currentY - lastY);
        if (this.m_parameters.m_limitTo4Directions) {
            return diffX + diffY;
        }
        if (diffX < diffY) {
            return diffX * 1.4f + Math.abs(diffX - diffY);
        }
        return diffY * 1.4f + Math.abs(diffX - diffY);
    }
    
    private float computeHeuristic(final int currentX, final int currentY, final int lastX, final int lastY) {
        final int diffX = Math.abs(currentX - lastX);
        final int diffY = Math.abs(currentY - lastY);
        if (this.m_parameters.m_limitTo4Directions) {
            if (diffX == 0 && diffY == 0) {
                return 0.0f;
            }
            final float realDistance = Vector2.length(diffX, diffY) * 0.01f;
            return diffX + diffY + realDistance;
        }
        else {
            if (diffX < diffY) {
                return diffX * 1.4f + Math.abs(diffX - diffY);
            }
            return diffY * 1.4f + Math.abs(diffX - diffY);
        }
    }
    
    private static int getIndexAndCountHashCode(final int index, final int count) {
        return (index & 0xFFFF) << 16 | (count & 0xFFFF);
    }
    
    private static int getIndexFromIndexAndCountHashCode(final int hashcode) {
        return hashcode >>> 16 & 0xFFFF;
    }
    
    private static int getCountFromIndexAndCountHashCode(final int hashcode) {
        return hashcode & 0xFFFF;
    }
    
    protected static long get2DHashCode(final int x, final int y) {
        return (x + 131071 & 0x3FFFF) << 18 | (y + 131071 & 0x3FFFF);
    }
    
    private static long get3DHashCode(final int x, final int y, final short z) {
        return (x + 131071 & 0x3FFFF) << 34 | (y + 131071 & 0x3FFFF) << 16 | (z + 32767 & 0xFFFF);
    }
    
    public static int getXFrom3DHashCode(final long hashcode) {
        return (int)((hashcode >>> 34 & 0x3FFFFL) - 131071L);
    }
    
    public static int getYFrom3DHashCode(final long hashcode) {
        return (int)((hashcode >>> 16 & 0x3FFFFL) - 131071L);
    }
    
    public static short getZFrom3DHashCode(final long hashcode) {
        return (short)((hashcode & 0xFFFFL) - 32767L);
    }
    
    private PathFinderNode popFirstNode() {
        if (this.m_firstOpenNodeIndex < 0) {
            return null;
        }
        final PathFinderNode node = this.m_openNodes[this.m_firstOpenNodeIndex];
        ++this.m_firstOpenNodeIndex;
        if (this.m_firstOpenNodeIndex > this.m_lastOpenNodeIndex) {
            final short n = -1;
            this.m_lastOpenNodeIndex = n;
            this.m_firstOpenNodeIndex = n;
        }
        return node;
    }
    
    private void removeOpenNode(final PathFinderNode node) {
        if (node == null) {
            return;
        }
        if (this.m_firstOpenNodeIndex == -1) {
            return;
        }
        int start = this.m_firstOpenNodeIndex;
        int end = this.m_lastOpenNodeIndex;
        if (this.m_openNodes[start] == node) {
            ++this.m_firstOpenNodeIndex;
            if (this.m_firstOpenNodeIndex > this.m_lastOpenNodeIndex) {
                final short n = -1;
                this.m_lastOpenNodeIndex = n;
                this.m_firstOpenNodeIndex = n;
            }
            return;
        }
        if (this.m_openNodes[end] == node) {
            --this.m_lastOpenNodeIndex;
            if (this.m_lastOpenNodeIndex < this.m_firstOpenNodeIndex) {
                final short n2 = -1;
                this.m_lastOpenNodeIndex = n2;
                this.m_firstOpenNodeIndex = n2;
            }
            return;
        }
        while (start < end) {
            final int middle = (start + end) / 2;
            if (this.m_openNodes[middle] == node) {
                System.arraycopy(this.m_openNodes, middle + 1, this.m_openNodes, middle, this.m_lastOpenNodeIndex - middle);
                --this.m_lastOpenNodeIndex;
                return;
            }
            if (node.m_f >= this.m_openNodes[middle].m_f) {
                start = middle + 1;
            }
            else {
                end = middle - 1;
            }
        }
    }
    
    private void insertOpenNode(final PathFinderNode node) {
        assert node != null : "'can't insert a null PathFinderNode";
        if (this.m_firstOpenNodeIndex == -1) {
            this.m_openNodes[0] = node;
            final boolean b = false;
            this.m_lastOpenNodeIndex = (short)(b ? 1 : 0);
            this.m_firstOpenNodeIndex = (short)(b ? 1 : 0);
            return;
        }
        int i = this.m_firstOpenNodeIndex;
        while (i <= this.m_lastOpenNodeIndex) {
            if (node.m_f < this.m_openNodes[i].m_f) {
                if (this.m_firstOpenNodeIndex <= 0) {
                    System.arraycopy(this.m_openNodes, i, this.m_openNodes, i + 1, this.m_lastOpenNodeIndex - i + 1);
                    this.m_openNodes[i] = node;
                    ++this.m_lastOpenNodeIndex;
                    return;
                }
                if (i == this.m_firstOpenNodeIndex) {
                    --this.m_firstOpenNodeIndex;
                    this.m_openNodes[this.m_firstOpenNodeIndex] = node;
                    return;
                }
                System.arraycopy(this.m_openNodes, this.m_firstOpenNodeIndex, this.m_openNodes, this.m_firstOpenNodeIndex - 1, i - this.m_firstOpenNodeIndex);
                --this.m_firstOpenNodeIndex;
                this.m_openNodes[i - 1] = node;
                return;
            }
            else {
                ++i;
            }
        }
        ++this.m_lastOpenNodeIndex;
        this.m_openNodes[this.m_lastOpenNodeIndex] = node;
    }
    
    private PathFinderNode getNode(final long cellHashCode) {
        return this.getNode(getXFrom3DHashCode(cellHashCode), getYFrom3DHashCode(cellHashCode), getZFrom3DHashCode(cellHashCode));
    }
    
    private PathFinderNode getNode(final int x, final int y, final short z) {
        final long hashCode = get3DHashCode(x, y, z);
        final short index = this.m_nodesHashToIndex.get(hashCode);
        if (index != 0) {
            return this.m_nodes[index];
        }
        if (this.m_lastNodeIndex >= PathFinder.MAX_NODES - 1) {
            PathFinder.m_logger.error((Object)"No more free nodes. Ceel can't be added to open nodes. Think about increasing PathFinder.MAX_NODES");
            return null;
        }
        final int indexAndCount = this.retrieveCellPathData(x, y);
        if (indexAndCount == 0) {
            return null;
        }
        for (int startIndex = getIndexFromIndexAndCountHashCode(indexAndCount), elementsCount = getCountFromIndexAndCountHashCode(indexAndCount), j = startIndex; j < startIndex + elementsCount; ++j) {
            if (this.m_cellPathData[j].m_z == z) {
                final PathFinderNode node = this.m_nodes[this.m_lastNodeIndex + 1];
                node.m_firstElementIndex = (short)startIndex;
                node.m_nodeElementIndex = (byte)(j - startIndex);
                node.m_cellElementsCount = (byte)elementsCount;
                node.m_parentNode = null;
                node.m_currentPathLength = 0;
                node.m_opened = false;
                node.m_f = 0.0f;
                node.m_g = Float.MAX_VALUE;
                node.m_h = 0.0f;
                ++this.m_lastNodeIndex;
                this.m_nodesHashToIndex.put(hashCode, this.m_lastNodeIndex);
                return node;
            }
        }
        return null;
    }
    
    private int retrieveCellPathData(final int x, final int y) {
        final long cellPathDataHash = get2DHashCode(x, y);
        int indexAndCount = this.m_cellPathDataHashToIndexAndCount.get(cellPathDataHash);
        if (indexAndCount != 0) {
            return indexAndCount;
        }
        final TopologyMap map = this.m_topologyMapInstanceSet.getTopologyMapFromCell(x, y);
        if (map == null) {
            return 0;
        }
        if (!map.isInMap(x, y)) {
            PathFinder.m_logger.error((Object)"Map pas pr\u00e9sente pour ces coordonn\u00e9es... Probl\u00e8me de topologyMapInstanceSet mal charg\u00e9/initialis\u00e9 ?");
            return 0;
        }
        final int cellElementsCount = map.getPathData(x, y, this.m_cellPathData, this.m_nextCellPathDataIndex);
        assert cellElementsCount != 0 : "no data for a specific cell " + x + "," + y + "@?";
        indexAndCount = getIndexAndCountHashCode(this.m_nextCellPathDataIndex, cellElementsCount);
        this.m_cellPathDataHashToIndexAndCount.put(cellPathDataHash, indexAndCount);
        this.m_nextCellPathDataIndex += (short)cellElementsCount;
        return indexAndCount;
    }
    
    private int computeResultPath(final PathFinderNode endNode) {
        assert endNode != null : "can't compute a path with a null end node";
        this.m_lastPathFoundSize = 0;
        PathFinderNode node = endNode;
        if (!this.m_parameters.m_includeEndCell && node.m_parentNode != null) {
            node = node.m_parentNode;
        }
        if (!this.m_parameters.m_includeStartCell) {
            while (node.m_parentNode != null && this.m_lastPathFoundSize < 128) {
                final CellPathData data = this.m_cellPathData[node.m_firstElementIndex + node.m_nodeElementIndex];
                final long[] lastPathFoundData = this.m_lastPathFoundData;
                final short lastPathFoundSize = this.m_lastPathFoundSize;
                this.m_lastPathFoundSize = (short)(lastPathFoundSize + 1);
                lastPathFoundData[lastPathFoundSize] = get3DHashCode(data.m_x, data.m_y, data.m_z);
                node = node.m_parentNode;
            }
            if (node.m_parentNode == null) {
                return this.m_lastPathFoundSize;
            }
        }
        else {
            while (node != null && this.m_lastPathFoundSize < 128) {
                final CellPathData data = this.m_cellPathData[node.m_firstElementIndex + node.m_nodeElementIndex];
                final long[] lastPathFoundData2 = this.m_lastPathFoundData;
                final short lastPathFoundSize2 = this.m_lastPathFoundSize;
                this.m_lastPathFoundSize = (short)(lastPathFoundSize2 + 1);
                lastPathFoundData2[lastPathFoundSize2] = get3DHashCode(data.m_x, data.m_y, data.m_z);
                node = node.m_parentNode;
            }
            if (node == null) {
                return this.m_lastPathFoundSize;
            }
        }
        return -1;
    }
    
    public boolean isStraightMovePossible(final Point3 startCell, final Point3 endCell) {
        if (startCell == null || endCell == null) {
            PathFinder.m_logger.error((Object)("IMpossible de savoir si un chemin en ligne droite existe: " + startCell + "/" + endCell));
            return false;
        }
        if (endCell.equals(startCell)) {
            return true;
        }
        if (startCell.getX() != endCell.getX() && startCell.getY() != endCell.getY()) {
            PathFinder.m_logger.info((Object)"Cellules non align\u00e9es : impossible d'avoir un chemin en ligne droite");
            return false;
        }
        final int startX = startCell.getX();
        final int endX = endCell.getX();
        final int startY = startCell.getY();
        final int endY = endCell.getY();
        int xInc;
        int yInc;
        int movesCount;
        if (startX == endX) {
            xInc = 0;
            yInc = ((endY > startY) ? 1 : -1);
            movesCount = Math.abs(endY - startY);
        }
        else {
            xInc = ((endX > startX) ? 1 : -1);
            yInc = 0;
            movesCount = Math.abs(endX - startX);
        }
        int currentX = startX;
        int currentY = startY;
        final int currentHash = this.retrieveCellPathData(currentX, currentY);
        if (currentHash == 0) {
            PathFinder.m_logger.info((Object)("IMpossible de r\u00e9cup\u00e9rer les informations de la cellule " + currentX + ", " + currentY));
            return false;
        }
        int currentDataIndex = getIndexFromIndexAndCountHashCode(currentHash);
        int currentDataCount = getCountFromIndexAndCountHashCode(currentHash);
        int currentZIndex = TopologyChecker.getIndexFromZ(currentDataIndex, currentDataCount, this.m_cellPathData, startCell.getZ());
        if (currentZIndex == -32768) {
            PathFinder.m_logger.error((Object)("Position en z non valide pour cette cellule : " + startCell));
            return false;
        }
        try {
            for (int i = 0; i < movesCount; ++i) {
                final int nextX = currentX + xInc;
                final int nextY = currentY + yInc;
                final int nextHashcode = this.retrieveCellPathData(nextX, nextY);
                if (nextHashcode == 0) {
                    PathFinder.m_logger.debug((Object)("Une cellule du mouvement en ligne droite n'existe pas : " + nextX + ", " + nextY));
                    return false;
                }
                final int nextDataIndex = getIndexFromIndexAndCountHashCode(nextHashcode);
                final int nextDataCount = getCountFromIndexAndCountHashCode(nextHashcode);
                final int validIndexes = this.m_pathChecker.getValidIndexesOnNextCell(currentZIndex + currentDataIndex, currentDataIndex, currentDataCount, this.m_cellPathData, nextDataIndex, nextDataCount, this.m_cellPathData);
                if (validIndexes == 0) {
                    PathFinder.m_logger.debug((Object)("Cellule " + nextX + ", " + nextY + " emp\u00eache le mouvement"));
                    return false;
                }
                currentX = nextX;
                currentY = nextY;
                currentDataIndex = nextDataIndex;
                currentDataCount = nextDataCount;
                currentZIndex = this.m_pathChecker.m_validIndexes[0] - currentDataIndex;
            }
        }
        catch (Throwable t) {
            PathFinder.m_logger.error((Object)"Exception pendant le check pour savoir s'il y a un chemin en ligne droite : ", t);
            return false;
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PathFinder.class);
        m_debugLogger = Logger.getLogger("debug");
        PathFinder.MAX_NODES = 1024;
        PathFinder.MAX_CELL_PATH_DATA = PathFinder.MAX_NODES * 3;
        JUMP_COST_PER_HEIGHT_UNIT = new float[] { 0.0f, 0.0f, 0.5f, 2.5f, 3.5f };
        PATH_NOT_FOUND_RESULT = PathFindResult.EMPTY;
        DIRECTIONS_DX_FIRST = new int[][] { { 1, 0, 1 }, { -1, 0, 4 }, { 0, 1, 2 }, { 0, -1, 8 }, { 1, 1, 3 }, { -1, 1, 6 }, { -1, -1, 12 }, { 1, -1, 9 } };
        DIRECTIONS_DY_FIRST = new int[][] { { 0, 1, 2 }, { 0, -1, 8 }, { 1, 0, 1 }, { -1, 0, 4 }, { 1, 1, 3 }, { -1, 1, 6 }, { -1, -1, 12 }, { 1, -1, 9 } };
        m_staticPool = new MonitoredPool(new ObjectFactory<PathFinder>() {
            @Override
            public PathFinder makeObject() {
                return new PathFinder();
            }
        });
    }
}
