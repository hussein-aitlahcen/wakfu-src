package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gift;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;

public class GiftInventoryRequestMessage extends OutputOnlyProxyMessage
{
    private Locale m_locale;
    
    @Override
    public byte[] encode() {
        final String language = this.m_locale.getLanguage();
        final byte[] rawLanguage = StringUtils.toUTF8(language);
        return this.addClientHeader((byte)3, rawLanguage);
    }
    
    public void setLocale(final Locale locale) {
        this.m_locale = locale;
    }
    
    @Override
    public int getId() {
        return 13001;
    }
}
