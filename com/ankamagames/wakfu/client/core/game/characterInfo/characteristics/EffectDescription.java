package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import org.apache.log4j.*;
import java.util.regex.*;
import com.ankamagames.baseImpl.common.clientAndServer.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.text.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;

public class EffectDescription
{
    private static final Logger m_logger;
    private static final Pattern PLUS_MINUS_CHAR_PATTERN;
    private final TShortIntHashMap characValues;
    private final TByteIntHashMap characToActionId;
    private final GameContentTranslator m_translator;
    
    public EffectDescription(final GameContentTranslator translator, final ArrayList<WakfuEffect> totalEffect, final short itemBaseLevel) {
        this(translator, totalEffect, itemBaseLevel, false);
    }
    
    public EffectDescription(final GameContentTranslator translator, final ArrayList<WakfuEffect> totalEffect, final short itemBaseLevel, final boolean forceUsableEffects) {
        super();
        this.characValues = new TShortIntHashMap();
        this.characToActionId = new TByteIntHashMap();
        this.mergeEffects(totalEffect, itemBaseLevel, forceUsableEffects);
        this.m_translator = translator;
    }
    
    public ArrayList<String> getEffectsDescription() {
        final TByteObjectHashMap<String> msgs = new TByteObjectHashMap<String>();
        final TShortIntIterator it = this.characValues.iterator();
        while (it.hasNext()) {
            it.advance();
            final FighterCharacteristicType type = FighterCharacteristicType.getCharacteristicTypeFromId(MathHelper.getFirstByteFromShort(it.key()));
            final int actionId = this.characToActionId.get(type.getId());
            String text = this.getTranslatedText(actionId, it.value());
            final Elements element = Elements.getElementFromId(MathHelper.getSecondByteFromShort(it.key()));
            if (element != null) {
                text = text + " " + CastableDescriptionGenerator.addElement(element);
            }
            msgs.put(type.getId(), text);
        }
        final ArrayList<String> descr = new ArrayList<String>(msgs.size());
        final byte[] keys = msgs.keys();
        Arrays.sort(keys);
        for (final byte key : keys) {
            descr.add(msgs.get(key));
        }
        return descr;
    }
    
    public TByteObjectHashMap<String> getEffectsDescription(final EffectUser character) {
        final TByteObjectHashMap<String> msgs = new TByteObjectHashMap<String>();
        final TShortIntIterator it = this.characValues.iterator();
        while (it.hasNext()) {
            it.advance();
            final FighterCharacteristicType type = FighterCharacteristicType.getCharacteristicTypeFromId(MathHelper.getFirstByteFromShort(it.key()));
            final Elements element = Elements.getElementFromId(MathHelper.getSecondByteFromShort(it.key()));
            final int actionId = this.characToActionId.get(type.getId());
            final int value = it.value();
            final String text = this.getTranslatedText(actionId, value);
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(text);
            if (element != null) {
                try {
                    final String elementUrl = String.format(WakfuConfiguration.getInstance().getString("elementsSmallIconsPath"), element.name());
                    sb.append(" ").append(TextUtils.getImageTag(elementUrl, -1, -1, null));
                }
                catch (PropertyException e) {
                    EffectDescription.m_logger.error((Object)"Probl\u00e8me de propri\u00e9t\u00e9", (Throwable)e);
                }
            }
            if (type == FighterCharacteristicType.HP || type == FighterCharacteristicType.AP || type == FighterCharacteristicType.MP || type == FighterCharacteristicType.WP) {
                sb.append(" (").append(character.getCharacteristic(type).max()).append(")");
            }
            else {
                sb.append(" (").append(character.getCharacteristicValue(type)).append(")");
            }
            msgs.put(type.getId(), sb.finishAndToString());
        }
        return msgs;
    }
    
    private String getTranslatedText(final int actionId, final int value) {
        String caracText = this.m_translator.getStringWithoutFormat(10, actionId);
        caracText = EffectDescription.PLUS_MINUS_CHAR_PATTERN.matcher(caracText).replaceAll("");
        final String valueText = (value < 0) ? String.valueOf(value) : ("+" + value);
        return StringFormatter.format(caracText, valueText);
    }
    
