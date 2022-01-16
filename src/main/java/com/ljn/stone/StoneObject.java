package com.ljn.stone;

import com.ljn.stone.env.Env;

public class StoneObject {
    private static class AccessException extends Exception{}
    protected Env env;

    public StoneObject(Env env) {
        this.env = env;
    }

    public Object read(String name) throws AccessException {
        return getEnv(name).get(name);
    }

    public void write(String member,Object value) throws AccessException {
        getEnv(member).putNew(member,value);
    }

    /**
     * 对象的成员必须定义在对象的env里
     * @param member
     * @return
     * @throws AccessException
     */
    protected Env getEnv(String member) throws AccessException{
        Env e = env.where(member);
        if(e != null && e == env){
            return e;
        }
        throw new AccessException();
    }
}
