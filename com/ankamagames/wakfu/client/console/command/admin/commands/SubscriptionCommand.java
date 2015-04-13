package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

@Documentation(commandName = "subscriber | sub", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR, AdminRightsGroup.STAFF }, commandDescription = "sub h to show full documentation.", commandObsolete = false)
public class SubscriptionCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final byte HELP = 0;
    public static final byte SERVER_STATUS = 1;
    public static final byte ACTIVATE_DEFAULT = 2;
    public static final byte DEACTIVATE = 3;
    public static final byte SHOW_SUBSCRIPTION_LEVELS = 4;
    public static final byte ADD_RIGHT = 5;
    public static final byte REMOVE_RIGHT = 6;
    public static final byte SHOW_CLIENT_INFO = 7;
    public static final byte SHOW_SUBSCRIPTION_RIGHTS = 8;
    public static final byte SET_SUBSCRIPTION_LEVEL = 9;
    public static final byte ADD_RIGHT_IN_CLIENT = 10;
    public static final byte SHOW_SUBSCRIPTION_RIGHT_SET = 11;
    public static final byte REFRESH_SUBSCRIPTION = 12;
    private final int m_commandId;
    private final String[] m_args;
    
    public SubscriptionCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 1:
            case 4:
            case 7:
            case 8:
            case 12: {
                return this.m_args.length == 0;
            }
            case 2:
            case 3: {
                return this.m_args.length == 0 || this.m_args.length == 1;
            }
            case 5:
            case 6:
            case 9:
            case 10:
            case 11: {
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
                    help();
                }
                case 1: {
                    serverStatus();
                }
                case 2: {
                    this.activateDefault();
                }
                case 3: {
                    this.deactivate();
                }
                case 4: {
                    showSubscriptionLevels();
                }
                case 5: {
                    this.addRight();
                }
                case 6: {
                    this.removeRight();
                }
                case 7: {
                    showClientInfo();
                }
                case 8: {
                    showSubscriptionRights();
                }
                case 9: {
                    this.setSubscriptionLevel();
                }
                case 10: {
                    this.addRightInClient();
                }
                case 11: {
                    this.showSubscriptionRightSet();
                }
                case 12: {
                    refreshSubscription();
                }
            }
        }
        catch (Exception e) {
            SubscriptionCommand.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private static void refreshSubscription() {
        ModerationCommand.log("Demande de mise a jour des donn\u00e9es d'abonnement");
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final RefreshSubscriptionRequestMessage msg = new RefreshSubscriptionRequestMessage();
        networkEntity.sendMessage(msg);
    }
    
    private void showSubscriptionRightSet() {
        final int subscriptionId = Integer.parseInt(this.m_args[0]);
        final SubscriptionLevel subscriptionLevel = SubscriptionLevel.fromId(subscriptionId);
        if (subscriptionLevel == null) {
            ModerationCommand.log("Niveau de souscription inconnu");
            return;
        }
        ModerationCommand.log("Droits pour le niveau d'abonnement " + subscriptionLevel.name());
        final EnumSet<SubscriptionRight> rightsSet = subscriptionLevel.getRightsSet();
        for (final SubscriptionRight right : rightsSet) {
            ModerationCommand.log(right.toString());
        }
    }
    
    private void addRightInClient() {
        final int rightId = Integer.parseInt(this.m_args[0]);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final SubscriptionRight right = SubscriptionRight.fromId(rightId);
        if (right == null) {
            ModerationCommand.log("Droit inconnu");
            return;
        }
        localPlayer.getAccountInformationHandler().addSubscriptionRight(right);
        ModerationCommand.log("Ajout du droit " + right + " pour le client seulement");
    }
    
    private void setSubscriptionLevel() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)180);
        netMessage.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(netMessage);
    }
    
    private static void showSubscriptionRights() {
        final SubscriptionRight[] values = SubscriptionRight.values();
        for (int i = 0; i < values.length; ++i) {
            final SubscriptionRight value = values[i];
            ModerationCommand.log(value.toString());
        }
    }
    
    private static void showClientInfo() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        ModerationCommand.log(localPlayer.getAccountInformationHandler().toString());
    }
    
    private void removeRight() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)179);
        netMessage.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(netMessage);
    }
    
    private void addRight() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)178);
        netMessage.addIntParameter(Integer.parseInt(this.m_args[0]));
        networkEntity.sendMessage(netMessage);
    }
    
    private static void showSubscriptionLevels() {
        final SubscriptionLevel[] values = SubscriptionLevel.values();
        for (int i = 0; i < values.length; ++i) {
            final SubscriptionLevel value = values[i];
            ModerationCommand.log(value.toString());
        }
    }
    
    private void deactivate() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)177);
        if (this.m_args.length == 0) {
            netMessage.addLongParameter(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId());
        }
        else {
            netMessage.addLongParameter(Long.parseLong(this.m_args[0]));
        }
        networkEntity.sendMessage(netMessage);
    }
    
    private void activateDefault() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)176);
        if (this.m_args.length == 0) {
            netMessage.addLongParameter(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId());
        }
        else {
            netMessage.addLongParameter(Long.parseLong(this.m_args[0]));
        }
        networkEntity.sendMessage(netMessage);
    }
    
    private static void serverStatus() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)2);
        netMessage.setCommand((short)175);
        netMessage.addLongParameter(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId());
        networkEntity.sendMessage(netMessage);
    }
    
    private static void help() {
        ModerationCommand.log("(subscriber | sub) (help | h) :  show full documentation");
        ModerationCommand.log("(subscriber | sub) (serverStatus | ss) :  show account subscription type");
        ModerationCommand.log("(subscriber | sub) on :  enable default subscription for this account");
        ModerationCommand.log("(subscriber | sub) id on :  same for another account");
        ModerationCommand.log("(subscriber | sub) off :  disable subscription for current account and set EU_FREE");
        ModerationCommand.log("(subscriber | sub) id off :  same for another account");
        ModerationCommand.log("(subscriber | sub) (showLevels | sl) :  show subscription levels");
        ModerationCommand.log("(subscriber | sub) (showRights | sr) :  show subscription rights");
        ModerationCommand.log("(subscriber | sub) (showSubscriptionRightsSet | ssrs) levelID :  show rights for a specified subscription level");
        ModerationCommand.log("(subscriber | sub) (addRight | ar) rightId :  add subscription right for this session");
        ModerationCommand.log("(subscriber | sub) (addRightInClient | arc) rightId :  same but only for the client");
        ModerationCommand.log("(subscriber | sub) (removeRight | rr) rightId :  remove a right given for session");
        ModerationCommand.log("(subscriber | sub) (setSubscriptionLevel | ssl) typeId :  change current subscription type");
        ModerationCommand.log("(subscriber | sub) (showClientInfo | sci) :  show client info about subscription");
        ModerationCommand.log("(subscriber | sub) (refreshSubscription | rs) :  update subscription");
    }
    
    static {
        m_logger = Logger.getLogger((Class)SubscriptionCommand.class);
    }
}
