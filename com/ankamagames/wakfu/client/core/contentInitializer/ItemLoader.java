package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;

public class ItemLoader implements ContentInitializer
{
    private static final ItemLoader m_instance;
    
    public static ItemLoader getInstance() {
        return ItemLoader.m_instance;
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ItemTypeBinaryData(), new LoadProcedure<ItemTypeBinaryData>() {
            @Override
            public void load(final ItemTypeBinaryData data) {
                ItemTypeManager.getInstance().addItemTypeFromBinaryForm(data);
            }
        });
        BinaryDocumentManager.getInstance().foreach(new BagsBinaryData(), new LoadProcedure<BagsBinaryData>() {
            @Override
            public void load(final BagsBinaryData data) {
                BagStoringManager.INSTANCE.put(data.getId(), data.getValidItemCategories(), data.getCapacity());
            }
        });
        ItemTypeManager.getInstance().updateItemTypeList();
        ItemTypeManager.getInstance().updateItemTypesImbueTables();
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.item");
    }
    
    static {
        m_instance = new ItemLoader();
    }
}
