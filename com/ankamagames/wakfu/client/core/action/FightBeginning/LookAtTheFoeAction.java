package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class LookAtTheFoeAction extends TimedAction implements Releasable
{
    private Iterable<CharacterInfo> m_fighters;
    private CharacterInfo m_target;
    private CharacterInfo m_instigator;
    private static final MonitoredPool m_staticPool;
    
    public static LookAtTheFoeAction checkout(final int uniqueId, final int actionType, final int actionId, final Iterable<CharacterInfo> fighters, final CharacterInfo target) {
        try {
            final LookAtTheFoeAction lookAtTheFoeAction = (LookAtTheFoeAction)LookAtTheFoeAction.m_staticPool.borrowObject();
            lookAtTheFoeAction.setUniqueId(uniqueId);
            lookAtTheFoeAction.setActionType(actionType);
            lookAtTheFoeAction.setActionId(actionId);
            lookAtTheFoeAction.m_fighters = fighters;
            lookAtTheFoeAction.m_target = target;
            return lookAtTheFoeAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    public static LookAtTheFoeAction checkout(final int uniqueId, final int actionType, final int actionId, final CharacterInfo instigator, final CharacterInfo target) {
        try {
            final LookAtTheFoeAction lookAtTheFoeAction = (LookAtTheFoeAction)LookAtTheFoeAction.m_staticPool.borrowObject();
            lookAtTheFoeAction.setUniqueId(uniqueId);
            lookAtTheFoeAction.setActionType(actionType);
            lookAtTheFoeAction.setActionId(actionId);
            lookAtTheFoeAction.m_instigator = instigator;
            lookAtTheFoeAction.m_target = target;
            return lookAtTheFoeAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            LookAtTheFoeAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            LookAtTheFoeAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_fighters = null;
        this.m_target = null;
        this.m_instigator = null;
    }
    
    private LookAtTheFoeAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        final Point3 targetPosition = this.m_target.getPosition();
        if (this.m_fighters != null) {
            for (final CharacterInfo fighter : this.m_fighters) {
                setDirectionFromPosition(targetPosition, fighter);
            }
        }
        if (this.m_instigator != null) {
            setDirectionFromPosition(targetPosition, this.m_instigator);
        }
        return 100L;
    }
    
    private static void setDirectionFromPosition(final Point3 targetPosition, final CharacterInfo character) {
        final Point3 position = character.getPosition();
        character.setDirection(Vector3.getDirection4FromVector(targetPosition.getX() - position.getX(), targetPosition.getY() - position.getY()));
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<LookAtTheFoeAction>() {
            @Override
            public LookAtTheFoeAction makeObject() {
                return new LookAtTheFoeAction(null);
            }
        });
    }
}
