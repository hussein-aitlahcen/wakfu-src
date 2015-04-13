package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class CraftOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private final CraftInteractiveElement m_table;
    private final CraftRecipe m_recipe;
    private final long m_duration;
    private static final Runnable CRAFT_SCHEDULER;
    
    public CraftOccupation(final CraftInteractiveElement craftTable, final CraftRecipe recipe, final long duration) {
        super();
        this.m_table = craftTable;
        this.m_recipe = recipe;
        this.m_duration = duration;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 8;
    }
    
    @Override
    public boolean isAllowed() {
        if (this.m_localPlayer.isDead() || this.m_localPlayer.isOnFight()) {
            CraftOccupation.m_logger.warn((Object)("Le joueur " + this.m_localPlayer + " est mort ou en combat et ne peut utiliser de machine de craft"));
            return false;
        }
        if (!this.m_localPlayer.getCraftHandler().contains(this.m_table.getCraftId())) {
            CraftOccupation.m_logger.warn((Object)("Le joueur " + this.m_localPlayer + " essaye d'utiliser la recette " + this.m_recipe + " du m\u00e9tier " + this.m_table.getCraftId() + " sur la machine " + this.m_table + " alors qu'il ne connais pas le m\u00e9tier"));
            return false;
        }
        if (!this.m_table.isRecipeAllowed(this.m_recipe.getId(), this.m_recipe.getType())) {
            CraftOccupation.m_logger.warn((Object)("Le joueur " + this.m_localPlayer + " essaye d'utiliser la recette " + this.m_recipe + " du m\u00e9tier " + this.m_table.getCraftId() + " sur la machine " + this.m_table + " alors qu'elle n'est pas autoris\u00e9e"));
            return false;
        }
        if (!this.m_recipe.isValid(this.m_localPlayer)) {
            CraftOccupation.m_logger.warn((Object)("Le joueur " + this.m_localPlayer + " essaye d'utiliser la recette " + this.m_recipe + " du m\u00e9tier " + this.m_table.getCraftId() + " sur la machine " + this.m_table + " alors qu'elle ne valide pas le crit\u00e8re"));
            return false;
        }
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_table.getVisualId());
        if (!this.isOwnerEquippedForAction(visual)) {
            CraftOccupation.m_logger.warn((Object)("Le joueur " + this.m_localPlayer + " essaye d'utiliser la recette " + this.m_recipe + " du m\u00e9tier " + this.m_table.getCraftId() + " sur la machine " + this.m_table + " alors qu'il n'a pas l'objet requis dans le visuel"));
            return false;
        }
        final ClientBagContainer bags = this.m_localPlayer.getBags();
        return this.m_localPlayer.canAffordRecipe(this.m_recipe);
    }
    
    @Override
    public void begin() {
        CraftOccupation.m_logger.error((Object)("[CRAFTOCC] Craft START id=" + this.m_table.getCraftId()));
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_table.getVisualId());
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)this.m_localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
        this.m_localPlayer.getActor().setVisualAnimation(visual.getVisualId(), false);
        UICraftTableFrame.getInstance().startCraft(this.m_duration);
        ProcessScheduler.getInstance().schedule(CraftOccupation.CRAFT_SCHEDULER, this.m_duration, 1);
    }
    
    @Override
    public boolean finish() {
        CraftOccupation.m_logger.error((Object)("[CRAFTOCC] Craft FINISHED id=" + this.m_table.getCraftId()));
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType((byte)2);
        netMsg.setOccupationType((short)8);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_table.getVisualId());
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)this.m_localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
        this.m_localPlayer.getActor().setVisualAnimation(visual.getVisualId(), true);
        return true;
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        CraftOccupation.m_logger.error((Object)"[CRAFTOCC] Craft CANCELED");
        ProcessScheduler.getInstance().remove(CraftOccupation.CRAFT_SCHEDULER);
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_table.getVisualId());
        final Item item = ((ArrayInventoryWithoutCheck<Item, R>)this.m_localPlayer.getEquipmentInventory()).getFromPosition(EquipmentPosition.ACCESSORY.m_id);
        this.m_localPlayer.getActor().setVisualAnimation(visual.getVisualId(), true);
        UICraftTableFrame.getInstance().initializeUi();
        if (sendMessage) {
            this.requestCancel();
        }
        return true;
    }
    
    public void requestCancel() {
        CraftOccupation.m_logger.error((Object)"[CRAFTOCC] Craft CANCEL request");
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType((byte)3);
        netMsg.setOccupationType((short)8);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CraftOccupation.class);
        CRAFT_SCHEDULER = new Runnable() {
            @Override
            public void run() {
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                ProcessScheduler.getInstance().remove(this);
                player.finishCurrentOccupation();
            }
        };
    }
}
