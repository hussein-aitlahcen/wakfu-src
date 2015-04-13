package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

@Documentation(commandName = "fightChallenge | fc", commandParameters = "&lt;help", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "fc h to show full doc.", commandObsolete = false)
public final class FightChallengeCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int HELP = 0;
    public static final int LIST = 1;
    public static final int START = 2;
    private final int m_commandId;
    private final String[] m_args;
    
    public FightChallengeCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 1: {
                return this.m_args.length == 0;
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
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9!");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 0: {
                    this.help();
                }
                case 1: {
                    this.list();
                }
                case 2: {
                    this.start();
                }
            }
        }
        catch (Exception e) {
            FightChallengeCommand.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private void start() {
        final int challengeId = Integer.parseInt(this.m_args[0]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)171);
        msg.addIntParameter(challengeId);
        networkEntity.sendMessage(msg);
    }
    
    private void list() {
        final ArrayList<Entry> list = new ArrayList<Entry>();
        FightChallengeManager.INSTANCE.forEachChallenge(new TObjectProcedure<FightChallenge>() {
            @Override
            public boolean execute(final FightChallenge object) {
                list.add(new Entry(object));
                return true;
            }
        });
        Collections.sort(list);
        ModerationCommand.log(list.size() + " challenges disponibles : ");
        for (int i = 0, size = list.size(); i < size; ++i) {
            final Entry entry = list.get(i);
            ModerationCommand.log(entry.toString());
        }
    }
    
    private void help() {
        ModerationCommand.log("(fightchallenge | fc) (help | h) : show documentation");
        ModerationCommand.log("(fightchallenge | fc) (list | l) : show list of available challenges");
        ModerationCommand.log("(fightchallenge | fc) (start | s) id : launch challenge with specified id");
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightChallengeCommand.class);
    }
    
    private static class Entry implements Comparable<Entry>
    {
        private final int m_id;
        private final String m_name;
        private final String m_description;
        
        private Entry(final FightChallenge challenge) {
            super();
            this.m_id = challenge.getId();
            this.m_name = WakfuTranslator.getInstance().getString(140, this.m_id, new Object[0]);
            this.m_description = WakfuTranslator.getInstance().getString(141, this.m_id, new Object[0]);
        }
        
        @Override
        public int compareTo(final Entry o) {
            return this.m_name.compareTo(o.m_name);
        }
        
        @Override
        public String toString() {
            return this.m_id + " : " + this.m_name + " - " + this.m_description;
        }
    }
}
