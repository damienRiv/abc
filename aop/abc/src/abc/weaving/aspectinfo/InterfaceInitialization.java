package abc.weaving.aspectinfo;

import java.util.Hashtable;

import soot.*;

import polyglot.util.Position;

import abc.weaving.residues.*;
import abc.weaving.matching.*;

/** Handler for <code>initialization</code> shadow pointcut. */
public class InterfaceInitialization extends ShadowPointcut {
    private ClassnamePattern pattern;

    public InterfaceInitialization(ClassnamePattern pattern,Position pos) {
	super(pos);
	this.pattern=pattern;
    }

    public ClassnamePattern getPattern() {
	return pattern;
    }


    protected Residue matchesAt(ShadowMatch sm) {
	if(!(sm instanceof InterfaceInitializationShadowMatch)) return null;
	InterfaceInitializationShadowMatch ism=(InterfaceInitializationShadowMatch) sm;
	if(!(getPattern().matchesClass(ism.getInterface()))) return null;
	return AlwaysMatch.v;
    }

    public String toString() {
	return "interfaceinitialization()";
    }

	/* (non-Javadoc)
	 * @see abc.weaving.aspectinfo.Pointcut#equivalent(abc.weaving.aspectinfo.Pointcut, java.util.Hashtable)
	 */
	public boolean equivalent(Pointcut otherpc, Hashtable renaming) {
		if (otherpc instanceof InterfaceInitialization) {
			return pattern.equivalent(((InterfaceInitialization)otherpc).getPattern());
		} else return false;
	}

}