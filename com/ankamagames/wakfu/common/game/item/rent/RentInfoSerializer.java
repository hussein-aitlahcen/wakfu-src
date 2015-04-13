package com.ankamagames.wakfu.common.game.item.rent;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public final class RentInfoSerializer
{
    private static final Logger m_logger;
    
    public static RentInfo unserialize(final RawRentInfo rawRentInfo) {
        final int type = rawRentInfo.type;
        RentInfo res = null;
        switch (type) {
            case 1: {
                res = new FightLimitedRentInfo();
                break;
            }
            case 2: {
                res = new TimeLimitedRentInfo();
                break;
            }
            default: {
                RentInfoSerializer.m_logger.error((Object)("Type de location inconnu " + type));
                return null;
            }
        }
        res.fromRaw(rawRentInfo);
        return res;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RentInfoSerializer.class);
    }
}
