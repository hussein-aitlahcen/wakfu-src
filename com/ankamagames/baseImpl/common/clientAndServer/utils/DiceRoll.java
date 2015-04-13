package com.ankamagames.baseImpl.common.clientAndServer.utils;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;

public final class DiceRoll
{
    protected static final Logger m_logger;
    
    public static int roll(final int diceValue) {
        if (diceValue <= 0) {
            DiceRoll.m_logger.error((Object)("DiceRoll.roll appel\u00e9 avec une valeur de d\u00e9 de " + diceValue + "\n" + ExceptionFormatter.toString(new RuntimeException("StackTrace de DiceRoll"))));
            return 1;
        }
        return MersenneTwister.getInstance().nextInt(diceValue) + 1;
    }
    
    public static long roll(final long diceValue) {
        if (diceValue <= 0L) {
            DiceRoll.m_logger.error((Object)("DiceRoll.roll appel\u00e9 avec une valeur de d\u00e9 de " + diceValue + "\n" + ExceptionFormatter.toString(new RuntimeException("StackTrace de DiceRoll"))));
            return 1L;
        }
        return MersenneTwister.getInstance().nextLong(diceValue) + 1L;
    }
    
    public static int roll(final int diceCount, final int diceValue, final int moderator) {
        if (diceValue <= 0) {
            DiceRoll.m_logger.error((Object)("DiceRoll.roll appel\u00e9 avec une valeur de d\u00e9 de " + diceValue + "\n" + ExceptionFormatter.toString(new RuntimeException("StackTrace de DiceRoll"))));
            return 1;
        }
        int total = moderator + diceCount;
        if (diceValue > 0 && diceCount > 0) {
            for (int i = diceCount; i > 0; --i) {
                total += MersenneTwister.getInstance().nextInt(diceValue);
            }
        }
        return total;
    }
    
    public static int roll(final int min, final int max) {
        assert min > 0;
        assert max >= min;
        int total;
        if ((total = min) > 0 && max > 0 && max - min > 0) {
            total = min + MersenneTwister.getInstance().nextInt(max - min + 1);
        }
        return total;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DiceRoll.class);
    }
}
