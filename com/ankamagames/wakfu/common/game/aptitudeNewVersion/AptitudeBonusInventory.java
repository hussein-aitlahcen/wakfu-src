package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import com.ankamagames.wakfu.common.game.aptitudeNewVersion.listener.*;

public final class AptitudeBonusInventory extends AbstractAptitudeBonusInventory
{
    public AptitudeBonusInventory() {
        super();
        this.addListener(new AptitudeBonusModificationLogger());
    }
    
    public InactiveAptitudeBonusInventory getInactiveCopy() {
        return new InactiveAptitudeBonusInventory(this);
    }
}
