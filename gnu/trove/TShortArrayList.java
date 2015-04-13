package gnu.trove;

import java.util.*;
import java.io.*;

public class TShortArrayList implements Externalizable, Cloneable
{
    static final long serialVersionUID = 1L;
    protected short[] _data;
    protected int _pos;
    protected static final int DEFAULT_CAPACITY = 10;
    
    public TShortArrayList() {
        this(10);
    }
    
    public TShortArrayList(final int capacity) {
        super();
        this._data = new short[capacity];
        this._pos = 0;
    }
    
    public TShortArrayList(final short[] values) {
        this(Math.max(values.length, 10));
        this.add(values);
    }
    
    public void ensureCapacity(final int capacity) {
        if (capacity > this._data.length) {
            final int newCap = Math.max(this._data.length << 1, capacity);
            final short[] tmp = new short[newCap];
            System.arraycopy(this._data, 0, tmp, 0, this._data.length);
            this._data = tmp;
        }
    }
    
    public int size() {
        return this._pos;
    }
    
    public boolean isEmpty() {
        return this._pos == 0;
    }
    
    public void trimToSize() {
        if (this._data.length > this.size()) {
            final short[] tmp = new short[this.size()];
            this.toNativeArray(tmp, 0, tmp.length);
            this._data = tmp;
        }
    }
    
    public void add(final short val) {
        this.ensureCapacity(this._pos + 1);
        this._data[this._pos++] = val;
    }
    
    public void add(final short[] vals) {
        this.add(vals, 0, vals.length);
    }
    
    public void add(final short[] vals, final int offset, final int length) {
        this.ensureCapacity(this._pos + length);
        System.arraycopy(vals, offset, this._data, this._pos, length);
        this._pos += length;
    }
    
    public void insert(final int offset, final short value) {
        if (offset == this._pos) {
            this.add(value);
            return;
        }
        this.ensureCapacity(this._pos + 1);
        System.arraycopy(this._data, offset, this._data, offset + 1, this._pos - offset);
        this._data[offset] = value;
        ++this._pos;
    }
    
    public void insert(final int offset, final short[] values) {
        this.insert(offset, values, 0, values.length);
    }
    
    public void insert(final int offset, final short[] values, final int valOffset, final int len) {
        if (offset == this._pos) {
            this.add(values, valOffset, len);
            return;
        }
        this.ensureCapacity(this._pos + len);
        System.arraycopy(this._data, offset, this._data, offset + len, this._pos - offset);
        System.arraycopy(values, valOffset, this._data, offset, len);
        this._pos += len;
    }
    
