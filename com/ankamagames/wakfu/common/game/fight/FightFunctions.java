package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.los.*;

public class FightFunctions
{
    private static final Logger m_logger;
    private static final Point3 HEAD_POSITION;
    private static final Point3 HEAD_POSITION_2;
    private static final int EMPTY_TARGETED_CELL_HEIGHT_RAISE_VALUE = 6;
    public static final float EYES_HEIGHT_PERCENT = 0.8f;
    
    public static boolean hasLineOfSight(final Target checker, final AbstractFight fight, final int targetX, final int targetY, final short targetZ) {
        return hasLineOfSight(checker, fight.getFightMap(), targetX, targetY, targetZ, fight.getCharacterInfoAtPosition(targetX, targetY));
    }
    
    public static boolean hasLineOfSight(final Target checker, final FightMap fightMap, final int targetX, final int targetY, final short targetZ, final Target target) {
        if (checker == null) {
            return false;
        }
        final int startX = checker.getWorldCellX();
        final int startY = checker.getWorldCellY();
        final short startZ = checker.getWorldCellAltitude();
        short targetHeight = 6;
        if (target != null) {
            targetHeight = (short)(target.getHeight() * 0.8f);
        }
        if (checker instanceof FightObstacle) {
            fightMap.addIgnoredSightObstacle((FightObstacle)checker);
        }
        boolean res;
        try {
            res = hasLineOfSight(startX, startY, startZ, checker.getHeight(), targetX, targetY, targetZ, targetHeight, fightMap);
        }
        catch (Exception e) {
            FightFunctions.m_logger.error((Object)"Exception levee", (Throwable)e);
            res = false;
        }
        finally {
            fightMap.clearIgnoredSightObstacles();
        }
        return res;
    }
    
    public static boolean hasLineOfSight(final int startX, final int startY, final short startZ, final int startHeight, final int targetX, final int targetY, final short targetZ, final TopologyMapInstanceSet fightMap) {
        return hasLineOfSight(startX, startY, startZ, startHeight, targetX, targetY, targetZ, 6, fightMap);
    }
    
    public static boolean hasLineOfSight(final int startX, final int startY, final short startZ, final int startHeight, final int targetX, final int targetY, final short targetZ, final int targetHeight, final TopologyMapInstanceSet fightMap) {
        final short casterEyesHeight = (short)(startHeight * 0.8f);
        if (casterEyesHeight < 0) {
            return true;
        }
        FightFunctions.HEAD_POSITION.set(startX, startY, startZ);
        FightFunctions.HEAD_POSITION.setZ((short)(FightFunctions.HEAD_POSITION.getZ() + casterEyesHeight));
        FightFunctions.HEAD_POSITION_2.set(targetX, targetY, targetZ);
        FightFunctions.HEAD_POSITION_2.setZ((short)(FightFunctions.HEAD_POSITION_2.getZ() + targetHeight));
        final LineOfSightChecker losChecker = LineOfSightChecker.checkOut();
        losChecker.setTopologyMapInstanceSet(fightMap);
        losChecker.setStartPoint(FightFunctions.HEAD_POSITION.getX(), FightFunctions.HEAD_POSITION.getY(), FightFunctions.HEAD_POSITION.getZ());
        losChecker.setEndPoint(targetX, targetY, targetZ);
        final boolean footLOS = losChecker.checkLOS();
        if (footLOS) {
            losChecker.release();
            return true;
        }
        if (targetHeight == 0) {
            losChecker.release();
            return false;
        }
        losChecker.setEndPoint(FightFunctions.HEAD_POSITION_2.getX(), FightFunctions.HEAD_POSITION_2.getY(), FightFunctions.HEAD_POSITION_2.getZ());
        final boolean headLOS = losChecker.checkLOS();
        losChecker.release();
        return headLOS;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightFunctions.class);
        HEAD_POSITION = new Point3();
        HEAD_POSITION_2 = new Point3();
    }
}
