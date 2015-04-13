package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

import com.ankamagames.framework.external.*;
import java.util.*;

public enum InteractiveElementActivationPattern implements ExportableEnum
{
    NONE((short)0, (byte)0), 
    FRONT((short)1, (byte)0), 
    BACK((short)2, (byte)4), 
    LEFT((short)4, (byte)6), 
    RIGHT((short)8, (byte)2), 
    FRONT_RIGHT((short)16, (byte)1), 
    FRONT_LEFT((short)32, (byte)7), 
    BACK_RIGHT((short)64, (byte)3), 
    BACK_LEFT((short)128, (byte)5), 
    FOUR_DIRECTION_NORMAL((short)15, (byte)0), 
    FOUR_DIRECTION_DIAGONAL((short)240, (byte)0), 
    CENTER((short)256, (byte)0);
    
    private final short m_patternId;
    private final byte m_positionOffset;
    
    private InteractiveElementActivationPattern(final short patternId, final byte positionOffset) {
        this.m_patternId = patternId;
        this.m_positionOffset = positionOffset;
    }
    
    public static InteractiveElementActivationPattern valueOf(final short patternId) {
        for (final InteractiveElementActivationPattern pattern : values()) {
            if (pattern.getPatternId() == patternId) {
                return pattern;
            }
        }
        return null;
    }
    
    public short getPatternId() {
        return this.m_patternId;
    }
    
    public byte getPositionOffset() {
        return this.m_positionOffset;
    }
    
    @Override
    public String getEnumId() {
        return Short.toString(this.m_patternId);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static ArrayList<InteractiveElementActivationPattern> getPatterns(final short patternId) {
        final ArrayList<InteractiveElementActivationPattern> patterns = new ArrayList<InteractiveElementActivationPattern>();
        for (final InteractiveElementActivationPattern pattern : values()) {
            if (pattern != InteractiveElementActivationPattern.FOUR_DIRECTION_NORMAL && pattern != InteractiveElementActivationPattern.FOUR_DIRECTION_DIAGONAL) {
                if (pattern != InteractiveElementActivationPattern.NONE) {
                    if ((pattern.getPatternId() & patternId) == pattern.getPatternId()) {
                        patterns.add(pattern);
                    }
                }
            }
        }
        return patterns;
    }
}
