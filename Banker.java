public class Banker 
{
	
	private final String name;
	private int money = 100000;
	
	public Banker(String name)
	{
		this.name = name;
	}
	
	public void changeMoney(int changeAmount)
	{
		this.money += changeAmount;
	}
	
	//GETTER-SETTERS
	public String getName() {return this.name;}
	
	public int getMoney() {return this.money;}
	//

}