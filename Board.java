import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Board
{

	//Constructs the Utility(Non-Property Squares)
	Square[] utilities = new Square[] {new Utility("Go",1),new Utility("Community",3)
			,new Utility("Tax",5),new Utility("Chance",8),new Utility("Jail",11)
			,new Utility("Community",18),new Utility("Park",21),new Utility("Chance",23)
			,new Utility("GoToJail",31),new Utility("Community",34),new Utility("Chance",37)
			,new Utility("Tax",39)};
	//
	
	//Builds the Board by adding Property and Utility Squares
	ArrayList<Square> board = new ArrayList<Square>();
	
	PropertyJsonReader propertyJson = new PropertyJsonReader();
	{
		for (int i=0; i<propertyJson.getProperties().size(); i++)
		{
			board.add(propertyJson.getProperties().get(i));
		}
	}
	
	{
		for (int i=0 ; i<utilities.length ;i++)
		{
			board.add(utilities[i]);
		}
	}
	//
	
	//Sorts the Squares by indexes for the final look of Board
	{
		Collections.sort(board , new Comparator<Square>()
		{
			public int compare(Square s1, Square s2)
			{
				return s1.getIndex() - s2.getIndex();
			}
		});
	}
	//
	
	//GETTER-SETTERS
	public ArrayList<Square> getBoard() {return this.board;}
	//
 
}