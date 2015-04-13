package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

public abstract class InputOnlyProxyMessage extends ClientProxyMessage
{
    @Override
    public byte[] encode() {
        throw new UnsupportedOperationException(this.getClass().getName() + " ne peut \u00eatre encod\u00e9");
    }
}
