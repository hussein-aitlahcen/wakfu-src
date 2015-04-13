package org.apache.tools.ant.types.selectors;

import java.io.*;
import org.apache.tools.ant.types.*;

public class SizeSelector extends BaseExtendSelector
{
    private static final int KILO = 1000;
    private static final int KIBI = 1024;
    private static final int KIBI_POS = 4;
    private static final int MEGA = 1000000;
    private static final int MEGA_POS = 9;
    private static final int MEBI = 1048576;
    private static final int MEBI_POS = 13;
    private static final long GIGA = 1000000000L;
    private static final int GIGA_POS = 18;
    private static final long GIBI = 1073741824L;
    private static final int GIBI_POS = 22;
    private static final long TERA = 1000000000000L;
    private static final int TERA_POS = 27;
    private static final long TEBI = 1099511627776L;
    private static final int TEBI_POS = 31;
    private static final int END_POS = 36;
    public static final String SIZE_KEY = "value";
    public static final String UNITS_KEY = "units";
    public static final String WHEN_KEY = "when";
    private long size;
    private long multiplier;
    private long sizelimit;
    private Comparison when;
    
    public SizeSelector() {
        super();
        this.size = -1L;
        this.multiplier = 1L;
        this.sizelimit = -1L;
        this.when = Comparison.EQUAL;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{sizeselector value: ");
        buf.append(this.sizelimit);
        buf.append("compare: ").append(this.when.getValue());
        buf.append("}");
        return buf.toString();
    }
    
    public void setValue(final long size) {
        this.size = size;
        if (this.multiplier != 0L && size > -1L) {
            this.sizelimit = size * this.multiplier;
        }
    }
    
    public void setUnits(final ByteUnits units) {
        final int i = units.getIndex();
        this.multiplier = 0L;
        if (i > -1 && i < 4) {
            this.multiplier = 1000L;
        }
        else if (i < 9) {
            this.multiplier = 1024L;
        }
        else if (i < 13) {
            this.multiplier = 1000000L;
        }
        else if (i < 18) {
            this.multiplier = 1048576L;
        }
        else if (i < 22) {
            this.multiplier = 1000000000L;
        }
        else if (i < 27) {
            this.multiplier = 1073741824L;
        }
        else if (i < 31) {
            this.multiplier = 1000000000000L;
        }
        else if (i < 36) {
            this.multiplier = 1099511627776L;
        }
        if (this.multiplier > 0L && this.size > -1L) {
            this.sizelimit = this.size * this.multiplier;
        }
    }
    
    public void setWhen(final SizeComparisons when) {
        this.when = when;
    }
    
    public void setParameters(final Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                final String paramname = parameters[i].getName();
                if ("value".equalsIgnoreCase(paramname)) {
                    try {
                        this.setValue(Long.parseLong(parameters[i].getValue()));
                    }
                    catch (NumberFormatException nfe) {
                        this.setError("Invalid size setting " + parameters[i].getValue());
                    }
                }
                else if ("units".equalsIgnoreCase(paramname)) {
                    final ByteUnits units = new ByteUnits();
                    units.setValue(parameters[i].getValue());
                    this.setUnits(units);
                }
                else if ("when".equalsIgnoreCase(paramname)) {
                    final SizeComparisons scmp = new SizeComparisons();
                    scmp.setValue(parameters[i].getValue());
                    this.setWhen(scmp);
                }
                else {
                    this.setError("Invalid parameter " + paramname);
                }
            }
        }
    }
    
    public void verifySettings() {
        if (this.size < 0L) {
            this.setError("The value attribute is required, and must be positive");
        }
        else if (this.multiplier < 1L) {
            this.setError("Invalid Units supplied, must be K,Ki,M,Mi,G,Gi,T,or Ti");
        }
        else if (this.sizelimit < 0L) {
            this.setError("Internal error: Code is not setting sizelimit correctly");
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        this.validate();
        if (file.isDirectory()) {
            return true;
        }
        final long diff = file.length() - this.sizelimit;
        return this.when.evaluate((diff == 0L) ? 0 : ((int)(diff / Math.abs(diff))));
    }
    
    public static class ByteUnits extends EnumeratedAttribute
    {
        public String[] getValues() {
            return new String[] { "K", "k", "kilo", "KILO", "Ki", "KI", "ki", "kibi", "KIBI", "M", "m", "mega", "MEGA", "Mi", "MI", "mi", "mebi", "MEBI", "G", "g", "giga", "GIGA", "Gi", "GI", "gi", "gibi", "GIBI", "T", "t", "tera", "TERA", "Ti", "TI", "ti", "tebi", "TEBI" };
        }
    }
    
    public static class SizeComparisons extends Comparison
    {
    }
}
