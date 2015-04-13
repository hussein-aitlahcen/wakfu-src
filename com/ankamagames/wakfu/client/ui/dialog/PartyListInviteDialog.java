package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.listener.*;

public class PartyListInviteDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public PartyListInviteDialog() {
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
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.CENTER);
        element.setSize(new Dimension(-2, -2));
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
        element2.setStyle("whiteTitle");
        element2.setText("%group.party.invite.action%".replace("%group.party.invite.action%", Xulor.getInstance().getTranslatedString("group.party.invite.action")));
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id = "name";
        final TextEditor textEditor = new TextEditor();
        textEditor.onCheckOut();
        textEditor.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, textEditor);
        }
        checkOut.addBasicElement(textEditor);
        textEditor.onAttributesInitialized();
        textEditor.onChildrenAdded();
        final Container checkOut3 = Container.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        final Button element3 = new Button();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setText("%ok%".replace("%ok%", Xulor.getInstance().getTranslatedString("ok")));
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("wakfu.partyList:invite(name)");
        element3.setOnClick(onClick);
        checkOut3.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final Button element4 = new Button();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setText("%cancel%".replace("%cancel%", Xulor.getInstance().getTranslatedString("cancel")));
        final MouseClickedListener onClick2 = new MouseClickedListener();
        onClick2.setCallBackFunc("unloadDialog");
        element4.setOnClick(onClick2);
        checkOut3.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        checkOut3.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
