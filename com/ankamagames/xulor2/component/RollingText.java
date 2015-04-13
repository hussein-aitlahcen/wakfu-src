package com.ankamagames.xulor2.component;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class RollingText extends Container implements FontClient, ColorClient
{
    private static Logger m_logger;
    public static final String TAG = "rollingText";
    private long m_number;
    private static final String[] NUMBERS;
    private static final String TEXT_ID = "text";
    private ArrayList<Digit> m_listDigits;
    private ArrayList<RollEndListener> m_rollEndListeners;
    private ArrayList<ValueChangedListener> m_valueChangedListeners;
    private boolean m_stopScrolling;
    private static final int UPDATE_TIME = 25;
    private static final float RANDOM_MOVEMENT_DURATION = 500.0f;
    public static final int TOTAL_DURATION = 2000;
    private int m_cycleDuration;
    private int m_nbOfDigits;
    private int m_digitLockedIndex;
    private TextRenderer m_textRenderer;
    private Color m_textColor;
    private boolean m_digitsChanged;
    private boolean m_valueChanged;
    
    public RollingText() {
        super();
        this.m_listDigits = new ArrayList<Digit>();
        this.m_rollEndListeners = new ArrayList<RollEndListener>();
        this.m_valueChangedListeners = new ArrayList<ValueChangedListener>();
        this.m_digitLockedIndex = -1;
    }
    
    @Override
    public void setFont(final TextRenderer renderer) {
        if (renderer == this.m_textRenderer) {
            return;
        }
        this.m_textRenderer = renderer;
        for (final Digit digit : this.m_listDigits) {
            final TextView textView = (TextView)digit.getList().getElementMap().getElement("text");
            textView.setFont(renderer);
        }
    }
    
    @Override
    public void setColor(final Color c, final String name) {
        if (name == null || name.equals("text")) {
            if (c == this.m_textColor) {
                return;
            }
            this.m_textColor = c;
            for (final Digit digit : this.m_listDigits) {
                final TextView textView = (TextView)digit.getList().getElementMap().getElement("text");
                textView.setColor(c, "text");
            }
        }
    }
    
    private List createList() {
        int maxWidth = 0;
        int maxHeight = 0;
        for (final String n : RollingText.NUMBERS) {
            final int visualCharacterWidth = this.m_textRenderer.getCharacterWidth(n.charAt(0));
            final int visualCharacterHeight = this.m_textRenderer.getVisualCharacterHeight(n.charAt(0));
            if (visualCharacterWidth > maxWidth) {
                maxWidth = visualCharacterWidth;
            }
            if (visualCharacterHeight > maxHeight) {
                maxHeight = visualCharacterHeight;
            }
        }
        final List list = new List();
        list.onCheckOut();
        list.setElementMap(this.m_elementMap);
        list.setCellSize(new Dimension(maxWidth, maxHeight));
        list.setAutoIdealSize(true);
        list.setIdealSizeMinRows(1);
        list.setIdealSizeMaxRows(1);
        list.setIdealSizeMinColumns(1);
        list.setIdealSizeMaxColumns(1);
        list.setExpandable(false);
        list.setNonBlocking(false);
        list.setScrollMode(List.ListScrollMode.CIRCULAR);
        list.setSelectionable(false);
        list.setScrollBar(false);
        list.setScrollOnMouseWheel(false);
        final RowLayoutData rld = new RowLayoutData();
        rld.onCheckOut();
        rld.setAlign(Alignment9.CENTER);
        list.add(rld);
        final ItemRenderer itemRenderer = new ItemRenderer();
        itemRenderer.onCheckOut();
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(this.m_elementMap);
        textView.setExpandable(false);
        final RowLayoutData rld2 = new RowLayoutData();
        rld2.onCheckOut();
        rld2.setAlign(Alignment9.CENTER);
        textView.add(rld2);
        if (this.m_textRenderer != null) {
            final FontElement fontElement = new FontElement();
            fontElement.onCheckOut();
            fontElement.setRenderer(this.m_textRenderer);
            textView.getAppearance().add(fontElement);
        }
        if (this.m_textColor != null) {
            final ColorElement colorElement = new ColorElement();
            colorElement.onCheckOut();
            colorElement.setColor(this.m_textColor);
            textView.getAppearance().add(colorElement);
        }
        textView.setId("text");
        final ItemElement item = new ItemElement();
        item.onCheckOut();
        item.setAttribute("text");
        textView.add(item);
        textView.onChildrenAdded();
        itemRenderer.add(textView);
        itemRenderer.onChildrenAdded();
        list.add(itemRenderer);
        list.onChildrenAdded();
        list.setContent(RollingText.NUMBERS);
        this.add(list);
        this.onChildrenAdded();
        return list;
    }
    
    public void setNumber(final long number) {
        this.m_number = number;
        final int length = String.valueOf(this.m_number).length();
        if (length != this.m_nbOfDigits) {
            this.m_nbOfDigits = length;
            this.m_cycleDuration = 2000 / (this.m_nbOfDigits * 2 - 1);
            this.m_digitsChanged = true;
            this.setNeedsToPreProcess();
        }
        this.m_valueChanged = true;
        this.setNeedsToPostProcess();
    }
    
    private void addList() {
        Digit previousDigit = null;
        final int currentDigitsSize = this.m_listDigits.size();
        if (!this.m_listDigits.isEmpty()) {
            previousDigit = this.m_listDigits.get(currentDigitsSize - 1);
        }
        final Byte targetDigit = Byte.valueOf("" + String.valueOf(this.m_number).charAt(currentDigitsSize));
        try {
            final Digit digit = new Digit(this.createList(), currentDigitsSize, previousDigit, (byte)targetDigit);
            this.m_listDigits.add(digit);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean start() {
        if (this.m_listDigits.isEmpty()) {
            return false;
        }
        if (this.m_nbOfDigits > this.m_listDigits.size()) {
            return false;
        }
        final Digit firstDigit = this.m_listDigits.get(this.m_listDigits.size() - 1);
        this.m_digitLockedIndex = 0;
        if (this.m_listDigits.size() > 1) {
            firstDigit.makeFullRoll();
        }
        else {
            firstDigit.scrollToTarget();
        }
        return true;
    }
    
    public void clean() {
        for (final Digit digit : this.m_listDigits) {
            digit.clean();
        }
        this.m_listDigits.clear();
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_digitsChanged) {
            for (int i = 0, size = this.m_listDigits.size(); i < size; ++i) {
                final Digit digit = this.m_listDigits.get(i);
                digit.getList().destroySelfFromParent();
            }
            this.m_listDigits.clear();
            for (int i = 0; i < this.m_nbOfDigits; ++i) {
                this.addList();
            }
            this.m_digitsChanged = false;
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (this.m_valueChanged) {
            final boolean ok = this.start();
            if (ok) {
                this.m_valueChanged = false;
            }
        }
        return ret;
    }
    
    private int getCurrentValue() {
        int total = 0;
        for (final Digit digit : this.m_listDigits) {
            total += (int)(digit.getCurrentByteValue() * Math.pow(10.0, this.m_listDigits.size() - 1 - digit.getDigitIndex()));
        }
        return total;
    }
    
    public long getTotalDuration() {
        int total = 0;
        for (final Digit digit : this.m_listDigits) {
            final byte targetDigit = digit.getTargetDigit();
            total += this.m_cycleDuration * 2 * targetDigit;
        }
        return 4000L;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final RowLayout rowLayout = new RowLayout();
        rowLayout.onCheckOut();
        rowLayout.setAlign(Alignment9.EAST);
        this.add(rowLayout);
        this.m_valueChanged = false;
        this.m_digitsChanged = false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.clean();
        this.m_valueChangedListeners.clear();
    }
    
    @Override
    public String getTag() {
        return "rollingText";
    }
    
    public void addRollEndListener(final RollEndListener rollEndListener) {
        if (!this.m_rollEndListeners.contains(rollEndListener)) {
            this.m_rollEndListeners.add(rollEndListener);
        }
    }
    
    public void removeRollEndListener(final RollEndListener rollEndListener) {
        this.m_rollEndListeners.remove(rollEndListener);
    }
    
    private void onEnd() {
        for (int i = this.m_rollEndListeners.size() - 1; i >= 0; --i) {
            this.m_rollEndListeners.get(i).onRollEnd();
        }
    }
    
    public void addValueChangedListener(final ValueChangedListener valueChangedListener) {
        if (!this.m_valueChangedListeners.contains(valueChangedListener)) {
            this.m_valueChangedListeners.add(valueChangedListener);
        }
    }
    
    public void removeValueChangedListener(final ValueChangedListener valueChangedListener) {
        this.m_valueChangedListeners.remove(valueChangedListener);
    }
    
    private void onValueChanged(final int value) {
        for (int i = this.m_valueChangedListeners.size() - 1; i >= 0; --i) {
            this.m_valueChangedListeners.get(i).onValueChanged(value);
        }
    }
    
    static {
        RollingText.m_logger = Logger.getLogger((Class)RollingText.class);
        NUMBERS = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    }
    
    private class Digit
    {
        private Runnable m_scrollRunnable;
        private final List m_list;
        private final int m_digitIndex;
        private final Digit m_nextDigit;
        private final byte m_targetDigit;
        private short m_currentIndex;
        private byte m_currentByteValue;
        
        private Digit(final List list, final int digitIndex, final Digit nextDigit, final byte targetDigit) {
            super();
            this.m_list = list;
            this.m_digitIndex = digitIndex;
            this.m_nextDigit = nextDigit;
            this.m_targetDigit = targetDigit;
        }
        
        private Digit(final RollingText rollingText, final List list, final int digitIndex, final byte targetDigit) {
            this(rollingText, list, digitIndex, null, targetDigit);
        }
        
        private void scrollTo(final short targetedIndex, final boolean forward, final int scrollCount, final float movementSpeed) {
            RollingText.this.m_stopScrolling = false;
            final float startOffset = this.m_list.getOffset();
            if (this.m_scrollRunnable != null) {
                return;
            }
            this.m_scrollRunnable = new Runnable() {
                @Override
                public void run() {
                    if (Digit.this.m_list.isUnloading()) {
                        return;
                    }
                    final float offset = Digit.this.m_list.getOffset();
                    final float inc = forward ? movementSpeed : (-movementSpeed);
                    if (RollingText.this.m_stopScrolling && this.tryStop(offset, inc, forward)) {
                        return;
                    }
                    if (targetedIndex != -1 && !RollingText.this.m_stopScrolling) {
                        boolean end;
                        if (forward) {
                            end = (Digit.this.m_list.getOffset() - startOffset + inc >= scrollCount);
                        }
                        else {
                            end = (Digit.this.m_list.getOffset() - startOffset + inc <= scrollCount);
                        }
                        if (end) {
                            this.tryStop(offset, inc, forward);
                            return;
                        }
                    }
                    Digit.this.setListOffset(offset + inc);
                    if (Digit.this.m_list.getRenderables().isEmpty()) {
                        return;
                    }
                    final TextWidget t = (TextWidget)Digit.this.m_list.getRenderableByOffset((int)Digit.this.m_list.getOffset()).getInnerElementMap().getElement("text");
                    final String text = t.getText();
                    if (text.length() == 0) {
                        return;
                    }
                    final Byte newValue = Byte.valueOf(text);
                    final boolean valueChanged = newValue != Digit.this.m_currentByteValue;
                    Digit.this.m_currentByteValue = newValue;
                    if (valueChanged && Digit.this.m_digitIndex == RollingText.this.m_nbOfDigits - 1) {
                        RollingText.this.onValueChanged(RollingText.this.getCurrentValue());
                    }
                }
                
                private boolean tryStop(final float offset, final float inc, final boolean forward) {
                    int bound;
                    boolean stop;
                    if (forward) {
                        bound = MathHelper.fastCeil(offset);
                        stop = (offset + inc >= bound);
                    }
                    else {
                        bound = MathHelper.fastFloor(offset);
                        stop = (offset + inc <= bound);
                    }
                    if (!stop) {
                        return false;
                    }
                    Digit.this.setListOffset(bound);
                    ProcessScheduler.getInstance().remove(Digit.this.m_scrollRunnable);
                    Digit.this.m_scrollRunnable = null;
                    Digit.this.m_currentIndex = targetedIndex;
                    if (RollingText.this.m_digitLockedIndex == Digit.this.m_digitIndex && Digit.this.m_targetDigit == Digit.this.m_currentIndex) {
                        RollingText.this.m_digitLockedIndex++;
                        if (Digit.this.m_digitIndex == RollingText.this.m_nbOfDigits - 1) {
                            RollingText.this.onEnd();
                        }
                        return true;
                    }
                    if (Digit.this.m_nextDigit != null && Digit.this.m_currentIndex == RollingText.NUMBERS.length) {
                        if (RollingText.this.m_digitLockedIndex == Digit.this.m_digitIndex) {
                            Digit.this.scrollToTarget();
                        }
                        else {
                            Digit.this.makeFullRoll();
                            if (RollingText.this.m_digitLockedIndex == Digit.this.m_nextDigit.getDigitIndex()) {
                                Digit.this.m_nextDigit.scrollToTarget();
                            }
                            else {
                                Digit.this.m_nextDigit.makeFullRoll();
                            }
                        }
                    }
                    return true;
                }
            };
            ProcessScheduler.getInstance().schedule(this.m_scrollRunnable, 25L);
        }
        
        public void scrollToTarget() {
            final float movement = this.m_targetDigit / (RollingText.this.m_cycleDuration / 25.0f);
            final int fullRoll = RollingText.NUMBERS.length;
            this.scrollTo(this.m_targetDigit, true, this.m_targetDigit, movement);
        }
        
        public void makeFullRoll() {
            final int fullRoll = RollingText.NUMBERS.length;
            final float movement = fullRoll / (RollingText.this.m_cycleDuration / 25.0f);
            this.scrollTo((short)fullRoll, true, fullRoll, movement);
        }
        
        private void setListOffset(final float offset) {
            this.m_list.setListOffset(offset);
        }
        
        public Byte getCurrentByteValue() {
            return this.m_currentByteValue;
        }
        
        public byte getTargetDigit() {
            return this.m_targetDigit;
        }
        
        public int getDigitIndex() {
            return this.m_digitIndex;
        }
        
        public void clean() {
            ProcessScheduler.getInstance().remove(this.m_scrollRunnable);
        }
        
        public List getList() {
            return this.m_list;
        }
    }
    
    public interface ValueChangedListener
    {
        void onValueChanged(int p0);
    }
    
    public interface RollEndListener
    {
        void onRollEnd();
    }
}
