package com.ankamagames.baseImpl.common.clientAndServer.game.pathfind;

import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;

public class PathChecker
{
    private int m_moverHeight;
    private int m_moverJumpCapacity;
    private byte m_moverPhysicalRadius;
    public final int[] m_validIndexes;
    
    public PathChecker() {
        super();
        this.m_moverHeight = 0;
        this.m_moverJumpCapacity = -1;
        this.m_moverPhysicalRadius = -1;
        this.m_validIndexes = new int[32];
    }
    
    public PathChecker(final int moverHeight, final byte moverPhysicalRadius, final int moverJumpCapacity) {
        super();
        this.m_moverHeight = 0;
        this.m_moverJumpCapacity = -1;
        this.m_moverPhysicalRadius = -1;
        this.m_validIndexes = new int[32];
        this.m_moverPhysicalRadius = moverPhysicalRadius;
        this.m_moverHeight = moverHeight;
        this.m_moverJumpCapacity = moverJumpCapacity;
    }
    
    public final int getMoverHeight() {
        return this.m_moverHeight;
    }
    
    public final void setMoverCaracteristics(final int moverHeight, final byte moverPhysicalRadius, final int moverJumpCapacity) {
        this.m_moverHeight = moverHeight;
        this.m_moverJumpCapacity = moverJumpCapacity;
        this.m_moverPhysicalRadius = moverPhysicalRadius;
    }
    
    public final int getMoverJumpCapacity() {
        return this.m_moverJumpCapacity;
    }
    
    public byte getMoverPhysicalRadius() {
        return this.m_moverPhysicalRadius;
    }
    
    public final int getValidIndexesOnNextCell(final int startCellIndex, final int startCellFirstDataIndex, final int startCellDataCount, final CellPathData[] startCellData, final int nextCellFirstDataIndex, final int nextCellDataCount, final CellPathData[] nextCellData) {
        assert this.m_moverHeight > 0 : "no moverHeight defined";
        assert this.m_moverJumpCapacity >= 0 : "no jump capacity defined";
        assert startCellData != null && nextCellData != null : "startCellData and nextCellData can't be null";
        assert startCellFirstDataIndex >= 0 && startCellDataCount > 0 && startCellFirstDataIndex + startCellDataCount < startCellData.length : "startCell indexes out of bounds";
        assert nextCellFirstDataIndex >= 0 && nextCellDataCount > 0 && nextCellFirstDataIndex + nextCellDataCount < nextCellData.length : "nextCell indexes out of bounds";
        assert startCellIndex >= startCellFirstDataIndex && startCellIndex < startCellFirstDataIndex + startCellDataCount : "startCellIndex out of bounds";
        assert TopologyChecker.checkHeightIndexValidity(startCellIndex, startCellFirstDataIndex, startCellDataCount, startCellData, this.m_moverHeight) : "incoming position is not valid : " + startCellData[startCellIndex].m_x + "," + startCellData[startCellIndex].m_y + ", " + startCellData[startCellIndex].m_z;
        final CellPathData startCell = startCellData[startCellIndex];
        if (nextCellDataCount != 1) {
            int indexesFound = 0;
        Label_0727:
            for (int i = nextCellFirstDataIndex; i < nextCellFirstDataIndex + nextCellDataCount; ++i) {
                final CellPathData element = nextCellData[i];
                if (element.m_cost != -1) {
                    if (!element.m_hollow) {
                        final int heightDiff = element.m_z - startCell.m_z;
                        if (((heightDiff < 0) ? (-heightDiff) : heightDiff) <= this.m_moverJumpCapacity) {
                            if (validateMurfinMovement(startCell, element)) {
                                final int moverHeadZ = element.m_z + this.m_moverHeight;
                                final int maxMoverHeadZDuringMovement = Math.max(moverHeadZ, startCell.m_z + this.m_moverHeight);
                            Label_0715:
                                for (int j = i + 1; j < nextCellFirstDataIndex + nextCellDataCount; ++j) {
                                    final CellPathData aboveElement = nextCellData[j];
                                    final int elementBottomZ = aboveElement.m_z - aboveElement.m_height;
                                    if (elementBottomZ >= maxMoverHeadZDuringMovement) {
                                        if (heightDiff <= 0) {
                                            break;
                                        }
                                        if (startCellIndex == startCellFirstDataIndex + startCellDataCount - 1) {
                                            break;
                                        }
                                        for (int k = startCellIndex + 1; k < startCellFirstDataIndex + startCellDataCount; ++k) {
                                            final CellPathData possibleWallElement = startCellData[k];
                                            if (possibleWallElement.m_z - possibleWallElement.m_height >= moverHeadZ) {
                                                break Label_0715;
                                            }
                                            if (!element.m_hollow) {
                                                continue Label_0727;
                                            }
                                        }
                                    }
                                    if (!aboveElement.m_hollow) {
                                        continue Label_0727;
                                    }
                                }
                                this.m_validIndexes[indexesFound++] = i;
                            }
                        }
                    }
                }
            }
            return indexesFound;
        }
        final CellPathData nextCell = nextCellData[nextCellFirstDataIndex];
        if (nextCell.m_cost == -1 || nextCell.m_hollow) {
            return 0;
        }
        final int heightDiff2 = nextCell.m_z - startCell.m_z;
        if (((heightDiff2 < 0) ? (-heightDiff2) : heightDiff2) > this.m_moverJumpCapacity) {
            return 0;
        }
        if (!validateMurfinMovement(startCell, nextCell)) {
            return 0;
        }
        if (heightDiff2 <= 0) {
            this.m_validIndexes[0] = nextCellFirstDataIndex;
            return 1;
        }
        if (startCellIndex == startCellFirstDataIndex + startCellDataCount - 1) {
            this.m_validIndexes[0] = nextCellFirstDataIndex;
            return 1;
        }
        final int maxReachedZ = nextCell.m_z + this.m_moverHeight;
        for (int l = startCellIndex + 1; l < startCellFirstDataIndex + startCellDataCount; ++l) {
            final CellPathData element2 = startCellData[l];
            if (element2.m_z - element2.m_height >= maxReachedZ) {
                this.m_validIndexes[0] = nextCellFirstDataIndex;
                return 1;
            }
            if (!element2.m_hollow) {
                return 0;
            }
        }
        this.m_validIndexes[0] = nextCellFirstDataIndex;
        return 1;
    }
    
