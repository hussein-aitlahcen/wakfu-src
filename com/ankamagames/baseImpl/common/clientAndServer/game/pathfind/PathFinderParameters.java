package com.ankamagames.baseImpl.common.clientAndServer.game.pathfind;

public class PathFinderParameters
{
    public boolean m_limitTo4Directions;
    public int m_searchLimit;
    public int m_maxPathLength;
    public boolean m_includeStartCell;
    public boolean m_includeEndCell;
    public boolean m_stopBeforeEndCell;
    public boolean m_checkValidityIfStopBeforeEndCell;
    public boolean m_punishDirectionChangeIn4D;
    public boolean m_permissiveStartCellAltitude;
    public boolean m_punishJump;
    public boolean m_reduceGreaterAxisFirst;
    public boolean m_allowMoboSterile;
    public boolean m_allowGap;
    
    public PathFinderParameters() {
        super();
        this.m_limitTo4Directions = true;
        this.m_maxPathLength = -1;
        this.m_includeStartCell = true;
        this.m_includeEndCell = true;
        this.m_stopBeforeEndCell = false;
        this.m_checkValidityIfStopBeforeEndCell = true;
        this.m_punishDirectionChangeIn4D = false;
        this.m_permissiveStartCellAltitude = false;
        this.m_punishJump = false;
        this.m_reduceGreaterAxisFirst = false;
        this.m_allowMoboSterile = true;
        this.m_allowGap = false;
    }
    
    public PathFinderParameters(final boolean limitTo4Directions, final int searchLimit, final int maxPathLength, final boolean includeStartCell, final boolean includeEndCell, final boolean stopBeforeEndCell, final boolean checkValidityIfStopBeforeEndCell, final boolean punishDirectionChangeIn4D, final boolean permissiveStartCellAltitude, final boolean punishJump, final boolean reduceGreaterAxisFirst) {
        super();
        this.m_limitTo4Directions = true;
        this.m_maxPathLength = -1;
        this.m_includeStartCell = true;
        this.m_includeEndCell = true;
        this.m_stopBeforeEndCell = false;
        this.m_checkValidityIfStopBeforeEndCell = true;
        this.m_punishDirectionChangeIn4D = false;
        this.m_permissiveStartCellAltitude = false;
        this.m_punishJump = false;
        this.m_reduceGreaterAxisFirst = false;
        this.m_allowMoboSterile = true;
        this.m_allowGap = false;
        this.m_limitTo4Directions = limitTo4Directions;
        this.m_searchLimit = searchLimit;
        this.m_maxPathLength = maxPathLength;
        this.m_includeStartCell = includeStartCell;
        this.m_includeEndCell = includeEndCell;
        this.m_stopBeforeEndCell = stopBeforeEndCell;
        this.m_checkValidityIfStopBeforeEndCell = checkValidityIfStopBeforeEndCell;
        this.m_punishDirectionChangeIn4D = punishDirectionChangeIn4D;
        this.m_permissiveStartCellAltitude = permissiveStartCellAltitude;
        this.m_punishJump = punishJump;
        this.m_reduceGreaterAxisFirst = reduceGreaterAxisFirst;
    }
}
