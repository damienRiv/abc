package abc.eaj.weaving.aspectinfo;

import soot.*;

import polyglot.util.Position;

import abc.weaving.aspectinfo.*;
import abc.weaving.matching.ShadowMatch;
import abc.weaving.residues.*;

import abc.eaj.weaving.matching.*;

/**
 * @author Pavel Avgustinov
 *
 */

public class ArrayGet extends ShadowPointcut {
	public ArrayGet(Position pos) {
		super(pos);
	}
	
	protected Residue matchesAt(ShadowMatch sm) {
		if(sm instanceof ArrayGetShadowMatch)
			return AlwaysMatch.v();
		else 
			return NeverMatch.v();
	}

	public String toString() {
		return "arrayget()";
	}
	
	public boolean unify(Pointcut other, Unification uni) {
        if (other.getClass() == this.getClass())
            return true;
        else // Do the right thing if otherpc was a local vars pc
            return LocalPointcutVars.unifyLocals(this,other, uni);
	}

}
