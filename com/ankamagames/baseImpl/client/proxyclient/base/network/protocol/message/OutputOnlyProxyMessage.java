package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

public abstract class OutputOnlyProxyMessage extends ClientProxyMessage
{
    @Override
    public boolean decode(final byte[] rawDatas) {
        throw new UnsupportedOperationException(this.getClass().getName() + " ne peut \u00eatre d\u00e9cod\u00e9");
    }
}
