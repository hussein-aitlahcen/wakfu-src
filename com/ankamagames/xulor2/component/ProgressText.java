package com.ankamagames.xulor2.component;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;

public class ProgressText extends Container implements FontClient, ColorClient, ModulationColorClient
{
    public static final String TAG = "progressText";
    public static final String HORIZONTAL_SEPARATOR = " / ";
    public static final String VERTICAL_SEPARATOR = "¯";
    public static final String PERCENT = "%";
    private Label m_label;
    private Label m_label2;
    private Label m_label3;
    private Orientation m_textOrientation;
    private Color m_color;
    private TextRenderer m_textRenderer;
    private boolean m_horizontal;
    private ProgressBarDisplayValue m_displayValue;
    private boolean m_splitText;
    public static final float DEFAULT_MIN_VALUE = 0.0f;
    public static final float DEFAULT_MAX_VALUE = 1.0f;
    private float m_minBound;
    private float m_maxBound;
    private float m_value;
    public static final int DISPLAY_VALUE_HASH;
    public static final int FONT_HASH;
    public static final int HORIZONTAL_HASH;
    public static final int MAX_BOUND_HASH;
    public static final int MIN_BOUND_HASH;
    public static final int SPLIT_TEXT_HASH;
    public static final int TEXT_ORIENTATION_HASH;
    public static final int VALUE_HASH;
    
    public ProgressText() {
        super();
        this.m_textOrientation = Orientation.EAST;
        this.m_color = null;
        this.m_textRenderer = null;
        this.m_horizontal = true;
        this.m_displayValue = ProgressBarDisplayValue.NOTHING;
        this.m_splitText = true;
        this.m_minBound = 0.0f;
        this.m_maxBound = 1.0f;
        this.m_value = 0.0f;
    }
    
    @Override
    public String getTag() {
        return "progressText";
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof TextWidgetAppearance;
    }
    
    @Override
    public TextWidgetAppearance getAppearance() {
        return (TextWidgetAppearance)this.m_appearance;
    }
    
    public void setTextOrientation(final Orientation textOrientation) {
        this.m_textOrientation = textOrientation;
        if (this.m_label != null) {
            this.m_label.setOrientation(textOrientation);
        }
        if (this.m_label2 != null) {
            this.m_label2.setOrientation(textOrientation);
        }
        if (this.m_label3 != null) {
            this.m_label3.setOrientation(textOrientation);
        }
    }
    
