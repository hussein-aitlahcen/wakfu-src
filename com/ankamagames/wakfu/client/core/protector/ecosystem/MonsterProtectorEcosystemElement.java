package com.ankamagames.wakfu.client.core.protector.ecosystem;

import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;

public class MonsterProtectorEcosystemElement extends ProtectorEcosystemElement
{
    public MonsterProtectorEcosystemElement(final int familyId, final int protectPrice, final int reintroducePrice) {
        super(familyId, MonsterFamilyManager.getInstance().getMonsterFamily(familyId).getParentFamilyId(), protectPrice, reintroducePrice);
    }
    
    @Override
    protected String getName() {
        return WakfuTranslator.getInstance().getString(38, this.m_familyId, new Object[0]);
    }
    
    @Override
    protected boolean isProtected() {
        return ProtectorView.getInstance().getProtector().getEcosystemHandler().isProtectedMonsterFamily(this.m_familyId);
    }
    
    @Override
    protected boolean canBeReintroduced() {
        return true;
    }
    
    @Override
    protected String getIconUrl() {
        if (this.isExtinct()) {
            return GroupDifficultyColor.HARD.getIconURL(this.m_parentFamilyId);
        }
        if (this.isEndangered()) {
            return GroupDifficultyColor.NORMAL.getIconURL(this.m_parentFamilyId);
        }
        return GroupDifficultyColor.EASY.getIconURL(this.m_parentFamilyId);
    }
    
    @Override
    protected boolean isEndangered() {
        final WakfuEcosystemFamilyInfo info = WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(this.m_familyId);
        return info.getCurrentRatio() < 0.2f;
    }
    
    @Override
    protected boolean isExtinct() {
        final WakfuEcosystemFamilyInfo info = WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(this.m_familyId);
        return info.getCurrentRatio() < 0.05f && !info.isReintroducing();
    }
    
    @Override
    public boolean isMonster() {
        return true;
    }
    
    @Override
    public String getResourceTypeSeedName() {
        return WakfuTranslator.getInstance().getString(100, ResourceType.MOB.getAgtId(), new Object[0]);
    }
}
