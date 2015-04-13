package org.jdom;

import org.xml.sax.*;
import java.sql.*;
import java.rmi.*;
import java.lang.reflect.*;
import java.io.*;

public class JDOMException extends Exception
{
    private static final String CVS_ID = "@(#) $RCSfile: JDOMException.java,v $ $Revision: 1.23 $ $Date: 2004/02/27 11:32:57 $ $Name: jdom_1_0 $";
    private Throwable cause;
    
    public JDOMException() {
        super("Error occurred in JDOM application.");
    }
    
    public JDOMException(final String message) {
        super(message);
    }
    
    public JDOMException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }
    
    public Throwable getCause() {
        return this.cause;
    }
    
    public String getMessage() {
        String msg = super.getMessage();
        Throwable child;
        for (Throwable parent = this; (child = getNestedException(parent)) != null; parent = child) {
            String msg2 = child.getMessage();
            if (child instanceof SAXException) {
                final Throwable grandchild = ((SAXException)child).getException();
                if (grandchild != null && msg2 != null && msg2.equals(grandchild.getMessage())) {
                    msg2 = null;
                }
            }
            if (msg2 != null) {
                if (msg != null) {
                    msg = String.valueOf(msg) + ": " + msg2;
                }
                else {
                    msg = msg2;
                }
            }
            if (child instanceof JDOMException) {
                break;
            }
        }
        return msg;
    }
    
    private static Throwable getNestedException(final Throwable parent) {
        if (parent instanceof JDOMException) {
            return ((JDOMException)parent).getCause();
        }
        if (parent instanceof SAXException) {
            return ((SAXException)parent).getException();
        }
        if (parent instanceof SQLException) {
            return ((SQLException)parent).getNextException();
        }
        if (parent instanceof InvocationTargetException) {
            return ((InvocationTargetException)parent).getTargetException();
        }
        if (parent instanceof ExceptionInInitializerError) {
            return ((ExceptionInInitializerError)parent).getException();
        }
        if (parent instanceof RemoteException) {
            return ((RemoteException)parent).detail;
        }
        Throwable nestedException = getNestedException(parent, "javax.naming.NamingException", "getRootCause");
        if (nestedException != null) {
            return nestedException;
        }
        nestedException = getNestedException(parent, "javax.servlet.ServletException", "getRootCause");
        if (nestedException != null) {
            return nestedException;
        }
        return null;
    }
    
    private static Throwable getNestedException(final Throwable parent, final String className, final String methodName) {
        try {
            final Class testClass = Class.forName(className);
            final Class objectClass = parent.getClass();
            if (testClass.isAssignableFrom(objectClass)) {
                final Class[] argClasses = new Class[0];
                final Method method = testClass.getMethod(methodName, (Class[])argClasses);
                final Object[] args = new Object[0];
                return (Throwable)method.invoke(parent, args);
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    public Throwable initCause(final Throwable cause) {
        this.cause = cause;
        return this;
    }
    
    public void printStackTrace() {
        super.printStackTrace();
        Throwable child;
        for (Throwable parent = this; (child = getNestedException(parent)) != null; parent = child) {
            System.err.print("Caused by: ");
            child.printStackTrace();
            if (child instanceof JDOMException) {
                break;
            }
        }
    }
    
    public void printStackTrace(final PrintStream s) {
        super.printStackTrace(s);
        Throwable child;
        for (Throwable parent = this; (child = getNestedException(parent)) != null; parent = child) {
            s.print("Caused by: ");
            child.printStackTrace(s);
            if (child instanceof JDOMException) {
                break;
            }
        }
    }
    
    public void printStackTrace(final PrintWriter w) {
        super.printStackTrace(w);
        Throwable child;
        for (Throwable parent = this; (child = getNestedException(parent)) != null; parent = child) {
            w.print("Caused by: ");
            child.printStackTrace(w);
            if (child instanceof JDOMException) {
                break;
            }
        }
    }
}
