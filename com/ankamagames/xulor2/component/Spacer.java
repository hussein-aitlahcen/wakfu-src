package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;

public class Spacer extends Widget
{
    public static final String TAG = "spacer";
    
    @Override
    public String getTag() {
        return "spacer";
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
    }
}
