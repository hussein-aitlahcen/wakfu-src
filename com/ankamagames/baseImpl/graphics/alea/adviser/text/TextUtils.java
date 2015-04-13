package com.ankamagames.baseImpl.graphics.alea.adviser.text;

public class TextUtils
{
    private static final int DEFAULT_DURATION = 1000;
    private static final int DEFAULT_DURATION_MULT = 100;
    
    public static int getTextDuration(final String text) {
        int duration = 0;
        if (text != null) {
            duration = text.length() * 100;
        }
        return 1000 + duration;
    }
}
