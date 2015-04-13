package com.ankamagames.wakfu.client.core.game.travel.provider;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public abstract class ClientTravelProvider extends TravelProvider
{
    private static final Logger m_logger;
    
    @Override
    protected void onError(final TravelError error) {
        switch (error) {
            case NOT_ENOUGH_KAMA: {
                ErrorsMessageTranslator.getInstance().pushMessage(3, 3, new Object[0]);
                break;
            }
            case CRITERION_FAIL: {
                ErrorsMessageTranslator.getInstance().pushMessage(107, 3, new Object[0]);
                break;
            }
            default: {
                ClientTravelProvider.m_logger.error((Object)("Erreur " + error + " non g\u00e9r\u00e9e sur un transporteur " + this.getType()));
                break;
            }
        }
    }
    
    public abstract String getOverHeadInfo(final TravelMachine p0);
    
    @Nullable
    public abstract String getTravelCostLabel(final TravelMachine p0);
    
    public abstract boolean canUse(final LocalPlayerCharacter p0, final TravelMachine p1);
    
    static {
        m_logger = Logger.getLogger((Class)ClientTravelProvider.class);
    }
}
