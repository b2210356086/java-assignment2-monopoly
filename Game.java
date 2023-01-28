import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Game
{
	
	Player player1 = new Player("Player 1");
	Player player2 = new Player("Player 2");
	Banker banker = new Banker("Banker");
	Board board = new Board();
	private static int chanceTurn = 1; //Keeps track of Chance Card order
	private static int communityTurn = 1; //Keeps track of Community Chest Card order
	private static String fileName; //Name of the command file
	FileWriter writer;
	Scanner scanner;
	
	{
	try 
	{
		scanner = new Scanner(new File(fileName));
		writer = new FileWriter("output.txt");
		while (scanner.hasNext() && player1.getMoney()>0 && player2.getMoney()>0) //Condition for the game to continue
		{
			String line = scanner.nextLine();
			if (line.startsWith("Player 1"))
			{
				//Moves player to new position
				player1.setOldPosition(player1.getPosition());
				int dice = Integer.parseInt(line.substring(9, line.length()));
				if (!player1.inJail) 
				{
					player1.moveTo((player1.getPosition() + dice)%40);
					if (player1.getPosition() == 0)
					{
						player1.moveTo(40);
					}
				}
				//
				player1.action(board,player2,banker,chanceTurn,communityTurn,dice,writer); //Does action depending on new Square
				//One time check for last round of player 2 if player 1 is out of money
				if (player1.getMoney() <= 0)
				{
					player2.setOldPosition(player2.getPosition());
					String tempLine = scanner.nextLine();
					int tempDice = Integer.parseInt(tempLine.substring(9, tempLine.length()));
					if (!player2.inJail && !player2.isParking)
					{
						player2.moveTo((player2.getPosition() + tempDice)%40);
						if (player2.getPosition() == 0)
						{
							player2.moveTo(40);
						}
					}
					player2.action(board,player1,banker,chanceTurn,communityTurn,tempDice,writer);
				}
				//
			}
			else if (line.startsWith("Player 2"))
			{
				player2.setOldPosition(player2.getPosition());
				int dice = Integer.parseInt(line.substring(9, line.length()));
				if (!player2.inJail && !player2.isParking)
				{
					player2.moveTo((player2.getPosition() + dice)%40);
					if (player2.getPosition() == 0)
					{
						player2.moveTo(40);
					}
				}
				player2.action(board,player1,banker,chanceTurn,communityTurn,dice,writer);
			}
			else
			{
				//Show Command
				writer.write("-----------------------------------------------------------------------------------------------------------\n");
				writer.write(player1 + "\n");
				writer.write(player2 + "\n");
				writer.write(banker.getName() + "\t" + banker.getMoney()+"\n");
				if (player1.getMoney() < player2.getMoney())
				{
					writer.write("Winner\tPlayer 2\n");
				}
				else
				{
					writer.write("Winner\tPlayer 1\n");
				}
				writer.write("-----------------------------------------------------------------------------------------------------------\n");
			    //
			}
		}
		//Show Command for Game Over
		writer.write("-----------------------------------------------------------------------------------------------------------\n");
		writer.write(player1 + "\n");
		writer.write(player2 + "\n");
		writer.write(banker.getName() + "\t" + banker.getMoney() + "\n");
		if (player1.getMoney() < player2.getMoney())
		{
			writer.write("Winner\tPlayer 2\n");
		}
		else
		{
			writer.write("Winner\tPlayer 1\n");
		}
		writer.write("-----------------------------------------------------------------------------------------------------------\n");
		writer.close();
		//
	} 
	catch (FileNotFoundException e) 
	{
		e.printStackTrace();
	} 
	catch (IOException e) 
	{
		e.printStackTrace();
	}
	}
	
	//Increases index for next Chance/Community card
	public static void increaseChanceTurn()
	{
		chanceTurn++;
	}
	
	public static void increaseCommunityTurn()
	{
		communityTurn++;
	}
	//
	
	//GETTER-SETTERS
	public static void setFileName(String name)
	{
		fileName = name;
	}
	//
	
}