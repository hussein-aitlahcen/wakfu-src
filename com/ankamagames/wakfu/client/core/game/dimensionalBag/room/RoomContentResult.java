package com.ankamagames.wakfu.client.core.game.dimensionalBag.room;

public enum RoomContentResult
{
    OK, 
    USER_CANCEL, 
    BLOCKED, 
    CHECKER_RETURN_FALSE, 
    MAX_CONTENT_REACHED;
    
    public boolean hasErrorKey() {
        switch (this) {
            case OK:
            case USER_CANCEL: {
                return false;
            }
            case BLOCKED:
            case CHECKER_RETURN_FALSE:
            case MAX_CONTENT_REACHED: {
                return true;
            }
            default: {
                throw new IllegalArgumentException("R\u00e9sultat " + this + " non g\u00e9r\u00e9");
            }
        }
    }
    
    public String getErrorKey() {
        switch (this) {
            case BLOCKED:
            case CHECKER_RETURN_FALSE: {
                return "dimensionalBag.error.ie.badPosition";
            }
            case MAX_CONTENT_REACHED: {
                return "dimensionalBag.error.ie.maxContentReached";
            }
            default: {
                throw new IllegalArgumentException("R\u00e9sultat " + this + " non g\u00e9r\u00e9");
            }
        }
    }
}
