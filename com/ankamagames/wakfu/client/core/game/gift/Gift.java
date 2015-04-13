package com.ankamagames.wakfu.client.core.game.gift;

import com.ankamagames.framework.reflect.*;

public interface Gift extends FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String ALLOW_CONTROLS_FIELD = "allowControls";
    public static final String[] FIELDS = { "iconUrl", "name", "quantity" };
    
    boolean consume(boolean p0);
}
