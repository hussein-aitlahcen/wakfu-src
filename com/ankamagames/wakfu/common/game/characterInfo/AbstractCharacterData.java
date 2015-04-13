package com.ankamagames.wakfu.common.game.characterInfo;

public abstract class AbstractCharacterData
{
    protected long m_id;
    protected String m_name;
    protected short m_breedId;
    protected byte m_sex;
    protected long m_guildId;
    protected String m_guildName;
    protected CharacterDataAppearance m_appearance;
    
    protected AbstractCharacterData() {
        super();
    }
    
    protected AbstractCharacterData(final long id, final String name, final short breedId, final byte sex, final long guildId, final String guildName, final CharacterDataAppearance appearance) {
        super();
        this.m_id = id;
        this.m_name = name;
        this.m_breedId = breedId;
        this.m_sex = sex;
        this.m_guildId = guildId;
        this.m_guildName = guildName;
        this.m_appearance = appearance;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public byte getSex() {
        return this.m_sex;
    }
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public String getGuildName() {
        return this.m_guildName;
    }
    
    public CharacterDataAppearance getAppearance() {
        return this.m_appearance;
    }
}
