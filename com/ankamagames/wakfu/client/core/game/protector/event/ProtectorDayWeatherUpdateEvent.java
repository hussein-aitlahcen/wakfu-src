package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public class ProtectorDayWeatherUpdateEvent extends ProtectorEvent
{
    @Override
    public ProtectorMood getProtectorMood() {
        return ProtectorMood.NEUTRAL;
    }
}
