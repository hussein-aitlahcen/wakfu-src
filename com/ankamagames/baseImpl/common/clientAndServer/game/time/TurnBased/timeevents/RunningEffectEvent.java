package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public abstract class RunningEffectEvent extends DelayableTimeEvent
{
    private static final int EFFECT_CLEARED_ID = -1;
    protected RunningEffect m_runningEffect;
    private static final Logger m_logger;
    private long m_runningEffectId;
    private long m_targetId;
    
    public RunningEffect getRunningEffect() {
        if (this.m_runningEffect != null && this.m_runningEffect.getUniqueId() != this.m_runningEffectId) {
            RunningEffectEvent.m_logger.error((Object)("Le running effect n'est plus le m\u00eame qu'a la creation de l'event " + this.m_runningEffectId));
            return null;
        }
        return this.m_runningEffect;
    }
    
    @Override
    public boolean isValid() {
        return this.m_runningEffect != null && this.m_runningEffect.getUniqueId() == this.m_runningEffectId;
    }
    
    protected void setRunningEffect(final RunningEffect re) {
        this.m_runningEffect = re;
        this.m_runningEffectId = re.getUniqueId();
        this.m_runningEffect.setLinkedEvent(this);
    }
    
    protected void setTargetId(final long targetId) {
        this.m_targetId = targetId;
    }
    
    protected void initialize(final RunningEffect re, final long targetId) {
        this.setRunningEffect(re);
        this.m_targetId = targetId;
    }
    
    @Override
    public long getFighterToAttachToById() {
        final long defaultId = this.m_targetId;
        if (this.m_runningEffect == null) {
            return defaultId;
        }
        final Effect genericEffect = this.m_runningEffect.getGenericEffect();
        if (genericEffect == null) {
            return defaultId;
        }
        if (genericEffect.mustStoreOnCaster() && this.m_runningEffect.getCaster() != null) {
            return this.m_runningEffect.getCaster().getId();
        }
        return defaultId;
    }
    
    @Override
    protected int specializedSerializedSize() {
        return 16;
    }
    
    @Override
    protected void specializedSerialize(final ByteBuffer buffer) {
        if (this.m_runningEffect == null) {
            buffer.putLong(-1L);
        }
        else {
            buffer.putLong(this.m_runningEffectId);
        }
        buffer.putLong(this.m_targetId);
    }
    
    @Override
    protected void specializedRead(final TimelineUnmarshallingContext ctx, final ByteBuffer buffer) {
        final long reUid = buffer.getLong();
        this.m_targetId = buffer.getLong();
        if (reUid == -1L) {
            return;
        }
        final RunningEffect re = ctx.getRunningEffect(reUid);
        if (re == null) {
            RunningEffectEvent.m_logger.warn((Object)("D\u00e9s\u00e9rialisation de timeline : on ne trouve pas le RunningEffect d'UID " + reUid));
            return;
        }
        this.setRunningEffect(re);
    }
    
    public void setRunningEffectToNull() {
        this.m_runningEffect = null;
        this.notifyBeingObsolete();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' + "m_runningEffectId=" + this.m_runningEffectId + ", m_targetId=" + this.m_targetId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)RunningEffectEvent.class);
    }
}
