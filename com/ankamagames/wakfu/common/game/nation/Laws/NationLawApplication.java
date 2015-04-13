package com.ankamagames.wakfu.common.game.nation.Laws;

import java.util.*;

public enum NationLawApplication
{
    CITIZEN, 
    ALLIED_FOREIGNER, 
    NEUTRAL_FOREIGNER;
    
    public final int mask;
    
    private NationLawApplication(final int ordinal) {
        this.mask = 1 << this.ordinal();
    }
    
    public static int bitSet(final Iterable<NationLawApplication> applications) {
        int bitSet = 0;
        for (final NationLawApplication application : applications) {
            bitSet |= application.mask;
        }
        return bitSet;
    }
}
