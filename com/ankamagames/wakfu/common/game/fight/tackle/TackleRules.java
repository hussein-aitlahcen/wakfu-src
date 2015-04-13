package com.ankamagames.wakfu.common.game.fight.tackle;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.common.configuration.*;

public final class TackleRules
{
    private final NewTackleComputer m_newTackleComputer;
    private final OldTackleComputer m_oldTackleComputer;
    
    public TackleRules() {
        super();
        this.m_newTackleComputer = new NewTackleComputer();
        this.m_oldTackleComputer = new OldTackleComputer();
    }
    
    public TackleResult getTackleResult(final BasicCharacterInfo mover, final Collection<? extends BasicCharacterInfo> potentialTacklers, final Point3 currentCell) {
        if (mover == null) {
            return TackleResult.NO_TRY;
        }
        if (mover.isCarried() || mover.isOffPlay() || mover.isOutOfPlay() || mover.isActiveProperty(FightPropertyType.ESCAPE_TACKLE)) {
            return TackleResult.NO_TRY;
        }
        if (potentialTacklers.isEmpty()) {
            return TackleResult.NO_TRY;
        }
        TackleResult tackleResult;
        if (fightReworkEnabled()) {
            this.m_newTackleComputer.setMover(mover);
            tackleResult = this.m_newTackleComputer.getTackleResult(potentialTacklers, currentCell);
        }
        else {
            this.m_oldTackleComputer.setMover(mover);
            tackleResult = this.m_oldTackleComputer.getTackleResult(potentialTacklers, currentCell);
            tackleResult.setTacklers(Collections.singletonList(this.m_oldTackleComputer.getTackler()));
        }
        return tackleResult;
    }
    
    private static boolean fightReworkEnabled() {
        return SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.FIGHT_REWORK_ENABLED);
    }
}
