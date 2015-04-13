package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.*;

public class ProtectorEcosystemProtectionLoader implements ContentInitializer
{
    protected static Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.loadFromStorage();
        clientInstance.fireContentInitializerDone(this);
    }
    
    public void loadFromStorage() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ProtectorEcosystemProtectionBinaryData(), new LoadProcedure<ProtectorEcosystemProtectionBinaryData>() {
            @Override
            public void load(final ProtectorEcosystemProtectionBinaryData ecosystemBs) {
                final ProtectorEcosystemProtectionDefinition definition = new ProtectorEcosystemProtectionDefinition();
                ProtectorEcosystemProtectionLoader.this.loadFauna(ecosystemBs, definition);
                ProtectorEcosystemProtectionLoader.this.loadFlora(ecosystemBs, definition);
                ProtectorEcosystemProtectionManager.INSTANCE.add(ecosystemBs.getProtectorId(), definition);
            }
        });
    }
    
    private void loadFlora(final ProtectorEcosystemProtectionBinaryData ecosystemBs, final ProtectorEcosystemProtectionDefinition definition) {
        for (final ProtectorEcosystemProtectionBinaryData.ProtectorFloraProtection flora : ecosystemBs.getFloraProtection()) {
            definition.addResourceProtectionDefinition(flora.getResourceFamilyId(), flora.getProtectionCost(), flora.getReintroductionCost(), flora.getReintroductionItemId(), flora.getReintroductionItemQty());
        }
    }
    
    private void loadFauna(final ProtectorEcosystemProtectionBinaryData ecosystemBs, final ProtectorEcosystemProtectionDefinition definition) {
        for (final ProtectorEcosystemProtectionBinaryData.ProtectorFaunaProtection fauna : ecosystemBs.getFaunaProtection()) {
            definition.addMonsterProtectionDefinition(fauna.getMonsterFamilyId(), fauna.getProtectionCost(), fauna.getReintroductionCost(), fauna.getReintroductionItemId(), fauna.getReintroductionItemQty());
        }
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.protectorEcosystem");
    }
    
    static {
        ProtectorEcosystemProtectionLoader.m_logger = Logger.getLogger((Class)ProtectorEcosystemProtectionLoader.class);
    }
}
