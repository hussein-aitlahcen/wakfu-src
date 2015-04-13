package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class FormConverter implements Converter<FormValidateCallBack>
{
    private Class<FormValidateCallBack> TEMPLATE;
    
    public FormConverter() {
        super();
        this.TEMPLATE = FormValidateCallBack.class;
    }
    
    @Override
    public FormValidateCallBack convert(final String value) {
        return this.convert((Class<? extends FormValidateCallBack>)this.TEMPLATE, value);
    }
    
    @Override
    public FormValidateCallBack convert(final Class<? extends FormValidateCallBack> type, final String func) {
        return this.convert(type, func, (ElementMap)null);
    }
    
    @Override
    public FormValidateCallBack convert(final Class<? extends FormValidateCallBack> type, final String func, final ElementMap map) {
        if (func == null) {
            return null;
        }
        if (type.equals(FormValidateCallBack.class)) {
            final FormValidateCallBack validate = new FormValidateCallBack();
            final Environment env = Xulor.getInstance().getEnvironment();
            final ElementMap currentElementMap = env.getCurrentElementMap();
            final Form currentForm = env.getCurrentForm();
            validate.setFunc(func, currentElementMap, currentForm);
            return validate;
        }
        return null;
    }
    
    @Override
    public Class<FormValidateCallBack> convertsTo() {
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
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends FormValidateCallBack> type, final String func, final Environment env) {
        if (func == null) {
            return null;
        }
        if (type.equals(FormValidateCallBack.class)) {
            final String varName = doc.getUnusedVarName();
            doc.addImport(type);
            doc.addImport(Environment.class);
            doc.addGeneratedCommandLine(new ClassVariable(type, varName, "new " + type.getSimpleName() + "()"));
            doc.addGeneratedCommandLine(new ClassMethodCall(null, "setFunc", varName, new String[] { "\"" + func + "\"", "env.getCurrentElementMap()", "env.getCurrentForm()" }));
            return varName;
        }
        return null;
    }
}
