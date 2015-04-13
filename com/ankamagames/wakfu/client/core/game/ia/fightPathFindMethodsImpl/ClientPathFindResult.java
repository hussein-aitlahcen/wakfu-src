package com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl;

import com.ankamagames.framework.ai.pathfinder.*;

public final class ClientPathFindResult
{
    private final PathFindResult m_pathFindResult;
    private final boolean m_isOnRails;
    
    public ClientPathFindResult(final PathFindResult pathFindResult, final boolean isOnRails) {
        super();
        this.m_pathFindResult = pathFindResult;
        this.m_isOnRails = isOnRails;
    }
    
    public PathFindResult getPathFindResult() {
        return this.m_pathFindResult;
    }
    
    public boolean isOnRails() {
        return this.m_isOnRails;
    }
    
    public int getPathLength() {
        if (this.m_pathFindResult == null) {
            return 0;
        }
        return this.m_pathFindResult.getPathLength();
    }
}
