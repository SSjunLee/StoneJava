package com.ljn.stone.member.opt;

import com.ljn.stone.ast.impl.ClassStmt;
import com.ljn.stone.ast.impl.DefStmt;
import com.ljn.stone.env.Env;
import com.ljn.stone.env.Symbols;
import com.ljn.stone.member.StoneClassInfo;
import java.util.List;
public class OptClassInfo extends StoneClassInfo {
    protected Symbols methods,fields;
    protected DefStmt [] methodsDefs;
    public OptClassInfo(ClassStmt classDef, Env env,Symbols methods,Symbols fields) {
        super(classDef, env);
        this.methods = methods;
        this.fields = fields;
        methodsDefs = null;
    }

    public  void setMethods(DefStmt[] methods){this.methodsDefs = methods;}
    public Integer fieldIndex(String name){return fields.find(name);}
    public Integer methodIndex(String name){return methods.find(name);}
    public OptMethod method(OptStoneObject self,int index){
        DefStmt def = methodsDefs[index];
        return new OptMethod(def.paramList(), def.body(), env(),def.locals(),self);
    }

    public void copyOf(Symbols m,Symbols f,List<DefStmt> list){
            m.append(methods);
            f.append(fields);
            for(DefStmt defStmt:methodsDefs){
                list.add(defStmt);
            }
    }


    public int size() {
        return fields.size();
    }
}
