package com.ankamagames.wakfu.client.chat;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public class ChatWorldPropertyTypeErrorManager
{
    public static void writeChatErrorMessage(final short errorId) {
        final String errorMessage = WakfuTranslator.getInstance().getString("error.worldPropertyType." + WorldPropertyType.getPropertyFromId(errorId).name());
        final ChatMessage chatErrorMessage = new ChatMessage(errorMessage);
        chatErrorMessage.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(chatErrorMessage);
    }
}
