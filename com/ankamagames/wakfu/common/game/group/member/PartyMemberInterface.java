package com.ankamagames.wakfu.common.game.group.member;

import com.ankamagames.framework.kernel.core.maths.*;

public interface PartyMemberInterface
{
    boolean isExist();
    
    long getCharacterId();
    
    void setGroupId(long p0);
    
    String getName();
    
    Point3 getPosition();
    
    short getInstanceId();
    
    void removeGroupId(byte p0);
    
    long getGroupId();
    
    long getPartyId();
    
    void setFollowed(boolean p0);
    
    short getLevel();
    
    boolean isFollowed();
    
    long getClientId();
    
    int getCurrentHp();
    
    int getMaxHp();
    
    void setCurrentHp(int p0);
    
    void setMaxHp(int p0);
    
    boolean isDead();
    
    boolean isInFight();
    
    void setClientId(long p0);
    
    void setCharacterId(long p0);
    
    void setName(String p0);
    
    void setInFight(boolean p0);
    
    void setDead(boolean p0);
    
    void setBreedId(short p0);
    
    void setLevel(short p0);
    
    void setInstanceId(short p0);
    
    void setPosition(Point3 p0);
    
    int getRegen();
    
    void setRegen(int p0);
    
    void addListener(PlayerMemberModelListener p0);
    
    String getGameServerOwner();
    
    boolean isCompanion();
    
    boolean isHero();
    
    int getType();
    
    boolean isAuthorizedReconnection();
    
    short getBreedId();
}
