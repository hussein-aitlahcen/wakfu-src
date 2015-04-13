package com.ankamagames.baseImpl.graphics.alea.display;

import java.util.*;

public abstract class DisplayedScreenElementParameteredComparator implements Comparator<DisplayedScreenElement>
{
    private Parameters m_parameters;
    
    public Parameters getParameters() {
        return this.m_parameters;
    }
    
    public void setParameters(final Parameters parameters) {
        this.m_parameters = parameters;
    }
    
    public static class Parameters
    {
        public final AleaWorldScene m_scene;
        public final float m_mouseX;
        public final float m_mouseY;
        public final float m_altitude;
        
        public Parameters(final AleaWorldScene scene, final float altitude, final float mouseX, final float mouseY) {
            super();
            this.m_scene = scene;
            this.m_altitude = altitude;
            this.m_mouseX = mouseX;
            this.m_mouseY = mouseY;
        }
    }
}
