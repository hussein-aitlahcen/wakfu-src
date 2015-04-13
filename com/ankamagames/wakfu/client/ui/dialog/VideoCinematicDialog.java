package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.appearance.*;

public class VideoCinematicDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public VideoCinematicDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("wakfu.videoCinematic:onClick");
        checkOut.setOnClick(onClick);
        checkOut.setNonBlocking(false);
        checkOut.onAttributesInitialized();
        final StaticLayout element = new StaticLayout();
        element.onCheckOut();
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayoutData element2 = new StaticLayoutData();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setAlign(Alignment17.CENTER);
        element2.setSize(new Dimension(100.0f, 100.0f));
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final DecoratorAppearance appearance = checkOut.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final PlainBackground element3 = new PlainBackground();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        appearance.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        appearance.onChildrenAdded();
        final String id = "video";
        final VideoWidget checkOut2 = VideoWidget.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut2);
        }
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final StaticLayoutData element4 = new StaticLayoutData();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setAlign(Alignment17.CENTER);
        element4.setSize(new Dimension(100.0f, 100.0f));
        checkOut2.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        checkOut2.onChildrenAdded();
        final TextView element5 = new TextView();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setStyle("systemMessage");
        element5.setText("Buffering : 0%");
        element5.setNonBlocking(true);
        checkOut.addBasicElement(element5);
        element5.onAttributesInitialized();
        final StaticLayoutData element6 = new StaticLayoutData();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setSize(new Dimension(-2, -2));
        element6.setAlign(Alignment17.SOUTH);
        element6.setYOffset(50);
        element5.addBasicElement(element6);
        element6.onAttributesInitialized();
        element6.onChildrenAdded();
        final PropertyElement checkOut3 = PropertyElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setName("video");
        checkOut3.setAttribute("text");
        checkOut3.setField("bufferingDesc");
        element5.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final DecoratorAppearance appearance2 = element5.getAppearance();
        appearance2.setElementMap(elementMap);
        element5.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final PropertyElement checkOut4 = PropertyElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setName("video");
        checkOut4.setAttribute("modulationColor");
        checkOut4.setField("modulationColor");
        appearance2.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        appearance2.onChildrenAdded();
        element5.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
