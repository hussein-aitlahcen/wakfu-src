package com.ankamagames.wakfu.client.core.game.achievements;

import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;

public class AchievementContextManager
{
    public static final AchievementContextManager INSTANCE;
    private final TLongObjectHashMap<ClientAchievementsContext> m_contexts;
    
    private AchievementContextManager() {
        super();
        this.m_contexts = new TLongObjectHashMap<ClientAchievementsContext>();
    }
    
    public void registerContext(final long id, final ClientAchievementsContext context) {
        this.m_contexts.put(id, context);
    }
    
    public void unregisterContext(final long id) {
        this.m_contexts.remove(id);
    }
    
    public ClientAchievementsContext getContext(final long id) {
        return this.m_contexts.get(id);
    }
    
    static {
        INSTANCE = new AchievementContextManager();
    }
}