    @Override
    public void setFont(final TextRenderer renderer) {
        if (this.m_label != null) {
            this.m_label.setFont(renderer);
        }
        if (this.m_label2 != null) {
            this.m_label2.setFont(renderer);
        }
        if (this.m_label3 != null) {
            this.m_label3.setFont(renderer);
        }
        this.m_textRenderer = renderer;
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_label != null) {
            this.m_label.setModulationColor(c);
        }
        if (this.m_label2 != null) {
            this.m_label2.setModulationColor(c);
        }
        if (this.m_label3 != null) {
            this.m_label3.setModulationColor(c);
        }
    }
    
    @Override
    public Color getModulationColor() {
        if (this.m_label != null) {
            return this.m_label.getModulationColor();
        }
        return null;
    }
    
    @Override
    public void setColor(final Color color, final String name) {
        if (name == null || name.equalsIgnoreCase("text")) {
            if (this.m_color == color) {
                return;
            }
            this.m_color = color;
            if (this.m_label != null) {
                this.m_label.setColor(this.m_color, null);
            }
            if (this.m_label2 != null) {
                this.m_label2.setColor(this.m_color, null);
            }
            if (this.m_label3 != null) {
                this.m_label3.setColor(this.m_color, null);
            }
        }
    }
    
    private void setText(final String text) {
        this.setText(text, null);
    }
    
    private void setText(String text1, final String text2) {
        if (!this.m_splitText && text1 != null && !text1.isEmpty() && text2 != null && !text2.isEmpty()) {
            text1 = text1 + " / " + text2;
            if (this.m_label2 != null) {
                this.destroy(this.m_label2);
                this.m_label2 = null;
            }
            if (this.m_label3 != null) {
                this.destroy(this.m_label3);
                this.m_label3 = null;
            }
        }
        if (text1 == null || text1.isEmpty()) {
            if (this.m_label != null) {
                this.destroy(this.m_label);
                this.m_label = null;
            }
            if (this.m_label2 != null) {
                this.destroy(this.m_label2);
                this.m_label2 = null;
            }
            if (this.m_label3 != null) {
                this.destroy(this.m_label3);
                this.m_label3 = null;
            }
            return;
        }
        if (this.m_label == null) {
            (this.m_label = new Label()).onCheckOut();
            this.reInitLabel(this.m_label);
            this.add(this.m_label);
        }
        this.m_label.setText(text1);
        if (!this.m_splitText) {
            return;
        }
        if (text2 != null && !text2.isEmpty()) {
            if (this.m_label2 == null) {
                (this.m_label2 = new Label()).onCheckOut();
                this.reInitLabel(this.m_label2);
                this.add(this.m_label2);
                this.m_label2.setText("¯");
            }
            if (this.m_label3 == null) {
                (this.m_label3 = new Label()).onCheckOut();
                this.reInitLabel(this.m_label3);
                this.add(this.m_label3);
            }
            this.m_label3.setText(text2);
        }
        else {
            if (this.m_label2 != null) {
                this.destroy(this.m_label2);
                this.m_label2 = null;
            }
            if (this.m_label3 != null) {
                this.destroy(this.m_label3);
                this.m_label3 = null;
            }
        }
    }
    
    private String getText() {
        if (this.m_label != null) {
            return this.m_label.getText();
        }
        return "";
    }
    
    public float getMinBound() {
        return this.m_minBound;
    }
    
    public void setMinBound(final float minBound) {
        this.m_minBound = minBound;
        if (this.m_value < this.m_minBound) {
            this.m_value = this.m_minBound;
        }
        this.updateText();
    }
    
    public float getMaxBound() {
        return this.m_maxBound;
    }
    
    public void setMaxBound(final float maxBound) {
        this.m_maxBound = maxBound;
        if (this.m_value > this.m_maxBound) {
            this.m_value = this.m_maxBound;
        }
        this.updateText();
    }
    
    public float getPercentage() {
        return (this.m_value - this.m_minBound) / (this.m_maxBound - this.m_minBound);
    }
    
    public float getValue() {
        return this.m_value;
    }
    
    public void setValue(float value) {
        if (value < this.m_minBound) {
            value = this.m_minBound;
        }
        if (value > this.m_maxBound) {
            value = this.m_maxBound;
        }
        this.m_value = value;
        this.updateText();
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        if (this.m_horizontal != horizontal) {
            this.m_horizontal = horizontal;
        }
        this.invalidateMinSize();
    }
    
    public boolean getSplitText() {
        return this.m_splitText;
    }
    
    public void setSplitText(final boolean splitText) {
        this.m_splitText = splitText;
    }
    
    public ProgressBarDisplayValue getDisplayValue() {
        return this.m_displayValue;
    }
    
    public void setDisplayValue(final ProgressBarDisplayValue displayValue) {
        this.m_displayValue = displayValue;
        this.updateText();
    }
    
    private void reInitLabel(final Label l) {
        l.setExpandable(false);
        l.setAlign(Alignment9.CENTER);
        l.setFont(this.m_textRenderer);
        l.setOrientation(this.m_textOrientation);
        l.setColor(this.m_color, null);
    }
    
    @Override
    public void validate() {
        super.validate();
    }
    
    private void updateText() {
        switch (this.m_displayValue) {
            case CURRENT_VALUE: {
                if (this.m_value - Math.floor(this.m_value) != 0.0) {
                    this.setText(Float.toString(this.m_value));
                    break;
                }
                this.setText(Integer.toString((int)this.m_value));
                break;
            }
            case CURRENT_PERCENTAGE: {
                this.setText(Integer.toString((int)(this.getPercentage() * 100.0f)) + "%");
                break;
            }
            case CURRENT_AND_TOTAL_VALUES: {
                String text1;
                if (this.m_value - Math.floor(this.m_value) != 0.0) {
                    text1 = Float.toString(this.m_value);
                }
                else {
                    text1 = Integer.toString((int)this.m_value);
                }
                String text2;
                if (this.m_maxBound - Math.floor(this.m_maxBound) != 0.0) {
                    text2 = Float.toString(this.m_maxBound);
                }
                else {
                    text2 = Integer.toString((int)this.m_maxBound);
                }
                this.setText(text1, text2);
                break;
            }
            default: {
                this.setText("");
                break;
            }
        }
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final ProgressText pb = (ProgressText)source;
        pb.setHorizontal(this.m_horizontal);
        pb.setValue(this.m_value);
        pb.setMaxBound(this.m_maxBound);
        pb.setMinBound(this.m_minBound);
        pb.setSplitText(this.m_splitText);
        pb.setTextOrientation(this.m_textOrientation);
        pb.setText(this.getText(), (this.m_label3 != null) ? this.m_label3.getText() : "");
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_textOrientation = null;
        this.m_textRenderer = null;
        this.m_label = null;
        this.m_label2 = null;
        this.m_label3 = null;
        this.m_color = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final TextWidgetAppearance twa = TextWidgetAppearance.checkOut();
        twa.setWidget(this);
        this.add(twa);
        final ProgressTextLayout rl = new ProgressTextLayout();
        rl.onCheckOut();
        this.add(rl);
        this.setNonBlocking(false);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ProgressText.DISPLAY_VALUE_HASH) {
            this.setDisplayValue(ProgressBarDisplayValue.value(value));
        }
        else if (hash == ProgressText.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressText.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressText.FONT_HASH) {
            this.setFont(cl.convertToFont(value));
        }
        else if (hash == ProgressText.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressText.SPLIT_TEXT_HASH) {
            this.setSplitText(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressText.TEXT_ORIENTATION_HASH) {
            this.setTextOrientation(Orientation.value(value));
        }
        else {
            if (hash != ProgressText.VALUE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ProgressText.DISPLAY_VALUE_HASH) {
            this.setDisplayValue((ProgressBarDisplayValue)value);
        }
        else if (hash == ProgressText.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressText.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ProgressText.FONT_HASH) {
            this.setFont((TextRenderer)value);
        }
        else if (hash == ProgressText.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressText.SPLIT_TEXT_HASH) {
            this.setSplitText(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ProgressText.TEXT_ORIENTATION_HASH) {
            this.setTextOrientation((Orientation)value);
        }
        else {
            if (hash != ProgressText.VALUE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    static {
        DISPLAY_VALUE_HASH = "displayValue".hashCode();
        FONT_HASH = "font".hashCode();
        HORIZONTAL_HASH = "horizontal".hashCode();
        MAX_BOUND_HASH = "maxBound".hashCode();
        MIN_BOUND_HASH = "minBound".hashCode();
        SPLIT_TEXT_HASH = "splitText".hashCode();
        TEXT_ORIENTATION_HASH = "textOrientation".hashCode();
        VALUE_HASH = "value".hashCode();
    }
    
    public enum ProgressBarDisplayValue
    {
        CURRENT_VALUE, 
        CURRENT_PERCENTAGE, 
        CURRENT_AND_TOTAL_VALUES, 
        NOTHING;
        
        public static ProgressBarDisplayValue value(final String value) {
            final ProgressBarDisplayValue[] arr$;
            final ProgressBarDisplayValue[] values = arr$ = values();
            for (final ProgressBarDisplayValue a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
        
        public static ProgressBarDisplayValue valueFromOrdinal(final int ordinal) {
            final ProgressBarDisplayValue[] values = values();
            if (values.length > ordinal && ordinal >= 0) {
                return values[ordinal];
            }
            return null;
        }
    }
    
    class ProgressTextLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            int height = 0;
            int width = 0;
            if (ProgressText.this.m_horizontal) {
                if (ProgressText.this.m_label != null) {
                    final Dimension labelMinSize = ProgressText.this.m_label.getMinSize();
                    height = Math.max(height, labelMinSize.height);
                    width += labelMinSize.width;
                }
                if (ProgressText.this.m_label2 != null) {
                    final Dimension labelMinSize = ProgressText.this.m_label2.getMinSize();
                    height = Math.max(height, labelMinSize.height);
                    width += labelMinSize.width;
                }
                if (ProgressText.this.m_label3 != null) {
                    final Dimension labelMinSize = ProgressText.this.m_label3.getMinSize();
                    height = Math.max(height, labelMinSize.height);
                    width += labelMinSize.width;
                }
            }
            else {
                if (ProgressText.this.m_label != null) {
                    final Dimension labelMinSize = ProgressText.this.m_label.getMinSize();
                    width = Math.max(width, labelMinSize.width);
                    height += labelMinSize.height;
                }
                if (ProgressText.this.m_label3 != null) {
                    final Dimension labelMinSize = ProgressText.this.m_label3.getMinSize();
                    width = Math.max(width, labelMinSize.width);
                    height += labelMinSize.height;
                }
            }
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            int height = 0;
            int width = 0;
            if (ProgressText.this.m_horizontal) {
                if (ProgressText.this.m_label != null) {
                    final Dimension labelPrefSize = ProgressText.this.m_label.getPrefSize();
                    height = Math.max(height, labelPrefSize.height);
                    width += labelPrefSize.width;
                }
                if (ProgressText.this.m_label2 != null) {
                    final Dimension labelPrefSize = ProgressText.this.m_label2.getPrefSize();
                    height = Math.max(height, labelPrefSize.height);
                    width += labelPrefSize.width;
                }
                if (ProgressText.this.m_label3 != null) {
                    final Dimension labelPrefSize = ProgressText.this.m_label3.getPrefSize();
                    height = Math.max(height, labelPrefSize.height);
                    width += labelPrefSize.width;
                }
            }
            else {
                if (ProgressText.this.m_label != null) {
                    final Dimension labelPrefSize = ProgressText.this.m_label.getPrefSize();
                    width = Math.max(width, labelPrefSize.width);
                    height += labelPrefSize.height;
                }
                if (ProgressText.this.m_label3 != null) {
                    final Dimension labelPrefSize = ProgressText.this.m_label3.getPrefSize();
                    width = Math.max(width, labelPrefSize.width);
                    height += labelPrefSize.height;
                }
            }
            return new Dimension(width, height);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            final int availableHeight = parent.getAppearance().getContentHeight();
            final int availableWidth = parent.getAppearance().getContentWidth();
            if (ProgressText.this.m_horizontal) {
                int usedWidth = 0;
                if (ProgressText.this.m_label != null) {
                    usedWidth += ProgressText.this.m_label.getPrefSize().width;
                }
                if (ProgressText.this.m_label2 != null) {
                    usedWidth += ProgressText.this.m_label2.getPrefSize().width;
                }
                if (ProgressText.this.m_label3 != null) {
                    usedWidth += ProgressText.this.m_label3.getPrefSize().width;
                }
                int x = Alignment9.CENTER.getX(usedWidth, availableWidth);
                if (ProgressText.this.m_label != null) {
                    final Dimension prefSize = ProgressText.this.m_label.getPrefSize();
                    final int y = Alignment9.CENTER.getY(prefSize.height, availableHeight);
                    ProgressText.this.m_label.setPosition(x, y);
                    ProgressText.this.m_label.setSizeToPrefSize();
                    x += prefSize.width;
                }
                if (ProgressText.this.m_label2 != null) {
                    final Dimension prefSize = ProgressText.this.m_label2.getPrefSize();
                    final int y = Alignment9.CENTER.getY(prefSize.height, availableHeight);
                    ProgressText.this.m_label2.setPosition(x, y);
                    ProgressText.this.m_label2.setSizeToPrefSize();
                    x += prefSize.width;
                }
                if (ProgressText.this.m_label3 != null) {
                    final Dimension prefSize = ProgressText.this.m_label3.getPrefSize();
                    final int y = Alignment9.CENTER.getY(prefSize.height, availableHeight);
                    ProgressText.this.m_label3.setPosition(x, y);
                    ProgressText.this.m_label3.setSizeToPrefSize();
                }
            }
            else {
                int usedHeight = 0;
                if (ProgressText.this.m_label != null) {
                    usedHeight += ProgressText.this.m_label.getPrefSize().height;
                }
                if (ProgressText.this.m_label3 != null) {
                    usedHeight += ProgressText.this.m_label3.getPrefSize().height;
                }
                int y2 = Alignment9.CENTER.getY(usedHeight, availableHeight);
                if (ProgressText.this.m_label3 != null) {
                    final Dimension prefSize = ProgressText.this.m_label3.getPrefSize();
                    final int x2 = Alignment9.CENTER.getX(prefSize.width, availableWidth);
                    ProgressText.this.m_label3.setPosition(x2, y2);
                    ProgressText.this.m_label3.setSizeToPrefSize();
                    y2 += prefSize.height;
                }
                if (ProgressText.this.m_label != null) {
                    final Dimension prefSize = ProgressText.this.m_label.getPrefSize();
                    final int x2 = Alignment9.CENTER.getX(prefSize.width, availableWidth);
                    ProgressText.this.m_label.setPosition(x2, y2);
                    ProgressText.this.m_label.setSizeToPrefSize();
                }
                if (ProgressText.this.m_label2 != null) {
                    final Dimension prefSize = ProgressText.this.m_label2.getPrefSize();
                    final int x2 = Alignment9.CENTER.getX(prefSize.width, availableWidth);
                    ProgressText.this.m_label2.setPosition(x2, ProgressText.this.m_label.getY() - 2);
                    ProgressText.this.m_label2.setSizeToPrefSize();
                }
            }
        }
    }
}
