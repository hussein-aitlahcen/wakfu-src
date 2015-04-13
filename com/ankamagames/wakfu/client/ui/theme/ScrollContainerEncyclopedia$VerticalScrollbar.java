package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;

public class ScrollContainerEncyclopedia$VerticalScrollbar implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ScrollContainerEncyclopedia$VerticalScrollbar() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public void applyStyle(final ElementMap item, final DocumentParser doc, final Widget widget) {
        this.doc = doc;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final DecoratorAppearance appearance = widget.getAppearance();
        appearance.setElementMap(elementMap);
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Widget widgetByThemeElementName = appearance.getParentOfType(Widget.class).getWidgetByThemeElementName("verticalDecreaseButton", false);
        if (widgetByThemeElementName != null) {
            final DecoratorAppearance appearance2 = widgetByThemeElementName.getAppearance();
            appearance2.setElementMap(elementMap);
            appearance2.setState("default");
            widgetByThemeElementName.addBasicElement(appearance2);
            appearance2.onAttributesInitialized();
            final String id = "pmSmallDownSideArrow2Default";
            final PixmapElement checkOut = PixmapElement.checkOut();
            checkOut.setElementMap(elementMap);
            if (elementMap != null && id != null) {
                elementMap.add(id, checkOut);
            }
            checkOut.setHeight(16);
            checkOut.setPosition(Alignment17.CENTER);
            checkOut.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut.setWidth(28);
            checkOut.setX(465);
            checkOut.setY(180);
            appearance2.addBasicElement(checkOut);
            checkOut.onAttributesInitialized();
            checkOut.onChildrenAdded();
            appearance2.onChildrenAdded();
            final DecoratorAppearance appearance3 = widgetByThemeElementName.getAppearance();
            appearance3.setElementMap(elementMap);
            appearance3.setState("mouseHover");
            widgetByThemeElementName.addBasicElement(appearance3);
            appearance3.onAttributesInitialized();
            final String id2 = "pmSmallDownSideArrow2Over";
            final PixmapElement checkOut2 = PixmapElement.checkOut();
            checkOut2.setElementMap(elementMap);
            if (elementMap != null && id2 != null) {
                elementMap.add(id2, checkOut2);
            }
            checkOut2.setHeight(16);
            checkOut2.setPosition(Alignment17.CENTER);
            checkOut2.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut2.setWidth(28);
            checkOut2.setX(317);
            checkOut2.setY(199);
            appearance3.addBasicElement(checkOut2);
            checkOut2.onAttributesInitialized();
            checkOut2.onChildrenAdded();
            appearance3.onChildrenAdded();
            final DecoratorAppearance appearance4 = widgetByThemeElementName.getAppearance();
            appearance4.setElementMap(elementMap);
            appearance4.setState("pressed");
            widgetByThemeElementName.addBasicElement(appearance4);
            appearance4.onAttributesInitialized();
            final String id3 = "pmSmallDownSideArrow2Pressed";
            final PixmapElement checkOut3 = PixmapElement.checkOut();
            checkOut3.setElementMap(elementMap);
            if (elementMap != null && id3 != null) {
                elementMap.add(id3, checkOut3);
            }
            checkOut3.setHeight(16);
            checkOut3.setPosition(Alignment17.CENTER);
            checkOut3.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut3.setWidth(28);
            checkOut3.setX(534);
            checkOut3.setY(199);
            appearance4.addBasicElement(checkOut3);
            checkOut3.onAttributesInitialized();
            checkOut3.onChildrenAdded();
            appearance4.onChildrenAdded();
            final DecoratorAppearance appearance5 = widgetByThemeElementName.getAppearance();
            appearance5.setElementMap(elementMap);
            appearance5.setState("disabled");
            widgetByThemeElementName.addBasicElement(appearance5);
            appearance5.onAttributesInitialized();
            final String id4 = "pmSmallDownSideArrow2Disabled";
            final PixmapElement checkOut4 = PixmapElement.checkOut();
            checkOut4.setElementMap(elementMap);
            if (elementMap != null && id4 != null) {
                elementMap.add(id4, checkOut4);
            }
            checkOut4.setHeight(16);
            checkOut4.setPosition(Alignment17.CENTER);
            checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut4.setWidth(28);
            checkOut4.setX(2);
            checkOut4.setY(199);
            appearance5.addBasicElement(checkOut4);
            checkOut4.onAttributesInitialized();
            checkOut4.onChildrenAdded();
            appearance5.onChildrenAdded();
            final DecoratorAppearance appearance6 = widgetByThemeElementName.getAppearance();
            appearance6.setElementMap(elementMap);
            appearance6.setState("selected");
            widgetByThemeElementName.addBasicElement(appearance6);
            appearance6.onAttributesInitialized();
            final String id5 = "pmSmallDownSideArrow2DefaultSelected";
            final PixmapElement checkOut5 = PixmapElement.checkOut();
            checkOut5.setElementMap(elementMap);
            if (elementMap != null && id5 != null) {
                elementMap.add(id5, checkOut5);
            }
            checkOut5.setHeight(16);
            checkOut5.setPosition(Alignment17.CENTER);
            checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut5.setWidth(28);
            checkOut5.setX(465);
            checkOut5.setY(180);
            appearance6.addBasicElement(checkOut5);
            checkOut5.onAttributesInitialized();
            checkOut5.onChildrenAdded();
            appearance6.onChildrenAdded();
            final DecoratorAppearance appearance7 = widgetByThemeElementName.getAppearance();
            appearance7.setElementMap(elementMap);
            appearance7.setState("mouseHoverSelected");
            widgetByThemeElementName.addBasicElement(appearance7);
            appearance7.onAttributesInitialized();
            final String id6 = "pmSmallDownSideArrow2OverSelected";
            final PixmapElement checkOut6 = PixmapElement.checkOut();
            checkOut6.setElementMap(elementMap);
            if (elementMap != null && id6 != null) {
                elementMap.add(id6, checkOut6);
            }
            checkOut6.setHeight(16);
            checkOut6.setPosition(Alignment17.CENTER);
            checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut6.setWidth(28);
            checkOut6.setX(465);
            checkOut6.setY(180);
            appearance7.addBasicElement(checkOut6);
            checkOut6.onAttributesInitialized();
            checkOut6.onChildrenAdded();
            appearance7.onChildrenAdded();
            final DecoratorAppearance appearance8 = widgetByThemeElementName.getAppearance();
            appearance8.setElementMap(elementMap);
            appearance8.setState("pressedSelected");
            widgetByThemeElementName.addBasicElement(appearance8);
            appearance8.onAttributesInitialized();
            final String id7 = "pmSmallDownSideArrow2PressedSelected";
            final PixmapElement checkOut7 = PixmapElement.checkOut();
            checkOut7.setElementMap(elementMap);
            if (elementMap != null && id7 != null) {
                elementMap.add(id7, checkOut7);
            }
            checkOut7.setHeight(16);
            checkOut7.setPosition(Alignment17.CENTER);
            checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut7.setWidth(28);
            checkOut7.setX(465);
            checkOut7.setY(180);
            appearance8.addBasicElement(checkOut7);
            checkOut7.onAttributesInitialized();
            checkOut7.onChildrenAdded();
            appearance8.onChildrenAdded();
            final DecoratorAppearance appearance9 = widgetByThemeElementName.getAppearance();
            appearance9.setElementMap(elementMap);
            appearance9.setState("disabledSelected");
            widgetByThemeElementName.addBasicElement(appearance9);
            appearance9.onAttributesInitialized();
            final String id8 = "pmSmallDownSideArrow2DisabledSelected";
            final PixmapElement checkOut8 = PixmapElement.checkOut();
            checkOut8.setElementMap(elementMap);
            if (elementMap != null && id8 != null) {
                elementMap.add(id8, checkOut8);
            }
            checkOut8.setHeight(16);
            checkOut8.setPosition(Alignment17.CENTER);
            checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut8.setWidth(28);
            checkOut8.setX(984);
            checkOut8.setY(181);
            appearance9.addBasicElement(checkOut8);
            checkOut8.onAttributesInitialized();
            checkOut8.onChildrenAdded();
            appearance9.onChildrenAdded();
        }
        final Widget widgetByThemeElementName2 = appearance.getParentOfType(Widget.class).getWidgetByThemeElementName("verticalIncreaseButton", false);
        if (widgetByThemeElementName2 != null) {
            final DecoratorAppearance appearance10 = widgetByThemeElementName2.getAppearance();
            appearance10.setElementMap(elementMap);
            appearance10.setState("default");
            widgetByThemeElementName2.addBasicElement(appearance10);
            appearance10.onAttributesInitialized();
            final String id9 = "pmSmallUpSideArrow2Default";
            final PixmapElement checkOut9 = PixmapElement.checkOut();
            checkOut9.setElementMap(elementMap);
            if (elementMap != null && id9 != null) {
                elementMap.add(id9, checkOut9);
            }
            checkOut9.setHeight(16);
            checkOut9.setPosition(Alignment17.CENTER);
            checkOut9.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut9.setWidth(28);
            checkOut9.setX(33);
            checkOut9.setY(199);
            appearance10.addBasicElement(checkOut9);
            checkOut9.onAttributesInitialized();
            checkOut9.onChildrenAdded();
            appearance10.onChildrenAdded();
            final DecoratorAppearance appearance11 = widgetByThemeElementName2.getAppearance();
            appearance11.setElementMap(elementMap);
            appearance11.setState("mouseHover");
            widgetByThemeElementName2.addBasicElement(appearance11);
            appearance11.onAttributesInitialized();
            final String id10 = "pmSmallUpSideArrow2Over";
            final PixmapElement checkOut10 = PixmapElement.checkOut();
            checkOut10.setElementMap(elementMap);
            if (elementMap != null && id10 != null) {
                elementMap.add(id10, checkOut10);
            }
            checkOut10.setHeight(16);
            checkOut10.setPosition(Alignment17.CENTER);
            checkOut10.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut10.setWidth(28);
            checkOut10.setX(946);
            checkOut10.setY(199);
            appearance11.addBasicElement(checkOut10);
            checkOut10.onAttributesInitialized();
            checkOut10.onChildrenAdded();
            appearance11.onChildrenAdded();
            final DecoratorAppearance appearance12 = widgetByThemeElementName2.getAppearance();
            appearance12.setElementMap(elementMap);
            appearance12.setState("pressed");
            widgetByThemeElementName2.addBasicElement(appearance12);
            appearance12.onAttributesInitialized();
            final String id11 = "pmSmallUpSideArrow2Pressed";
            final PixmapElement checkOut11 = PixmapElement.checkOut();
            checkOut11.setElementMap(elementMap);
            if (elementMap != null && id11 != null) {
                elementMap.add(id11, checkOut11);
            }
            checkOut11.setHeight(16);
            checkOut11.setPosition(Alignment17.CENTER);
            checkOut11.setTexture(this.doc.getTexture("default_0.tga"));
            checkOut11.setWidth(28);
            checkOut11.setX(378);
            checkOut11.setY(37);
            appearance12.addBasicElement(checkOut11);
            checkOut11.onAttributesInitialized();
            checkOut11.onChildrenAdded();
            appearance12.onChildrenAdded();
            final DecoratorAppearance appearance13 = widgetByThemeElementName2.getAppearance();
            appearance13.setElementMap(elementMap);
            appearance13.setState("disabled");
            widgetByThemeElementName2.addBasicElement(appearance13);
            appearance13.onAttributesInitialized();
            final String id12 = "pmSmallUpSideArrow2Disabled";
            final PixmapElement checkOut12 = PixmapElement.checkOut();
            checkOut12.setElementMap(elementMap);
            if (elementMap != null && id12 != null) {
                elementMap.add(id12, checkOut12);
            }
            checkOut12.setHeight(16);
            checkOut12.setPosition(Alignment17.CENTER);
            checkOut12.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut12.setWidth(28);
            checkOut12.setX(899);
            checkOut12.setY(180);
            appearance13.addBasicElement(checkOut12);
            checkOut12.onAttributesInitialized();
            checkOut12.onChildrenAdded();
            appearance13.onChildrenAdded();
            final DecoratorAppearance appearance14 = widgetByThemeElementName2.getAppearance();
            appearance14.setElementMap(elementMap);
            appearance14.setState("selected");
            widgetByThemeElementName2.addBasicElement(appearance14);
            appearance14.onAttributesInitialized();
            final String id13 = "pmSmallUpSideArrow2DefaultSelected";
            final PixmapElement checkOut13 = PixmapElement.checkOut();
            checkOut13.setElementMap(elementMap);
            if (elementMap != null && id13 != null) {
                elementMap.add(id13, checkOut13);
            }
            checkOut13.setHeight(16);
            checkOut13.setPosition(Alignment17.CENTER);
            checkOut13.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut13.setWidth(28);
            checkOut13.setX(33);
            checkOut13.setY(199);
            appearance14.addBasicElement(checkOut13);
            checkOut13.onAttributesInitialized();
            checkOut13.onChildrenAdded();
            appearance14.onChildrenAdded();
            final DecoratorAppearance appearance15 = widgetByThemeElementName2.getAppearance();
            appearance15.setElementMap(elementMap);
            appearance15.setState("mouseHoverSelected");
            widgetByThemeElementName2.addBasicElement(appearance15);
            appearance15.onAttributesInitialized();
            final String id14 = "pmSmallUpSideArrow2OverSelected";
            final PixmapElement checkOut14 = PixmapElement.checkOut();
            checkOut14.setElementMap(elementMap);
            if (elementMap != null && id14 != null) {
                elementMap.add(id14, checkOut14);
            }
            checkOut14.setHeight(16);
            checkOut14.setPosition(Alignment17.CENTER);
            checkOut14.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut14.setWidth(28);
            checkOut14.setX(33);
            checkOut14.setY(199);
            appearance15.addBasicElement(checkOut14);
            checkOut14.onAttributesInitialized();
            checkOut14.onChildrenAdded();
            appearance15.onChildrenAdded();
            final DecoratorAppearance appearance16 = widgetByThemeElementName2.getAppearance();
            appearance16.setElementMap(elementMap);
            appearance16.setState("pressedSelected");
            widgetByThemeElementName2.addBasicElement(appearance16);
            appearance16.onAttributesInitialized();
            final String id15 = "pmSmallUpSideArrow2PressedSelected";
            final PixmapElement checkOut15 = PixmapElement.checkOut();
            checkOut15.setElementMap(elementMap);
            if (elementMap != null && id15 != null) {
                elementMap.add(id15, checkOut15);
            }
            checkOut15.setHeight(16);
            checkOut15.setPosition(Alignment17.CENTER);
            checkOut15.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut15.setWidth(28);
            checkOut15.setX(33);
            checkOut15.setY(199);
            appearance16.addBasicElement(checkOut15);
            checkOut15.onAttributesInitialized();
            checkOut15.onChildrenAdded();
            appearance16.onChildrenAdded();
            final DecoratorAppearance appearance17 = widgetByThemeElementName2.getAppearance();
            appearance17.setElementMap(elementMap);
            appearance17.setState("disabledSelected");
            widgetByThemeElementName2.addBasicElement(appearance17);
            appearance17.onAttributesInitialized();
            final String id16 = "pmSmallUpSideArrow2DisabledSelected";
            final PixmapElement checkOut16 = PixmapElement.checkOut();
            checkOut16.setElementMap(elementMap);
            if (elementMap != null && id16 != null) {
                elementMap.add(id16, checkOut16);
            }
            checkOut16.setHeight(16);
            checkOut16.setPosition(Alignment17.CENTER);
            checkOut16.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut16.setWidth(28);
            checkOut16.setX(369);
            checkOut16.setY(199);
            appearance17.addBasicElement(checkOut16);
            checkOut16.onAttributesInitialized();
            checkOut16.onChildrenAdded();
            appearance17.onChildrenAdded();
        }
        final Widget widgetByThemeElementName3 = appearance.getParentOfType(Widget.class).getWidgetByThemeElementName("verticalSlider", false);
        if (widgetByThemeElementName3 != null) {
            final DecoratorAppearance appearance18 = widgetByThemeElementName3.getAppearance();
            appearance18.setElementMap(elementMap);
            widgetByThemeElementName3.addBasicElement(appearance18);
            appearance18.onAttributesInitialized();
            final Widget widgetByThemeElementName4 = appearance18.getParentOfType(Widget.class).getWidgetByThemeElementName("verticalButton", false);
            if (widgetByThemeElementName4 != null) {
                final DecoratorAppearance appearance19 = widgetByThemeElementName4.getAppearance();
                appearance19.setElementMap(elementMap);
                appearance19.setState("default");
                widgetByThemeElementName4.addBasicElement(appearance19);
                appearance19.onAttributesInitialized();
                final PixmapBorder element = new PixmapBorder();
                element.onCheckOut();
                element.setElementMap(elementMap);
                appearance19.addBasicElement(element);
                element.onAttributesInitialized();
                final PixmapElement checkOut17 = PixmapElement.checkOut();
                checkOut17.setElementMap(elementMap);
                checkOut17.setHeight(28);
                checkOut17.setPosition(Alignment17.NORTH_WEST);
                checkOut17.setTexture(this.doc.getTexture("default_0.tga"));
                checkOut17.setWidth(11);
                checkOut17.setX(307);
                checkOut17.setY(349);
                element.addBasicElement(checkOut17);
                checkOut17.onAttributesInitialized();
                checkOut17.onChildrenAdded();
                final PixmapElement checkOut18 = PixmapElement.checkOut();
                checkOut18.setElementMap(elementMap);
                checkOut18.setHeight(28);
                checkOut18.setPosition(Alignment17.NORTH);
                checkOut18.setTexture(this.doc.getTexture("default_0.tga"));
                checkOut18.setWidth(1);
                checkOut18.setX(487);
                checkOut18.setY(506);
                element.addBasicElement(checkOut18);
                checkOut18.onAttributesInitialized();
                checkOut18.onChildrenAdded();
                final PixmapElement checkOut19 = PixmapElement.checkOut();
                checkOut19.setElementMap(elementMap);
                checkOut19.setHeight(28);
                checkOut19.setPosition(Alignment17.NORTH_EAST);
                checkOut19.setTexture(this.doc.getTexture("default_0.tga"));
                checkOut19.setWidth(11);
                checkOut19.setX(389);
                checkOut19.setY(285);
                element.addBasicElement(checkOut19);
                checkOut19.onAttributesInitialized();
                checkOut19.onChildrenAdded();
                final PixmapElement checkOut20 = PixmapElement.checkOut();
                checkOut20.setElementMap(elementMap);
                checkOut20.setHeight(1);
                checkOut20.setPosition(Alignment17.EAST);
                checkOut20.setTexture(this.doc.getTexture("default_1.tga"));
                checkOut20.setWidth(11);
                checkOut20.setX(767);
                checkOut20.setY(181);
                element.addBasicElement(checkOut20);
                checkOut20.onAttributesInitialized();
                checkOut20.onChildrenAdded();
                final PixmapElement checkOut21 = PixmapElement.checkOut();
                checkOut21.setElementMap(elementMap);
                checkOut21.setHeight(28);
                checkOut21.setPosition(Alignment17.SOUTH_EAST);
                checkOut21.setTexture(this.doc.getTexture("default_0.tga"));
                checkOut21.setWidth(11);
                checkOut21.setX(389);
                checkOut21.setY(347);
                element.addBasicElement(checkOut21);
                checkOut21.onAttributesInitialized();
                checkOut21.onChildrenAdded();
                final PixmapElement checkOut22 = PixmapElement.checkOut();
                checkOut22.setElementMap(elementMap);
                checkOut22.setHeight(28);
                checkOut22.setPosition(Alignment17.SOUTH);
                checkOut22.setTexture(this.doc.getTexture("default_0.tga"));
                checkOut22.setWidth(1);
                checkOut22.setX(319);
                checkOut22.setY(505);
                element.addBasicElement(checkOut22);
                checkOut22.onAttributesInitialized();
                checkOut22.onChildrenAdded();
                final PixmapElement checkOut23 = PixmapElement.checkOut();
                checkOut23.setElementMap(elementMap);
                checkOut23.setHeight(28);
                checkOut23.setPosition(Alignment17.SOUTH_WEST);
                checkOut23.setTexture(this.doc.getTexture("default_0.tga"));
                checkOut23.setWidth(11);
                checkOut23.setX(477);
                checkOut23.setY(292);
                element.addBasicElement(checkOut23);
                checkOut23.onAttributesInitialized();
                checkOut23.onChildrenAdded();
                final PixmapElement checkOut24 = PixmapElement.checkOut();
                checkOut24.setElementMap(elementMap);
                checkOut24.setHeight(1);
                checkOut24.setPosition(Alignment17.WEST);
                checkOut24.setTexture(this.doc.getTexture("default_1.tga"));
                checkOut24.setWidth(11);
                checkOut24.setX(502);
                checkOut24.setY(146);
                element.addBasicElement(checkOut24);
                checkOut24.onAttributesInitialized();
                checkOut24.onChildrenAdded();
                element.onChildrenAdded();
                final PixmapBackground checkOut25 = PixmapBackground.checkOut();
                checkOut25.setElementMap(elementMap);
                checkOut25.setEnabled(true);
                checkOut25.setScaled(true);
                appearance19.addBasicElement(checkOut25);
                checkOut25.onAttributesInitialized();
                final PixmapElement checkOut26 = PixmapElement.checkOut();
                checkOut26.setElementMap(elementMap);
                checkOut26.setHeight(1);
                checkOut26.setPosition(Alignment17.CENTER);
                checkOut26.setTexture(this.doc.getTexture("default_1.tga"));
                checkOut26.setWidth(1);
                checkOut26.setX(808);
                checkOut26.setY(177);
                checkOut25.addBasicElement(checkOut26);
                checkOut26.onAttributesInitialized();
                checkOut26.onChildrenAdded();
                checkOut25.onChildrenAdded();
                appearance19.onChildrenAdded();
                final DecoratorAppearance appearance20 = widgetByThemeElementName4.getAppearance();
                appearance20.setElementMap(elementMap);
                appearance20.setState("mouseHover");
                widgetByThemeElementName4.addBasicElement(appearance20);
                appearance20.onAttributesInitialized();
                final PixmapBorder element2 = new PixmapBorder();
                element2.onCheckOut();
                element2.setElementMap(elementMap);
                appearance20.addBasicElement(element2);
                element2.onAttributesInitialized();
                final PixmapElement checkOut27 = PixmapElement.checkOut();
                checkOut27.setElementMap(elementMap);
                checkOut27.setHeight(28);
                checkOut27.setPosition(Alignment17.NORTH_WEST);
                checkOut27.setTexture(this.doc.getTexture("default_0.tga"));
                checkOut27.setWidth(11);
                checkOut27.setX(396);
                checkOut27.setY(414);
                element2.addBasicElement(checkOut27);
                checkOut27.onAttributesInitialized();
                checkOut27.onChildrenAdded();
                this.method0(element2);
                this.method1(element2);
                this.method2(element2);
                this.method3(element2);
                this.method4(element2);
                this.method5(element2);
                this.method6(element2);
                element2.onChildrenAdded();
                this.method7(appearance20);
                appearance20.onChildrenAdded();
                this.method8(widgetByThemeElementName4);
            }
            this.method9(appearance18);
            appearance18.onChildrenAdded();
        }
        appearance.onChildrenAdded();
    }
    
    public BasicElement method0(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(28);
        checkOut.setPosition(Alignment17.NORTH);
        checkOut.setWidth(0);
        checkOut.setX(214);
        checkOut.setY(405);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method1(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(28);
        checkOut.setPosition(Alignment17.NORTH_EAST);
        checkOut.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut.setWidth(11);
        checkOut.setX(307);
        checkOut.setY(287);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method2(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(1);
        checkOut.setPosition(Alignment17.EAST);
        checkOut.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut.setWidth(11);
        checkOut.setX(51);
        checkOut.setY(137);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method3(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(28);
        checkOut.setPosition(Alignment17.SOUTH_EAST);
        checkOut.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut.setWidth(11);
        checkOut.setX(307);
        checkOut.setY(411);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method4(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(28);
        checkOut.setPosition(Alignment17.SOUTH);
        checkOut.setWidth(0);
        checkOut.setX(214);
        checkOut.setY(434);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method5(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(28);
        checkOut.setPosition(Alignment17.SOUTH_WEST);
        checkOut.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut.setWidth(11);
        checkOut.setX(307);
        checkOut.setY(318);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method6(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(1);
        checkOut.setPosition(Alignment17.WEST);
        checkOut.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut.setWidth(11);
        checkOut.setX(588);
        checkOut.setY(198);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method7(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final PixmapBackground checkOut = PixmapBackground.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setEnabled(true);
        checkOut.setScaled(true);
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setHeight(1);
        checkOut2.setPosition(Alignment17.CENTER);
        checkOut2.setWidth(0);
        checkOut2.setX(214);
        checkOut2.setY(433);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
    
    public BasicElement method8(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final DecoratorAppearance appearance = ((Widget)basicElement).getAppearance();
        appearance.setElementMap(elementMap);
        appearance.setState("pressed");
        basicElement.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final PixmapBorder element = new PixmapBorder();
        element.onCheckOut();
        element.setElementMap(elementMap);
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(28);
        checkOut.setPosition(Alignment17.NORTH_WEST);
        checkOut.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut.setWidth(11);
        checkOut.setX(890);
        checkOut.setY(416);
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setHeight(28);
        checkOut2.setPosition(Alignment17.NORTH);
        checkOut2.setWidth(0);
        checkOut2.setX(214);
        checkOut2.setY(405);
        element.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setHeight(28);
        checkOut3.setPosition(Alignment17.NORTH_EAST);
        checkOut3.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut3.setWidth(11);
        checkOut3.setX(477);
        checkOut3.setY(323);
        element.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setHeight(1);
        checkOut4.setPosition(Alignment17.EAST);
        checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut4.setWidth(11);
        checkOut4.setX(982);
        checkOut4.setY(137);
        element.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setHeight(28);
        checkOut5.setPosition(Alignment17.SOUTH_EAST);
        checkOut5.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut5.setWidth(11);
        checkOut5.setX(389);
        checkOut5.setY(316);
        element.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setHeight(28);
        checkOut6.setPosition(Alignment17.SOUTH);
        checkOut6.setWidth(0);
        checkOut6.setX(214);
        checkOut6.setY(434);
        element.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setHeight(28);
        checkOut7.setPosition(Alignment17.SOUTH_WEST);
        checkOut7.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut7.setWidth(11);
        checkOut7.setX(307);
        checkOut7.setY(380);
        element.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final PixmapElement checkOut8 = PixmapElement.checkOut();
        checkOut8.setElementMap(elementMap);
        checkOut8.setHeight(1);
        checkOut8.setPosition(Alignment17.WEST);
        checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut8.setWidth(11);
        checkOut8.setX(502);
        checkOut8.setY(142);
        element.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        element.onChildrenAdded();
        final PixmapBackground checkOut9 = PixmapBackground.checkOut();
        checkOut9.setElementMap(elementMap);
        checkOut9.setEnabled(true);
        checkOut9.setScaled(true);
        appearance.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        final PixmapElement checkOut10 = PixmapElement.checkOut();
        checkOut10.setElementMap(elementMap);
        checkOut10.setHeight(1);
        checkOut10.setPosition(Alignment17.CENTER);
        checkOut10.setWidth(0);
        checkOut10.setX(214);
        checkOut10.setY(433);
        checkOut9.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        checkOut9.onChildrenAdded();
        appearance.onChildrenAdded();
        return appearance;
    }
    
    public BasicElement method9(final BasicElement basicElement) {
        final ElementMap elementMap = this.elementMaps.peek();
        final Margin checkOut = Margin.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setInsets(new Insets(5, 3, 5, 3));
        basicElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
