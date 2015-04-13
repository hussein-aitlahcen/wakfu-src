package com.ankamagames.xulor2.core.renderer.condition;

import java.util.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class ConditionResult extends NonGraphicalElement implements ResultProvider
{
    public static final String TAG = "Condition";
    private Condition m_condition;
    private Object m_value;
    private boolean m_valueInit;
    private Object m_elseValue;
    private boolean m_elseValueInit;
    private boolean m_returnOriginalValue;
    private final Collection<ConditionResult> m_conditionResult;
    protected ResultProviderParent m_resultProviderParent;
    public static final int VALUE_HASH;
    public static final int ELSE_VALUE_HASH;
    public static final int RETURN_ORIGINAL_VALUE_HASH;
    
    public ConditionResult() {
        super();
        this.m_value = true;
        this.m_elseValue = false;
        this.m_conditionResult = new ArrayList<ConditionResult>();
    }
    
    @Override
    public void add(final EventDispatcher element) {
        if (element instanceof Condition) {
            this.setCondition((Condition)element);
        }
        else if (element instanceof ConditionResult) {
            this.addConditionResult((ConditionResult)element);
        }
        super.add(element);
    }
    
    @Override
    public String getTag() {
        return "Condition";
    }
    
    @Override
    public Object getResult(final Object object) {
        final Object trueReturn = (this.m_valueInit || !this.m_returnOriginalValue) ? this.m_value : object;
        final Object falseReturn = (this.m_elseValueInit || !this.m_returnOriginalValue) ? this.m_elseValue : object;
        if (!this.m_conditionResult.isEmpty()) {
            for (final ConditionResult condition : this.m_conditionResult) {
                if (condition.m_condition.isValid(object)) {
                    return condition.isComposite() ? condition.getResult(object) : condition.m_value;
                }
            }
            return falseReturn;
        }
        if (this.m_condition == null) {
            ConditionResult.m_logger.warn((Object)"Condition sans tests : ");
            return trueReturn;
        }
        return this.m_condition.isValid(object) ? trueReturn : falseReturn;
    }
    
    public Condition getCondition() {
        return this.m_condition;
    }
    
    public void setCondition(final Condition condition) {
        this.m_condition = condition;
        if (this.m_condition != null) {
            this.m_condition.setConditionParent(this);
        }
    }
    
    public Object getElseValue() {
        return this.m_elseValue;
    }
    
    public void setElseValue(final Object false1) {
        this.m_elseValue = false1;
        this.m_elseValueInit = true;
    }
    
    public void setElseValue(final String false1) {
        this.m_elseValue = false1;
        this.m_elseValueInit = true;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object true1) {
        this.m_value = true1;
        this.m_valueInit = true;
    }
    
    public void setValue(final String true1) {
        this.m_value = true1;
        this.m_valueInit = true;
    }
    
    public boolean isReturnOriginalValue() {
        return this.m_returnOriginalValue;
    }
    
    public void setReturnOriginalValue(final boolean returnOriginalValue) {
        this.m_returnOriginalValue = returnOriginalValue;
    }
    
    public void fireConditionChanged(final boolean full) {
        if (full && this.m_resultProviderParent != null) {
            this.m_resultProviderParent.fireResultProviderChanged(this);
        }
        this.invalidateContentClient();
    }
    
    public void invalidateContentClient() {
        EventDispatcher parent;
        do {
            parent = this.getParent();
        } while (parent instanceof Condition || parent instanceof ConditionResult);
        if (parent instanceof ItemRenderer) {
            final ItemRenderer renderer = (ItemRenderer)parent;
            renderer.getManager().invalidateRenderers();
        }
    }
    
    @Override
    public void setResultProviderParent(final ResultProviderParent parent) {
        this.m_resultProviderParent = parent;
    }
    
    public void addConditionResult(final ConditionResult condition) {
        this.m_conditionResult.add(condition);
    }
    
    public boolean isComposite() {
        return !this.m_conditionResult.isEmpty();
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final ConditionResult e = (ConditionResult)source;
        super.copyElement(e);
        if (this.m_valueInit) {
            e.setValue(this.m_value);
        }
        if (this.m_elseValueInit) {
            e.setElseValue(this.m_elseValue);
        }
        e.m_returnOriginalValue = this.m_returnOriginalValue;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ConditionResult.ELSE_VALUE_HASH) {
            this.setElseValue(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == ConditionResult.VALUE_HASH) {
            this.setValue(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != ConditionResult.RETURN_ORIGINAL_VALUE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.m_returnOriginalValue = PrimitiveConverter.getBoolean(value);
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ConditionResult.ELSE_VALUE_HASH) {
            this.setElseValue(value);
        }
        else if (hash == ConditionResult.VALUE_HASH) {
            this.setValue(value);
        }
        else {
            if (hash != ConditionResult.RETURN_ORIGINAL_VALUE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.m_returnOriginalValue = PrimitiveConverter.getBoolean(value);
        }
        return true;
    }
    
    static {
        VALUE_HASH = "value".hashCode();
        ELSE_VALUE_HASH = "elseValue".hashCode();
        RETURN_ORIGINAL_VALUE_HASH = "returnOriginalValue".hashCode();
    }
}
