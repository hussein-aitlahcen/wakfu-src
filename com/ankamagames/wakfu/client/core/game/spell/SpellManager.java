package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.binaryStorage.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.effect.*;
import org.apache.log4j.*;

public class SpellManager extends AbstractSpellManager<Spell>
{
    private final BinaryLoader<SpellBinaryData> m_creator;
    
    @Nullable
    @Override
    public Spell getSpell(final int spellId) {
        if (spellId == 0) {
            return null;
        }
        Spell spell = (Spell)this.m_spells.get(spellId);
        if (spell == null && !this.m_spells.contains(spellId)) {
            final SpellBinaryData data = this.m_creator.createFromId(spellId);
            if (data == null) {
                return null;
            }
            spell = this.createSpellFromBinaryForm(data);
            if (spell != null) {
                this.addSpell(spell);
            }
        }
        return spell;
    }
    
    public static SpellManager getInstance() {
        if (SpellManager.m_instance == null) {
            SpellManager.m_instance = new SpellManager();
        }
        return (SpellManager)SpellManager.m_instance;
    }
    
    private SpellManager() {
        super();
        this.m_creator = new BinaryLoaderFromFile<SpellBinaryData>(new SpellBinaryData());
    }
    
    @Override
    public AbstractSpellLevel getDefaultSpellLevel(final int spellId, final short level) {
        final long uid = AbstractSpellLevel.getDefaultUId(spellId, level);
        AbstractSpellLevel sl = this.m_defaultSpellLevels.get(uid);
        if (sl == null) {
            sl = new SpellLevel(this.getSpell(spellId), level, uid);
            this.m_defaultSpellLevels.put(uid, sl);
        }
        return sl;
    }
    
    public void addSpellFromBinaryForm(final SpellBinaryData bs) {
        assert bs != null;
        final Spell spell = this.createSpellFromBinaryForm(bs);
        this.addSpell(spell);
    }
    
    private Spell createSpellFromBinaryForm(final SpellBinaryData bs) {
        assert bs != null;
        final Spell spell = new Spell(buildSpellParameters(bs));
        final short spellBreedId = bs.getBreedId();
        if (spellBreedId == AvatarBreed.COMMON.getBreedId()) {
            for (final AvatarBreedInfo avatarBreedInfo : AvatarBreedInfoManager.getInstance().getBreedInfos()) {
                avatarBreedInfo.addSpell(spell, null);
            }
        }
        else if (spellBreedId != 0 && spellBreedId != AvatarBreed.NONE.getBreedId()) {
            final AvatarBreedInfo avatarBreedInfo2 = AvatarBreedInfoManager.getInstance().getBreedInfo(spellBreedId);
            if (avatarBreedInfo2 != null) {
                avatarBreedInfo2.addSpell(spell, null);
            }
        }
        addEffects(spell, bs.getEffectIds(), EffectManager.getInstance());
        return spell;
    }
    
