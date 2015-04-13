package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class DropValidateConverter implements Converter<DropValidateCallBack>
{
    private Class<DropValidateCallBack> TEMPLATE;
    
    public DropValidateConverter() {
        super();
        this.TEMPLATE = DropValidateCallBack.class;
    }
    
    @Override
    public DropValidateCallBack convert(final String value) {
        return this.convert((Class<? extends DropValidateCallBack>)this.TEMPLATE, value);
    }
    
    @Override
    public DropValidateCallBack convert(final Class<? extends DropValidateCallBack> type, final String func) {
        return this.convert(type, func, (ElementMap)null);
    }
    
    @Override
    public DropValidateCallBack convert(final Class<? extends DropValidateCallBack> type, final String func, final ElementMap map) {
        if (func == null) {
            return null;
        }
        if (type.equals(DropValidateCallBack.class)) {
            final DropValidateCallBack validate = new DropValidateCallBack();
            final ElementMap currentElementMap = Xulor.getInstance().getEnvironment().getCurrentElementMap();
            validate.setFunc(func, currentElementMap);
            return validate;
        }
        return null;
    }
    
    @Override
    public Class<DropValidateCallBack> convertsTo() {
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
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends DropValidateCallBack> type, final String func, final Environment env) {
        if (func == null) {
            return "null";
        }
        if (type.equals(DropValidateCallBack.class)) {
            doc.addImport(DropValidateCallBack.class);
            doc.addImport(Environment.class);
            final String dvc = doc.getUnusedVarName();
            doc.addGeneratedCommandLine(new ClassVariable(DropValidateCallBack.class, dvc, "new DropValidateCallBack()"));
            doc.addGeneratedCommandLine(new ClassMethodCall(null, "setFunc", dvc, new String[] { "\"" + func + "\"", "env.getCurrentElementMap()" }));
            return dvc;
        }
        return "null";
    }
}
