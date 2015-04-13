package com.ankamagames.wakfu.common.game.fight.tackle;

import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public class TackleResult
{
    public static TackleResult TACKLED;
    public static TackleResult DODGED;
    public static TackleResult NO_TRY;
    private int m_apLoss;
    private int m_mpLoss;
    private List<? extends BasicCharacterInfo> m_tacklers;
    
    public void setApLoss(final int apLoss) {
        this.m_apLoss = apLoss;
    }
    
    public void setMpLoss(final int mpLoss) {
        this.m_mpLoss = mpLoss;
    }
    
    public void setTacklers(final List<? extends BasicCharacterInfo> tacklers) {
        if (tacklers == null) {
            this.m_tacklers = Collections.emptyList();
        }
        else {
            this.m_tacklers = new ArrayList<BasicCharacterInfo>(tacklers);
        }
    }
    
    public int getApLoss() {
        return this.m_apLoss;
    }
    
    public int getMpLoss() {
        return this.m_mpLoss;
    }
    
    public List<? extends BasicCharacterInfo> getTacklers() {
        if (this.m_tacklers == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.m_tacklers);
    }
    
    @Override
    public String toString() {
        return "TackleResult{m_apLoss=" + this.m_apLoss + ", m_mpLoss=" + this.m_mpLoss + ", m_tacklers=" + this.m_tacklers + '}';
    }
    
    public boolean isDodged() {
        return this != TackleResult.NO_TRY && this != TackleResult.TACKLED && (this == TackleResult.DODGED || (this.m_apLoss == 0 && this.m_mpLoss == 0));
    }
    
    public boolean isTackled() {
        return this != TackleResult.NO_TRY && this != TackleResult.DODGED && (this == TackleResult.TACKLED || this.m_apLoss != 0 || this.m_mpLoss != 0);
    }
    
    static {
        TackleResult.TACKLED = new TackleResult();
        TackleResult.DODGED = new TackleResult();
        TackleResult.NO_TRY = new TackleResult();
    }
}
