package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.appearance.*;

public class CraftItemPopup implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public CraftItemPopup() {
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
        checkOut.setStyle("popup");
        checkOut.onAttributesInitialized();
        final BorderLayoutData checkOut2 = BorderLayoutData.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setData(BorderLayoutData.Values.CENTER);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final RowLayout checkOut3 = RowLayout.checkOut();
        checkOut3.setHorizontal(false);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final Label element = new Label();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setStyle("whiteSubTitle");
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        final DecoratorAppearance appearance = element.getAppearance();
        appearance.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance).setAlign(Alignment9.NORTH_WEST);
        element.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        appearance.onChildrenAdded();
        final PropertyElement checkOut4 = PropertyElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setName("itemPopupDetail");
        checkOut4.setAttribute("text");
        checkOut4.setField("name");
        element.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        element.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
