package com.ankamagames.wakfu.common.game.group.partySearch;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class PartyPlayerDefinition
{
    private final long m_id;
    private short m_breedId;
    private PartyRole m_role;
    private short m_level;
    private String m_name;
    private boolean m_isCompanion;
    
    public PartyPlayerDefinition(final long id, final PartyRole role, final short breedId, final short level) {
        super();
        this.m_id = id;
        this.m_breedId = breedId;
        this.m_role = role;
        this.m_level = level;
    }
    
    public PartyPlayerDefinition(final ByteBuffer bb) {
        super();
        this.m_id = bb.getLong();
        this.m_role = PartyRole.getFromId(bb.get());
        this.m_breedId = bb.getShort();
        this.m_level = bb.getShort();
        if (bb.get() != 0) {
            final byte[] bytes = new byte[bb.get()];
            bb.get(bytes);
            this.m_name = StringUtils.fromUTF8(bytes);
        }
    }
    
    public byte[] serialize() {
        return this.serialize(false);
    }
    
    public byte[] serialize(final boolean withName) {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_id);
        ba.put(this.m_role.getId());
        ba.putShort(this.m_breedId);
        ba.putShort(this.m_level);
        ba.putBoolean(withName);
        if (withName) {
            final byte[] bytes = StringUtils.toUTF8(this.m_name);
            ba.put((byte)bytes.length);
            ba.put(bytes);
        }
        return ba.toArray();
    }
    
    public void setRole(final PartyRole role) {
        this.m_role = role;
    }
    
    public void setLevel(final short level) {
        this.m_level = level;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public PartyRole getRole() {
        return this.m_role;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public void setBreedId(final short breedId) {
        this.m_breedId = breedId;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public boolean isCompanion() {
        return this.m_isCompanion;
    }
    
    public void setCompanion(final boolean isCompanion) {
        this.m_isCompanion = isCompanion;
    }
    
    public void update(final PartyPlayerDefinition definition) {
        this.m_level = definition.m_level;
        this.m_role = definition.m_role;
        this.m_breedId = definition.m_breedId;
    }
    
    @Override
    public String toString() {
        return "PartyPlayerDefinition{m_id=" + this.m_id + ", m_breedId=" + this.m_breedId + ", m_role=" + this.m_role + ", m_level=" + this.m_level + '}';
    }
}
