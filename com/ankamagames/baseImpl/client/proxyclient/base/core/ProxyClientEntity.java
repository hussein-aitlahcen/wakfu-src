package com.ankamagames.baseImpl.client.proxyclient.base.core;

import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

public abstract class ProxyClientEntity extends FrameworkEntity
{
    private byte[] m_clientIp;
    private NetworkEntity m_networkEntity;
    private byte m_disconnectionReason;
    
    public ProxyClientEntity() {
        super();
        this.pushFrame(new NetBasicsFrame(this));
    }
    
    public NetworkEntity getNetworkEntity() {
        return this.m_networkEntity;
    }
    
    public void setNetworkEntity(final NetworkEntity networkEntity) {
        this.m_networkEntity = networkEntity;
    }
    
    public byte[] getClientIp() {
        return this.m_clientIp;
    }
    
    public void setClientIp(final byte[] clientIp) {
        this.m_clientIp = clientIp;
    }
    
    public byte getDisconnectionReason() {
        return this.m_disconnectionReason;
    }
    
    public void setDisconnectionReason(final byte disconnectionReason) {
        ProxyClientEntity.m_logger.info((Object)("Raison de la d\u00e9connection de l'entit\u00e9 " + this.getId() + " : " + disconnectionReason));
        this.m_disconnectionReason = disconnectionReason;
    }
    
    public void partialCleanUp() {
        this.removeAllFrames();
        this.pushFrame(new NetBasicsFrame(this));
    }
    
    public void cleanUp() {
        ProxyClientEntity.m_logger.info((Object)"cleanUp() de la ProxyClientEntity, on fait un setTicket \u00e0 null");
        this.setDisconnectionReason((byte)0);
        this.partialCleanUp();
    }
    
    public abstract void onQueueFinished();
    
    public abstract void onQueryResult(final int p0);
    
    public abstract void onInvalidClientVersion(final byte[] p0);
}
