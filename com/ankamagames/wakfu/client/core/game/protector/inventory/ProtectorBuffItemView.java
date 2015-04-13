package com.ankamagames.wakfu.client.core.game.protector.inventory;

import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.*;

public class ProtectorBuffItemView extends ProtectorMerchantItemView
{
    private final int m_buffId;
    private final ProtectorBuff m_buff;
    
    public ProtectorBuffItemView(final ProtectorMerchantInventoryItem merchantItem, final int buffId) {
        super(merchantItem);
        this.m_buffId = buffId;
        this.m_buff = (ProtectorBuff)ProtectorBuffManager.INSTANCE.getBuff(buffId);
    }
    
    @Override
    public String getName() {
        final String name = this.m_buff.getName();
        return (name != null) ? name : "";
    }
    
    @Override
    public ProtectorWalletContext getWalletContext() {
        return ProtectorWalletContext.BUFF;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("description")) {
            final String desc = this.m_buff.getDescription();
            return (desc != null) ? desc : "";
        }
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("protectorBuffsIconsPath", "defaultIconPath", this.m_buff.getGfxId());
        }
        return super.getFieldValue(fieldName);
    }
    
    public int getBuffId() {
        return this.m_buffId;
    }
}