    public static SpellParameters buildSpellParameters(final SpellBinaryData bs) {
        final SpellParameters spellParameters = new SpellParameters();
        spellParameters.setId(bs.getId());
        spellParameters.setScriptId(bs.getScriptId());
        spellParameters.setGfxId(bs.getGfxId());
        spellParameters.setMaxLevel(bs.getMaxLevel());
        spellParameters.setBreedId(bs.getBreedId());
        spellParameters.setCastMaxPerTarget((byte)bs.getCastMaxPerTarget());
        spellParameters.setCastMaxPerTurn(bs.getCastMaxPerTurn());
        spellParameters.setCastMinInterval((byte)bs.getCastMinInterval());
        spellParameters.setTestLineOfSight(bs.isTestLineOfSight());
        spellParameters.setCastOnlyInLine(bs.isCastOnlyLine());
        spellParameters.setCastOnlyInDiag(bs.isCastOnlyInDiag());
        spellParameters.setTestFreeCell(bs.isTestFreeCell());
        spellParameters.setTestNotBorderCell(bs.isTestNotBorderCell());
        spellParameters.setTestDirectPath(bs.isTestDirectPath());
        spellParameters.setTargetFilter(bs.getTargetFilter());
        SimpleCriterion castCrit = null;
        try {
            final String castCriterion = bs.getCastCriterion();
            castCrit = CriteriaCompiler.compileBoolean(castCriterion);
        }
        catch (Exception e) {
            SpellManager.m_logger.error((Object)("Erreur lors de la compilation des crit\u00e8res du sort " + bs.getId()), (Throwable)e);
        }
        spellParameters.setCastCriterions(castCrit);
        addBaseCharacCost(bs, spellParameters);
        spellParameters.setRangeMinBase(bs.getRangeMinBase());
        spellParameters.setRangeMinLevelIncrement(bs.getRangeMinLevelIncrement());
        spellParameters.setRangeMaxBase(bs.getRangeMaxBase());
        spellParameters.setRangeMaxLevelIncrement(bs.getRangeMaxInc());
        spellParameters.setMaxEffectCap((byte)bs.getMaxEffectCap());
        spellParameters.setElementId((byte)bs.getElement());
        spellParameters.setXpGainPercentage(bs.getXpGainPercentage());
        spellParameters.setSpellType((byte)bs.getSpellType());
        spellParameters.setUiPosition(bs.getUiPosition());
        SimpleCriterion learningCriteria = null;
        try {
            learningCriteria = CriteriaCompiler.compileBoolean(bs.getLearnCriteria());
        }
        catch (Exception e2) {
            SpellManager.m_logger.error((Object)("Erreur lors de la compilation des crit\u00e8res d'apprentissage du sort " + bs.getId()), (Throwable)e2);
        }
        spellParameters.setLearningCriterions(learningCriteria);
        spellParameters.setPassive(bs.getPassive());
        spellParameters.setUseAutomaticDescription(bs.isUseAutomaticDescription());
        spellParameters.setShowInTimeline(bs.isShowInTimeline());
        spellParameters.setCanCastWhenCarrying(bs.isCanCastWhenCarrying());
        spellParameters.setSpellCastRangeDynamic(bs.isSpellCastRangeIsDynamic());
        spellParameters.setCastSpellWillBreakInvisibility(bs.isCastSpellWillBreakInvisibility());
        spellParameters.setCastOnRandomCell(bs.isCastOnRandomCell());
        spellParameters.setTunnelable(bs.isTunnelable());
        spellParameters.setCanCastOnCasterCell(bs.isCanCastOnCasterCell());
        spellParameters.setCastMaxPerTurnIncr(bs.getCastMaxPerTurnIncr());
        final THashMap<String, SpellBinaryData.CastParam> alternativeCasts = bs.getAlternativeCasts();
        if (alternativeCasts != null && alternativeCasts.size() != 0) {
            alternativeCasts.forEachEntry(new TObjectObjectProcedure<String, SpellBinaryData.CastParam>() {
                @Override
                public boolean execute(final String criterion, final SpellBinaryData.CastParam param) {
                    try {
                        final SimpleCriterion alternativeCastCriterion = CriteriaCompiler.compileBoolean(criterion);
                        spellParameters.addAlternativeCastParameters(alternativeCastCriterion, convert(param));
                    }
                    catch (Exception e) {
                        SpellManager.m_logger.error((Object)("Erreur lors de la compilation des crit\u00e8res d'apprentissage du sort " + bs.getId()), (Throwable)e);
                    }
                    return true;
                }
            });
        }
        spellParameters.setProperties(bs.getProperties());
        return spellParameters;
    }
    
    private static SpellCastParameters convert(final SpellBinaryData.CastParam p) {
        final SpellCastParameters result = new SpellCastParameters(p.getPA_base(), p.getPA_inc(), p.getPW_base(), p.getPW_inc(), p.getPM_base(), p.getPM_inc());
        result.setLosAware(p.isLosAware());
        result.setOnlyInLine(p.isOnlyInLine());
        result.setRangeDynamic(p.isRangeIsDynamic());
        result.setRangeMax(p.getRangeMaxBase(), p.getRangeMaxInc());
        result.setRangeMin(p.getRangeMinBase(), p.getRangeMinInc());
        final TByteObjectHashMap<SpellBinaryData.CustomCharac> costs = p.getCosts();
        if (costs != null && costs.size() != 0) {
            costs.forEachEntry(new TByteObjectProcedure<SpellBinaryData.CustomCharac>() {
                @Override
                public boolean execute(final byte type, final SpellBinaryData.CustomCharac charac) {
                    result.addCost(FighterCharacteristicType.getCharacteristicTypeFromId(type), charac.getBase(), charac.getIncrement());
                    return true;
                }
            });
        }
        return result;
    }
    
    private static void addBaseCharacCost(final SpellBinaryData bs, final SpellParameters spellParameters) {
        final TByteObjectHashMap<SpellBinaryData.CustomCharac> params = bs.getBaseCastParameters();
        spellParameters.addBaseCharacCost(FighterCharacteristicType.AP, bs.getPA_base(), bs.getPA_inc());
        spellParameters.addBaseCharacCost(FighterCharacteristicType.MP, bs.getPM_base(), bs.getPM_inc());
        spellParameters.addBaseCharacCost(FighterCharacteristicType.WP, bs.getPW_base(), bs.getPW_inc());
        if (params != null && params.size() == 0) {
            params.forEachEntry(new TByteObjectProcedure<SpellBinaryData.CustomCharac>() {
                @Override
                public boolean execute(final byte type, final SpellBinaryData.CustomCharac charac) {
                    spellParameters.addBaseCharacCost(FighterCharacteristicType.getCharacteristicTypeFromId(type), charac.getBase(), charac.getIncrement());
                    return true;
                }
            });
        }
    }
    
    public static void addEffects(final AbstractSpell spell, final int[] effectIds, final EffectManager effectManager) {
        for (final int effectId : effectIds) {
            final WakfuEffect effect = effectManager.loadAndAddEffect(effectId);
            if (effect != null) {
                spell.addEffect(effect);
            }
            else {
                SpellManager.m_logger.error((Object)("Probl\u00e8me de chargmeent de Spell " + spell.getId()));
            }
        }
    }
}
