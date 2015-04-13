package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class GuildBonusSubCategoryView extends ImmutableFieldProvider
{
    public static final String NAME = "name";
    public static final String OPENED = "opened";
    public static final String BONUSES = "bonuses";
    private boolean m_opened;
    private final ArrayList<GuildBonusView> m_bonusViews;
    private final int m_level;
    
    public GuildBonusSubCategoryView(final int level, final ArrayList<GuildBonusView> bonusViews) {
        super();
        this.m_opened = false;
        this.m_level = level;
        this.m_bonusViews = bonusViews;
        this.m_opened = (level == 1);
    }
    
    public int getLevel() {
        return this.m_level;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString("levelParam", this.m_level);
        }
        if (fieldName.equals("opened")) {
            return this.m_opened;
        }
        if (fieldName.equals("bonuses")) {
            return this.m_bonusViews;
        }
        return null;
    }
    
    public void toggleOpen() {
        this.m_opened = !this.m_opened;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "opened");
    }
}
