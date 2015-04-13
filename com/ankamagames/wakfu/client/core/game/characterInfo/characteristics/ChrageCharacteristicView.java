package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.xulor2.util.text.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class ChrageCharacteristicView extends CharacteristicView
{
    public static final int CHRAGE_SPELL_ID = 2107;
    
    public ChrageCharacteristicView(final FighterCharacteristic charac, final CharacteristicViewProvider provider) {
        super(charac, provider, (byte)10);
    }
    
    @Override
    public String getValueDescription() {
        float chrageToDmgRatio = 0.0f;
        final SpellLevel chrageSpell = WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(2107);
        if (chrageSpell == null) {
            return "";
        }
        for (final WakfuEffect effect : chrageSpell) {
            if (effect.getActionId() == RunningEffectConstants.SET_CHRAGE_TO_DMG_RATIO.getId()) {
                chrageToDmgRatio = effect.getParam(0, chrageSpell.getLevel());
                break;
            }
        }
        final float chrageDmgIncrease = this.m_charac.value() * chrageToDmgRatio;
        final String ratio = TextUtils.toBold(this.convert(chrageToDmgRatio), new String[0]);
        final String value = TextUtils.toBold(this.convert(chrageDmgIncrease), new String[0]);
        return WakfuTranslator.getInstance().getString("sacrierChrageDescription", ratio, value);
    }
    
    private String convert(final float value) {
        if (value == 0.0f) {
            return "0";
        }
        final String format = String.format("%.2f", value);
        if (format.indexOf(46) != -1) {
            int lastIndexOfZero = format.length();
            for (int i = format.length() - 1; i >= 0 && (format.charAt(i) == '0' || format.charAt(i) == '.'); --i) {
                lastIndexOfZero = i;
                if (format.charAt(i) == '.') {
                    break;
                }
            }
            return format.substring(0, lastIndexOfZero);
        }
        return format;
    }
}
