package com.ankamagames.wakfu.common.game.chaos;

import com.ankamagames.framework.external.*;

public enum ChaosInteractiveCategory implements ExportableEnum
{
    NO_CHAOS(0, "N'est pas impact\u00e9 par le chaos"), 
    LOW(1, "Chaos mineur"), 
    MEDIUM(2, "Chaos basique"), 
    HIGH(3, "Chaos majeur");
    
    private final byte id;
    private final String desc;
    
    private ChaosInteractiveCategory(final int id, final String desc) {
        this.id = (byte)id;
        this.desc = desc;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return this.desc;
    }
    
    public static ChaosInteractiveCategory fromId(final byte idx) {
        final ChaosInteractiveCategory[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final ChaosInteractiveCategory value = values[i];
            if (value.id == idx) {
                return value;
            }
        }
        return null;
    }
}
