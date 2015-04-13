package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.appearance.*;

public class SessionTimerDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public SessionTimerDialog() {
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
        checkOut.setNonBlocking(true);
        checkOut.setStyle("popup");
        checkOut.onAttributesInitialized();
        final DecoratorAppearance appearance = checkOut.getAppearance();
        appearance.setElementMap(elementMap);
        appearance.setModulationColor(new Color(1.0f, 1.0f, 1.0f, 0.6f));
        checkOut.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        appearance.onChildrenAdded();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.NORTH_EAST);
        element.setYOffset(-5);
        element.setXOffset(-50);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final RowLayout checkOut2 = RowLayout.checkOut();
        checkOut2.setHorizontal(false);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final Label element2 = new Label();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setStyle("popupInformation");
        element2.setText("%timedSession.remaining%".replace("%timedSession.remaining%", Xulor.getInstance().getTranslatedString("timedSession.remaining")));
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id = "text";
        final Label label = new Label();
        label.onCheckOut();
        label.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, label);
        }
        label.setNonBlocking(true);
        label.setStyle("whitebold");
        checkOut.addBasicElement(label);
        label.onAttributesInitialized();
        final StaticLayoutData element3 = new StaticLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setAlign(Alignment17.CENTER);
        label.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final DecoratorAppearance appearance2 = label.getAppearance();
        appearance2.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance2).setAlignment(Alignment9.CENTER);
        label.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        appearance2.onChildrenAdded();
        final PropertyElement checkOut3 = PropertyElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setAttribute("text");
        checkOut3.setName("sessionTimer");
        checkOut3.setField("remainingTimeField");
        label.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        label.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
