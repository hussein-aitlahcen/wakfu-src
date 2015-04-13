package com.ankamagames.wakfu.common.account.subscription;

import gnu.trove.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.title.*;

public final class SubscriptionEmoteAndTitleLimitations
{
    public static final TIntHashSet AUTHORIZED_EMOTES_FOR_ALL;
    public static final int ZH_TITLE_VIP_1 = 278;
    public static final int ZH_TITLE_VIP_2 = 279;
    public static final int ZH_TITLE_VIP_3 = 280;
    public static final int ZH_TITLE_VIP_4 = 281;
    public static final int ZH_EMOTE_VIP_3 = 20143;
    public static final int ZH_EMOTE_VIP_4 = 20144;
    
    public static boolean isAuthorizedTitle(final SubscriptionLevel level, final int titleId) {
        if (!WakfuAccountInformationHandler.ZH_SUBSCRIBER_LEVELS.contains(level) && level != SubscriptionLevel.ZH_FREE) {
            return true;
        }
        if (titleId == 278) {
            return level == SubscriptionLevel.ZH_VIP1;
        }
        if (titleId == 279) {
            return level == SubscriptionLevel.ZH_VIP2;
        }
        if (titleId == 280) {
            return level == SubscriptionLevel.ZH_VIP3;
        }
        return titleId != 281 || level == SubscriptionLevel.ZH_VIP4;
    }
    
    public static boolean isAuthorizedEmote(final SubscriptionLevel level, final int emoteId) {
        if (!WakfuAccountInformationHandler.ZH_SUBSCRIBER_LEVELS.contains(level) && level != SubscriptionLevel.ZH_FREE) {
            return true;
        }
        if (emoteId == 20143) {
            return level == SubscriptionLevel.ZH_VIP3;
        }
        return emoteId != 20144 || level == SubscriptionLevel.ZH_VIP4;
    }
    
    public static void resetCurrentTitleIfNecessary(final TitleHolder titleHolder, final SubscriptionLevel subscriptionLevel) {
        final short currentTitle = titleHolder.getCurrentTitle();
        if (!isAuthorizedTitle(subscriptionLevel, currentTitle)) {
            titleHolder.setCurrentTitle((short)(-1));
        }
    }
    
    static {
        AUTHORIZED_EMOTES_FOR_ALL = new TIntHashSet(new int[0]);
    }
}
