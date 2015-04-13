package cern.jet.math;

public class Polynomial extends Constants
{
    public static double p1evl(final double n, final double[] array, final int n2) throws ArithmeticException {
        double n3 = n + array[0];
        for (int i = 1; i < n2; ++i) {
            n3 = n3 * n + array[i];
        }
        return n3;
    }
    
    public static double polevl(final double n, final double[] array, final int n2) throws ArithmeticException {
        double n3 = array[0];
        for (int i = 1; i <= n2; ++i) {
            n3 = n3 * n + array[i];
        }
        return n3;
    }
}
