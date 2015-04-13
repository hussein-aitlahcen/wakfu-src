package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.event.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import gnu.trove.*;

public class CNationJusticeEventHandler implements NationJusticeEventHandler
{
    public static CNationJusticeEventHandler INSTANCE;
    private int m_currentNationId;
    private ArrayList<Long> m_lawsCache;
    
    private CNationJusticeEventHandler() {
        super();
        this.m_currentNationId = -1;
        this.m_lawsCache = new ArrayList<Long>();
    }
    
    @Override
    public void onLawsChanged(final Nation nation) {
        final int nationId = nation.getNationId();
        final boolean firstChange = this.m_currentNationId == -1 || this.m_currentNationId != nationId;
        this.m_currentNationId = nationId;
        if (firstChange) {
            this.m_lawsCache.clear();
        }
        final NationLawsManager lawManager = nation.getLawManager();
        final TLongObjectIterator<NationLaw> it = lawManager.lawsIterator();
        while (it.hasNext()) {
            it.advance();
            final long lawId = it.key();
            if (lawManager.isActive(lawId)) {
                if (this.m_lawsCache.contains(lawId)) {
                    continue;
                }
                this.m_lawsCache.add(lawId);
                if (firstChange) {
                    continue;
                }
                final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                final String lawName = WakfuTranslator.getInstance().getString(97, (int)lawId, new Object[0]);
                final String title = WakfuTranslator.getInstance().getString("notification.lawAddedTitle");
                final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.lawAddedText", nationName, lawName), NotificationMessageType.NATION, "2");
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
                Worker.getInstance().pushMessage(uiNotificationMessage);
            }
            else {
                if (firstChange || !this.m_lawsCache.contains(lawId)) {
                    continue;
                }
                this.m_lawsCache.remove(lawId);
                final String nationName = WakfuTranslator.getInstance().getString(39, nationId, new Object[0]);
                final String lawName = WakfuTranslator.getInstance().getString(97, (int)lawId, new Object[0]);
                final String title = WakfuTranslator.getInstance().getString("notification.lawRemovedTitle");
                final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.lawRemovedText", nationName, lawName), NotificationMessageType.NATION, "2");
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
                Worker.getInstance().pushMessage(uiNotificationMessage);
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(ProtectorView.getInstance(), "nation");
    }
    
    public void clean() {
        this.m_lawsCache.clear();
        this.m_currentNationId = -1;
    }
    
    static {
        CNationJusticeEventHandler.INSTANCE = new CNationJusticeEventHandler();
    }
}
