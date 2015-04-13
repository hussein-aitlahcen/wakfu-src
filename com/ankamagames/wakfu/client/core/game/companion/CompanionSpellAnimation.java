package com.ankamagames.wakfu.client.core.game.companion;

import java.util.*;

public class CompanionSpellAnimation
{
    private final ArrayList<String> m_animations;
    private final int m_apsCaster;
    private final long m_apsDelayCaster;
    
    public CompanionSpellAnimation(final ArrayList<String> animations, final int apsCaster, final long apsDelayCaster) {
        super();
        this.m_animations = animations;
        this.m_apsCaster = apsCaster;
        this.m_apsDelayCaster = apsDelayCaster;
    }
    
    public ArrayList<String> getAnimations() {
        return this.m_animations;
    }
    
    public int getApsCaster() {
        return this.m_apsCaster;
    }
    
    public long getApsDelayCaster() {
        return this.m_apsDelayCaster;
    }
}
