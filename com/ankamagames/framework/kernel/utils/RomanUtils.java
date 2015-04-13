package com.ankamagames.framework.kernel.utils;

public class RomanUtils
{
    private static final String[] acceptedchar;
    private static final String[] _RRUTR;
    private static final String[] _RRDTR;
    private static final String[] _RRCTR;
    private static final String[] _RRMTR;
    
    static boolean IsRomanNumber(final String val) {
        for (int i = 0; i < val.length(); ++i) {
            boolean actuval = false;
            for (int j = 0; j < RomanUtils.acceptedchar.length; ++j) {
                if (val.substring(i, i + 1).equals(RomanUtils.acceptedchar[j])) {
                    actuval = true;
                }
            }
            if (!actuval) {
                return false;
            }
        }
        return true;
    }
    
    static boolean IsRomanNumber(final int val) {
        return val > 0 && val <= 39999;
    }
    
    static int RomanCharToValue(final char vv) {
        switch (vv) {
            case 'I': {
                return 1;
            }
            case 'V': {
                return 5;
            }
            case 'X': {
                return 10;
            }
            case 'L': {
                return 50;
            }
            case 'C': {
                return 100;
            }
            case 'D': {
                return 500;
            }
            case 'M': {
                return 1000;
            }
            default: {
                return 0;
            }
        }
    }
    
    public static String IntToRoman(final int value) {
        if (IsRomanNumber(value)) {
            if (value >= 0 && value <= 9) {
                return RomanUtils._RRUTR[value];
            }
            if (value >= 10 && value <= 99) {
                return RomanUtils._RRDTR[value / 10] + RomanUtils._RRUTR[value % 10];
            }
            if (value >= 100 && value <= 999) {
                return RomanUtils._RRCTR[value / 100] + RomanUtils._RRDTR[value / 10 % 10] + RomanUtils._RRUTR[value % 10];
            }
            if (value >= 1000 && value <= 3999) {
                return RomanUtils._RRMTR[value / 1000] + RomanUtils._RRCTR[value / 100 % 10] + RomanUtils._RRDTR[value / 10 % 10] + RomanUtils._RRUTR[value % 10];
            }
        }
        return "";
    }
    
    public static int RomanToInt(final String value) {
        int OldRV = 0;
        int Result = 0;
        if (IsRomanNumber(value)) {
            for (int oc = 0; oc < value.length(); ++oc) {
                final int NewRV = RomanCharToValue(value.toCharArray()[oc]);
                if (NewRV > OldRV) {
                    Result += NewRV - (OldRV << 1);
                }
                else {
                    Result += NewRV;
                }
                OldRV = NewRV;
            }
            return Result;
        }
        return -1;
    }
    
    static {
        acceptedchar = new String[] { "I", "V", "X", "L", "C", "D", "M" };
        _RRUTR = new String[] { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" };
        _RRDTR = new String[] { "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC" };
        _RRCTR = new String[] { "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" };
        _RRMTR = new String[] { "", "M", "MM", "MMM", "", "", "", "", "", "" };
    }
}
