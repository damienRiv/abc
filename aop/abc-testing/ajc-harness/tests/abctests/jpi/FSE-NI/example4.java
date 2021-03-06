import org.aspectj.testing.Tester;
import java.util.Date;
import Util.*;

jpi void CheckingOut(Item item, float price, int amount, Customer cus);
jpi void Buying(Item item, float price, int amount, Customer cus) extends CheckingOut(item,price,amount,cus);
jpi void Renting(Item item, float price, int amount, Customer cus) extends CheckingOut(item,price,amount,cus);

public class ShoppingSession{
	
	exhibits void Buying(Item i, float price, int amount, Customer c):
		call(* checkOut(..)) 
		&& args(i,price,amount,c) 
		&& withincode(void ShoppingSession.buy(..));

	exhibits void Renting(Item i, float price, int amount, Customer c):
		call(* checkOut(..)) 
		&& args(i,price,amount,c) 
		&& withincode(void ShoppingSession.rent(..));
	
	public static int counter = 0;
	
	public static ShoppingCart sc = new ShoppingCart();
	Invoice inv = new Invoice();
	
	public void checkOut(Item item, float price, int amount, Customer cus){
		sc.add(item, price, amount);
		inv.add(item, amount, cus);
	}
	
	public void buy(Item item, float price, int amount, Customer cus){
		checkOut(item, price, amount, cus);
		//do something else...		
	}

	public void rent(Item item, float price, int amount, Customer cus, Date date){
		checkOut(item, price, amount, cus);
		//do something else...
	}

	public static void main(String[] args){
		ShoppingSession sp = new ShoppingSession();		
		sp.buy(Item.factory(),Item.getPrice(),11,Customer.factory(false));
		sp.buy(Item.factory(),Item.getPrice(),11,Customer.factory(true));
		sp.rent(Item.factory(),Item.getPrice(),11,Customer.factory(false), new Date());
		sp.rent(Item.factory(),Item.getPrice(),11,Customer.factory(true), new Date());
		
		Tester.check(4==ShoppingSession.counter,"must be executed only 4 times, but was executed "+counter);
		
	}
}

aspect Discount{
	
	private static float checkingOutDiscount(Customer cus){
		return (float) (cus.hasBirthday() ? 0.95 : 1);
	}
	
	void around CheckingOut(Item item, float price, int amount, Customer cus){
		ShoppingSession.counter++;
		float factor = checkingOutDiscount(cus);
		proceed(item, price*factor, amount, cus);
	}
}