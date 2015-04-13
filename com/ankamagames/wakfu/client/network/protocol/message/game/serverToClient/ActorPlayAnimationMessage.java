package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class ActorPlayAnimationMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private String m_linkAnimation;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buff.getLong();
        final byte[] adata = new byte[buff.getInt()];
        buff.get(adata);
        this.m_linkAnimation = StringUtils.fromUTF8(adata);
        return true;
    }
    
    @Override
    public int getId() {
        return 9200;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public String getLinkAnimation() {
        return this.m_linkAnimation;
    }
}
