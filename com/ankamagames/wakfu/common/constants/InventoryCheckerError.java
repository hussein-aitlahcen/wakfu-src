package com.ankamagames.wakfu.common.constants;

import gnu.trove.*;

public class InventoryCheckerError
{
    private static final TIntIntHashMap ERROR_TO_RESULT_CONSTANTS;
    
    public static int getResultCode(final int inventoryContentResult, final int defaultError) {
        final int result = InventoryCheckerError.ERROR_TO_RESULT_CONSTANTS.get(inventoryContentResult);
        return (result == 0) ? defaultError : result;
    }
    
    static {
        (ERROR_TO_RESULT_CONSTANTS = new TIntIntHashMap()).put(-11, 67);
    }
}
