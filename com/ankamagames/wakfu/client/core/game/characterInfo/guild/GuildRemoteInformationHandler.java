package com.ankamagames.wakfu.client.core.game.characterInfo.guild;

import com.ankamagames.wakfu.common.game.guild.definition.*;
import gnu.trove.*;

public class GuildRemoteInformationHandler implements ClientGuildInformationHandler
{
    protected long m_guildId;
    private long m_blazon;
    private short m_level;
    private String m_name;
    private int m_nationId;
    
    @Override
    public GuildMember getMember(final long id) {
        throw new UnsupportedOperationException("On n'est pas cens\u00e9 r\u00e9cup\u00e9rer des membres de guilde d'un remote");
    }
    
    @Override
    public int getNationId() {
        return this.m_nationId;
    }
    
    public void setNationId(final int nationId) {
        this.m_nationId = nationId;
    }
    
    @Override
    public GuildRank getRank(final long rankId) {
        throw new UnsupportedOperationException("On n'est pas cens\u00e9 r\u00e9cup\u00e9rer des rangs de guilde d'un remote");
    }
    
    @Override
    public long getBestRank() {
        throw new UnsupportedOperationException("On n'est pas cens\u00e9 r\u00e9cup\u00e9rer le best rank d'un remote");
    }
    
    @Override
    public int getHavenWorldId() {
        throw new UnsupportedOperationException("On n'est pas cens\u00e9 r\u00e9cup\u00e9rer l'id de HM d'un remote");
    }
    
    @Override
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public void setGuildId(final long guildId) {
        this.m_guildId = guildId;
    }
    
    @Override
    public long getBlazon() {
        return this.m_blazon;
    }
    
    public void setBlazon(final long blazon) {
        this.m_blazon = blazon;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setLevel(final short level) {
        this.m_level = level;
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public TIntHashSet getActiveBonuses() {
        throw new UnsupportedOperationException("On n'est pas cens\u00e9 r\u00e9cup\u00e9rer les bonus d'un remote");
    }
    
    @Override
    public void clear() {
        this.m_guildId = 0L;
        this.m_blazon = 0L;
        this.m_level = 0;
        this.m_name = "";
    }
    
    @Override
    public String toString() {
        return "GuildRemoteInformationHandler{m_guildId=" + this.m_guildId + ", m_blazon=" + this.m_blazon + ", m_level=" + this.m_level + ", m_name='" + this.m_name + '\'' + '}';
    }
}
