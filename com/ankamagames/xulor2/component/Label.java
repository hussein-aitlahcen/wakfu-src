package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.text.builder.*;
import com.ankamagames.xulor2.component.text.document.*;

public class Label extends TextWidget
{
    public static final String TAG = "Label";
    
    @Override
    public String getTag() {
        return "Label";
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final TextWidgetAppearance app = TextWidgetAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        this.setTextBuilder(new TextBuilder(new SinglePartTextDocument()));
        this.getTextBuilder().setTextWidget(this);
        this.setMultiline(false);
    }
}
