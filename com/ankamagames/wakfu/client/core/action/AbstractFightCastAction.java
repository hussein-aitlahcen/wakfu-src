package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.cast.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import org.jetbrains.annotations.*;

public abstract class AbstractFightCastAction extends AbstractFightScriptedAction
{
    private static final int CRITICAL_MISS_SCRIPT = 999;
    private static final float CRITICAL_ZOOM_FACTOR = 1.4f;
    protected static final FightLogger m_fightLogger;
    private IsoWorldTarget m_savedCameraTarget;
    private double m_savedZoomFactor;
    private final boolean m_isCriticalHit;
    private final boolean m_isCriticalMiss;
    private int m_x;
    private int m_y;
    private short m_z;
    
    public AbstractFightCastAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final boolean criticalHit, final boolean criticalMiss, final long casterId, final int x, final int y, final short z) {
        super(uniqueId, actionType, actionId, fightId);
        this.addJavaFunctionsLibrary(new CastFunctionsLibrary(this));
        this.setInstigatorId(casterId);
        this.m_isCriticalHit = criticalHit;
        this.m_isCriticalMiss = criticalMiss;
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    @Override
    public long onRun() {
        if (this.m_isCriticalMiss) {
            this.setScriptFileId(999);
        }
        return super.onRun();
    }
    
    public boolean isCriticalHit() {
        return this.m_isCriticalHit;
    }
    
    public boolean isCriticalMiss() {
        return this.m_isCriticalMiss;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public short getZ() {
        return this.m_z;
    }
    
    public void setZ(final short z) {
        this.m_z = z;
    }
    
    public abstract short getLevel();
    
    public boolean isTargetCellInRange() {
        return false;
    }
    
    @Nullable
    public BasicEffectArea getValidInputGate() {
        return null;
    }
    
    @Nullable
    public BasicEffectArea getValidOutputGate() {
        return null;
    }
    
    static {
        m_fightLogger = new FightLogger();
    }
}
