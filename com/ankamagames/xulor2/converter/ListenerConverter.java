package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class ListenerConverter implements Converter<AbstractEventListener>
{
    private Class<AbstractEventListener> TEMPLATE;
    
    public ListenerConverter() {
        super();
        this.TEMPLATE = AbstractEventListener.class;
    }
    
    @Override
    public AbstractEventListener convert(final String value) {
        return null;
    }
    
    @Override
    public AbstractEventListener convert(final Class<? extends AbstractEventListener> type, final String value) {
        return this.convert(type, value, (ElementMap)null);
    }
    
    @Override
    public AbstractEventListener convert(final Class<? extends AbstractEventListener> type, final String value, final ElementMap map) {
        AbstractEventListener l = null;
        if (value != null) {
            if (type.equals(ActivationChangedListener.class)) {
                l = new ActivationChangedListener();
            }
            else if (type.equals(ColorChangedListener.class)) {
                l = new ColorChangedListener();
            }
            else if (type.equals(FocusChangedListener.class)) {
                l = new FocusChangedListener();
            }
            else if (type.equals(KeyPressedListener.class)) {
                l = new KeyPressedListener();
            }
            else if (type.equals(KeyReleasedListener.class)) {
                l = new KeyReleasedListener();
            }
            else if (type.equals(KeyTypedListener.class)) {
                l = new KeyTypedListener();
            }
            else if (type.equals(MapClickListener.class)) {
                l = new MapClickListener();
            }
            else if (type.equals(MapDoubleClickListener.class)) {
                l = new MapDoubleClickListener();
            }
            else if (type.equals(MapMoveListener.class)) {
                l = new MapMoveListener();
            }
            else if (type.equals(MousePressedListener.class)) {
                l = new MousePressedListener();
            }
            else if (type.equals(MouseReleasedListener.class)) {
                l = new MouseReleasedListener();
            }
            else if (type.equals(MouseClickedListener.class)) {
                l = new MouseClickedListener();
            }
            else if (type.equals(MouseDoubleClickedListener.class)) {
                l = new MouseDoubleClickedListener();
            }
            else if (type.equals(MouseEnteredListener.class)) {
                l = new MouseEnteredListener();
            }
            else if (type.equals(MouseExitedListener.class)) {
                l = new MouseExitedListener();
            }
            else if (type.equals(MouseDraggedOutListener.class)) {
                l = new MouseDraggedOutListener();
            }
            else if (type.equals(MouseDraggedListener.class)) {
                l = new MouseDraggedListener();
            }
            else if (type.equals(MouseDraggedInListener.class)) {
                l = new MouseDraggedInListener();
            }
            else if (type.equals(MouseMovedListener.class)) {
                l = new MouseMovedListener();
            }
            else if (type.equals(MouseReleasedListener.class)) {
                l = new MouseReleasedListener();
            }
            else if (type.equals(MouseWheeledListener.class)) {
                l = new MouseWheeledListener();
            }
            else if (type.equals(SpacingChangedListener.class)) {
                l = new SpacingChangedListener();
            }
            else if (type.equals(SelectionChangedListener.class)) {
                l = new SelectionChangedListener();
            }
            else if (type.equals(SliderMovedListener.class)) {
                l = new SliderMovedListener();
            }
            else if (type.equals(ValueChangedListener.class)) {
                l = new ValueChangedListener();
            }
            else if (type.equals(WindowStickListener.class)) {
                l = new WindowStickListener();
            }
            else if (type.equals(ItemOutListener.class)) {
                l = new ItemOutListener();
            }
            else if (type.equals(ItemOverListener.class)) {
                l = new ItemOverListener();
            }
            else if (type.equals(ItemClickListener.class)) {
                l = new ItemClickListener();
            }
            else if (type.equals(ItemDoubleClickListener.class)) {
                l = new ItemDoubleClickListener();
            }
            else if (type.equals(DragListener.class)) {
                l = new DragListener();
            }
            else if (type.equals(DragOutListener.class)) {
                l = new DragOutListener();
            }
            else if (type.equals(DragOverListener.class)) {
                l = new DragOverListener();
            }
            else if (type.equals(DropListener.class)) {
                l = new DropListener();
            }
            else if (type.equals(DropOutListener.class)) {
                l = new DropOutListener();
            }
            else if (type.equals(ListSelectionChangedListener.class)) {
                l = new ListSelectionChangedListener();
            }
            else if (type.equals(PopupDisplayListener.class)) {
                l = new PopupDisplayListener();
            }
            else if (type.equals(PopupHideListener.class)) {
                l = new PopupHideListener();
            }
        }
        if (l != null) {
            l.setCallBackFunc(value);
        }
        return l;
    }
    
    @Override
    public Class<AbstractEventListener> convertsTo() {
        return this.TEMPLATE;
    }
    
    @Override
    public boolean canConvertFromScratch() {
        return true;
    }
    
    @Override
    public boolean canConvertWithoutVariables() {
        return true;
    }
    
    @Override
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends AbstractEventListener> type, final String attr, final Environment env) {
        final String varName = doc.getUnusedVarName();
        doc.addImport(type);
        doc.addGeneratedCommandLine(new ClassVariable(type, varName, "new " + type.getSimpleName() + "()"));
        doc.addGeneratedCommandLine(new ClassMethodCall(null, "setCallBackFunc", varName, new String[] { "\"" + attr + "\"" }));
        return varName;
    }
}
