package com.ankamagames.wakfu.common.constants;

public class RecustomHelper
{
    private static final short _RECUSTOM_SEX = 1;
    private static final short _RECUSTOM_NAME = 2;
    private static final short _RECUSTOM_COLOR = 4;
    private static final short _RECUSTOM_APPEARANCE = 8;
    private static final short _RECUSTOM_BREED = 16;
    private static final short _RECUSTOM_APTITUDES = 32;
    private static final short _RECUSTOM_SPELLS = 64;
    public static final short NO_RECUSTOM = 0;
    public static final short NAME_RECUSTOM = 2;
    public static final short COLOR_RECUSTOM = 4;
    public static final short PHYSICAL_APPEARANCE = 12;
    public static final short SEX_RECUSTOM = 13;
    public static final short BREED_RESCUSTOM = 125;
    public static final short FULL_RESCUSTOM = 127;
    
    public static boolean isSexRecustom(final short value) {
        return contains(value, (short)1);
    }
    
    public static boolean isNameRecustom(final short value) {
        return contains(value, (short)2);
    }
    
    public static boolean isBreedRecustom(final short value) {
        return contains(value, (short)16);
    }
    
    public static boolean isColorRecustom(final short value) {
        return contains(value, (short)4);
    }
    
    public static boolean isAppearanceRecustom(final short value) {
        return contains(value, (short)8);
    }
    
    public static boolean isSpellsRecustom(final short value) {
        return contains(value, (short)64);
    }
    
    public static boolean isAptitudesRecustom(final short value) {
        return contains(value, (short)32);
    }
    
    private static boolean contains(final short value, final short comparator) {
        return (value & comparator) == comparator;
    }
}
