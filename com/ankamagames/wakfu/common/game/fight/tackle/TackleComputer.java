package com.ankamagames.wakfu.common.game.fight.tackle;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

public final class TackleComputer
{
    public static final int TACKLER_OUT_OF_RANGE_SCORE = -1;
    private static final float BACK_DODGE_POWER = 1.5f;
    private static final float SIDE_DODGE_POWER = 1.25f;
    private static final float FRONT_DODGE_POWER = 1.0f;
    public static final int MIN_TACKLE_PERCENTAGE = 5;
    public static final int MAX_TACKLE_PERCENTAGE = 95;
    private static final int DEFAULT_PERCENTAGE = 50;
    private TackleUser m_mover;
    private TackleUser m_tackler;
    private Point3 m_moverPos;
    private boolean m_isTackled;
    private boolean m_isDodge;
    
    public TackleComputer() {
        super();
    }
    
    public TackleComputer(final TackleUser mover) {
        super();
        this.m_mover = mover;
    }
    
    public void setMover(final TackleUser mover) {
        this.m_mover = mover;
    }
    
    public void setTackler(final TackleUser tackler) {
        this.m_tackler = tackler;
    }
    
    public void setMoverPos(final Point3 moverPos) {
        this.m_moverPos = moverPos;
    }
    
    public boolean isTackled() {
        return this.m_isTackled;
    }
    
    public boolean isDodge() {
        return this.m_isDodge;
    }
    
    public void testTackle() {
        this.m_isTackled = false;
        this.m_isDodge = false;
        final int tacklePercentage = this.getTacklePercentage();
        if (tacklePercentage == 0 || tacklePercentage == -1) {
            return;
        }
        final int roll = DiceRoll.roll(100);
        if (roll < tacklePercentage) {
            this.m_isTackled = true;
        }
        else {
            this.m_isDodge = true;
        }
    }
    
    public int getTacklePercentage() {
        if (this.moverIsNotInTacklerRange()) {
            return -1;
        }
        if (!this.m_tackler.canTackle()) {
            return -1;
        }
        final int dodgeDiff = this.m_mover.getDodgeValue() - this.m_tackler.getTackleValue();
        final float dodgeBeforePositionFactor = 50 + dodgeDiff / 2;
        final float dodge = dodgeBeforePositionFactor * this.getDodgeFactorFromPosition();
        final float tackle = Math.max(0.0f, 100.0f - dodge);
        final int minTacklePercentage = this.m_mover.isActiveProperty(FightPropertyType.NO_DODGE_LIMIT) ? ((int)tackle) : 5;
        return MathHelper.clamp((int)tackle, minTacklePercentage, 95);
    }
    
    private boolean moverIsNotInTacklerRange() {
        return TackleUtils.moverIsNotInTacklerRange(this.m_tackler, this.m_mover, this.m_moverPos);
    }
    
    private float getDodgeFactorFromPosition() {
        if (this.m_tackler.getPartLocalisator() != null) {
            Part part;
            if (this.m_moverPos == null) {
                part = this.m_tackler.getPartLocalisator().getMainPartInSightFromPosition(this.m_mover.getWorldCellX(), this.m_mover.getWorldCellY(), this.m_mover.getWorldCellAltitude());
            }
            else {
                part = this.m_tackler.getPartLocalisator().getMainPartInSightFromPosition(this.m_moverPos.getX(), this.m_moverPos.getY(), this.m_moverPos.getZ());
            }
            if (part != null) {
                switch (part.getPartId()) {
                    case 2: {
                        return 1.5f;
                    }
                    case 1:
                    case 3: {
                        return 1.25f;
                    }
                    default: {
                        return 1.0f;
                    }
                }
            }
        }
        return 0.0f;
    }
}
