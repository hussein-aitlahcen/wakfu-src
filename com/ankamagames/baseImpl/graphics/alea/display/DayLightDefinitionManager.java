package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class DayLightDefinitionManager
{
    private final SortedList<DayLightDefinition> m_definitions;
    
    public DayLightDefinitionManager() {
        super();
        this.m_definitions = new SortedList<DayLightDefinition>(DayLightDefinition.COMPARATOR);
    }
    
    public void addDefinition(final byte dayPercentage, final float red, final float green, final float blue) {
        this.m_definitions.add(new DayLightDefinition(dayPercentage, red, green, blue));
    }
    
    public DayLightDefinition get(final int dayPercentage) {
        DayLightDefinition previousDef = this.m_definitions.getLast();
        for (int i = 0, size = this.m_definitions.size(); i < size; ++i) {
            final DayLightDefinition definition = this.m_definitions.get(i);
            if (dayPercentage < definition.m_dayPercentage) {
                return previousDef;
            }
            previousDef = definition;
        }
        return this.m_definitions.getLast();
    }
    
    public DayLightDefinition getNext(final DayLightDefinition definition) {
        final int idx = this.m_definitions.indexOf(definition);
        return (idx < this.m_definitions.size() - 1) ? this.m_definitions.get(idx + 1) : this.m_definitions.getFirst();
    }
    
    public static class DayLightDefinition implements Comparable
    {
        public static Comparator<DayLightDefinition> COMPARATOR;
        private final byte m_dayPercentage;
        private final float m_red;
        private final float m_green;
        private final float m_blue;
        
        public DayLightDefinition(final byte dayPercentage, final float red, final float green, final float blue) {
            super();
            this.m_dayPercentage = dayPercentage;
            this.m_red = red;
            this.m_green = green;
            this.m_blue = blue;
        }
        
        @Override
        public int compareTo(final Object o) {
            final DayLightDefinition d = (DayLightDefinition)o;
            return this.m_dayPercentage - d.m_dayPercentage;
        }
        
        public float getRed() {
            return this.m_red;
        }
        
        public float getGreen() {
            return this.m_green;
        }
        
        public float getBlue() {
            return this.m_blue;
        }
        
        public byte getDayPercentage() {
            return this.m_dayPercentage;
        }
        
        @Override
        public String toString() {
            return "DayLightDefinition{" + this.m_dayPercentage + "%Day" + " Color = {" + this.m_red + ", " + this.m_green + ", " + this.m_blue + '}';
        }
        
        static {
            DayLightDefinition.COMPARATOR = new Comparator<DayLightDefinition>() {
                @Override
                public int compare(final DayLightDefinition o1, final DayLightDefinition o2) {
                    return o1.compareTo(o2);
                }
            };
        }
    }
}
