package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.runningEffectIterator;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import gnu.trove.*;

public class LinkedToEffectUserIterator implements Iterator<RunningEffect>, Poolable
{
    protected static final Logger m_logger;
    private static final MonitoredPool m_iteratorPool;
    private RunningEffectManager m_runningEffectManagerCreator;
    private TLongObjectIterator<RunningEffect> m_iterator;
    private RunningEffect m_currentRunningEffect;
    private EffectUser m_effectUser;
    private ArrayList<RunningEffect> m_removedRunningEffects;
    private boolean m_iterateOnlyBetweenTargetingEffects;
    private boolean m_nextChecked;
    
    public LinkedToEffectUserIterator() {
        super();
        this.m_currentRunningEffect = null;
        this.m_removedRunningEffects = new ArrayList<RunningEffect>();
        this.m_iterateOnlyBetweenTargetingEffects = false;
        this.m_nextChecked = false;
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        for (int i = 0; i < this.m_removedRunningEffects.size(); ++i) {
            this.m_runningEffectManagerCreator.onEffectRemoved(this.m_removedRunningEffects.get(i));
        }
        this.m_removedRunningEffects.clear();
        this.m_iterator = null;
        this.m_currentRunningEffect = null;
        this.m_effectUser = null;
        this.m_runningEffectManagerCreator = null;
        this.m_iterateOnlyBetweenTargetingEffects = false;
    }
    
    public static LinkedToEffectUserIterator checkOut(final RunningEffectManager creator, final TLongObjectIterator<RunningEffect> effects, final EffectUser user, final boolean iterateOnlyBetweenTargetingEffects) {
        if (effects != null && user != null) {
            LinkedToEffectUserIterator iterator;
            try {
                iterator = (LinkedToEffectUserIterator)LinkedToEffectUserIterator.m_iteratorPool.borrowObject();
            }
            catch (Exception e) {
                iterator = new LinkedToEffectUserIterator();
                LinkedToEffectUserIterator.m_logger.error((Object)("erreur dans le checkOut de " + iterator.getClass()));
            }
            iterator.m_runningEffectManagerCreator = creator;
            iterator.m_iterator = effects;
            iterator.m_effectUser = user;
            iterator.m_iterateOnlyBetweenTargetingEffects = iterateOnlyBetweenTargetingEffects;
            return iterator;
        }
        if (effects == null) {
            throw new UnsupportedOperationException("checkOut d'un iterator sans liste derri\u00e8re");
        }
        throw new UnsupportedOperationException("checkOut d'un  LinkedToEffectUserIterator sans effectUser");
    }
    
    @Override
    public boolean hasNext() {
        this.m_nextChecked = true;
        if (!this.m_iterator.hasNext()) {
            return false;
        }
        while (this.m_iterator.hasNext()) {
            this.m_iterator.advance();
            this.m_currentRunningEffect = this.m_iterator.value();
            if ((this.m_currentRunningEffect.getCaster() == this.m_effectUser && !this.m_iterateOnlyBetweenTargetingEffects) || this.m_currentRunningEffect.getTarget() == this.m_effectUser) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public RunningEffect next() {
        if (!this.m_nextChecked && !this.hasNext()) {
            throw new UnsupportedOperationException("Depassement de liste, cause probable : appel de next() sans v\u00e9rification");
        }
        this.m_nextChecked = false;
        return this.m_currentRunningEffect;
    }
    
    @Override
    public void remove() {
        this.m_iterator.remove();
        this.m_removedRunningEffects.add(this.m_currentRunningEffect);
    }
    
    public void release() {
        if (LinkedToEffectUserIterator.m_iteratorPool != null) {
            try {
                LinkedToEffectUserIterator.m_iteratorPool.returnObject(this);
            }
            catch (Exception e) {
                LinkedToEffectUserIterator.m_logger.error((Object)"impossible");
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LinkedToEffectUserIterator.class);
        m_iteratorPool = new MonitoredPool(new ObjectFactory<LinkedToEffectUserIterator>() {
            @Override
            public LinkedToEffectUserIterator makeObject() {
                return new LinkedToEffectUserIterator();
            }
        });
    }
}
