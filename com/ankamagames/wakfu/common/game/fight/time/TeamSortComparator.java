package com.ankamagames.wakfu.common.game.fight.time;

import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

final class TeamSortComparator implements Comparator<BasicCharacterInfo>
{
    private final int m_fightId;
    private final InitProvider m_initProvider;
    
    TeamSortComparator(final int fightId, final InitProvider initProvider) {
        super();
        this.m_fightId = fightId;
        this.m_initProvider = initProvider;
    }
    
    @Override
    public int compare(final BasicCharacterInfo firstFighter, final BasicCharacterInfo secondFighter) {
        if (this.m_initProvider.getInit(firstFighter.getId()) > this.m_initProvider.getInit(secondFighter.getId())) {
            return -1;
        }
        if (this.m_initProvider.getInit(firstFighter.getId()) < this.m_initProvider.getInit(secondFighter.getId())) {
            return 1;
        }
        if (firstFighter.getLevel() > secondFighter.getLevel()) {
            return -1;
        }
        if (firstFighter.getLevel() < secondFighter.getLevel()) {
            return 1;
        }
        final int pseudoRandomResult = Integer.valueOf(this.getPseudoRandom(firstFighter.getId())).compareTo(Integer.valueOf(this.getPseudoRandom(secondFighter.getId())));
        if (pseudoRandomResult != 0) {
            return pseudoRandomResult;
        }
        return Long.valueOf(firstFighter.getId()).compareTo(Long.valueOf(secondFighter.getId()));
    }
    
    private int getPseudoRandom(final long fighterId) {
        final int i = (int)(fighterId + this.m_fightId + this.m_initProvider.getInit(fighterId) & 0xFFFFL);
        final int sq = i * i;
        return (sq & 0xFF) ^ (sq >> 16 & 0xFF00);
    }
}
