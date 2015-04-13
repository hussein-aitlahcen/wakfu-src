package com.ankamagames.wakfu.common.game.group.partySearch;

import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.google.common.collect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public abstract class AbstractPartyRequester
{
    private long m_id;
    private GameDateConst m_registrationDate;
    protected ArrayList<PartyOccupation> m_occupations;
    protected PartyMood m_mood;
    protected String m_description;
    
    protected AbstractPartyRequester(final ByteBuffer bb) {
        super();
        this.m_id = bb.getLong();
        this.m_registrationDate = GameDate.fromLong(bb.getLong());
        this.m_occupations = new ArrayList<PartyOccupation>();
        for (int occupationNumber = bb.getInt(), i = 0; i < occupationNumber; ++i) {
            final PartyOccupation occupation = PartyOccupationManager.INSTANCE.getPartyOccupation(bb.getLong());
            if (occupation != null) {
                this.m_occupations.add(occupation);
            }
        }
        this.m_mood = PartyMood.getFromId(bb.get());
        final byte[] encodedString = new byte[bb.getShort()];
        bb.get(encodedString);
        this.m_description = StringUtils.fromUTF8(encodedString);
    }
    
    protected AbstractPartyRequester(final long id, final PartyMood mood, final String description, final GameDateConst registrationDate, final List<PartyOccupation> occupations) {
        super();
        this.m_id = id;
        this.m_mood = mood;
        this.m_description = description;
        this.m_registrationDate = registrationDate;
        this.m_occupations = new ArrayList<PartyOccupation>(occupations);
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public AbstractPartyRequester updateRequester(final PartyMood mood, final String description, final List<PartyOccupation> occupations) {
        this.m_mood = mood;
        this.m_description = description;
        this.m_occupations = new ArrayList<PartyOccupation>(occupations);
        return this;
    }
    
    public void setMood(final PartyMood mood) {
        this.m_mood = mood;
    }
    
    public PartyMood getMood() {
        return this.m_mood;
    }
    
    public void setDescription(final String description) {
        this.m_description = description;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public GameDateConst getRegistrationDate() {
        return this.m_registrationDate;
    }
    
    public void setRegistrationDate(final GameDateConst registrationDate) {
        this.m_registrationDate = registrationDate;
    }
    
    public void setLeader(final long newLeader) {
        this.m_id = newLeader;
    }
    
    public ImmutableList<PartyOccupation> getOccupations() {
        return (ImmutableList<PartyOccupation>)ImmutableList.copyOf((Collection)this.m_occupations);
    }
    
    public boolean hasOccupation(final PartyOccupation occupation) {
        return this.m_occupations.contains(occupation);
    }
    
    public byte[] serialize(final boolean withOccupations) {
        return this.doSerialize(withOccupations).toArray();
    }
    
    protected ByteArray doSerialize(final boolean withOccupations) {
        final ByteArray bb = new ByteArray();
        bb.putLong(this.m_id);
        bb.putLong(this.m_registrationDate.toLong());
        if (withOccupations) {
            bb.putInt(this.m_occupations.size());
            for (int i = 0, size = this.m_occupations.size(); i < size; ++i) {
                final PartyOccupation occupation = this.m_occupations.get(i);
                bb.putLong(occupation.getId());
            }
        }
        else {
            bb.putInt(0);
        }
        bb.put(this.m_mood.getId());
        final byte[] encodedString = StringUtils.toUTF8(this.m_description);
        bb.putShort((short)encodedString.length);
        bb.put(encodedString);
        return bb;
    }
    
    public boolean isValid(final SearchParameters params) {
        final PartyMood mood = params.getMood();
        return (mood == PartyMood.NONE || this.m_mood == mood) && this.hasOccupation(params.getPartyOccupation());
    }
    
    @Override
    public String toString() {
        return "AbstractPartyRequester{m_id=" + this.m_id + ", m_registrationDate=" + this.m_registrationDate + ", m_occupations=" + this.m_occupations + ", m_mood=" + this.m_mood + ", m_description='" + this.m_description + '\'' + '}';
    }
}