    public short get(final int offset) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return this._data[offset];
    }
    
    public short getQuick(final int offset) {
        return this._data[offset];
    }
    
    public void set(final int offset, final short val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        this._data[offset] = val;
    }
    
    public short getSet(final int offset, final short val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        final short old = this._data[offset];
        this._data[offset] = val;
        return old;
    }
    
    public void set(final int offset, final short[] values) {
        this.set(offset, values, 0, values.length);
    }
    
    public void set(final int offset, final short[] values, final int valOffset, final int length) {
        if (offset < 0 || offset + length > this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(values, valOffset, this._data, offset, length);
    }
    
    public void setQuick(final int offset, final short val) {
        this._data[offset] = val;
    }
    
    public void clear() {
        this.clear(10);
    }
    
    public void clear(final int capacity) {
        this._data = new short[capacity];
        this._pos = 0;
    }
    
    public void reset() {
        this._pos = 0;
        this.fill((short)0);
    }
    
    public void resetQuick() {
        this._pos = 0;
    }
    
    public short remove(final int offset) {
        final short old = this.get(offset);
        this.remove(offset, 1);
        return old;
    }
    
    public void remove(final int offset, final int length) {
        if (offset < 0 || offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        if (offset == 0) {
            System.arraycopy(this._data, length, this._data, 0, this._pos - length);
        }
        else if (this._pos - length != offset) {
            System.arraycopy(this._data, offset + length, this._data, offset, this._pos - (offset + length));
        }
        this._pos -= length;
    }
    
    public void transformValues(final TShortFunction function) {
        int i = this._pos;
        while (i-- > 0) {
            this._data[i] = function.execute(this._data[i]);
        }
    }
    
    public void reverse() {
        this.reverse(0, this._pos);
    }
    
    public void reverse(final int from, final int to) {
        if (from == to) {
            return;
        }
        if (from > to) {
            throw new IllegalArgumentException("from cannot be greater than to");
        }
        for (int i = from, j = to - 1; i < j; ++i, --j) {
            this.swap(i, j);
        }
    }
    
    public void shuffle(final Random rand) {
        int i = this._pos;
        while (i-- > 1) {
            this.swap(i, rand.nextInt(i));
        }
    }
    
    private final void swap(final int i, final int j) {
        final short tmp = this._data[i];
        this._data[i] = this._data[j];
        this._data[j] = tmp;
    }
    
    public Object clone() {
        TShortArrayList list = null;
        try {
            list = (TShortArrayList)super.clone();
            list._data = this.toNativeArray();
        }
        catch (CloneNotSupportedException ex) {}
        return list;
    }
    
    public TShortArrayList subList(final int begin, final int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end index " + end + " greater than begin index " + begin);
        }
        if (begin < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        if (end > this._data.length) {
            throw new IndexOutOfBoundsException("end index < " + this._data.length);
        }
        final TShortArrayList list = new TShortArrayList(end - begin);
        for (int i = begin; i < end; ++i) {
            list.add(this._data[i]);
        }
        return list;
    }
    
    public short[] toNativeArray() {
        return this.toNativeArray(0, this._pos);
    }
    
    public short[] toNativeArray(final int offset, final int len) {
        final short[] rv = new short[len];
        this.toNativeArray(rv, offset, len);
        return rv;
    }
    
    public void toNativeArray(final short[] dest, final int offset, final int len) {
        if (len == 0) {
            return;
        }
        if (offset < 0 || offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(this._data, offset, dest, 0, len);
    }
    
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TShortArrayList)) {
            return false;
        }
        final TShortArrayList that = (TShortArrayList)other;
        if (that.size() != this.size()) {
            return false;
        }
        int i = this._pos;
        while (i-- > 0) {
            if (this._data[i] != that._data[i]) {
                return false;
            }
        }
        return true;
    }
    
    public int hashCode() {
        int h = 0;
        int i = this._pos;
        while (i-- > 0) {
            h = 37 * h + HashFunctions.hash(this._data[i]);
        }
        return h;
    }
    
    public boolean forEach(final TShortProcedure procedure) {
        for (int i = 0; i < this._pos; ++i) {
            if (!procedure.execute(this._data[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachDescending(final TShortProcedure procedure) {
        int i = this._pos;
        while (i-- > 0) {
            if (!procedure.execute(this._data[i])) {
                return false;
            }
        }
        return true;
    }
    
    public void sort() {
        Arrays.sort(this._data, 0, this._pos);
    }
    
    public void sort(final int fromIndex, final int toIndex) {
        Arrays.sort(this._data, fromIndex, toIndex);
    }
    
    public void fill(final short val) {
        Arrays.fill(this._data, 0, this._pos, val);
    }
    
    public void fill(final int fromIndex, final int toIndex, final short val) {
        if (toIndex > this._pos) {
            this.ensureCapacity(toIndex);
            this._pos = toIndex;
        }
        Arrays.fill(this._data, fromIndex, toIndex, val);
    }
    
    public int binarySearch(final short value) {
        return this.binarySearch(value, 0, this._pos);
    }
    
    public int binarySearch(final short value, final int fromIndex, final int toIndex) {
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > this._pos) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            final int mid = low + high >>> 1;
            final short midVal = this._data[mid];
            if (midVal < value) {
                low = mid + 1;
            }
            else {
                if (midVal <= value) {
                    return mid;
                }
                high = mid - 1;
            }
        }
        return -(low + 1);
    }
    
    public int indexOf(final short value) {
        return this.indexOf(0, value);
    }
    
    public int indexOf(final int offset, final short value) {
        for (int i = offset; i < this._pos; ++i) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final short value) {
        return this.lastIndexOf(this._pos, value);
    }
    
    public int lastIndexOf(final int offset, final short value) {
        int i = offset;
        while (i-- > 0) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean contains(final short value) {
        return this.lastIndexOf(value) >= 0;
    }
    
    public TShortArrayList grep(final TShortProcedure condition) {
        final TShortArrayList list = new TShortArrayList();
        for (int i = 0; i < this._pos; ++i) {
            if (condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }
    
    public TShortArrayList inverseGrep(final TShortProcedure condition) {
        final TShortArrayList list = new TShortArrayList();
        for (int i = 0; i < this._pos; ++i) {
            if (!condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }
    
    public short max() {
        if (this.size() == 0) {
            throw new IllegalStateException("cannot find maximum of an empty list");
        }
        short max = -32768;
        for (int i = 0; i < this._pos; ++i) {
            if (this._data[i] > max) {
                max = this._data[i];
            }
        }
        return max;
    }
    
    public short min() {
        if (this.size() == 0) {
            throw new IllegalStateException("cannot find minimum of an empty list");
        }
        short min = 32767;
        for (int i = 0; i < this._pos; ++i) {
            if (this._data[i] < min) {
                min = this._data[i];
            }
        }
        return min;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        for (int i = 0, end = this._pos - 1; i < end; ++i) {
            buf.append(this._data[i]);
            buf.append(", ");
        }
        if (this.size() > 0) {
            buf.append(this._data[this._pos - 1]);
        }
        buf.append("}");
        return buf.toString();
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(1);
        out.writeInt(this._pos);
        final int len = this._pos;
        out.writeInt(this._pos);
        for (int i = 0; i < len; ++i) {
            out.writeShort(this._data[i]);
        }
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._pos = in.readInt();
        final int len = in.readInt();
        this._data = new short[len];
        for (int i = 0; i < len; ++i) {
            this._data[i] = in.readShort();
        }
    }
}
