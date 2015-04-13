package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.*;

public class NetBasicsFrame implements MessageFrame
{
    private static Logger m_logger;
    private final ProxyClientEntity m_proxyClientEntity;
    
    public NetBasicsFrame(final ProxyClientEntity proxyClientEntity) {
        super();
        this.m_proxyClientEntity = proxyClientEntity;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 6: {
                final ForcedDisconnectionReasonMessage msg = (ForcedDisconnectionReasonMessage)message;
                this.m_proxyClientEntity.setDisconnectionReason(msg.getReason());
                return false;
            }
            case 8: {
                final ClientVersionResultMessage msg2 = (ClientVersionResultMessage)message;
                final boolean versionMatch = msg2.isVersionMatch();
                final byte[] serverVersion = msg2.getNeededVersion();
                if (!versionMatch) {
                    this.m_proxyClientEntity.onInvalidClientVersion(serverVersion);
                }
                return false;
            }
            case 110: {
                final ClientIpMessage msg3 = (ClientIpMessage)message;
                this.m_proxyClientEntity.setClientIp(msg3.getIp());
                return false;
            }
            case 102: {
                final ConsoleAdminCommandResultMessage msg4 = (ConsoleAdminCommandResultMessage)message;
                switch (msg4.getMessageType()) {
                    case 0: {
                        ConsoleManager.getInstance().trace(msg4.getMessage());
                        break;
                    }
                    case 1: {
                        ConsoleManager.getInstance().log(msg4.getMessage());
                        break;
                    }
                    case 2: {
                        ConsoleManager.getInstance().err(msg4.getMessage());
                        break;
                    }
                    default: {
                        NetBasicsFrame.m_logger.error((Object)("Type de message inconnu " + msg4.getMessageType()));
                        break;
                    }
                }
                return false;
            }
            case 105: {
                final ModerationCommandResultMessage msg5 = (ModerationCommandResultMessage)message;
                switch (msg5.getMessageType()) {
                    case 0: {
                        ConsoleManager.getInstance().trace(msg5.getMessage());
                        break;
                    }
                    case 1: {
                        ConsoleManager.getInstance().log(msg5.getMessage());
                        break;
                    }
                    case 2: {
                        ConsoleManager.getInstance().err(msg5.getMessage());
                        break;
                    }
                    case 3: {
                        ConsoleManager.getInstance().customTrace(msg5.getMessage(), msg5.getColor());
                        break;
                    }
                    default: {
                        NetBasicsFrame.m_logger.error((Object)("Type de message inconnu " + msg5.getMessageType()));
                        break;
                    }
                }
                return false;
            }
            case 103: {
                final DefaultResultsMessage msg6 = (DefaultResultsMessage)message;
                this.m_proxyClientEntity.onQueryResult(msg6.getQueryResultCode());
                return false;
            }
            case 111: {
                final ServerPingMessage msg7 = (ServerPingMessage)message;
                final byte architectureTarget = msg7.getArchitectureTarget();
                final ServerPongMessage pongMsg = new ServerPongMessage();
                pongMsg.setArchitecture(architectureTarget);
                this.m_proxyClientEntity.getNetworkEntity().sendMessage(pongMsg);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        NetBasicsFrame.m_logger = Logger.getLogger((Class)NetBasicsFrame.class);
    }
}
