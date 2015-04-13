package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class UiPropertyHelper
{
    private static final Logger m_logger;
    
    public static void initializeAndSet(final WakfuRunningEffect effect, final String providerTypeId, final String propertyName, final String propertyValue) {
        final HMIFiledProviderType providerType = HMIFiledProviderType.getByName(providerTypeId);
        if (providerType == null) {
            UiPropertyHelper.m_logger.error((Object)("Mauvais param\u00e8tres " + providerTypeId + "ne correspondant \u00e0 aucun HMIProviderType"), (Throwable)new IllegalArgumentException());
            return;
        }
        final HMIPropertyValueType valueType = HMIPropertyValueType.getByName(propertyValue);
        if (valueType == null) {
            UiPropertyHelper.m_logger.error((Object)("Mauvais param\u00e8tres " + propertyValue + "ne correspondant \u00e0 aucun HMIPropertyValueType"), (Throwable)new IllegalArgumentException());
            return;
        }
        final Object value = getValue(effect, valueType);
        setProperty(providerType, value);
    }
    
    private static void setProperty(final HMIFiledProviderType providerType, final Object value) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (providerType) {
            case PRISONER_TAG: {
                UiPropertyHelper.m_logger.warn((Object)"TODO prison : refaire le system de HMI Action de prison");
                break;
            }
        }
    }
    
    public static void remove(final String providerTypeId, final String propertyName, final String propertyValue) {
        final HMIFiledProviderType providerType = HMIFiledProviderType.getByName(providerTypeId);
        if (providerType == null) {
            UiPropertyHelper.m_logger.error((Object)("Mauvais param\u00e8tres " + providerTypeId + "ne correspondant \u00e0 aucun HMIProviderType"), (Throwable)new IllegalArgumentException());
            return;
        }
        removeProperty(providerType);
    }
    
    private static void removeProperty(final HMIFiledProviderType providerType) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (providerType) {
            case PRISONER_TAG: {
                UiPropertyHelper.m_logger.warn((Object)"TODO prison : refaire le system de HMI Action de prison");
                break;
            }
        }
    }
    
    private static Object getValue(final WakfuRunningEffect effect, final HMIPropertyValueType valueType) {
        Object value = null;
        switch (valueType) {
            case DURATION: {
                value = effect.getRemainingTimeInMs();
                break;
            }
        }
        return value;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UiPropertyHelper.class);
    }
}
