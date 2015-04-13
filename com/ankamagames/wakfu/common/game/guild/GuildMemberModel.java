package com.ankamagames.wakfu.common.game.guild;

import java.util.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;

final class GuildMemberModel implements GuildMember
{
    private final ArrayList<GuildMemberListener> m_listeners;
    private final long m_id;
    private long m_rank;
    private int m_guildPoints;
    private String m_name;
    private long m_xp;
    private boolean m_connected;
    private byte m_smiley;
    private int m_nationId;
    private byte m_sex;
    private short m_breedId;
    
    GuildMemberModel(final long id) {
        super();
        this.m_listeners = new ArrayList<GuildMemberListener>();
        this.m_id = id;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public int getGuildPoints() {
        return this.m_guildPoints;
    }
    
    @Override
    public long getRank() {
        return this.m_rank;
    }
    
    @Override
    public long getXp() {
        return this.m_xp;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public boolean isConnected() {
        return this.m_connected;
    }
    
    @Override
    public byte getSmiley() {
        return this.m_smiley;
    }
    
    @Override
    public byte getSex() {
        return this.m_sex;
    }
    
    @Override
    public short getBreedId() {
        return this.m_breedId;
    }
    
    @Override
    public int getNationId() {
        return this.m_nationId;
    }
    
    void setGuildPoints(final int guildPoints) {
        this.m_guildPoints = guildPoints;
        this.fireMemberChanged();
    }
    
    void setRank(final long rank) {
        this.m_rank = rank;
        this.fireMemberChanged();
    }
    
    void setName(final String name) {
        this.m_name = name;
        this.fireMemberChanged();
    }
    
    void setXp(final long xp) {
        this.m_xp = xp;
        this.fireMemberChanged();
    }
    
    void setConnected(final boolean connected) {
        this.m_connected = connected;
        this.fireMemberChanged();
    }
    
    void setSmiley(final byte smiley) {
        this.m_smiley = smiley;
        this.fireMemberChanged();
    }
    
    void setSex(final byte sex) {
        this.m_sex = sex;
        this.fireMemberChanged();
    }
    
    void setBreedId(final short breedId) {
        this.m_breedId = breedId;
        this.fireMemberChanged();
    }
    
    void setNationId(final int nationId) {
        this.m_nationId = nationId;
        this.fireMemberChanged();
    }
    
    private void fireMemberChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).memberChanged(this);
        }
    }
    
    @Override
    public boolean addListener(final GuildMemberListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final GuildMemberListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "GuildMemberModel{m_listeners=" + this.m_listeners + ", m_id=" + this.m_id + ", m_rank=" + this.m_rank + ", m_guildPoints=" + this.m_guildPoints + ", m_name='" + this.m_name + '\'' + ", m_xp=" + this.m_xp + ", m_connected=" + this.m_connected + ", m_smiley=" + this.m_smiley + ", m_nationId=" + this.m_nationId + ", m_sex=" + this.m_sex + ", m_breedId=" + this.m_breedId + '}';
    }
}
