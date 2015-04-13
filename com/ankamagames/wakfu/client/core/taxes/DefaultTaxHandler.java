package com.ankamagames.wakfu.client.core.taxes;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.core.protector.*;

public class DefaultTaxHandler implements TaxHandler
{
    private static final Logger m_logger;
    public static final DefaultTaxHandler INSTANCE;
    
    @Override
    public float getTaxValue(final TaxContext context) {
        return context.initialValue;
    }
    
    @Override
    public void setTaxes(final TaxContext context, final float value) {
    }
    
    @Override
    public int getTaxAmount(final MerchantClient taxSource, final TaxContext taxContext, final int kamasCount) {
        return MathHelper.fastRound(kamasCount * this.getTaxValue(taxContext));
    }
    
    @Override
    public void checkInTax(final TaxContext context, final int taxAmount) {
        DefaultTaxHandler.m_logger.info((Object)("[TAX] R\u00e9cup\u00e9ration de " + taxAmount + " kamas pour le context " + context));
    }
    
    @Override
    public boolean mustTax(final MerchantClient taxSource) {
        if (!(taxSource instanceof LocalPlayerCharacter)) {
            return false;
        }
        final short instanceId = ((LocalPlayerCharacter)taxSource).getInstanceId();
        return HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(instanceId) == null && ProtectorView.getInstance().getProtector() == null && ProtectorView.getInstance().getProtectorCacheForDimensionalBag() == null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DefaultTaxHandler.class);
        INSTANCE = new DefaultTaxHandler();
    }
}
