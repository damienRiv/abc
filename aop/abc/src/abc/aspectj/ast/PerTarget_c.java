package abc.aspectj.ast;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

import abc.aspectj.types.AspectType;

/**
 * 
 * @author Oege de Moor
 *
 */
public class PerTarget_c extends PerClause_c implements PerTarget
{

    Pointcut pc;

    public PerTarget_c(Position pos, Pointcut pc)
    {
	super(pos);
        this.pc = pc;
    }


	public int kind() {
		return AspectType.PER_TARGET;
	}
	
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("pertarget (");
        print(pc, w, tr);
        w.write(")");
    }
    
	protected PerTarget_c reconstruct(Pointcut pc) {
		if (pc != this.pc ) {
			PerTarget_c n = (PerTarget_c) copy();
			n.pc = pc;
			return n;
		}
		return this;
	}


	public Node visitChildren(NodeVisitor v) {
		Pointcut pc = (Pointcut) visitChild(this.pc, v);
		return reconstruct(pc);
	}

    public abc.weaving.aspectinfo.Per makeAIPer() {
	return new abc.weaving.aspectinfo.PerTarget(pc.makeAIPointcut(),position());
    }
}
