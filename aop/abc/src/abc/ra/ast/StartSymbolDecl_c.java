package abc.ra.ast;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import polyglot.types.Flags;
import polyglot.util.Position;
import abc.aspectj.ast.FormalPattern;
import abc.aspectj.ast.ModifierPattern;
import abc.aspectj.ast.Pointcut;
import abc.tm.ast.SymbolDecl;
import abc.tm.ast.SymbolDecl_c;
import abc.tm.ast.SymbolKind;
import abc.tm.ast.TMNodeFactory;

public class StartSymbolDecl_c extends SymbolDecl_c implements SymbolDecl {

	private static final Position POS = Position.compilerGenerated();

	public StartSymbolDecl_c(Position pos, String name, TMNodeFactory nf) {
		super(pos,name,createKind(nf),createPC(nf));
	}

	/**
	 * Creates pointcut <code>execution(* *.main(String[]))</code>
	 */
	private static Pointcut createPC(TMNodeFactory nf) {
		List<ModifierPattern> mods = new LinkedList<ModifierPattern>();
		mods.add(nf.ModifierPattern(POS, Flags.PUBLIC, true));
		mods.add(nf.ModifierPattern(POS, Flags.STATIC, true));
		
		List<FormalPattern> formals = new LinkedList<FormalPattern>();
		formals.add(nf.TypeFormalPattern(POS, nf.TPEArray(POS, nf.TPERefTypePat(POS, nf.RTPName(POS, nf.SimpleNamePattern(POS, "String"))), 1)));
		return nf.PCExecution(
				POS,
				nf.MethodPattern(
						POS,
						mods,
						nf.TPEUniversal(POS),
						nf.ClassTypeDotId(POS, nf.CPEUniversal(POS), nf.SimpleNamePattern(POS, "main")),
						formals,
						Collections.emptyList()
				)
		);
	}

	private static SymbolKind createKind(TMNodeFactory nf) {
		return nf.BeforeSymbol(POS);
	}

}
