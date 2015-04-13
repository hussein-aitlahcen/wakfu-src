package com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

final class CellValidationResult
{
    private boolean m_isValid;
    private boolean m_error;
    private short m_arrivalAltitude;
    private boolean m_stopped;
    private FightObstacle m_obstacle;
    
    public boolean isValid() {
        return this.m_isValid;
    }
    
    public void setValid(final boolean valid) {
        this.m_isValid = valid;
    }
    
    public void setError(final boolean error) {
        this.m_error = error;
    }
    
    public boolean isError() {
        return this.m_error;
    }
    
    public void setArrivalAltitude(final short arrivalAltitude) {
        this.m_arrivalAltitude = arrivalAltitude;
    }
    
    public short getArrivalAltitude() {
        return this.m_arrivalAltitude;
    }
    
    public void setStopped(final boolean stopped) {
        this.m_stopped = stopped;
    }
    
    public boolean isStopped() {
        return this.m_stopped;
    }
    
    public void setObstacle(final FightObstacle obstacle) {
        this.m_obstacle = obstacle;
    }
    
    public FightObstacle getObstacle() {
        return this.m_obstacle;
    }
}
