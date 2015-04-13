package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class FriendListMessage extends InputOnlyProxyMessage
{
    private final ArrayList<FriendInformation> m_friendInformationList;
    
    public FriendListMessage() {
        super();
        this.m_friendInformationList = new ArrayList<FriendInformation>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final int listSize = bb.get() & 0xFF;
        this.m_friendInformationList.clear();
        this.m_friendInformationList.ensureCapacity(listSize);
        for (int i = 0; i < listSize; ++i) {
            final short taille = bb.getShort();
            final byte[] data = new byte[taille];
            bb.get(data);
            final FriendInformation finfo = new FriendInformation();
            finfo.unserialize(data);
            this.m_friendInformationList.add(finfo);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 3144;
    }
    
    public Iterable<FriendInformation> getFriendInformationList() {
        return this.m_friendInformationList;
    }
    
    public static class FriendInformation
    {
        public String name;
        public String characterName;
        public String commentary;
        public boolean notify;
        public long userId;
        public short breedId;
        public byte sex;
        public long xp;
        
        public void unserialize(final byte[] data) {
            final ByteBuffer bb = ByteBuffer.wrap(data);
            final byte[] name = new byte[bb.get() & 0xFF];
            bb.get(name);
            this.name = StringUtils.fromUTF8(name);
            final byte[] characterName = new byte[bb.get() & 0xFF];
            bb.get(characterName);
            this.characterName = StringUtils.fromUTF8(characterName);
            final byte[] commentary = new byte[bb.get() & 0xFF];
            bb.get(commentary);
            this.commentary = StringUtils.fromUTF8(commentary);
            this.notify = (bb.get() == 1);
            this.userId = bb.getLong();
            this.breedId = bb.getShort();
            this.sex = bb.get();
            this.xp = bb.getLong();
        }
    }
}
