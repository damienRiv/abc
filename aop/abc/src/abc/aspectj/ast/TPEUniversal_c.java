package abc.aspectj.ast;

import abc.aspectj.visit.*;

import polyglot.ast.*;

import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

/**
 * A type pattern expression that matches everything.
 * @author Oege de Moor
 * @author Aske Simon Christensen
 *
 */
public class TPEUniversal_c extends TypePatternExpr_c implements TPEUniversal
{
    public TPEUniversal_c(Position pos)  {
	super(pos);
    }

    public Precedence precedence() {
	return Precedence.UNARY;
    }

    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
	w.write("*");
    }

    public String toString() {
	return "*";
    }

    public boolean matchesClass(PatternMatcher matcher, PCNode cl) {
	return true;
    }

    public boolean matchesClassArray(PatternMatcher matcher, PCNode cl, int dim) {
	return true;
    }

    public boolean matchesPrimitive(PatternMatcher matcher, String prim) {
	return true;
    }

    public boolean matchesPrimitiveArray(PatternMatcher matcher, String prim, int dim) {
	return true;
    }

    public ClassnamePatternExpr transformToClassnamePattern(AJNodeFactory nf) throws SemanticException {
	return nf.CPEUniversal(position);
    }

    public boolean equivalent(TypePatternExpr t) {
	if (t instanceof TPEUniversal) {
	    return true;
	} else return false;
    }

}
