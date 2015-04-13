package com.ankamagames.wakfu.client.core.descriptionGenerator;

import com.ankamagames.wakfu.common.game.descriptionGenerator.delegate.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.core.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.console.command.debug.*;

public class ClientCastableDescriptionGenerator implements CastableDescriptionGeneratorUtilityDelegate
{
    private static final Logger m_logger;
    
    @Override
    public void initialize(@NotNull final GameContentTranslator translator, @NotNull final TextWidgetFormatterFactory twfFactory) {
        CastableDescriptionGenerator.m_translator = translator;
        CastableDescriptionGenerator.m_twfFactory = twfFactory;
        CastableDescriptionGenerator.m_utilityDelegate = this;
        try {
            CastableDescriptionGenerator.PLOT_URL = WakfuConfiguration.getInstance().getString("effectDescPlotIconUrl");
            CastableDescriptionGenerator.SUB_EFFECT_CARAC_PREFIX = CastableDescriptionGenerator.m_twfFactory.createNew().append("    ").addImage(CastableDescriptionGenerator.PLOT_URL, -1, -1, null).append(" ").finishAndToString();
        }
        catch (PropertyException e) {
            e.printStackTrace();
        }
        CastableDescriptionGenerator.CRITICAL = "critical";
        CastableDescriptionGenerator.REQUIREMENTS = "requirements";
        CastableDescriptionGenerator.HP_VAR_BEACON = "hp.var.beacon";
        CastableDescriptionGenerator.HP_VAR_BARREL = "hp.var.barrel";
        CastableDescriptionGenerator.LEVEL_SHORT = "levelShort.custom";
        CastableDescriptionGenerator.LEVEL_SHORT_CUMULABLE = "levelShort.customCumulable";
        CastableDescriptionGenerator.WISDOM = "WISDOMShort";
        CastableDescriptionGenerator.AGILITY = "AGILITYShort";
        CastableDescriptionGenerator.STRENGTH = "STRENGTHShort";
        CastableDescriptionGenerator.LUCK = "LUCKShort";
        CastableDescriptionGenerator.INTELLIGENCE = "INTELLIGENCEShort";
        CastableDescriptionGenerator.VITALITY = "vitalityShort";
        CastableDescriptionGenerator.MAX_SHORT = "max";
        CastableDescriptionGenerator.LEVEL = "level";
        CastableDescriptionGenerator.EFFECT_DESCRIPTION_TRANSLATION_TYPE = 10;
        CastableDescriptionGenerator.EFFECT_CUSTOM_DESCRIPTION_TRANSLATION_TYPE = 33;
        CastableDescriptionGenerator.AREA_NAME_TRANSLATION_TYPE = 6;
        CastableDescriptionGenerator.SPELL_FREE_DESCRIPTION_TRANSLATION_TYPE = 5;
    }
    
    @Override
    public TextWidgetFormater formatInvalidCriterion(final TextWidgetFormater sb, final String invalidCriterion) {
        return sb.openText().addColor("cc4444").append(invalidCriterion);
    }
    
    @Override
    public void formatGlyphName(final TextWidgetFormater sb, final AbstractEffectArea abstractEffectArea, final short glyphLevel) {
        sb.b().u().addColor("70FEC5").addId("glyph_" + MathHelper.getIntFromTwoShort((short)abstractEffectArea.getBaseId(), glyphLevel));
        sb.append(WakfuTranslator.getInstance().getString(6, (short)abstractEffectArea.getBaseId(), new Object[0]));
        sb._u()._b();
    }
    
    @Override
    public void formatStateName(final TextWidgetFormater sb, final State currentState, final short stateLevel) {
        sb.b().u().addColor("70FEC5").addId("state_" + MathHelper.getIntFromTwoShort(currentState.getStateBaseId(), stateLevel));
        sb.append(currentState.getName());
        sb._u()._b();
    }
    
