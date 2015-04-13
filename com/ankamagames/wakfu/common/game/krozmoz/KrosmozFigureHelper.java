package com.ankamagames.wakfu.common.game.krozmoz;

import org.apache.commons.lang3.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;

public class KrosmozFigureHelper
{
    public static final KrosmozFigure INVALID_KROSMOZ_FIGURE;
    public static final int PHYSICAL_KROSMOZ_PEDESTAL = 1;
    public static final int WAKFU_KROSMOZ_PEDESTAL = 5;
    public static final int FORCED_PEDESTAL = 5;
    private static final int[] VALID_PEDESTALS;
    
    public static boolean isValidPedestal(final int pedestal) {
        return ArrayUtils.contains(KrosmozFigureHelper.VALID_PEDESTALS, pedestal);
    }
    
    public static KrosmozFigure unserializeKrosmozFigure(final ByteBuffer buffer) {
        final RawKrosmozFigure rawKrosmozFigure = new RawKrosmozFigure();
        rawKrosmozFigure.unserialize(buffer);
        final KrosmozFigure figure = new KrosmozFigure();
        figure.fromRaw(rawKrosmozFigure);
        return figure;
    }
    
    static {
        INVALID_KROSMOZ_FIGURE = new KrosmozFigure("", -1, -1, null, "", false);
        VALID_PEDESTALS = new int[] { 1, 5 };
    }
}
