package cern.jet.stat;

import cern.jet.math.*;

public class Gamma extends Constants
{
    public static double beta(final double n, final double n2) throws ArithmeticException {
        final double gamma = gamma(n + n2);
        if (gamma == 0.0) {
            return 1.0;
        }
        double n3;
        if (n > n2) {
            n3 = gamma(n) / gamma * gamma(n2);
        }
        else {
            n3 = gamma(n2) / gamma * gamma(n);
        }
        return n3;
    }
    
    public static double gamma(double a) throws ArithmeticException {
        final double[] array = { 1.6011952247675185E-4, 0.0011913514700658638, 0.010421379756176158, 0.04763678004571372, 0.20744822764843598, 0.4942148268014971, 1.0 };
        final double[] array2 = { -2.3158187332412014E-5, 5.396055804933034E-4, -0.004456419138517973, 0.011813978522206043, 0.035823639860549865, -0.23459179571824335, 0.0714304917030273, 1.0 };
        final double abs = Math.abs(a);
        if (abs > 33.0) {
            if (a >= 0.0) {
                return stirlingFormula(a);
            }
            final double floor = Math.floor(abs);
            if (floor == abs) {
                throw new ArithmeticException("gamma: overflow");
            }
            final int n = (int)floor;
            double n2 = abs - floor;
            if (n2 > 0.5) {
                n2 = abs - (floor + 1.0);
            }
            final double a2 = abs * Math.sin(3.141592653589793 * n2);
            if (a2 == 0.0) {
                throw new ArithmeticException("gamma: overflow");
            }
            return -(3.141592653589793 / (Math.abs(a2) * stirlingFormula(abs)));
        }
        else {
            double n3;
            for (n3 = 1.0; a >= 3.0; --a, n3 *= a) {}
            while (a < 0.0) {
                if (a == 0.0) {
                    throw new ArithmeticException("gamma: singular");
                }
                if (a > -1.0E-9) {
                    return n3 / ((1.0 + 0.5772156649015329 * a) * a);
                }
                n3 /= a;
                ++a;
            }
            while (a < 2.0) {
                if (a == 0.0) {
                    throw new ArithmeticException("gamma: singular");
                }
                if (a < 1.0E-9) {
                    return n3 / ((1.0 + 0.5772156649015329 * a) * a);
                }
                n3 /= a;
                ++a;
            }
            if (a == 2.0 || a == 3.0) {
                return n3;
            }
            a -= 2.0;
            return n3 * Polynomial.polevl(a, array, 6) / Polynomial.polevl(a, array2, 7);
        }
    }
    
    public static double incompleteBeta(final double n, final double n2, final double n3) throws ArithmeticException {
        if (n <= 0.0 || n2 <= 0.0) {
            throw new ArithmeticException("ibeta: Domain error!");
        }
        if (n3 <= 0.0 || n3 >= 1.0) {
            if (n3 == 0.0) {
                return 0.0;
            }
            if (n3 == 1.0) {
                return 1.0;
            }
            throw new ArithmeticException("ibeta: Domain error!");
        }
        else {
            boolean b = false;
            if (n2 * n3 <= 1.0 && n3 <= 0.95) {
                return powerSeries(n, n2, n3);
            }
            final double n4 = 1.0 - n3;
            double b2;
            double b3;
            double n5;
            double n6;
            if (n3 > n / (n + n2)) {
                b = true;
                b2 = n2;
                b3 = n;
                n5 = n3;
                n6 = n4;
            }
            else {
                b2 = n;
                b3 = n2;
                n5 = n4;
                n6 = n3;
            }
            if (b && b3 * n6 <= 1.0 && n6 <= 0.95) {
                final double powerSeries = powerSeries(b2, b3, n6);
                double n7;
                if (powerSeries <= 1.1102230246251565E-16) {
                    n7 = 0.9999999999999999;
                }
                else {
                    n7 = 1.0 - powerSeries;
                }
                return n7;
            }
            double incompleteBetaFraction1;
            if (n6 * (b2 + b3 - 2.0) - (b2 - 1.0) < 0.0) {
                incompleteBetaFraction1 = incompleteBetaFraction1(b2, b3, n6);
            }
            else {
                incompleteBetaFraction1 = incompleteBetaFraction2(b2, b3, n6) / n5;
            }
            final double a = b2 * Math.log(n6);
            final double a2 = b3 * Math.log(n5);
            if (b2 + b3 < 171.6243769563027 && Math.abs(a) < 709.782712893384 && Math.abs(a2) < 709.782712893384) {
                double n8 = Math.pow(n5, b3) * Math.pow(n6, b2) / b2 * incompleteBetaFraction1 * (gamma(b2 + b3) / (gamma(b2) * gamma(b3)));
                if (b) {
                    if (n8 <= 1.1102230246251565E-16) {
                        n8 = 0.9999999999999999;
                    }
                    else {
                        n8 = 1.0 - n8;
                    }
                }
                return n8;
            }
            final double a3 = a + (a2 + logGamma(b2 + b3) - logGamma(b2) - logGamma(b3)) + Math.log(incompleteBetaFraction1 / b2);
            double exp;
            if (a3 < -745.1332191019412) {
                exp = 0.0;
            }
            else {
                exp = Math.exp(a3);
            }
            if (b) {
                if (exp <= 1.1102230246251565E-16) {
                    exp = 0.9999999999999999;
                }
                else {
                    exp = 1.0 - exp;
                }
            }
            return exp;
        }
    }
    
