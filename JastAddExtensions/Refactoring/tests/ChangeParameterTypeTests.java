package tests;

import junit.framework.TestCase;
import AST.MethodDecl;
import AST.Program;
import AST.RefactoringException;
import AST.TypeDecl;

public class ChangeParameterTypeTests extends TestCase {
	private void testSucc(MethodDecl md, int idx, TypeDecl type, Program in, Program out) {
		assertNotNull(in);
		assertNotNull(out);
		assertNotNull(md);
		assertNotNull(type);
		try {
			md.getParameter(idx).changeType(type);
			assertEquals(out.toString(), in.toString());
		} catch(RefactoringException rfe) {
			assertEquals(out.toString(), rfe.getMessage());
		}
	}
	
	private void testSucc(String methname, int idx, String typename, Program in, Program out) {
		assertNotNull(in);
		MethodDecl md = in.findMethod(methname);
		TypeDecl type = in.findType(typename);
		testSucc(md, idx, type, in, out);
	}
	
	private void testSucc(String hosttypename, String methname, int idx, String typename, Program in, Program out) {
		assertNotNull(in);
		TypeDecl host = in.findType(hosttypename);
		assertNotNull(host);
		MethodDecl md = null;
		if(methname.contains("("))
			md = (MethodDecl)host.localMethodsSignature(methname).iterator().next();
		else
			md = host.findMethod(methname);
		TypeDecl type = in.findType(typename);
		testSucc(md, idx, type, in, out);
	}
	
	private void testFail(MethodDecl md, int idx, TypeDecl type, Program in) {
		assertNotNull(in);
		assertNotNull(md);
		assertNotNull(type);
		try {
			md.getParameter(idx).changeType(type);
			assertEquals("<failure>", in.toString());
		} catch(RefactoringException rfe) {
		}
	}
	
	private void testFail(String methname, int idx, String typename, Program in) {
		assertNotNull(in);
		MethodDecl md = in.findMethod(methname);
		TypeDecl type = in.findType(typename);
		testFail(md, idx, type, in);
	}
	
	private void testFail(String hosttypename, String methname, int idx, String typename, Program in) {
		assertNotNull(in);
		TypeDecl host = in.findType(hosttypename);
		assertNotNull(host);
		MethodDecl md = null;
		if(methname.contains("("))
			md = (MethodDecl)host.localMethodsSignature(methname).iterator().next();
		else
			md = host.findMethod(methname);
		TypeDecl type = in.findType(typename);
		testFail(md, idx, type, in);
	}
	
	public void test1() {
		testSucc("m", 0, "java.lang.Object",
			Program.fromClasses("class A { void m(A a) { } }"),
			Program.fromClasses("class A { void m(Object a) { } }"));
	}
	
	public void test2() {
		testSucc("A", "m", 0, "java.lang.Object",
				Program.fromClasses("class A { void m(A a) { } }",
									"class B extends A { void m(A a) { } }"),
				Program.fromClasses("class A { void m(Object a) { } }",
						            "class B extends A { void m(Object a) { } }"));		
	}
	
	public void test3() {
		testSucc("B", "m", 0, "java.lang.Object",
				Program.fromClasses("class A { void m(A a) { } }",
									"class B extends A { void m(A a) { } }"),
				Program.fromClasses("class A { void m(Object a) { } }",
						            "class B extends A { void m(Object a) { } }"));		
	}
	
	public void test4() {
		testFail("A", "m(A)", 0, "java.lang.Object",
				 Program.fromClasses("class A { void m(Object o) { } " +
				 					 "          void m(A a) { } }"));
	}
	
	public void test5() {
		testFail("A", "m", 0, "java.lang.Object",
				 Program.fromClasses("class A { void m(A a) { a.m(this); } }"));
	}
	
	public void test6() {
		testSucc("A", "m(A)", 0, "java.lang.Object",
				 Program.fromClasses("class Super { void m(Object o) { } }",
				 					 "class A extends Super { void m(A a) { } }"),
				 Program.fromClasses("class Super { private void m(Object o) { } }",
						 			 "class A extends Super { void m(Object a) { } }"));
	}
	
	private final Program example =
		Program.fromClasses(
				"class A {" +
				"	public static void main(String[] args) {" +
				"		Outer.C c = new Outer.C();" +
				"		System.out.println(c.zip() + c.zap());" +
				"	}" +
				"}",
				"interface I {" +
				"	int foo(Outer.C x);" +
				"}",
				"class Outer {" +
				"	private static class B implements I {" +
				"		public int foo(Object o) {" +
				"			return 1;" +
				"		}" +
				"		public int foo(C x) {" + 
				"			return x.foo(new Object()) + 2;" + 
				"		}" +
				"		public int foo(Object o, Outer.C x) {" +
				"			return 7;" +
				"		}" +
				"		public int zip(){ return foo(this);  }" + 
				"	}" +
				"	static class C extends B {" +
				"		public int zap(){ return foo(this,this); }" +
				"	}" +
				"}");
	
