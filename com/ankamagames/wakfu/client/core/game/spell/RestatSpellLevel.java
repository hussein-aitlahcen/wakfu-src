package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.restat.*;

public class RestatSpellLevel extends SpellLevel
{
    public static final String CAN_ADD_LEVEL_FIELD = "canAddLevel";
    public static final String CAN_REMOVE_LEVEL_FIELD = "canRemoveLevel";
    public static final String XP_TO_ADD_LEVEL_FIELD = "xpToAddLevel";
    public static final String XP_FROM_REMOVING_LEVEL_FIELD = "xpFromRemovingLevel";
    public static final String ACTIVATED_FIELD = "activated";
    static final String[] LOCAL_FIELDS;
    static final String[] LOCAL_ALL_FIELDS;
    
    @Override
    public String[] getFields() {
        return RestatSpellLevel.LOCAL_ALL_FIELDS;
    }
    
    public RestatSpellLevel(final Spell spell) {
        super(spell, (short)0, -1L);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final SpellRestatComputer spellRestatComputer = UISpellsRestatFrame.INSTANCE.getSpellRestatManagerComputer();
        final Elements element = Elements.getElementFromId(this.getSpell().getElementId());
        if (fieldName.equals("canAddLevel")) {
            return spellRestatComputer.canIncrementSpellLevel(element, this.getSpell().getId());
        }
        if (fieldName.equals("canRemoveLevel")) {
            return spellRestatComputer.canDecrementSpellLevel(element, this.getSpell().getId());
        }
        if (fieldName.equals("xpToAddLevel")) {
            final long xpNeededToIncrementSpellLevel = spellRestatComputer.getXpNeededToIncrementSpellLevel(element, this.getSpell().getId());
            if (xpNeededToIncrementSpellLevel < 0L) {
                return null;
            }
            final TextWidgetFormater twf = new TextWidgetFormater();
            if (spellRestatComputer.canIncrementSpellLevel(element, this.getSpell().getId())) {
                twf.addColor(Color.RED.getRGBAtoHex());
            }
            twf.append("- " + WakfuTranslator.getInstance().getString("xpGain", xpNeededToIncrementSpellLevel));
            return twf.finishAndToString();
        }
        else if (fieldName.equals("xpFromRemovingLevel")) {
            final long xpGainedByDecrementingSpellLevel = spellRestatComputer.getXpGainedByDecrementingSpellLevel(element, this.getSpell().getId());
            if (xpGainedByDecrementingSpellLevel < 0L) {
                return null;
            }
            final TextWidgetFormater twf = new TextWidgetFormater();
            if (spellRestatComputer.canDecrementSpellLevel(element, this.getSpell().getId())) {
                twf.addColor(Color.RED.getRGBAtoHex());
            }
            twf.append("+ " + WakfuTranslator.getInstance().getString("xpGain", xpGainedByDecrementingSpellLevel));
            return twf.finishAndToString();
        }
        else {
            if (fieldName.equals("activated")) {
                return spellRestatComputer.isSpellActivated(element, this.getSpell().getId());
            }
            return this.getRealSpellLevel().getFieldValue(fieldName);
        }
    }
    
    public SpellLevel getRealSpellLevel() {
        final SpellRestatComputer spellRestatComputer = UISpellsRestatFrame.INSTANCE.getSpellRestatManagerComputer();
        return spellRestatComputer.getSpellLevel(Elements.getElementFromId(this.getSpell().getElementId()), this.getSpell().getId());
    }
    
    @Override
    public int compareTo(final Object o) {
        final short uiPosition = this.getSpell().getUiPosition();
        if (!(o instanceof RestatSpellLevel)) {
            return 0;
        }
        return uiPosition - ((RestatSpellLevel)o).getSpell().getUiPosition();
    }
    
    static {
        LOCAL_FIELDS = new String[] { "canAddLevel", "canRemoveLevel", "xpToAddLevel", "xpFromRemovingLevel", "activated" };
        LOCAL_ALL_FIELDS = new String[RestatSpellLevel.LOCAL_FIELDS.length + SpellLevel.FIELDS.length];
        System.arraycopy(RestatSpellLevel.LOCAL_FIELDS, 0, RestatSpellLevel.LOCAL_ALL_FIELDS, 0, RestatSpellLevel.LOCAL_FIELDS.length);
        System.arraycopy(SpellLevel.FIELDS, 0, RestatSpellLevel.LOCAL_ALL_FIELDS, RestatSpellLevel.LOCAL_FIELDS.length, SpellLevel.FIELDS.length);
    }
}
