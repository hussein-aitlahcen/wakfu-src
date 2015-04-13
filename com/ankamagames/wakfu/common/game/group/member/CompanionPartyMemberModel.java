package com.ankamagames.wakfu.common.game.group.member;

import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class CompanionPartyMemberModel implements PartyMemberInterface
{
    private CompanionModel m_companionModel;
    private long m_partyId;
    private long m_clientId;
    private boolean m_isInFight;
    
    public CompanionPartyMemberModel() {
        super();
    }
    
    public CompanionPartyMemberModel(final CompanionModel companionModel) {
        super();
        this.m_companionModel = companionModel;
    }
    
    public CompanionModel getCompanionModel() {
        return this.m_companionModel;
    }
    
    public void setCompanionModel(final CompanionModel companionModel) {
        this.m_companionModel = companionModel;
    }
    
    @Override
    public short getBreedId() {
        return this.m_companionModel.getBreedId();
    }
    
    @Override
    public boolean isExist() {
        return this.m_companionModel != null;
    }
    
    @Override
    public long getCharacterId() {
        return -this.m_companionModel.getId();
    }
    
    @Override
    public void setGroupId(final long id) {
        this.m_partyId = id;
    }
    
    @Override
    public String getName() {
        return this.m_companionModel.getName();
    }
    
    @Override
    public Point3 getPosition() {
        throw new UnsupportedOperationException("Ne devrait pas etre utilis\u00e9");
    }
    
    @Override
    public short getInstanceId() {
        return 0;
    }
    
    @Override
    public void removeGroupId(final byte typeFromId) {
    }
    
    @Override
    public long getGroupId() {
        return this.getPartyId();
    }
    
    @Override
    public long getPartyId() {
        return this.m_partyId;
    }
    
    @Override
    public void setFollowed(final boolean activate) {
    }
    
    @Override
    public short getLevel() {
        if (this.m_companionModel == null) {
            return 0;
        }
        return this.m_companionModel.getLevel();
    }
    
    @Override
    public boolean isFollowed() {
        return false;
    }
    
    @Override
    public long getClientId() {
        return this.m_clientId;
    }
    
    @Override
    public int getCurrentHp() {
        return this.m_companionModel.getCurrentHp();
    }
    
    @Override
    public int getMaxHp() {
        return this.m_companionModel.getHpMax();
    }
    
    @Override
    public void setCurrentHp(final int value) {
        this.m_companionModel.setCurrentHp(value);
    }
    
    @Override
    public void setMaxHp(final int max) {
        this.m_companionModel.setHpMax(max);
    }
    
    @Override
    public boolean isDead() {
        return false;
    }
    
    @Override
    public boolean isInFight() {
        return this.m_isInFight;
    }
    
    @Override
    public void setClientId(final long clientId) {
        this.m_clientId = clientId;
    }
    
    @Override
    public void setCharacterId(final long characterId) {
    }
    
    @Override
    public void setName(final String name) {
    }
    
    @Override
    public void setBreedId(final short breedId) {
    }
    
    @Override
    public void setInFight(final boolean inFight) {
        this.m_isInFight = inFight;
    }
    
    @Override
    public void setDead(final boolean dead) {
    }
    
    @Override
    public void setLevel(final short level) {
    }
    
    @Override
    public void setInstanceId(final short instanceId) {
    }
    
    @Override
    public void setPosition(final Point3 position) {
    }
    
    @Override
    public int getRegen() {
        return 0;
    }
    
    @Override
    public void setRegen(final int regen) {
    }
    
    @Override
    public void addListener(final PlayerMemberModelListener partyMemberListener) {
    }
    
    @Override
    public String getGameServerOwner() {
        return null;
    }
    
    @Override
    public boolean isCompanion() {
        return true;
    }
    
    @Override
    public boolean isHero() {
        return false;
    }
    
    @Override
    public int getType() {
        return 2;
    }
    
    @Override
    public boolean isAuthorizedReconnection() {
        return false;
    }
}
