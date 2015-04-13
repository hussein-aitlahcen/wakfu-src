package com.ankamagames.wakfu.common.constants;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class ActivationConstants
{
    public static final boolean GAZETTE_DEBUG = false;
    public static final boolean ALMANACH_BROWSE_DEBUG = false;
    private static final boolean KROSMOZ_DEBUG = false;
    public static final boolean ACTIVATE_CHAOS = false;
    public static final GameDateConst ALMANAX_UNLOCK_DATE;
    public static final GameDateConst KROZMASTER_UNLOCK_DATE;
    
    static {
        ALMANAX_UNLOCK_DATE = new GameDate(0, 0, 0, 18, 9, 2012);
        KROZMASTER_UNLOCK_DATE = new GameDate(0, 0, 0, 11, 12, 2012);
    }
}
