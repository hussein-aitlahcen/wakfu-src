package com.ankamagames.wakfu.client.core.game.challenge;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public abstract class AbstractChallengeReward extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String ITEM_FIELD = "item";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String XP_FIELD = "xp";
    public static final String KAMA_FIELD = "kama";
    public static final String XP_ICON_URL = "xpIconUrl";
    public static final String RANK_DESCRIPTION_FIELD = "rankDescription";
    public static final String[] FIELDS;
    
    @Override
    public String[] getFields() {
        return AbstractChallengeReward.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("item")) {
            return this.getItem();
        }
        if (fieldName.equals("quantity")) {
            return this.getQuantity();
        }
        if (fieldName.equals("xp")) {
            return this.getXp();
        }
        if (fieldName.equals("kama")) {
            return this.getKama();
        }
        if (fieldName.equals("xpIconUrl")) {
            return this.getXpIconUrl();
        }
        if (fieldName.equals("rankDescription")) {
            return this.getRankDescription();
        }
        return null;
    }
    
    protected abstract AbstractReferenceItem getItem();
    
    protected abstract int getQuantity();
    
    protected abstract int getXp();
    
    protected abstract int getKama();
    
    protected abstract String getXpIconUrl();
    
    protected abstract String getRankDescription();
    
    static {
        m_logger = Logger.getLogger((Class)AbstractChallengeReward.class);
        FIELDS = new String[] { "item", "quantity", "xpIconUrl", "xp", "kama", "rankDescription" };
    }
}
