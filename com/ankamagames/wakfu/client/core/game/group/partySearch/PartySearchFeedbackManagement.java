package com.ankamagames.wakfu.client.core.game.group.partySearch;

import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public class PartySearchFeedbackManagement
{
    public static void computeFeedback(final PartySearchFeedbackEnum feedback) {
        switch (feedback) {
            case NO_ERROR: {
                return;
            }
            case UNREGISTER_OK:
            case REMOVED_FOR_FULL_GROUP:
            case REMOVED_FOR_VOID_OCCUPATION:
            case REMOVED_FOR_INVALID_OCCUPATION: {
                UIPartySearchFrame.cleanPartyRequester();
                break;
            }
        }
        pushChatFeedbackMessage(feedback.getId(), feedback.isError());
    }
    
    private static void pushChatFeedbackMessage(final byte id, final boolean isError) {
        final String chatMessage = WakfuTranslator.getInstance().getString("partySearch.feedback" + id);
        ChatManager.getInstance().pushMessage(chatMessage, isError ? 3 : 4);
    }
}
