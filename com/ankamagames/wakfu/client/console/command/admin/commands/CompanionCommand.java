package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.companion.freeCompanion.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.companion.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.account.*;
import java.util.*;

@Documentation(commandName = "companion | comp", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "comp h to show full documentation. Permit to manage companions.", commandObsolete = false)
public final class CompanionCommand extends ModerationCommand
{
    private static final Logger m_logger;
    public static final int HELP = 0;
    public static final int ACTIVATE_COMPANION = 1;
    public static final int DELETE_COMPANION = 2;
    public static final int COMPANION_LIST = 3;
    public static final int CLEAR = 4;
    public static final int RENAME_COMPANION = 5;
    public static final int UPDATE_LIST = 6;
    public static final int TO_ITEM = 7;
    public static final int ADD_XP = 8;
    public static final int ADD_TO_GROUP = 9;
    public static final int ADD_ITEM_TO_EQUIPMENT = 10;
    public static final int REMOVE_ITEM_FROM_EQUIPMENT = 11;
    public static final int SET_FREE_COMPANION = 12;
    public static final int SHOW_FREE = 13;
    public static final int SET_NEXT_FREE_COMPANION = 14;
    public static final int SET_COMPANION_TO_MAX_XP = 15;
    public static final int CHANGE_UNLOCK_GROUP_LIMIT = 16;
    public static final int SET_FREE_COMPANION_CYCLE_PERIOD = 17;
    private final int m_commandId;
    private final String[] m_args;
    