    static double incompleteBetaFraction1(final double n, final double n2, final double n3) throws ArithmeticException {
        double n4 = n;
        double n5 = n + n2;
        double n6 = n;
        double n7 = n + 1.0;
        double n8 = 1.0;
        double n9 = n2 - 1.0;
        double n10 = n7;
        double n11 = n + 2.0;
        double n12 = 0.0;
        double n13 = 1.0;
        double n14 = 1.0;
        double n15 = 1.0;
        double n16 = 1.0;
        double n17 = 1.0;
        int n18 = 0;
        final double n19 = 3.3306690738754696E-16;
        do {
            final double n20 = -(n3 * n4 * n5) / (n6 * n7);
            final double n21 = n14 + n12 * n20;
            final double n22 = n15 + n13 * n20;
            final double n23 = n14;
            final double n24 = n21;
            final double n25 = n15;
            final double n26 = n22;
            final double n27 = n3 * n8 * n9 / (n10 * n11);
            final double n28 = n24 + n23 * n27;
            final double n29 = n26 + n25 * n27;
            n12 = n24;
            n14 = n28;
            n13 = n26;
            n15 = n29;
            if (n29 != 0.0) {
                n17 = n28 / n29;
            }
            double abs;
            if (n17 != 0.0) {
                abs = Math.abs((n16 - n17) / n17);
                n16 = n17;
            }
            else {
                abs = 1.0;
            }
            if (abs < n19) {
                return n16;
            }
            ++n4;
            ++n5;
            n6 += 2.0;
            n7 += 2.0;
            ++n8;
            --n9;
            n10 += 2.0;
            n11 += 2.0;
            if (Math.abs(n29) + Math.abs(n28) > 4.503599627370496E15) {
                n12 *= 2.220446049250313E-16;
                n14 *= 2.220446049250313E-16;
                n13 *= 2.220446049250313E-16;
                n15 *= 2.220446049250313E-16;
            }
            if (Math.abs(n29) >= 2.220446049250313E-16 && Math.abs(n28) >= 2.220446049250313E-16) {
                continue;
            }
            n12 *= 4.503599627370496E15;
            n14 *= 4.503599627370496E15;
            n13 *= 4.503599627370496E15;
            n15 *= 4.503599627370496E15;
        } while (++n18 < 300);
        return n16;
    }
    
