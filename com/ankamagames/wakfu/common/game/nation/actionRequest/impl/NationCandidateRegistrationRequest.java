package com.ankamagames.wakfu.common.game.nation.actionRequest.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.item.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;
import java.nio.*;

public class NationCandidateRegistrationRequest extends NationActionRequest
{
    private static final Logger m_logger;
    public static final NationActionRequestFactory FACTORY;
    private long m_characterId;
    private byte[] m_slogan;
    private int m_citizenScore;
    private float m_wakfuGauge;
    private byte m_color1Index;
    private byte m_color1Factor;
    private byte m_color2Index;
    private byte m_color2Factor;
    private byte m_color3Index;
    private byte m_clothIndex;
    private byte m_faceIndex;
    private int m_headRefId;
    private int m_shouldersRefId;
    private int m_chestRefId;
    
    public NationCandidateRegistrationRequest() {
        super(NationActionRequestType.CANDIDATE_REGISTRATION_REQUEST);
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setSlogan(final String slogan) {
        this.m_slogan = StringUtils.toUTF8(slogan);
    }
    
    public void setCitizenScore(final int citizenScore) {
        this.m_citizenScore = citizenScore;
    }
    
    public void setWakfuGauge(final float wakfuGauge) {
        this.m_wakfuGauge = wakfuGauge;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public String getSlogan() {
        return StringUtils.fromUTF8(this.m_slogan);
    }
    
    public int getCitizenScore() {
        return this.m_citizenScore;
    }
    
    public float getWakfuGauge() {
        return this.m_wakfuGauge;
    }
    
    public byte getColor1Index() {
        return this.m_color1Index;
    }
    
    public byte getColor1Factor() {
        return this.m_color1Factor;
    }
    
    public byte getColor2Index() {
        return this.m_color2Index;
    }
    
    public byte getColor2Factor() {
        return this.m_color2Factor;
    }
    
    public byte getColor3Index() {
        return this.m_color3Index;
    }
    
    public byte getClothIndex() {
        return this.m_clothIndex;
    }
    
    public byte getFaceIndex() {
        return this.m_faceIndex;
    }
    
    public int getHeadRefId() {
        return this.m_headRefId;
    }
    
    public int getShouldersRefId() {
        return this.m_shouldersRefId;
    }
    
    public int getChestRefId() {
        return this.m_chestRefId;
    }
    
    public void setColors(final byte color1Index, final byte color1Factor, final byte color2Index, final byte color2Factor, final byte color3Index, final byte clothIndex, final byte faceIndex) {
        this.m_color1Index = color1Index;
        this.m_color1Factor = color1Factor;
        this.m_color2Index = color2Index;
        this.m_color2Factor = color2Factor;
        this.m_color3Index = color3Index;
        this.m_clothIndex = clothIndex;
        this.m_faceIndex = faceIndex;
    }
    
    public void setEquipment(@Nullable final Item head, @Nullable final Item shoulders, @Nullable final Item chest) {
        this.m_headRefId = ((head != null) ? head.getReferenceId() : 0);
        this.m_shouldersRefId = ((shoulders != null) ? shoulders.getReferenceId() : 0);
        this.m_chestRefId = ((chest != null) ? chest.getReferenceId() : 0);
    }
    
    @Override
    public void execute() {
        final Nation nation = this.getConcernedNation();
        if (nation == null) {
            NationCandidateRegistrationRequest.m_logger.error((Object)("Impossible d'ex\u00e9cuter l'action " + this + " : la nation " + this.m_nationId + " n'existe pas"));
            return;
        }
        nation.requestRegisterCandidate(this);
    }
    
    @Override
    public boolean authorizedFromClient(final Citizen citizen) {
        final int requesterNationId = citizen.getCitizenComportment().getNationId();
        if (requesterNationId != this.m_nationId) {
            return false;
        }
        if (citizen.getLevel() < 1) {
            return false;
        }
        final int citizenScore = citizen.getCitizenComportment().getCitizenScoreForNation(requesterNationId);
        return CitizenRankManager.getInstance().getRankFromCitizenScore(citizenScore).hasRule(CitizenRankRule.CAN_STAND_FOR_ELECTIONS);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_characterId);
        buffer.putShort((short)this.m_slogan.length);
        buffer.put(this.m_slogan);
        buffer.putInt(this.m_citizenScore);
        buffer.putFloat(this.m_wakfuGauge);
        buffer.put(this.m_color1Index);
        buffer.put(this.m_color1Factor);
        buffer.put(this.m_color2Index);
        buffer.put(this.m_color2Factor);
        buffer.put(this.m_color3Index);
        buffer.put(this.m_clothIndex);
        buffer.put(this.m_faceIndex);
        buffer.putInt(this.m_headRefId);
        buffer.putInt(this.m_shouldersRefId);
        buffer.putInt(this.m_chestRefId);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.m_characterId = buffer.getLong();
        final short sloganSize = buffer.getShort();
        buffer.get(this.m_slogan = new byte[sloganSize]);
        this.m_citizenScore = buffer.getInt();
        this.m_wakfuGauge = buffer.getFloat();
        this.m_color1Index = buffer.get();
        this.m_color1Factor = buffer.get();
        this.m_color2Index = buffer.get();
        this.m_color2Factor = buffer.get();
        this.m_color3Index = buffer.get();
        this.m_clothIndex = buffer.get();
        this.m_faceIndex = buffer.get();
        this.m_headRefId = buffer.getInt();
        this.m_shouldersRefId = buffer.getInt();
        this.m_chestRefId = buffer.getInt();
        return true;
    }
    
    @Override
    public int serializedSize() {
        return 10 + this.m_slogan.length + 4 + 4 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 4 + 4 + 4;
    }
    
    @Override
    public void clear() {
        this.m_nationId = -1;
        this.m_characterId = -1L;
        this.m_slogan = null;
        this.m_citizenScore = 0;
        this.m_wakfuGauge = 0.0f;
        this.m_color1Index = -1;
        this.m_color1Factor = -1;
        this.m_color2Index = -1;
        this.m_color2Factor = -1;
        this.m_color3Index = -1;
        this.m_clothIndex = -1;
        this.m_faceIndex = -1;
        this.m_headRefId = -1;
        this.m_shouldersRefId = -1;
        this.m_chestRefId = -1;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationCandidateRegistrationRequest.class);
        FACTORY = new NationActionRequestFactory() {
            @Override
            public NationActionRequest createNew() {
                return new NationCandidateRegistrationRequest();
            }
        };
    }
}
