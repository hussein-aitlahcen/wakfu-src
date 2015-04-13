package com.ankamagames.framework.kernel.core.maths.equations;

public class SplineParser
{
    private static final String PROPERTY_SEPARATOR = ";";
    private static final String VALUE_SEPARATOR = ":";
    
    public static String toString(final Spline spline) {
        final StringBuilder sb = new StringBuilder();
        writeName(sb, spline);
        writeBounds(sb, spline);
        for (int i = 0; i < spline.size(); ++i) {
            writePoint(sb, spline.get(i));
        }
        return sb.toString();
    }
    
    public static Spline fromText(final String paramsString) {
        final Spline spline = new Spline("");
        return fromText(spline, paramsString) ? spline : null;
    }
    
    public static boolean fromText(final Spline spline, final String paramsString) {
        if (paramsString == null) {
            spline.reset();
            return false;
        }
        spline.clear();
        try {
            final String[] arr$;
            final String[] properties = arr$ = paramsString.split(";");
            for (final String prop : arr$) {
                final String[] p = prop.split(":");
                setValueFromString(spline, p[0], p[1]);
            }
            spline.sort();
        }
        catch (Exception ex) {
            spline.reset();
        }
        return true;
    }
    
    private static void setValueFromString(final Spline spline, final String name, final String value) {
        if (name.equals("bounds")) {
            readBounds(spline, value);
        }
        else if (name.equals("point")) {
            readPoint(spline, value);
        }
        else if (name.equals("name")) {
            spline.setName(value);
        }
    }
    
    private static void writeName(final StringBuilder sb, final Spline spline) {
        sb.append("name").append(":");
        sb.append(spline.getName());
        sb.append(";");
    }
    
    private static void writeBounds(final StringBuilder sb, final Spline spline) {
        sb.append("bounds").append(":");
        sb.append(spline.getBoundMinX()).append(",");
        sb.append(spline.getBoundMaxX()).append(",");
        sb.append(spline.getBoundMinY()).append(",");
        sb.append(spline.getBoundMaxY());
        sb.append(";");
    }
    
    private static void readBounds(final Spline spline, final String value) {
        final String[] v = value.split(",");
        final double minX = Double.parseDouble(v[0]);
        final double maxX = Double.parseDouble(v[1]);
        final double minY = Double.parseDouble(v[2]);
        final double maxY = Double.parseDouble(v[3]);
        spline.setValueBounds(minX, maxX, minY, maxY);
    }
    
    private static void writePoint(final StringBuilder sb, final ControlPoint pt) {
        sb.append("point").append(":");
        sb.append(pt.getX()).append(",").append(pt.getY());
        if (pt.hasInTangent()) {
            sb.append(",").append("i").append(",").append(pt.getInX()).append(",").append(pt.getInY());
        }
        if (pt.hasOutTangent()) {
            sb.append(",").append("o").append(",").append(pt.getOutX()).append(",").append(pt.getOutY());
        }
        sb.append(";");
    }
    
    private static void readPoint(final Spline spline, final String value) {
        final String[] v = value.split(",");
        final double x = Double.parseDouble(v[0]);
        final double y = Double.parseDouble(v[1]);
        final ControlPoint c = new ControlPoint(x, y);
        if (v.length > 2) {
            readTangent(c, v, 2);
        }
        if (v.length > 5) {
            readTangent(c, v, 5);
        }
        spline.addPoint(c);
    }
    
    private static void readTangent(final ControlPoint c, final String[] v, final int index) {
        final double x = Double.parseDouble(v[index + 1]);
        final double y = Double.parseDouble(v[index + 2]);
        if (v[index].equals("i")) {
            c.setInTangent(x, y);
            return;
        }
        if (v[index].equals("o")) {
            c.setOutTangent(x, y);
        }
    }
}
