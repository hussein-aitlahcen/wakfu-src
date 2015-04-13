package com.ankamagames.baseImpl.graphics.alea.display.effects;

import com.ankamagames.framework.graphics.engine.*;

public class CameraEffect extends Effect
{
    protected Direction m_direction;
    
    public CameraEffect() {
        super();
        this.m_direction = Direction.BOTH;
    }
    
    public void setDirection(final Direction direction) {
        if (direction == null) {
            this.m_direction = Direction.BOTH;
        }
        else {
            this.m_direction = direction;
        }
    }
    
    public void setDirection(final String direction) {
        this.setDirection(Direction.valueOf(direction));
    }
    
    @Override
    public void clear() {
        this.m_camera = null;
    }
    
    @Override
    public void render(final Renderer renderer) {
    }
    
    protected final boolean applyOnX() {
        return this.m_direction.applyOnX();
    }
    
    protected final boolean applyOnY() {
        return this.m_direction.applyOnY();
    }
    
    public enum Direction
    {
        NONE {
            @Override
            boolean applyOnX() {
                return false;
            }
            
            @Override
            boolean applyOnY() {
                return false;
            }
        }, 
        X {
            @Override
            boolean applyOnX() {
                return true;
            }
            
            @Override
            boolean applyOnY() {
                return false;
            }
        }, 
        Y {
            @Override
            boolean applyOnX() {
                return false;
            }
            
            @Override
            boolean applyOnY() {
                return true;
            }
        }, 
        BOTH {
            @Override
            boolean applyOnX() {
                return true;
            }
            
            @Override
            boolean applyOnY() {
                return true;
            }
        };
        
        abstract boolean applyOnX();
        
        abstract boolean applyOnY();
    }
}
