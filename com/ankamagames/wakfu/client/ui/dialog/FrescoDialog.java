package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;

public class FrescoDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public FrescoDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "mainWindow";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.setNonBlocking(false);
        checkOut.setStyle("backgroundPopup");
        checkOut.onAttributesInitialized();
        final DecoratorAppearance appearance = checkOut.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Padding element = new Padding();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setInsets(new Insets(7, 10, 9, 10));
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        appearance.onChildrenAdded();
        final StaticLayoutData element2 = new StaticLayoutData();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setSize(new Dimension(100.0f, -2));
        element2.setAlign(Alignment17.CENTER);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final RowLayout checkOut2 = RowLayout.checkOut();
        checkOut2.setHorizontal(false);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final String id2 = "closeButton";
        final Button button = new Button();
        button.onCheckOut();
        button.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, button);
        }
        button.setStyle("close");
        button.setExpandable(false);
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("wakfu.fresco:closeDialog");
        button.setOnClick(onClick);
        checkOut.addBasicElement(button);
        button.onAttributesInitialized();
        final RowLayoutData element3 = new RowLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setAlign(Alignment9.EAST);
        button.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        button.onChildrenAdded();
        final Container checkOut3 = Container.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        final StaticLayout element4 = new StaticLayout();
        element4.onCheckOut();
        element4.setAdaptToContentSize(true);
        checkOut3.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        final String id3 = "multipleImage";
        final MultipleImage multipleImage = new MultipleImage();
        multipleImage.onCheckOut();
        multipleImage.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, multipleImage);
        }
        multipleImage.setManualInnerMove(true);
        multipleImage.setShrinkToImageHeight(true);
        checkOut3.addBasicElement(multipleImage);
        multipleImage.onAttributesInitialized();
        final StaticLayoutData element5 = new StaticLayoutData();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setSize(new Dimension(100.0f, 100.0f));
        multipleImage.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        final String id4 = "internalPopup";
        final Container checkOut4 = Container.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut4);
        }
        checkOut4.setStyle("popup");
        multipleImage.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        final String id5 = "internalPopupTextView";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, textView);
        }
        textView.setStyle("white");
        textView.setMinWidth(250);
        checkOut4.addBasicElement(textView);
        textView.onAttributesInitialized();
        textView.onChildrenAdded();
        checkOut4.onChildrenAdded();
        multipleImage.onChildrenAdded();
        final String id6 = "leftArrowContainer";
        final Container checkOut5 = Container.checkOut();
        checkOut5.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut5);
        }
        checkOut5.setStyle("leftArrowParticle");
        checkOut3.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        final StaticLayoutData element6 = new StaticLayoutData();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setSize(new Dimension(50, 50));
        element6.setAlign(Alignment17.WEST);
        checkOut5.addBasicElement(element6);
        element6.onAttributesInitialized();
        element6.onChildrenAdded();
        checkOut5.onChildrenAdded();
        final String id7 = "rightArrowContainer";
        final Container checkOut6 = Container.checkOut();
        checkOut6.setElementMap(elementMap);
        if (elementMap != null && id7 != null) {
            elementMap.add(id7, checkOut6);
        }
        checkOut6.setStyle("rightArrowParticle");
        checkOut3.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        final StaticLayoutData element7 = new StaticLayoutData();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        element7.setSize(new Dimension(50, 50));
        element7.setAlign(Alignment17.EAST);
        checkOut6.addBasicElement(element7);
        element7.onAttributesInitialized();
        element7.onChildrenAdded();
        checkOut6.onChildrenAdded();
        checkOut3.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
