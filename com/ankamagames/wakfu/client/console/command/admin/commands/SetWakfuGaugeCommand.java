package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "setWakfuGauge | swg", commandParameters = "&lt;value&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "Value between -1 and 1 (not included). Set Wakfu or Stasis level.", commandObsolete = false)
public class SetWakfuGaugeCommand extends ModerationCommand
{
    private final float m_wakfuGaugeValue;
    
    public SetWakfuGaugeCommand(final float wakfuGaugeValue) {
        super();
        this.m_wakfuGaugeValue = wakfuGaugeValue;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void execute() {
        if (this.m_wakfuGaugeValue < -1.0 || this.m_wakfuGaugeValue > 1.0) {
            ConsoleManager.getInstance().err("La valeur d'une jauge de wakfu doit \u00eatre dans [-1.0, 1.0]");
            return;
        }
        final ModerationCommandMessage message = new ModerationCommandMessage();
        message.setServerId((byte)3);
        message.setCommand((short)77);
        message.addFloatParameter(this.m_wakfuGaugeValue);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            networkEntity.sendMessage(message);
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
    }
}
