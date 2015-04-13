package com.ankamagames.wakfu.common.game.xp;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.xp.character.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class SpellXpComputer
{
    public static final Logger m_logger;
    private static final double[] CHARACTER_XP_TO_SPELL_XP_RATIO_BY_CHARACTER_LEVEL;
    
    private static double[] initializeCharacterXpToSpellXpRatio() {
        final XpTable characterXpTable = CharacterXpTable.getInstance();
        final XpTable spellXpTable = SpellXpTable.getInstance();
        final double[] data = new double[201];
        for (short level = 1; level < 200; ++level) {
            final double xpForSpellAtThisLevel = spellXpTable.getLevelExtent(level);
            final double totalXpToWinAtThisLevel = 6.0 * xpForSpellAtThisLevel;
            final long characterXpInLevel = characterXpTable.getLevelExtent(level);
            data[level] = totalXpToWinAtThisLevel / characterXpInLevel;
        }
        data[200] = (data[0] = 0.0);
        return data;
    }
    
    public static long getTreeVirtualXp(final BasicCharacterInfo player, final long playerXp, final Elements element) {
        final double theoricalTotal = getMaxSpellXpFromPlayerXp(playerXp);
        final long limitForNoRegressionInOneElement = Math.round(theoricalTotal / 3.0);
        return realXpToVirtualXpDiff(0L, getTreeXp(player, element), limitForNoRegressionInOneElement);
    }
    
    public static long getTreeXp(final BasicCharacterInfo player, final Elements element) {
        if (player == null || element == null) {
            return 0L;
        }
        final SpellInventory<? extends AbstractSpellLevel> spellInventory = player.getSpellInventory();
        if (spellInventory == null) {
            return 0L;
        }
        long totalXp = 0L;
        for (final AbstractSpellLevel spellLevel : spellInventory) {
            if (spellLevel.getElement() != element) {
                continue;
            }
            totalXp += spellLevel.getXp();
        }
        return totalXp;
    }
    
    public static long getMaxSpellXpFromPlayerLevel(final short playerLevel) {
        final long playerXp = CharacterXpTable.getInstance().getXpByLevel(playerLevel);
        return getMaxSpellXpFromPlayerXp(playerLevel, playerXp);
    }
    
    public static long getMaxSpellXpFromPlayerXp(final long playerXp) {
        final short playerLevel = CharacterXpTable.getInstance().getLevelByXp(playerXp);
        return getMaxSpellXpFromPlayerXp(playerLevel, playerXp);
    }
    
    public static double getCharacterToSpellXpRatio(final short playerLevel) {
        if (playerLevel <= 0) {
            return 0.0;
        }
        if (playerLevel >= 200) {
            return 0.0;
        }
        return SpellXpComputer.CHARACTER_XP_TO_SPELL_XP_RATIO_BY_CHARACTER_LEVEL[playerLevel];
    }
    
    private static long getMaxSpellXpFromPlayerXp(final short playerLevel, final long playerXp) {
        final XpTable characterXpTable = CharacterXpTable.getInstance();
        final XpTable spellXpTable = SpellXpTable.getInstance();
        final double spellXpUpToBaseLevel = 6.0 * spellXpTable.getXpByLevel(playerLevel);
        final long playerXpFromBaseLevel = characterXpTable.getXpInLevel(playerXp);
        final double spellXpForUnfinishedLevel = playerXpFromBaseLevel * SpellXpComputer.CHARACTER_XP_TO_SPELL_XP_RATIO_BY_CHARACTER_LEVEL[playerLevel];
        return Math.round(spellXpForUnfinishedLevel + spellXpUpToBaseLevel);
    }
    
    public static long realXpToVirtualXpDiff(final long virtualXpBase, final long realXpModification, final long limitForXpPenalization) {
        if (realXpModification == 0L) {
            return 0L;
        }
        if (realXpModification > 0L) {
            if (virtualXpBase > limitForXpPenalization) {
                return realXpModification * 2L;
            }
            if (virtualXpBase + realXpModification <= limitForXpPenalization) {
                return realXpModification;
            }
            final long xpUnpenalized = Math.max(limitForXpPenalization - virtualXpBase, 0L);
            final long xpPenalized = Math.max(0L, realXpModification - xpUnpenalized) * 2L;
            return xpUnpenalized + xpPenalized;
        }
        else {
            if (virtualXpBase <= limitForXpPenalization) {
                return realXpModification;
            }
            final long maxRealPenalizedXp = (virtualXpBase - limitForXpPenalization) / 2L;
            if (maxRealPenalizedXp >= Math.abs(realXpModification)) {
                return realXpModification * 2L;
            }
            final long xpPenalized = virtualXpBase - limitForXpPenalization;
            final long xpUnpenalized2 = Math.abs(realXpModification) - maxRealPenalizedXp;
            return -(xpUnpenalized2 + xpPenalized);
        }
    }
    
    public static double virtualXpToRealXpDiff(final long virtualXpBase, final long virtualXpModification, final long limitForXpPenalization) {
        if (virtualXpModification == 0L) {
            return 0.0;
        }
        if (virtualXpModification > 0L) {
            if (virtualXpBase > limitForXpPenalization) {
                return virtualXpModification / 2.0;
            }
            if (virtualXpBase + virtualXpModification <= limitForXpPenalization) {
                return virtualXpModification;
            }
            final double xpUnpenalized = Math.max(limitForXpPenalization - virtualXpBase, 0L);
            final double xpPenalized = Math.max(0.0, virtualXpModification - xpUnpenalized) / 2.0;
            return xpUnpenalized + xpPenalized;
        }
        else {
            if (virtualXpBase <= limitForXpPenalization) {
                return virtualXpModification;
            }
            if (virtualXpBase + virtualXpModification > limitForXpPenalization) {
                return virtualXpModification / 2.0;
            }
            final double xpUnpenalized = Math.min(0L, virtualXpBase - limitForXpPenalization + virtualXpModification);
            final double xpPenalized = (limitForXpPenalization - virtualXpBase) / 2.0;
            return xpUnpenalized + xpPenalized;
        }
    }
    
    public static long getPlayerVirtualXpMissing(@NotNull final BasicCharacterInfo character) {
        if (!(character instanceof PlayerCharacterLevelable)) {
            SpellXpComputer.m_logger.error((Object)("Trying to lock spell for a characterInfo without xp : " + character));
            return 0L;
        }
        final long characterXp = ((PlayerCharacterLevelable)character).getCurrentXp();
        long totalXp = 0L;
        for (final Elements element : Elements.values()) {
            if (element.isElemental()) {
                final long treeXp = getTreeVirtualXp(character, characterXp, element);
                totalXp += treeXp;
            }
        }
        final long theoricalXp = getMaxSpellXpFromPlayerXp(characterXp);
        return theoricalXp - totalXp;
    }
    
    public static SpellLockValidity getPlayerSpellLockValidity(@NotNull final BasicCharacterInfo character) {
        if (!(character instanceof SpellXpLocker)) {
            SpellXpComputer.m_logger.error((Object)("Trying to lock spell for a characterInfo which is not a SpellXpLocker : " + character));
            return SpellLockValidity.CAP_NOT_REACHED;
        }
        if (character.isOnFight()) {
            return SpellLockValidity.BAD_CHARACTER_STATE;
        }
        final short characterLevel = character.getLevel();
        if (characterLevel < XpConstants.getPlayerCharacterLevelCap()) {
            return SpellLockValidity.CAP_NOT_REACHED;
        }
        final long virtualXpMissing = getPlayerVirtualXpMissing(character);
        if (virtualXpMissing > 1L) {
            return SpellLockValidity.CAP_REACHED_BUT_REMAINING_XP;
        }
        return SpellLockValidity.VALID;
    }
    
    public static SpellLockValidity getPlayerSpellLockValidity(@NotNull final BasicCharacterInfo character, final int spellId) {
        final SpellLockValidity globalValidity = getPlayerSpellLockValidity(character);
        if (globalValidity != SpellLockValidity.VALID) {
            return globalValidity;
        }
        final SpellInventory<? extends AbstractSpellLevel> spellInventory = character.getSpellInventory();
        if (spellInventory == null) {
            return SpellLockValidity.SPELL_UNKNOWN;
        }
        final AbstractSpellLevel level = spellInventory.getFirstWithReferenceId(spellId);
        if (level == null) {
            return SpellLockValidity.SPELL_UNKNOWN;
        }
        if (!level.getElement().isElemental()) {
            return SpellLockValidity.UNAUTHORIZED_SPELL;
        }
        if (level.getXp() == 0L) {
            return SpellLockValidity.SPELL_HAS_NO_XP;
        }
        return SpellLockValidity.VALID;
    }
    
    public static boolean canPlayerUnlockSpell(@NotNull final BasicFighter character) {
        return !character.isOnFight();
    }
    
    public static void main(final String[] args) {
        final XpTable xpTable = CharacterXpTable.getInstance();
        final long xpDeBase = xpTable.getXpByLevel(4);
        final long extendAtLevle4 = xpTable.getLevelExtent((short)4);
        final long totalXp = xpDeBase + 740L;
        final long spellXpAtLevel4 = getMaxSpellXpFromPlayerXp(xpDeBase);
        final long spellXpAtLevel2 = getMaxSpellXpFromPlayerXp(xpTable.getXpByLevel(5));
        final long spellXpAtMyLevel = getMaxSpellXpFromPlayerXp(totalXp);
        System.out.println("coucou");
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellXpComputer.class);
        CHARACTER_XP_TO_SPELL_XP_RATIO_BY_CHARACTER_LEVEL = initializeCharacterXpToSpellXpRatio();
    }
}
