package com.ankamagames.wakfu.client.ui.protocol.message.dimensionalBag;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class UIDimensionalBagSelectAppearanceMessage extends UIMessage
{
    private UIDimensionalBagAppearanceManagerFrame.DimensionalBagCustomView m_view;
    
    public UIDimensionalBagSelectAppearanceMessage(final UIDimensionalBagAppearanceManagerFrame.DimensionalBagCustomView view) {
        super();
        this.m_view = view;
    }
    
    public UIDimensionalBagAppearanceManagerFrame.DimensionalBagCustomView getView() {
        return this.m_view;
    }
}
