package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UIFightSpellCastAndItemUseInteractionFrame extends UIAbstractFightCastInteractionFrame
{
    private static UIFightSpellCastAndItemUseInteractionFrame m_instance;
    private Item m_item;
    private byte m_equipmentPos;
    private SpellLevel m_selectedSpell;
    
    private UIFightSpellCastAndItemUseInteractionFrame() {
        super();
        this.m_item = null;
        this.m_selectedSpell = null;
        this.m_rangeDisplayer = ItemUseDisplayZone.getInstance();
        ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).addHighlightCellProvidersToUpdate((CustomTextureHighlightingProvider)this.m_rangeDisplayer);
    }
    
    public static UIFightSpellCastAndItemUseInteractionFrame getInstance() {
        return UIFightSpellCastAndItemUseInteractionFrame.m_instance;
    }
    
    public void setSelectedSpell(final SpellLevel selectedSpell) {
        this.m_selectedSpell = selectedSpell;
    }
    
    public SpellLevel getSelectedSpell() {
        return this.m_selectedSpell;
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
        final SpellLevelCastAndItemUseRequestMessage netMessage = new SpellLevelCastAndItemUseRequestMessage();
        netMessage.setFighterId(this.m_character.getId());
        netMessage.setItemUid(this.m_item.getUniqueId());
        netMessage.setEquipmentPos(this.m_equipmentPos);
        netMessage.setSpellId(this.m_selectedSpell.getUniqueId());
        netMessage.setCastPosition(castPositionX, castPositionY, castPositionZ);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
    }
    
    @Override
    protected void updateUsage() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.m_character == localPlayer) {
            localPlayer.incrementFightSpellUsage(this.m_item);
            localPlayer.incrementFightSpellUsage(this.m_selectedSpell);
        }
    }
    
    @Override
    protected String getCastMouseIcon() {
        if (this.m_item != null) {
            return (String)this.m_item.getFieldValue("smallIconUrl");
        }
        return null;
    }
    
    @Override
    protected byte getCastType() {
        return 2;
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
        UIFightSpellCastAndItemUseInteractionFrame.m_instance = new UIFightSpellCastAndItemUseInteractionFrame();
    }
}
