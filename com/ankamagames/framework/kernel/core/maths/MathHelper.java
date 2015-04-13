package com.ankamagames.framework.kernel.core.maths;

import java.util.*;
import java.util.zip.*;
import java.math.*;

public class MathHelper
{
    public static final float F_PI = 3.1415927f;
    public static final Random RANDOM;
    private static final CRC32 CRC_COMPUTER;
    public static final float EPSILON = 1.0E-5f;
    public static final float PI_TIMES2 = 6.2831855f;
    public static final float PI = 3.1415927f;
    public static final float PI_DIV2 = 1.5707964f;
    public static final float PI_DIV3 = 1.0471976f;
    public static final float PI_DIV4 = 0.7853982f;
    public static final float PI_DIV6 = 0.5235988f;
    public static final float PI_DIV8 = 0.3926991f;
    public static final float DEGREE_TO_RADIAN = 0.017453292f;
    public static final float RADIAN_TO_DEGREE = 57.29578f;
    public static final double LOG_2;
    public static final float SQRT_2;
    private static final float[] m_cosLUT;
    private static final float[] m_sinLUT;
    private static int m_sphericalRandomIndex;
    private static int m_zElipticalRandomIndex;
    private static final float[][] SPHERICAL_RANDOM;
    private static final float[][] Z_ELIPTICAL_RANDOM;
    
    public static void setSeed(final long seed) {
        MathHelper.RANDOM.setSeed(seed);
    }
    
    public static int random(final int value) {
        return MathHelper.RANDOM.nextInt(value);
    }
    
    public static int random() {
        return MathHelper.RANDOM.nextInt();
    }
    
    public static int randomInt31() {
        return MathHelper.RANDOM.nextInt() >> 1;
    }
    
    public static int randomRadius(final int pos, final int radius) {
        return pos - radius + MathHelper.RANDOM.nextInt(2 * radius);
    }
    
    public static void randomBytes(final byte[] bytesArray) {
        MathHelper.RANDOM.nextBytes(bytesArray);
    }
    
    public static boolean randomBoolean() {
        return MathHelper.RANDOM.nextBoolean();
    }
    
    public static float randomFloat() {
        return MathHelper.RANDOM.nextFloat();
    }
    
    public static double randomDouble() {
        return MathHelper.RANDOM.nextDouble();
    }
    
    public static int random(final int valueMin, final int valueMax) {
        if (valueMin == valueMax) {
            return valueMin;
        }
        return MathHelper.RANDOM.nextInt(valueMax - valueMin) + valueMin;
    }
    
    public static long random(final long valueMin, final long valueMax) {
        if (valueMin == valueMax) {
            return valueMin;
        }
        return Math.abs(MathHelper.RANDOM.nextLong() % (valueMax - valueMin)) + valueMin;
    }
    
    public static float random(final float valueMin, final float valueMax) {
        if (valueMin == valueMax) {
            return valueMin;
        }
        return MathHelper.RANDOM.nextFloat() * (valueMax - valueMin) + valueMin;
    }
    
    public static float lerp(final float value1, final float value2, final float amount) {
        return value1 + amount * (value2 - value1);
    }
    
    public static double lerp(final double value1, final double value2, final double amount) {
        return value1 + amount * (value2 - value1);
    }
    
    public static short clamp(final short value, final short min, final short max) {
        return (value <= min) ? min : ((value >= max) ? max : value);
    }
    
    public static int clamp(final int value, final int min, final int max) {
        return (value <= min) ? min : ((value >= max) ? max : value);
    }
    
    public static long clamp(final long value, final long min, final long max) {
        return (value <= min) ? min : ((value >= max) ? max : value);
    }
    
    public static float clamp(final float value, final float min, final float max) {
        return (value <= min) ? min : ((value >= max) ? max : value);
    }
    
    public static double clamp(final double value, final double min, final double max) {
        return (value <= min) ? min : ((value >= max) ? max : value);
    }
    
    public static float decimalPart(final float value) {
        final float abs = Math.abs(value);
        return Math.signum(value) * (abs - fastFloor(abs));
    }
    
    public static double decimalPart(final double value) {
        final double abs = Math.abs(value);
        return Math.signum(value) * (abs - fastFloor(abs));
    }
    
    public static float round(final float value, final int decimal) {
        final BigDecimal d = BigDecimal.valueOf(value);
        return d.round(new MathContext(d.precision() - d.scale() + decimal)).floatValue();
    }
    
    public static double round(final double value, final int decimal) {
        final BigDecimal d = BigDecimal.valueOf(value);
        return d.round(new MathContext(d.precision() - d.scale() + decimal)).doubleValue();
    }
    
    public static int fastRound(final float value) {
        return fastFloor(value + 0.5f);
    }
    
    public static long fastRound(final double value) {
        return fastFloor(value + 0.5);
    }
    
    public static int fastFloor(final float value) {
        final int v = (int)value;
        if (value >= 0.0f) {
            return v;
        }
        return (v == value) ? v : (v - 1);
    }
    
    public static int fastCeil(final float value) {
        final int v = (int)value;
        if (value <= 0.0f) {
            return v;
        }
        return (v == value) ? v : (v + 1);
    }
    
    public static long fastFloor(final double value) {
        final long v = (long)value;
        if (value >= 0.0) {
            return v;
        }
        return (v == value) ? v : (v - 1L);
    }
    
    public static long fastCeil(final double value) {
        final int v = (int)value;
        if (value <= 0.0) {
            return v;
        }
        return (v == value) ? v : ((long)(v + 1));
    }
    
    public static float maxFloat(final float a, final float b) {
        return (a >= b) ? a : b;
    }
    
    public static float minFloat(final float a, final float b) {
        return (a >= b) ? b : a;
    }
    
    public static byte minByte(final byte a, final byte b) {
        return (a >= b) ? b : a;
    }
    
    public static short minShort(final short a, final short b) {
        return (a >= b) ? b : a;
    }
    
