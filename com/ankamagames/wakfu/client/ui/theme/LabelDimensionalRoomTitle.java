package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.appearance.*;

public class LabelDimensionalRoomTitle implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public LabelDimensionalRoomTitle() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public void applyStyle(final ElementMap item, final DocumentParser doc, final Widget widget) {
        this.doc = doc;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final Margin checkOut = Margin.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setInsets(new Insets(0, 5, 0, 0));
        widget.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        final DecoratorAppearance appearance = widget.getAppearance();
        appearance.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance).setAlignment(Alignment9.CENTER);
        appearance.setState("default");
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final FontElement checkOut2 = FontElement.checkOut();
        checkOut2.setRenderer(Xulor.getInstance().getDocumentParser().getFont("styleBoldSmallTitleFont"));
        checkOut2.setElementMap(elementMap);
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final String id = "defaultDarkColor";
        final ColorElement checkOut3 = ColorElement.checkOut();
        checkOut3.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut3);
        }
        checkOut3.setColor(new Color(0.29f, 0.17f, 0.07f, 1.0f));
        appearance.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final String id2 = "characterBookLabelBackground";
        final PixmapBackground checkOut4 = PixmapBackground.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut4);
        }
        checkOut4.setScaled(true);
        appearance.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setHeight(15);
        checkOut5.setPosition(Alignment17.CENTER);
        checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut5.setWidth(208);
        checkOut5.setX(465);
        checkOut5.setY(162);
        checkOut4.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        checkOut4.onChildrenAdded();
        final String id3 = "characterBookLabelBorder";
        final PixmapBorder pixmapBorder = new PixmapBorder();
        pixmapBorder.onCheckOut();
        pixmapBorder.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, pixmapBorder);
        }
        appearance.addBasicElement(pixmapBorder);
        pixmapBorder.onAttributesInitialized();
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setHeight(4);
        checkOut6.setPosition(Alignment17.NORTH_WEST);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(4);
        checkOut6.setX(592);
        checkOut6.setY(262);
        pixmapBorder.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setHeight(4);
        checkOut7.setPosition(Alignment17.NORTH);
        checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut7.setWidth(208);
        checkOut7.setX(117);
        checkOut7.setY(133);
        pixmapBorder.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final PixmapElement checkOut8 = PixmapElement.checkOut();
        checkOut8.setElementMap(elementMap);
        checkOut8.setHeight(4);
        checkOut8.setPosition(Alignment17.NORTH_EAST);
        checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut8.setWidth(4);
        checkOut8.setX(834);
        checkOut8.setY(211);
        pixmapBorder.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        final PixmapElement checkOut9 = PixmapElement.checkOut();
        checkOut9.setElementMap(elementMap);
        checkOut9.setHeight(15);
        checkOut9.setPosition(Alignment17.EAST);
        checkOut9.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut9.setWidth(4);
        checkOut9.setX(1017);
        checkOut9.setY(141);
        pixmapBorder.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        final PixmapElement checkOut10 = PixmapElement.checkOut();
        checkOut10.setElementMap(elementMap);
        checkOut10.setHeight(4);
        checkOut10.setPosition(Alignment17.SOUTH_EAST);
        checkOut10.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut10.setWidth(4);
        checkOut10.setX(508);
        checkOut10.setY(212);
        pixmapBorder.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        final PixmapElement checkOut11 = PixmapElement.checkOut();
        checkOut11.setElementMap(elementMap);
        checkOut11.setHeight(4);
        checkOut11.setPosition(Alignment17.SOUTH);
        checkOut11.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut11.setWidth(204);
        checkOut11.setX(465);
        checkOut11.setY(133);
        pixmapBorder.addBasicElement(checkOut11);
        checkOut11.onAttributesInitialized();
        checkOut11.onChildrenAdded();
        final PixmapElement checkOut12 = PixmapElement.checkOut();
        checkOut12.setElementMap(elementMap);
        checkOut12.setHeight(4);
        checkOut12.setPosition(Alignment17.SOUTH_WEST);
        checkOut12.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut12.setWidth(4);
        checkOut12.setX(939);
        checkOut12.setY(205);
        pixmapBorder.addBasicElement(checkOut12);
        checkOut12.onAttributesInitialized();
        checkOut12.onChildrenAdded();
        final PixmapElement checkOut13 = PixmapElement.checkOut();
        checkOut13.setElementMap(elementMap);
        checkOut13.setHeight(15);
        checkOut13.setPosition(Alignment17.WEST);
        checkOut13.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut13.setWidth(4);
        checkOut13.setX(823);
        checkOut13.setY(140);
        pixmapBorder.addBasicElement(checkOut13);
        checkOut13.onAttributesInitialized();
        checkOut13.onChildrenAdded();
        pixmapBorder.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
