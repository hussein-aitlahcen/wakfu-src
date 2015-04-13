package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.restat;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.nio.*;

public class SpellsRestatNeededMessage extends InputOnlyProxyMessage
{
    private boolean m_globalRestat;
    private TObjectLongHashMap<Elements> m_xpPerElement;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_globalRestat = (bb.get() == 1);
        final int elementsCount = bb.get();
        if (elementsCount == 0) {
            return true;
        }
        this.m_xpPerElement = new TObjectLongHashMap<Elements>();
        for (int i = 0; i < elementsCount; ++i) {
            final byte elementId = bb.get();
            final Elements element = Elements.getElementFromId(elementId);
            if (element == null) {
                SpellsRestatNeededMessage.m_logger.error((Object)("Unable to find Element of id " + elementId));
                bb.getLong();
            }
            else {
                this.m_xpPerElement.put(element, bb.getLong());
            }
        }
        return true;
    }
    
    public boolean isGlobalRestat() {
        return this.m_globalRestat;
    }
    
    public TObjectLongHashMap<Elements> getXpPerElement() {
        return this.m_xpPerElement;
    }
    
    @Override
    public int getId() {
        return 13200;
    }
    
    @Override
    public String toString() {
        return "SpellsRestatNeededMessage{m_globalRestat=" + this.m_globalRestat + '}';
    }
}
