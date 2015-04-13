package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;

final class ScriptedActionUtils
{
    protected static void refreshShortcutsIfNecessary(final CharacterInfo instigator, final CharacterInfo target) {
        LocalPlayerCharacter lpc = null;
        if (instigator != null && instigator.isControlledByLocalPlayer()) {
            final CharacterInfo controller = instigator.getController();
            if (controller instanceof LocalPlayerCharacter) {
                lpc = (LocalPlayerCharacter)controller;
            }
        }
        else if (target != null && target.isControlledByLocalPlayer()) {
            final CharacterInfo controller = target.getController();
            if (controller instanceof LocalPlayerCharacter) {
                lpc = (LocalPlayerCharacter)controller;
            }
        }
        if (lpc != null) {
            lpc.updateShortcutBars();
        }
    }
}
