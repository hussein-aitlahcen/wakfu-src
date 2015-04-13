package com.ankamagames.framework.kernel.core.common.collections;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;

public class EmptyIterable<T> implements Iterable<T>
{
    @Override
    public Iterator<T> iterator() {
        return new EmptyIterator<T>();
    }
}
