package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.gem.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public final class MagicraftLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public String getName() {
        return "";
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.execute();
        clientInstance.fireContentInitializerDone(this);
    }
    
    public void execute() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new MagiCraftLootListBinaryData(), new LoadProcedure<MagiCraftLootListBinaryData>() {
            @Override
            public void load(final MagiCraftLootListBinaryData data) {
                final GemLootList lootList = new GemLootList();
                final MagiCraftLootListBinaryData.LootEntry[] binaryList = data.getEntries();
                for (int i = 0, length = binaryList.length; i < length; ++i) {
                    final MagiCraftLootListBinaryData.LootEntry info = binaryList[i];
                    if (MetaItemManager.INSTANCE.get(info.getItemId()) == null) {
                        MagicraftLoader.m_logger.error((Object)("Item a looter inconnu : " + info.getItemId() + " sur la lootList de forgemagie."));
                    }
                    else {
                        lootList.addLoot(new GemLoot(info.getItemId()));
                    }
                }
                GemsDefinitionManager.INSTANCE.setMetaGemsList(lootList, data.getGemType());
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)MagicraftLoader.class);
    }
}
