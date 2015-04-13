package com.ankamagames.wakfu.common.game.group.member;

public interface PlayerMemberModelListener
{
    void onSetLevel(PartyMemberModel p0);
    
    void onSetDead(PartyMemberModel p0);
    
    void onSetInFight(PartyMemberModel p0);
    
    void onSetCurrentHp(PartyMemberModel p0);
    
    void onSetMaxHp(PartyMemberModel p0);
}
