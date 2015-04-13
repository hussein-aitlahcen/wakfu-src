package com.ankamagames.wakfu.common.game.nation.government;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.government.data.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.election.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.rawData.*;

public class GovernmentInfo extends AbstractGovernmentInfo implements Nationable
{
    private final int m_nationId;
    private final NationRank m_rank;
    private final GovernmentData m_data;
    
    public GovernmentInfo(final NationRank rank, final int nationId, final long id, final String name, final short breedId, final byte sex, final long guildId, final String guildName, final int citizenScore, final float wakfuGauge, final CharacterDataAppearance appearance, final GovernmentData data) {
        super(id, name, breedId, sex, guildId, guildName, citizenScore, wakfuGauge, appearance);
        this.m_rank = rank;
        this.m_nationId = nationId;
        this.m_data = data;
    }
    
    public GovernmentInfo(final CandidateInfo info, final NationRank rank, final int nationId, final GovernmentData data) {
        this(rank, nationId, info.getId(), info.getName(), info.getBreedId(), info.getSex(), info.getGuildId(), info.getGuildName(), info.getCitizenScore(), info.getWakfuGauge(), info.getAppearance(), data);
    }
    
    public GovernmentInfo(final GovernmentInfo info, final NationRank rank, final GovernmentData data) {
        super(info.m_id, info.m_name, info.m_breedId, info.m_sex, info.m_guildId, info.m_guildName, info.m_citizenScore, info.m_wakfuGauge, info.m_appearance);
        this.m_nationId = info.m_nationId;
        this.m_rank = rank;
        this.m_data = data;
    }
    
    public void setCitizenScore(final int citizenScore) {
        this.m_citizenScore = citizenScore;
    }
    
    public void setWakfuGauge(final float wakfuGauge) {
        this.m_wakfuGauge = wakfuGauge;
    }
    
    public NationRank getRank() {
        return this.m_rank;
    }
    
    @Override
    public int getNationId() {
        return this.m_nationId;
    }
    
    public GovernmentData getData() {
        return this.m_data;
    }
    
    @Override
    public String toString() {
        return "{Government " + this.m_id + ' ' + this.m_name + '}';
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_rank.getId());
        buffer.putInt(this.m_nationId);
        buffer.putLong(this.m_id);
        final byte[] name = StringUtils.toUTF8(this.m_name);
        buffer.putShort((short)name.length);
        buffer.put(name);
        buffer.putShort(this.m_breedId);
        buffer.put(this.m_sex);
        buffer.putLong(this.m_guildId);
        final byte[] guildName = StringUtils.toUTF8(this.getGuildName());
        buffer.putShort((short)guildName.length);
        buffer.put(guildName);
        buffer.putInt(this.m_citizenScore);
        buffer.putFloat(this.m_wakfuGauge);
        this.m_appearance.serialize(buffer);
        final RawNationGovernmentData rawData = new RawNationGovernmentData();
        this.m_data.toRaw(rawData);
        rawData.serialize(buffer);
    }
    
    public static GovernmentInfo fromBuild(final ByteBuffer buffer) {
        final NationRank rank = NationRank.getById(buffer.getLong());
        final int nationId = buffer.getInt();
        final long characterId = buffer.getLong();
        final byte[] name = new byte[buffer.getShort()];
        buffer.get(name);
        final short breedId = buffer.getShort();
        final byte sex = buffer.get();
        final long guildId = buffer.getLong();
        final byte[] guildName = new byte[buffer.getShort()];
        buffer.get(guildName);
        final int citizenScore = buffer.getInt();
        final float wakfuGauge = buffer.getFloat();
        final CharacterDataAppearance appearance = CharacterDataAppearance.fromBuild(buffer);
        final RawNationGovernmentData rawGovernment = new RawNationGovernmentData();
        rawGovernment.unserialize(buffer);
        final GovernmentData data = GovernmentData.createNew(rank);
        data.fromRaw(rawGovernment);
        return new GovernmentInfo(rank, nationId, characterId, StringUtils.fromUTF8(name), breedId, sex, guildId, StringUtils.fromUTF8(guildName), citizenScore, wakfuGauge, appearance, data);
    }
    
    public int serializedSize() {
        final byte[] name = StringUtils.toUTF8(this.getName());
        final byte[] guildName = StringUtils.toUTF8(this.getGuildName());
        final RawNationGovernmentData rawData = new RawNationGovernmentData();
        this.m_data.toRaw(rawData);
        return 22 + name.length + 2 + 1 + 8 + 2 + guildName.length + 4 + 4 + this.m_appearance.serializedSize() + rawData.serializedSize();
    }
}
