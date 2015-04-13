package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.krosmoz;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;

public class KrosmozFigureAddResultMessage extends InputOnlyProxyMessage
{
    private boolean m_ok;
    private KrosmozFigure m_figure;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_ok = (buffer.get() != 0);
        if (this.m_ok) {
            this.m_figure = KrosmozFigureHelper.unserializeKrosmozFigure(buffer);
        }
        return true;
    }
    
    public boolean isOk() {
        return this.m_ok;
    }
    
    public KrosmozFigure getFigure() {
        return this.m_figure;
    }
    
    @Override
    public int getId() {
        return 14013;
    }
}
