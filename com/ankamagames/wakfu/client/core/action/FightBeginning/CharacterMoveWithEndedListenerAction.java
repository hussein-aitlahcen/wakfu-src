package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.wakfu.client.core.action.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class CharacterMoveWithEndedListenerAction extends AbstractPositionEndedAction implements Releasable
{
    private static final Logger m_logger;
    private ArrayList<CharacterActor> m_actors;
    private ArrayList<Direction8Path> m_paths;
    private ArrayList<Boolean> m_finished;
    private Object m_mutex;
    private static final MonitoredPool m_staticPool;
    
    public static CharacterMoveWithEndedListenerAction checkout(final int uniqueId, final int actionType, final int actionId, final CharacterActor actor, final Direction8Path path) {
        try {
            final CharacterMoveWithEndedListenerAction characterMoveWithEndedListenerAction = (CharacterMoveWithEndedListenerAction)CharacterMoveWithEndedListenerAction.m_staticPool.borrowObject();
            characterMoveWithEndedListenerAction.setUniqueId(uniqueId);
            characterMoveWithEndedListenerAction.setActionType(actionType);
            characterMoveWithEndedListenerAction.setActionId(actionId);
            if (path.steps() > 0) {
                characterMoveWithEndedListenerAction.m_actors.add(actor);
                characterMoveWithEndedListenerAction.m_paths.add(path);
                characterMoveWithEndedListenerAction.m_finished.add(false);
            }
            return characterMoveWithEndedListenerAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            CharacterMoveWithEndedListenerAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            CharacterMoveWithEndedListenerAction.m_logger.error((Object)("Exception dans le release de " + this.getClass() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_mutex = new Object();
    }
    
    @Override
    public void onCheckIn() {
        this.m_actors.clear();
        this.m_paths.clear();
        this.m_finished.clear();
        this.m_mutex = null;
    }
    
    private CharacterMoveWithEndedListenerAction() {
        super(0, 0, 0);
        this.m_actors = new ArrayList<CharacterActor>();
        this.m_paths = new ArrayList<Direction8Path>();
        this.m_finished = new ArrayList<Boolean>();
    }
    
    @Override
    protected long onRun() {
        for (int i = this.m_actors.size() - 1; i >= 0; --i) {
            final CharacterActor actor = this.m_actors.get(i);
            CharacterMoveWithEndedListenerAction.m_logger.info((Object)("onRun :" + actor.getId()));
            actor.addEndPositionListener(this);
            actor.updateActorPath(this.m_paths.get(i));
        }
        return 7000L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        synchronized (this.m_mutex) {
            mobile.removeEndPositionListener(this);
            boolean finished = true;
            for (int i = this.m_actors.size() - 1; i >= 0; --i) {
                if (this.m_actors.get(i) == mobile) {
                    CharacterMoveWithEndedListenerAction.m_logger.info((Object)("pathEnded :" + this.m_actors.get(i).getId()));
                    this.m_finished.remove(i);
                    this.m_finished.add(i, true);
                }
                if (!this.m_finished.get(i)) {
                    finished = false;
                    CharacterMoveWithEndedListenerAction.m_logger.info((Object)("all finish :" + this.m_actors.get(i).getId()));
                }
            }
            if (finished) {
                MessageScheduler.getInstance().removeClock(this.m_clockId);
                this.fireActionFinishedEvent();
            }
        }
    }
    
    public void addCharacterMovement(final CharacterActor actor, final Direction8Path path) {
        if (path.steps() > 0) {
            this.m_actors.add(actor);
            this.m_paths.add(path);
            this.m_finished.add(false);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterMoveWithEndedListenerAction.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacterMoveWithEndedListenerAction>() {
            @Override
            public CharacterMoveWithEndedListenerAction makeObject() {
                return new CharacterMoveWithEndedListenerAction(null);
            }
        });
    }
}
