package com.ankamagames.wakfu.client.console.command.world;

import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class MovePlayerManager
{
    public static final MovePlayerManager INSTANCE;
    private static final TargetPositionListener<PathMobile> LISTENER;
    private final EnumSet<Direction8> m_directions;
    
    private MovePlayerManager() {
        super();
        this.m_directions = EnumSet.noneOf(Direction8.class);
    }
    
    public void move(final Direction8 direction) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player == null) {
            return;
        }
        final Direction8 finalDirection = Direction8.NORTH;
        if (!this.m_directions.remove(direction)) {
            this.m_directions.add(direction);
        }
        final CharacterActor actor = player.getActor();
        final Point3 position = new Point3(player.getPosition());
        if (!actor.containsPositionListener(MovePlayerManager.LISTENER)) {
            actor.addPositionListener(MovePlayerManager.LISTENER);
        }
        PathFindResult finalResult = null;
        for (int i = 1; i <= 5; ++i) {
            position.shift(finalDirection);
            final PathFindResult result = actor.getPathResult(position, false, true);
            if (!result.isPathFound()) {
                break;
            }
            finalResult = result;
        }
        if (finalResult != null && finalResult.isPathFound()) {
            actor.applyPathResult(finalResult, true);
        }
    }
    
    public void moveTest(final Direction8 direction) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player == null) {
            return;
        }
        final CharacterActor actor = player.getActor();
        final Point3 position = new Point3(player.getPosition());
        if (!this.m_directions.remove(direction)) {
            this.m_directions.add(direction);
        }
        if (this.m_directions.isEmpty()) {
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
            final PathFindResult result = actor.getPathResult(position, false, true);
            if (!result.isPathFound()) {
                break;
            }
            finalResult = result;
        }
        if (finalResult != null && finalResult.isPathFound()) {
            actor.applyPathResult(finalResult, true);
        }
    }
    
    static {
        INSTANCE = new MovePlayerManager();
        LISTENER = new PositionListener();
    }
    
    private static class PositionListener implements TargetPositionListener<PathMobile>
    {
        private Point3 m_position;
        
        @Override
        public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
            this.m_position = new Point3(worldX, worldY, altitude);
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            final CharacterActor actor = player.getActor();
            System.err.println(this.m_position + " : " + new Point3(actor.getWorldCellX(), actor.getWorldCellY(), actor.getWorldCellAltitude()));
        }
    }
}
