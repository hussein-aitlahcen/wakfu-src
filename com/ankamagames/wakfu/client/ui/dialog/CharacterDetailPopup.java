package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;

public class CharacterDetailPopup implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public CharacterDetailPopup() {
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
        checkOut.setPrefSize(new Dimension(230, 0));
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
        checkOut3.setAlign(Alignment9.NORTH);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final Label element = new Label();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setStyle("whiteBold");
        element.setText("%bonusPenalties.total%".replace("%bonusPenalties.total%", Xulor.getInstance().getTranslatedString("bonusPenalties.total")));
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        final DecoratorAppearance appearance = element.getAppearance();
        appearance.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance).setAlignment(Alignment9.CENTER);
        element.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        appearance.onChildrenAdded();
        element.onChildrenAdded();
        final Container checkOut4 = Container.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setStyle("lineSeparator2");
        checkOut4.setPrefSize(new Dimension(0, 1));
        checkOut.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        final DecoratorAppearance appearance2 = checkOut4.getAppearance();
        appearance2.setElementMap(elementMap);
        checkOut4.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final Margin checkOut5 = Margin.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setInsets(new Insets(5, 0, 0, 0));
        appearance2.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance2.onChildrenAdded();
        checkOut4.onChildrenAdded();
        final TextView element2 = new TextView();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setStyle("white");
        element2.setMinWidth(180);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        final DecoratorAppearance appearance3 = element2.getAppearance();
        appearance3.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance3).setAlign(Alignment9.WEST);
        element2.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final Margin checkOut6 = Margin.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setInsets(new Insets(10, 0, 0, 0));
        appearance3.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        appearance3.onChildrenAdded();
        element2.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
