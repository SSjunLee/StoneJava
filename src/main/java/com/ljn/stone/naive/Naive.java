package com.ljn.stone.naive;

import com.ljn.stone.env.Env;
import com.ljn.stone.exception.StoneException;

import java.lang.reflect.Method;

public class Naive {
    public Env register(Env env) {
        appendNaives(env);
        return env;
    }

    protected void appendNaives(Env env) {
        append(env, "print", Naive.class, "print", Object.class);
        append(env, "length", Naive.class, "length", Object.class);
        append(env, "currentTime", Naive.class, "currentTime");
    }

    public static int length(Object s) {
        if (s instanceof String) return ((String) s).length();
        return ((Object[]) s).length;
    }

    public static long startTime = System.currentTimeMillis();

    public static int currentTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }


    protected void append(Env env, String name, Class<?> clazz, String methodName, Class<?>... params) {
        try {
            Method method = clazz.getMethod(name, params);
            env.putNew(methodName, new NaiveFunction(method, methodName));
        } catch (Exception e) {
            //e.printStackTrace();
            throw new StoneException("no such method " + name);
        }
    }


    public static void print(Object args) {
        System.out.println(args.toString());
    }
}
