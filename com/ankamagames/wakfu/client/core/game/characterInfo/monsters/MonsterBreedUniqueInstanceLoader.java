package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.wakfu.common.game.wakfu.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.common.game.craft.collect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.characterInfo.action.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.graphics.image.*;

final class MonsterBreedUniqueInstanceLoader
{
    private static final Logger m_logger;
    
    public MonsterBreed loadFromBinaryForm(final MonsterBinaryData bs) {
        final short breedId = (short)bs.getId();
        final MonsterBreed breed = createBreed(bs, breedId);
        loadCollect(bs, breedId, breed);
        loadActions(bs, breed);
        loadBehaviors(bs);
        loadEvolutions(bs);
        return breed;
    }
    
    private static MonsterBreed createBreed(final MonsterBinaryData bs, final short breedId) {
        final int defeatScriptId = (bs.getDefeatScriptId() != 0) ? bs.getDefeatScriptId() : 30000;
        final EnumMap<FighterCharacteristicType, ObjectPair<Integer, Float>> fightCharacteristics = loadFightCharacteristics(bs);
        fightCharacteristics.put(FighterCharacteristicType.PERCEPTION, new ObjectPair<Integer, Float>(bs.getBasePerception(), bs.getPerceptionInc()));
        final MovementSpeed walkSpeed = MovementSpeed.getFromId(bs.getWalkingSpeed());
        final MovementSpeed runSpeed = MovementSpeed.getFromId(bs.getRunningSpeed());
        final MonsterBreed breed = new MonsterBreed(breedId, bs.getFamilyId(), bs.hasDeadEvolution(), bs.getLevelMin(), bs.getLevelMax(), fightCharacteristics, bs.getGfx(), bs.getTimelineGfx(), bs.getWorldProperties(), bs.getFightProperties(), bs.getGfxEquipment(), bs.getMonsterHeight(), bs.getNaturalStates(), bs.getAgroRadius(), bs.getSightRadius(), (byte)bs.getRadiusSize(), bs.getFamilyRank(), defeatScriptId, walkSpeed, runSpeed, bs.getRunningRadiusInWorld(), bs.getRunningRadiusInFight(), WakfuMonsterAlignment.getAlignmentById(bs.getAlignmentId()), bs.getArcadePointsMultiplicator());
        breed.setRequiredLeadershipPoints(bs.getRequiredLeadershipPoints());
        breed.setSpecialGfx(createSpecialGfx(bs));
        breed.setTimelineBuffId(bs.getTimelineBuffId());
        for (final MonsterBinaryData.SpellInfo spellInfo : bs.getSpellsIdAndLevel()) {
            breed.addSpell(spellInfo.getId(), spellInfo.getLevel());
        }
        return breed;
    }
    
