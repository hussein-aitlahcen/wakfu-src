package com.ankamagames.wakfu.common.game.havenWorld.buff;

import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class UnlockGuildStorageCompartmentDefinition implements HavenWorldBuffDefinition
{
    private final GuildStorageCompartmentUnlockType m_type;
    private final byte m_level;
    
    UnlockGuildStorageCompartmentDefinition(final GuildStorageCompartmentUnlockType type, final int level) {
        super();
        this.m_type = type;
        this.m_level = MathHelper.ensureByte(level);
    }
    
    public GuildStorageCompartmentUnlockType getType() {
        return this.m_type;
    }
    
    public byte getLevel() {
        return this.m_level;
    }
}
