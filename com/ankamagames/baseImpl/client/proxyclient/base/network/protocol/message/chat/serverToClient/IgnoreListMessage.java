package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.utils.*;

public class IgnoreListMessage extends InputOnlyProxyMessage
{
    private final ArrayList<ObjectPair<Long, ObjectPair<String, String>>> m_ignoreList;
    
    public IgnoreListMessage() {
        super();
        this.m_ignoreList = new ArrayList<ObjectPair<Long, ObjectPair<String, String>>>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final int listSize = bb.get();
        final TIntObjectHashMap<ObjectPair<Long, String>> tempList = new TIntObjectHashMap<ObjectPair<Long, String>>();
        this.m_ignoreList.ensureCapacity(listSize);
        for (int i = 0; i < listSize; ++i) {
            final byte[] entryName = new byte[bb.get() & 0xFF];
            bb.get(entryName);
            final String name = StringUtils.fromUTF8(entryName);
            final long userId = bb.getLong();
            tempList.put(i, new ObjectPair<Long, String>(userId, name));
        }
        for (int i = 0; i < listSize; ++i) {
            final byte[] characterName = new byte[bb.get() & 0xFF];
            bb.get(characterName);
            final String name = StringUtils.fromUTF8(characterName);
            final ObjectPair<Long, String> tempObjectPair = tempList.get(i);
            this.m_ignoreList.add(new ObjectPair<Long, ObjectPair<String, String>>(tempObjectPair.getFirst(), new ObjectPair<String, String>(tempObjectPair.getSecond(), name)));
        }
        return true;
    }
    
    @Override
    public final int getId() {
        return 3146;
    }
    
    public final ArrayList<ObjectPair<Long, ObjectPair<String, String>>> getIgnoreList() {
        return this.m_ignoreList;
    }
}
