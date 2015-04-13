package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public class TurnCharacterCommand implements Command
{
    private static final Logger m_logger;
    private static final long SECURITY_DELAY = 500L;
    private static long s_startTime;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        TurnCharacterCommand.s_startTime = ((TurnCharacterCommand.s_startTime == -1L) ? System.currentTimeMillis() : -1L);
        Direction8 direction = null;
        if (!args.isEmpty()) {
            try {
                direction = Direction8.getDirectionFromIndex(Integer.parseInt(args.get(2)));
            }
            catch (NumberFormatException e) {
                TurnCharacterCommand.m_logger.error((Object)"Mauvais argument", (Throwable)e);
            }
            catch (RuntimeException e2) {
                TurnCharacterCommand.m_logger.error((Object)"Exception \u00e0 la r\u00e9cuperation de la direction", (Throwable)e2);
            }
        }
        if (direction != null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
            if (currentOccupation != null && currentOccupation.getOccupationTypeId() != 1) {
                return;
            }
            final Fight fight = localPlayer.getCurrentFight();
            if (fight != null) {
                if (System.currentTimeMillis() - TurnCharacterCommand.s_startTime < 500L) {
                    return;
                }
                short fightMessageId = -1;
                switch (direction) {
                    case NORTH_EAST: {
                        fightMessageId = 18006;
                        break;
                    }
                    case NORTH_WEST: {
                        fightMessageId = 18003;
                        break;
                    }
                    case SOUTH_WEST: {
                        fightMessageId = 18005;
                        break;
                    }
                    case SOUTH_EAST: {
                        fightMessageId = 18004;
                        break;
                    }
                }
                ControlCenterFightDialogActions.fightSetDirection(fight, fightMessageId);
            }
            else {
                if (System.currentTimeMillis() - TurnCharacterCommand.s_startTime < 500L) {
                    WakfuGameEntity.getInstance().getLocalPlayer().getMobile().setDirection(direction);
                    return;
                }
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new ActorDirectionChangeRequestMessage(direction));
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TurnCharacterCommand.class);
        TurnCharacterCommand.s_startTime = -1L;
    }
}
