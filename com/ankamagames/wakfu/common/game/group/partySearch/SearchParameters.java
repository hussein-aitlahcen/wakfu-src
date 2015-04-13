package com.ankamagames.wakfu.common.game.group.partySearch;

import com.ankamagames.wakfu.common.datas.Breed.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class SearchParameters
{
    private PartyOccupation m_partyOccupation;
    private short m_minLevel;
    private short m_maxLevel;
    private Breed m_breed;
    private PartyMood m_mood;
    private PartyRole m_role;
    
    public SearchParameters() {
        super();
        this.m_maxLevel = 32767;
        this.m_breed = AvatarBreed.NONE;
        this.m_mood = PartyMood.NONE;
        this.m_role = PartyRole.NONE;
    }
    
    public SearchParameters(final ByteBuffer bb) {
        super();
        this.m_maxLevel = 32767;
        this.m_breed = AvatarBreed.NONE;
        this.m_mood = PartyMood.NONE;
        this.m_role = PartyRole.NONE;
        this.m_mood = PartyMood.getFromId(bb.get());
        this.m_role = PartyRole.getFromId(bb.get());
        this.m_breed = AvatarBreed.getBreedFromId(bb.getShort());
        this.m_minLevel = bb.getShort();
        this.m_maxLevel = bb.getShort();
        this.m_partyOccupation = PartyOccupationManager.INSTANCE.getPartyOccupation(bb.getLong());
    }
    
    public SearchParameters(final PartyOccupation partyOccupation, final short minLevel, final short maxLevel, final Breed breed, final PartyMood mood, final PartyRole role) {
        super();
        this.m_maxLevel = 32767;
        this.m_breed = AvatarBreed.NONE;
        this.m_mood = PartyMood.NONE;
        this.m_role = PartyRole.NONE;
        this.m_partyOccupation = partyOccupation;
        this.m_minLevel = minLevel;
        this.m_maxLevel = maxLevel;
        this.m_breed = breed;
        this.m_mood = mood;
        this.m_role = role;
    }
    
    public PartyOccupation getPartyOccupation() {
        return this.m_partyOccupation;
    }
    
    public void setPartyOccupation(final PartyOccupation partyOccupation) {
        this.m_partyOccupation = partyOccupation;
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public void setMinLevel(final short minLevel) {
        this.m_minLevel = minLevel;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public void setMaxLevel(final short maxLevel) {
        this.m_maxLevel = maxLevel;
    }
    
    public Breed getBreed() {
        return this.m_breed;
    }
    
    public void setBreed(final Breed breed) {
        this.m_breed = breed;
    }
    
    public PartyMood getMood() {
        return this.m_mood;
    }
    
    public void setMood(final PartyMood mood) {
        this.m_mood = mood;
    }
    
    public PartyRole getRole() {
        return this.m_role;
    }
    
    public void setRole(final PartyRole role) {
        this.m_role = role;
    }
    
    public byte[] serialize() {
        final ByteArray ba = new ByteArray();
        ba.put(this.m_mood.getId());
        ba.put(this.m_role.getId());
        ba.putShort(this.m_breed.getBreedId());
        ba.putShort(this.m_minLevel);
        ba.putShort(this.m_maxLevel);
        ba.putLong(this.m_partyOccupation.getId());
        return ba.toArray();
    }
    
    @Override
    public String toString() {
        return "SearchParameters{m_partyOccupation=" + this.m_partyOccupation.getId() + ", m_minLevel=" + this.m_minLevel + ", m_maxLevel=" + this.m_maxLevel + ", m_breed=" + this.m_breed.getBreedId() + ", m_mood=" + this.m_mood.getId() + ", m_role=" + this.m_role.getId() + '}';
    }
}
