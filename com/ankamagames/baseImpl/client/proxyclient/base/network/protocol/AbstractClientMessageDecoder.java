package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public abstract class AbstractClientMessageDecoder implements WakfuMessageDecoder
{
    protected static Logger m_logger;
    
    @Override
    public Message decode(final ByteBuffer rawMessage) {
        rawMessage.mark();
        final int availableSize = rawMessage.remaining();
        if (availableSize < 2) {
            rawMessage.reset();
            return null;
        }
        final int msgSize = rawMessage.getShort() & 0xFFFF;
        if (msgSize < 4) {
            AbstractClientMessageDecoder.m_logger.error((Object)("D\u00e9codage impossible car taille trop petite (taille = " + msgSize + ", minimum = 6"));
            return null;
        }
        if (rawMessage.remaining() < 2) {
            rawMessage.reset();
            return null;
        }
        final short msgType = rawMessage.getShort();
        if (rawMessage.remaining() < msgSize - 4) {
            rawMessage.reset();
            return null;
        }
        final int msgEndPosition = rawMessage.position() + msgSize - 4;
        Message msg = null;
        switch (msgType) {
            case 102: {
                msg = new ConsoleAdminCommandResultMessage();
                break;
            }
            case 105: {
                msg = new ModerationCommandResultMessage();
                break;
            }
            case 109: {
                msg = new ModerationCommandResultNewMessage();
                break;
            }
            case 103: {
                msg = new DefaultResultsMessage();
                break;
            }
            case 106: {
                msg = new SystemNotificationMessage();
                break;
            }
            case 108: {
                msg = new PongMessage();
                break;
            }
            case 110: {
                msg = new ClientIpMessage();
                break;
            }
            case 111: {
                msg = new ServerPingMessage();
                break;
            }
            case 200: {
                msg = new InteractiveElementSpawnMessage();
                break;
            }
            case 202: {
                msg = new InteractiveElementUpdateMessage();
                break;
            }
            case 204: {
                msg = new DynamicInteractiveElementSpawnMessage();
                break;
            }
            case 206: {
                msg = new InteractiveElementDespawnMessage();
                break;
            }
            case 8: {
                msg = new ClientVersionResultMessage();
                break;
            }
            case 6: {
                msg = new ForcedDisconnectionReasonMessage();
                break;
            }
            case 9: {
                msg = new ProxyRelayErrorMessage();
                break;
            }
            default: {
                msg = this.createMessageFromType(msgType);
                break;
            }
        }
        if (msg == null) {
            rawMessage.position(msgEndPosition);
            AbstractClientMessageDecoder.m_logger.error((Object)("Le message type=" + msgType + " inconnu du d\u00e9codeur !"));
        }
        if (msg != null && rawMessage.remaining() != 0) {
            final byte[] messageDatas = new byte[msgSize - 4];
            rawMessage.get(messageDatas);
            try {
                msg.decode(messageDatas);
            }
            catch (Throwable e) {
                AbstractClientMessageDecoder.m_logger.error((Object)"Exception", e);
            }
        }
        return msg;
    }
    
    protected abstract Message createMessageFromType(final int p0);
    
    static {
        AbstractClientMessageDecoder.m_logger = Logger.getLogger((Class)AbstractClientMessageDecoder.class);
    }
}
