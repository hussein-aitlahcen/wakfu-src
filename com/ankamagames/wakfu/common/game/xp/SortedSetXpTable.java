package com.ankamagames.wakfu.common.game.xp;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.utils.*;

public class SortedSetXpTable implements XpTable
{
    private static final Logger m_logger;
    private SortedSet<Long> m_sortedTable;
    private short m_maxLevel;
    private short m_minLevel;
    private long[] m_table;
    private final long m_minValue;
    private final long m_maxValue;
    
    protected SortedSetXpTable(final long[] table, final int minLevel, final int maxLevel) {
        this(table, minLevel, maxLevel, 0L, Long.MAX_VALUE, 1.0);
    }
    
    protected SortedSetXpTable(final long[] table, final int minLevel, final int maxLevel, final double factor) {
        this(table, minLevel, maxLevel, 0L, Long.MAX_VALUE, factor);
    }
    
    protected SortedSetXpTable(final long[] table, final int minLevel, final int maxLevel, final long minValue, final long maxValue) {
        this(table, minLevel, maxLevel, minValue, maxValue, 1.0);
    }
    
    protected SortedSetXpTable(final long[] table, final int minLevel, final int maxLevel, final long minValue, final long maxValue, final double factor) {
        super();
        this.initializeAndValidateTable(table, minLevel, maxLevel, factor);
        this.copyTableToSortedSet();
        this.m_minValue = minValue;
        this.m_maxValue = maxValue;
    }
    
    private void copyTableToSortedSet() {
        final List<Long> tableList = Arrays.asList(PrimitiveArrays.toObject(this.m_table));
        this.m_sortedTable = Collections.unmodifiableSortedSet(new TreeSet<Long>(tableList));
    }
    
    private void initializeAndValidateTable(final long[] table, final int minLevel, final int maxLevel, final double factor) {
        this.checkInputTable(table, minLevel, maxLevel);
        final int tableSize = Math.min(maxLevel - minLevel + 1, table.length);
        this.m_minLevel = this.checkShort(minLevel);
        this.m_maxLevel = this.checkShort(this.m_minLevel + tableSize - 1);
        this.m_table = new long[tableSize];
        if (factor == 1.0) {
            System.arraycopy(table, 0, this.m_table, 0, tableSize);
        }
        else {
            for (int i = 0; i < tableSize; ++i) {
                final long l = table[i];
                final double v = l * factor;
                final long round1 = Math.round(v);
                this.m_table[i] = round1;
            }
        }
        this.checkInputTableOrder();
    }
    
    private void checkInputTableOrder() {
        for (int i = 0; i < this.m_table.length - 1; ++i) {
            if (this.m_table[i + 1] == this.m_table[i]) {
                throw new IllegalArgumentException(this.duplicateValueErrorMessage(i));
            }
            if (this.m_table[i + 1] < this.m_table[i]) {
                throw new IllegalArgumentException(this.inputNotSortedErrorMessage(i));
            }
        }
    }
    
    private short checkShort(final int i) {
        if (i < -32768 || i > 32767) {
            SortedSetXpTable.m_logger.error((Object)("D\u00e9passement de short pour une limite de niveau dans " + this.getClass().getSimpleName()));
        }
        return (short)i;
    }
    
    private void checkInputTable(final long[] table, final int minLevel, final int maxLevel) {
        final int tableLength = table.length;
        final int expectedTableLength = maxLevel - minLevel + 1;
        if (tableLength < expectedTableLength) {
            SortedSetXpTable.m_logger.error((Object)this.notEnoughValuesErrorMessage(expectedTableLength, tableLength));
        }
    }
    
    @Override
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    @Override
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    private long maxValue() {
        return this.m_maxValue;
    }
    
    private long minValue() {
        return this.m_minValue;
    }
    
    @Override
    public long getXpByLevel(final int level) {
        if (level < this.getMinLevel()) {
            return this.minValue();
        }
        if (level > this.getMaxLevel()) {
            return this.maxValue();
        }
        return this.m_table[level - this.getMinLevel()];
    }
    
    @Override
    public short getLevelByXp(final long xp) {
        return (short)(this.m_sortedTable.headSet(xp + 1L).size() + this.getMinLevel() - 1);
    }
    
    @Override
    public long getMinXp() {
        return this.getXpByLevel(this.getMinLevel());
    }
    
    @Override
    public long getMaxXp() {
        return this.getXpByLevel(this.getMaxLevel());
    }
    
    @Override
    public boolean isLevelValid(final short level) {
        return level >= this.getMinLevel() && level <= this.getMaxLevel();
    }
    
    @Override
    public boolean isXpValid(final long xp) {
        return xp >= this.getMinXp() && xp <= this.getMaxXp();
    }
    
    @Override
    public float getPercentageInLevel(final short level, final long xp) {
        final long levelExtent = this.getLevelExtent(level);
        if (levelExtent == 0L) {
            return 0.0f;
        }
        return MathHelper.clamp(this.getXpInLevel(xp, level) / levelExtent, 0.0f, 1.0f);
    }
    
    @Override
    public long getXpInLevel(final long xp) {
        return this.getXpInLevel(xp, this.getLevelByXp(xp));
    }
    
    private long getXpInLevel(final long xp, final short level) {
        return MathHelper.clamp(xp - this.getXpByLevel(level), 0L, this.getLevelExtent(level));
    }
    
    @Override
    public long getLevelExtent(final short level) {
        if (level < this.getMinLevel() || level >= this.getMaxLevel()) {
            return 0L;
        }
        return this.getXpByLevel(level + 1) - this.getXpByLevel(level);
    }
    
    private String tooManyValuesErrorMessage(final int expectedTableLength, final int tableLength) {
        return "La table de " + this.getClass().getSimpleName() + " contient trop de valeurs - " + tableLength + " au lieu de " + expectedTableLength + ", les valeurs suppl\u00e9mentaires ont \u00e9t\u00e9 ignor\u00e9es." + ExceptionFormatter.currentStackTrace(1, 6);
    }
    
    private String notEnoughValuesErrorMessage(final int expectedTableLength, final int tableLength) {
        return "La table de " + this.getClass().getSimpleName() + " ne contient pas assez de valeurs - " + tableLength + " au lieu de " + expectedTableLength + ", le niveau maximal a \u00e9t\u00e9 r\u00e9duit." + ExceptionFormatter.currentStackTrace(1, 6);
    }
    
    private String inputNotSortedErrorMessage(final int i) {
        return "Tableau mal tri\u00e9 \u00e0 " + i + " - " + this.getClass().getSimpleName();
    }
    
    private String duplicateValueErrorMessage(final int i) {
        return "Tableau contenant une valeur double \u00e0 " + i + " - " + this.getClass().getSimpleName();
    }
    
    static {
        m_logger = Logger.getLogger((Class)SortedSetXpTable.class);
    }
}
