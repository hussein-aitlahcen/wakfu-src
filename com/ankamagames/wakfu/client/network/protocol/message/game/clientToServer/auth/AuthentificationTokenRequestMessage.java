package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.auth;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public class AuthentificationTokenRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = false;
    private final long m_address;
    private final String m_lang;
    
    public AuthentificationTokenRequestMessage(final long address) {
        super();
        this.m_address = address;
        this.m_lang = null;
    }
    
    public AuthentificationTokenRequestMessage(final long address, final String lang) {
        super();
        this.m_address = address;
        this.m_lang = lang;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray array = new ByteArray();
        array.putLong(this.m_address);
        if (this.m_lang != null) {
            final byte[] bytes = StringUtils.toUTF8(this.m_lang);
            array.putShort((short)bytes.length);
            array.put(bytes);
        }
        else {
            array.putShort((short)0);
        }
        return this.addClientHeader((byte)2, array.toArray());
    }
    
    @Override
    public int getId() {
        return 2079;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AuthentificationTokenRequestMessage.class);
    }
}
