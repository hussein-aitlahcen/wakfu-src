package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.vault;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.vault.*;
import java.nio.*;

public class VaultConsultResultMessage extends InputOnlyProxyMessage
{
    private RawVault m_rawVault;
    private VaultUpgrade m_upgrade;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        (this.m_rawVault = new RawVault()).unserialize(bb);
        this.m_upgrade = VaultUpgrade.getFromId(bb.get());
        return true;
    }
    
    @Override
    public int getId() {
        return 15671;
    }
    
    public RawVault getRawVault() {
        return this.m_rawVault;
    }
    
    public VaultUpgrade getUpgrade() {
        return this.m_upgrade;
    }
}
