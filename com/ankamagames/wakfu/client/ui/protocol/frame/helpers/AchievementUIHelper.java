package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.preferences.*;

public class AchievementUIHelper
{
    public static void displayFollowedAchievements() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final boolean forceOpen = localPlayer.hasProperty(WorldPropertyType.FOLLOW_ACHIEVEMENT_UI_FORCE_OPENED);
        boolean display;
        if (forceOpen) {
            display = true;
        }
        else if (localPlayer.getCurrentFight() != null) {
            display = false;
        }
        else {
            final WakfuGamePreferences pref = WakfuClientInstance.getInstance().getGamePreferences();
            display = pref.getBooleanValue(WakfuKeyPreferenceStoreEnum.FOLLOWED_QUESTS_DISPLAY);
        }
        PropertiesProvider.getInstance().setPropertyValue("followedQuestsDisplay", display);
    }
}
