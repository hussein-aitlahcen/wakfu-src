package com.ankamagames.wakfu.common.game.hero;

import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.*;

public class HeroesLeaderManager
{
    public static final HeroesLeaderManager INSTANCE;
    private final TLongLongHashMap m_leaders;
    
    public HeroesLeaderManager() {
        super();
        this.m_leaders = new TLongLongHashMap();
    }
    
    public void putLeader(final long ownerId, final long characterId) {
        this.m_leaders.put(ownerId, characterId);
    }
    
    public boolean containsClient(final long ownerId) {
        return this.m_leaders.containsKey(ownerId);
    }
    
    public long getLeader(final long ownerId) {
        return this.m_leaders.get(ownerId);
    }
    
    public boolean isLeader(final BasicCharacterInfo info) {
        return !this.isHero(info);
    }
    
    private boolean isHero(final BasicCharacterInfo info) {
        return this.isHero(info.getOwnerId(), info.getId());
    }
    
    public boolean isHero(final long ownerId, final long characterId) {
        return this.m_leaders.containsKey(ownerId) && this.getLeader(ownerId) != characterId;
    }
    
    public boolean isHeroOfClient(final long ownerId, final BasicCharacterInfo characterInfo) {
        return this.m_leaders.containsKey(ownerId) && this.getLeader(ownerId) != characterInfo.getId() && characterInfo.getOwnerId() == ownerId;
    }
    
    public void removeLeader(final long ownerId) {
        this.m_leaders.remove(ownerId);
    }
    
    public void clear() {
        this.m_leaders.clear();
    }
    
    static {
        INSTANCE = new HeroesLeaderManager();
    }
}
