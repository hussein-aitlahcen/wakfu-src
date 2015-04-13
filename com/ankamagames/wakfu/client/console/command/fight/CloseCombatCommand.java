package com.ankamagames.wakfu.client.console.command.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class CloseCombatCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.get(2).length() >= 1) {
            final ShortcutBarManager shortcutBarManager = WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager();
            ShortCutItem shortcutItem = null;
            if (shortcutBarManager.getCurrentBarType() == ShortCutBarType.SYMBIOT_BAR) {
                return;
            }
            switch (args.get(2).charAt(0)) {
                case 'l': {
                    shortcutItem = shortcutBarManager.getLeftHandWeaponShortcut();
                    break;
                }
                case 'r': {
                    shortcutItem = shortcutBarManager.getRightHandWeaponShortcut();
                    break;
                }
            }
            Item item;
            byte position;
            if (shortcutItem.getReferenceId() == 2145) {
                final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
                final Item realItem = new Item(-1L);
                realItem.initializeWithReferenceItem(refItem);
                realItem.setQuantity((short)1);
                item = realItem;
                position = 15;
            }
            else {
                item = ((ArrayInventoryWithoutCheck<Item, R>)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory()).getWithUniqueId(shortcutItem.getUniqueId());
                position = (byte)WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory().getPosition(item.getUniqueId());
            }
            if (item == null) {
                CloseCombatCommand.m_logger.error((Object)("impossible de retrouver l'item d'id " + shortcutItem.getUniqueId() + " suppos\u00e9 plac\u00e9 dans les mains du personnage"));
                return;
            }
            if (!item.isActive()) {
                return;
            }
            final UIFighterSelectAttackMessage message = new UIFighterSelectAttackMessage();
            message.setItem(item, position);
            message.setId(18009);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CloseCombatCommand.class);
    }
}
