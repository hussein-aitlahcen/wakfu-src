package com.ankamagames.wakfu.common.game.havenWorld.action;

public interface ActionListener
{
    void actionSuccess(HavenWorldAction p0);
    
    void actionFailed(HavenWorldAction p0, HavenWorldError p1);
}
