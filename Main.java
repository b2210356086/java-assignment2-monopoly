public class Main 
{
	
	public static void main(String[] args) 
	{
		
		Game.setFileName(args[0]); //Sets the name of command file which is a static variable
		new Game(); //Starts a new game of Monopoly
	
	}

}