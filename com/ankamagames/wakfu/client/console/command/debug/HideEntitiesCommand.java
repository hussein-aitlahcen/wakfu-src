package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class HideEntitiesCommand implements Command
{
    public static int DEBUG_HIDE;
    public static final int HIDE_NPC = 1;
    public static final int HIDE_PLAYERS = 2;
    public static final int HIDE_LOCAL_PLAYER = 4;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        int mask = 0;
        if (!args.isEmpty()) {
            mask = HideEntitiesCommand.DEBUG_HIDE;
            for (final String arg : args.get(0).split(" ")) {
                mask = computeMask(mask, arg);
            }
        }
        HideEntitiesCommand.DEBUG_HIDE = mask;
        Entity.DEBUG_HIDDER = ((HideEntitiesCommand.DEBUG_HIDE == 0) ? null : this.createHidder());
        this.trace(manager);
    }
    
    private void trace(final ConsoleManager manager) {
        if (HideEntitiesCommand.DEBUG_HIDE == 0) {
            manager.trace("Tout visible");
            return;
        }
        String hidden = "";
        if ((HideEntitiesCommand.DEBUG_HIDE & 0x1) == 0x1) {
            hidden += "mobs ";
        }
        if ((HideEntitiesCommand.DEBUG_HIDE & 0x2) == 0x2) {
            hidden += "joueurs ";
        }
        if ((HideEntitiesCommand.DEBUG_HIDE & 0x4) == 0x4) {
            hidden += "localPlayer ";
        }
        manager.trace("masqu\u00e9: " + hidden);
    }
    
    private static int computeMask(final int mask, final String arg) {
        if (arg.equals("npc")) {
            return mask ^ 0x1;
        }
        if (arg.equals("players")) {
            return mask ^ 0x2;
        }
        if (arg.equals("local")) {
            return mask ^ 0x4;
        }
        if (arg.equals("0")) {
            return 0;
        }
        return mask;
    }
    
    private Entity.Hidder createHidder() {
        return new Entity.Hidder() {
            @Override
            public boolean isHidden(final Object owner) {
                if (!(owner instanceof CharacterActor)) {
                    return false;
                }
                final CharacterInfo character = ((CharacterActor)owner).getCharacterInfo();
                return ((HideEntitiesCommand.DEBUG_HIDE & 0x1) == 0x1 && character instanceof NonPlayerCharacter) || ((HideEntitiesCommand.DEBUG_HIDE & 0x2) == 0x2 && character instanceof PlayerCharacter && !character.isLocalPlayer()) || ((HideEntitiesCommand.DEBUG_HIDE & 0x4) == 0x4 && character.isLocalPlayer());
            }
        };
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        HideEntitiesCommand.DEBUG_HIDE = 0;
    }
}
