package com.ankamagames.xulor2.appearance;

public enum WidgetShape
{
    RECTANGLE, 
    LOSANGE, 
    CIRCLE;
    
    public boolean insideInsets(int x, int y, final int width, final int height) {
        switch (this) {
            case RECTANGLE: {
                return x >= 0 && y >= 0 && x < width && y < height;
            }
            case LOSANGE: {
                final float h2 = height / 2.0f;
                final float h2w2x = h2 / (width / 2.0f) * x;
                return y >= -h2w2x + h2 && y >= h2w2x - h2 && y < h2w2x + h2 && y < -h2w2x + 3.0f * h2;
            }
            case CIRCLE: {
                x -= width / 2;
                y -= height / 2;
                final int r = Math.min(width, height) / 2;
                return x * x + y * y <= r * r;
            }
            default: {
                return true;
            }
        }
    }
    
    public int getOnScreenX(final int x, final int y, final int width, final int height) {
        switch (this) {
            case RECTANGLE: {
                if (x < 0) {
                    return 0;
                }
                if (x > width) {
                    return width;
                }
                return x;
            }
            case CIRCLE: {
                final int radius = (int)(Math.min(width / 2, height / 2) * 0.8f);
                final int factor = (x < width / 2) ? -1 : 1;
                if (x * x + y * y < radius * radius) {
                    return x;
                }
                return factor * (int)Math.sqrt(radius * radius * (1 / (y * y / (x * x) + 1)));
            }
            case LOSANGE: {
                final float w2 = width / 2.0f;
                final float h2 = height / 2.0f;
                final float a1 = (y - h2) / (x - w2);
                final float b1 = y - a1 * x;
                float a2;
                float b2;
                if (x < w2) {
                    if (y < h2) {
                        a2 = -h2 / w2;
                        b2 = h2;
                    }
                    else {
                        a2 = h2 / w2;
                        b2 = h2;
                    }
                }
                else if (y < h2) {
                    a2 = h2 / w2;
                    b2 = -h2;
                }
                else {
                    a2 = -h2 / w2;
                    b2 = 3.0f * h2;
                }
                return (int)((b2 - b1) / (a1 - a2));
            }
            default: {
                return x;
            }
        }
    }
    
    public int getOnScreenY(final int x, final int y, final int width, final int height) {
        switch (this) {
            case RECTANGLE: {
                if (y < 0) {
                    return 0;
                }
                if (y > height) {
                    return height;
                }
                return y;
            }
            case CIRCLE: {
                final int radius = (int)(Math.min(width / 2, height / 2) * 0.8f);
                final int factor = (y < height / 2) ? -1 : 1;
                if (x * x + y * y < radius * radius) {
                    return y;
                }
                return factor * (int)Math.sqrt(radius * radius * (1 / (x * x / (y * y) + 1)));
            }
            case LOSANGE: {
                final float w2 = width / 2.0f;
                final float h2 = height / 2.0f;
                final float a1 = (y - h2) / (x - w2);
                final float b1 = y - a1 * x;
                float a2;
                float b2;
                if (x < w2) {
                    if (y < h2) {
                        a2 = -h2 / w2;
                        b2 = h2;
                    }
                    else {
                        a2 = h2 / w2;
                        b2 = h2;
                    }
                }
                else if (y < h2) {
                    a2 = h2 / w2;
                    b2 = -h2;
                }
                else {
                    a2 = -h2 / w2;
                    b2 = 3.0f * h2;
                }
                final float finalX = (b2 - b1) / (a1 - a2);
                return (int)(a1 * finalX + b1);
            }
            default: {
                return y;
            }
        }
    }
    
    public static WidgetShape value(final String value) {
        final WidgetShape[] arr$;
        final WidgetShape[] values = arr$ = values();
        for (final WidgetShape a : arr$) {
            if (a.name().equals(value.toUpperCase())) {
                return a;
            }
        }
        return values[0];
    }
}
