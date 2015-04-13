package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IEHavenWorldBoardParameter extends IEParameter
{
    public static final IEHavenWorldBoardParameter FAKE_PARAM;
    private final int m_havenWorldDefinitionId;
    private short m_miniOriginX;
    private short m_miniOriginY;
    private short m_miniOriginZ;
    
    public IEHavenWorldBoardParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorId, final short havenWorldDefinitionId) {
        super(paramId, visualId, chaosCategory, chaosCollectorId);
        this.m_havenWorldDefinitionId = havenWorldDefinitionId;
    }
    
    public int getHavenWorldDefinitionId() {
        return this.m_havenWorldDefinitionId;
    }
    
    public void setMiniOriginCell(final short cellX, final short cellY, final short cellZ) {
        this.m_miniOriginX = cellX;
        this.m_miniOriginY = cellY;
        this.m_miniOriginZ = cellZ;
    }
    
    public short getMiniOriginX() {
        return this.m_miniOriginX;
    }
    
    public short getMiniOriginY() {
        return this.m_miniOriginY;
    }
    
    public short getMiniOriginZ() {
        return this.m_miniOriginZ;
    }
    
    @Override
    public String toString() {
        return "IEHavenWorldBoardParameter{m_havenWorldDefinitionId=" + this.m_havenWorldDefinitionId + '}';
    }
    
    static {
        FAKE_PARAM = new FakeParam();
    }
    
    private static class FakeParam extends IEHavenWorldBoardParameter
    {
        FakeParam() {
            super(0, 0, ChaosInteractiveCategory.NO_CHAOS, 0, (short)0);
        }
    }
}
