package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

@Documentation(commandName = "achievement", commandParameters = "startdate &lt;achievementId&gt; &lt;sec&gt; &lt;min&gt; &lt;hour&gt; &lt;day&gt; &lt;month&gt; &lt;year&gt;", commandDescription = "Define a start date for specified event. It needs disconnect and reconnect.", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandObsolete = false)
public class AchievementDateCommand extends ModerationCommand
{
    private final byte m_cmd;
    private final int m_value;
    private final int m_seconds;
    private final int m_minutes;
    private final int m_hours;
    private final int m_day;
    private final int m_month;
    private final int m_year;
    
    public AchievementDateCommand(final byte cmd, final int value, final int seconds, final int minutes, final int hours, final int day, final int month, final int year) {
        super();
        this.m_cmd = cmd;
        this.m_value = value;
        this.m_seconds = seconds;
        this.m_minutes = minutes;
        this.m_hours = hours;
        this.m_day = day;
        this.m_month = month;
        this.m_year = year;
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
        final GameDate date = new GameDate(this.m_seconds, this.m_minutes, this.m_hours, this.m_day, this.m_month, this.m_year);
        switch (this.m_cmd) {
            case 6: {
                netMessage.addIntParameter(this.m_value);
                netMessage.addLongParameter(date.toLong());
                final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
                if (networkEntity != null) {
                    networkEntity.sendMessage(netMessage);
                }
                else {
                    ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
                }
            }
            default: {
                throw new UnsupportedOperationException("La commande " + this.m_cmd + " n'est pas support\u00e9e");
            }
        }
    }
}
