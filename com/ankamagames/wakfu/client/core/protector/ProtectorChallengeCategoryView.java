package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.protector.inventory.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ProtectorChallengeCategoryView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String ICON_URL = "iconUrl";
    public static final String ENABLED = "enabled";
    public static final String ITEMS = "items";
    public static final String ID_STRING = "idString";
    private final ChallengeCategory m_category;
    private final ArrayList<ProtectorChallengeItemView> m_items;
    
    public ProtectorChallengeCategoryView(final ChallengeCategory category) {
        super();
        this.m_items = new ArrayList<ProtectorChallengeItemView>();
        this.m_category = category;
    }
    
    public void clear() {
        this.m_items.clear();
    }
    
    public void addItem(final ProtectorChallengeItemView item) {
        this.m_items.add(item);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    public boolean isEnabled() {
        return this.m_items.size() != 0;
    }
    
    public void onWalletUpdated() {
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            this.m_items.get(i).onWalletUpdated();
        }
    }
    
    public ChallengeCategory getCategory() {
        return this.m_category;
    }
    
    void updateTime() {
        for (int i = 0, size = this.m_items.size(); i < size; ++i) {
            final ProtectorChallengeItemView item = this.m_items.get(i);
            item.updateTime();
        }
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("challengeCategoryIconsPath"), this.m_category.getId());
            }
            catch (PropertyException e) {
                ProtectorChallengeCategoryView.m_logger.error((Object)"Exception", (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("enabled")) {
            return this.m_items.size() > 0;
        }
        if (fieldName.equals("items")) {
            return this.m_items;
        }
        if (fieldName.equals("idString")) {
            return String.valueOf(this.m_category.getId());
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorChallengeCategoryView.class);
    }
}
