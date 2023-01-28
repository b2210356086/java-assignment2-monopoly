public class Property extends Square
{
	
	private final String name;
	private final String type;
	private final int id;
	private final int cost;
	
	public Property(String name, String type, int id, int cost)
	{
		super(id);
		this.name = name;
		this.type = type;
		this.id = id;
		this.cost = cost;
	}
	
	//GETTER-SETTERS
	public String getName() {return this.name;}
	
	public String getType() {return this.type;}
	
	public int getId() {return this.id;}
	
	public int getCost() {return this.cost;}
	//

}