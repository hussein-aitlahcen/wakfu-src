package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class PSEnterResultMessage extends InputOnlyProxyMessage
{
    private boolean m_successful;
    private RawDimensionalBagForClient m_serializedPersonalSpace;
    private boolean m_yourDimBag;
    private boolean m_fleaAllowed;
    private boolean m_onMarket;
    private int m_partitionNationId;
    
    public PSEnterResultMessage() {
        super();
        this.m_fleaAllowed = true;
        this.m_onMarket = false;
    }
    
    public boolean isSuccessful() {
        return this.m_successful;
    }
    
    public RawDimensionalBagForClient getSerializedPersonalSpace() {
        return this.m_serializedPersonalSpace;
    }
    
    public int getPartitionNationId() {
        return this.m_partitionNationId;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_successful = (buffer.get() == 1);
        this.m_yourDimBag = (buffer.get() == 1);
        this.m_partitionNationId = buffer.getInt();
        if (this.m_successful) {
            if (!this.m_yourDimBag) {
                (this.m_serializedPersonalSpace = new RawDimensionalBagForClient()).unserialize(buffer);
            }
            else {
                this.m_onMarket = (buffer.get() == 1);
                this.m_fleaAllowed = (buffer.get() == 1);
                this.m_serializedPersonalSpace = null;
            }
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 10004;
    }
    
    public boolean isYourDimBag() {
        return this.m_yourDimBag;
    }
    
    public boolean isOnMarket() {
        return this.m_onMarket;
    }
    
    public boolean isFleaAllowed() {
        return this.m_fleaAllowed;
    }
}