    private void mergeEffects(final ArrayList<WakfuEffect> totalEffect, final short itemBaseLevel, final boolean forceUsable) {
        Collections.sort(totalEffect, WakfuEffectTypeComparator.COMPARATOR);
        for (int j = 0, size = totalEffect.size(); j < size; ++j) {
            final WakfuEffect effect = totalEffect.get(j);
            if (forceUsable || !effect.isAnUsableEffect()) {
                final WakfuRunningEffect wre = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
                final Elements element = wre.getElement();
                CharacteristicType type;
                boolean isPositive;
                if (wre instanceof CharacGain) {
                    type = ((CharacModification)wre).getCharacteristicType();
                    isPositive = true;
                }
                else if (wre instanceof CharacLoss) {
                    type = ((CharacModification)wre).getCharacteristicType();
                    isPositive = false;
                }
                else if (wre instanceof CharacBuff) {
                    type = ((CharacBuff)wre).getCharacteristicType();
                    isPositive = true;
                }
                else {
                    if (!(wre instanceof CharacDebuff)) {
                        continue;
                    }
                    type = ((CharacDebuff)wre).getCharacteristicType();
                    isPositive = false;
                }
                if (type == null) {
                    assert false;
                    EffectDescription.m_logger.error((Object)("CharacteristicType inconnu pour l'effet" + effect.getEffectId() + " actionId=" + effect.getActionId() + "  " + wre.getClass()));
                }
                else {
                    final byte typeId = type.getId();
                    if (!this.characToActionId.contains(typeId)) {
                        this.characToActionId.put(typeId, effect.getActionId());
                    }
                    int value;
                    if (isPositive) {
                        value = effect.getParam(0, itemBaseLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    }
                    else {
                        value = -effect.getParam(0, itemBaseLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    }
                    final short key = MathHelper.getShortFromTwoBytes(typeId, (byte)((element != null) ? element.getId() : -1));
                    this.characValues.adjustOrPutValue(key, value, value);
                }
            }
        }
    }
    
    public String createBonusDescription(final EffectUser character) {
        return createBonusDescription(this.getEffectsDescription(character));
    }
    
    private static String createBonusDescription(final TByteObjectHashMap<String> msgs) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        boolean hasCharacBonus = false;
        String bonus = msgs.get(FighterCharacteristicType.HP.getId());
        if (bonus != null) {
            sb.b().addColor("fe201b").append(bonus)._b().newLine();
            hasCharacBonus = true;
        }
        bonus = msgs.get(FighterCharacteristicType.AP.getId());
        if (bonus != null) {
            sb.b().addColor("00afea").append(bonus)._b().newLine();
            hasCharacBonus = true;
        }
        bonus = msgs.get(FighterCharacteristicType.MP.getId());
        if (bonus != null) {
            sb.b().addColor("00b400").append(bonus)._b().newLine();
            hasCharacBonus = true;
        }
        bonus = msgs.get(FighterCharacteristicType.WP.getId());
        if (bonus != null) {
            sb.b().addColor("c04aff").append(bonus)._b().newLine();
            hasCharacBonus = true;
        }
        if (hasCharacBonus) {
            sb.newLine();
        }
        if (writeCharac(msgs, sb, new FighterCharacteristicType[] { FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.RES_IN_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_BACKSTAB })) {
            sb.newLine();
        }
        if (writeCharac(msgs, sb, new FighterCharacteristicType[] { FighterCharacteristicType.RANGE, FighterCharacteristicType.INIT, FighterCharacteristicType.FEROCITY, FighterCharacteristicType.FUMBLE_RATE, FighterCharacteristicType.TACKLE, FighterCharacteristicType.DODGE, FighterCharacteristicType.WISDOM, FighterCharacteristicType.PROSPECTION, FighterCharacteristicType.BLOCK, FighterCharacteristicType.EQUIPMENT_KNOWLEDGE })) {
            sb.newLine();
        }
        if (writeCharac(msgs, sb, new FighterCharacteristicType[] { FighterCharacteristicType.LEADERSHIP, FighterCharacteristicType.SUMMONING_MASTERY, FighterCharacteristicType.HEAL_IN_PERCENT, FighterCharacteristicType.BACKSTAB_BONUS })) {
            sb.newLine();
        }
        writeCharac(msgs, sb, new FighterCharacteristicType[] { FighterCharacteristicType.OCCUPATION_CRAFT_QUICKNESS, FighterCharacteristicType.OCCUPATION_HARVEST_QUICKNESS, FighterCharacteristicType.OCCUPATION_RESOURCEFULNESS, FighterCharacteristicType.OCCUPATION_GREEN_THUMBS });
        final String bonuses = sb.finishAndToString();
        int num = 0;
        for (int bonusesLength = bonuses.length(); num < bonusesLength && bonuses.charAt(bonusesLength - num - 1) == '\n'; ++num) {}
        return bonuses.substring(0, bonuses.length() - num);
    }
    
    private static boolean writeCharac(final TByteObjectHashMap<String> msgs, final TextWidgetFormater sb, final FighterCharacteristicType[] charac) {
        boolean hasCharac = false;
        for (final FighterCharacteristicType c : charac) {
            final String bonus = msgs.get(c.getId());
            if (bonus != null) {
                sb.append(bonus).newLine();
                hasCharac = true;
            }
        }
        return hasCharac;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectDescription.class);
        PLUS_MINUS_CHAR_PATTERN = Pattern.compile("[+-]");
    }
    
    public static class WakfuEffectTypeComparator implements Comparator<WakfuEffect>
    {
        public static final WakfuEffectTypeComparator COMPARATOR;
        
        @Override
        public int compare(final WakfuEffect o1, final WakfuEffect o2) {
            return o1.getActionId() - o2.getActionId();
        }
        
        static {
            COMPARATOR = new WakfuEffectTypeComparator();
        }
    }
}
