package com.ankamagames.wakfu.client.console.command.world;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import java.util.*;

public class MovePlayer implements Command
{
    private final EnumSet<Direction8> m_directions;
    private final Runnable m_scheduler;
    private ElementSelection m_elementSelection;
    private static final boolean DEBUG = false;
    
    public MovePlayer() {
        super();
        this.m_directions = EnumSet.noneOf(Direction8.class);
        this.m_scheduler = new MovePlayerRunnable();
        this.m_elementSelection = new ElementSelection("Bla", new float[] { 0.0f, 1.0f, 0.0f, 0.6f });
    }
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final Integer directionIdx = Integer.valueOf(args.get(2));
        final Direction8 direction = Direction8.getDirectionFromIndex(directionIdx);
        this.movePlayer(direction);
    }
    
    public void movePlayer(final Direction8 direction) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player == null) {
            return;
        }
        if (player.getCurrentFight() != null) {
            return;
        }
        final CharacterActor actor = player.getActor();
        final Point3 position = new Point3(actor.getWorldCoordinates());
        if (this.m_directions.isEmpty()) {
            ProcessScheduler.getInstance().schedule(this.m_scheduler, 1000L);
        }
        if (!this.m_directions.remove(direction)) {
            this.m_directions.add(direction);
        }
        if (this.m_directions.isEmpty()) {
            position.shift(actor.getDirection());
            final PathFindResult pathResult = actor.getPathResult(position, false, true);
            actor.applyPathResult(pathResult, true);
            ProcessScheduler.getInstance().remove(this.m_scheduler);
            return;
        }
        final Vector2 vector = new Vector2();
        for (final Direction8 dir : this.m_directions) {
            vector.setAdd(new Vector2(dir.m_x, dir.m_y));
        }
        final Direction8 finalDirection = Direction8.getDirectionFromVector(MathHelper.clamp((int)vector.m_x, -1, 1), MathHelper.clamp((int)vector.m_y, -1, 1));
        if (finalDirection == null) {
            return;
        }
        PathFindResult finalResult = null;
        for (int i = 1; i <= 5; ++i) {
            position.shift(finalDirection);
            if (TopologyMapManager.isGap(position.getX(), position.getY())) {
                position.shift(finalDirection);
            }
            this.setNearestWalkableZ(player, position);
            final PathFindResult result = actor.getPathResult(position, false, true);
            if (!result.isPathFound()) {
                break;
            }
            finalResult = result;
        }
        if (actor.applyPathResult(finalResult, true)) {
            PvpInteractionManager.INSTANCE.cancelInteraction();
        }
    }
    
    private void setNearestWalkableZ(final LocalPlayerCharacter localPlayer, final Point3 target) {
        TopologyMapManager.setMoverCaracteristics(localPlayer.getHeight(), localPlayer.getPhysicalRadius(), localPlayer.getJumpCapacity());
        final short nearestWalkableZ = TopologyMapManager.getPossibleNearestWalkableZ(target.getX(), target.getY(), target.getZ());
        if (nearestWalkableZ == -32768 || Math.abs(target.getZ() - nearestWalkableZ) > 4) {
            return;
        }
        target.setZ(nearestWalkableZ);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    private class MovePlayerRunnable implements Runnable
    {
        @Override
        public void run() {
            MovePlayer.this.movePlayer(Direction8.NONE);
        }
    }
}
