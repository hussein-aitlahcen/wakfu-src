package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public class GroupClientInvitationRequestMessage extends OutputOnlyProxyMessage
{
    public static final byte PLAYER_BY_NAME = 0;
    public static final byte PLAYER_BY_ID = 1;
    public boolean m_sendToGlobalServer;
    private byte m_groupType;
    private long m_groupId;
    private String m_requestedPlayerName;
    private long m_requestedPlayerId;
    private boolean m_fromPartySearch;
    private long m_occupationId;
    
    public GroupClientInvitationRequestMessage() {
        super();
        this.m_sendToGlobalServer = true;
        this.m_fromPartySearch = false;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray bb = new ByteArray();
        bb.put(this.m_groupType);
        bb.putLong(this.m_groupId);
        bb.put((byte)(this.m_fromPartySearch ? 1 : 0));
        bb.putLong(this.m_occupationId);
        if (this.m_requestedPlayerName != null) {
            final byte[] fn = StringUtils.toUTF8(this.m_requestedPlayerName);
            bb.put((byte)0);
            bb.put((byte)fn.length);
            bb.put(fn);
        }
        else {
            bb.put((byte)1);
            bb.putLong(this.m_requestedPlayerId);
        }
        if (this.m_sendToGlobalServer) {
            return this.addClientHeader((byte)6, bb.toArray());
        }
        return this.addClientHeader((byte)2, bb.toArray());
    }
    
    @Override
    public int getId() {
        return 501;
    }
    
    public void setGroupType(final byte groupType) {
        this.m_groupType = groupType;
    }
    
    public void setRequestedPlayerName(final String requestedPlayerName) {
        this.m_requestedPlayerName = requestedPlayerName;
        this.m_requestedPlayerId = -1L;
    }
    
    public void setRequestedPlayerId(final long requestedPlayerId) {
        this.m_requestedPlayerId = requestedPlayerId;
        this.m_requestedPlayerName = null;
    }
    
    public void setOccupationId(final long occupationId) {
        this.m_occupationId = occupationId;
    }
    
    public void setSendToGlobalServer(final boolean sendToGlobalServer) {
        this.m_sendToGlobalServer = sendToGlobalServer;
    }
    
    public void setGroupId(final long groupId) {
        this.m_groupId = groupId;
    }
    
    public void setFromPartySearch(final boolean fromPartySearch) {
        this.m_fromPartySearch = fromPartySearch;
    }
}
