package com.ankamagames.wakfu.common.game.nation.election;

import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class CandidateInfo extends AbstractGovernmentInfo
{
    private final String m_slogan;
    private short m_ballotCount;
    private boolean m_withDraw;
    
    public CandidateInfo(final long id, final String name, final String slogan, final short ballotCount, final short breedId, final byte sex, final boolean withDraw, final long guildId, final String guildName, final int citizenScore, final float wakfuGauge, final byte color1Index, final byte color1Factor, final byte color2Index, final byte color2Factor, final byte color3Index, final byte clothIndex, final byte faceIndex, final int headRefId, final int shouldersRefId, final int chestRefId) {
        super(id, name, breedId, sex, guildId, guildName, citizenScore, wakfuGauge, new CharacterDataAppearance(color1Index, color1Factor, color2Index, color2Factor, color3Index, clothIndex, faceIndex, headRefId, shouldersRefId, chestRefId));
        this.m_ballotCount = 0;
        this.m_withDraw = false;
        this.m_slogan = slogan;
        this.m_ballotCount = ballotCount;
        this.m_withDraw = withDraw;
    }
    
    public CandidateInfo(final long id, final String name, final String slogan, final short ballotCount, final short breedId, final byte sex, final boolean withDraw, final long guildId, final String guildName, final int citizenScore, final float wakfuGauge, final CharacterDataAppearance appearance) {
        super(id, name, breedId, sex, guildId, guildName, citizenScore, wakfuGauge, appearance);
        this.m_ballotCount = 0;
        this.m_withDraw = false;
        this.m_slogan = slogan;
        this.m_ballotCount = ballotCount;
        this.m_withDraw = withDraw;
    }
    
    public String getSlogan() {
        return this.m_slogan;
    }
    
    public short getBallotCount() {
        return this.m_ballotCount;
    }
    
    public void setBallotCount(final short ballotCount) {
        this.m_ballotCount = ballotCount;
    }
    
    public void setWitDraw(final boolean withdraw) {
        this.m_withDraw = withdraw;
    }
    
    public boolean isWithDraw() {
        return this.m_withDraw;
    }
    
    @Override
    public String toString() {
        return "{Candidate " + this.m_id + ' ' + this.m_name + '}';
    }
    
    public void serializeWithoutBallots(final ByteBuffer buffer) {
        this.serialize(buffer, false);
    }
    
    public void serialize(final ByteBuffer buffer) {
        this.serialize(buffer, true);
    }
    
    public void serialize(final ByteBuffer buffer, final boolean serializeBallots) {
        buffer.putLong(this.m_id);
        final byte[] name = StringUtils.toUTF8(this.m_name);
        buffer.putShort((short)name.length);
        buffer.put(name);
        final byte[] slogan = StringUtils.toUTF8(this.m_slogan);
        buffer.putShort((short)slogan.length);
        buffer.put(slogan);
        buffer.putShort((short)(serializeBallots ? this.m_ballotCount : 0));
        buffer.putShort(this.m_breedId);
        buffer.put(this.m_sex);
        buffer.put((byte)(this.m_withDraw ? 1 : 0));
        buffer.putLong(this.m_guildId);
        final byte[] guildName = StringUtils.toUTF8(this.getGuildName());
        buffer.putShort((short)guildName.length);
        buffer.put(guildName);
        buffer.putInt(this.m_citizenScore);
        buffer.putFloat(this.m_wakfuGauge);
        this.m_appearance.serialize(buffer);
    }
    
    public static CandidateInfo fromBuild(final ByteBuffer buffer) {
        final long characterId = buffer.getLong();
        final byte[] name = new byte[buffer.getShort()];
        buffer.get(name);
        final byte[] slogan = new byte[buffer.getShort()];
        buffer.get(slogan);
        final short ballotCount = buffer.getShort();
        final short breedId = buffer.getShort();
        final byte sex = buffer.get();
        final boolean withDraw = buffer.get() == 1;
        final long guildId = buffer.getLong();
        final byte[] guildName = new byte[buffer.getShort()];
        buffer.get(guildName);
        final int citizenScore = buffer.getInt();
        final float wakfuGauge = buffer.getFloat();
        return new CandidateInfo(characterId, StringUtils.fromUTF8(name), StringUtils.fromUTF8(slogan), ballotCount, breedId, sex, withDraw, guildId, StringUtils.fromUTF8(guildName), citizenScore, wakfuGauge, CharacterDataAppearance.fromBuild(buffer));
    }
    
    public int serializedSize() {
        final byte[] slogan = StringUtils.toUTF8(this.getSlogan());
        final byte[] name = StringUtils.toUTF8(this.getName());
        final byte[] guildName = StringUtils.toUTF8(this.getGuildName());
        return 10 + name.length + 2 + slogan.length + 2 + guildName.length + 8 + 2 + 2 + 1 + 1 + 4 + 4 + this.m_appearance.serializedSize();
    }
}
