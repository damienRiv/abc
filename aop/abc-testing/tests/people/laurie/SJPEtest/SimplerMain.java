public class SimplerMain {

  static int k = 13;
  
  public static void main(String args[])
    { int k = 100; 
      B b1 = new B(5,6);
      b1.foo(3);
      try {
	int x = b1.foo(4) / 0;
      }
      catch (Exception e)
      { System.out.println("                         " +    e);
      }
    }
}

class A {

  int x = 4;

  A (int x) 
    { this.x = x; }
} 

class B extends A {
  int y;
  static int k = 4;
  static int j = 5;
  static int l = 6;

  int foo(int i) 
    { y = y + i;  
      return(i+1); 
    }

  B (int x, int y) 
    { super( x + y + k);  // this field-get should be in preinit
      this.y = x + y;  // this field-set should be in constr. exec.
    }
}

aspect Aspects {

  static private int aspectnesting = 0;

  static void message(String s)
    { for (int i=0; i<aspectnesting; i++) System.out.print("---+");
      System.out.println(s);
    }

  // before advice
  before () : !within(Aspects) 
   { message(
	  "BEFORE: " +  
          thisJoinPointStaticPart.getKind() + " at " +
		      thisJoinPointStaticPart.getSourceLocation() );
     message(
	  "  enclosed by " + 
	  thisEnclosingJoinPointStaticPart.getKind() + " at "
	  +  thisEnclosingJoinPointStaticPart.getSourceLocation());
          if (!thisJoinPointStaticPart.getKind().equals("exception-handler")) 
        	aspectnesting++;
	      }

  // after advice
  after () returning : !within(Aspects)  && !handler(*)
            {  aspectnesting--;
	      message(
		  "AFTER: " +  
		      thisJoinPointStaticPart.getKind() + " at " +
		      thisJoinPointStaticPart.getSourceLocation() );
	      message(
		  "  enclosed by " + 
		  thisEnclosingJoinPointStaticPart.getKind() + " at "
		  +  thisEnclosingJoinPointStaticPart.getSourceLocation());


	     }
}
