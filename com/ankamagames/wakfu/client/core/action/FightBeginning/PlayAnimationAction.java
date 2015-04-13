package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PlayAnimationAction extends TimedAction implements Releasable
{
    private CharacterInfo m_target;
    private String m_animation;
    private long m_time;
    private static final MonitoredPool m_staticPool;
    
    public static PlayAnimationAction checkout(final int uniqueId, final int actionType, final int actionId, final CharacterInfo character, final String animation, final int time) {
        try {
            final PlayAnimationAction playAnimationAction = (PlayAnimationAction)PlayAnimationAction.m_staticPool.borrowObject();
            playAnimationAction.setUniqueId(uniqueId);
            playAnimationAction.setActionType(actionType);
            playAnimationAction.setActionId(actionId);
            playAnimationAction.setTarget(character);
            playAnimationAction.setAnimation(animation);
            playAnimationAction.setTime(time);
            return playAnimationAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            PlayAnimationAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            PlayAnimationAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_target = null;
        this.m_animation = null;
        this.m_time = 0L;
    }
    
    private PlayAnimationAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        this.m_target.getActor().setAnimation(this.m_animation);
        return this.m_time;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    public CharacterInfo getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final CharacterInfo target) {
        this.m_target = target;
    }
    
    public String getAnimation() {
        return this.m_animation;
    }
    
    public void setAnimation(final String animation) {
        this.m_animation = animation;
    }
    
    public long getTime() {
        return this.m_time;
    }
    
    public void setTime(final long time) {
        this.m_time = time;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PlayAnimationAction>() {
            @Override
            public PlayAnimationAction makeObject() {
                return new PlayAnimationAction(null);
            }
        });
    }
}