    public CompanionCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args.clone();
    }
    
    @Override
    public boolean isValid() {
        switch (this.m_commandId) {
            case 0:
            case 3:
            case 4:
            case 6:
            case 13:
            case 14:
            case 15:
            case 16: {
                return this.m_args.length == 0;
            }
            case 1:
            case 2:
            case 7:
            case 9:
            case 12: {
                return this.m_args.length == 1;
            }
            case 5:
            case 8: {
                return this.m_args.length == 2;
            }
            case 10: {
                return this.m_args.length == 3;
            }
            case 11:
            case 17: {
                return this.m_args.length == 4;
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
                    this.activateCompanion();
                }
                case 3: {
                    companionList();
                }
                case 4: {
                    this.clear();
                }
                case 5: {
                    this.renameCompanion();
                }
                case 6: {
                    this.updateList();
                }
                case 7: {
                    this.toItem();
                }
                case 8: {
                    this.addXp();
                }
                case 9: {
                    this.addToGroup();
                }
                case 10: {
                    this.addItemToEquipment();
                }
                case 11: {
                    this.removeItemFromEquipment();
                }
                case 12: {
                    this.setFreeCompanion();
                }
                case 13: {
                    showFree();
                }
                case 14: {
                    nextFree();
                }
                case 15: {
                    setCompanionToMaxXp();
                }
                case 16: {
                    changeUnlockGroupLimit();
                }
                case 17: {
                    this.setFreeCompanionCyclePeriod();
                }
            }
        }
        catch (Exception e) {
            CompanionCommand.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private void setFreeCompanionCyclePeriod() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)2);
        msg.setCommand((short)230);
        msg.addIntParameter(Integer.parseInt(this.m_args[0]));
        msg.addIntParameter(Integer.parseInt(this.m_args[1]));
        msg.addIntParameter(Integer.parseInt(this.m_args[2]));
        msg.addIntParameter(Integer.parseInt(this.m_args[3]));
        networkEntity.sendMessage(msg);
    }
    
    private static void changeUnlockGroupLimit() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        UnlockedCompanionGroupLimitManager.INSTANCE.changeLockStateFor(localPlayer.getClientId());
        UICompanionsEmbeddedFrame.refreshAllCompanionsLists();
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)6);
        msg.setCommand((short)181);
        networkEntity.sendMessage(msg);
    }
    
    private static void setCompanionToMaxXp() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)2);
        msg.setCommand((short)174);
        networkEntity.sendMessage(msg);
    }
    
    private static void showFree() {
        ModerationCommand.log("Compagnon \u00e0 l'essai : " + FreeCompanionManager.INSTANCE.getFreeCompanionBreedId());
    }
    
    private static void nextFree() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)2);
        msg.setCommand((short)173);
        networkEntity.sendMessage(msg);
    }
    
    private void setFreeCompanion() {
        final short freeCompanionBreedId = Short.parseShort(this.m_args[0]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.addShortParameter(freeCompanionBreedId);
        msg.setServerId((byte)2);
        msg.setCommand((short)172);
        networkEntity.sendMessage(msg);
    }
    
    private void removeItemFromEquipment() {
        final long companionId = Long.parseLong(this.m_args[0]);
        final long itemUid = Long.parseLong(this.m_args[1]);
        final long bagId = Long.parseLong(this.m_args[2]);
        final short position = Short.parseShort(this.m_args[3]);
        final RemoveItemFromCompanionEquipmentRequestMessage msg = new RemoveItemFromCompanionEquipmentRequestMessage(companionId, itemUid, bagId, position);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private void addItemToEquipment() {
        final long companionId = Long.parseLong(this.m_args[0]);
        final byte equipmentPosition = Byte.parseByte(this.m_args[1]);
        final long itemUid = Long.parseLong(this.m_args[2]);
        final AddItemToCompanionEquipmentRequestMessage msg = new AddItemToCompanionEquipmentRequestMessage(companionId, equipmentPosition, itemUid);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private void addToGroup() {
        final long companionId = Long.parseLong(this.m_args[0]);
        final Message msg = new AddCompanionToGroupRequestMessage(companionId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private void addXp() {
        final long companionId = Long.parseLong(this.m_args[0]);
        final long xpToAdd = Long.parseLong(this.m_args[1]);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)170);
        msg.addLongParameter(companionId);
        msg.addLongParameter(xpToAdd);
        networkEntity.sendMessage(msg);
    }
    
    private void toItem() {
        final long companionId = Long.parseLong(this.m_args[0]);
        final ItemizeCompanionRequestMessage msg = new ItemizeCompanionRequestMessage(companionId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    private void updateList() {
        sendSimpleCommand((short)169);
    }
    
    private void renameCompanion() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)168);
        final long companionId = Long.parseLong(this.m_args[0]);
        msg.addLongParameter(companionId);
        msg.addStringParameter(this.m_args[1]);
        networkEntity.sendMessage(msg);
    }
    
    private void clear() {
        sendSimpleCommand((short)167);
    }
    
    private static void sendSimpleCommand(final short commandId) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand(commandId);
        networkEntity.sendMessage(msg);
    }
    
    private static void companionList() {
        final LocalAccountInformations localPlayer = WakfuGameEntity.getInstance().getLocalAccount();
        final List<CompanionModel> companions = CompanionManager.INSTANCE.getCompanions(localPlayer.getAccountId());
        if (companions == null || companions.isEmpty()) {
            ModerationCommand.log("Aucun compagnon");
            return;
        }
        for (final CompanionModel companion : companions) {
            ModerationCommand.log(companion.toString() + "\n");
        }
    }
    
    private void activateCompanion() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)166);
        final int companionBreedId = Integer.parseInt(this.m_args[0]);
        msg.addIntParameter(companionBreedId);
        networkEntity.sendMessage(msg);
    }
    
    private static void help() {
        ModerationCommand.log("(companion | comp) (help | h) : show help");
        ModerationCommand.log("(companion | comp) (list | l) : show available companions");
        ModerationCommand.log("(companion | comp) (clear | c) : clear companions list");
        ModerationCommand.log("(companion | comp) (add | a) breedId : add companion of given id");
        ModerationCommand.log("(companion | comp) (rename | re) id \"nom\" : rename companion of given id ");
        ModerationCommand.log("(companion | comp) (update | u) : update companion list");
        ModerationCommand.log("(companion | comp) (toItem | ti) id : untie companion of given id");
        ModerationCommand.log("(companion | comp) (addXp | xp) id xpToAdd : add xp to companion of given id");
        ModerationCommand.log("(companion | comp) (addToGroup | atg) id : add companion to group");
        ModerationCommand.log("(companion | comp) (addEquipment | equip) id pos itemUid: add item on companion inventory at specified position");
        ModerationCommand.log("(companion | comp) (removeEquipment | rvequip) id itemUid bagId pos : unequip item of companion and put it on bag position given");
        ModerationCommand.log("(companion | comp) (setFreeCompanion | free) breedId : change default free companion, update free companion cycle reference date");
        ModerationCommand.log("(companion | comp) (setFreeCompanionCycle | sfcc) second minute hour day : change free companion cycle duration");
        ModerationCommand.log("(companion | comp) (showFree | sfree) : show current free companion id");
        ModerationCommand.log("(companion | comp) (nextFree) : finish free companion current time and launch next, update free companion cycle reference date");
        ModerationCommand.log("(companion | comp) (setCompanionToMaxXp | maxxp) : enable or disable companion max xp");
        ModerationCommand.log("(companion | comp) (changeUnlockGroupLimit | groupLimit | gl) : enable or disable max companion number in a group.");
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionCommand.class);
    }
}