    public static byte maxByte(final byte a, final byte b) {
        return (a >= b) ? a : b;
    }
    
    public static short maxShort(final short a, final short b) {
        return (a >= b) ? a : b;
    }
    
    public static byte clampByte(final byte value, final byte min, final byte max) {
        return maxByte(minByte(value, min), max);
    }
    
    public static byte ensureByte(final long value) {
        return (byte)clamp(value, -128L, 127L);
    }
    
    public static short ensureShort(final long value) {
        return (short)clamp(value, -32768L, 32767L);
    }
    
    public static short ensurePositiveShort(final long value) {
        return (short)clamp(value, 0L, 32767L);
    }
    
    public static int ensureInt(final long value) {
        return (int)clamp(value, -2147483648L, 2147483647L);
    }
    
    public static int ensurePositiveInt(final long value) {
        return (int)clamp(value, 0L, 2147483647L);
    }
    
    public static long getLongFromTwoInt(final int a, final int b) {
        final long la = a & 0xFFFFFFFFL;
        final long lb = b & 0xFFFFFFFFL;
        return la << 32 | lb;
    }
    
    public static int getFirstIntFromLong(final long value) {
        return (int)(value >> 32 & 0xFFFFFFFFL);
    }
    
    public static int getSecondIntFromLong(final long value) {
        return (int)(value & 0xFFFFFFFFL);
    }
    
    public static int getIntFromFourByte(final byte a, final byte b, final byte c, final byte d) {
        final int sa = a & 0xFF;
        final int sb = b & 0xFF;
        final int sc = c & 0xFF;
        final int sd = d & 0xFF;
        return sa << 8 | sb << 16 | sc << 24 | sd;
    }
    
    public static short getShortFromTwoBytes(final byte a, final byte b) {
        final short sa = (short)(a & 0xFF);
        final short sb = (short)(b & 0xFF);
        return (short)(sa << 8 | sb);
    }
    
    public static byte getFirstByteFromShort(final short value) {
        return (byte)(value >> 8 & 0xFF);
    }
    
    public static byte getSecondByteFromShort(final short value) {
        return (byte)(value & 0xFF);
    }
    
    public static int getIntFromTwoShort(final short a, final short b) {
        final int la = a & 0xFFFF;
        final int lb = b & 0xFFFF;
        return la << 16 | lb;
    }
    
    public static int getIntFromTwoInt(final int a, final int b) {
        assert a > -32768 && a < 32767;
        assert b > -32768 && b < 32767;
        return a << 16 | (b & 0xFFFF);
    }
    
    public static short getFirstShortFromInt(final int value) {
        return (short)(value >> 16 & 0xFFFF);
    }
    
    public static short getSecondShortFromInt(final int value) {
        return (short)(value & 0xFFFF);
    }
    
    public static boolean isEqual(final float f1, final float f2) {
        return isEqual(f1, f2, 1.0E-5f);
    }
    
    public static boolean isEqual(final float f1, final float f2, final float epsilon) {
        return Math.abs(f1 - f2) < epsilon;
    }
    
    public static boolean isZero(final float f) {
        return isEqual(f, 0.0f);
    }
    
    public static boolean isZero(final double f) {
        return isEqual(f, 0.0, 9.999999747378752E-6);
    }
    
    public static boolean isEqual(final double d1, final double d2, final double precision) {
        return fastFloor(d1 / precision) == fastFloor(d2 / precision);
    }
    
    public static float sin(final float angle) {
        return (float)Math.sin(angle);
    }
    
    public static float cos(final float angle) {
        return (float)Math.cos(angle);
    }
    
    public static float sinf(final float angle) {
        int a = (int)(angle * 57.29578f) % 360;
        if (a < 0) {
            a += 360;
        }
        return MathHelper.m_sinLUT[a];
    }
    
    public static float cosf(final float angle) {
        int a = (int)(angle * 57.29578f) % 360;
        if (a < 0) {
            a += 360;
        }
        return MathHelper.m_cosLUT[a];
    }
    
    public static float acos(final float angle) {
        return (float)Math.acos(angle);
    }
    
    public static float atan2(final float y, final float x) {
        return (float)Math.atan2(y, x);
    }
    
    public static float sqrt(final float a) {
        return (float)Math.sqrt(a);
    }
    
    public static int isqrt(int n) {
        int a;
        for (a = 0; n >= 2 * a + 1; n -= 2 * a++ + 1) {}
        return a;
    }
    
    public static int nearestGreatestPowOfTwo(int value) {
        assert value >= 0;
        if (value < 2) {
            return value;
        }
        value = (--value | value >> 1);
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        return ++value;
    }
    
    public static double pow2(final double value) {
        return value * value;
    }
    
    public static float pow2(final float value) {
        return value * value;
    }
    
    public static double pow3(final double value) {
        return value * value * value;
    }
    
    public static float pow3(final float value) {
        return value * value * value;
    }
    
    public static int pown(final int value, final int exp) {
        int res = 1;
        for (int i = 0; i < exp; ++i) {
            res *= value;
        }
        return res;
    }
    
    public static long pownL(final long value, final int exp) {
        long res = 1L;
        for (int i = 0; i < exp; ++i) {
            res *= value;
        }
        return res;
    }
    
    public static int log2i(final int value) {
        assert value > 0;
        int count;
        int c;
        for (count = 1, c = 0; value > count; count *= 2, ++c) {}
        return c;
    }
    
    public static float log2(final float value) {
        return (float)(Math.log(value) / MathHelper.LOG_2);
    }
    
    public static double log2(final double value) {
        return Math.log(value) / MathHelper.LOG_2;
    }
    
    public static int max(final int first, final int second, final int... others) {
        int max = Math.max(first, second);
        for (int i = 0, size = others.length; i < size; ++i) {
            max = Math.max(max, others[i]);
        }
        return max;
    }
    
    public static Random getRandomGenerator() {
        return MathHelper.RANDOM;
    }
    
