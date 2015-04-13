package com.ankamagames.baseImpl.common.clientAndServer.account.admin;

import java.util.*;

public class AdminRightHelper
{
    public static final int MAX_SHIFT = 32;
    public static final int[] NO_RIGHT;
    
    public static int getMaskArraySize() {
        return AdminRightsEnum.values().length;
    }
    
    public static void addRight(final int[] adminRights, final short rightToAdd) {
        final short n = (short)(rightToAdd / 32);
        adminRights[n] |= 1 << rightToAdd % 32;
    }
    
    public static void addRight(final int[] adminRights, final AdminRightsEnum rightToAdd) {
        addRight(adminRights, rightToAdd.getId());
    }
    
    public static boolean checkRights(final int[] adminRights, final int[] rightsToCheck) {
        return Arrays.equals(adminRights, rightsToCheck);
    }
    
    public static boolean checkRight(final int[] adminRights, final AdminRightsEnum rightToCheck) {
        return checkRight(adminRights, rightToCheck.getId());
    }
    
    public static boolean checkRight(final int[] adminRights, final short rightToCheck) {
        return (adminRights[rightToCheck / 32] & 1 << rightToCheck % 32) != 0x0;
    }
    
    static {
        NO_RIGHT = new int[getMaskArraySize()];
    }
}