    @Override
    public void formatSpellName(final TextWidgetFormater sb, final AbstractSpell spell) {
        sb.b().u().addColor("70FEC5").addId("spell_" + spell.getId());
        sb.append(WakfuTranslator.getInstance().getString(3, spell.getId(), new Object[0]));
        sb._u()._b();
    }
    
    @Override
    public void formatSpellPropertyName(final TextWidgetFormater sb, final int propertyId) {
        sb.append(WakfuTranslator.getInstance().getString(String.format("spellProperty.%d", propertyId)));
    }
    
    @Override
    public AbstractSpellLevel getSpell(final short spellId) {
        SpellLevel spellLevel = WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(spellId);
        if (spellLevel == null) {
            spellLevel = (SpellLevel)SpellManager.getInstance().getDefaultSpellLevel(spellId, (short)0);
        }
        else {
            spellLevel = spellLevel.getCopy(false, true);
        }
        if (spellLevel == null) {
            spellLevel = new SpellLevel(SpellManager.getInstance().getSpell(spellId), (short)0, 0L);
        }
        return spellLevel;
    }
    
    @Override
    public String getAchievementName(final int id) {
        return WakfuTranslator.getInstance().getString(62, id, new Object[0]);
    }
    
    @Override
    public String getItemTypeName(final int id) {
        return ItemTypeManager.getInstance().getItemType(id).getName();
    }
    
    @Override
    public String getItemName(final int id) {
        return WakfuTranslator.getInstance().getString(15, id, new Object[0]);
    }
    
    @Override
    public String getSkillName(final short id) {
        return ReferenceSkillManager.getInstance().getReferenceSkill(id).getName();
    }
    
    @Override
    public String getCraftName(final short id) {
        return WakfuTranslator.getInstance().getString(43, id, new Object[0]);
    }
    
    @Override
    public String getBonusName(final int id) {
        return WakfuTranslator.getInstance().getString(138, id, new Object[0]);
    }
    
    @Override
    public String getMonsterName(final short id) {
        final MonsterBreed breedFromId = MonsterBreedManager.getInstance().getBreedFromId(id);
        if (breedFromId == null) {
            return null;
        }
        return breedFromId.getName();
    }
    
    @Override
    public String getMonsterTypeName(final short id) {
        return WakfuTranslator.getInstance().getString(38, id, new Object[0]);
    }
    
    @Override
    public String getAvatarBreedName(final int id) {
        return WakfuTranslator.getInstance().getString("breed." + id);
    }
    
    @Override
    public String getNationName(final int nationId) {
        return WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
    }
    
    @Override
    public String getAlignmentName(final byte alignmentId) {
        return WakfuTranslator.getInstance().getString("nation.alignmentName" + NationAlignement.getFromId(alignmentId).name());
    }
    
    @Override
    public String getSubscriptionLevelName(final int level) {
        return WakfuTranslator.getInstance().getString("subscriptionLevelName" + level);
    }
    
    @Override
    public String getPvpRankName(final NationPvpRanks rank) {
        return WakfuTranslator.getInstance().getString("nation.pvpRank." + rank);
    }
    
    @Override
    public String getInstanceName(final int instanceId) {
        return WakfuTranslator.getInstance().getString(77, instanceId, new Object[0]);
    }
    
