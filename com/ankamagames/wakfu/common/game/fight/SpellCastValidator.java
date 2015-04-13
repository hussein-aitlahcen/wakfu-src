package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.datas.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.spellCastValidation.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.game.xp.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.*;

final class SpellCastValidator extends CommonCastValidator<AbstractSpellLevel, BasicCharacterInfo>
{
    private static Logger m_logger;
    private final SpellCastThroughGateValidator m_castThroughGateValidator;
    private boolean m_gatesEnabled;
    
    SpellCastValidator(final AbstractFight linkedFight) {
        super(linkedFight);
        this.m_castThroughGateValidator = new SpellCastThroughGateValidator();
        this.m_gatesEnabled = true;
    }
    
    CastValidity getSpellCastValidity(final BasicCharacterInfo fighter, final AbstractSpellLevel spellLevel, final Point3 targetCell, final boolean checkUseCost) {
        if (spellLevel == null) {
            SpellCastValidator.m_logger.error((Object)this.m_linkedFight.withFightId("cast d'un spell null"));
            return CastValidity.INVALID_CONTAINER;
        }
        final AbstractSpell spell = spellLevel.getSpell();
        if (SpellCastValidation.checkPassivity(spell)) {
            return CastValidity.PASSIVE;
        }
        if (fighter.isActiveProperty(FightPropertyType.CANNOT_CAST_SPELL)) {
            return CastValidity.CANNOT_CAST_SPELL;
        }
        if (!SpellCastValidation.checkCarryCast(fighter, spell)) {
            return CastValidity.CAST_CRITERIONS_NOT_VALID;
        }
        if (checkUseCost) {
            if (!SpellCastValidation.checkAp(fighter, spellLevel, spell)) {
                return CastValidity.NOT_ENOUGH_AP;
            }
            if (!SpellCastValidation.checkWp(fighter, spellLevel, spell)) {
                return CastValidity.NOT_ENOUGH_FP;
            }
            if (!SpellCastValidation.checkMp(fighter, spellLevel, spell)) {
                return CastValidity.NOT_ENOUGH_MP;
            }
            if (!SpellCastValidation.checkCharac(fighter, FighterCharacteristicType.CHRAGE, spellLevel, spell)) {
                return CastValidity.NOT_ENOUGH_CHRAGE;
            }
        }
        final CastValidity historyvalidity = fighter.getSpellLevelCastHistory().canCastSpell(spellLevel, this.m_linkedFight.getTimeline().getCurrentTableturn());
        if (!historyvalidity.isValid()) {
            return historyvalidity;
        }
        final int rangeMin = spell.getRangeMin(spellLevel, fighter, targetCell, this.m_linkedFight.getContext());
        final int rangeMax = spell.getRangeMax(spellLevel, fighter, targetCell, this.m_linkedFight.getContext());
        final boolean spellCastRangeDynamic = spell.isSpellCastRangeDynamic(spellLevel, fighter, targetCell, this.m_linkedFight.getContext());
        final int boostedRangeMax = ((CommonCastValidator<T, BasicCharacterInfo>)this).computeRangeMax(fighter, spellCastRangeDynamic, rangeMin, rangeMax);
        this.m_castThroughGateValidator.setParams(this.m_linkedFight, fighter, spellLevel, rangeMin, boostedRangeMax, targetCell);
        if (this.m_gatesEnabled) {
            this.m_castThroughGateValidator.computeGatesIfNecessary();
        }
        CastValidity castValidity = CastValidity.CANNOT_EVALUATE;
        try {
            if (targetCell != null) {
                final BasicCharacterInfo target = this.m_linkedFight.getCharacterInfoAtPosition(targetCell.getX(), targetCell.getY());
                if (!this.checkCellAlignement(fighter, targetCell, spellLevel, target, this.m_linkedFight, rangeMin, boostedRangeMax)) {
                    return CastValidity.CELLS_NOT_ALIGNED;
                }
                final List<EffectUser> targetsOnCell = (List<EffectUser>)this.m_linkedFight.getPossibleTargetsAtPosition(targetCell);
                for (final EffectUser targetOnCell : targetsOnCell) {
                    final CastValidity validity = fighter.getSpellLevelCastHistory().canCastSpell(spellLevel, this.m_linkedFight.getTimeline().getCurrentTableturn(), targetOnCell);
                    if (!validity.isValid()) {
                        return validity;
                    }
                }
            }
            if (fighter instanceof SpellXpLocker) {
                final int lockedSpellId = ((SpellXpLocker)fighter).getLockedSpellId();
                if (lockedSpellId == spell.getId()) {
                    SpellCastValidator.m_logger.error((Object)("Trying to cast a locked spell. Spell :" + spell.getId() + " caster : " + fighter));
                    return CastValidity.CAST_CRITERIONS_NOT_VALID;
                }
            }
            castValidity = this.getCastValidity(fighter, spellLevel, targetCell, spellCastRangeDynamic, rangeMin, rangeMax, spell.isTestLineOfSight(spellLevel, fighter, targetCell, this.m_linkedFight.getContext()), spell.hasToTestFreeCell(), spell.hasToTestNotBorderCell(), spell.isCanCastOnCasterCell(), spell.getCastCriterions());
        }
        catch (Exception e) {
            SpellCastValidator.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            this.m_castThroughGateValidator.clear();
        }
        return castValidity;
    }
    
