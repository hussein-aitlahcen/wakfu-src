package com.ankamagames.wakfu.common.game.nation.actionRequest.impl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.actionRequest.*;
import com.ankamagames.wakfu.common.game.item.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.nio.*;

public class NationGovernmentNominationConfirmationResult extends NationActionRequest
{
    public static byte ACCEPTED;
    public static byte REFUSED;
    public static byte CRITERION_FAIL;
    public static byte PDC_FAIL;
    public static byte FORBIDDEN_BY_REVOKE;
    private static final Logger m_logger;
    public static final NationActionRequestFactory FACTORY;
    private long m_characterId;
    private long m_rankId;
    private byte m_result;
    private int m_citizenScore;
    private float m_wakfuGauge;
    private Colors m_colors;
    private Equipments m_equipment;
    
    public NationGovernmentNominationConfirmationResult() {
        super(NationActionRequestType.GOVERNMENT_NOMINATION_CONFIRMATION_RESULT);
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
    
    public void setCitizenScore(final int citizenScore) {
        this.m_citizenScore = citizenScore;
    }
    
    public long getRankId() {
        return this.m_rankId;
    }
    
    public void setRankId(final long rankId) {
        this.m_rankId = rankId;
    }
    
    public void setWakfuGauge(final float wakfuGauge) {
        this.m_wakfuGauge = wakfuGauge;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getCitizenScore() {
        return this.m_citizenScore;
    }
    
    public byte getResult() {
        return this.m_result;
    }
    
    public void setResult(final byte result) {
        this.m_result = result;
    }
    
    public float getWakfuGauge() {
        return this.m_wakfuGauge;
    }
    
    public Colors getColors() {
        return this.m_colors;
    }
    
    public Equipments getEquipment() {
        return this.m_equipment;
    }
    
    public void setColors(final byte color1Index, final byte color1Factor, final byte color2Index, final byte color2Factor, final byte color3Index, final byte clothIndex, final byte faceIndex) {
        this.m_colors = new Colors(color1Index, color1Factor, color2Index, color2Factor, color3Index, clothIndex, faceIndex);
    }
    
    public void setEquipment(@Nullable final Item head, @Nullable final Item shoulders, @Nullable final Item chest) {
        this.m_equipment = new Equipments((head != null) ? head.getReferenceId() : 0, (shoulders != null) ? shoulders.getReferenceId() : 0, (chest != null) ? chest.getReferenceId() : 0);
    }
    
    @Override
    public void execute() {
        final Nation nation = this.getConcernedNation();
        if (nation == null) {
            NationGovernmentNominationConfirmationResult.m_logger.error((Object)("Impossible d'ex\u00e9cuter l'action " + this + " : la nation " + this.m_nationId + " n'existe pas"));
            return;
        }
        nation.requestGovernmentNominationResult(this);
    }
    
    @Override
    public boolean authorizedFromClient(final Citizen citizen) {
        final int requesterNationId = citizen.getCitizenComportment().getNationId();
        return requesterNationId == this.m_nationId && citizen.getId() == this.m_characterId;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_characterId);
        buffer.putLong(this.m_rankId);
        buffer.put(this.m_result);
        buffer.putInt(this.m_citizenScore);
        buffer.putFloat(this.m_wakfuGauge);
        if (this.m_colors != null) {
            buffer.put((byte)1);
            this.m_colors.serialize(buffer);
        }
        else {
            buffer.put((byte)0);
        }
        if (this.m_equipment != null) {
            buffer.put((byte)1);
            this.m_equipment.serialize(buffer);
        }
        else {
            buffer.put((byte)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.m_characterId = buffer.getLong();
        this.m_rankId = buffer.getLong();
        this.m_result = buffer.get();
        this.m_citizenScore = buffer.getInt();
        this.m_wakfuGauge = buffer.getFloat();
        if (buffer.get() == 1) {
            this.m_colors = Colors.fromBuild(buffer);
        }
        if (buffer.get() == 1) {
            this.m_equipment = Equipments.fromBuild(buffer);
        }
        return true;
    }
    
    @Override
    public int serializedSize() {
        return 26 + ((this.m_colors != null) ? this.m_colors.serializedSize() : 0) + 1 + ((this.m_equipment != null) ? this.m_equipment.serializedSize() : 0);
    }
    
    @Override
    public void clear() {
        this.m_nationId = -1;
        this.m_characterId = -1L;
        this.m_result = -1;
        this.m_citizenScore = 0;
        this.m_wakfuGauge = 0.0f;
        this.m_colors = null;
        this.m_equipment = null;
    }
    
    static {
        NationGovernmentNominationConfirmationResult.ACCEPTED = 1;
        NationGovernmentNominationConfirmationResult.REFUSED = 2;
        NationGovernmentNominationConfirmationResult.CRITERION_FAIL = 3;
        NationGovernmentNominationConfirmationResult.PDC_FAIL = 4;
        NationGovernmentNominationConfirmationResult.FORBIDDEN_BY_REVOKE = 5;
        m_logger = Logger.getLogger((Class)NationGovernmentNominationConfirmationResult.class);
        FACTORY = new NationActionRequestFactory() {
            @Override
            public NationActionRequest createNew() {
                return new NationGovernmentNominationConfirmationResult();
            }
        };
    }
    
    public static class Colors
    {
        private byte m_color1Index;
        private byte m_color1Factor;
        private byte m_color2Index;
        private byte m_color2Factor;
        private byte m_color3Index;
        private byte m_clothIndex;
        private byte m_faceIndex;
        
        private Colors(final byte color1Index, final byte color1Factor, final byte color2Index, final byte color2Factor, final byte color3Index, final byte clothIndex, final byte faceIndex) {
            super();
            this.m_color1Index = color1Index;
            this.m_color1Factor = color1Factor;
            this.m_color2Index = color2Index;
            this.m_color2Factor = color2Factor;
            this.m_color3Index = color3Index;
            this.m_clothIndex = clothIndex;
            this.m_faceIndex = faceIndex;
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
        
        public void serialize(final ByteBuffer buffer) {
            buffer.put(this.m_color1Index);
            buffer.put(this.m_color1Factor);
            buffer.put(this.m_color2Index);
            buffer.put(this.m_color2Factor);
            buffer.put(this.m_color3Index);
            buffer.put(this.m_clothIndex);
            buffer.put(this.m_faceIndex);
        }
        
        public static Colors fromBuild(final ByteBuffer buffer) {
            return new Colors(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get());
        }
        
        public int serializedSize() {
            return 7;
        }
    }
    
    public static class Equipments
    {
        private int m_headRefId;
        private int m_shouldersRefId;
        private int m_chestRefId;
        
        private Equipments(final int head, final int shoulders, final int chest) {
            super();
            this.m_headRefId = head;
            this.m_shouldersRefId = shoulders;
            this.m_chestRefId = chest;
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
        
        public void serialize(final ByteBuffer buffer) {
            buffer.putInt(this.m_headRefId);
            buffer.putInt(this.m_shouldersRefId);
            buffer.putInt(this.m_chestRefId);
        }
        
        public static Equipments fromBuild(final ByteBuffer buffer) {
            return new Equipments(buffer.getInt(), buffer.getInt(), buffer.getInt());
        }
        
        public int serializedSize() {
            return 12;
        }
    }
}
