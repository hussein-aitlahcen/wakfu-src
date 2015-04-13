package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class VisualAnimationMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_visualId;
    private int m_linkedItemRefId;
    private boolean m_endAnimation;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        this.m_visualId = bb.getInt();
        this.m_linkedItemRefId = bb.getInt();
        this.m_endAnimation = (bb.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 15720;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public int getLinkedItemRefId() {
        return this.m_linkedItemRefId;
    }
    
    public boolean isEndAnimation() {
        return this.m_endAnimation;
    }
}
