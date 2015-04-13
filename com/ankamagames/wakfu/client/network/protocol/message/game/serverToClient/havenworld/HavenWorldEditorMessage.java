package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class HavenWorldEditorMessage extends InputOnlyProxyMessage
{
    private HavenWorld m_world;
    private HavenWorldError m_error;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_error = HavenWorldError.valueOf(bb.getInt());
        this.m_world = HavenWorldSerializer.unserialize(bb);
        return true;
    }
    
    public HavenWorld getWorld() {
        return this.m_world;
    }
    
    public HavenWorldError getError() {
        return this.m_error;
    }
    
    @Override
    public int getId() {
        return 5516;
    }
}
