package com.ankamagames.wakfu.client.core.game.shortcut;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public final class SpellCastValidatorForShortcuts
{
    private static final Logger m_logger;
    private Fight m_linkedFight;
    
    public CastValidity getSpellCastValidity(final BasicCharacterInfo fighter, final AbstractSpellLevel spelllevel, final Point3 targetCell, final boolean checkUseCost) {
        if (this.m_linkedFight == null) {
            SpellCastValidatorForShortcuts.m_logger.error((Object)"On ne check pas les critere si on a pas de combat associ\u00e9 dans le validateur");
            return CastValidity.OK;
        }
        if (spelllevel == null) {
            return CastValidity.INVALID_CONTAINER;
        }
        final AbstractSpell spell = spelllevel.getSpell();
        if (SpellCastValidation.checkPassivity(spell)) {
            return CastValidity.PASSIVE;
        }
        if (!SpellCastValidation.checkCarryCast(fighter, spell)) {
            return CastValidity.CAST_CRITERIONS_NOT_VALID;
        }
        if (checkUseCost) {
            if (!SpellCastValidation.checkAp(fighter, spelllevel, spell)) {
                return CastValidity.NOT_ENOUGH_AP;
            }
            if (!SpellCastValidation.checkWp(fighter, spelllevel, spell)) {
                return CastValidity.NOT_ENOUGH_FP;
            }
            if (!SpellCastValidation.checkMp(fighter, spelllevel, spell)) {
                return CastValidity.NOT_ENOUGH_MP;
            }
            if (!SpellCastValidation.checkCharac(fighter, FighterCharacteristicType.CHRAGE, spelllevel, spell)) {
                return CastValidity.NOT_ENOUGH_CHRAGE;
            }
        }
        final CastValidity historyvalidity = fighter.getSpellLevelCastHistory().canCastSpell(spelllevel, this.m_linkedFight.getTimeline().getCurrentTableturn());
        if (!historyvalidity.isValid()) {
            return historyvalidity;
        }
        if (!this.m_linkedFight.getTimeline().hasCurrentFighter() || this.m_linkedFight.getTimeline().getCurrentFighterId() != fighter.getId()) {
            return CastValidity.CANT_CAST_BETWEEN_TURN;
        }
        final SimpleCriterion castCriterions = spell.getCastCriterions();
        if (castCriterions != null) {
            if (castCriterions.isValid(fighter, null, spelllevel, this.m_linkedFight)) {
                return CastValidity.OK;
            }
            if (!this.hasValidTarget(fighter, (SpellLevel)spelllevel, castCriterions)) {
                return CastValidity.CAST_CRITERIONS_NOT_VALID;
            }
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (fighter instanceof LocalPlayerCharacter && localPlayer != null && localPlayer.getLockedSpellId() == spell.getId()) {
            return CastValidity.LOCKED_SPELL;
        }
        return CastValidity.OK;
    }
    
    private boolean hasValidTarget(final BasicCharacterInfo fighter, final SpellLevel spellLevel, final SimpleCriterion castCriterions) {
        final Collection<CharacterInfo> fighters = this.m_linkedFight.getFighters();
        for (final CharacterInfo characterInfo : fighters) {
            if (castCriterions.isValid(fighter, characterInfo.getPosition(), spellLevel, this.m_linkedFight)) {
                return true;
            }
        }
        final Collection<BasicEffectArea> areas = this.m_linkedFight.getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (castCriterions.isValid(fighter, area, spellLevel, this.m_linkedFight)) {
                return true;
            }
        }
        final Iterator<EffectUser> additionalTargets = this.m_linkedFight.getAdditionalTargets();
        if (additionalTargets != null) {
            while (additionalTargets.hasNext()) {
                final EffectUser next = additionalTargets.next();
                if (castCriterions.isValid(fighter, next, spellLevel, this.m_linkedFight)) {
                    return true;
                }
            }
        }
        final Point3 position = fighter.getPosition();
        final Point3 checkedPosition = new Point3(position);
        int x;
        for (int maxRange = spellLevel.getMaxRange(), minRange = x = spellLevel.getMinRange(); x <= maxRange; ++x) {
            for (int y = minRange; y <= maxRange; ++y) {
                checkedPosition.add(x, y);
                if (castCriterions.isValid(fighter, checkedPosition, spellLevel, this.m_linkedFight)) {
                    return true;
                }
                checkedPosition.set(position);
                if (x != y) {
                    checkedPosition.add(-x, y);
                    if (castCriterions.isValid(fighter, checkedPosition, spellLevel, this.m_linkedFight)) {
                        return true;
                    }
                    checkedPosition.set(position);
                    checkedPosition.add(x, -y);
                    if (castCriterions.isValid(fighter, checkedPosition, spellLevel, this.m_linkedFight)) {
                        return true;
                    }
                    checkedPosition.set(position);
                    checkedPosition.add(-x, -y);
                    if (castCriterions.isValid(fighter, checkedPosition, spellLevel, this.m_linkedFight)) {
                        return true;
                    }
                    checkedPosition.set(position);
                }
            }
        }
        return false;
    }
    
    public void setLinkedFight(final Fight linkedFight) {
        this.m_linkedFight = linkedFight;
    }
    
    public void clear() {
        this.m_linkedFight = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellCastValidatorForShortcuts.class);
    }
}
