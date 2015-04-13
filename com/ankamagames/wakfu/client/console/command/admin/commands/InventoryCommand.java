package com.ankamagames.wakfu.client.console.command.admin.commands;

import com.ankamagames.wakfu.client.console.command.admin.commands.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import gnu.trove.*;

@Documentation(commandName = "inventory | inv", commandParameters = "&lt;help&gt;", commandRights = { AdminRightsGroup.ADMINISTRATOR }, commandDescription = "inv h to show full documentation.", commandObsolete = false)
public final class InventoryCommand extends ModerationCommand
{
    public static final int HELP = 0;
    public static final int EMPTY = 1;
    public static final int REPACK = 2;
    public static final int SHUFFLE = 3;
    public static final int DESCRIBE = 4;
    public static final int REMOVE_REF_ITEM = 5;
    private static final short[] SHORTS;
    private final int m_commandId;
    private final List<Integer> m_priorities;
    private final String[] m_args;
    
    public InventoryCommand(final int commandId) {
        super();
        this.m_commandId = commandId;
        this.m_priorities = null;
        this.m_args = null;
    }
    
    public InventoryCommand(final int commandId, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_args = args;
        this.m_priorities = null;
    }
    
    public InventoryCommand(final int commandId, final List<Integer> priorities) {
        super();
        this.m_commandId = commandId;
        this.m_priorities = priorities;
        this.m_args = null;
    }
    
    public InventoryCommand(final int commandId, final List<Integer> priorities, final String... args) {
        super();
        this.m_commandId = commandId;
        this.m_priorities = priorities;
        this.m_args = args;
    }
    
    @Override
    public boolean isValid() {
        return true;
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
                    break;
                }
                case 1: {
                    reset();
                    break;
                }
                case 2: {
                    this.repack();
                    break;
                }
                case 3: {
                    shuffle();
                    break;
                }
                case 4: {
                    describe();
                    break;
                }
                case 5: {
                    this.removeRefItem();
                    break;
                }
            }
        }
        catch (Exception e) {
            ConsoleManager.getInstance().err("Probl\u00e8me d'une commande" + e);
        }
    }
    
    private void removeRefItem() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)143);
        netMessage.addIntParameter(5);
        netMessage.addIntParameter(Integer.parseInt(this.m_args[0]));
        netMessage.addIntParameter(Integer.parseInt(this.m_args[1]));
        networkEntity.sendMessage(netMessage);
    }
    
    private static void describe() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientBagContainer bags = localPlayer.getBags();
        final TLongObjectIterator<AbstractBag> bagsIterator = bags.getBagsIterator();
        while (bagsIterator.hasNext()) {
            bagsIterator.advance();
            final AbstractBag bag = bagsIterator.value();
            ModerationCommand.log(bag.toString());
            for (final Item item : bag) {
                ModerationCommand.log(" \t" + item);
            }
        }
    }
    
    private static void shuffle() {
        sendCommandToGameServer(3);
    }
    
    private void repack() {
        sendCommandToGameServer(2);
        final byte bagId = (byte)((this.m_args != null && this.m_args.length > 0) ? Byte.parseByte(this.m_args[0]) : -1);
        final short[] priorities = (this.m_priorities != null) ? new short[this.m_priorities.size()] : InventoryCommand.SHORTS;
        if (this.m_priorities != null) {
            for (int i = 0, n = this.m_priorities.size(); i < n; ++i) {
                priorities[i] = (short)(Object)this.m_priorities.get(i);
            }
        }
        final InventoryRepackRequestMessage msg = new InventoryRepackRequestMessage(bagId, priorities);
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        networkEntity.sendMessage(msg);
    }
    
    private static void reset() {
        sendCommandToGameServer(1);
    }
    
    private static void help() {
        ModerationCommand.log("help | h : show full documentation");
        ModerationCommand.log("-empty | -e : delete full inventory (except quest inventory)");
        ModerationCommand.log("-repack | -rp : automatic sort of inventory");
        ModerationCommand.log("-repack | -rp (typeof item id list) : automatic sort with priority. ex : inv -rp  100 106 226 will sort spell first then consumables then resources. The rest will be sorted with default param");
        ModerationCommand.log("-repackBag | -rpb bagPos (typeof item id list): same one but only for a bag.");
        ModerationCommand.log("-shuffle | -s : Shuffle items");
        ModerationCommand.log("-describe | -desc : show text of inventory");
        ModerationCommand.log("-removeRefItem | -rri refId qty : destroy qty of specified item. qty < 0 to destroy all");
    }
    
    private static void sendCommandToGameServer(final int commandId) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)143);
        netMessage.addIntParameter(commandId);
        networkEntity.sendMessage(netMessage);
    }
    
    static {
        SHORTS = new short[0];
    }
}
