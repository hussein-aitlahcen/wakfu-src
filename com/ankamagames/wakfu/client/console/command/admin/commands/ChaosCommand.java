package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import java.util.*;

@Documentation(commandName = "", commandParameters = "", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "", commandObsolete = true)
public class ChaosCommand extends ModerationCommand
{
    public static final byte START = 1;
    public static final byte STOP = 2;
    private final byte m_cmd;
    private final String[] m_args;
    
    public ChaosCommand(final byte command, final String... args) {
        super();
        this.m_cmd = command;
        System.arraycopy(args, 0, this.m_args = new String[args.length], 0, args.length);
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_cmd) {
            case 1: {
                return this.m_args.length == 1 || this.m_args.length == 2;
            }
            case 2: {
                return this.m_args.length == 1;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void execute() {
        switch (this.m_cmd) {
            case 1: {
                this.start();
            }
            case 2: {
                this.stop();
            }
            default: {
                ConsoleManager.getInstance().err("Commande " + this.m_cmd + " invalide!");
            }
        }
    }
    
    private void start() {
        final int chaosId = Integer.parseInt(this.m_args[0]);
        final int zoneId = (this.m_args.length >= 2) ? Integer.parseInt(this.m_args[1]) : -1;
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setCommand((short)107);
        netMessage.setServerId((byte)3);
        netMessage.addIntParameter(chaosId);
        netMessage.addIntParameter(zoneId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        networkEntity.sendMessage(netMessage);
    }
    
    private void stop() {
        final int zoneId = Integer.parseInt(this.m_args[0]);
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setCommand((short)108);
        netMessage.setServerId((byte)3);
        netMessage.addIntParameter(zoneId);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        networkEntity.sendMessage(netMessage);
    }
    
    @Override
    public String toString() {
        return "ChaosCommand{m_cmd=" + this.m_cmd + ", m_args=" + ((this.m_args == null) ? null : Arrays.asList(this.m_args)) + '}';
    }
}
