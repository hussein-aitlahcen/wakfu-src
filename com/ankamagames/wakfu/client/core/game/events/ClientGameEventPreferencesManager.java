package com.ankamagames.wakfu.client.core.game.events;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import gnu.trove.*;

public final class ClientGameEventPreferencesManager
{
    private static final Logger m_logger;
    public static final String PREFERENCES_PATTERN = "( \\d+,)*";
    
    public static void setActiveValueFromPreferences() {
        final WakfuGamePreferences gamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final String activeListeners = (String)gamePreferences.getFieldValue(WakfuKeyPreferenceStoreEnum.ACTIVE_CLIENT_EVENT_LISTENERS.getKey());
        final String inactiveListeners = (String)gamePreferences.getFieldValue(WakfuKeyPreferenceStoreEnum.INACTIVE_CLIENT_EVENT_LISTENERS.getKey());
        TIntHashSet activeIds;
        TIntHashSet inactiveIds;
        try {
            activeIds = extractIds(activeListeners);
            inactiveIds = extractIds(inactiveListeners);
        }
        catch (Exception e) {
            ClientGameEventPreferencesManager.m_logger.error((Object)"Exception levee, on reinitialise \u00e0 0 les pr\u00e9f\u00e9rences", (Throwable)e);
            gamePreferences.setValue(WakfuKeyPreferenceStoreEnum.ACTIVE_CLIENT_EVENT_LISTENERS, "");
            gamePreferences.setValue(WakfuKeyPreferenceStoreEnum.INACTIVE_CLIENT_EVENT_LISTENERS, "");
            return;
        }
        removeUnknownIds(activeIds, WakfuKeyPreferenceStoreEnum.ACTIVE_CLIENT_EVENT_LISTENERS);
        removeUnknownIds(inactiveIds, WakfuKeyPreferenceStoreEnum.INACTIVE_CLIENT_EVENT_LISTENERS);
        removeDouble(activeIds, inactiveIds);
        changeActivationState(activeIds, true);
        changeActivationState(inactiveIds, false);
    }
    
    private static void changeActivationState(final TIntHash activeIds, final boolean isActive) {
        activeIds.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                final ClientGameEventListener clientGameEventListener = ClientGameEventManager.INSTANCE.getListener(value);
                clientGameEventListener.setActive(isActive);
                return true;
            }
        });
    }
    
    private static void removeDouble(final TIntHashSet activeIds, final TIntHashSet inactiveIds) {
        final TIntIterator it = activeIds.iterator();
        while (it.hasNext()) {
            final int id = it.next();
            if (!inactiveIds.contains(id)) {
                continue;
            }
            it.remove();
            inactiveIds.remove(id);
            removeIdFrom(WakfuKeyPreferenceStoreEnum.ACTIVE_CLIENT_EVENT_LISTENERS, getIdStringForPreferences(id));
            removeIdFrom(WakfuKeyPreferenceStoreEnum.INACTIVE_CLIENT_EVENT_LISTENERS, getIdStringForPreferences(id));
        }
    }
    
    private static void removeUnknownIds(final TIntHashSet ids, final KeyInterface key) {
        final TIntIterator it = ids.iterator();
        while (it.hasNext()) {
            final int id = it.next();
            if (ClientGameEventManager.INSTANCE.getListener(id) == null) {
                removeIdFrom(key, getIdStringForPreferences(id));
                it.remove();
            }
        }
    }
    
    private static TIntHashSet extractIds(final String preferenceString) {
        final boolean matches = preferenceString == null || preferenceString.matches("( \\d+,)*");
        if (!matches) {
            throw new IllegalArgumentException("Mauvais formatage des pr\u00e9f\u00e9rences");
        }
        final String[] splittedActive = (preferenceString != null && preferenceString.length() > 0) ? preferenceString.split(",") : new String[0];
        final TIntHashSet ids = new TIntHashSet();
        for (int i = 0; i < splittedActive.length; ++i) {
            final String active = splittedActive[i].replaceAll(" ", "");
            final int id = Integer.parseInt(active);
            ids.add(id);
        }
        return ids;
    }
    
    public static void changePreferences(final ClientGameEventListener listener) {
        final String idString = getIdStringForPreferences(listener.getId());
        if (listener.isActive()) {
            addIdTo(WakfuKeyPreferenceStoreEnum.ACTIVE_CLIENT_EVENT_LISTENERS, idString);
            removeIdFrom(WakfuKeyPreferenceStoreEnum.INACTIVE_CLIENT_EVENT_LISTENERS, idString);
        }
        else {
            addIdTo(WakfuKeyPreferenceStoreEnum.INACTIVE_CLIENT_EVENT_LISTENERS, idString);
            removeIdFrom(WakfuKeyPreferenceStoreEnum.ACTIVE_CLIENT_EVENT_LISTENERS, idString);
        }
    }
    
    private static String getIdStringForPreferences(final int id) {
        return ' ' + Integer.toString(id) + ',';
    }
    
    private static void addIdTo(final KeyInterface key, final String idToAdd) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final String activatedListeners = wakfuGamePreferences.getStringValue(key);
        if (activatedListeners == null || activatedListeners.length() == 0) {
            wakfuGamePreferences.setValue(key, idToAdd);
        }
        else if (!activatedListeners.contains(idToAdd)) {
            final String concat = activatedListeners.concat(idToAdd);
            wakfuGamePreferences.setValue(key, concat);
        }
    }
    
    private static void removeIdFrom(final KeyInterface key, final String idToAdd) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final String activatedListeners = wakfuGamePreferences.getStringValue(key);
        if (activatedListeners == null || activatedListeners.length() == 0) {
            return;
        }
        if (!activatedListeners.contains(idToAdd)) {
            return;
        }
        final String s = activatedListeners.replaceAll(idToAdd, "");
        wakfuGamePreferences.setValue(key, s);
    }
    
    public static void main(final String[] args) {
        final String pattern = "( \\d+,)*";
        final String s = "";
        doesMatch(pattern, "");
        doesMatch(pattern, "qsfd");
        doesMatch(pattern, "125");
        doesMatch(pattern, "125, 123");
        doesMatch(pattern, "125, 1231,");
        doesMatch(pattern, "125, 1231, ");
        doesMatch(pattern, " 125, 1231, ");
        doesMatch(pattern, " 125, 1231,");
        doesMatch(pattern, "125, 1231, n, ");
    }
    
    private static void doesMatch(final String pattern, final String s) {
        System.err.println(s + " -> " + s.matches(pattern));
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientGameEventPreferencesManager.class);
    }
}
