/**
 * Test if the extends keyword is supported in the jpi definition.
 */

import org.aspectj.testing.Tester;

jpi void JP(int amount);
jpi void JP2(int items) extends JP(items);
jpi void bar(int z, Object x) //there is not foo definition 
	throws Exception 
		extends foo(a,b); //a and b aren't defined.

public class InheritanceSyntax{

    exhibits void JP(int i):
        execution(* foo(..)) && args(i);

    void foo(int x){}

    public static void main(String[] args){
        new InheritanceSyntax().foo(5);
    }
}
