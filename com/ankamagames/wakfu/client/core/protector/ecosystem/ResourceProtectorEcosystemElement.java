package com.ankamagames.wakfu.client.core.protector.ecosystem;

import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;

public class ResourceProtectorEcosystemElement extends ProtectorEcosystemElement
{
    public ResourceProtectorEcosystemElement(final int familyId, final int protectPrice, final int reintroducePrice) {
        super(familyId, familyId, protectPrice, reintroducePrice);
    }
    
    @Override
    protected String getName() {
        return WakfuTranslator.getInstance().getString(37, this.m_familyId, new Object[0]);
    }
    
    @Override
    protected boolean isProtected() {
        return ProtectorView.getInstance().getProtector().getEcosystemHandler().isProtectedResourceFamily(this.m_familyId);
    }
    
    @Override
    protected boolean canBeReintroduced() {
        return true;
    }
    
    @Override
    protected String getIconUrl() {
        return WakfuConfiguration.getInstance().getIconUrl("ecosystemDifficultyIconsPath", "defaultIconPath", this.m_familyId, this.isExtinct() ? 2 : (this.isEndangered() ? 1 : 0));
    }
    
    @Override
    protected boolean isEndangered() {
        final WakfuEcosystemFamilyInfo info = WakfuResourceZoneManager.getInstance().getResourceFamilyInfo(this.m_familyId);
        return info.getCurrentRatio() < 0.2f;
    }
    
    @Override
    protected boolean isExtinct() {
        final WakfuEcosystemFamilyInfo info = WakfuResourceZoneManager.getInstance().getResourceFamilyInfo(this.m_familyId);
        return info.getCurrentRatio() < 0.05f && !info.isReintroducing();
    }
    
    @Override
    public boolean isMonster() {
        return false;
    }
    
    @Override
    public String getResourceTypeSeedName() {
        return WakfuTranslator.getInstance().getString(100, this.m_familyId, new Object[0]);
    }
}
