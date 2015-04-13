package com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;

public final class PathComputationResult
{
    private boolean m_blocked;
    private final List<Point3> m_path;
    private int m_fallHeight;
    private int m_nbCellsCovered;
    private FightObstacle m_obstacle;
    
    public PathComputationResult() {
        super();
        this.m_blocked = false;
        this.m_path = new ArrayList<Point3>();
        this.m_fallHeight = 0;
        this.m_nbCellsCovered = 0;
        this.m_obstacle = null;
    }
    
    public void setBlocked(final boolean blockedByObstacle) {
        this.m_blocked = blockedByObstacle;
    }
    
    public boolean isBlocked() {
        return this.m_blocked;
    }
    
    public Point3 getArrivalCell() {
        if (this.m_path.isEmpty()) {
            return null;
        }
        return this.m_path.get(this.m_path.size() - 1);
    }
    
    public List<Point3> getPath() {
        return this.m_path;
    }
    
    public void addNextCell(final int x, final int y, final short z) {
        this.m_path.add(new Point3(x, y, z));
    }
    
    public void setFallHeight(final int fallHeight) {
        this.m_fallHeight = fallHeight;
    }
    
    public int getFallHeight() {
        return this.m_fallHeight;
    }
    
    public void setNbCellsCovered(final int nbCellsCovered) {
        this.m_nbCellsCovered = nbCellsCovered;
    }
    
    public int getNbCellsCovered() {
        return this.m_nbCellsCovered;
    }
    
    public void setObstacle(final FightObstacle obstacle) {
        this.m_obstacle = obstacle;
    }
    
    public FightObstacle getObstacle() {
        return this.m_obstacle;
    }
}
