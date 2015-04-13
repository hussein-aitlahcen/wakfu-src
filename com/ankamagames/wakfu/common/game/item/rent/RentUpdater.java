package com.ankamagames.wakfu.common.game.item.rent;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

public final class RentUpdater
{
    private static final Logger m_logger;
    
    public static boolean updateType(final int type, final BasicCharacterInfo player) {
        if (player == null) {
            return false;
        }
        switch (type) {
            case 1: {
                return updateFightRent(player);
            }
            case 2: {
                break;
            }
            default: {
                RentUpdater.m_logger.error((Object)("Type de location inconnu pour la mise \u00e0 jour " + type));
                break;
            }
        }
        return false;
    }
    
    private static boolean updateFightRent(final BasicCharacterInfo player) {
        final ItemEquipment equipment = player.getEquipmentInventory();
        if (equipment == null) {
            return false;
        }
        boolean somethingModified = false;
        for (final Item item : equipment) {
            if (!item.isRent()) {
                continue;
            }
            final RentInfo rentInfo = item.getRentInfo();
            if (rentInfo.getType() != 1) {
                continue;
            }
            final FightLimitedRentInfo fightRentInfo = (FightLimitedRentInfo)rentInfo;
            fightRentInfo.incFightCount();
            somethingModified = true;
        }
        return somethingModified;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RentUpdater.class);
    }
}