    @Deprecated
    public static int getCRC(final String name) {
        MathHelper.CRC_COMPUTER.reset();
        MathHelper.CRC_COMPUTER.update(name.getBytes());
        return (int)MathHelper.CRC_COMPUTER.getValue();
    }
    
    public static float[] getNextSpericalRandom() {
        return MathHelper.SPHERICAL_RANDOM[++MathHelper.m_sphericalRandomIndex & 0xFF];
    }
    
    public static float[] getNextZElipticalRandom() {
        return MathHelper.Z_ELIPTICAL_RANDOM[++MathHelper.m_zElipticalRandomIndex & 0xFF];
    }
    
    public static boolean getBooleanAt(final long booleanArray, final int index) {
        assert index >= 0 && index < 64;
        return (booleanArray >> index & 0x1L) == 0x1L;
    }
    
    public static long setBooleanAt(long booleanArray, final int index, final boolean value) {
        assert index >= 0 && index < 64;
        if (value) {
            booleanArray |= 1L << index;
        }
        else {
            booleanArray &= ~(1L << index);
        }
        return booleanArray;
    }
    
    public static boolean getBooleanAt(final short booleanArray, final int index) {
        assert index >= 0 && index < 16;
        return (booleanArray >> index & 0x1) == 0x1;
    }
    
    public static short setBooleanAt(short booleanArray, final int index, final boolean value) {
        assert index >= 0 && index < 16;
        if (value) {
            booleanArray |= (short)(1 << index);
        }
        else {
            booleanArray &= (short)~(1 << index);
        }
        return booleanArray;
    }
    
    public static int compare(final long a, final long b) {
        if (a > b) {
            return 1;
        }
        if (a < b) {
            return -1;
        }
        return 0;
    }
    
