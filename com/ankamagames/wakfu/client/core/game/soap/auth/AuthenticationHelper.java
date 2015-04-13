package com.ankamagames.wakfu.client.core.game.soap.auth;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

final class AuthenticationHelper
{
    private static final String SERVER_ID = "iFilterDataServerId";
    private static final String CHARACTER_ID = "lFilterDataCharacterId";
    
    static void addMetaData(final SOAPElement element) {
        element.putParameter("iFilterDataServerId", WakfuGameEntity.getInstance().getServerId());
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null) {
            element.putParameter("lFilterDataCharacterId", localPlayer.getId());
        }
    }
}
