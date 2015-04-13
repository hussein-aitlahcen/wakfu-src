package com.ankamagames.wakfu.common.game.havenWorld.definition;

import com.ankamagames.framework.kernel.core.maths.*;

public class BuildingIEDefinition
{
    private final int m_templateId;
    private final Point3 m_relativePos;
    
    public BuildingIEDefinition(final int templateId, final Point3 relativePos) {
        super();
        this.m_relativePos = new Point3();
        this.m_templateId = templateId;
        this.m_relativePos.set(relativePos);
    }
    
    public int getTemplateId() {
        return this.m_templateId;
    }
    
    public Point3 getRelativePos() {
        return this.m_relativePos;
    }
    
    @Override
    public String toString() {
        return "BuildingIEDefinition{m_templateId=" + this.m_templateId + ", m_relativePos=" + this.m_relativePos + '}';
    }
}
