package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class AddItemToCompanionEquipmentErrorMessageRunner implements MessageRunner<AddItemToCompanionEquipmentErrorMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final AddItemToCompanionEquipmentErrorMessage msg) {
        ErrorsMessageTranslator.getInstance().pushMessage(msg.getErrorCode(), 3, new Object[0]);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5563;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddItemToCompanionEquipmentErrorMessageRunner.class);
    }
}