    private static EnumMap<FighterCharacteristicType, ObjectPair<Integer, Float>> loadFightCharacteristics(final MonsterBinaryData bs) {
        final EnumMap<FighterCharacteristicType, ObjectPair<Integer, Float>> fightCharacteristics = new EnumMap<FighterCharacteristicType, ObjectPair<Integer, Float>>(FighterCharacteristicType.class);
        fightCharacteristics.put(FighterCharacteristicType.HP, new ObjectPair<Integer, Float>(bs.getBaseHp(), bs.getHpInc()));
        fightCharacteristics.put(FighterCharacteristicType.AP, new ObjectPair<Integer, Float>(bs.getBaseAp(), bs.getApInc()));
        fightCharacteristics.put(FighterCharacteristicType.MP, new ObjectPair<Integer, Float>(bs.getBaseMp(), bs.getMpInc()));
        fightCharacteristics.put(FighterCharacteristicType.WP, new ObjectPair<Integer, Float>(bs.getBaseWp(), bs.getWpInc()));
        fightCharacteristics.put(FighterCharacteristicType.RANGE, new ObjectPair<Integer, Float>(bs.getBaseRange(), bs.getRangeInc()));
        fightCharacteristics.put(FighterCharacteristicType.INIT, new ObjectPair<Integer, Float>(bs.getBaseInit(), bs.getInitInc()));
        fightCharacteristics.put(FighterCharacteristicType.FEROCITY, new ObjectPair<Integer, Float>(bs.getBaseCriticalHit(), bs.getCriticalHitInc()));
        fightCharacteristics.put(FighterCharacteristicType.FUMBLE_RATE, new ObjectPair<Integer, Float>(bs.getBaseCriticalMiss(), bs.getCriticalMissInc()));
        fightCharacteristics.put(FighterCharacteristicType.TACKLE, new ObjectPair<Integer, Float>(bs.getBaseTackleBonus(), bs.getTackleBonusInc()));
        fightCharacteristics.put(FighterCharacteristicType.DODGE, new ObjectPair<Integer, Float>(bs.getBaseTackleResistance(), bs.getTackleResistanceInc()));
        fightCharacteristics.put(FighterCharacteristicType.BLOCK, new ObjectPair<Integer, Float>(bs.getBaseParade(), bs.getParadeInc()));
        fightCharacteristics.put(FighterCharacteristicType.RES_FIRE_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseFireResistance(), bs.getFireResistanceInc()));
        fightCharacteristics.put(FighterCharacteristicType.RES_WATER_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseWaterResistance(), bs.getWaterResistanceInc()));
        fightCharacteristics.put(FighterCharacteristicType.RES_EARTH_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseEarthResistance(), bs.getEarthResistanceInc()));
        fightCharacteristics.put(FighterCharacteristicType.RES_AIR_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseWindResistance(), bs.getWindResistanceInc()));
        fightCharacteristics.put(FighterCharacteristicType.AP_DEBUFF_RES, new ObjectPair<Integer, Float>(bs.getBaseAPLossResistance(), bs.getApLossResistanceInc()));
        fightCharacteristics.put(FighterCharacteristicType.MP_DEBUFF_RES, new ObjectPair<Integer, Float>(bs.getBasePMLossResistance(), bs.getPmLossResistanceInc()));
        fightCharacteristics.put(FighterCharacteristicType.AP_DEBUFF_POWER, new ObjectPair<Integer, Float>(bs.getBaseAPDebuffPower(), bs.getApDebuffPowerInc()));
        fightCharacteristics.put(FighterCharacteristicType.MP_DEBUFF_POWER, new ObjectPair<Integer, Float>(bs.getBaseMPDebuffPower(), bs.getMpDebuffPowerInc()));
        fightCharacteristics.put(FighterCharacteristicType.DMG_EARTH_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseEarthDamageBonus(), bs.getEarthDamageBonusInc()));
        fightCharacteristics.put(FighterCharacteristicType.DMG_AIR_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseWindDamageBonus(), bs.getWindDamageBonusInc()));
        fightCharacteristics.put(FighterCharacteristicType.DMG_FIRE_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseFireDamageBonus(), bs.getFireDamageBonusInc()));
        fightCharacteristics.put(FighterCharacteristicType.DMG_WATER_PERCENT, new ObjectPair<Integer, Float>(bs.getBaseWaterDamageBonus(), bs.getWaterDamageBonusInc()));
        fightCharacteristics.put(FighterCharacteristicType.KO_TIME_BEFORE_DEATH, new ObjectPair<Integer, Float>(bs.getBaseTimeBeforeDeath(), 0.0f));
        return fightCharacteristics;
    }
    
    private static void loadCollect(final MonsterBinaryData bs, final short breedId, final MonsterBreed breed) {
        for (final MonsterBinaryData.MonsterCollectActionData action : bs.getMonsterCollectActionData()) {
            final int actionId = action.getId();
            final int skillMaxSimultaneousPlayer = 1;
            final int consumableId = 0;
            final int consumableGfxId = 0;
            SimpleCriterion criterion = null;
            try {
                criterion = CriteriaCompiler.compileBoolean(action.getCriteria());
            }
            catch (Exception e) {
                MonsterBreedUniqueInstanceLoader.m_logger.error((Object)("Erreur de compilation du crit\u00e8rre sur l'action de collect " + action.getSkillVisualFeedbackId() + " du monstre " + breed.getBreedId()));
            }
            final ClientConsumableInfo consumableInfo = new ClientConsumableInfo(0, 0);
            final CollectAction collectAction = new CollectAction(actionId, breedId, action.getSkillId(), action.getSkillLevelRequired(), 1, action.getDuration(), action.getCollectItemId(), action.getSkillVisualFeedbackId(), criterion, -1, (byte)0, consumableInfo, action.isDisplayInCraftDialog(), action.getLootList());
            breed.addCollectAction(collectAction);
        }
    }
    
    private static void loadActions(final MonsterBinaryData bs, final MonsterBreed breed) {
        for (final MonsterBinaryData.MonsterAction action : bs.getMonsterActionData()) {
            final int actionId = action.getId();
            final byte actionTypeId = action.getType();
            final MonsterActionConstants constants = MonsterActionConstants.getFromId(actionTypeId);
            if (constants == null) {
                MonsterBreedUniqueInstanceLoader.m_logger.error((Object)("Impossible de trouver l'action de monstre " + actionTypeId));
            }
            else {
                SimpleCriterion criterion = null;
                try {
                    criterion = CriteriaCompiler.compileBoolean(action.getCriteria());
                }
                catch (Exception e) {
                    MonsterBreedUniqueInstanceLoader.m_logger.error((Object)("Probl\u00e8me de compilation d'un crit\u00e8re sur l'action " + actionId + " du monstre " + breed));
                }
                final ActionVisual visual = ActionVisualManager.getInstance().get(action.getVisualId());
                final AbstractClientMonsterAction monsterAction = MonsterActionFactory.newAction(actionId, actionTypeId, criterion, action.isCriteriaOnNpc(), visual, action.getScriptId(), action.getDuration(), action.isShowProgressBar(), action.isMoveToMonsterBeforeInteractWithHim());
                breed.addAction(monsterAction);
            }
        }
    }
    
    private static void loadBehaviors(final MonsterBinaryData bs) {
        for (final MonsterBinaryData.MonsterBehaviourData behaviour : bs.getMonsterBehaviourData()) {
            final int behaviourId = behaviour.getId();
            final int type = behaviour.getType();
            final int scriptId = behaviour.getScriptId();
            final boolean needsToWaitPathEnd = behaviour.isNeedsToWaitPathEnd();
            MonsterBehaviourManager.getInstance().addBehaviour(bs.getId(), new MonsterBehaviour(behaviourId, type, scriptId, needsToWaitPathEnd));
        }
    }
    
    private static void loadEvolutions(final MonsterBinaryData bs) {
        for (final MonsterBinaryData.MonsterEvolutionData evolutionData : bs.getMonsterEvolutionData()) {
            final int evolutionId = evolutionData.getId();
            final int scriptId = evolutionData.getScriptId();
            final short evolvedBreedId = (short)evolutionData.getBreedId();
            final MonsterEvolutionStep evolution = new MonsterEvolutionStep(evolutionId, evolvedBreedId, scriptId);
            MonsterEvolutionStepManager.getInstance().addEvolutionStep(evolution);
        }
    }
    
    @Nullable
    public static MonsterSpecialGfx createSpecialGfx(final MonsterBinaryData bs) {
        final MonsterSpecialGfx.Anim[] anims = createAnimsFrom(bs.getSpecialGfxAnim());
        final MonsterSpecialGfx.Colors[] colors = createColorsFrom(bs.getSpecialGfxColor());
        final MonsterSpecialGfx.Equipment[] equipments = createEquipementsFrom(bs.getSpecialGfxEquipement());
        if (equipments != null || colors != null || anims != null) {
            final MonsterSpecialGfx special = new MonsterSpecialGfx();
            special.setAnims(anims);
            special.setColors(colors);
            special.setEquipements(equipments);
            return special;
        }
        return null;
    }
    
    @Nullable
    private static MonsterSpecialGfx.Anim[] createAnimsFrom(final MonsterBinaryData.Anim[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        final MonsterSpecialGfx.Anim[] result = new MonsterSpecialGfx.Anim[data.length];
        for (int i = 0; i < data.length; ++i) {
            result[i] = new MonsterSpecialGfx.Anim(data[i].getKey(), data[i].getAnimName());
        }
        return result;
    }
    
    @Nullable
    private static MonsterSpecialGfx.Colors[] createColorsFrom(final MonsterBinaryData.Color[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        final MonsterSpecialGfx.Colors[] result = new MonsterSpecialGfx.Colors[data.length];
        for (int i = 0; i < data.length; ++i) {
            result[i] = new MonsterSpecialGfx.Colors(data[i].getPartIndex(), new Color(data[i].getColor()));
        }
        return result;
    }
    
    @Nullable
    private static MonsterSpecialGfx.Equipment[] createEquipementsFrom(final MonsterBinaryData.Equipement[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        final MonsterSpecialGfx.Equipment[] result = new MonsterSpecialGfx.Equipment[data.length];
        for (int i = 0; i < data.length; ++i) {
            result[i] = new MonsterSpecialGfx.Equipment(data[i].getFileId(), data[i].getParts());
        }
        return result;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MonsterBreedUniqueInstanceLoader.class);
    }
}
