package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "achievement", commandParameters = "&lt;var set | var get | complete | reset | complete_objective | reset all&gt; &lt;achievementId&gt;", commandDescription = "Achievements management", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandObsolete = false)
public class AchievementCommand extends ModerationCommand
{
    private final byte m_cmd;
    private final String m_varName;
    private final int m_value;
    
    public AchievementCommand(final byte cmd, final String varName) {
        super();
        this.m_cmd = cmd;
        this.m_varName = varName;
        this.m_value = -1;
    }
    
    public AchievementCommand(final byte cmd, final String varName, final int value) {
        super();
        this.m_cmd = cmd;
        this.m_varName = varName;
        this.m_value = value;
    }
    
    public AchievementCommand(final byte cmd, final int value) {
        super();
        this.m_cmd = cmd;
        this.m_varName = null;
        this.m_value = value;
    }
    
    public AchievementCommand(final byte cmd) {
        super();
        this.m_cmd = cmd;
        this.m_varName = null;
        this.m_value = -1;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setCommand((short)75);
        netMessage.setServerId((byte)3);
        netMessage.addByteParameter(this.m_cmd);
        switch (this.m_cmd) {
            case 2: {
                netMessage.addStringParameter(this.m_varName);
                netMessage.addIntParameter(this.m_value);
                break;
            }
            case 1: {
                netMessage.addStringParameter(this.m_varName);
                break;
            }
            case 3: {
                netMessage.addIntParameter(this.m_value);
                break;
            }
            case 4: {
                netMessage.addIntParameter(this.m_value);
                break;
            }
            case 5: {
                netMessage.addIntParameter(this.m_value);
                break;
            }
            case 7: {
                break;
            }
            default: {
                throw new UnsupportedOperationException("La commande " + this.m_cmd + " n'est pas support\u00e9e");
            }
        }
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(netMessage);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
