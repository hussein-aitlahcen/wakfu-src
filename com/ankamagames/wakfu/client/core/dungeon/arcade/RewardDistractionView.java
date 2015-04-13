package com.ankamagames.wakfu.client.core.dungeon.arcade;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;

public class RewardDistractionView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String REWARDS_FIELD = "rewards";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String[] FIELDS;
    private final String m_name;
    private final String m_description;
    private final ArrayList<ReferenceItem> m_rewards;
    
    public RewardDistractionView(final String name, final String description, final int[] itemIds) {
        super();
        this.m_rewards = new ArrayList<ReferenceItem>();
        this.m_name = name;
        this.m_description = description;
        for (final int itemId : itemIds) {
            this.m_rewards.add(ReferenceItemManager.getInstance().getReferenceItem(itemId));
        }
    }
    
    @Override
    public String[] getFields() {
        return RewardDistractionView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("rewards")) {
            return (this.m_rewards.size() > 0) ? this.m_rewards : null;
        }
        if (fieldName.equals("description")) {
            return this.m_description;
        }
        return null;
    }
    
    static {
        FIELDS = new String[] { "name", "rewards", "description" };
    }
}
