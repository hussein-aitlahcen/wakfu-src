package org.jdom.filter;

import java.io.*;

public interface Filter extends Serializable
{
    boolean matches(Object p0);
}
