package com.ankamagames.xulor2.core.form;

import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;

public class FormValidateCallBack extends CallBack
{
    private Form m_form;
    
    public FormValidateCallBack() {
        super();
        this.m_form = null;
    }
    
    public void setFunc(final String func, final ElementMap elementMap, final Form form) {
        this.m_form = form;
        this.setCallBackFunc(func, elementMap);
    }
    
    @Override
    protected void fillParameters(final String[] parameters, final List<Class<?>> parameterTypes, final List<Object> args) {
        parameterTypes.add(Form.class);
        args.add(this.m_form);
        super.fillParameters(parameters, parameterTypes, args);
    }
    
    public void copyCallback(final FormValidateCallBack listener) {
        listener.setFunc(this.m_func, this.m_elementMap, this.m_form);
    }
    
    public FormValidateCallBack cloneListener() {
        final FormValidateCallBack cb = new FormValidateCallBack();
        this.copyCallback(cb);
        return cb;
    }
}
