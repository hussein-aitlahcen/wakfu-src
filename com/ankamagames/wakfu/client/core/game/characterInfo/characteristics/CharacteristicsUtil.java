package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class CharacteristicsUtil
{
    public static String displayPercentage(final int value, final int max, final boolean format) {
        final boolean displayPositiveSignum = true;
        return displayPercentage(value, max, format, true);
    }
    
    public static String displayPercentage(final int value, final int max, final boolean format, final boolean displayPositiveSignum) {
        String signum = "";
        String color;
        if (max != -1 && value == max) {
            signum = ((displayPositiveSignum && value >= 0) ? "+" : "");
            color = "FFF82D";
        }
        else if (value < 0) {
            color = "C62700";
        }
        else {
            signum = (displayPositiveSignum ? "+" : "");
            color = "55AA55";
        }
        final String text = signum + value + '%';
        if (format) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.openText().addColor(color).append(text);
            return sb.finishAndToString();
        }
        return text;
    }
    
    public static String displayPercentageAndBaseValue(final int value, final int max, final int alternativeValue) {
        String signum = "";
        String color;
        if (max != -1 && value == max) {
            signum = ((value >= 0) ? "+" : "");
            color = "FFF82D";
        }
        else if (value < 0) {
            color = "C62700";
        }
        else {
            signum = "+";
            color = "55AA55";
        }
        final String text = WakfuTranslator.getInstance().getString("resist.for.charac.sheet", value, signum + alternativeValue);
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.openText().addColor(color).append(text);
        return sb.finishAndToString();
    }
    
    public static String formatCharacteristic(final int characValue) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        if (characValue < 0) {
            sb.openText().addColor("C62700");
        }
        else if (characValue > 0) {
            sb.openText().addColor("55AA55").append("+");
        }
        sb.append(characValue);
        return sb.finishAndToString();
    }
}