    static double incompleteBetaFraction2(final double n, final double n2, final double n3) throws ArithmeticException {
        double n4 = n;
        double n5 = n2 - 1.0;
        double n6 = n;
        double n7 = n + 1.0;
        double n8 = 1.0;
        double n9 = n + n2;
        double n10 = n + 1.0;
        double n11 = n + 2.0;
        double n12 = 0.0;
        double n13 = 1.0;
        double n14 = 1.0;
        double n15 = 1.0;
        final double n16 = n3 / (1.0 - n3);
        double n17 = 1.0;
        double n18 = 1.0;
        int n19 = 0;
        final double n20 = 3.3306690738754696E-16;
        do {
            final double n21 = -(n16 * n4 * n5) / (n6 * n7);
            final double n22 = n14 + n12 * n21;
            final double n23 = n15 + n13 * n21;
            final double n24 = n14;
            final double n25 = n22;
            final double n26 = n15;
            final double n27 = n23;
            final double n28 = n16 * n8 * n9 / (n10 * n11);
            final double n29 = n25 + n24 * n28;
            final double n30 = n27 + n26 * n28;
            n12 = n25;
            n14 = n29;
            n13 = n27;
            n15 = n30;
            if (n30 != 0.0) {
                n18 = n29 / n30;
            }
            double abs;
            if (n18 != 0.0) {
                abs = Math.abs((n17 - n18) / n18);
                n17 = n18;
            }
            else {
                abs = 1.0;
            }
            if (abs < n20) {
                return n17;
            }
            ++n4;
            --n5;
            n6 += 2.0;
            n7 += 2.0;
            ++n8;
            ++n9;
            n10 += 2.0;
            n11 += 2.0;
            if (Math.abs(n30) + Math.abs(n29) > 4.503599627370496E15) {
                n12 *= 2.220446049250313E-16;
                n14 *= 2.220446049250313E-16;
                n13 *= 2.220446049250313E-16;
                n15 *= 2.220446049250313E-16;
            }
            if (Math.abs(n30) >= 2.220446049250313E-16 && Math.abs(n29) >= 2.220446049250313E-16) {
                continue;
            }
            n12 *= 4.503599627370496E15;
            n14 *= 4.503599627370496E15;
            n13 *= 4.503599627370496E15;
            n15 *= 4.503599627370496E15;
        } while (++n19 < 300);
        return n17;
    }
    
    public static double incompleteGamma(final double n, final double a) throws ArithmeticException {
        if (a <= 0.0 || n <= 0.0) {
            return 0.0;
        }
        if (a > 1.0 && a > n) {
            return 1.0 - incompleteGammaComplement(n, a);
        }
        final double a2 = n * Math.log(a) - a - logGamma(n);
        if (a2 < -709.782712893384) {
            return 0.0;
        }
        final double exp = Math.exp(a2);
        double n2 = n;
        double n3 = 1.0;
        double n4 = 1.0;
        do {
            ++n2;
            n3 *= a / n2;
            n4 += n3;
        } while (n3 / n4 > 1.1102230246251565E-16);
        return n4 * exp / n;
    }
    
    public static double incompleteGammaComplement(final double n, final double a) throws ArithmeticException {
        if (a <= 0.0 || n <= 0.0) {
            return 1.0;
        }
        if (a < 1.0 || a < n) {
            return 1.0 - incompleteGamma(n, a);
        }
        final double a2 = n * Math.log(a) - a - logGamma(n);
        if (a2 < -709.782712893384) {
            return 0.0;
        }
        final double exp = Math.exp(a2);
        double n2 = 1.0 - n;
        double n3 = a + n2 + 1.0;
        double n4 = 0.0;
        double n5 = 1.0;
        double n6 = a;
        double n7 = a + 1.0;
        double n8 = n3 * a;
        double n9 = n7 / n8;
        double abs;
        do {
            ++n4;
            ++n2;
            n3 += 2.0;
            final double n10 = n2 * n4;
            final double a3 = n7 * n3 - n5 * n10;
            final double n11 = n8 * n3 - n6 * n10;
            if (n11 != 0.0) {
                final double n12 = a3 / n11;
                abs = Math.abs((n9 - n12) / n12);
                n9 = n12;
            }
            else {
                abs = 1.0;
            }
            n5 = n7;
            n7 = a3;
            n6 = n8;
            n8 = n11;
            if (Math.abs(a3) > 4.503599627370496E15) {
                n5 *= 2.220446049250313E-16;
                n7 *= 2.220446049250313E-16;
                n6 *= 2.220446049250313E-16;
                n8 *= 2.220446049250313E-16;
            }
        } while (abs > 1.1102230246251565E-16);
        return n9 * exp;
    }
    
