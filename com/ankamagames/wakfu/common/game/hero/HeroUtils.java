package com.ankamagames.wakfu.common.game.hero;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class HeroUtils
{
    private static final Logger m_logger;
    
    public boolean canInviteHeroOrCompanion(final long ownerId) {
        HeroUtils.m_logger.error((Object)new Exception("Can't check with common, try with client or server HeroUtils"));
        return false;
    }
    
    public static Point3 getLeaderPosition(final long ownerId) {
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(HeroesLeaderManager.INSTANCE.getLeader(ownerId));
        return (hero != null) ? hero.getPosition() : new Point3();
    }
    
    public static int getLeaderWorldX(final long ownerId) {
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(HeroesLeaderManager.INSTANCE.getLeader(ownerId));
        return (hero != null) ? hero.getWorldCellX() : 0;
    }
    
    public static int getLeaderWorldY(final long ownerId) {
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(HeroesLeaderManager.INSTANCE.getLeader(ownerId));
        return (hero != null) ? hero.getWorldCellY() : 0;
    }
    
    public static int getLeaderWorldZ(final long ownerId) {
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(HeroesLeaderManager.INSTANCE.getLeader(ownerId));
        return (hero != null) ? hero.getWorldCellAltitude() : 0;
    }
    
    public static <T extends BasicCharacterInfo> T getHeroWithBagUid(final long ownerId, final long bagUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final T hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero == null) {
                continue;
            }
            final AbstractBagContainer bags = hero.getBags();
            if (bags == null) {
                continue;
            }
            if (bags.containsBag(bagUid)) {
                return hero;
            }
        }
        return null;
    }
    
    public static <T extends BasicCharacterInfo> T getHeroWithItemUidInBags(final long ownerId, final long itemUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final T hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero == null) {
                continue;
            }
            final AbstractBagContainer bags = hero.getBags();
            if (bags == null) {
                continue;
            }
            if (bags.contains(itemUid)) {
                return hero;
            }
        }
        return null;
    }
    
    public static <T extends BasicCharacterInfo> T getHeroWithItemInEquipment(final long ownerId, final long itemUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final T hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero.getEquipmentInventory().containsUniqueId(itemUid)) {
                return hero;
            }
        }
        return null;
    }
    
    public static <T extends BasicCharacterInfo> T getHeroWithItemUidFromBagsOrEquipment(final long ownerId, final long itemUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final T hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero == null) {
                continue;
            }
            final ItemEquipment inv = hero.getEquipmentInventory();
            if (inv == null) {
                continue;
            }
            final AbstractBagContainer bags = hero.getBags();
            if (bags == null) {
                continue;
            }
            if (inv.containsUniqueId(itemUid) || bags.contains(itemUid)) {
                return hero;
            }
        }
        return null;
    }
    
    public static Item getItemFromHero(final long ownerId, final long itemUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero == null) {
                continue;
            }
            final AbstractBagContainer bags = hero.getBags();
            if (bags == null) {
                continue;
            }
            if (bags.contains(itemUid)) {
                return bags.getFirstContainerWith(itemUid).getWithUniqueId(itemUid);
            }
        }
        return null;
    }
    
    public static Item getItemFromHeroEquipment(final long ownerId, final long itemUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero == null) {
                continue;
            }
            final ItemEquipment inv = hero.getEquipmentInventory();
            if (inv == null) {
                continue;
            }
            if (inv.containsUniqueId(itemUid)) {
                return ((ArrayInventoryWithoutCheck<Item, R>)inv).getWithUniqueId(itemUid);
            }
        }
        return null;
    }
    
    public static Item getItemFromHeroBagOrInventory(final long ownerId, final long itemUid) {
        final Item item = getItemFromHero(ownerId, itemUid);
        if (item != null) {
            return item;
        }
        return getItemFromHeroEquipment(ownerId, itemUid);
    }
    
    public static AbstractBag getBagFromHero(final long ownerId, final long bagUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero == null) {
                continue;
            }
            final AbstractBagContainer bags = hero.getBags();
            if (bags == null) {
                continue;
            }
            if (bags.containsBag(bagUid)) {
                return bags.getBagFromUid(bagUid);
            }
        }
        return null;
    }
    
    public static AbstractBag getBagFromHeroItem(final long ownerId, final long itemUid) {
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(ownerId)) {
            final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(characterId);
            if (hero == null) {
                continue;
            }
            final AbstractBagContainer bags = hero.getBags();
            if (bags == null) {
                continue;
            }
            if (bags.contains(itemUid)) {
                return bags.getFirstContainerWith(itemUid);
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HeroUtils.class);
    }
}
