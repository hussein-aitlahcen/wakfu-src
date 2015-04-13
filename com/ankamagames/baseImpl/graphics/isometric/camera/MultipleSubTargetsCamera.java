package com.ankamagames.baseImpl.graphics.isometric.camera;

import com.ankamagames.baseImpl.graphics.isometric.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class MultipleSubTargetsCamera implements IsoWorldTarget
{
    private static final Logger m_logger;
    private final IsoWorldTarget m_defaultTarget;
    private final FightMap m_fightMap;
    private final ArrayList<IsoWorldTarget> m_targets;
    private final TIntArrayList m_targetsPond;
    
    public MultipleSubTargetsCamera(final IsoWorldTarget defaultTarget, final FightMap fightMap) {
        super();
        this.m_targets = new ArrayList<IsoWorldTarget>();
        this.m_targetsPond = new TIntArrayList();
        this.m_defaultTarget = defaultTarget;
        this.m_fightMap = fightMap;
    }
    
    public void clearTargets() {
        this.m_targets.clear();
        this.m_targetsPond.clear();
    }
    
    public void addTarget(final IsoWorldTarget target, final int pond) {
        if (!this.m_targets.contains(target)) {
            this.m_targets.add(target);
            this.m_targetsPond.add(pond);
        }
    }
    
    public int targetCount() {
        return this.m_targets.size();
    }
    
    @Override
    public float getEntityRenderRadius() {
        return 0.0f;
    }
    
    @Override
    public short getWorldCellAltitude() {
        return (short)MathHelper.fastFloor(this.getAltitude());
    }
    
    @Override
    public float getAltitude() {
        final int size = this.m_targets.size();
        int p = 0;
        float mz = 0.0f;
        for (int i = 0; i < size; ++i) {
            if (this.isValid(this.m_targets.get(i))) {
                final int pond = this.m_targetsPond.get(i);
                mz += this.m_targets.get(i).getAltitude() * pond;
                p += pond;
            }
        }
        if (p == 0) {
            MultipleSubTargetsCamera.m_logger.error((Object)"Aucune target d\u00e9finie");
            return this.m_defaultTarget.getAltitude();
        }
        return mz / p;
    }
    
    @Override
    public int getWorldCellX() {
        return MathHelper.fastRound(this.getWorldX());
    }
    
    @Override
    public int getWorldCellY() {
        return MathHelper.fastRound(this.getWorldY());
    }
    
    @Override
    public float getWorldX() {
        final int size = this.m_targets.size();
        int p = 0;
        float mx = 0.0f;
        for (int i = 0; i < size; ++i) {
            if (this.isValid(this.m_targets.get(i))) {
                final int pond = this.m_targetsPond.get(i);
                mx += this.m_targets.get(i).getWorldX() * pond;
                p += pond;
            }
        }
        if (p == 0) {
            MultipleSubTargetsCamera.m_logger.error((Object)"Aucune target d\u00e9finie");
            return this.m_defaultTarget.getWorldX();
        }
        return mx / p;
    }
    
    @Override
    public float getWorldY() {
        final int size = this.m_targets.size();
        int p = 0;
        float my = 0.0f;
        for (int i = 0; i < size; ++i) {
            if (this.isValid(this.m_targets.get(i))) {
                final int pond = this.m_targetsPond.get(i);
                my += this.m_targets.get(i).getWorldY() * pond;
                p += pond;
            }
        }
        if (p == 0) {
            MultipleSubTargetsCamera.m_logger.error((Object)"Aucune target d\u00e9finie");
            return this.m_defaultTarget.getWorldY();
        }
        return my / p;
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY) {
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float worldZ) {
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
    
    private boolean isValid(final IsoWorldTarget target) {
        return this.m_fightMap == null || this.m_fightMap.isInMap(target.getWorldCellX(), target.getWorldCellY());
    }
    
    static {
        m_logger = Logger.getLogger((Class)MultipleSubTargetsCamera.class);
    }
}
