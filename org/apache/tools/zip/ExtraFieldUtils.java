package org.apache.tools.zip;

import java.util.zip.*;
import java.util.*;
import java.util.concurrent.*;

public class ExtraFieldUtils
{
    private static final int WORD = 4;
    private static final Map<ZipShort, Class<?>> implementations;
    
    public static void register(final Class<?> c) {
        try {
            final ZipExtraField ze = (ZipExtraField)c.newInstance();
            ExtraFieldUtils.implementations.put(ze.getHeaderId(), c);
        }
        catch (ClassCastException cc) {
            throw new RuntimeException(c + " doesn't implement ZipExtraField");
        }
        catch (InstantiationException ie) {
            throw new RuntimeException(c + " is not a concrete class");
        }
        catch (IllegalAccessException ie2) {
            throw new RuntimeException(c + "'s no-arg constructor is not public");
        }
    }
    
    public static ZipExtraField createExtraField(final ZipShort headerId) throws InstantiationException, IllegalAccessException {
        final Class<?> c = ExtraFieldUtils.implementations.get(headerId);
        if (c != null) {
            return (ZipExtraField)c.newInstance();
        }
        final UnrecognizedExtraField u = new UnrecognizedExtraField();
        u.setHeaderId(headerId);
        return u;
    }
    
    public static ZipExtraField[] parse(final byte[] data) throws ZipException {
        return parse(data, true, UnparseableExtraField.THROW);
    }
    
    public static ZipExtraField[] parse(final byte[] data, final boolean local) throws ZipException {
        return parse(data, local, UnparseableExtraField.THROW);
    }
    
    public static ZipExtraField[] parse(final byte[] data, final boolean local, final UnparseableExtraField onUnparseableData) throws ZipException {
        final List<ZipExtraField> v = new ArrayList<ZipExtraField>();
        int start = 0;
    Label_0346:
        while (start <= data.length - 4) {
            final ZipShort headerId = new ZipShort(data, start);
            final int length = new ZipShort(data, start + 2).getValue();
            if (start + 4 + length > data.length) {
                switch (onUnparseableData.getKey()) {
                    case 0: {
                        throw new ZipException("bad extra field starting at " + start + ".  Block length of " + length + " bytes exceeds remaining" + " data of " + (data.length - start - 4) + " bytes.");
                    }
                    case 2: {
                        final UnparseableExtraFieldData field = new UnparseableExtraFieldData();
                        if (local) {
                            field.parseFromLocalFileData(data, start, data.length - start);
                        }
                        else {
                            field.parseFromCentralDirectoryData(data, start, data.length - start);
                        }
                        v.add(field);
                    }
                    case 1: {
                        break Label_0346;
                    }
                    default: {
                        throw new ZipException("unknown UnparseableExtraField key: " + onUnparseableData.getKey());
                    }
                }
            }
            else {
                try {
                    final ZipExtraField ze = createExtraField(headerId);
                    if (local || !(ze instanceof CentralDirectoryParsingZipExtraField)) {
                        ze.parseFromLocalFileData(data, start + 4, length);
                    }
                    else {
                        ((CentralDirectoryParsingZipExtraField)ze).parseFromCentralDirectoryData(data, start + 4, length);
                    }
                    v.add(ze);
                }
                catch (InstantiationException ie) {
                    throw new ZipException(ie.getMessage());
                }
                catch (IllegalAccessException iae) {
                    throw new ZipException(iae.getMessage());
                }
                start += length + 4;
            }
        }
        final ZipExtraField[] result = new ZipExtraField[v.size()];
        return v.toArray(result);
    }
    
    public static byte[] mergeLocalFileDataData(final ZipExtraField[] data) {
        final boolean lastIsUnparseableHolder = data.length > 0 && data[data.length - 1] instanceof UnparseableExtraFieldData;
        final int regularExtraFieldCount = lastIsUnparseableHolder ? (data.length - 1) : data.length;
        int sum = 4 * regularExtraFieldCount;
        for (final ZipExtraField element : data) {
            sum += element.getLocalFileDataLength().getValue();
        }
        final byte[] result = new byte[sum];
        int start = 0;
        for (int i = 0; i < regularExtraFieldCount; ++i) {
            System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
            System.arraycopy(data[i].getLocalFileDataLength().getBytes(), 0, result, start + 2, 2);
            final byte[] local = data[i].getLocalFileDataData();
            System.arraycopy(local, 0, result, start + 4, local.length);
            start += local.length + 4;
        }
        if (lastIsUnparseableHolder) {
            final byte[] local2 = data[data.length - 1].getLocalFileDataData();
            System.arraycopy(local2, 0, result, start, local2.length);
        }
        return result;
    }
    
    public static byte[] mergeCentralDirectoryData(final ZipExtraField[] data) {
        final boolean lastIsUnparseableHolder = data.length > 0 && data[data.length - 1] instanceof UnparseableExtraFieldData;
        final int regularExtraFieldCount = lastIsUnparseableHolder ? (data.length - 1) : data.length;
        int sum = 4 * regularExtraFieldCount;
        for (final ZipExtraField element : data) {
            sum += element.getCentralDirectoryLength().getValue();
        }
        final byte[] result = new byte[sum];
        int start = 0;
        for (int i = 0; i < regularExtraFieldCount; ++i) {
            System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
            System.arraycopy(data[i].getCentralDirectoryLength().getBytes(), 0, result, start + 2, 2);
            final byte[] local = data[i].getCentralDirectoryData();
            System.arraycopy(local, 0, result, start + 4, local.length);
            start += local.length + 4;
        }
        if (lastIsUnparseableHolder) {
            final byte[] local2 = data[data.length - 1].getCentralDirectoryData();
            System.arraycopy(local2, 0, result, start, local2.length);
        }
        return result;
    }
    
    static {
        implementations = new ConcurrentHashMap<ZipShort, Class<?>>();
        register(AsiExtraField.class);
        register(JarMarker.class);
        register(UnicodePathExtraField.class);
        register(UnicodeCommentExtraField.class);
        register(Zip64ExtendedInformationExtraField.class);
    }
    
    public static final class UnparseableExtraField
    {
        public static final int THROW_KEY = 0;
        public static final int SKIP_KEY = 1;
        public static final int READ_KEY = 2;
        public static final UnparseableExtraField THROW;
        public static final UnparseableExtraField SKIP;
        public static final UnparseableExtraField READ;
        private final int key;
        
        private UnparseableExtraField(final int k) {
            super();
            this.key = k;
        }
        
        public int getKey() {
            return this.key;
        }
        
        static {
            THROW = new UnparseableExtraField(0);
            SKIP = new UnparseableExtraField(1);
            READ = new UnparseableExtraField(2);
        }
    }
}