    public final boolean checkMoveOnNextCellValidity(final int startCellIndex, final int startCellFirstDataIndex, final int startCellDataCount, final CellPathData[] startCellData, final int nextCellIndex, final int nextCellFirstDataIndex, final int nextCellDataCount, final CellPathData[] nextCellData) {
        final int numIndices = this.getValidIndexesOnNextCell(startCellIndex, startCellFirstDataIndex, startCellDataCount, startCellData, nextCellFirstDataIndex, nextCellDataCount, nextCellData);
        if (numIndices == 0) {
            return false;
        }
        for (int i = 0; i < numIndices; ++i) {
            if (this.m_validIndexes[i] == nextCellIndex) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean validateMurfinMovement(final CellPathData cellFrom, final CellPathData cellTo) {
        assert cellFrom != null : "Can't check murfin movement validity for null cell";
        assert cellTo != null : "Can't check murfin movement validity for null cell";
        final int fromInfo = cellFrom.getMurfinInfo();
        final int toInfo = cellTo.getMurfinInfo();
        if (fromInfo == toInfo) {
            return true;
        }
        if (!transitionValid(cellFrom.getTransition(), cellTo.getTransition())) {
            return false;
        }
        if (cellFrom.getGroupInfo() == cellTo.getGroupInfo()) {
            return true;
        }
        final int fromType = cellFrom.getMurfinType();
        final int toType = cellTo.getMurfinType();
        switch (fromType) {
            case 0: {
                return toType == 0 || toType == 64;
            }
            case 128: {
                return cellFrom.getGroupId() == cellTo.getGroupId() && (toType == 128 || toType == 192);
            }
            case 64: {
                return toType == 0 || toType == 64 || (toType == 192 && cellFrom.getGroupId() == cellTo.getGroupId());
            }
            case 192: {
                return cellFrom.getGroupId() == cellTo.getGroupId() && (toType == 64 || toType == 128 || toType == 192);
            }
            default: {
                assert false : "Type de Cellule non connu poru valider un murfin : " + fromType;
                return false;
            }
        }
    }
    
    private static boolean transitionValid(final int fromTransType, final int toTransType) {
        return fromTransType == toTransType || (fromTransType | toTransType) == 0x30;
    }
}
