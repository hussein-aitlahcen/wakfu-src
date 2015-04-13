package com.ankamagames.wakfu.client.core.game.pet;

import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class PetFollowProcedure implements MobileStartPathListener, TargetPositionListener<PathMobile>, MobileEndPathListener
{
    private static final PathFinderParameters PATHFIND_PARAMETERS;
    private static final int MAX_PATH_LENGTH = 25;
    private static final int SEARCH_LIMIT = 400;
    private static final int MAX_DISTANCE = 300;
    private static final TopologyMapInstanceSet PATHFINDER_MAP_INSTANCE_SET;
    private final Actor m_petMobile;
    
    public PetFollowProcedure(final Actor petMobile) {
        super();
        this.m_petMobile = petMobile;
    }
    
    @Override
    public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
        final PathFindResult path = target.getCurrentPath();
        if (path != null) {
            return;
        }
        this.moveNear(target, path, worldX, worldY, altitude);
    }
    
    @Override
    public void pathStarted(final PathMobile mobile, final PathFindResult path) {
        if (path == null) {
            return;
        }
        final int[] destination = path.getLastStep();
        if (destination == null) {
            return;
        }
        final int x = destination[0];
        final int y = destination[1];
        final short z = (short)destination[2];
        this.moveNear(mobile, path, x, y, z);
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.moveNear(mobile, mobile.getCurrentPath(), x, y, altitude);
    }
    
    private void moveNear(final PathMobile target, final PathFindResult path, final int x, final int y, final short z) {
        final int petX = this.m_petMobile.getWorldCellX();
        final int petY = this.m_petMobile.getWorldCellY();
        final int dX = petX - x;
        final int dY = petY - y;
        final Point3 nextPetCell = getNextPetCell(x, y, z);
        if (dX * dX + dY * dY > 300) {
            this.m_petMobile.setWorldPosition(nextPetCell.getX(), nextPetCell.getY(), nextPetCell.getZ());
            return;
        }
        PetFollowProcedure.PATHFINDER_MAP_INSTANCE_SET.reset();
        final PathFinder pathFinder = PathFinder.checkOut();
        PetFollowProcedure.PATHFIND_PARAMETERS.m_limitTo4Directions = false;
        PetFollowProcedure.PATHFIND_PARAMETERS.m_allowGap = false;
        pathFinder.setParameters(PetFollowProcedure.PATHFIND_PARAMETERS);
        pathFinder.setMoverCaracteristics(this.m_petMobile.getHeight(), this.m_petMobile.getPhysicalRadius(), this.m_petMobile.getJumpCapacity());
        pathFinder.setTopologyMapInstanceSet(PetFollowProcedure.PATHFINDER_MAP_INSTANCE_SET);
        pathFinder.addStartCell(this.m_petMobile.getWorldCellX(), this.m_petMobile.getWorldCellY(), this.m_petMobile.getWorldCellAltitude());
        TopologyMapManager.getTopologyMapInstances(petX, petY, nextPetCell.getX(), nextPetCell.getY(), PetFollowProcedure.PATHFINDER_MAP_INSTANCE_SET);
        pathFinder.setStopCell(nextPetCell.getX(), nextPetCell.getY(), nextPetCell.getZ());
        pathFinder.findPath();
        final PathFindResult petPath = pathFinder.getPathResult();
        pathFinder.release();
        if (petPath.isPathFound()) {
            ProcessScheduler.getInstance().schedule(new PetPathRunnable(petPath, this.m_petMobile), this.m_petMobile.getMovementStyle().getCellSpeed(this.m_petMobile) / 2, 1);
        }
        else if (path != null) {
            target.addEndPositionListener(this);
        }
        else {
            this.m_petMobile.setWorldPosition(nextPetCell.getX(), nextPetCell.getY(), nextPetCell.getZ());
        }
    }
    
    public static Point3 getNextPetCell(final int playerX, final int playerY, final short playerZ) {
        final Direction8[] direction4 = Direction8.getDirection4Values();
        Collections.shuffle(Arrays.asList(direction4));
        int petX = playerX;
        int petY = playerY;
        short petZ = playerZ;
        boolean nOk = true;
        for (int i = 0, size = direction4.length; i < size && nOk; ++i) {
            final Direction8 direction2 = direction4[i];
            petX = playerX + direction2.m_x;
            petY = playerY + direction2.m_y;
            if (TopologyMapManager.getMapFromCell(petX, petY) != null) {
                petZ = TopologyMapManager.getNearestWalkableZ(petX, petY, playerZ);
                if (petZ != -32768 && Math.abs(playerZ - petZ) <= 6 && !TopologyMapManager.isGap(petX, petY)) {
                    nOk = false;
                }
            }
        }
        if (nOk) {
            petX = playerX;
            petY = playerY;
            petZ = playerZ;
        }
        return new Point3(petX, petY, petZ);
    }
    
    static {
        PATHFIND_PARAMETERS = new PathFinderParameters();
        PetFollowProcedure.PATHFIND_PARAMETERS.m_limitTo4Directions = true;
        PetFollowProcedure.PATHFIND_PARAMETERS.m_maxPathLength = 25;
        PetFollowProcedure.PATHFIND_PARAMETERS.m_searchLimit = 400;
        PetFollowProcedure.PATHFIND_PARAMETERS.m_stopBeforeEndCell = false;
        PetFollowProcedure.PATHFIND_PARAMETERS.m_punishDirectionChangeIn4D = true;
        PATHFINDER_MAP_INSTANCE_SET = new TopologyMapInstanceSet();
    }
    
    private static class PetPathRunnable implements Runnable
    {
        private final PathFindResult m_petPath;
        private final Actor m_petMobile;
        
        PetPathRunnable(final PathFindResult petPath, final Actor petMobile) {
            super();
            this.m_petPath = petPath;
            this.m_petMobile = petMobile;
        }
        
        @Override
        public void run() {
            this.m_petMobile.applyPathResult(this.m_petPath, false);
        }
        
        @Override
        public String toString() {
            return "PetPathRunnable{m_petPath=" + this.m_petPath + '}';
        }
    }
}
