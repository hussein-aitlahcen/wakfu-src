package com.ankamagames.xulor2.core.converter;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import java.awt.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.baseImpl.graphics.isometric.text.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.decorator.mesh.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.converter.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.engine.text.*;

public class ConverterLibrary
{
    private static final Logger m_logger;
    private final HashMap<Class<?>, Converter> m_converters;
    private static final ConverterLibrary m_instance;
    
    private ConverterLibrary() {
        super();
        this.m_converters = new HashMap<Class<?>, Converter>();
        this.registerDefaultConverters();
    }
    
    public static ConverterLibrary getInstance() {
        return ConverterLibrary.m_instance;
    }
    
    private void registerDefaultConverters() {
        this.register(Color.class, new ColorConverter());
        this.register(Dimension.class, new DimensionConverter());
        this.register(Insets.class, new InsetsConverter());
        this.register(String.class, new StringConverter());
        this.register(Percentage.class, new PercentageConverter());
        this.register(Texture.class, new TextureConverter());
        final Converter enumConv = new EnumConverter();
        this.register(Alignment4.class, enumConv);
        this.register(Alignment5.class, enumConv);
        this.register(Alignment8.class, enumConv);
        this.register(Alignment9.class, enumConv);
        this.register(Alignment16.class, enumConv);
        this.register(Alignment17.class, enumConv);
        this.register(BackgroundedTextHotPointPosition.class, enumConv);
        this.register(BorderLayoutData.Values.class, enumConv);
        this.register(CursorFactory.CursorType.class, enumConv);
        this.register(Events.class, enumConv);
        this.register(GradientBackgroundMesh.GradientBackgroundColorAlign.class, enumConv);
        this.register(List.ListLayoutMode.class, enumConv);
        this.register(List.ListScrollMode.class, enumConv);
        this.register(MapOverlay.MapShape.class, enumConv);
        this.register(Orientation.class, enumConv);
        this.register(ProgressBar.ProgressBarDisplayType.class, enumConv);
        this.register(ProgressText.ProgressBarDisplayValue.class, enumConv);
        this.register(ScrollBar.ScrollBarBehaviour.class, enumConv);
        this.register(GeometrySprite.SpriteRotation.class, enumConv);
        this.register(WidgetShape.class, enumConv);
        this.register(ComboBox.ComboBoxBehaviour.class, enumConv);
        this.register(Object.class, new ObjectConverter());
        final Converter primConv = new PrimitiveConverter();
        this.register(Boolean.TYPE, primConv);
        this.register(Integer.TYPE, primConv);
        this.register(Long.TYPE, primConv);
        this.register(Float.TYPE, primConv);
        this.register(Double.TYPE, primConv);
        this.register(Byte.TYPE, primConv);
        this.register(Short.TYPE, primConv);
        this.register(Boolean.class, primConv);
        this.register(Integer.class, primConv);
        this.register(Long.class, primConv);
        this.register(Float.class, primConv);
        this.register(Double.class, primConv);
        this.register(Byte.class, primConv);
        this.register(Short.class, primConv);
        final Converter lc = new ListenerConverter();
        this.register(AbstractEventListener.class, lc);
        this.register(ActivationChangedListener.class, lc);
        this.register(ColorChangedListener.class, lc);
        this.register(FocusChangedListener.class, lc);
        this.register(DragListener.class, lc);
        this.register(DragOutListener.class, lc);
        this.register(DragOverListener.class, lc);
        this.register(DropListener.class, lc);
        this.register(DropOutListener.class, lc);
        this.register(ItemOutListener.class, lc);
        this.register(ItemOverListener.class, lc);
        this.register(ItemClickListener.class, lc);
        this.register(ItemDoubleClickListener.class, lc);
        this.register(KeyPressedListener.class, lc);
        this.register(KeyReleasedListener.class, lc);
        this.register(KeyTypedListener.class, lc);
        this.register(ListSelectionChangedListener.class, lc);
        this.register(MapClickListener.class, lc);
        this.register(MapDoubleClickListener.class, lc);
        this.register(MapMoveListener.class, lc);
        this.register(MouseClickedListener.class, lc);
        this.register(MouseDoubleClickedListener.class, lc);
        this.register(MouseDraggedListener.class, lc);
        this.register(MouseDraggedInListener.class, lc);
        this.register(MouseDraggedOutListener.class, lc);
        this.register(MouseEnteredListener.class, lc);
        this.register(MouseExitedListener.class, lc);
        this.register(MouseMovedListener.class, lc);
        this.register(MousePressedListener.class, lc);
        this.register(MouseReleasedListener.class, lc);
        this.register(MouseWheeledListener.class, lc);
        this.register(SpacingChangedListener.class, lc);
        this.register(SelectionChangedListener.class, lc);
        this.register(SliderMovedListener.class, lc);
        this.register(ValueChangedListener.class, lc);
        this.register(WindowStickListener.class, lc);
        this.register(PopupDisplayListener.class, lc);
        this.register(PopupHideListener.class, lc);
        this.register(FormValidateCallBack.class, new FormConverter());
        this.register(DropValidateCallBack.class, new DropValidateConverter());
    }
    
    public void register(final Class<?> template, final Converter converter) {
        if (this.m_converters.containsKey(template)) {
            ConverterLibrary.m_logger.error((Object)("le convertisseur (template=" + template.toString() + ") est d\u00e9j\u00e0 utilis\u00e9 !"));
        }
        else {
            this.m_converters.put(template, converter);
        }
    }
    
    public boolean hasConverter(final Class<?> template) {
        if (template.equals(Object.class)) {
            return false;
        }
        final boolean notFound = !this.m_converters.containsKey(template);
        if (notFound) {
            for (final Class c : this.m_converters.keySet()) {
                if (template.isAssignableFrom(c)) {
                    return true;
                }
            }
        }
        return true;
    }
    
    public <J> J convert(final Class<J> template, final String value, final ElementMap map) {
        return this.getConverter(template).convert((Class<? extends J>)template, value, map);
    }
    
    public <J> J convert(final Class<J> template, final String value) {
        return this.getConverter(template).convert((Class<? extends J>)template, value);
    }
    
    public <J> Converter<J> getConverter(final Class<J> template) {
        final Converter<J> converter = this.m_converters.get(template);
        if (converter == null) {
            throw new UnsupportedOperationException("On essaye de trouver un convertisseur pour le type " + template.getSimpleName());
        }
        return converter;
    }
    
    public Color convertToColor(final String color) {
        return this.convert(Color.class, color);
    }
    
    public Dimension convertToDimension(final String value) {
        return this.convert(Dimension.class, value);
    }
    
    public String convertToString(final String value, final ElementMap map) {
        return this.convert(String.class, value, map);
    }
    
    public Insets convertToInsets(final String value) {
        return this.convert(Insets.class, value);
    }
    
    public Texture convertToTexture(final String value) {
        return this.convert(Texture.class, value);
    }
    
    public TextRenderer convertToFont(final String value) {
        return this.convert(TextRenderer.class, value);
    }
    
    public Percentage convertToPercentage(final String value) {
        return this.convert(Percentage.class, value);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ConverterLibrary.class);
        m_instance = new ConverterLibrary();
    }
}
