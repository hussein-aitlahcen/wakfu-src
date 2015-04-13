package org.jdom.output;

import java.lang.reflect.*;

public class Format implements Cloneable
{
    private static final String CVS_ID = "@(#) $RCSfile: Format.java,v $ $Revision: 1.10 $ $Date: 2004/09/07 06:37:20 $ $Name: jdom_1_0 $";
    private static final String STANDARD_INDENT = "  ";
    private static final String STANDARD_LINE_SEPARATOR = "\r\n";
    private static final String STANDARD_ENCODING = "UTF-8";
    String indent;
    String lineSeparator;
    String encoding;
    boolean omitDeclaration;
    boolean omitEncoding;
    boolean expandEmptyElements;
    boolean ignoreTrAXEscapingPIs;
    TextMode mode;
    EscapeStrategy escapeStrategy;
    static /* synthetic */ Class class$java$lang$String;
    
    private Format() {
        super();
        this.indent = null;
        this.lineSeparator = "\r\n";
        this.encoding = "UTF-8";
        this.omitDeclaration = false;
        this.omitEncoding = false;
        this.expandEmptyElements = false;
        this.ignoreTrAXEscapingPIs = false;
        this.mode = TextMode.PRESERVE;
        this.escapeStrategy = new DefaultEscapeStrategy(this.encoding);
    }
    
    static /* synthetic */ Class class$(final String class$) {
        try {
            return Class.forName(class$);
        }
        catch (ClassNotFoundException forName) {
            throw new NoClassDefFoundError(forName.getMessage());
        }
    }
    
    protected Object clone() {
        Format format = null;
        try {
            format = (Format)super.clone();
        }
        catch (CloneNotSupportedException ex) {}
        return format;
    }
    
    public static Format getCompactFormat() {
        final Format f = new Format();
        f.setTextMode(TextMode.NORMALIZE);
        return f;
    }
    
    public String getEncoding() {
        return this.encoding;
    }
    
    public EscapeStrategy getEscapeStrategy() {
        return this.escapeStrategy;
    }
    
    public boolean getExpandEmptyElements() {
        return this.expandEmptyElements;
    }
    
    public boolean getIgnoreTrAXEscapingPIs() {
        return this.ignoreTrAXEscapingPIs;
    }
    
    public String getIndent() {
        return this.indent;
    }
    
    public String getLineSeparator() {
        return this.lineSeparator;
    }
    
    public boolean getOmitDeclaration() {
        return this.omitDeclaration;
    }
    
    public boolean getOmitEncoding() {
        return this.omitEncoding;
    }
    
    public static Format getPrettyFormat() {
        final Format f = new Format();
        f.setIndent("  ");
        f.setTextMode(TextMode.TRIM);
        return f;
    }
    
    public static Format getRawFormat() {
        return new Format();
    }
    
    public TextMode getTextMode() {
        return this.mode;
    }
    
    public Format setEncoding(final String encoding) {
        this.encoding = encoding;
        this.escapeStrategy = new DefaultEscapeStrategy(encoding);
        return this;
    }
    
    public Format setEscapeStrategy(final EscapeStrategy strategy) {
        this.escapeStrategy = strategy;
        return this;
    }
    
    public Format setExpandEmptyElements(final boolean expandEmptyElements) {
        this.expandEmptyElements = expandEmptyElements;
        return this;
    }
    
    public void setIgnoreTrAXEscapingPIs(final boolean ignoreTrAXEscapingPIs) {
        this.ignoreTrAXEscapingPIs = ignoreTrAXEscapingPIs;
    }
    
    public Format setIndent(String indent) {
        if ("".equals(indent)) {
            indent = null;
        }
        this.indent = indent;
        return this;
    }
    
    public Format setLineSeparator(final String separator) {
        this.lineSeparator = separator;
        return this;
    }
    
    public Format setOmitDeclaration(final boolean omitDeclaration) {
        this.omitDeclaration = omitDeclaration;
        return this;
    }
    
    public Format setOmitEncoding(final boolean omitEncoding) {
        this.omitEncoding = omitEncoding;
        return this;
    }
    
    public Format setTextMode(final TextMode mode) {
        this.mode = mode;
        return this;
    }
    
    class DefaultEscapeStrategy implements EscapeStrategy
    {
        private int bits;
        Object encoder;
        Method canEncode;
        
        public DefaultEscapeStrategy(final String encoding) {
            super();
            if ("UTF-8".equalsIgnoreCase(encoding) || "UTF-16".equalsIgnoreCase(encoding)) {
                this.bits = 16;
            }
            else if ("ISO-8859-1".equalsIgnoreCase(encoding) || "Latin1".equalsIgnoreCase(encoding)) {
                this.bits = 8;
            }
            else if ("US-ASCII".equalsIgnoreCase(encoding) || "ASCII".equalsIgnoreCase(encoding)) {
                this.bits = 7;
            }
            else {
                this.bits = 0;
                try {
                    final Class charsetClass = Class.forName("java.nio.charset.Charset");
                    final Class encoderClass = Class.forName("java.nio.charset.CharsetEncoder");
                    final Method forName = charsetClass.getMethod("forName", (Format.class$java$lang$String != null) ? Format.class$java$lang$String : (Format.class$java$lang$String = Format.class$("java.lang.String")));
                    final Object charsetObj = forName.invoke(null, encoding);
                    final Method newEncoder = charsetClass.getMethod("newEncoder", (Class[])null);
                    this.encoder = newEncoder.invoke(charsetObj, (Object[])null);
                    this.canEncode = encoderClass.getMethod("canEncode", Character.TYPE);
                }
                catch (Exception ex) {}
            }
        }
        
        public boolean shouldEscape(final char ch) {
            if (this.bits == 16) {
                return false;
            }
            if (this.bits == 8) {
                return ch > '\u00ff';
            }
            if (this.bits == 7) {
                return ch > '\u007f';
            }
            if (this.canEncode != null && this.encoder != null) {
                try {
                    final Boolean val = (Boolean)this.canEncode.invoke(this.encoder, new Character(ch));
                    return val ^ true;
                }
                catch (Exception ex) {}
            }
            return false;
        }
    }
    
    public static class TextMode
    {
        public static final TextMode PRESERVE;
        public static final TextMode TRIM;
        public static final TextMode NORMALIZE;
        public static final TextMode TRIM_FULL_WHITE;
        private final String name;
        
        static {
            PRESERVE = new TextMode("PRESERVE");
            TRIM = new TextMode("TRIM");
            NORMALIZE = new TextMode("NORMALIZE");
            TRIM_FULL_WHITE = new TextMode("TRIM_FULL_WHITE");
        }
        
        private TextMode(final String name) {
            super();
            this.name = name;
        }
        
        public String toString() {
            return this.name;
        }
    }
}
