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
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild.*;

@Documentation(commandName = "guild", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "guild help to show full documentation. Permit to manage a guild.", commandObsolete = false)
public final class GuildCommand extends ModerationCommand
{
    public static final int ADD_POINT = 0;
    public static final int MONEY = 2;
    public static final int STATS = 3;
    public static final int ADD_BONUS = 4;
    public static final int SET_LEARNING_DURATION_FACTOR = 5;
    public static final int SET_MAX_SIMULTANEOUS = 6;
    public static final int GET_GUILD_ID = 7;
    public static final int SET_MAX_POINTS_PER_WEEK = 8;
    public static final int INFO = 9;
    public static final int SET_POINT_EARNED_FACTOR = 10;
    public static final int SET_LEVEL = 11;
    public static final int HELP = 12;
    public static final int CHANGE_MESSAGE = 13;
    public static final int CHANGE_DESC = 14;
    public static final int SET_NATION = 15;
    private int m_commandId;
    private String[] m_args;
    
    public GuildCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 3:
            case 7:
            case 9:
            case 12: {
                return this.m_args.length == 0;
            }
            case 0:
            case 2:
            case 4:
            case 5:
            case 6:
            case 8:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15: {
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
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player.getGuildHandler().getGuildId() <= 0L) {
            ConsoleManager.getInstance().err("Il faut avoir une guilde!");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 0: {
                    this.addGuildPoint(networkEntity);
                    break;
                }
                case 2: {
                    this.money(networkEntity);
                    break;
                }
                case 3: {
                    stats(networkEntity);
                    break;
                }
                case 4: {
                    this.addBonus(networkEntity);
                    break;
                }
                case 5: {
                    this.setLearningDurationFactor(networkEntity);
                    break;
                }
                case 6: {
                    this.setMaxSimultaneous(networkEntity);
                    break;
                }
                case 7: {
                    getGuildId();
                    break;
                }
                case 8: {
                    this.setMaxPointsPerWeek(networkEntity);
                    break;
                }
                case 9: {
                    info();
                    break;
                }
                case 10: {
                    this.setPointEarnedFactor(networkEntity);
                    break;
                }
                case 11: {
                    this.setLevel(networkEntity);
                    break;
                }
                case 13: {
                    this.changeMessage(networkEntity);
                    break;
                }
                case 14: {
                    this.changeDesc(networkEntity);
                    break;
                }
                case 15: {
                    this.setNation(networkEntity);
                    break;
                }
                case 12: {
                    help();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me d'une commande" + e);
        }
    }
    
