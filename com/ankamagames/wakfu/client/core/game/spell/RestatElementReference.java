package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.restat.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.characteristics.*;

public class RestatElementReference extends ElementReference
{
    public static final String DMG_PERCENT_FIELD = "dmgPercent";
    public static final String RES_PERCENT_FIELD = "resPercent";
    public static final String XP_VALUE_FIELD = "xpValue";
    public static final String XP_TEXT_FIELD = "xpText";
    static final String[] LOCAL_FIELDS;
    static final String[] LOCAL_ALL_FIELDS;
    private final FighterCharacteristicManager m_fighterCharacteristicManager;
    
    @Override
    public String[] getFields() {
        return RestatElementReference.LOCAL_ALL_FIELDS;
    }
    
    public RestatElementReference(final Elements element) {
        super(element);
        this.m_fighterCharacteristicManager = new FighterCharacteristicManager();
        final CharacBoostAnotherCharacProcedure masteryToDmgProcedure = new CharacBoostAnotherCharacProcedure(this.m_fighterCharacteristicManager, this.m_element.getDamageBonusCharacteristic(), 1.0f, 0);
        final FighterCharacteristicType masteryCharacteristic = this.m_element.getMasteryCharacteristic();
        if (masteryCharacteristic != null) {
            final FighterCharacteristic characteristic = this.m_fighterCharacteristicManager.getCharacteristic((CharacteristicType)masteryCharacteristic);
            characteristic.addProcedure(masteryToDmgProcedure);
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("dmgPercent")) {
            final int value = (int)(this.getMastery() * 1.0f);
            return this.getPercentage(this.m_element.getDamageBonusCharacteristic(), value);
        }
        if (fieldName.equals("resPercent")) {
            if (this.m_element.getMasteryCharacteristic() == FighterCharacteristicType.STASIS_MASTERY) {
                return this.getPercentage(this.m_element.getResistanceBonusCharacteristic(), 0);
            }
            return this.getPercentage(this.m_element.getResistanceBonusCharacteristic(), 0);
        }
        else if (fieldName.equals("xpValue")) {
            final SpellRestatComputer spellRestatComputer = UISpellsRestatFrame.INSTANCE.getSpellRestatManagerComputer();
            final long totalXpToDistribute = spellRestatComputer.getTotalXpToDistribute(this.m_element);
            if (totalXpToDistribute == 0L) {
                return 0.0f;
            }
            return spellRestatComputer.getRemainingXpToDistribute(this.m_element) / totalXpToDistribute;
        }
        else {
            if (fieldName.equals("xpText")) {
                final SpellRestatComputer spellRestatManagerDisplayer = UISpellsRestatFrame.INSTANCE.getSpellRestatManagerComputer();
                return WakfuTranslator.getInstance().getString("xp.remaining", spellRestatManagerDisplayer.getRemainingXpToDistribute(this.m_element));
            }
            return super.getFieldValue(fieldName);
        }
    }
    
    public int getMastery() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Iterable<SpellLevel> spellLevelsForElement = UISpellsRestatFrame.INSTANCE.getSpellRestatManager().getSpellLevelsForElement(this.m_element);
        final TShortArrayList levels = new TShortArrayList();
        for (final SpellLevel spellLevel : spellLevelsForElement) {
            if (spellLevel.getLevel() > 0) {
                levels.add(spellLevel.getLevel());
            }
        }
        return localPlayer.getBreed().getMasteryCharacsCalculator().calculateMastery(levels, this.m_element.getMasteryCharacteristic());
    }
    
    public String getPercentage(final FighterCharacteristicType fighterCharacteristicType, final int value) {
        final int max = this.m_fighterCharacteristicManager.getCharacteristicMaxValue(fighterCharacteristicType);
        return CharacteristicsUtil.displayPercentage(value, max, true);
    }
    
    static {
        LOCAL_FIELDS = new String[] { "dmgPercent", "resPercent", "xpValue", "xpText" };
        LOCAL_ALL_FIELDS = new String[RestatElementReference.LOCAL_FIELDS.length + ElementReference.FIELDS.length];
        System.arraycopy(RestatElementReference.LOCAL_FIELDS, 0, RestatElementReference.LOCAL_ALL_FIELDS, 0, RestatElementReference.LOCAL_FIELDS.length);
        System.arraycopy(ElementReference.FIELDS, 0, RestatElementReference.LOCAL_ALL_FIELDS, RestatElementReference.LOCAL_FIELDS.length, ElementReference.FIELDS.length);
    }
}
