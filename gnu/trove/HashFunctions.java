package gnu.trove;

public final class HashFunctions
{
    public static int hash(final double value) {
        assert !Double.isNaN(value) : "Values of NaN are not supported.";
        final long bits = Double.doubleToLongBits(value);
        return (int)(bits ^ bits >>> 32);
    }
    
    public static int hash(final float value) {
        assert !Float.isNaN(value) : "Values of NaN are not supported.";
        return Float.floatToIntBits(value * 6.6360896E8f);
    }
    
    public static int hash(final int value) {
        return value * 31;
    }
    
    public static int hash(final long value) {
        return (int)(value ^ value >>> 32) * 31;
    }
    
    public static int hash(final Object object) {
        return (object == null) ? 0 : object.hashCode();
    }
    
    static int fastCeil(final float v) {
        int possible_result = (int)v;
        if (v - possible_result > 0.0f) {
            ++possible_result;
        }
        return possible_result;
    }
}