    private void setNation(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)239);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    public static String getHelp() {
        return null;
    }
    
    private static void help() {
        ModerationCommand.log("guild ('point'|'p'|'pt') delta : give delta guild points");
        ModerationCommand.log("guild 'money' delta : give delta money");
        ModerationCommand.log("guild 'stats' : show guild stats");
        ModerationCommand.log("guild ('addBonus'|'ab') bonusId : unlock bonus");
        ModerationCommand.log("guild ('setLearningFactor'|'slf') factor : modify learning bonus speed");
        ModerationCommand.log("guild ('setMaxSimultaneous' | 'sms') max : modify maximum simultaneous bonus learning");
        ModerationCommand.log("guild 'id' : show guild id");
        ModerationCommand.log("guild ('maxPerWeek' | 'mpw') maxPerWeek : modify guild points limit per week");
        ModerationCommand.log("guild ('pointEarnedFactor' | 'pef') factor : modify point gain ratio");
        ModerationCommand.log("guild 'info' :  show guild info");
        ModerationCommand.log("guild ('setLevel' | 'sl') level : modify guild level");
        ModerationCommand.log("guild ('changeMessage' | 'cm') level : modify guild message");
        ModerationCommand.log("guild ('changeDescription' | 'cd') level : modify guild description");
        ModerationCommand.log("guild ('setNation' | 'sn') nationId : modify guild nation");
    }
    
    private void changeDesc(final NetworkEntity networkEntity) {
        final String newDesc = this.m_args[0];
        final Message msg = new GuildChangeDescriptionRequestMessage(newDesc);
        networkEntity.sendMessage(msg);
    }
    
    private void changeMessage(final NetworkEntity networkEntity) {
        final String newMessage = this.m_args[0];
        final Message msg = new GuildChangeMessageRequestMessage(newMessage);
        networkEntity.sendMessage(msg);
    }
    
    private void setLevel(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)157);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void setPointEarnedFactor(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)155);
        final float pointEarnedFactor = Float.parseFloat(this.m_args[0]);
        msg.addFloatParameter(pointEarnedFactor);
        networkEntity.sendMessage(msg);
        final Guild guild = WakfuGuildView.getInstance().getGuild();
        if (guild == null) {
            ConsoleManager.getInstance().err("Il faut avoir les infos de la guilde pour mettre le facteur a jour dans le client");
            return;
        }
        guild.getConstantManager().setBonusPointEarnedFactor(pointEarnedFactor);
    }
    
    private static void info() {
        final Guild guild = WakfuGuildView.getInstance().getGuild();
        if (guild == null) {
            ConsoleManager.getInstance().err("Il faut avoir les infos de la guilde pour mettre le facteur a jour dans le client");
            return;
        }
        ModerationCommand.log("Total de point : " + guild.getTotalGuildPoints());
        ModerationCommand.log("Points actuels : " + guild.getCurrentGuildPoints());
        ModerationCommand.log("Limite de points par semaine : " + guild.getWeeklyPointsLimit());
        ModerationCommand.log("Points gagn\u00e9s cette semaine : " + guild.getEarnedPointsWeekly());
    }
    
    private void setMaxPointsPerWeek(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)154);
        final int maxPointsPerDay = Integer.parseInt(this.m_args[0]);
        msg.addIntParameter(maxPointsPerDay);
        networkEntity.sendMessage(msg);
        final Guild guild = WakfuGuildView.getInstance().getGuild();
        if (guild == null) {
            ConsoleManager.getInstance().err("Il faut avoir les infos de la guilde pour mettre le facteur a jour dans le client");
            return;
        }
        new GuildController(guild).setWeeklyPointsLimit(maxPointsPerDay);
    }
    
    private static void getGuildId() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        ModerationCommand.log("Id de la guilde : " + player.getGuildHandler().getGuildId());
    }
    
    private void setMaxSimultaneous(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)150);
        final int maxSimultaneous = Integer.parseInt(this.m_args[0]);
        msg.addIntParameter(maxSimultaneous);
        networkEntity.sendMessage(msg);
        final Guild guild = WakfuGuildView.getInstance().getGuild();
        if (guild == null) {
            ConsoleManager.getInstance().err("Il faut avoir les infos de la guilde pour mettre le facteur a jour dans le client");
            return;
        }
        guild.getConstantManager().setMaxSimultaneous(maxSimultaneous);
    }
    
    private void setLearningDurationFactor(final NetworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)149);
        final float durationFactor = Float.parseFloat(this.m_args[0]);
        msg.addFloatParameter(durationFactor);
        networkEntity.sendMessage(msg);
        final Guild guild = WakfuGuildView.getInstance().getGuild();
        if (guild == null) {
            ConsoleManager.getInstance().err("Il faut avoir les infos de la guilde pour mettre le facteur a jour dans le client");
            return;
        }
        guild.getConstantManager().setLearningDurationFactor(durationFactor);
    }
    
    private void addBonus(final FrameworkEntity networkEntity) {
        final Message msg = new GuildAddBonusRequestMessage(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void addGuildPoint(final FrameworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)121);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private void money(final FrameworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)126);
        msg.addLongParameter(Long.parseLong(this.m_args[0]));
        networkEntity.sendMessage(msg);
    }
    
    private static void stats(final FrameworkEntity networkEntity) {
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)127);
        networkEntity.sendMessage(msg);
    }
}
