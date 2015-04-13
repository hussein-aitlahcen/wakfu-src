package com.ankamagames.wakfu.common.game.xp.modifications;

import java.util.*;
import java.nio.*;

public class PlayerXpModification implements Iterable<SkillOrSpellXpModification>
{
    private final long m_playerId;
    private final XpModification m_playerXpModification;
    private final List<SkillOrSpellXpModification> m_skillOrSpellXpModifications;
    
    public static PlayerXpModification none(final long playerId) {
        return new PlayerXpModification(playerId, XpModification.NONE) {
            @Override
            public void addSkillOrSpellXpModification(final SkillOrSpell skillOrSpell, final XpModification xpModification) {
            }
        };
    }
    
    public PlayerXpModification(final long playerId, final XpModification playerXpModification) {
        super();
        this.m_skillOrSpellXpModifications = new ArrayList<SkillOrSpellXpModification>();
        this.m_playerId = playerId;
        this.m_playerXpModification = playerXpModification;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public XpModification getPlayerXpModification() {
        return this.m_playerXpModification;
    }
    
    public int size() {
        return this.m_skillOrSpellXpModifications.size();
    }
    
    @Override
    public Iterator<SkillOrSpellXpModification> iterator() {
        return this.m_skillOrSpellXpModifications.iterator();
    }
    
    int serializedSize() {
        return 19 + this.m_skillOrSpellXpModifications.size() * 15;
    }
    
    void write(final ByteBuffer buffer) {
        buffer.putLong(this.m_playerId);
        PlayerXpModificationCollection.writeXpModification(this.m_playerXpModification, buffer);
        buffer.put((byte)this.m_skillOrSpellXpModifications.size());
        for (int i = 0; i < this.m_skillOrSpellXpModifications.size(); ++i) {
            final SkillOrSpellXpModification skillOrSpellXpModification = this.m_skillOrSpellXpModifications.get(i);
            skillOrSpellXpModification.write(buffer);
        }
    }
    
    static PlayerXpModification deserialize(final ByteBuffer buffer) {
        final long playerId = buffer.getLong();
        final XpModification xpModification = PlayerXpModificationCollection.readXpModification(buffer);
        return new PlayerXpModification(playerId, xpModification).read(buffer);
    }
    
    private PlayerXpModification read(final ByteBuffer buffer) {
        final byte numXpModifications = buffer.get();
        for (int i = 0; i < numXpModifications; ++i) {
            this.m_skillOrSpellXpModifications.add(SkillOrSpellXpModification.deserialize(buffer));
        }
        return this;
    }
    
    public void addSkillOrSpellXpModification(final SkillOrSpell skillOrSpell, final XpModification xpModification) {
        this.m_skillOrSpellXpModifications.add(new SkillOrSpellXpModification(skillOrSpell, xpModification));
    }
}