	public void test7() {
		testFail("B", "foo(Outer.C)", 0, "I", example);
	}
	
	public void test8() {
		testSucc("B", "foo(Outer.C)", 0, "Outer.B",
				example,
				Program.fromClasses(
				"class A {" +
				"	public static void main(String[] args) {" +
				"		Outer.C c = new Outer.C();" +
				"		System.out.println(c.zip() + c.zap());" +
				"	}" +
				"}",
				"interface I {" +
				"	int foo(Outer.B x);" +
				"}",
				"class Outer {" +
				"	static class B implements I {" +
				"		public int foo(Object o) {" +
				"			return 1;" +
				"		}" +
				"		public int foo(B x) {" + 
				"			return x.foo(new Object()) + 2;" + 
				"		}" +
				"		public int foo(Object o, Outer.C x) {" +
				"			return 7;" +
				"		}" +
				"		public int zip(){ return foo((Object)this);  }" + 
				"	}" +
				"	static class C extends B {" +
				"		public int zap(){ return foo(this,this); }" +
				"	}" +
				"}"));		
	}
	
	public void test9() {
		testFail("A", "m", 0, "A",
				Program.fromClasses("class A { void m(Object o) { } { m(new Object()); } }"));
	}
	
	public void test10() {
		testSucc("A", "m", 0, "java.lang.Object",
				Program.fromClasses(
				"class A {" +
				"  B b;" +
				"  void m(A a) {" +
				"    b.n(a);" +
				"  }" +
				"}" +
				"class B {" +
				"  void n(A a) { }" +
				"}"),
				Program.fromClasses(
				"class A {" +
				"  B b;" +
				"  void m(Object a) {" +
				"    b.n(a);" +
				"  }" +
				"}" +
				"class B {" +
				"  void n(Object a) { }" +
				"}"));
	}
	
	public void test11() {
		testSucc("Super", "m", 0, "java.lang.Object",
				Program.fromClasses(
				"class Super {" +
				"  public void m(String s) { }" +
				"}",
				"interface I {" +
				"  void m(String s);" +
				"}",
				"class A extends Super implements I { }"),
				Program.fromClasses(
				"class Super {" +
				"  public void m(Object s) { }" +
				"}",
				"interface I {" +
				"  void m(Object s);" +
				"}",
				"class A extends Super implements I { }"));
	}
	
	public void test12() {
		testFail("A", "m", 0, "java.lang.Object",
				Program.fromClasses(
				"class A {" +
				"  void m(String s) {" +
				"    String[] ss = { s };" +
				"  }" +
				"}"));
	}
	
	public void test13() {
		testSucc("A", "m", 0, "java.lang.Object",
				Program.fromClasses(
				"class A {" +
				"  void m(String s) {" +
				"    n(s, \"\");" +
				"  }" +
				"  void n(String s1, String s2) {" +
				"    System.out.println(23);" +
				"  }" +
				"  void n(String s, Object o) {" +
				"    System.out.println(42);" +
				"  }" +
				"  { n(null, \"\"); }" +
				"}"),
				Program.fromClasses(
				"class A {" +
				"  void m(Object s) {" +
				"    n(s, \"\");" +
				"  }" +
				"  void n(Object s1, String s2) {" +
				"    System.out.println(23);" +
				"  }" +
				"  void n(String s, Object o) {" +
				"    System.out.println(42);" +
				"  }" +
				"  { n((Object)null, (String)\"\"); }" +
				"}"));
	}
	
	public void test14() {
		testSucc("A", "m", 0, "java.lang.Object",
				Program.fromClasses(
				"class A {" +
				"  void m(String s) {" +
				"    new B(s, \"\");" +
				"  }" +
				"  { new B(null, \"\"); }" +
				"}",
				"class B {" +
				"  B(String s1, String s2) {" +
				"    System.out.println(23);" +
				"  }" +
				"  B(String s, Object o) {" +
				"    System.out.println(42);" +
				"  }" +
				"}"),
				Program.fromClasses(
				"class A {" +
				"  void m(Object s) {" +
				"    new B(s, \"\");" +
				"  }" +
				"  { new B((Object)null, (String)\"\"); }" +
				"}",
				"class B {" +
				"  B(Object s1, String s2) {" +
				"    System.out.println(23);" +
				"  }" +
				"  B(String s, Object o) {" +
				"    System.out.println(42);" +
				"  }" +
				"}"));
	}
}
