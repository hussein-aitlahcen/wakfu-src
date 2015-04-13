package com.ankamagames.wakfu.common.game.xp.modifications;

import java.nio.*;

public class SkillOrSpellXpModification
{
    private final SkillOrSpell m_subject;
    private final XpModification m_xpModification;
    static final int SERIALIZED_SIZE = 15;
    
    SkillOrSpellXpModification(final SkillOrSpell subject, final XpModification xpModification) {
        super();
        this.m_subject = subject;
        this.m_xpModification = xpModification;
    }
    
    public SkillOrSpell getSubject() {
        return this.m_subject;
    }
    
    public XpModification getXpModification() {
        return this.m_xpModification;
    }
    
    void write(final ByteBuffer buffer) {
        writeSkillOrSpell(this.m_subject, buffer);
        PlayerXpModificationCollection.writeXpModification(this.m_xpModification, buffer);
    }
    
    static SkillOrSpellXpModification deserialize(final ByteBuffer buffer) {
        final SkillOrSpell subject = readSkillOrSpell(buffer);
        final XpModification xpModification = PlayerXpModificationCollection.readXpModification(buffer);
        return new SkillOrSpellXpModification(subject, xpModification);
    }
    
    private static void writeSkillOrSpell(final SkillOrSpell skillOrSpell, final ByteBuffer buffer) {
        buffer.put((byte)(skillOrSpell.isSkill() ? 1 : 0));
        buffer.putInt(skillOrSpell.getRefId());
    }
    
    private static SkillOrSpell readSkillOrSpell(final ByteBuffer buffer) {
        final boolean isSkill = buffer.get() == 1;
        final int refId = buffer.getInt();
        return new SkillOrSpell(isSkill, refId);
    }
}
