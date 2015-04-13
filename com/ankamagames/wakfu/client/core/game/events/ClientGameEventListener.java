package com.ankamagames.wakfu.client.core.game.events;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.game.events.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.preferences.*;
import java.util.*;

public class ClientGameEventListener
{
    private static final String PREFERENCE_STORE_BASE_STRING = "event.client.";
    private final int m_id;
    private final int m_listenedEventId;
    private final String[] m_listenedEventFilters;
    private final SimpleCriterion m_listenedEventMatchCriterion;
    private final int m_dropRate;
    private final int m_maxCount;
    private final boolean m_defaultActiveValue;
    private boolean m_active;
    private final DropTable<ClientEventActionList> m_actionsDropTable;
    
    public ClientGameEventListener(final int id, final int listenedEventId, final String[] listenedEventFilters, final SimpleCriterion listenedEventMatchCriterion, final int dropRate, final int maxCount, final boolean active) {
        super();
        this.m_id = id;
        this.m_listenedEventId = listenedEventId;
        this.m_listenedEventFilters = listenedEventFilters;
        this.m_listenedEventMatchCriterion = listenedEventMatchCriterion;
        this.m_dropRate = dropRate;
        this.m_maxCount = maxCount;
        this.m_active = active;
        this.m_defaultActiveValue = active;
        this.m_actionsDropTable = new DropTable<ClientEventActionList>();
    }
    
    public void processEvent(final ClientGameEvent event) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final PreferenceStore preferenceStore = GamePreferences.getCharacterPreferenceStore();
        if (this.m_maxCount > 0 && preferenceStore != null) {
            final int currentCount = preferenceStore.getInt("event.client." + this.m_id);
            if (currentCount >= this.m_maxCount) {
                return;
            }
        }
        if (this.m_listenedEventMatchCriterion != null && !this.m_listenedEventMatchCriterion.isValid(localPlayer, null, event, localPlayer.getAppropriateContext())) {
            return;
        }
        final int diceValue = DiceRoll.roll(100);
        if (diceValue > this.m_dropRate) {
            return;
        }
        final ClientEventActionList actionList = this.m_actionsDropTable.drop(localPlayer, null, event, localPlayer.getAppropriateContext());
        if (actionList != null) {
            final ArrayList<ClientEventAction> actions = actionList.getActions();
            for (int i = 0, size = actions.size(); i < size; ++i) {
                actions.get(i).execute();
            }
        }
        if (this.m_maxCount > 0 && preferenceStore != null) {
            final int currentCount2 = preferenceStore.getInt("event.client." + this.m_id);
            preferenceStore.setValue("event.client." + this.m_id, currentCount2 + 1);
        }
    }
    
    public DropTable<ClientEventActionList> getActionsDropTable() {
        return this.m_actionsDropTable;
    }
    
    public void addActionList(final ClientEventActionList actionList) {
        this.m_actionsDropTable.addDrop(actionList);
    }
    
    public int getListenedEventId() {
        return this.m_listenedEventId;
    }
    
    public String[] getListenedEventFilters() {
        return this.m_listenedEventFilters;
    }
    
    public SimpleCriterion getListenedEventMatchCriterion() {
        return this.m_listenedEventMatchCriterion;
    }
    
    public float getDropRate() {
        return this.m_dropRate;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean isActive() {
        return this.m_active;
    }
    
    public void setActive(final boolean active) {
        this.m_active = active;
    }
    
    public void setToDefaultActiveValue() {
        this.m_active = this.m_defaultActiveValue;
    }
}
