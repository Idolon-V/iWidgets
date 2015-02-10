package net.idolon.iwidgets.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Class<?> tryClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> tryClassForName(String className, ClassLoader classLoader) {
        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Method makeMethodAccessible(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw propagate(e);
        }
    }

    public static Method tryMakeMethodAccessible(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return makeMethodAccessible(clazz, name, parameterTypes);
        } catch (Exception e) {
            return null;
        }
    }

    public static Field makeFieldAccessible(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw propagate(e);
        }
    }

    public static Field tryMakeFieldAccessible(Class<?> clazz, String fieldName) {
        try {
            return makeFieldAccessible(clazz, fieldName);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Field field, Object object) {
        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        }
    }

    public static <T> T tryGet(Field field, Object object) {
        try {
            return get(field, object);
        } catch (Exception e) {
            return null;
        }
    }

    public static void set(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        }
    }

    public static void trySet(Field field, Object object, Object value) {
        try {
            set(field, object, value);
        } catch (Exception e) {
            // ignore it and safely return
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Method m, Object object, Object... args) {
        try {
            return (T) m.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw propagate(e);
        } catch (InvocationTargetException e) {
            throw propagate(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T tryInvoke(Method m, Object object, Object... args) {
        try {
            return (T) m.invoke(object, args);
        } catch (InvocationTargetException e) {
            throw propagate(e);
        } catch (Exception e) {
            return null;
        }
    }

    private static RuntimeException propagate(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        throw new RuntimeException(throwable);
    }

    private ReflectionUtil() {
        // hidden and empty
    }

}
