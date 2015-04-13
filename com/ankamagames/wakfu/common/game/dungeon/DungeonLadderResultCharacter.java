package com.ankamagames.wakfu.common.game.dungeon;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.dungeon.ranks.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class DungeonLadderResultCharacter extends AbstractCharacterData implements Nationable
{
    private final int m_nationId;
    private final short m_level;
    private final Rank m_rank;
    
    public DungeonLadderResultCharacter(final long id, final String name, final short breedId, final byte sex, final long guildId, final String guildName, final CharacterDataAppearance appearance, final int nationId, final short level, final Rank rank) {
        super(id, name, breedId, sex, guildId, guildName, appearance);
        this.m_nationId = nationId;
        this.m_level = level;
        this.m_rank = rank;
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_id);
        final byte[] name = StringUtils.toUTF8(this.m_name);
        buffer.putShort((short)name.length);
        buffer.put(name);
        buffer.putShort(this.m_breedId);
        buffer.put(this.m_sex);
        buffer.putLong(this.m_guildId);
        final byte[] guildName = StringUtils.toUTF8(this.m_guildName);
        buffer.putShort((short)guildName.length);
        buffer.put(guildName);
        this.m_appearance.serialize(buffer);
        buffer.putInt(this.m_nationId);
        buffer.putShort(this.m_level);
        buffer.putInt(this.m_rank.ordinal());
    }
    
    public static DungeonLadderResultCharacter fromBuild(final ByteBuffer buffer) {
        final long id = buffer.getLong();
        final byte[] name = new byte[buffer.getShort()];
        buffer.get(name);
        final short breedId = buffer.getShort();
        final byte sex = buffer.get();
        final long guildId = buffer.getLong();
        final byte[] guildName = new byte[buffer.getShort()];
        buffer.get(guildName);
        final CharacterDataAppearance appearance = CharacterDataAppearance.fromBuild(buffer);
        final int nationId = buffer.getInt();
        final short level = buffer.getShort();
        final Rank rank = Rank.values()[buffer.getInt()];
        return new DungeonLadderResultCharacter(id, StringUtils.fromUTF8(name), breedId, sex, guildId, StringUtils.fromUTF8(guildName), appearance, nationId, level, rank);
    }
    
    public int serializedSize() {
        final byte[] name = StringUtils.toUTF8(this.m_name);
        final byte[] guildName = StringUtils.toUTF8(this.m_guildName);
        return 10 + name.length + 2 + 1 + 8 + 2 + guildName.length + this.m_appearance.serializedSize() + 4 + 2 + 4;
    }
    
    @Override
    public int getNationId() {
        return this.m_nationId;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public Rank getRank() {
        return this.m_rank;
    }
}
