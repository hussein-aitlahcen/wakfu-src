package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.krosmoz;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.krozmoz.*;

public class KrosmozFigureListResultMessage extends InputOnlyProxyMessage
{
    private ArrayList<KrosmozFigure> m_figures;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final short numFigures = buffer.getShort();
        this.m_figures = new ArrayList<KrosmozFigure>(numFigures);
        for (int i = 0; i < numFigures; ++i) {
            final KrosmozFigure figure = KrosmozFigureHelper.unserializeKrosmozFigure(buffer);
            this.m_figures.add(figure);
        }
        return true;
    }
    
    public ArrayList<KrosmozFigure> getFigures() {
        return this.m_figures;
    }
    
    @Override
    public int getId() {
        return 14011;
    }
}
