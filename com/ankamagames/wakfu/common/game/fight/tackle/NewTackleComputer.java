package com.ankamagames.wakfu.common.game.fight.tackle;

import com.ankamagames.wakfu.common.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;

public final class NewTackleComputer
{
    private static final int LOSS_SUM_MAX = 8;
    private static final float BASE_FACTOR = 2.3333333f;
    private static final int FRONT_MODIFICATOR = 0;
    private static final int SIDE_MODIFICATOR = -1;
    private static final int BACK_MODIFICATOR = -2;
    private TackleUser m_mover;
    
    public NewTackleComputer() {
        super();
    }
    
    public NewTackleComputer(final TackleUser mover) {
        super();
        this.m_mover = mover;
    }
    
    public void setMover(@NotNull final TackleUser mover) {
        this.m_mover = mover;
    }
    
    public TackleResult getTackleResult(final Collection<? extends BasicCharacterInfo> potentialTacklers, final Point3 moverPos) {
        final List<? extends BasicCharacterInfo> inRangeTacklers = this.getSortedInRangeTacklers(potentialTacklers, moverPos);
        if (inRangeTacklers.isEmpty()) {
            final TackleResult res = new TackleResult();
            res.setTacklers(Collections.emptyList());
            return res;
        }
        final int tackleValue = computeTackleValue(inRangeTacklers);
        final int dodgeValue = this.m_mover.getDodgeValue();
        return this.computeTackleResult(inRangeTacklers, tackleValue, dodgeValue);
    }
    
    private TackleResult computeTackleResult(final List<? extends BasicCharacterInfo> inRangeTacklers, final int tackleValue, final int dodgeValue) {
        final int lossSum = this.computeLossSum(tackleValue, dodgeValue, inRangeTacklers);
        final int mpLoss = MathHelper.fastCeil(lossSum / 2.0f);
        final int apLoss = lossSum - mpLoss;
        final TackleResult res = new TackleResult();
        res.setTacklers(inRangeTacklers);
        res.setApLoss(apLoss);
        res.setMpLoss(mpLoss);
        return res;
    }
    
    private float levelModificator() {
        final short level = this.m_mover.getLevel();
        return levelModificator(level);
    }
    
    private static float levelModificator(final short level) {
        return Math.min(0.5f + level / 200.0f, 1.0f);
    }
    
    private int computeLossSum(final int tackleValue, final int dodgeValue, final List<? extends BasicCharacterInfo> inRangeTacklers) {
        final int positiveDodgeValue = Math.max(0, dodgeValue);
        final int positiveTackleValue = Math.max(0, tackleValue);
        float baseValue;
        if (positiveDodgeValue == positiveTackleValue) {
            baseValue = 2.3333333f * (positiveTackleValue - positiveDodgeValue);
        }
        else {
            baseValue = 2.3333333f * (positiveTackleValue - positiveDodgeValue) / (positiveDodgeValue + positiveTackleValue);
        }
        final float clampedBaseValue = MathHelper.clamp(baseValue, -1.0f, 1.0f);
        final float valueBasedOnMaxLoss = (clampedBaseValue + 1.0f) * 8.0f / 2.0f * this.levelModificator();
        final float finalValue = valueBasedOnMaxLoss + this.getPositionModificator(inRangeTacklers);
        return MathHelper.fastFloor(MathHelper.maxFloat(0.0f, finalValue));
    }
    
    private int getPositionModificator(final List<? extends BasicCharacterInfo> inRangeTacklers) {
        final int worstPartId = this.computeWorstPartId(inRangeTacklers);
        return getModificatorFromPartId(worstPartId);
    }
    
    private static int getModificatorFromPartId(final int worstPartId) {
        switch (worstPartId) {
            case 0: {
                return 0;
            }
            case 1:
            case 3: {
                return -1;
            }
            case 2: {
                return -2;
            }
            default: {
                return 0;
            }
        }
    }
    
    private int computeWorstPartId(final List<? extends BasicCharacterInfo> inRangeTacklers) {
        int worstPartId = 2;
        for (final BasicCharacterInfo inRangeTackler : inRangeTacklers) {
            final PartLocalisator partLocalisator = inRangeTackler.getPartLocalisator();
            final Part part = partLocalisator.getMainPartInSightFromPosition(this.m_mover.getWorldCellX(), this.m_mover.getWorldCellY(), this.m_mover.getWorldCellAltitude());
            if (part == null) {
                continue;
            }
            final int partId = part.getPartId();
            if ((partId == 3 || partId == 1) && worstPartId != 0) {
                worstPartId = partId;
            }
            if (partId != 0) {
                continue;
            }
            worstPartId = partId;
        }
        return worstPartId;
    }
    
    private static int computeTackleValue(final List<? extends BasicCharacterInfo> inRangeTacklers) {
        int tackleValue = 0;
        for (int i = 0, n = inRangeTacklers.size(); i < n; ++i) {
            final BasicCharacterInfo tackler = (BasicCharacterInfo)inRangeTacklers.get(i);
            tackleValue += tackler.getTackleValue() / (i + 1);
        }
        return tackleValue;
    }
    
    private List<? extends BasicCharacterInfo> getSortedInRangeTacklers(final Collection<? extends BasicCharacterInfo> potentialTacklers, final Point3 moverPos) {
        final List<? extends BasicCharacterInfo> inRangeTacklers = this.computeInRangeTacklers(potentialTacklers, moverPos);
        decreasingSort(inRangeTacklers);
        return inRangeTacklers;
    }
    
    private List<? extends BasicCharacterInfo> computeInRangeTacklers(final Collection<? extends BasicCharacterInfo> potentialTacklers, final Point3 moverPos) {
        final List<? extends BasicCharacterInfo> inRangeTacklers = new ArrayList<BasicCharacterInfo>(potentialTacklers);
        final Iterator<? extends BasicCharacterInfo> it = inRangeTacklers.iterator();
        while (it.hasNext()) {
            final BasicCharacterInfo next = (BasicCharacterInfo)it.next();
            if (TackleUtils.moverIsNotInTacklerRange(next, this.m_mover, moverPos) || !next.hasCharacteristic(FighterCharacteristicType.TACKLE)) {
                it.remove();
            }
        }
        return inRangeTacklers;
    }
    
    private static void decreasingSort(final List<? extends BasicCharacterInfo> inRangeTacklers) {
        Collections.sort(inRangeTacklers, new Comparator<Object>() {
            @Override
            public int compare(final Object o1, final Object o2) {
                final int tackle1 = ((TackleUser)o1).getTackleValue();
                final int tackle2 = ((TackleUser)o2).getTackleValue();
                return Integer.valueOf(tackle2).compareTo(Integer.valueOf(tackle1));
            }
        });
    }
}
