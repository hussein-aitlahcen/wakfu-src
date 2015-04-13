package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class UIFightItemUseInteractionFrame extends UIAbstractFightCastInteractionFrame
{
    private static UIFightItemUseInteractionFrame m_instance;
    private Item m_item;
    private byte m_equipmentPos;
    
    private UIFightItemUseInteractionFrame() {
        super();
        this.m_item = null;
        this.m_rangeDisplayer = ItemUseDisplayZone.getInstance();
        ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).addHighlightCellProvidersToUpdate((CustomTextureHighlightingProvider)this.m_rangeDisplayer);
    }
    
    public static UIFightItemUseInteractionFrame getInstance() {
        return UIFightItemUseInteractionFrame.m_instance;
    }
    
    public void setSelectedItem(final Item selectedItem, final byte equipmentPos) {
        this.m_item = selectedItem;
        this.m_equipmentPos = equipmentPos;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    @Override
    protected EffectContainer getEffectContainer() {
        return this.m_item;
    }
    
    @Override
    protected void sendCastMessage(final int castPositionX, final int castPositionY, final short castPositionZ) {
        final FighterUseItemRequestMessage netMessage = new FighterUseItemRequestMessage();
        netMessage.setFighterId(this.m_character.getId());
        netMessage.setItemUid(this.m_item.getUniqueId());
        netMessage.setEquipmentPos(this.m_equipmentPos);
        netMessage.setCastPosition(castPositionX, castPositionY, castPositionZ);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    protected void updateUsage() {
        if (this.m_character == WakfuGameEntity.getInstance().getLocalPlayer()) {
            WakfuGameEntity.getInstance().getLocalPlayer().incrementFightSpellUsage(this.m_item);
        }
    }
    
    @Override
    protected String getCastMouseIcon() {
        if (this.m_item != null) {
            return (String)this.m_item.getFieldValue("iconUrl");
        }
        return null;
    }
    
    @Override
    protected byte getCastType() {
        return 0;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void selectRange() {
        super.selectRange();
        if (this.m_item != null && this.m_character != null) {
            ((ItemUseDisplayZone)this.m_rangeDisplayer).selectItemRange(this.m_item, this.m_character);
        }
    }
    
    @Override
    protected String getMouseText() {
        return "";
    }
    
    static {
        UIFightItemUseInteractionFrame.m_instance = new UIFightItemUseInteractionFrame();
    }
}