    @Override
    public int getNationId() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null) {
            final CitizenComportment citizenComportment = localPlayer.getCitizenComportment();
            if (citizenComportment != null) {
                return citizenComportment.getNationId();
            }
        }
        return -1;
    }
    
    @Override
    public TextWidgetFormater getCraftIcon(final TextWidgetFormater sb) {
        try {
            final String elementUrl = String.format(WakfuConfiguration.getInstance().getString("elementsSmallIconsPath"), "craft");
            sb.addImage(elementUrl, -1, -1, null);
        }
        catch (PropertyException e) {
            ClientCastableDescriptionGenerator.m_logger.warn((Object)e.getMessage());
        }
        return sb;
    }
    
    @Override
    public TextWidgetFormater getElementIcon(final TextWidgetFormater sb, final Elements element) {
        try {
            final String elementUrl = String.format(WakfuConfiguration.getInstance().getString("elementsSmallIconsPath"), element.name());
            sb.addImage(elementUrl, -1, -1, null);
        }
        catch (PropertyException e) {
            ClientCastableDescriptionGenerator.m_logger.warn((Object)e.getMessage());
        }
        return sb;
    }
    
    @Override
    public TextWidgetFormater getSpellTargetIcon(final TextWidgetFormater sb, final String name) {
        try {
            final String url = String.format(WakfuConfiguration.getInstance().getString("targetEffectIconsPath"), name);
            sb.addImage(url, -1, -1, null, name);
        }
        catch (PropertyException e) {
            ClientCastableDescriptionGenerator.m_logger.warn((Object)e.getMessage());
        }
        return sb;
    }
    
    @Override
    public String getAreaOfEffectIcon(final AreaOfEffect areaOfEffect) {
        try {
            final String url = String.format(WakfuConfiguration.getInstance().getString("areasIconsPath"), areaOfEffect.getShape().name());
            String popupTranslation = "areaRange";
            final int visualRange = areaOfEffect.getVisualRange();
            if (visualRange > 0) {
                popupTranslation = popupTranslation + "-" + visualRange;
            }
            return CastableDescriptionGenerator.m_twfFactory.createNew().addImage(url, -1, -1, null, popupTranslation).finishAndToString();
        }
        catch (PropertyException e) {
            ClientCastableDescriptionGenerator.m_logger.error((Object)e.toString());
            return null;
        }
    }
    
    @Override
    public String getChrageIcon() {
        try {
            return CastableDescriptionGenerator.m_twfFactory.createNew().addImage(WakfuTextImageProvider._getIconUrl((byte)10), -1, -1, null, "critere.chrage").append(" ").finishAndToString();
        }
        catch (PropertyException e) {
            e.getMessage();
            return null;
        }
    }
    
    @Override
    public String getCitizenPointsIcon() {
        try {
            return CastableDescriptionGenerator.m_twfFactory.createNew().addImage(WakfuTextImageProvider._getIconUrl((byte)9), -1, -1, null, "citizenScore").append(" ").finishAndToString();
        }
        catch (PropertyException e) {
            e.getMessage();
            return null;
        }
    }
    
    @Override
    public String getAreaOfEffectIcon(final String shapeName) {
        try {
            final String url = String.format(WakfuConfiguration.getInstance().getString("areasIconsPath"), shapeName);
            return CastableDescriptionGenerator.m_twfFactory.createNew().addImage(url, -1, -1, null).finishAndToString();
        }
        catch (PropertyException e) {
            ClientCastableDescriptionGenerator.m_logger.error((Object)e.toString());
            return null;
        }
    }
    
    @Override
    public String getInfiniteDurationString(final GameContentTranslator translator) {
        return CastableDescriptionGenerator.m_translator.getString("cast.infiniteDuration");
    }
    
    @Override
    public String getRemainingDurationString(final GameContentTranslator translator, final int duration) {
        return CastableDescriptionGenerator.m_translator.getString("remaining.duration.turn", duration);
    }
    
    @Override
    public boolean useAutomaticDescription(final AbstractSpellLevel spellLevel) {
        return ((SpellLevel)spellLevel).getSpell().isUseAutomaticDescription();
    }
    
    @Override
    public BasicCharacterInfo getCaster() {
        return WakfuGameEntity.getInstance().getLocalPlayer();
    }
    
    @Override
    public boolean forceDisplayState() {
        return DisplayStatesCommand.forceDisplayStates();
    }
    
    @Override
    public Object getCasterEffectContext() {
        return this.getCaster().getEffectContext();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientCastableDescriptionGenerator.class);
    }
}
