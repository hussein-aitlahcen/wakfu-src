package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import org.apache.log4j.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ModerationCommandMessage extends OutputOnlyProxyMessage
{
    private static final Logger m_logger;
    protected byte m_serverId;
    protected short m_command;
    protected TByteArrayList m_datas;
    
    public ModerationCommandMessage() {
        super();
        this.m_datas = new TByteArrayList(64);
    }
    
    public void setServerId(final byte serverId) {
        this.m_serverId = serverId;
    }
    
    @Override
    public byte[] encode() {
        if (this.m_datas.size() > 65535) {
            ModerationCommandMessage.m_logger.error((Object)("Impossible d'encoder un message de taille " + this.m_datas.size() + " > 0xFFFF"));
            return null;
        }
        final ByteBuffer bb = ByteBuffer.allocate(4 + this.m_datas.size());
        bb.putShort(this.m_command);
        bb.putShort((short)this.m_datas.size());
        bb.put(this.m_datas.toNativeArray());
        return this.addClientHeader(this.m_serverId, bb.array());
    }
    
    @Override
    public int getId() {
        return 104;
    }
    
    public void setCommand(final short command) {
        this.m_command = command;
    }
    
    public void addParameter(final Object o) {
        if (o instanceof Integer) {
            this.addIntParameter((int)o);
            return;
        }
        if (o instanceof Byte) {
            this.addByteParameter((byte)o);
            return;
        }
        if (o instanceof Long) {
            this.addLongParameter((long)o);
            return;
        }
        if (o instanceof Float) {
            this.addFloatParameter((float)o);
            return;
        }
        if (o instanceof Short) {
            this.addShortParameter((short)o);
            return;
        }
        if (o instanceof Boolean) {
            this.addBooleanParameter((boolean)o);
            return;
        }
        if (o instanceof String) {
            this.addStringParameter((String)o);
            return;
        }
        throw new IllegalArgumentException("type " + o.getClass().getSimpleName() + " non ajoutable au message");
    }
    
    public void addStringParameter(final String p) {
        final byte[] encoded = StringUtils.toUTF8(p);
        if (encoded != null && encoded.length <= 65535) {
            this.addShortParameter((short)encoded.length);
            this.m_datas.add(encoded);
        }
        else {
            ModerationCommandMessage.m_logger.error((Object)"Impossible d'ajouter le param\u00e8tre de type cha\u00eene.");
        }
    }
    
    public void addIntParameter(final int p) {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(p);
        this.m_datas.add(bb.array());
    }
    
    public void addBooleanParameter(final boolean p) {
        this.m_datas.add((byte)(p ? 1 : 0));
    }
    
    public void addByteParameter(final byte p) {
        this.m_datas.add(p);
    }
    
    public void addLongParameter(final long l) {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(l);
        this.m_datas.add(bb.array());
    }
    
    public void addShortParameter(final short s) {
        final ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort(s);
        this.m_datas.add(bb.array());
    }
    
    public void addFloatParameter(final float f) {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putFloat(f);
        this.m_datas.add(bb.array());
    }
    
    static {
        m_logger = Logger.getLogger((Class)ModerationCommandMessage.class);
    }
}
