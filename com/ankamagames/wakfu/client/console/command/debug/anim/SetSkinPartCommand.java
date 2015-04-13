package com.ankamagames.wakfu.client.console.command.debug.anim;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class SetSkinPartCommand implements Command
{
    private static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            manager.err("pas de localPlayer");
            return;
        }
        if (args.size() != 4) {
            return;
        }
        final CharacterActor actor = localPlayer.getActor();
        final String partName = args.get(3);
        try {
            final int gfxId = Integer.parseInt(args.get(2));
            try {
                final String[] parts = AnmPartHelper.getParts(args.get(3));
                actor.applyParts(Actor.getGfxFile(gfxId), parts);
            }
            catch (Exception e) {
                SetSkinPartCommand.m_logger.error((Object)"", (Throwable)e);
            }
        }
        catch (NumberFormatException e2) {
            actor.applyParts(args.get(2), partName);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetSkinPartCommand.class);
    }
}
