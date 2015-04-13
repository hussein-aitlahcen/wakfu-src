package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class EffectExecutionResult implements Poolable
{
    private static final Logger m_logger;
    private final BitSet m_executedTriggers;
    private final HashSet<EffectUser> m_targettedEffectUsers;
    private int m_executionCount;
    private static final MonitoredPool m_staticPool;
    
    public EffectExecutionResult() {
        super();
        this.m_executedTriggers = new BitSet();
        this.m_targettedEffectUsers = new HashSet<EffectUser>();
        this.m_executionCount = 0;
    }
    
    public static EffectExecutionResult checkOut() {
        EffectExecutionResult executionResult;
        try {
            executionResult = (EffectExecutionResult)EffectExecutionResult.m_staticPool.borrowObject();
        }
        catch (Exception e) {
            executionResult = new EffectExecutionResult();
            EffectExecutionResult.m_logger.error((Object)("Erreur de checkout : " + e.getMessage()));
        }
        return executionResult;
    }
    
    public void addExecutedTriggers(final BitSet executedTriggers) {
        this.m_executedTriggers.or(executedTriggers);
    }
    
    public void addTargettedEffectUsers(final Collection<EffectUser> TargettedEffectUsers) {
        this.m_targettedEffectUsers.addAll((Collection<?>)TargettedEffectUsers);
    }
    
    public void addTargettedEffectUser(final EffectUser targettedEffectUser) {
        this.m_targettedEffectUsers.add(targettedEffectUser);
    }
    
    public void addOneExecution() {
        ++this.m_executionCount;
    }
    
    public BitSet getExecutedTriggers() {
        return this.m_executedTriggers;
    }
    
    public HashSet<EffectUser> getTargettedEffectUsers() {
        return this.m_targettedEffectUsers;
    }
    
    public void merge(final EffectExecutionResult resultToMerge) {
        if (resultToMerge == null) {
            return;
        }
        this.addExecutedTriggers(resultToMerge.getExecutedTriggers());
        this.addTargettedEffectUsers(resultToMerge.getTargettedEffectUsers());
        this.m_executionCount += resultToMerge.getExecutionCount();
    }
    
    public int getExecutionCount() {
        return this.m_executionCount;
    }
    
    public void clear() {
        this.m_executionCount = 0;
        this.m_targettedEffectUsers.clear();
        this.m_executedTriggers.clear();
    }
    
    public void release() {
        if (EffectExecutionResult.m_staticPool != null) {
            try {
                EffectExecutionResult.m_staticPool.returnObject(this);
            }
            catch (Exception e) {
                EffectExecutionResult.m_logger.error((Object)("Impossible de retourner l'\u00e9v\u00e9nement " + this + " au pool"), (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_executionCount = 0;
    }
    
    @Override
    public void onCheckIn() {
        this.m_executedTriggers.clear();
        this.m_targettedEffectUsers.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectExecutionResult.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<EffectExecutionResult>() {
            @Override
            public EffectExecutionResult makeObject() {
                return new EffectExecutionResult();
            }
        });
    }
}
