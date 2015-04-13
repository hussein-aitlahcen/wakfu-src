package gnu.trove;

import java.util.*;
import java.io.*;

public class TByteArrayList implements Externalizable, Cloneable
{
    static final long serialVersionUID = 1L;
    protected byte[] _data;
    protected int _pos;
    protected static final int DEFAULT_CAPACITY = 10;
    
    public TByteArrayList() {
        this(10);
    }
    
    public TByteArrayList(final int capacity) {
        super();
        this._data = new byte[capacity];
        this._pos = 0;
    }
    
    public TByteArrayList(final byte[] values) {
        this(Math.max(values.length, 10));
        this.add(values);
    }
    
    public void ensureCapacity(final int capacity) {
        if (capacity > this._data.length) {
            final int newCap = Math.max(this._data.length << 1, capacity);
            final byte[] tmp = new byte[newCap];
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
            final byte[] tmp = new byte[this.size()];
            this.toNativeArray(tmp, 0, tmp.length);
            this._data = tmp;
        }
    }
    
    public void add(final byte val) {
        this.ensureCapacity(this._pos + 1);
        this._data[this._pos++] = val;
    }
    
    public void add(final byte[] vals) {
        this.add(vals, 0, vals.length);
    }
    
    public void add(final byte[] vals, final int offset, final int length) {
        this.ensureCapacity(this._pos + length);
        System.arraycopy(vals, offset, this._data, this._pos, length);
        this._pos += length;
    }
    
    public void insert(final int offset, final byte value) {
        if (offset == this._pos) {
            this.add(value);
            return;
        }
        this.ensureCapacity(this._pos + 1);
        System.arraycopy(this._data, offset, this._data, offset + 1, this._pos - offset);
        this._data[offset] = value;
        ++this._pos;
    }
    
    public void insert(final int offset, final byte[] values) {
        this.insert(offset, values, 0, values.length);
    }
    
    public void insert(final int offset, final byte[] values, final int valOffset, final int len) {
        if (offset == this._pos) {
            this.add(values, valOffset, len);
            return;
        }
        this.ensureCapacity(this._pos + len);
        System.arraycopy(this._data, offset, this._data, offset + len, this._pos - offset);
        System.arraycopy(values, valOffset, this._data, offset, len);
        this._pos += len;
    }
    
    public byte get(final int offset) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        return this._data[offset];
    }
    
    public byte getQuick(final int offset) {
        return this._data[offset];
    }
    
    public void set(final int offset, final byte val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        this._data[offset] = val;
    }
    
    public byte getSet(final int offset, final byte val) {
        if (offset >= this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        final byte old = this._data[offset];
        this._data[offset] = val;
        return old;
    }
    
    public void set(final int offset, final byte[] values) {
        this.set(offset, values, 0, values.length);
    }
    
    public void set(final int offset, final byte[] values, final int valOffset, final int length) {
        if (offset < 0 || offset + length > this._pos) {
            throw new ArrayIndexOutOfBoundsException(offset);
        }
        System.arraycopy(values, valOffset, this._data, offset, length);
    }
    
    public void setQuick(final int offset, final byte val) {
        this._data[offset] = val;
    }
    
    public void clear() {
        this.clear(10);
    }
    
    public void clear(final int capacity) {
        this._data = new byte[capacity];
        this._pos = 0;
    }
    
    public void reset() {
        this._pos = 0;
        this.fill((byte)0);
    }
    
    public void resetQuick() {
        this._pos = 0;
    }
    
    public byte remove(final int offset) {
        final byte old = this.get(offset);
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
    
    public void transformValues(final TByteFunction function) {
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
        final byte tmp = this._data[i];
        this._data[i] = this._data[j];
        this._data[j] = tmp;
    }
    
    public Object clone() {
        TByteArrayList list = null;
        try {
            list = (TByteArrayList)super.clone();
            list._data = this.toNativeArray();
        }
        catch (CloneNotSupportedException ex) {}
        return list;
    }
    
    public TByteArrayList subList(final int begin, final int end) {
        if (end < begin) {
            throw new IllegalArgumentException("end index " + end + " greater than begin index " + begin);
        }
        if (begin < 0) {
            throw new IndexOutOfBoundsException("begin index can not be < 0");
        }
        if (end > this._data.length) {
            throw new IndexOutOfBoundsException("end index < " + this._data.length);
        }
        final TByteArrayList list = new TByteArrayList(end - begin);
        for (int i = begin; i < end; ++i) {
            list.add(this._data[i]);
        }
        return list;
    }
    
    public byte[] toNativeArray() {
        return this.toNativeArray(0, this._pos);
    }
    
    public byte[] toNativeArray(final int offset, final int len) {
        final byte[] rv = new byte[len];
        this.toNativeArray(rv, offset, len);
        return rv;
    }
    
    public void toNativeArray(final byte[] dest, final int offset, final int len) {
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
        if (!(other instanceof TByteArrayList)) {
            return false;
        }
        final TByteArrayList that = (TByteArrayList)other;
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
    
    public boolean forEach(final TByteProcedure procedure) {
        for (int i = 0; i < this._pos; ++i) {
            if (!procedure.execute(this._data[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachDescending(final TByteProcedure procedure) {
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
    
    public void fill(final byte val) {
        Arrays.fill(this._data, 0, this._pos, val);
    }
    
    public void fill(final int fromIndex, final int toIndex, final byte val) {
        if (toIndex > this._pos) {
            this.ensureCapacity(toIndex);
            this._pos = toIndex;
        }
        Arrays.fill(this._data, fromIndex, toIndex, val);
    }
    
    public int binarySearch(final byte value) {
        return this.binarySearch(value, 0, this._pos);
    }
    
    public int binarySearch(final byte value, final int fromIndex, final int toIndex) {
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
            final byte midVal = this._data[mid];
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
    
    public int indexOf(final byte value) {
        return this.indexOf(0, value);
    }
    
    public int indexOf(final int offset, final byte value) {
        for (int i = offset; i < this._pos; ++i) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final byte value) {
        return this.lastIndexOf(this._pos, value);
    }
    
    public int lastIndexOf(final int offset, final byte value) {
        int i = offset;
        while (i-- > 0) {
            if (this._data[i] == value) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean contains(final byte value) {
        return this.lastIndexOf(value) >= 0;
    }
    
    public TByteArrayList grep(final TByteProcedure condition) {
        final TByteArrayList list = new TByteArrayList();
        for (int i = 0; i < this._pos; ++i) {
            if (condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }
    
    public TByteArrayList inverseGrep(final TByteProcedure condition) {
        final TByteArrayList list = new TByteArrayList();
        for (int i = 0; i < this._pos; ++i) {
            if (!condition.execute(this._data[i])) {
                list.add(this._data[i]);
            }
        }
        return list;
    }
    
    public byte max() {
        if (this.size() == 0) {
            throw new IllegalStateException("cannot find maximum of an empty list");
        }
        byte max = -128;
        for (int i = 0; i < this._pos; ++i) {
            if (this._data[i] > max) {
                max = this._data[i];
            }
        }
        return max;
    }
    
    public byte min() {
        if (this.size() == 0) {
            throw new IllegalStateException("cannot find minimum of an empty list");
        }
        byte min = 127;
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
            out.writeByte(this._data[i]);
        }
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._pos = in.readInt();
        final int len = in.readInt();
        this._data = new byte[len];
        for (int i = 0; i < len; ++i) {
            this._data[i] = in.readByte();
        }
    }
}
