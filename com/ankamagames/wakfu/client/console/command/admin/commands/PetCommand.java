package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.item.*;

@Documentation(commandName = "pet", commandParameters = "&lt;xp&gt; &lt;value&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "Give xp to pet. Type 2500 to be 50.", commandObsolete = false)
public final class PetCommand extends ModerationCommand
{
    public static final int XP = 0;
    public static final int FORCE_FEED = 1;
    private int m_commandId;
    private String[] m_args;
    
    public PetCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0: {
                return this.m_args.length == 1;
            }
            case 1: {
                return this.m_args.length == 0;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void execute() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9!");
            return;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (((ArrayInventoryWithoutCheck<Item, R>)player.getEquipmentInventory()).getFromPosition(EquipmentPosition.PET.m_id) == null) {
            ConsoleManager.getInstance().err("Il faut avoir un familier \u00e9quip\u00e9!");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 0: {
                    this.addXp(networkEntity);
                    break;
                }
                case 1: {
                    forceFeed(networkEntity);
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me d'une commande" + e);
        }
    }
    
    private void addXp(final FrameworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)115);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private static void forceFeed(final FrameworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)116);
        networkEntity.sendMessage(msg);
    }
}