    static {
        RANDOM = new MersenneTwister();
        CRC_COMPUTER = new CRC32();
        LOG_2 = Math.log(2.0);
        SQRT_2 = (float)Math.sqrt(2.0);
        m_cosLUT = new float[360];
        m_sinLUT = new float[360];
        for (int i = 0, size = MathHelper.m_cosLUT.length; i < size; ++i) {
            MathHelper.m_cosLUT[i] = (float)Math.cos(i * 3.141592653589793 / 180.0);
        }
        for (int i = 0, size = MathHelper.m_cosLUT.length; i < size; ++i) {
            MathHelper.m_sinLUT[i] = (float)Math.sin(i * 3.141592653589793 / 180.0);
        }
        SPHERICAL_RANDOM = new float[][] { { 0.5708353f, -0.6139014f, 0.5452267f }, { 0.4132552f, -0.46965155f, -0.7801587f }, { 0.6405728f, -0.5643295f, 0.5207675f }, { 0.47169217f, 0.61407936f, 0.6327821f }, { 0.15227868f, 0.121725954f, 0.9808129f }, { -0.87439466f, -0.34855837f, -0.33755156f }, { 0.77132064f, 0.6136505f, -0.1688119f }, { 0.4826271f, -0.8640989f, -0.14284301f }, { 0.49470016f, -0.7475342f, -0.44324282f }, { 0.6329491f, -0.18055533f, 0.75284475f }, { 0.4869739f, 0.5622945f, -0.6683422f }, { 0.2285417f, -0.07848456f, -0.97036535f }, { -0.22788522f, -0.7168149f, -0.65897256f }, { -0.6383118f, -0.5510635f, 0.53748214f }, { -0.6907991f, 0.6864441f, -0.2271368f }, { 0.0342741f, -0.9926095f, -0.11641126f }, { 0.27287376f, 0.41165859f, 0.869527f }, { 0.00990071f, 0.635923f, 0.77168894f }, { 0.5978979f, -0.67560804f, -0.43136048f }, { -0.9579213f, 0.28607544f, 0.023401625f }, { -0.86878103f, -0.35798943f, -0.3421446f }, { -0.62233365f, 0.2561255f, 0.73966247f }, { -0.3543662f, 0.6203524f, -0.6997053f }, { -0.86271113f, -0.5032229f, -0.04996087f }, { -0.4263197f, 0.88112336f, -0.2046293f }, { 0.58957165f, 0.7441359f, 0.31411308f }, { -0.9304057f, 0.36426166f, 0.040726334f }, { 0.88667125f, 0.043089554f, 0.46038827f }, { -0.26487175f, 0.62990123f, 0.7301146f }, { -0.31943563f, -0.17047592f, -0.93214744f }, { -0.5922897f, 0.48554268f, 0.642994f }, { 0.84751815f, 0.3683601f, 0.38213062f }, { -0.5880656f, 0.6549881f, 0.4745201f }, { 0.7625849f, 0.11474337f, -0.6366303f }, { 0.6694696f, -0.67933136f, 0.30053172f }, { -0.2873283f, -0.5944033f, 0.75108397f }, { -0.8043973f, -0.29633617f, 0.5149078f }, { -0.033203024f, 0.66501033f, 0.7460957f }, { -0.72020334f, -0.037909884f, 0.69272643f }, { 0.6309926f, -0.3906175f, -0.67027336f }, { 0.9063712f, -0.366167f, -0.21074362f }, { 0.8494861f, 0.39479378f, 0.35001588f }, { 0.9216346f, 0.3775673f, 0.08962431f }, { 0.2789247f, -0.8765322f, -0.392291f }, { 0.25420117f, -0.96065444f, 0.1119148f }, { 0.34705904f, -0.7478827f, 0.5658811f }, { -0.3868459f, 0.783865f, 0.48570144f }, { 0.3206616f, 0.70318407f, 0.634593f }, { -0.57327485f, 0.2966969f, -0.7637583f }, { 0.603305f, 0.78502536f, -0.14056426f }, { 0.7512555f, 0.36190385f, -0.55194277f }, { -0.65518194f, 0.61065596f, -0.44478753f }, { 0.98099965f, 0.1436933f, -0.13035236f }, { -0.60294366f, -0.63008505f, -0.48933816f }, { -0.36632854f, -0.83804846f, -0.4043244f }, { 0.880623f, 0.4026446f, -0.24976073f }, { -0.39466205f, 0.47551984f, -0.7862078f }, { 0.07596095f, 0.4059133f, 0.9107493f }, { 0.7021051f, 0.3449947f, -0.6229181f }, { 0.56224215f, -0.63307226f, 0.5320746f }, { -0.43509927f, 0.41802734f, -0.79745954f }, { -0.461028f, 0.30334285f, -0.8339282f }, { -0.46377057f, -0.4244343f, 0.7776711f }, { 0.24655874f, -0.7575924f, -0.6043695f }, { 0.86096966f, -0.46542385f, 0.20521183f }, { -0.57619935f, 0.7141871f, 0.39740553f }, { -0.40988854f, 0.6537044f, -0.6361304f }, { -0.15665698f, 0.7326548f, -0.66232586f }, { 0.6951415f, -0.717503f, 0.044358764f }, { 0.535588f, 0.28203335f, -0.79599166f }, { -0.708387f, -0.7029033f, -0.06414832f }, { -0.060282897f, 0.9604795f, 0.27174455f }, { -0.47378626f, -0.8492266f, 0.23311117f }, { 0.097770624f, 0.9754371f, -0.19739145f }, { -0.93824697f, 0.34596613f, -2.8347544E-4f }, { -0.05284173f, 0.020321144f, -0.9983961f }, { 0.42282704f, 0.47321168f, -0.7728441f }, { 0.9702957f, 0.233958f, -0.061561134f }, { -0.6260152f, -0.5795249f, 0.52178144f }, { -0.6479445f, 0.7608896f, -0.03485559f }, { 0.0660722f, 0.99745655f, -0.02673828f }, { -0.6674177f, 0.36485195f, -0.64918154f }, { 0.18630546f, 0.9119203f, -0.3656387f }, { -0.72285986f, 0.19917385f, -0.6616671f }, { -0.11553926f, 0.8691355f, 0.48088893f }, { 0.54829407f, 0.61802703f, -0.56339705f }, { 0.593462f, 0.5478089f, -0.5896679f }, { 0.639189f, 0.20744784f, -0.74054223f }, { -0.6391592f, -0.64150745f, 0.4241976f }, { -0.4549638f, 0.6583679f, -0.5996329f }, { -0.8713606f, -0.4141183f, -0.2631287f }, { 0.92302376f, 0.04600238f, -0.38198292f }, { 0.07584454f, 0.86577517f, 0.49465248f }, { 0.8783598f, -0.20424442f, 0.43216702f }, { 0.2786472f, -0.45479348f, -0.8458833f }, { 0.8800522f, -0.12387612f, 0.45843518f }, { 0.6028159f, -0.4979191f, -0.6234496f }, { -0.39914244f, 0.69446516f, 0.59866804f }, { 0.42770383f, -0.656982f, -0.62084144f }, { 0.9394859f, 0.32820874f, -0.09820987f }, { 0.976861f, 0.029231746f, 0.21186826f }, { 0.17214563f, -0.41702422f, -0.8924442f }, { 0.08946614f, 0.9677178f, -0.2356225f }, { -0.8162444f, -0.5776971f, 0.0033366794f }, { -0.21626784f, -0.74436945f, -0.6317771f }, { 0.1735309f, -0.19406089f, -0.96551925f }, { -0.93658215f, 0.3346914f, 0.10390185f }, { 0.7071796f, -0.6503142f, 0.2774679f }, { -0.43952128f, 0.8789354f, 0.18518473f }, { -0.5484355f, -0.64225596f, 0.5354676f }, { 0.79593503f, 0.17519617f, 0.57947713f }, { 0.92551595f, -0.28963193f, -0.24399523f }, { 0.95595795f, 0.15369508f, 0.2500442f }, { -0.8096863f, 0.5833115f, 0.064466126f }, { 0.9512082f, -0.07180032f, 0.30007952f }, { 0.39479882f, 0.41338828f, -0.82051444f }, { 0.49690923f, 0.8517794f, -0.16599107f }, { 0.6526827f, -0.41204572f, 0.6357858f }, { 0.45688727f, -0.6149302f, -0.64274f }, { 0.33829033f, -0.9316731f, 0.13245754f }, { -0.37689298f, -0.8636409f, -0.33477765f }, { -0.68987185f, 0.50892353f, 0.51485294f }, { -0.98133636f, -0.0035604762f, -0.1922663f }, { -0.6721537f, 0.41515097f, -0.61307335f }, { 0.75630623f, -0.48074573f, 0.44371668f }, { 0.8540043f, -0.42230058f, 0.30387315f }, { -0.46583244f, 0.45352775f, 0.7598111f }, { -0.4586807f, -0.54173917f, 0.7043655f }, { -0.6779395f, 0.093152046f, 0.72919184f }, { -0.83363485f, -0.55231166f, 0.0021758003f }, { -0.6842901f, -0.12166665f, 0.7189884f }, { -0.68675506f, -0.14606643f, 0.71206176f }, { 0.72472906f, -0.06969679f, -0.6854999f }, { 0.4960045f, 0.76544964f, -0.40995884f }, { 0.40753195f, 0.12911756f, -0.9040168f }, { 0.7311607f, -0.5036977f, 0.46010074f }, { 0.7102183f, -0.66151774f, -0.24079922f }, { 0.002656905f, 0.90847915f, -0.41792163f }, { 0.39867622f, -0.6994133f, -0.59319335f }, { 0.48156416f, 0.6983988f, 0.52946675f }, { 0.528222f, -0.064928f, -0.84662026f }, { 0.6755618f, 0.6992443f, 0.23382403f }, { -0.7088862f, 0.18238473f, 0.68133414f }, { -0.73883015f, -0.16721831f, -0.65281546f }, { 0.20634909f, -0.92851037f, 0.30868855f }, { -0.19215988f, 0.0705406f, -0.97882515f }, { -0.5823455f, -0.19687884f, -0.78874105f }, { -0.04085695f, 0.62829864f, -0.7768986f }, { -0.19889842f, 0.6212684f, -0.7579347f }, { 0.41419584f, -0.87258476f, 0.25891605f }, { 0.3869783f, -0.6417822f, 0.66209024f }, { -0.7703481f, -0.637368f, 0.018049818f }, { 0.43369254f, -0.29900986f, 0.85000235f }, { -0.28839418f, 0.19076936f, 0.9383155f }, { -0.432032f, -0.48842654f, 0.75814766f }, { -0.44781786f, 0.04616318f, -0.89293236f }, { 0.7093718f, 0.18906823f, -0.67900276f }, { 0.35653397f, -0.90729123f, 0.22294855f }, { 0.9355106f, -0.22352311f, -0.27360073f }, { 0.6762252f, 0.5192115f, -0.5226269f }, { 0.048460092f, 0.75642526f, -0.65228254f }, { -0.9681524f, 0.22604628f, -0.10762846f }, { -0.6444421f, -0.5538665f, -0.5271871f }, { 0.6941376f, -0.088139854f, -0.7144259f }, { -0.008108428f, -0.4474733f, 0.8942606f }, { 0.92908436f, 0.19124448f, -0.31658775f }, { -0.8386886f, 0.22139284f, 0.4975808f }, { -0.7068576f, -0.40931037f, 0.5769032f }, { -0.6881402f, -0.67885596f, 0.25615934f }, { 0.49827632f, 0.6138209f, -0.61232716f }, { 0.27214825f, 0.7111862f, -0.6481895f }, { -0.7745531f, -0.15798827f, 0.61245996f }, { -0.52418995f, 0.73044074f, 0.43781424f }, { -0.20524232f, -0.044816162f, 0.9776846f }, { 0.56070507f, 0.5770517f, 0.5938192f }, { 0.46629024f, 0.7085491f, 0.52965224f }, { -0.64171606f, 0.7036523f, 0.30508006f }, { 0.55045086f, 0.25162423f, 0.7960459f }, { 0.31247866f, 0.6404385f, -0.7015666f }, { -0.34708178f, -0.8063192f, -0.4789401f }, { 0.4533105f, 0.01278498f, 0.89126104f }, { 0.34022805f, -0.525888f, -0.7795426f }, { -0.53778994f, -0.8430184f, -0.010098429f }, { 0.554847f, 0.47032976f, 0.6862469f }, { 0.79340816f, 0.5978481f, -0.11437274f }, { 0.841805f, -0.12227329f, 0.5257506f }, { -0.3408287f, -0.7949187f, -0.5019364f }, { 0.69493556f, -0.334751f, 0.6364012f }, { -0.7132172f, 0.51549673f, -0.4749573f }, { 0.6188621f, 0.14462763f, -0.7720703f }, { 0.16908315f, 0.82959855f, -0.53214395f }, { -0.71726155f, 0.6967959f, 0.0033638927f }, { -0.74357843f, 0.23819894f, 0.6247819f }, { 0.66317064f, 0.7261589f, -0.1813776f }, { 0.43864068f, 0.82045585f, -0.36666957f }, { 0.16873841f, -0.81977224f, -0.54726666f }, { 0.10206672f, 0.18197332f, 0.97799194f }, { -0.0030889015f, -0.41684142f, 0.90897393f }, { -0.30605692f, 0.68262416f, 0.66359127f }, { -0.40565398f, 0.7936871f, 0.45332736f }, { -0.72874594f, 0.6273858f, 0.27443844f }, { -0.5983348f, -0.2867149f, -0.7481912f }, { 0.5465253f, 0.41054767f, 0.72990465f }, { 0.85901344f, -0.18634267f, 0.47683576f }, { -0.5587658f, 0.82514083f, 0.083206475f }, { 0.73956746f, -0.6626476f, 0.11805965f }, { 0.5116888f, 0.77452785f, -0.3718618f }, { 0.57300436f, 0.6709918f, -0.47057003f }, { -0.8265536f, -0.1406083f, -0.54501235f }, { 0.66643995f, 0.045168467f, -0.7441892f }, { -0.646514f, 0.5048743f, 0.57194537f }, { 0.22317494f, 0.9734679f, 0.050528772f }, { 0.88501227f, 0.37701255f, -0.2731571f }, { 0.578584f, 0.56752443f, -0.5857957f }, { 0.4901663f, 0.6107312f, -0.62188786f }, { 0.6495792f, 0.65299475f, 0.3894158f }, { 0.38019043f, -0.91212267f, -0.15325606f }, { 0.7580674f, -0.25419256f, 0.6005996f }, { 0.44785765f, 0.77882326f, -0.43915597f }, { -0.3244369f, 0.32642275f, -0.88780004f }, { 0.09900978f, 0.5097075f, 0.85463166f }, { -0.2937438f, 0.5263325f, -0.7979277f }, { 0.9082893f, 0.28128317f, 0.30966157f }, { 0.96880245f, 0.08177609f, 0.23395409f }, { -0.4257678f, -0.20907731f, -0.88034564f }, { -0.6762167f, 0.4360511f, -0.59379315f }, { -0.7104877f, 0.6485216f, -0.27317935f }, { -0.92623574f, 0.1229542f, 0.35632792f }, { -0.85436344f, -0.5104818f, 0.09732187f }, { -0.7734761f, -0.07080485f, 0.6298582f }, { -0.58942896f, -0.18532005f, 0.78627604f }, { 0.35788336f, 0.8735291f, 0.329949f }, { -0.8244525f, -0.26167104f, 0.5018031f }, { -0.3281725f, 0.8881941f, 0.32158044f }, { -0.57719177f, 0.5126006f, -0.63568103f }, { -0.5927615f, 0.33998337f, -0.7300993f }, { 0.04652253f, -0.23480979f, -0.9709274f }, { -0.51686555f, 0.8045143f, 0.2925862f }, { -0.1535005f, 0.030460795f, 0.98767895f }, { 0.5606552f, -0.5814097f, -0.58960044f }, { -0.4344841f, -0.52909404f, 0.72889173f }, { -0.19000271f, 0.7594953f, 0.6221462f }, { 0.38317522f, -0.79624015f, -0.46816486f }, { 0.08458876f, 0.8813494f, -0.46483108f }, { 0.69288504f, -0.7181526f, -0.064554304f }, { 0.84804195f, 0.3104189f, 0.42949387f }, { 0.60953355f, 0.39419693f, -0.6878063f }, { -0.9411591f, -0.13376737f, 0.310364f }, { 0.7096913f, -0.12656899f, -0.6930502f }, { -0.6335558f, -0.4813395f, 0.6057386f }, { -0.83221483f, -0.54542834f, -0.09963122f }, { 0.16353905f, -0.9175573f, -0.36241356f }, { -0.7896088f, -0.33133093f, -0.5164666f }, { 0.49091095f, 0.6483417f, 0.58194447f }, { -0.7029837f, -0.62562835f, 0.33823535f }, { 0.6494896f, -0.32497323f, -0.6874268f } };
        Z_ELIPTICAL_RANDOM = new float[][] { { 0.49756134f, 0.22448322f, 0.83787835f }, { 0.14717819f, 0.0014769214f, 0.9891089f }, { 0.27177078f, 0.30839524f, 0.9116102f }, { 0.19559723f, 0.023650857f, 0.9803991f }, { 0.3314414f, 0.16316378f, 0.9292601f }, { 0.06737462f, 0.27404675f, 0.95935345f }, { 0.15125367f, 0.053535294f, 0.9870443f }, { 0.05284634f, 0.21110243f, 0.97603434f }, { 0.020348553f, 0.9956124f, 0.09133342f }, { 0.37260792f, 0.2194753f, 0.90166175f }, { 0.016074581f, 0.24442458f, 0.9695351f }, { 0.15018348f, 0.17994761f, 0.9721439f }, { 0.12405737f, 0.05102748f, 0.9909621f }, { 0.06034564f, 0.13113871f, 0.9895257f }, { 0.044069856f, 0.14499256f, 0.9884508f }, { 0.4890187f, 0.255729f, 0.8339445f }, { 0.40426296f, 0.8102286f, 0.42438325f }, { 0.14008513f, 0.5821627f, 0.80091375f }, { 0.14893764f, 0.06965798f, 0.9863901f }, { 0.840596f, 0.51485467f, 0.16829447f }, { 0.18305975f, 0.07306587f, 0.9803828f }, { 0.20271322f, 0.9737819f, 0.10322868f }, { 0.17424688f, 0.32927895f, 0.9280158f }, { 0.34561333f, 0.020192016f, 0.93815976f }, { 0.25485098f, 0.024921585f, 0.9666591f }, { 0.09820176f, 0.16402546f, 0.981556f }, { 0.1014724f, 0.189009f, 0.9767185f }, { 0.60141516f, 0.104343526f, 0.7920936f }, { 0.11101186f, 0.13016582f, 0.985258f }, { 0.12754159f, 0.1353928f, 0.98254865f }, { 0.2787766f, 0.22707383f, 0.9331244f }, { 0.03221285f, 0.42383376f, 0.905167f }, { 0.15691106f, 0.028711297f, 0.9871953f }, { 0.0888859f, 0.09613364f, 0.9913918f }, { 0.30518705f, 0.15015171f, 0.94038045f }, { 0.08545087f, 0.12893878f, 0.98796403f }, { 0.106860735f, 0.11830801f, 0.9872102f }, { 0.14631411f, 0.15707321f, 0.9766884f }, { 0.14538196f, 0.19726388f, 0.96951073f }, { 0.04129767f, 0.2169264f, 0.975314f }, { 0.34805426f, 0.16975482f, 0.9219769f }, { 0.12834689f, 0.07743836f, 0.98870134f }, { 0.5458888f, 0.3492899f, 0.7615787f }, { 0.19083364f, 0.1523762f, 0.9697237f }, { 0.11513223f, 0.048200358f, 0.99218005f }, { 0.07832789f, 0.2751789f, 0.95819694f }, { 0.17914772f, 0.05800988f, 0.98211044f }, { 0.27842006f, 0.17621493f, 0.94415605f }, { 0.09251038f, 0.12455049f, 0.9878912f }, { 0.088154554f, 0.100685455f, 0.9910052f }, { 0.32485855f, 0.63168925f, 0.7038718f }, { 0.08309894f, 0.108091496f, 0.9906618f }, { 0.116453744f, 0.37493724f, 0.91970676f }, { 0.09285728f, 0.27444327f, 0.95710945f }, { 0.09286511f, 0.10214464f, 0.9904254f }, { 0.08153807f, 0.13687679f, 0.98722655f }, { 0.09908146f, 0.24012895f, 0.9656713f }, { 0.2967209f, 0.13707118f, 0.9450758f }, { 0.16997562f, 0.1209029f, 0.9780034f }, { 0.64440596f, 0.3565725f, 0.67645913f }, { 0.32555887f, 0.13212742f, 0.93624455f }, { 0.2141342f, 0.21798104f, 0.9521716f }, { 0.10310763f, 0.13070516f, 0.9860451f }, { 0.29433167f, 0.13194892f, 0.9465507f }, { 0.4688346f, 0.8828301f, 0.028376115f }, { 0.106797405f, 0.13090755f, 0.9856254f }, { 0.1769805f, 0.074213594f, 0.98141235f }, { 0.1697665f, 0.08335241f, 0.98195297f }, { 0.15888907f, 5.5858784E-4f, 0.9872963f }, { 0.0997858f, 0.14542022f, 0.984325f }, { 0.048946965f, 0.15844007f, 0.9861546f }, { 0.33046976f, 0.4106115f, 0.84981644f }, { 0.13953651f, 0.14811578f, 0.97907674f }, { 0.12917627f, 0.019722247f, 0.99142545f }, { 0.2303286f, 0.15342355f, 0.9609422f }, { 0.044058315f, 0.122271284f, 0.9915183f }, { 0.09330104f, 0.12721275f, 0.98747754f }, { 0.6186327f, 0.5913308f, 0.5173215f }, { 0.63378775f, 0.7718336f, 0.0508527f }, { 0.37492195f, 0.38649932f, 0.8426457f }, { 0.029687276f, 0.27429727f, 0.96118665f }, { 0.09935042f, 0.096224f, 0.990389f }, { 0.07249868f, 0.1508967f, 0.98588747f }, { 0.5379798f, 0.7171547f, 0.44302034f }, { 0.1888423f, 0.071829386f, 0.97937685f }, { 0.5932034f, 0.029997997f, 0.8044935f }, { 0.17264827f, 0.19099937f, 0.9662876f }, { 0.2491251f, 0.07369533f, 0.9656634f }, { 0.115620695f, 0.112317495f, 0.9869228f }, { 0.083513916f, 0.3054667f, 0.9485334f }, { 0.1714077f, 0.23922198f, 0.9557156f }, { 0.06740571f, 0.13743335f, 0.98821485f }, { 0.081165746f, 0.13367718f, 0.9876955f }, { 0.08952832f, 0.17646874f, 0.9802263f }, { 0.12773165f, 0.11253768f, 0.9854034f }, { 0.11342063f, 0.1655832f, 0.9796519f }, { 0.08931992f, 0.17470394f, 0.98056126f }, { 0.14712705f, 0.11770799f, 0.9820888f }, { 0.33072355f, 0.07222591f, 0.9409598f }, { 0.31123185f, 0.028463228f, 0.94990766f }, { 0.48958883f, 0.086720124f, 0.86763036f }, { 0.0830861f, 0.096301384f, 0.9918784f }, { 0.29848436f, 0.35455075f, 0.8861156f }, { 0.7432745f, 0.49399742f, 0.45112038f }, { 0.2869368f, 0.05372302f, 0.9564419f }, { 0.32242113f, 0.22689196f, 0.91900194f }, { 0.301785f, 0.2849079f, 0.9098095f }, { 0.47986382f, 0.4086944f, 0.7763373f }, { 0.17035314f, 0.11132324f, 0.9790745f }, { 0.0374786f, 0.20488788f, 0.97806764f }, { 0.3274832f, 0.13213028f, 0.9355728f }, { 0.06599883f, 0.5471322f, 0.83444023f }, { 0.12713334f, 0.22958703f, 0.9649492f }, { 0.1507704f, 0.011747671f, 0.988499f }, { 0.14213577f, 0.045392197f, 0.9888058f }, { 0.10507516f, 0.08719939f, 0.99063385f }, { 0.07965378f, 0.11324274f, 0.99036926f }, { 0.09819041f, 0.15185419f, 0.98351365f }, { 0.07062863f, 0.10999599f, 0.99141943f }, { 0.17254011f, 0.041534748f, 0.98412645f }, { 0.22139962f, 0.24400108f, 0.94416404f }, { 0.11912409f, 0.0768585f, 0.98990005f }, { 0.15703867f, 0.10801321f, 0.981668f }, { 0.13643299f, 0.9344091f, 0.32903746f }, { 0.16550113f, 0.16644202f, 0.97206295f }, { 0.16773245f, 0.13728651f, 0.9762265f }, { 0.007919231f, 0.13114694f, 0.9913313f }, { 0.16443689f, 0.14247482f, 0.97604376f }, { 0.026527511f, 0.28461242f, 0.95827556f }, { 0.37560496f, 0.12866256f, 0.91780543f }, { 0.004631573f, 0.14614783f, 0.989252f }, { 0.18962304f, 0.13899136f, 0.9719694f }, { 0.098676234f, 0.19191256f, 0.9764387f }, { 0.6580259f, 0.028999118f, 0.75243664f }, { 0.17899792f, 0.16246487f, 0.9703427f }, { 0.5974721f, 0.42935544f, 0.67726004f }, { 0.9811881f, 0.17204216f, 0.087586574f }, { 0.17675127f, 0.16366869f, 0.9705522f }, { 0.2871354f, 0.19672875f, 0.9374706f }, { 0.2882064f, 0.28823137f, 0.91315925f }, { 0.8898462f, 0.039128754f, 0.45457968f }, { 0.6905361f, 0.07754118f, 0.7191296f }, { 0.15082118f, 0.1801376f, 0.9720101f }, { 0.33916682f, 0.31744498f, 0.8855477f }, { 0.27014762f, 0.2552495f, 0.92836845f }, { 0.5789823f, 0.6566934f, 0.48325294f }, { 0.15183266f, 0.07357982f, 0.9856637f }, { 0.7191466f, 0.39689666f, 0.5703518f }, { 0.10800953f, 0.1967423f, 0.9744878f }, { 0.91813326f, 0.30010307f, 0.2587846f }, { 0.013305249f, 0.44003412f, 0.89788246f }, { 0.91485345f, 0.06433937f, 0.3986271f }, { 0.1396898f, 0.004951417f, 0.9901829f }, { 0.5770786f, 0.77255523f, 0.26483724f }, { 0.115430154f, 0.51797587f, 0.8475712f }, { 0.11568357f, 0.07634384f, 0.99034786f }, { 0.037412927f, 0.31171882f, 0.9494375f }, { 0.17585994f, 0.103900105f, 0.9789167f }, { 0.06685313f, 0.20988415f, 0.975438f }, { 0.21636215f, 0.21210212f, 0.95299536f }, { 0.06542105f, 0.11939547f, 0.99068904f }, { 0.15811904f, 0.0939761f, 0.9829378f }, { 0.50776905f, 0.22645456f, 0.8311973f }, { 0.4810373f, 0.27610743f, 0.83208644f }, { 0.20252123f, 0.16700499f, 0.9649324f }, { 0.025183806f, 0.30961522f, 0.9505283f }, { 0.0667333f, 0.10511597f, 0.9922183f }, { 0.138574f, 0.19579169f, 0.9708052f }, { 0.49393743f, 0.064802825f, 0.8670793f }, { 0.22292219f, 0.11617632f, 0.9678888f }, { 0.2127594f, 0.3167579f, 0.9243364f }, { 0.006743938f, 0.12513283f, 0.99211705f }, { 0.21638139f, 0.070336424f, 0.9737719f }, { 0.14817294f, 0.0981366f, 0.98408026f }, { 0.083774574f, 0.21822327f, 0.9722964f }, { 0.27531275f, 0.061161138f, 0.9594072f }, { 0.14117143f, 0.21458164f, 0.96644986f }, { 0.030193506f, 0.1742926f, 0.9842309f }, { 0.12521401f, 0.22870436f, 0.9654097f }, { 0.1414637f, 0.13738336f, 0.98036414f }, { 0.1077404f, 0.10500958f, 0.98861766f }, { 0.15396827f, 0.5417584f, 0.82631207f }, { 0.04914795f, 0.24356319f, 0.9686391f }, { 0.08580625f, 0.10159479f, 0.99111843f }, { 0.42541268f, 0.3578491f, 0.83124495f }, { 0.19359899f, 0.089698225f, 0.9769716f }, { 0.07467243f, 0.20092633f, 0.97675616f }, { 0.32728487f, 0.69101226f, 0.644505f }, { 0.34622487f, 0.16004154f, 0.92439985f }, { 0.12970987f, 0.12492025f, 0.9836515f }, { 0.13707949f, 0.0494514f, 0.9893249f }, { 0.075577386f, 0.16677716f, 0.9830938f }, { 0.5764278f, 0.16189818f, 0.80094945f }, { 0.11737904f, 0.0978234f, 0.98825747f }, { 0.08197988f, 0.12193561f, 0.9891466f }, { 0.28920123f, 0.12281735f, 0.9493569f }, { 0.21493705f, 0.3504242f, 0.9115947f }, { 0.049493674f, 0.35948256f, 0.93183833f }, { 0.017044023f, 0.18905315f, 0.9818189f }, { 0.028402703f, 0.19161864f, 0.9810584f }, { 0.02120577f, 0.14543775f, 0.98914015f }, { 0.14608961f, 0.08215707f, 0.98585397f }, { 0.35004398f, 0.027590279f, 0.93632686f }, { 0.46057534f, 0.2930635f, 0.83784497f }, { 0.1622013f, 0.06479218f, 0.98462826f }, { 0.21937941f, 0.36855507f, 0.9033493f }, { 0.08758094f, 0.11610445f, 0.98936814f }, { 0.17367832f, 0.20994064f, 0.96216464f }, { 0.066491194f, 0.12075184f, 0.99045336f }, { 0.08600856f, 0.114495f, 0.9896936f }, { 0.09365819f, 0.14604896f, 0.9848339f }, { 0.11174391f, 0.09896309f, 0.988797f }, { 0.18004635f, 0.10419094f, 0.97812456f }, { 0.14041719f, 0.11717297f, 0.98313457f }, { 0.1286886f, 0.17005311f, 0.976996f }, { 0.094773054f, 0.38625368f, 0.91751087f }, { 0.060095288f, 0.32011196f, 0.9454718f }, { 0.7339581f, 0.42128393f, 0.5327525f }, { 0.29252955f, 0.17685573f, 0.9397598f }, { 0.6454711f, 0.63630587f, 0.42247128f }, { 0.2991647f, 0.045092314f, 0.95313543f }, { 0.03401744f, 0.20906255f, 0.9773105f }, { 0.058245692f, 0.1234763f, 0.99063665f }, { 0.07014034f, 0.25757265f, 0.96370983f }, { 0.15768689f, 0.18558861f, 0.9698926f }, { 0.22160803f, 0.13112305f, 0.9662798f }, { 0.17545058f, 0.24872138f, 0.9525517f }, { 0.0025834225f, 0.38572565f, 0.92261f }, { 0.18043166f, 0.19974081f, 0.9630929f }, { 0.052273475f, 0.139042f, 0.98890585f }, { 0.215388f, 0.048576567f, 0.9753196f }, { 0.10432405f, 0.16771427f, 0.9803002f }, { 0.08442982f, 0.14867905f, 0.9852746f }, { 0.18654983f, 0.12623802f, 0.9743013f }, { 0.0926326f, 0.10649254f, 0.98998916f }, { 0.18451095f, 0.1771778f, 0.9667284f }, { 0.15685989f, 0.17908645f, 0.97124815f }, { 0.01811427f, 0.1416165f, 0.98975587f }, { 0.08844289f, 0.20704824f, 0.9743248f }, { 0.056534417f, 0.9927929f, 0.10566965f }, { 0.1288334f, 0.09958774f, 0.9866531f }, { 0.1227418f, 0.04530217f, 0.9914041f }, { 0.9158077f, 0.28578445f, 0.28217626f }, { 0.021541616f, 0.13033749f, 0.9912356f }, { 0.24526337f, 0.49639106f, 0.8327316f }, { 0.14637534f, 0.053712737f, 0.98776984f }, { 0.08568521f, 0.13191862f, 0.98755026f }, { 0.46878588f, 0.58396465f, 0.6627405f }, { 0.6314314f, 0.57007146f, 0.52565473f }, { 0.26345956f, 0.14771172f, 0.95329446f }, { 0.17676562f, 0.03052114f, 0.9837796f }, { 0.44984114f, 0.65379906f, 0.6084321f }, { 0.5786396f, 0.6376385f, 0.50852084f }, { 0.33100003f, 0.12020341f, 0.9359434f }, { 0.22243282f, 0.1965871f, 0.95492256f }, { 0.13549456f, 0.05314529f, 0.9893517f } };
    }
}
