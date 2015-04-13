package com.ankamagames.wakfu.common.game.group.member;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.hero.*;
import java.util.*;

public final class PartyMemberModel implements PartyMemberInterface
{
    private String m_name;
    private long m_clientId;
    private long m_characterId;
    private short m_instanceId;
    private Point3 m_position;
    private int m_currentHp;
    private int m_maxHp;
    private int m_regen;
    private long m_groupId;
    private short m_level;
    private boolean m_isFollowed;
    private boolean m_isDead;
    private boolean m_isInFight;
    private short m_breedId;
    private String m_gameServerOwner;
    private final List<PlayerMemberModelListener> m_listeners;
    
    public PartyMemberModel() {
        super();
        this.m_listeners = new ArrayList<PlayerMemberModelListener>();
    }
    
    @Override
    public boolean isExist() {
        return this.m_name != null;
    }
    
    @Override
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    @Override
    public void setGroupId(final long id) {
        this.m_groupId = id;
    }
    
    @Override
    public short getBreedId() {
        return this.m_breedId;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public Point3 getPosition() {
        return this.m_position;
    }
    
    @Override
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    @Override
    public void removeGroupId(final byte typeFromId) {
        this.m_groupId = 0L;
    }
    
    @Override
    public long getGroupId() {
        return this.m_groupId;
    }
    
    @Override
    public long getPartyId() {
        return this.m_groupId;
    }
    
    @Override
    public void setFollowed(final boolean activate) {
        this.m_isFollowed = activate;
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public boolean isFollowed() {
        return this.m_isFollowed;
    }
    
    @Override
    public long getClientId() {
        return this.m_clientId;
    }
    
    @Override
    public int getCurrentHp() {
        return this.m_currentHp;
    }
    
    @Override
    public int getMaxHp() {
        return this.m_maxHp;
    }
    
    @Override
    public void setCurrentHp(final int value) {
        this.m_currentHp = value;
        this.fireSetCurrentHp();
    }
    
    @Override
    public void setMaxHp(final int max) {
        this.m_maxHp = max;
        this.fireSetMaxHp();
    }
    
    @Override
    public boolean isDead() {
        return this.m_isDead;
    }
    
    @Override
    public boolean isInFight() {
        return this.m_isInFight;
    }
    
    @Override
    public boolean isAuthorizedReconnection() {
        return false;
    }
    
    @Override
    public void setClientId(final long clientId) {
        this.m_clientId = clientId;
    }
    
    @Override
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    @Override
    public void setName(final String name) {
        this.m_name = name;
    }
    
    @Override
    public void setBreedId(final short breedId) {
        this.m_breedId = breedId;
    }
    
    @Override
    public void setInFight(final boolean inFight) {
        this.m_isInFight = inFight;
        this.fireSetInFight();
    }
    
    @Override
    public void setDead(final boolean dead) {
        this.m_isDead = dead;
        this.fireSetDead();
    }
    
    @Override
    public void setLevel(final short level) {
        this.m_level = level;
        this.fireSetLevel();
    }
    
    @Override
    public void setInstanceId(final short instanceId) {
        this.m_instanceId = instanceId;
    }
    
    @Override
    public void setPosition(final Point3 position) {
        this.m_position = position;
    }
    
    @Override
    public int getRegen() {
        return this.m_regen;
    }
    
    @Override
    public void setRegen(final int regen) {
        this.m_regen = regen;
    }
    
    @Override
    public String getGameServerOwner() {
        return this.m_gameServerOwner;
    }
    
    @Override
    public boolean isCompanion() {
        return false;
    }
    
    @Override
    public boolean isHero() {
        return HeroesLeaderManager.INSTANCE.isHero(this.m_clientId, this.m_characterId);
    }
    
    @Override
    public int getType() {
        return 1;
    }
    
    @Override
    public void addListener(final PlayerMemberModelListener listener) {
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    public boolean remove(final PlayerMemberModelListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    private void fireSetLevel() {
        for (final PlayerMemberModelListener listener : this.m_listeners) {
            listener.onSetLevel(this);
        }
    }
    
    private void fireSetDead() {
        for (final PlayerMemberModelListener listener : this.m_listeners) {
            listener.onSetDead(this);
        }
    }
    
    private void fireSetInFight() {
        for (final PlayerMemberModelListener listener : this.m_listeners) {
            listener.onSetInFight(this);
        }
    }
    
    private void fireSetCurrentHp() {
        for (final PlayerMemberModelListener listener : this.m_listeners) {
            listener.onSetCurrentHp(this);
        }
    }
    
    private void fireSetMaxHp() {
        for (final PlayerMemberModelListener listener : this.m_listeners) {
            listener.onSetMaxHp(this);
        }
    }
}
