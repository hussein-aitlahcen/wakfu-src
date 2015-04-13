package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.pvp;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import java.util.*;
import java.nio.*;

public class NationPvpLadderPageResponse extends InputOnlyProxyMessage
{
    private int m_count;
    private int m_pageNum;
    private final List<PvpLadderEntry> m_entries;
    
    public NationPvpLadderPageResponse() {
        super();
        this.m_entries = new ArrayList<PvpLadderEntry>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_count = bb.getInt();
        this.m_pageNum = bb.getInt();
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            this.m_entries.add(new PvpLadderEntry(bb));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 20404;
    }
    
    public int getCount() {
        return this.m_count;
    }
    
    public int getPageNum() {
        return this.m_pageNum;
    }
    
    public List<PvpLadderEntry> getEntries() {
        return this.m_entries;
    }
}
