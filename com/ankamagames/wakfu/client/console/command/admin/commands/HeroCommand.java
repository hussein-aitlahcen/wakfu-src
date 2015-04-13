package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.account.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@Documentation(commandName = "hero", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "hero h to show full documentation. Permit to manage heroes.", commandObsolete = false)
public final class HeroCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int HELP = 0;
    public static final int ADD_XP = 1;
    public static final int LIST = 2;
    private final int m_commandId;
    private final String[] m_args;
    
    public HeroCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 2: {
                return this.m_args.length == 0;
            }
            case 1: {
                return this.m_args.length == 2;
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
                    this.addXp();
                }
                case 2: {
                    this.list();
                }
            }
        }
        catch (Exception e) {
            HeroCommand.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private void list() {
        final LocalAccountInformations localAccount = WakfuGameEntity.getInstance().getLocalAccount();
        final TLongHashSet heroes = HeroesManager.INSTANCE.getHeroes(localAccount.getAccountId());
        if (heroes == null || heroes.isEmpty()) {
            ModerationCommand.log("Aucun heros");
            return;
        }
        heroes.forEach(new TLongProcedure() {
            @Override
            public boolean execute(final long value) {
                final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(value);
                ModerationCommand.log(hero.getName() + " : " + hero.getId());
                return true;
            }
        });
    }
    
    private void addXp() {
        final long heroId = Long.parseLong(this.m_args[0]);
        final long xpToAdd = Long.parseLong(this.m_args[1]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)240);
        msg.addLongParameter(heroId);
        msg.addLongParameter(xpToAdd);
        networkEntity.sendMessage(msg);
    }
    
    private static void help() {
        ModerationCommand.log("(hero) (help | h) : show help");
        ModerationCommand.log("(hero) (list | l) : show available heroes");
        ModerationCommand.log("(hero) (addXp | xp) : add xp to hero of given id");
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionCommand.class);
    }
}
