
package abc.aspectj.types;

import java.util.List;
import java.util.LinkedList;

import polyglot.util.Position;
import polyglot.util.UniqueID;

import polyglot.ext.jl.types.ConstructorInstance_c;

import polyglot.ast.ConstructorCall;
import polyglot.ast.Expr;
import polyglot.ast.New;
import polyglot.ast.Formal;
import polyglot.ast.TypeNode;

import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Flags;
import polyglot.types.TypeSystem;
import polyglot.types.LocalInstance;


import abc.aspectj.ast.AJNodeFactory;

/**
 * @author Oege de Moor
 *
 */

public class InterTypeConstructorInstance_c
	extends ConstructorInstance_c
	implements InterTypeMemberInstance {
		
	protected ConstructorInstance mangled;
	protected ClassType mangleType;
    protected ClassType interfaceTarget;
	protected ClassType origin;
	protected String identifier;
	
	public ClassType origin() {
		return origin;
	}
	
	public String identifier() {
		return identifier;
	}

	/** create a constructor that can be traced back to the aspect
	 * that introduced it.
	 * 
	 */
	public InterTypeConstructorInstance_c(
		TypeSystem ts,
		Position pos,
		String identifier,
		ClassType origin,
		ClassType container,
		Flags flags,
		List formalTypes,
		List excTypes) {
		super(ts, pos, container, flags, formalTypes, excTypes);
		this.origin = origin;
		this.identifier = identifier;
		if (container.toClass().flags().isInterface())
			interfaceTarget = container.toClass();
		else
			interfaceTarget = null;
		
		if (flags().isPrivate() || flags().isPackage()) {
			mangleType = origin; // not quite right, same as ajc.
								// ought to generate a fresh type for each aspect
			List fts = new LinkedList(formalTypes);
			fts.add(mangleType);
			mangled = new ConstructorInstance_c(ts,pos,container,flags,fts,excTypes);
		}
	}
	
	
	public ClassType interfaceTarget() {
		return interfaceTarget;
	}
	
	public Flags origFlags() {
		return flags();
	}
	
	public void setMangle() {
		// to be filled in!
	}
	
	public void setMangleNameComponent() {
	}
	
	public ConstructorInstance mangled() {
		if (flags().isPrivate() || flags().isPackage())
			return mangled;
		else
			return this;
	}
	
	public ConstructorCall mangledCall(ConstructorCall cc, AJNodeFactory nf, AJTypeSystem ts) {
		if (flags().isPrivate() || flags.isPackage()) {
			Expr nl = nf.NullLit(cc.position());
			nl = nl.type(mangleType);
			List args = new LinkedList(cc.arguments());
			args.add(nl);
			ConstructorCall nc = (ConstructorCall) cc.arguments(args);
			return nc.constructorInstance(mangled()); 
		} else return cc;
	}

	public New mangledNew(New cc, AJNodeFactory nf, AJTypeSystem ts) {
		New nc;
		if (flags().isPrivate() || flags().isPackage()) {
			Expr nl = nf.NullLit(cc.position());
			nl = nl.type(mangleType);
			List args = new LinkedList(cc.arguments());
			args.add(nl);
		 	nc = (New) cc.arguments(args);
			return nc.constructorInstance(mangled()); 
		} else return cc;
	}
	
	public Formal mangledFormal(AJNodeFactory nf, AJTypeSystem ts) {
		TypeNode tn = nf.CanonicalTypeNode(position,mangleType);
		String name = UniqueID.newID("formal");
		Formal mangledFormal = nf.Formal(position,Flags.NONE,tn,name);
		LocalInstance li = ts.localInstance(position,Flags.NONE,mangleType,name);
		return mangledFormal.localInstance(li);
	}
}
