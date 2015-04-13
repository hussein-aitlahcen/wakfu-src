package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.tax.*;
import java.nio.*;
import gnu.trove.*;

public class ProtectorChangeTaxRequestMessage extends OutputOnlyProxyMessage
{
    private int m_protectorId;
    private final TByteFloatHashMap m_taxChanges;
    
    public ProtectorChangeTaxRequestMessage() {
        super();
        this.m_taxChanges = new TByteFloatHashMap();
    }
    
    public void setProtectorId(final int protectorId) {
        this.m_protectorId = protectorId;
    }
    
    public void addTaxChange(final Tax tax) {
        this.m_taxChanges.put(tax.getContext().id, tax.getValue());
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(6 + 5 * this.m_taxChanges.size());
        buffer.putInt(this.m_protectorId);
        buffer.putShort((short)this.m_taxChanges.size());
        this.m_taxChanges.forEachEntry(new TByteFloatProcedure() {
            @Override
            public boolean execute(final byte context, final float value) {
                buffer.put(context);
                buffer.putFloat(value);
                return true;
            }
        });
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15321;
    }
}
