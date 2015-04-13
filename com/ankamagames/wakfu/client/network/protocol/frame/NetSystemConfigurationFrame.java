package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.config.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;

public class NetSystemConfigurationFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static final NetSystemConfigurationFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 2067: {
                final ClientSystemConfigurationMessage msg = (ClientSystemConfigurationMessage)message;
                SystemConfiguration.INSTANCE.unserialize(msg.getData());
                final boolean shopEnabled = SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.SHOP_ENABLED);
                PropertiesProvider.getInstance().setPropertyValue("isShopAvailable", shopEnabled);
                PropertiesProvider.getInstance().setPropertyValue("systemConfiguration", new SystemConfigurationView());
                return false;
            }
            case 2081: {
                final ClientSystemConfigurationUpdateValueMessage msg2 = (ClientSystemConfigurationUpdateValueMessage)message;
                final String key = msg2.getKey();
                final String value = msg2.getValue();
                final SystemConfigurationType systemConfigurationType = SystemConfigurationType.getByKey(key);
                if (systemConfigurationType == null) {
                    NetSystemConfigurationFrame.m_logger.error((Object)("Property inconnue : " + key));
                    return false;
                }
                SystemConfiguration.INSTANCE.changePropertyValue(systemConfigurationType, value);
                if (systemConfigurationType == SystemConfigurationType.SHOP_ENABLED) {
                    final boolean shopEnabled2 = SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.SHOP_ENABLED);
                    PropertiesProvider.getInstance().setPropertyValue("isShopAvailable", shopEnabled2);
                }
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
    
    static {
        m_logger = Logger.getLogger((Class)NetSystemConfigurationFrame.class);
        INSTANCE = new NetSystemConfigurationFrame();
    }
}
