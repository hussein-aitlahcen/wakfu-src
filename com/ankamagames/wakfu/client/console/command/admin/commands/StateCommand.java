package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import java.util.*;

@Documentation(commandName = "state", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF, AdminRightsGroup.MODERATOR }, commandDescription = "state h to show full documentation.", commandObsolete = false)
public class StateCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int HELP = 0;
    public static final int SHOW = 1;
    public static final int ADD = 2;
    public static final int REMOVE = 3;
    public static final int SHOW_ALL = 4;
    public static final int SHOW_ALL_OF = 5;
    private final int m_commandId;
    private final String[] m_args;
    
    public StateCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 1: {
                return this.m_args.length == 0;
            }
            case 0: {
                return true;
            }
            case 2: {
                return this.m_args.length == 1 || this.m_args.length == 2;
            }
            case 3: {
                return this.m_args.length == 1;
            }
            case 4: {
                return this.m_args.length == 0;
            }
            case 5: {
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
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        try {
            switch (this.m_commandId) {
                case 1: {
                    this.show();
                    break;
                }
                case 0: {
                    this.help();
                    break;
                }
                case 2: {
                    this.add(networkEntity);
                    break;
                }
                case 3: {
                    this.remove(networkEntity);
                    break;
                }
                case 4: {
                    this.showAll(networkEntity);
                    break;
                }
                case 5: {
                    this.showAllOf(networkEntity);
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me lors de l'execution d'une commande de calendrier " + e);
        }
    }
    
    private void showAll(final NetworkEntity networkEntity) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final LocalTimedRunningEffectManager rem = localPlayer.getRunningEffectManager();
        ConsoleManager.getInstance().log("effets : ");
        for (final RunningEffect effect : rem) {
            try {
                final EffectContainer effectContainer = effect.getEffectContainer();
                final int containerType = (effectContainer == null) ? 0 : effectContainer.getContainerType();
                ConsoleManager.getInstance().log("UID : " + effect.getBaseUid() + ", " + effect.getClass().getSimpleName() + " (" + effect.getId() + ")" + ", id effet : " + effect.getGenericEffect().getEffectId() + ", container type : " + WakfuEffectContainerType.getTypeName(containerType) + " (" + containerType + ")" + ", id container : " + this.getContainerId(effect));
            }
            catch (Exception e) {
                StateCommand.m_logger.error((Object)"Erreur lors de la recuperation d'un effet");
            }
        }
    }
    
    private void showAllOf(final NetworkEntity networkEntity) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final LocalTimedRunningEffectManager rem = localPlayer.getRunningEffectManager();
        final int containerType = Integer.parseInt(this.m_args[0]);
        for (final RunningEffect effect : rem) {
            try {
                if (effect.getEffectContainer() == null || effect.getEffectContainer().getContainerType() != containerType) {
                    continue;
                }
                ConsoleManager.getInstance().log("UID : " + effect.getBaseUid() + ", " + effect.getClass().getSimpleName() + " (" + effect.getGenericEffect().getActionId() + ")" + ", id effet : " + effect.getGenericEffect().getEffectId() + ", container type : " + WakfuEffectContainerType.getTypeName(containerType) + " (" + containerType + ")" + ", id container : " + this.getContainerId(effect));
            }
            catch (Exception e) {
                StateCommand.m_logger.error((Object)"Erreur lors de la recuperation d'un effet");
            }
        }
    }
    
    private long getContainerId(final RunningEffect effect) {
        final EffectContainer container = effect.getEffectContainer();
        if (container instanceof AbstractSpellLevel) {
            return ((AbstractSpellLevel)container).getSpell().getId();
        }
        if (container instanceof Item) {
            return ((Item)container).getReferenceId();
        }
        if (container instanceof Aptitude) {
            return ((Aptitude)container).getReferenceId();
        }
        if (container instanceof State) {
            return ((State)container).getStateBaseId();
        }
        if (container instanceof AptitudeBonusEffectContainer) {
            return ((AptitudeBonusEffectContainer)container).getBonusModel().getId();
        }
        return container.getEffectContainerId();
    }
    
    private void add(final NetworkEntity networkEntity) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)101);
        netMessage.addShortParameter(Short.parseShort(this.m_args[0]));
        if (this.m_args.length == 2) {
            netMessage.addShortParameter(Short.parseShort(this.m_args[1]));
        }
        networkEntity.sendMessage(netMessage);
    }
    
    private void remove(final NetworkEntity networkEntity) {
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)100);
        netMessage.addShortParameter(Short.parseShort(this.m_args[0]));
        networkEntity.sendMessage(netMessage);
    }
    
    private void show() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final List<StateRunningEffect> states = localPlayer.getRunningEffectManager().getRunningState();
        for (int i = 0, size = states.size(); i < size; ++i) {
            final StateRunningEffect stateRunningEffect = states.get(i);
            ConsoleManager.getInstance().log("UID : " + stateRunningEffect.getBaseUid() + ", id Etat : " + stateRunningEffect.getState().getStateBaseId() + ", level : " + stateRunningEffect.getState().getLevel() + ", remaining time in ms : " + stateRunningEffect.getRemainingTimeInMs() + " ms");
        }
    }
    
    private void help() {
        ModerationCommand.log("-show | -s : show current states");
        ModerationCommand.log("-showall | -sa : show current effects");
        ModerationCommand.log("(-showallof | -sao) containerID : show effects with a container type (state = 1, spell = 11, item = 12, aptitude = 17)");
        ModerationCommand.log("(-add | -a) Id level? : apply state. Level is at 1 by default");
        ModerationCommand.log("(-remove | -r) Id : remove state");
    }
    
    static {
        m_logger = Logger.getLogger((Class)StateCommand.class);
    }
}
