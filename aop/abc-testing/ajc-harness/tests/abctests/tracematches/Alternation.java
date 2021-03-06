import org.aspectj.testing.Tester;

public class Alternation {
    public static void f() { System.out.println("f"); }
    public static void g() { System.out.println("g"); }
    public static void main(String[] args) {
	f(); f(); g(); g(); f();/*match*/
	g(); f(); f();/*match*/ g();
	Tester.expectEvent("fg*f+");
	Tester.expectEvent("fg*f+");
	Tester.checkAllEvents();
    }
}

aspect FG {
    tracematch() {
	sym f after : call(* *.f(..));
	sym g after : call(* *.g(..));

	f (f|g) (f|g) f

	    {
		Tester.event("fg*f+");
		System.out.println("fg*f+");
	    }
    }
}
