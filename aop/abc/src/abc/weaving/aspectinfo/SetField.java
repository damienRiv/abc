package abc.weaving.aspectinfo;

import java.util.Hashtable;

import soot.*;
import soot.jimple.*;

import polyglot.util.Position;

import abc.weaving.matching.*;
import abc.weaving.residues.*;

/** Handler for <code>set</code> shadow pointcut. */
public class SetField extends ShadowPointcut {
    private FieldPattern pattern;

    public SetField(FieldPattern pattern,Position pos) {
	super(pos);
	this.pattern = pattern;
    }

    public FieldPattern getPattern() {
	return pattern;
    }

    protected Residue matchesAt(ShadowMatch sm) {
	if(!(sm instanceof SetFieldShadowMatch)) return null;
	SootFieldRef fieldref=((SetFieldShadowMatch) sm).getFieldRef();

	if(!getPattern().matchesFieldRef(fieldref)) return null;
	return AlwaysMatch.v;
    }

    public String toString() {
	return "set("+pattern+")";
    }

	/* (non-Javadoc)
	 * @see abc.weaving.aspectinfo.Pointcut#equivalent(abc.weaving.aspectinfo.Pointcut, java.util.Hashtable)
	 */
	public boolean equivalent(Pointcut otherpc, Hashtable renaming) {
		if (otherpc instanceof SetField) {
			return pattern.equivalent(((SetField)otherpc).getPattern());
		} else return false;
	}

}