    @Override
    public boolean isValidRange(final BasicCharacterInfo fighter, final AbstractSpellLevel container, final int rangeMin, final int rangeMax, final boolean canCastOnCasterCell, final int targetX, final int targetY) {
        if (container.getSpell().hasProperty(SpellPropertyType.TEAM_GATES_TARGETABLE) && this.hasTeamGateOnPos(fighter, targetX, targetY)) {
            return true;
        }
        if (!container.getSpell().hasProperty(SpellPropertyType.USE_GATE_SPELL)) {
            return super.isValidRange(fighter, container, rangeMin, rangeMax, canCastOnCasterCell, targetX, targetY);
        }
        if (!this.m_castThroughGateValidator.hasInputGate()) {
            return super.isValidRange(fighter, container, rangeMin, rangeMax, canCastOnCasterCell, targetX, targetY);
        }
        final boolean rangeValidForGate = this.m_castThroughGateValidator.isValidRange();
        if (!rangeValidForGate) {
            return super.isValidRange(fighter, container, rangeMin, rangeMax, canCastOnCasterCell, targetX, targetY);
        }
        return rangeValidForGate;
    }
    
    private boolean hasTeamGateOnPos(final BasicCharacterInfo fighter, final int targetX, final int targetY) {
        final List<BasicEffectArea> areasOnPosition = this.m_linkedFight.getEffectAreaManager().getEffectAreasListOnPosition(new Point3(targetX, targetY));
        boolean hasTeamGateOnPos = false;
        for (final BasicEffectArea area : areasOnPosition) {
            if (area.getType() != EffectAreaType.GATE.getTypeId()) {
                continue;
            }
            if (area.getTeamId() != fighter.getTeamId()) {
                continue;
            }
            hasTeamGateOnPos = true;
        }
        return hasTeamGateOnPos;
    }
    
    @Override
    protected boolean isValidLos(final BasicCharacterInfo fighter, final AbstractSpellLevel container, final int targetX, final int targetY, final short targetZ, final int rangeMin, final int rangeMax, final boolean canCastOnCasterCell) {
        if (container.getSpell().hasProperty(SpellPropertyType.TEAM_GATES_TARGETABLE) && this.hasTeamGateOnPos(fighter, targetX, targetY)) {
            return true;
        }
        if (!container.getSpell().hasProperty(SpellPropertyType.USE_GATE_SPELL)) {
            return super.isValidLos(fighter, container, targetX, targetY, targetZ, rangeMin, rangeMax, canCastOnCasterCell);
        }
        if (!this.m_castThroughGateValidator.hasInputGate()) {
            return super.isValidLos(fighter, container, targetX, targetY, targetZ, rangeMin, rangeMax, canCastOnCasterCell);
        }
        final boolean validLos = this.m_castThroughGateValidator.isValidLos();
        if (!validLos) {
            return super.isValidRange(fighter, container, rangeMin, rangeMax, canCastOnCasterCell, targetX, targetY) && CommonCastValidator.checkCellAlignement(fighter, new Point3(targetX, targetY), container, null, this.m_linkedFight) && super.isValidLos(fighter, container, targetX, targetY, targetZ, rangeMin, rangeMax, canCastOnCasterCell);
        }
        return validLos;
    }
    
    boolean checkCellAlignement(final BasicCharacterInfo fighter, final Point3 targetCell, final AbstractSpellLevel container, final BasicCharacterInfo target, final AbstractFight fight, final int rangeMin, final int rangeMax) {
        final AbstractSpell spell = container.getSpell();
        final boolean castOnlyInLine = spell.isCastOnlyInLine(container, fighter, targetCell, fight.getContext());
        final boolean castOnlyInDiag = spell.isCastOnlyInDiag();
        if (!castOnlyInLine && !castOnlyInDiag) {
            return true;
        }
        final boolean isAlignedWithFighter = CommonCastValidator.checkCellAlignement(fighter, targetCell, container, target, fight);
        if (!spell.hasProperty(SpellPropertyType.USE_GATE_SPELL)) {
            return isAlignedWithFighter;
        }
        if (!this.m_castThroughGateValidator.hasInputGate()) {
            return isAlignedWithFighter;
        }
        final boolean alignmentValid = this.m_castThroughGateValidator.hasGateInRangeWithValidAlignment();
        if (!alignmentValid) {
            final boolean canCastOnCasterCell = spell.isCanCastOnCasterCell();
            final boolean isInFighterRange = super.isValidRange(fighter, container, rangeMin, rangeMax, canCastOnCasterCell, targetCell.getX(), targetCell.getY());
            return isInFighterRange && isAlignedWithFighter;
        }
        return alignmentValid;
    }
    
    public void disableGates() {
        this.m_gatesEnabled = false;
    }
    
    public void enableGates() {
        this.m_gatesEnabled = true;
    }
    
    static {
        SpellCastValidator.m_logger = Logger.getLogger((Class)SpellCastValidator.class);
    }
}
