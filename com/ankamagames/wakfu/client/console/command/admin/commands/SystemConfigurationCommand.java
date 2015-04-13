package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.google.common.primitives.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

@Documentation(commandName = "systemConfiguration || sysconf", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "sysconf h to show full documentation. Permit to manage server config.", commandObsolete = false)
public final class SystemConfigurationCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int HELP = 0;
    public static final int SHOW = 1;
    public static final int RECO_IN_FIGHT = 2;
    public static final int ZAAP_FREE = 3;
    public static final int MODIFY_NEW_APTITUDE = 4;
    public static final int PLAYER_LEVEL_CAP = 5;
    private final int m_commandId;
    private final String[] m_args;
    private String m_startDate;
    private String m_endDate;
    
    public SystemConfigurationCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0: {
                return this.m_args.length == 0;
            }
            case 1: {
                return this.m_args.length == 2;
            }
            case 2: {
                return this.m_args.length == 1;
            }
            case 4: {
                return this.m_args.length == 1;
            }
            case 3: {
                return this.m_args.length >= 1 && this.m_args.length <= 3;
            }
            case 5: {
                return this.m_args.length == 1 && Ints.tryParse(this.m_args[0]) != null;
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
        try {
            switch (this.m_commandId) {
                case 0: {
                    help();
                }
                case 1: {
                    this.show();
                }
                case 2: {
                    this.modifyConfiguration((short)209);
                }
                case 4: {
                    this.modifyConfiguration((short)233);
                }
                case 3: {
                    this.initDatesIfNecessary();
                    this.modifyConfiguration((short)214);
                }
                case 5: {
                    this.modifyConfiguration((short)234);
                }
            }
        }
        catch (Exception e) {
            SystemConfigurationCommand.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private void initDatesIfNecessary() {
        if (this.m_args.length < 3) {
            return;
        }
        this.m_startDate = this.m_args[1];
        this.m_endDate = this.m_args[2];
    }
    
    private void modifyConfiguration(final short commandId) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)2);
        msg.setCommand(commandId);
        msg.addStringParameter(this.m_args[0]);
        if (this.m_startDate != null) {
            final GameDate startDate = ModerationCommandUtils.getGameDate(this.m_startDate);
            msg.addLongParameter(startDate.toLong());
            if (startDate == null) {
                ConsoleManager.getInstance().err("Erreur de syntaxe dans la date de d\u00e9part - taper setBonusFactor help pour voir la syntaxe");
                return;
            }
        }
        if (this.m_endDate != null) {
            final GameDate endDate = ModerationCommandUtils.getGameDate(this.m_endDate);
            msg.addLongParameter(endDate.toLong());
            if (endDate == null) {
                ConsoleManager.getInstance().err("Erreur de syntaxe dans la date de fin - taper setBonusFactor help pour voir la syntaxe");
                return;
            }
        }
        networkEntity.sendMessage(msg);
    }
    
    private void show() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        final String target = this.m_args[0];
        if (target.equals("world")) {
            msg.setServerId((byte)2);
        }
        else if (target.equals("global")) {
            msg.setServerId((byte)6);
        }
        else if (target.equals("game")) {
            msg.setServerId((byte)3);
        }
        else {
            if (!target.equals("connection")) {
                ModerationCommand.log("Cible inconnue : (target = world, game, connection ou global)");
                return;
            }
            msg.setServerId((byte)1);
        }
        msg.setCommand((short)208);
        msg.addStringParameter(this.m_args[1]);
        networkEntity.sendMessage(msg);
    }
    
    private static void help() {
        ModerationCommand.log("(systemConfiguration | sysConf) (help | h) : show full documentation");
        ModerationCommand.log("(systemConfiguration | sysConf) (recoInFight) \"true|false\" : enable or disable reconnection in fight ");
        ModerationCommand.log("(systemConfiguration | sysConf) (newAptitude|na) \"true|false\" : enable or disable new aptitudes ");
        ModerationCommand.log("(systemConfiguration | sysConf) (playerCapLevel) \"value\" : set level cap to specified value");
        ModerationCommand.log("(systemConfiguration | sysConf) (zaapFree) \"true|false\" : enable or disable free zaap ");
        ModerationCommand.log("(systemConfiguration | sysConf) (zaapFree) \"true|false\" startDate endDate : enable or disable free zaap for a defined period ");
        ModerationCommand.log("(systemConfiguration | sysConf) (show) \"target\" \"confKey\" : show confKey state in target server (target = world, game, connection or global)");
        ModerationCommand.log("Date syntax : dd/mm/yyyy hh:mm");
    }
    
    static {
        m_logger = Logger.getLogger((Class)SystemConfigurationCommand.class);
    }
}
