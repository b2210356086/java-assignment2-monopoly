//Square is an abstract class because we don't want any Square objects,
//we just want to have Property and Utility Squares and we will keep these together in a board with Polymorphism 

public abstract class Square 
{
	
	private int index;
	private String name;
	private String type;
	private int id;
	private int cost;
	
	public Square(int index)
	{
		this.index = index;
	}
	
	//GETTER-SETTERS
	public int getIndex() {return this.index;}
	
	public String getName() {return this.name;}

	public String getType() {return this.type;}

	public int getId() {return this.id;}

	public int getCost() {return this.cost;}
	//

}