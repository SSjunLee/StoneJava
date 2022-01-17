package com.ljn.stone;

import com.ljn.stone.ast.impl.BlockStmt;
import com.ljn.stone.ast.impl.ParamList;
import com.ljn.stone.env.Symbols;

public class Util {
    public static int lookUp(Symbols symbols, ParamList paramList, BlockStmt blockStmt){
            Symbols newSymbols = new Symbols(symbols);
            paramList.lookUp(newSymbols);
            blockStmt.lookUp(newSymbols);
            return newSymbols.size();
    }
}
