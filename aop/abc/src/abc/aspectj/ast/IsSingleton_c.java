package abc.aspectj.ast;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

import abc.aspectj.ast.IsSingleton;

public class IsSingleton_c extends PerClause_c implements IsSingleton
{


    public IsSingleton_c(Position pos)
    {
	super(pos);
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("issingleton()"); // ajc requires the brackets
    }

    public abc.weaving.aspectinfo.Per makeAIPer() {
	return new abc.weaving.aspectinfo.Singleton(position());
    }

}
