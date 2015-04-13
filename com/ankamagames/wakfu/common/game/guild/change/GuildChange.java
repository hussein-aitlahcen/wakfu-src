package com.ankamagames.wakfu.common.game.guild.change;

import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;

public interface GuildChange
{
    byte[] serialize();
    
    void unSerialize(ByteBuffer p0);
    
    void compute(GuildController p0);
    
    GuildChangeType getType();
}
