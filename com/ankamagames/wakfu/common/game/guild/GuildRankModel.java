package com.ankamagames.wakfu.common.game.guild;

import java.util.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import gnu.trove.*;

class GuildRankModel implements GuildRank
{
    private final ArrayList<GuildRankListener> m_listeners;
    private final long m_id;
    private String m_name;
    private long m_authorisations;
    private short m_position;
    
    GuildRankModel(final long id) {
        super();
        this.m_listeners = new ArrayList<GuildRankListener>();
        this.m_id = id;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public long getAuthorisations() {
        return this.m_authorisations;
    }
    
    @Override
    public short getPosition() {
        return this.m_position;
    }
    
    @Override
    public boolean forEachAuthorisation(final TObjectProcedure<GuildRankAuthorisation> procedure) {
        final GuildRankAuthorisation[] values = GuildRankAuthorisation.values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final GuildRankAuthorisation authorisation = values[i];
            if (this.hasAuthorisation(authorisation) && !procedure.execute(authorisation)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasAuthorisation(final GuildRankAuthorisation authorisation) {
        return this.hasAuthorisation(authorisation, (short)(-1));
    }
    
    @Override
    public boolean hasAuthorisation(final GuildRankAuthorisation authorisation, final short targetPosition) {
        final int flag = 1 << authorisation.id;
        return (targetPosition == -1 || authorisation.hasAuthorisationConcerningPosition(this.m_position, targetPosition)) && (this.m_authorisations & flag) == flag;
    }
    
    public void setName(final String name) {
        this.m_name = name;
        this.fireRankChanged();
    }
    
    public void setAuthorisations(final long authorisations) {
        this.m_authorisations = authorisations;
        this.fireRankChanged();
    }
    
    @Override
    public void setPosition(final short position) {
        this.m_position = position;
        this.fireRankChanged();
    }
    
    private void fireRankChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).rankChanged(this);
        }
    }
    
    @Override
    public boolean addListener(final GuildRankListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final GuildRankListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "GuildRankModel{m_id=" + this.m_id + ", m_name='" + this.m_name + '\'' + ", m_authorisations=" + this.m_authorisations + '}';
    }
}
