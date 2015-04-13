package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.game.item.gem.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "gem", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "gem h to show full documentation.", commandObsolete = false)
public class GemCommand extends ModerationCommand
{
    public static final int LIST = 0;
    public static final int CREATE_ITEM = 1;
    public static final int HELP = 2;
    private final int m_commandId;
    private final int m_itemId;
    private final int m_gemId;
    private final int m_num;
    
    public GemCommand(final int commandId) {
        this(commandId, 0, 0, -1);
    }
    
    public GemCommand(final int commandId, final int itemId, final int gemId, final int num) {
        super();
        this.m_commandId = commandId;
        this.m_itemId = itemId;
        this.m_gemId = gemId;
        this.m_num = num;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 2: {
                return true;
            }
            case 1: {
                return this.m_itemId != 0 && this.m_gemId != 0;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void execute() {
        switch (this.m_commandId) {
            case 0: {
                GemsDefinitionManager.INSTANCE.forEach(new TIntProcedure() {
                    @Override
                    public boolean execute(final int refId) {
                        ConsoleManager.getInstance().log(refId + " : " + WakfuTranslator.getInstance().getString(15, refId, new Object[0]));
                        return true;
                    }
                });
                break;
            }
            case 1: {
                final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
                if (networkEntity == null) {
                    ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
                    return;
                }
                final ModerationCommandMessage netMessage = new ModerationCommandMessage();
                netMessage.setServerId((byte)3);
                netMessage.setCommand((short)142);
                netMessage.addIntParameter(this.m_itemId);
                netMessage.addIntParameter(this.m_gemId);
                netMessage.addByteParameter((byte)this.m_num);
                networkEntity.sendMessage(netMessage);
                break;
            }
            case 2: {
                ConsoleManager.getInstance().trace("gem list : affiche la liste des runes disponibles");
                ConsoleManager.getInstance().trace("gem (createItem|ci) itemId : create item with all gems at max level ");
                ConsoleManager.getInstance().trace("gem (createItem|ci) itemId gemId variation : create item with specified gem level (between 0 et 9)");
                break;
            }
        }
    }
}
