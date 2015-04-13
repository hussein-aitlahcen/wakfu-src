package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class ShortcutsModificationMessage extends OutputOnlyProxyMessage
{
    private TIntArrayList m_toRemove;
    private TIntObjectHashMap<ShortCutItem> m_toAdd;
    
    public ShortcutsModificationMessage() {
        super();
        this.m_toRemove = new TIntArrayList();
        this.m_toAdd = new TIntObjectHashMap<ShortCutItem>();
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(((this.m_toRemove != null) ? this.m_toRemove.size() : 0) * 4 + 2 + 21 * ((this.m_toAdd != null) ? this.m_toAdd.size() : 0) + 2);
        if (this.m_toRemove != null) {
            buffer.putShort((short)this.m_toRemove.size());
            for (int i = 0; i < this.m_toRemove.size(); ++i) {
                buffer.putInt(this.m_toRemove.get(i));
            }
            this.m_toRemove = null;
        }
        else {
            buffer.putShort((short)0);
        }
        if (this.m_toAdd != null) {
            buffer.putShort((short)this.m_toAdd.size());
            final TIntObjectIterator<ShortCutItem> it = this.m_toAdd.iterator();
            while (it.hasNext()) {
                it.advance();
                buffer.putInt(it.key());
                final RawShortcut rawShortcut = new RawShortcut();
                it.value().toRaw(rawShortcut);
                rawShortcut.serialize(buffer);
            }
            this.m_toAdd = null;
        }
        else {
            buffer.putShort((short)0);
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 9101;
    }
    
    public void addToAdds(final byte type, final byte bar, final byte position, final ShortCutItem item) {
        final int key = this.getKey(type, bar, position);
        this.m_toAdd.put(key, item);
    }
    
    public void addToRemoves(final byte type, final byte bar, final byte position) {
        this.m_toRemove.add(this.getKey(type, bar, position));
    }
    
    private int getKey(final byte type, final byte bar, final byte position) {
        return (type << 16) + (bar << 8) + position;
    }
}
