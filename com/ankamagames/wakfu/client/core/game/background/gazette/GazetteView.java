package com.ankamagames.wakfu.client.core.game.background.gazette;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.background.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class GazetteView extends ImmutableFieldProvider
{
    public static final String NEW = "new";
    public static final String TITLE = "title";
    public static final String ISSUE_NUMBER = "issueNumber";
    public static final String UNLOCKED = "unlocked";
    private final BackgroundDisplayData m_data;
    private final GameDateConst m_unlockDate;
    private int m_issueNumber;
    
    public GazetteView(final BackgroundDisplayData data, final GameDateConst unlockDate) {
        super();
        this.m_data = data;
        this.m_unlockDate = unlockDate;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    public int getId() {
        return this.m_data.getId();
    }
    
    public void setIssueNumber(final int issueNumber) {
        this.m_issueNumber = issueNumber;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("new")) {
            return !GazetteManager.INSTANCE.isRead(this.m_data.getId());
        }
        if (fieldName.equals("title")) {
            if (this.isUnlocked()) {
                return WakfuTranslator.getInstance().getString(121, this.m_data.getId(), new Object[0]);
            }
            return "???";
        }
        else {
            if (fieldName.equals("issueNumber")) {
                return this.m_issueNumber;
            }
            if (fieldName.equals("unlocked")) {
                return this.isUnlocked();
            }
            return null;
        }
    }
    
    public boolean isUnlocked() {
        if (this.m_unlockDate.isNull()) {
            return true;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        return now.afterOrEquals(this.m_unlockDate);
    }
    
    void refreshUI() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "new");
    }
}
