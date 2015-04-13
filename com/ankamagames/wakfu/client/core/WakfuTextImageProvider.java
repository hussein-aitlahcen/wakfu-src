package com.ankamagames.wakfu.client.core;

import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class WakfuTextImageProvider implements TextImageProvider
{
    public static final WakfuTextImageProvider INSTANCE;
    public static final byte ADD = 0;
    public static final byte PA = 1;
    public static final byte PM = 2;
    public static final byte PO = 3;
    public static final byte PDV = 4;
    public static final byte PW = 5;
    public static final byte TREASURE = 6;
    public static final byte UNSELECTED_TREASURE = 7;
    public static final byte CITIZEN_POINTS = 9;
    public static final byte CHRAGE = 10;
    public static final byte CONE_AOE = 11;
    public static final byte BOX = 12;
    public static final byte BLUE_PDV = 39;
    
    @Override
    public String getIconUrl(final byte id, final String align) throws PropertyException {
        return new TextWidgetFormater().openText().addImage(_getIconUrl(id), -1, -1, align).finishAndToString();
    }
    
    public static String _getIconUrl(final byte id) throws PropertyException {
        return String.format(WakfuConfiguration.getInstance().getString("textIconsPath"), id);
    }
    
    static {
        INSTANCE = new WakfuTextImageProvider();
    }
}
