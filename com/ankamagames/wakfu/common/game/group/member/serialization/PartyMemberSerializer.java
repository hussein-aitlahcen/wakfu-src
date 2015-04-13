package com.ankamagames.wakfu.common.game.group.member.serialization;

import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;

public final class PartyMemberSerializer extends BinarSerial
{
    private final PartyMemberInterface m_member;
    private final BinarSerialPart[] m_parts;
    private final IdentificationPart m_identificationPart;
    private final FightStatusPart m_fightStatusPart;
    private final DeathStatusPart m_deathStatusPart;
    private final LevelInformationPart m_levelInformationPart;
    private final PositionPart m_positionPart;
    private final CompanionPart m_companionPart;
    private final HpPart m_hpPart;
    
    public PartyMemberSerializer(final PartyMemberInterface member) {
        super();
        this.m_member = member;
        this.m_identificationPart = new IdentificationPart(member);
        this.m_fightStatusPart = new FightStatusPart(member);
        this.m_deathStatusPart = new DeathStatusPart(member);
        this.m_levelInformationPart = new LevelInformationPart(member);
        this.m_positionPart = new PositionPart(member);
        this.m_hpPart = new HpPart(member);
        this.m_companionPart = new CompanionPart(member);
        this.m_parts = new BinarSerialPart[] { this.m_identificationPart, this.m_fightStatusPart, this.m_deathStatusPart, this.m_levelInformationPart, this.m_positionPart, this.m_hpPart, this.m_companionPart };
    }
    
    public byte[] serialize() {
        return this.build(this.partsEnumeration());
    }
    
    public PartyMemberInterface unserialize(final byte[] data) {
        this.fromBuild(data);
        return this.m_member;
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return this.m_parts;
    }
    
    public byte[] serializedForDeathStatusUpdate() {
        return this.build(this.m_deathStatusPart);
    }
    
    public byte[] serializedForFightStatusUpdate() {
        return this.build(this.m_fightStatusPart);
    }
    
    public byte[] serializedForLevelUpdate() {
        return this.build(this.m_levelInformationPart);
    }
    
    public byte[] serializedForPositionUpdate() {
        return this.build(this.m_positionPart);
    }
    
    public byte[] serializedForHpUpdate() {
        return this.build(this.m_hpPart);
    }
    
    public byte[] serializedCompanionPart() {
        return this.build(this.m_companionPart);
    }
    
    public PartyMemberSerializationPart getHpPart() {
        return this.m_hpPart;
    }
    
    public byte[] serializeWithoutHp() {
        return this.build(this.m_identificationPart, this.m_fightStatusPart, this.m_deathStatusPart, this.m_levelInformationPart, this.m_positionPart);
    }
    
    public byte[] serializeForCompanion() {
        return this.build(this.m_identificationPart, this.m_fightStatusPart, this.m_deathStatusPart, this.m_levelInformationPart, this.m_companionPart);
    }
}
