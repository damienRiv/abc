package abc.aspectj.ast;

import abc.aspectj.visit.*;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

/**
 * negation of a type pattern expression.
 * @author Oege de Moor
 * @author Aske Simon Christensen
 *
 */
public class TPENot_c extends TypePatternExpr_c implements TPENot
{
    protected TypePatternExpr tpe;

    public TPENot_c(Position pos, TypePatternExpr tpe)  {
	super(pos);
        this.tpe = tpe;
    }

    public TypePatternExpr getTpe() {
	return tpe;
    }

    protected TPENot_c reconstruct(TypePatternExpr tpe) {
	if (tpe != this.tpe) {
	    TPENot_c n = (TPENot_c) copy();
	    n.tpe = tpe;
	    return n;
	}
	return this;
    }

    public Node visitChildren(NodeVisitor v) {
	TypePatternExpr tpe = (TypePatternExpr) visitChild(this.tpe, v);
	return reconstruct(tpe);
    }

    public Precedence precedence() {
	return Precedence.UNARY;
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
	w.write("!");
        printSubExpr(tpe, true, w, tr);
    }

    public String toString() {
	return "!"+tpe;
    }

    public boolean matchesClass(PatternMatcher matcher, PCNode cl) {
	return !tpe.matchesClass(matcher, cl);
    }

    public boolean matchesClassArray(PatternMatcher matcher, PCNode cl, int dim) {
	return !tpe.matchesClassArray(matcher, cl, dim);
    }

    public boolean matchesPrimitive(PatternMatcher matcher, String prim) {
	return !tpe.matchesPrimitive(matcher, prim);
    }

    public boolean matchesPrimitiveArray(PatternMatcher matcher, String prim, int dim) {
	return !tpe.matchesPrimitiveArray(matcher, prim, dim);
    };

    public ClassnamePatternExpr transformToClassnamePattern(AJNodeFactory nf) throws SemanticException {
	ClassnamePatternExpr cpe = tpe.transformToClassnamePattern(nf);
	return nf.CPENot(position, cpe);
    }

    public boolean equivalent(TypePatternExpr t) {
	if (t instanceof TPENot) {
	    return (tpe.equivalent(((TPENot)t).getTpe()));
	} else return false;
    }

}
