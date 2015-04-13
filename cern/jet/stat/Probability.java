package cern.jet.stat;

import cern.jet.math.*;

public class Probability extends Constants
{
    protected static final double[] P0;
    protected static final double[] Q0;
    protected static final double[] P1;
    protected static final double[] Q1;
    protected static final double[] P2;
    protected static final double[] Q2;
    
    public static double beta(final double n, final double n2, final double n3) {
        return Gamma.incompleteBeta(n, n2, n3);
    }
    
    public static double betaComplemented(final double n, final double n2, final double n3) {
        return Gamma.incompleteBeta(n2, n, n3);
    }
    
    public static double binomial(final int n, final int n2, final double n3) {
        if (n3 < 0.0 || n3 > 1.0) {
            throw new IllegalArgumentException();
        }
        if (n < 0 || n2 < n) {
            throw new IllegalArgumentException();
        }
        if (n == n2) {
            return 1.0;
        }
        if (n == 0) {
            return Math.pow(1.0 - n3, n2 - n);
        }
        return Gamma.incompleteBeta(n2 - n, n + 1, 1.0 - n3);
    }
    
    public static double binomialComplemented(final int n, final int n2, final double n3) {
        if (n3 < 0.0 || n3 > 1.0) {
            throw new IllegalArgumentException();
        }
        if (n < 0 || n2 < n) {
            throw new IllegalArgumentException();
        }
        if (n == n2) {
            return 0.0;
        }
        if (n == 0) {
            return 1.0 - Math.pow(1.0 - n3, n2 - n);
        }
        return Gamma.incompleteBeta(n + 1, n2 - n, n3);
    }
    
    public static double chiSquare(final double n, final double n2) throws ArithmeticException {
        if (n2 < 0.0 || n < 1.0) {
            return 0.0;
        }
        return Gamma.incompleteGamma(n / 2.0, n2 / 2.0);
    }
    
    public static double chiSquareComplemented(final double n, final double n2) throws ArithmeticException {
        if (n2 < 0.0 || n < 1.0) {
            return 0.0;
        }
        return Gamma.incompleteGammaComplement(n / 2.0, n2 / 2.0);
    }
    
    public static double errorFunction(final double a) throws ArithmeticException {
        final double[] array = { 9.604973739870516, 90.02601972038427, 2232.005345946843, 7003.325141128051, 55592.30130103949 };
        final double[] array2 = { 33.56171416475031, 521.3579497801527, 4594.323829709801, 22629.000061389095, 49267.39426086359 };
        if (Math.abs(a) > 1.0) {
            return 1.0 - errorFunctionComplemented(a);
        }
        final double n = a * a;
        return a * Polynomial.polevl(n, array, 4) / Polynomial.p1evl(n, array2, 5);
    }
    
    public static double errorFunctionComplemented(final double n) throws ArithmeticException {
        final double[] array = { 2.461969814735305E-10, 0.5641895648310689, 7.463210564422699, 48.63719709856814, 196.5208329560771, 526.4451949954773, 934.5285271719576, 1027.5518868951572, 557.5353353693994 };
        final double[] array2 = { 13.228195115474499, 86.70721408859897, 354.9377788878199, 975.7085017432055, 1823.9091668790973, 2246.3376081871097, 1656.6630919416134, 557.5353408177277 };
        final double[] array3 = { 0.5641895835477551, 1.275366707599781, 5.019050422511805, 6.160210979930536, 7.4097426995044895, 2.9788666537210022 };
        final double[] array4 = { 2.2605286322011726, 9.396035249380015, 12.048953980809666, 17.08144507475659, 9.608968090632859, 3.369076451000815 };
        double n2;
        if (n < 0.0) {
            n2 = -n;
        }
        else {
            n2 = n;
        }
        if (n2 < 1.0) {
            return 1.0 - errorFunction(n);
        }
        final double a = -n * n;
        if (a < -709.782712893384) {
            if (n < 0.0) {
                return 2.0;
            }
            return 0.0;
        }
        else {
            final double exp = Math.exp(a);
            double n3;
            double n4;
            if (n2 < 8.0) {
                n3 = Polynomial.polevl(n2, array, 8);
                n4 = Polynomial.p1evl(n2, array2, 8);
            }
            else {
                n3 = Polynomial.polevl(n2, array3, 5);
                n4 = Polynomial.p1evl(n2, array4, 6);
            }
            double n5 = exp * n3 / n4;
            if (n < 0.0) {
                n5 = 2.0 - n5;
            }
            if (n5 != 0.0) {
                return n5;
            }
            if (n < 0.0) {
                return 2.0;
            }
            return 0.0;
        }
    }
    
    public static double gamma(final double n, final double n2, final double n3) {
        if (n3 < 0.0) {
            return 0.0;
        }
        return Gamma.incompleteGamma(n2, n * n3);
    }
    
    public static double gammaComplemented(final double n, final double n2, final double n3) {
        if (n3 < 0.0) {
            return 0.0;
        }
        return Gamma.incompleteGammaComplement(n2, n * n3);
    }
    
    public static double negativeBinomial(final int n, final int n2, final double n3) {
        if (n3 < 0.0 || n3 > 1.0) {
            throw new IllegalArgumentException();
        }
        if (n < 0) {
            return 0.0;
        }
        return Gamma.incompleteBeta(n2, n + 1, n3);
    }
    
    public static double negativeBinomialComplemented(final int n, final int n2, final double n3) {
        if (n3 < 0.0 || n3 > 1.0) {
            throw new IllegalArgumentException();
        }
        if (n < 0) {
            return 0.0;
        }
        return Gamma.incompleteBeta(n + 1, n2, 1.0 - n3);
    }
    