    public static double logGamma(double a) throws ArithmeticException {
        final double[] array = { 8.116141674705085E-4, -5.950619042843014E-4, 7.936503404577169E-4, -0.002777777777300997, 0.08333333333333319 };
        final double[] array2 = { -1378.2515256912086, -38801.631513463784, -331612.9927388712, -1162370.974927623, -1721737.0082083966, -853555.6642457654 };
        final double[] array3 = { -351.81570143652345, -17064.210665188115, -220528.59055385445, -1139334.4436798252, -2532523.0717758294, -2018891.4143353277 };
        if (a < -34.0) {
            final double a2 = -a;
            final double logGamma = logGamma(a2);
            final double floor = Math.floor(a2);
            if (floor == a2) {
                throw new ArithmeticException("lgam: Overflow");
            }
            double n = a2 - floor;
            if (n > 0.5) {
                n = floor + 1.0 - a2;
            }
            final double a3 = a2 * Math.sin(3.141592653589793 * n);
            if (a3 == 0.0) {
                throw new ArithmeticException("lgamma: Overflow");
            }
            return 1.1447298858494002 - Math.log(a3) - logGamma;
        }
        else if (a < 13.0) {
            double n2;
            for (n2 = 1.0; a >= 3.0; --a, n2 *= a) {}
            while (a < 2.0) {
                if (a == 0.0) {
                    throw new ArithmeticException("lgamma: Overflow");
                }
                n2 /= a;
                ++a;
            }
            if (n2 < 0.0) {
                n2 = -n2;
            }
            if (a == 2.0) {
                return Math.log(n2);
            }
            a -= 2.0;
            return Math.log(n2) + a * Polynomial.polevl(a, array2, 5) / Polynomial.p1evl(a, array3, 6);
        }
        else {
            if (a > 2.556348E305) {
                throw new ArithmeticException("lgamma: Overflow");
            }
            final double n3 = (a - 0.5) * Math.log(a) - a + 0.9189385332046728;
            if (a > 1.0E8) {
                return n3;
            }
            final double n4 = 1.0 / (a * a);
            double n5;
            if (a >= 1000.0) {
                n5 = n3 + ((7.936507936507937E-4 * n4 - 0.002777777777777778) * n4 + 0.08333333333333333) / a;
            }
            else {
                n5 = n3 + Polynomial.polevl(n4, array, 4) / a;
            }
            return n5;
        }
    }
    
    static double powerSeries(final double b, final double n, final double n2) throws ArithmeticException {
        final double n3 = 1.0 / b;
        final double n4 = (1.0 - n) * n2;
        double n5;
        double a;
        double n6;
        double n7;
        double n8;
        for (a = (n5 = n4 / (b + 1.0)), n6 = n4, n7 = 2.0, n8 = 0.0; Math.abs(a) > 1.1102230246251565E-16 * n3; a = n6 / (b + n7), n8 += a, ++n7) {
            n6 *= (n7 - n) * n2 / n7;
        }
        final double a2 = n8 + n5 + n3;
        final double a3 = b * Math.log(n2);
        double exp;
        if (b + n < 171.6243769563027 && Math.abs(a3) < 709.782712893384) {
            exp = a2 * (gamma(b + n) / (gamma(b) * gamma(n))) * Math.pow(n2, b);
        }
        else {
            final double a4 = logGamma(b + n) - logGamma(b) - logGamma(n) + a3 + Math.log(a2);
            if (a4 < -745.1332191019412) {
                exp = 0.0;
            }
            else {
                exp = Math.exp(a4);
            }
        }
        return exp;
    }
    
    static double stirlingFormula(final double a) throws ArithmeticException {
        final double[] array = { 7.873113957930937E-4, -2.2954996161337813E-4, -0.0026813261780578124, 0.0034722222160545866, 0.08333333333334822 };
        final double n = 143.01608;
        final double n2 = 1.0 / a;
        final double exp = Math.exp(a);
        final double n3 = 1.0 + n2 * Polynomial.polevl(n2, array, 4);
        double n4;
        if (a > n) {
            final double pow = Math.pow(a, 0.5 * a - 0.25);
            n4 = pow * (pow / exp);
        }
        else {
            n4 = Math.pow(a, a - 0.5) / exp;
        }
        return 2.5066282746310007 * n4 * n3;
    }
}
