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
import com.ankamagames.xulor2.core.renderer.condition.*;
import com.ankamagames.xulor2.appearance.*;

public class VideoLoadingDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public VideoLoadingDialog() {
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
        onClick.setCallBackFunc("wakfu.videoLoading:onClick");
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
        final Container checkOut3 = Container.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        final StaticLayoutData element5 = new StaticLayoutData();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setAlign(Alignment17.SOUTH_EAST);
        element5.setSize(new Dimension(110, 110));
        checkOut3.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        final String id2 = "animatedElementViewer";
        final AnimatedElementViewer animatedElementViewer = new AnimatedElementViewer();
        animatedElementViewer.onCheckOut();
        animatedElementViewer.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, animatedElementViewer);
        }
        animatedElementViewer.setFilePath("animVideoLoading.anm");
        animatedElementViewer.setAnimName("AnimVideoLoading");
        animatedElementViewer.setOffsetX(0.0f);
        animatedElementViewer.setOffsetY(0.0f);
        animatedElementViewer.setScale(1.0f);
        checkOut3.addBasicElement(animatedElementViewer);
        animatedElementViewer.onAttributesInitialized();
        final PropertyElement checkOut4 = PropertyElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setAttribute("animName");
        checkOut4.setName("isNewWorldReady");
        animatedElementViewer.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        final ConditionResult element6 = new ConditionResult();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setValue("AnimVideoLoaded");
        element6.setElseValue("AnimVideoLoading");
        checkOut4.addBasicElement(element6);
        element6.onAttributesInitialized();
        final TrueCondition element7 = new TrueCondition();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        element6.addBasicElement(element7);
        element7.onAttributesInitialized();
        element7.onChildrenAdded();
        element6.onChildrenAdded();
        checkOut4.onChildrenAdded();
        animatedElementViewer.onChildrenAdded();
        checkOut3.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
