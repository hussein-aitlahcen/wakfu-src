package com.ankamagames.wakfu.common.game.market.constant;

import gnu.trove.*;

public enum PackType
{
    ONE(1, 1), 
    TEN(2, 10), 
    FIFTEEN(3, 50), 
    HUNDRED(4, 100);
    
    private static final TByteObjectHashMap<PackType> HELPER;
    public final byte id;
    public final short qty;
    
    private PackType(final int id, final int quantity) {
        this.id = (byte)id;
        this.qty = (short)quantity;
    }
    
    public static PackType fromId(final byte id) {
        return PackType.HELPER.get(id);
    }
    
    public static PackType fromQuantity(final short quantity) {
        final PackType[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final PackType value = values[i];
            if (value.qty == quantity) {
                return value;
            }
        }
        return null;
    }
    
    static {
        HELPER = new TByteObjectHashMap<PackType>();
        final PackType[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final PackType value = values[i];
            PackType.HELPER.put(value.id, value);
        }
    }
}
