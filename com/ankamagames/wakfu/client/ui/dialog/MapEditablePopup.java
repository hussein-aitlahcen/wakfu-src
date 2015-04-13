package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.renderer.condition.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class MapEditablePopup implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public MapEditablePopup() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "popup";
        final PopupElement element = new PopupElement();
        element.onCheckOut();
        element.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, element);
        }
        element.setAlign(Alignment9.NORTH_EAST);
        element.setHotSpotPosition(Alignment9.SOUTH_WEST);
        element.setHideOnClick(false);
        element.onAttributesInitialized();
        final StaticLayoutData element2 = new StaticLayoutData();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setSize(new Dimension(-2, -2));
        element.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final StaticLayout element3 = new StaticLayout();
        element3.onCheckOut();
        element3.setAdaptToContentSize(true);
        checkOut.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final String id2 = "container";
        final Container checkOut2 = Container.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut2.setStyle("chatBubble");
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final StaticLayoutData element4 = new StaticLayoutData();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setSize(new Dimension(100.0f, 100.0f));
        checkOut2.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        final DecoratorAppearance appearance = checkOut2.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut2.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut3 = Margin.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setInsets(new Insets(0, 0, 15, 0));
        appearance.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final Padding element5 = new Padding();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setInsets(new Insets(10, 15, 10, 15));
        appearance.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        appearance.onChildrenAdded();
        final TextView element6 = new TextView();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setStyle("smallboldMap");
        element6.setMinWidth(1);
        element6.setMaxWidth(200);
        checkOut2.addBasicElement(element6);
        element6.onAttributesInitialized();
        final PropertyElement checkOut4 = PropertyElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setName("mapPopupDescription");
        checkOut4.setAttribute("text");
        element6.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final PropertyElement checkOut5 = PropertyElement.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setName("mapPopupIsEditing");
        checkOut5.setAttribute("visible");
        element6.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        final ConditionResult element7 = new ConditionResult();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        checkOut5.addBasicElement(element7);
        element7.onAttributesInitialized();
        final FalseCondition element8 = new FalseCondition();
        element8.onCheckOut();
        element8.setElementMap(elementMap);
        element7.addBasicElement(element8);
        element8.onAttributesInitialized();
        element8.onChildrenAdded();
        element7.onChildrenAdded();
        checkOut5.onChildrenAdded();
        element6.onChildrenAdded();
        final String id3 = "textEditor";
        final TextEditor textEditor = new TextEditor();
        textEditor.onCheckOut();
        textEditor.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, textEditor);
        }
        textEditor.setStyle("withoutBorder");
        textEditor.setMaxChars(200);
        textEditor.setMinWidth(200);
        textEditor.setMaxWidth(200);
        final KeyTypedListener onKeyType = new KeyTypedListener();
        onKeyType.setCallBackFunc("wakfu.map:onTextEditorChange");
        textEditor.setOnKeyType(onKeyType);
        final KeyPressedListener onKeyPress = new KeyPressedListener();
        onKeyPress.setCallBackFunc("wakfu.map:onTextEditorKeyPress");
        textEditor.setOnKeyPress(onKeyPress);
        textEditor.setFocusable(true);
        textEditor.setSelectOnFocus(true);
        checkOut2.addBasicElement(textEditor);
        textEditor.onAttributesInitialized();
        final PropertyElement checkOut6 = PropertyElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setName("mapPopupDescription");
        checkOut6.setAttribute("text");
        textEditor.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PropertyElement checkOut7 = PropertyElement.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setName("mapPopupIsEditing");
        checkOut7.setAttribute("visible");
        textEditor.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final PropertyElement checkOut8 = PropertyElement.checkOut();
        checkOut8.setElementMap(elementMap);
        checkOut8.setName("mapPopupIsEditing");
        checkOut8.setAttribute("focused");
        textEditor.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        textEditor.onChildrenAdded();
        final String id4 = "valid";
        final Button button = new Button();
        button.onCheckOut();
        button.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, button);
        }
        button.setStyle("smallValid");
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("wakfu.map:applyNote");
        button.setOnClick(onClick);
        checkOut2.addBasicElement(button);
        button.onAttributesInitialized();
        final PropertyElement checkOut9 = PropertyElement.checkOut();
        checkOut9.setElementMap(elementMap);
        checkOut9.setName("mapPopupIsEditing");
        checkOut9.setAttribute("visible");
        button.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        button.onChildrenAdded();
        checkOut2.onChildrenAdded();
        final String id5 = "image";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, image);
        }
        image.setStyle("BubbleArrowLeft");
        image.setNonBlocking(true);
        checkOut.addBasicElement(image);
        image.onAttributesInitialized();
        final StaticLayoutData element9 = new StaticLayoutData();
        element9.onCheckOut();
        element9.setElementMap(elementMap);
        element9.setAlign(Alignment17.SOUTH_WEST);
        element9.setSize(new Dimension(-2, -2));
        image.addBasicElement(element9);
        element9.onAttributesInitialized();
        element9.onChildrenAdded();
        image.onChildrenAdded();
        checkOut.onChildrenAdded();
        element.onChildrenAdded();
        return element;
    }
}
