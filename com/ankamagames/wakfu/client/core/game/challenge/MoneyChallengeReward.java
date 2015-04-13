package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;

public class MoneyChallengeReward extends AbstractChallengeReward
{
    private static final Logger m_logger;
    public static final int ONE_KAMA_ITEM_ID = 9297;
    
    @Override
    protected AbstractReferenceItem getItem() {
        return ReferenceItemManager.getInstance().getReferenceItem(9297);
    }
    
    @Override
    protected int getQuantity() {
        return 1;
    }
    
    @Override
    protected int getXp() {
        return 0;
    }
    
    @Override
    protected int getKama() {
        return 0;
    }
    
    @Override
    protected String getXpIconUrl() {
        return null;
    }
    
    @Override
    protected String getRankDescription() {
        return WakfuTranslator.getInstance().getString("challenge.reward.rank.1");
    }
    
    static {
        m_logger = Logger.getLogger((Class)MoneyChallengeReward.class);
    }
}
