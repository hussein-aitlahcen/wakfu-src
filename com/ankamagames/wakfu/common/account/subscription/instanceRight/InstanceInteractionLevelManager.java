package com.ankamagames.wakfu.common.account.subscription.instanceRight;

import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public final class InstanceInteractionLevelManager
{
    public static final InstanceInteractionLevelManager INSTANCE;
    private final InstanceInteractionLevelsDictionary m_instanceInteractionLevelsDictionary;
    
    private InstanceInteractionLevelManager() {
        super();
        this.m_instanceInteractionLevelsDictionary = new InstanceInteractionLevelsDictionary();
    }
    
    public InstanceInteractionLevel getInstanceInteractionLevel(final int instanceId, final SubscriptionLevel subscriptionLevel) {
        if (subscriptionLevel == SubscriptionLevel.UNKNOWN) {
            return InstanceInteractionLevel.FORBIDDEN_ACCESS;
        }
        return this.m_instanceInteractionLevelsDictionary.get(instanceId, subscriptionLevel.getId());
    }
    
    public void add(final int instanceId, final int subscriptionLevel, final InstanceInteractionLevel instanceInteractionLevel) {
        this.m_instanceInteractionLevelsDictionary.add(instanceId, subscriptionLevel, instanceInteractionLevel);
    }
    
    @Override
    public String toString() {
        return "InstanceInteractionLevelManager{m_instanceInteractionLevelsDictionary=" + this.m_instanceInteractionLevelsDictionary + '}';
    }
    
    static {
        INSTANCE = new InstanceInteractionLevelManager();
    }
    
    private static class InstanceInteractionLevelsDictionary
    {
        private final Map<IntIntPair, InstanceInteractionLevel> m_map;
        
        InstanceInteractionLevelsDictionary() {
            super();
            this.m_map = new HashMap<IntIntPair, InstanceInteractionLevel>();
        }
        
        public InstanceInteractionLevel get(final int instanceId, final int subscriptionLevel) {
            final IntIntPair key = new IntIntPair(instanceId, subscriptionLevel);
            final InstanceInteractionLevel interactionLevel = this.m_map.get(key);
            return (interactionLevel != null) ? interactionLevel : InstanceInteractionLevel.FULL_ACCESS;
        }
        
        public void add(final int instanceId, final int subscriptionLevel, final InstanceInteractionLevel level) {
            this.m_map.put(new IntIntPair(instanceId, subscriptionLevel), level);
        }
        
        @Override
        public String toString() {
            return "InstanceInteractionLevelsDictionary{m_map=" + this.m_map + '}';
        }
    }
}
