package com.ankamagames.wakfu.common.game.shortcut;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public enum ShortCutBarType
{
    WORLD((InventoryContentChecker)new NoSpellShortcutInventoryContentChecker(), (short)10, (byte)4, true), 
    FIGHT((InventoryContentChecker)new SpellOrItemShortcutInventoryContentChecker(), (short)8, (byte)4, true), 
    SYMBIOT_BAR((InventoryContentChecker)new SpellOrItemShortcutInventoryContentChecker(), (short)10, (byte)1, false);
    
    private static int TOTAL_COUNT;
    private static int SERIALIZED_TOTAL_COUNT;
    public static final ShortCutBarType[] SERIALIZED_VALUES;
    private final InventoryContentChecker m_checker;
    private final short m_maxSize;
    private final byte m_count;
    private byte m_firstIndex;
    private byte m_serializedFirstIndex;
    private final boolean m_serialized;
    
    private ShortCutBarType(final InventoryContentChecker checker, final short maxSize, final byte count, final boolean serialized) {
        this.m_checker = checker;
        this.m_maxSize = maxSize;
        this.m_count = count;
        this.m_serialized = serialized;
    }
    
    public InventoryContentChecker getChecker() {
        return this.m_checker;
    }
    
    public boolean isSerialized() {
        return this.m_serialized;
    }
    
    public short getMaxSize() {
        return this.m_maxSize;
    }
    
    public byte getCount() {
        return this.m_count;
    }
    
    public short getFirstIndex() {
        return this.m_firstIndex;
    }
    
    public short getSerializedFirstIndex() {
        return this.m_serializedFirstIndex;
    }
    
    public static int getTotalBarCount() {
        return ShortCutBarType.TOTAL_COUNT;
    }
    
    public static int getSerializedTotalCount() {
        return ShortCutBarType.SERIALIZED_TOTAL_COUNT;
    }
    
    public static ShortCutBarType[] serializedValues() {
        return ShortCutBarType.SERIALIZED_VALUES;
    }
    
    public static byte getFirstBarIndexOfType(final ShortCutBarType type) {
        byte firstIndex = 0;
        for (final ShortCutBarType t : values()) {
            if (t == type) {
                break;
            }
            firstIndex += t.getCount();
        }
        return firstIndex;
    }
    
    public static byte getSerializedFirstBarIndexOfType(final ShortCutBarType type) {
        byte firstIndex = 0;
        for (final ShortCutBarType t : serializedValues()) {
            if (t == type) {
                break;
            }
            firstIndex += t.getCount();
        }
        return firstIndex;
    }
    
    static {
        ShortCutBarType.TOTAL_COUNT = 0;
        ShortCutBarType.SERIALIZED_TOTAL_COUNT = 0;
        int serializedTypes = 0;
        for (final ShortCutBarType type : values()) {
            type.m_firstIndex = getFirstBarIndexOfType(type);
            ShortCutBarType.TOTAL_COUNT += type.getCount();
            if (type.isSerialized()) {
                ++serializedTypes;
                ShortCutBarType.SERIALIZED_TOTAL_COUNT += type.getCount();
            }
        }
        SERIALIZED_VALUES = new ShortCutBarType[serializedTypes];
        int currentIndex = 0;
        for (final ShortCutBarType type2 : values()) {
            if (type2.isSerialized()) {
                ShortCutBarType.SERIALIZED_VALUES[currentIndex] = type2;
                ++currentIndex;
            }
        }
        for (final ShortCutBarType type2 : serializedValues()) {
            type2.m_serializedFirstIndex = getSerializedFirstBarIndexOfType(type2);
        }
    }
}
