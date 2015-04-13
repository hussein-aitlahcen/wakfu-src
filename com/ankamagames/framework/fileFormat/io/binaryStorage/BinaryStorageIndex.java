package com.ankamagames.framework.fileFormat.io.binaryStorage;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface BinaryStorageIndex {
    String name();
}
