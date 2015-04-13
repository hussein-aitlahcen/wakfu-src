package com.ankamagames.wakfu.common.game.group.party;

import com.ankamagames.wakfu.common.game.group.member.*;
import gnu.trove.*;
import java.util.*;

public interface PartyModelInterface
{
    long getLeaderId();
    
    void setLeaderId(long p0);
    
    void addMember(PartyMemberInterface p0);
    
    void addListener(PartyModelListener p0);
    
    boolean removeMember(long p0);
    
    void removeAllMembers();
    
    long getId();
    
    PartyMemberInterface getMember(long p0);
    
    boolean contains(long p0);
    
    TLongObjectHashMap<PartyMemberInterface> getMembers();
    
    boolean contains(String p0);
    
    boolean isLeader(PartyMemberInterface p0);
    
    boolean removeMember(PartyMemberInterface p0, boolean p1);
    
    boolean canRemove(PartyMemberInterface p0);
    
    TLongObjectHashMap<PartyMemberInterface> getMembersOfType(int p0);
    
    List<PartyMemberInterface> getCompanions(PartyMemberInterface p0);
    
    List<PartyMemberInterface> getCompanions(long p0);
    
    int getRealPlayerCount();
    
    boolean isEmpty();
    
    boolean isFull();
}
