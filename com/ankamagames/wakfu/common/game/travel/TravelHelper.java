package com.ankamagames.wakfu.common.game.travel;

import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import java.util.*;

public abstract class TravelHelper
{
    public static final int DRAGO_FEE = 0;
    private static final TravelInfoManager INFO_MANAGER;
    private static final Map<TravelType, TravelProvider> PROVIDERS;
    
    public static void registerProvider(final TravelProvider... providers) {
        for (int i = 0, size = providers.length; i < size; ++i) {
            final TravelProvider provider = providers[i];
            if (TravelHelper.PROVIDERS.containsKey(provider.getType())) {
                throw new IllegalArgumentException("Enregistrement multiple non autoris\u00e9 pour le provider " + provider);
            }
            TravelHelper.PROVIDERS.put(provider.getType(), provider);
        }
    }
    
    public static TravelInfoManager getInfoManager() {
        return TravelHelper.INFO_MANAGER;
    }
    
    public static <Provider extends TravelProvider> Provider getProvider(final TravelType type) {
        return (Provider)TravelHelper.PROVIDERS.get(type);
    }
    
    public static boolean needsToPayForBoat(final TravelHandler handler, final BoatLink link) {
        return link.isNeedsToPayEverytime() || !handler.isBoatDiscovered((int)link.getId());
    }
    
    static {
        INFO_MANAGER = TravelInfoManager.INSTANCE;
        PROVIDERS = new EnumMap<TravelType, TravelProvider>(TravelType.class);
    }
}
