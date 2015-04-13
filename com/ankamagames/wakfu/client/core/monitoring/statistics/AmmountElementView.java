package com.ankamagames.wakfu.client.core.monitoring.statistics;

public class AmmountElementView extends SimpleElementView
{
    private static final String SEPARATOR = " ";
    private static final byte SPLIT_REPETITION = 3;
    
    public AmmountElementView(final String name, final String key, final String prefix, final String suffix, final ColorController colorController) {
        super(name, key, prefix, suffix, colorController);
    }
    
    public AmmountElementView(final String name, final String key, final String prefix, final String suffix) {
        super(name, key, prefix, suffix);
    }
    
    public AmmountElementView(final String name, final String key, final ColorController colorController) {
        super(name, key, colorController);
    }
    
    public AmmountElementView(final String name, final String key) {
        super(name, key);
    }
    
    @Override
    public String getStringValue(final boolean formatted) {
        if (!(this.m_value instanceof Long)) {
            return super.getStringValue(formatted);
        }
        final String str = this.m_value.toString();
        StringBuffer bb = new StringBuffer(str);
        if (str.length() > 3) {
            for (int i = str.length() - 3; i > 0; i -= 3) {
                bb.insert(i, " ");
            }
        }
        if (this.m_prefix != null) {
            bb = new StringBuffer(this.m_prefix + " " + bb.toString());
        }
        if (this.m_suffix != null) {
            bb.append(" " + this.m_suffix);
        }
        return formatted ? this.format(bb.toString()) : bb.toString();
    }
}
