package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ModerationCommandResultNewMessage extends InputOnlyProxyMessage
{
    protected short m_command;
    protected ByteBuffer m_datas;
    
    public short getCommand() {
        return this.m_command;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 4, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_command = bb.getShort();
        final int paramSize = bb.getShort() & 0xFFFF;
        if (!this.checkMessageSize(rawDatas.length - 4, paramSize, true)) {
            return false;
        }
        final byte[] datas = new byte[paramSize];
        bb.get(datas);
        this.m_datas = ByteBuffer.wrap(datas);
        return true;
    }
    
    @Override
    public int getId() {
        return 109;
    }
    
    public String extractStringParameter() {
        if (this.m_datas.remaining() < 2) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractStringParameter() impossible d'extraire la taille de la cha\u00eene : " + this.m_datas.remaining() + " bytes restant dans le buffer (2 requis)"));
            return null;
        }
        final int length = this.m_datas.getShort() & 0xFFFF;
        final byte[] encoded = new byte[length];
        if (this.m_datas.remaining() < length) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractStringParameter() impossible d'extraire la cha\u00eene : " + this.m_datas.remaining() + " bytes restant dans le buffer (" + length + " requis)"));
            return null;
        }
        this.m_datas.get(encoded);
        return StringUtils.fromUTF8(encoded);
    }
    
    public int extractIntParameter() {
        if (this.m_datas.remaining() < 4) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractIntParameter() impossible d'extraire l'Integer : " + this.m_datas.remaining() + " bytes restant dans le buffer (4 requis)"));
            return 0;
        }
        return this.m_datas.getInt();
    }
    
    public boolean checkRemainingBytes(final int bytesCount) {
        return this.m_datas.remaining() >= bytesCount;
    }
    
    public boolean extractBooleanParameter() {
        if (this.m_datas.remaining() < 1) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractBooleanParameter() impossible d'extraire le Boolean : " + this.m_datas.remaining() + " bytes restant dans le buffer (1 requis)"));
            return false;
        }
        return this.m_datas.get() != 0;
    }
    
    public byte extractByteParameter() {
        if (this.m_datas.remaining() < 1) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractByteParameter() impossible d'extraire le Byte : " + this.m_datas.remaining() + " bytes restant dans le buffer (1 requis)"));
            return 0;
        }
        return this.m_datas.get();
    }
    
    public long extractLongParameter() {
        if (this.m_datas.remaining() < 8) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractLongParameter() impossible d'extraire le Long : " + this.m_datas.remaining() + " bytes restant dans le buffer (8 requis)"));
            return 0L;
        }
        return this.m_datas.getLong();
    }
    
    public short extractShortParameter() {
        if (this.m_datas.remaining() < 2) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractShortParameter() impossible d'extraire le Short : " + this.m_datas.remaining() + " bytes restant dans le buffer (2 requis)"));
            return 0;
        }
        return this.m_datas.getShort();
    }
    
    public float extractFloatParameter() {
        if (this.m_datas.remaining() < 4) {
            ModerationCommandResultNewMessage.m_logger.error((Object)("extractFloatParameter() impossible d'extraire le Float : " + this.m_datas.remaining() + " bytes restant dans le buffer (4 requis)"));
            return 0.0f;
        }
        return this.m_datas.getFloat();
    }
}
