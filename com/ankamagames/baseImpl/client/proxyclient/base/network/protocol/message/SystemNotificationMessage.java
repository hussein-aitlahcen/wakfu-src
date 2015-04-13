package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class SystemNotificationMessage extends InputOnlyProxyMessage
{
    private byte m_notificationType;
    private ByteBuffer m_datas;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 1, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_notificationType = bb.get();
        final byte[] datas = new byte[rawDatas.length - 1];
        bb.get(datas);
        this.m_datas = ByteBuffer.wrap(datas);
        return true;
    }
    
    @Override
    public int getId() {
        return 106;
    }
    
    public byte getNotificationType() {
        return this.m_notificationType;
    }
    
    public String extractStringParameter() {
        if (this.m_datas.remaining() < 2) {
            SystemNotificationMessage.m_logger.error((Object)("extractStringParameter() impossible d'extraire la taille de la cha\u00eene : " + this.m_datas.remaining() + " bytes restant dans le buffer (2 requis)"));
            return null;
        }
        final int length = this.m_datas.get() & 0xFF;
        final byte[] encoded = new byte[length];
        if (this.m_datas.remaining() < length) {
            SystemNotificationMessage.m_logger.error((Object)("extractStringParameter() impossible d'extraire la cha\u00eene : " + this.m_datas.remaining() + " bytes restant dans le buffer (" + length + " requis)"));
            return null;
        }
        this.m_datas.get(encoded);
        return StringUtils.fromUTF8(encoded);
    }
    
    public int extractIntParameter() {
        if (this.m_datas.remaining() < 4) {
            SystemNotificationMessage.m_logger.error((Object)("extractIntParameter() impossible d'extraire l'Integer : " + this.m_datas.remaining() + " bytes restant dans le buffer (4 requis)"));
            return 0;
        }
        return this.m_datas.getInt();
    }
    
    public boolean extractBooleanParameter() {
        if (this.m_datas.remaining() < 1) {
            SystemNotificationMessage.m_logger.error((Object)("extractBooleanParameter() impossible d'extraire le Boolean : " + this.m_datas.remaining() + " bytes restant dans le buffer (1 requis)"));
            return false;
        }
        return this.m_datas.get() != 0;
    }
    
    public byte extractByteParameter() {
        if (this.m_datas.remaining() < 1) {
            SystemNotificationMessage.m_logger.error((Object)("extractByteParameter() impossible d'extraire le Byte : " + this.m_datas.remaining() + " bytes restant dans le buffer (1 requis)"));
            return 0;
        }
        return this.m_datas.get();
    }
}
