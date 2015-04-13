package com.ankamagames.wakfu.client.service.updater.message;

public enum MessageId
{
    UNKNOWN, 
    UPDATE_ENGINE_STATE, 
    PROGRESS, 
    NEW_GAME_CLIENT_CONNECTED, 
    ERROR;
    
    static MessageId from(final String value) {
        for (final MessageId id : values()) {
            if (id.name().equals(value)) {
                return id;
            }
        }
        return MessageId.UNKNOWN;
    }
}
