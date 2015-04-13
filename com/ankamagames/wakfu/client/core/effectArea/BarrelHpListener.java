package com.ankamagames.wakfu.client.core.effectArea;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.framework.fileFormat.properties.*;
import java.io.*;

final class BarrelHpListener implements CharacteristicUpdateListener
{
    private static final Logger m_logger;
    private final BarrelEffectArea m_barrel;
    private int m_previousValue;
    
    BarrelHpListener(final BarrelEffectArea barrel) {
        super();
        this.m_barrel = barrel;
        this.m_previousValue = this.m_barrel.getCharacteristicValue(AbstractBarrelEffectArea.getHpCharac());
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        final int value = charac.value();
        if (this.m_barrel.isCarried()) {
            this.updateCarriedBarrel(value);
        }
        else {
            this.updateNotCarriedBarrel(value);
        }
        this.m_previousValue = value;
    }
    
    private void updateCarriedBarrel(final int value) {
        final Carrier carrier = this.m_barrel.getCarrier();
        if (carrier == null) {
            return;
        }
        final Mobile carriedMobile = ((PlayerCharacter)carrier).getActor().getCarriedMobile();
        this.updateAnimatedElement(value, carriedMobile, 1045, 1045);
    }
    
    private void updateNotCarriedBarrel(final int value) {
        final AnimatedInteractiveElement animatedElement = this.m_barrel.getGraphicalArea().getAnimatedElement();
        this.updateAnimatedElement(value, animatedElement, 10022, 10022);
    }
    
    private void updateAnimatedElement(final int value, final AnimatedInteractiveElement animatedElement, final int normalVisualId, final int brokenVisualID) {
        if (animatedElement == null) {
            return;
        }
        try {
            String gfxFile = WakfuConfiguration.getInstance().getString("ANMInteractiveElementPath");
            if (this.isGoingToBreak(value)) {
                gfxFile = String.format(gfxFile, brokenVisualID);
            }
            else {
                if (!this.isGoingBackToNormal(value)) {
                    return;
                }
                gfxFile = String.format(gfxFile, normalVisualId);
            }
            animatedElement.setGfxId(gfxFile);
            animatedElement.load(gfxFile, true);
            ((Mobile)animatedElement).setCustomColor(1, BreedColorsManager.getInstance().getSkinColor((PlayerCharacter)this.m_barrel.getOwner()).getCustomColor());
        }
        catch (PropertyException e) {
            BarrelHpListener.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        catch (IOException e2) {
            BarrelHpListener.m_logger.error((Object)"Exception levee", (Throwable)e2);
        }
    }
    
    private boolean isGoingBackToNormal(final int value) {
        return value > 1 && this.m_previousValue == 1;
    }
    
    private boolean isGoingToBreak(final int value) {
        return value == 1 && this.m_previousValue > 1;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BarrelHpListener.class);
    }
}
