package com.ankamagames.wakfu.client.core.game.storageBox;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;

public enum StorageBoxType
{
    HAVEN_BAG(0), 
    GUILD(1), 
    HOUSE(2), 
    MANSION(3), 
    THIEF(4);
    
    private final byte m_id;
    
    private StorageBoxType(final int id) {
        this.m_id = MathHelper.ensureByte(id);
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString("guild.storage.type." + this.m_id);
    }
    
    public String getIconUrl() {
        return WakfuConfiguration.getInstance().getIconUrl("guildStorageTypeIconsPath", "defaultIconPath", this.m_id);
    }
    
    public String getShortUnlockDescription() {
        return WakfuTranslator.getInstance().getString("guild.storage.unlock.shortDescription." + this.m_id);
    }
    
    public String getLongUnlockDescription() {
        return WakfuTranslator.getInstance().getString("guild.storage.unlock.longDescription." + this.m_id);
    }
}
