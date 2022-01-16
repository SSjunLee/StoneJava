package com.ljn.stone.rules;

import com.ljn.stone.ast.ASTList;
import com.ljn.stone.ast.ASTree;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public abstract class Factory {
    public abstract ASTree make0(Object arg) throws Exception;

    protected final static String factoryName = "create";

    public ASTree make(Object arg) {
        try {
            return make0(arg);
        } catch (IllegalArgumentException e1) {
            throw e1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Factory getForAstList(Class<? extends ASTree> clazz) {
        Factory factory = get(clazz, List.class);
        if (factory == null) {
            factory = new Factory() {
                @Override
                public ASTree make0(Object arg) throws Exception {
                    List<ASTree> res = (List<ASTree>) arg;
                    if (res.size() == 1) {
                        return res.get(0);
                    } else {
                        return new ASTList(res);
                    }
                }
            };
        }
        return factory;
    }

    public static Factory get(Class<? extends ASTree> clazz, Class<?> argType) {
        if (clazz == null) return null;
        try {
            final Method m = clazz.getMethod(factoryName, new Class<?>[]{argType});
            return new Factory() {
                @Override
                public ASTree make0(Object arg) throws Exception {
                    return (ASTree) m.invoke(null, arg);
                }
            };
        } catch (NoSuchMethodException e) {
            try {
                final Constructor<? extends ASTree> constructor = clazz.getConstructor(argType);
                return new Factory() {
                    @Override
                    public ASTree make0(Object arg) throws Exception {
                        return constructor.newInstance(arg);
                    }
                };
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(e);
            }
        }
    }

}
