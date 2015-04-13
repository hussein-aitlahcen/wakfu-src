package com.ankamagames.wakfu.common.game.fight.tackle;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import java.util.*;
import org.jetbrains.annotations.*;

public final class OldTackleComputer
{
    private BasicCharacterInfo m_tackler;
    private final TackleComputer m_tackleComputer;
    
    public OldTackleComputer() {
        super();
        this.m_tackleComputer = new TackleComputer();
    }
    
    public TackleResult getTackleResult(final Collection<? extends BasicCharacterInfo> potentialTacklers, final Point3 moverPos) {
        TackleResult result = TackleResult.NO_TRY;
        final Iterator<? extends BasicCharacterInfo> iterator = potentialTacklers.iterator();
        while (iterator.hasNext()) {
            this.m_tackler = (BasicCharacterInfo)iterator.next();
            this.m_tackleComputer.setTackler(this.m_tackler);
            this.m_tackleComputer.setMoverPos(moverPos);
            this.m_tackleComputer.testTackle();
            if (this.m_tackleComputer.isTackled()) {
                return TackleResult.TACKLED;
            }
            if (!this.m_tackleComputer.isDodge()) {
                continue;
            }
            result = TackleResult.DODGED;
        }
        return result;
    }
    
    public void setMover(@NotNull final TackleUser mover) {
        this.m_tackleComputer.setMover(mover);
    }
    
    public BasicCharacterInfo getTackler() {
        return this.m_tackler;
    }
}
