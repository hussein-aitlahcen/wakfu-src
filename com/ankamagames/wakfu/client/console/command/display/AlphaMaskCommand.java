package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;

public class AlphaMaskCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final boolean b = !wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY);
        wakfuGamePreferences.setValue(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY, b);
        applyAlphaMasks(b);
    }
    
    public static void applyAlphaMasks(final boolean enable) {
        applyAlphaMaskOnPlayer(enable);
        applyAlphaMaskOnFight(enable);
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString(enable ? "options.alphaMaskActivated" : "options.alphaMaskDisactivated"));
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    public static void applyAlphaMaskOnPlayer(final boolean enable) {
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final CharacterActor actor = localPlayer.getActor();
        if (actor == null) {
            return;
        }
        actor.enableAlphaMask(enable);
    }
    
    public static void applyAlphaMaskOnFight(final boolean enable) {
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final CharacterActor actor = localPlayer.getActor();
        assert actor != null;
        final Fight fight = localPlayer.getCurrentFight();
        if (fight == null) {
            return;
        }
        for (final CharacterInfo characterInfo : fight.getFighters()) {
            final CharacterActor characterActor = characterInfo.getActor();
            if (characterActor != null) {
                if (actor == characterActor) {
                    continue;
                }
                characterActor.enableAlphaMask(enable);
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