    public static double normal(final double n) throws ArithmeticException {
        final double a = n * 0.7071067811865476;
        final double abs = Math.abs(a);
        double n2;
        if (abs < 0.7071067811865476) {
            n2 = 0.5 + 0.5 * errorFunction(a);
        }
        else {
            n2 = 0.5 * errorFunctionComplemented(abs);
            if (a > 0.0) {
                n2 = 1.0 - n2;
            }
        }
        return n2;
    }
    
    public static double normal(final double n, final double n2, final double n3) throws ArithmeticException {
        if (n3 > 0.0) {
            return 0.5 + 0.5 * errorFunction((n3 - n) / Math.sqrt(2.0 * n2));
        }
        return 0.5 - 0.5 * errorFunction(-(n3 - n) / Math.sqrt(2.0 * n2));
    }
    
    public static double normalInverse(final double n) throws ArithmeticException {
        final double sqrt = Math.sqrt(6.283185307179586);
        if (n <= 0.0) {
            throw new IllegalArgumentException();
        }
        if (n >= 1.0) {
            throw new IllegalArgumentException();
        }
        boolean b = true;
        double a = n;
        if (a > 0.8646647167633873) {
            a = 1.0 - a;
            b = false;
        }
        if (a > 0.1353352832366127) {
            final double n2 = a - 0.5;
            final double n3 = n2 * n2;
            return (n2 + n2 * (n3 * Polynomial.polevl(n3, Probability.P0, 4) / Polynomial.p1evl(n3, Probability.Q0, 8))) * sqrt;
        }
        final double sqrt2 = Math.sqrt(-2.0 * Math.log(a));
        final double n4 = sqrt2 - Math.log(sqrt2) / sqrt2;
        final double n5 = 1.0 / sqrt2;
        double n6;
        if (sqrt2 < 8.0) {
            n6 = n5 * Polynomial.polevl(n5, Probability.P1, 8) / Polynomial.p1evl(n5, Probability.Q1, 8);
        }
        else {
            n6 = n5 * Polynomial.polevl(n5, Probability.P2, 8) / Polynomial.p1evl(n5, Probability.Q2, 8);
        }
        double n7 = n4 - n6;
        if (b) {
            n7 = -n7;
        }
        return n7;
    }
    
    public static double poisson(final int n, final double n2) throws ArithmeticException {
        if (n2 < 0.0) {
            throw new IllegalArgumentException();
        }
        if (n < 0) {
            return 0.0;
        }
        return Gamma.incompleteGammaComplement(n + 1, n2);
    }
    
    public static double poissonComplemented(final int n, final double n2) throws ArithmeticException {
        if (n2 < 0.0) {
            throw new IllegalArgumentException();
        }
        if (n < -1) {
            return 0.0;
        }
        return Gamma.incompleteGamma(n + 1, n2);
    }
    
    public static double studentT(final double n, final double n2) throws ArithmeticException {
        if (n <= 0.0) {
            throw new IllegalArgumentException();
        }
        if (n2 == 0.0) {
            return 0.5;
        }
        double n3 = 0.5 * Gamma.incompleteBeta(0.5 * n, 0.5, n / (n + n2 * n2));
        if (n2 >= 0.0) {
            n3 = 1.0 - n3;
        }
        return n3;
    }
    
    public static double studentTInverse(final double n, final int n2) {
        final double n3 = 1.0 - n / 2.0;
        double normalInverse = normalInverse(n3);
        if (n2 > 200) {
            return normalInverse;
        }
        double a = studentT(n2, normalInverse) - n3;
        double n4 = normalInverse;
        double a2;
        do {
            if (a > 0.0) {
                n4 /= 2.0;
            }
            else {
                n4 += normalInverse;
            }
            a2 = studentT(n2, n4) - n3;
        } while (a * a2 > 0.0);
        do {
            final double n5 = n4 - a2 / ((a2 - a) / (n4 - normalInverse));
            final double a3 = studentT(n2, n5) - n3;
            if (Math.abs(a3) < 1.0E-8) {
                return n5;
            }
            if (a3 * a2 < 0.0) {
                normalInverse = n4;
                a = a2;
                n4 = n5;
                a2 = a3;
            }
            else {
                a *= a2 / (a2 + a3);
                n4 = n5;
                a2 = a3;
            }
        } while (Math.abs(n4 - normalInverse) > 0.001);
        if (Math.abs(a2) <= Math.abs(a)) {
            return n4;
        }
        return normalInverse;
    }
    
    static {
        P0 = new double[] { -59.96335010141079, 98.00107541859997, -56.67628574690703, 13.931260938727968, -1.2391658386738125 };
        Q0 = new double[] { 1.9544885833814176, 4.676279128988815, 86.36024213908905, -225.46268785411937, 200.26021238006066, -82.03722561683334, 15.90562251262117, -1.1833162112133 };
        P1 = new double[] { 4.0554489230596245, 31.525109459989388, 57.16281922464213, 44.08050738932008, 14.684956192885803, 2.1866330685079025, -0.1402560791713545, -0.03504246268278482, -8.574567851546854E-4 };
        Q1 = new double[] { 15.779988325646675, 45.39076351288792, 41.3172038254672, 15.04253856929075, 2.504649462083094, -0.14218292285478779, -0.03808064076915783, -9.332594808954574E-4 };
        P2 = new double[] { 3.2377489177694603, 6.915228890689842, 3.9388102529247444, 1.3330346081580755, 0.20148538954917908, 0.012371663481782003, 3.0158155350823543E-4, 2.6580697468673755E-6, 6.239745391849833E-9 };
        Q2 = new double[] { 6.02427039364742, 3.6798356385616087, 1.3770209948908132, 0.21623699359449663, 0.013420400608854318, 3.2801446468212774E-4, 2.8924786474538068E-6, 6.790194080099813E-9 };
    }
}
