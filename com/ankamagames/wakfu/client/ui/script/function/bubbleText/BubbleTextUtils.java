package com.ankamagames.wakfu.client.ui.script.function.bubbleText;

import com.ankamagames.wakfu.client.core.*;

final class BubbleTextUtils
{
    static String getTranslatedText(final String text, final Object... parameters) {
        final String translatedText = WakfuTranslator.getInstance().getString(text, parameters);
        if (translatedText == null) {
            return text;
        }
        return WakfuGameVariable.replace(translatedText);
    }
}
