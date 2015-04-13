package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class CharacterTeleportAction extends TimedAction implements Releasable
{
    private static final Logger m_logger;
    private final ArrayList<CharacterInfo> m_actors;
    private final ArrayList<Point3> m_pos;
    private boolean m_trytomove;
    private static final MonitoredPool m_staticPool;
    
    public static CharacterTeleportAction checkout(final int uniqueId, final int actionType, final int actionId, final CharacterInfo actor, final Point3 pos, final boolean tryToMove) {
        final CharacterTeleportAction characterTeleportAction = checkout(uniqueId, actionType, actionId, actor, pos);
        characterTeleportAction.m_trytomove = tryToMove;
        return characterTeleportAction;
    }
    
    public static CharacterTeleportAction checkout(final int uniqueId, final int actionType, final int actionId, final CharacterInfo actor, final Point3 pos) {
        try {
            final CharacterTeleportAction characterTeleportAction = (CharacterTeleportAction)CharacterTeleportAction.m_staticPool.borrowObject();
            characterTeleportAction.setUniqueId(uniqueId);
            characterTeleportAction.setActionType(actionType);
            characterTeleportAction.setActionId(actionId);
            characterTeleportAction.m_actors.add(actor);
            characterTeleportAction.m_pos.add(pos);
            characterTeleportAction.m_trytomove = false;
            return characterTeleportAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            CharacterTeleportAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            CharacterTeleportAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_trytomove = true;
    }
    
    @Override
    public void onCheckIn() {
        this.m_actors.clear();
        this.m_pos.clear();
    }
    
    private CharacterTeleportAction() {
        super(0, 0, 0);
        this.m_actors = new ArrayList<CharacterInfo>();
        this.m_pos = new ArrayList<Point3>();
        this.m_trytomove = true;
    }
    
    @Override
    protected long onRun() {
        int i = 0;
        for (final CharacterInfo mover : this.m_actors) {
            final Point3 position = this.m_pos.get(i);
            boolean moved = false;
            if (this.m_trytomove) {
                moved = mover.getActor().moveTo(position, false, false);
            }
            if (!moved) {
                mover.teleport(position.getX(), position.getY(), position.getZ(), false);
            }
            ++i;
        }
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    public void addCharacterTeleport(final CharacterInfo actor, final Point3 pos) {
        this.m_actors.add(actor);
        this.m_pos.add(pos);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterTeleportAction.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacterTeleportAction>() {
            @Override
            public CharacterTeleportAction makeObject() {
                return new CharacterTeleportAction(null);
            }
        });
    }
}
