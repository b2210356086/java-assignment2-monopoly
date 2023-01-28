import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player 
{
	
	private final String name;
	private int money = 15000;
	public boolean isParking = false;
	public boolean inJail = false;
    private int turnsInJail = 0;
    private int position = 1;
    private int oldPosition = 1;
    private int numRailroads = 0; //Number of Railroads player has
    private ArrayList<Property> properties = new ArrayList<Property>(); //List of Properties player has
    private String propertyString = "have: "; //Later useful for showing Properties player has
    private static ListJsonReader listJson = new ListJsonReader(); //Used to get chanceList and communityList
    
    public Player(String name)
	{
		this.name = name;
	}
	
	public void moveTo(int newPosition)
	{
		this.position = newPosition;
	}
		
	//This Action method is where Player and Square(Board) interaction happens
	public void action(Board board, Player otherPlayer, Banker banker, int chanceTurn, int communityTurn, int dice, FileWriter writer) throws IOException
	{
		//Cash for passing GO Square
		if ((dice + this.oldPosition) > 40)
		{
			this.money += 200;
			banker.changeMoney(-200);
		}
		//
		//This block executes if landed Square is a Property(Calculations for Renting,Buying,Already Having the Property)
		if (board.getBoard().get(this.position-1) instanceof Property)
		{
			if(!otherPlayer.properties.contains(board.getBoard().get(this.position-1)) && !this.properties.contains(board.getBoard().get(this.position-1))) 
			{
				if (this.money >= board.getBoard().get(this.position-1).getCost())
				{
					if (board.getBoard().get(this.position-1).getType().equals("RailRoad"))
					{
						this.numRailroads++;
					}	
					this.properties.add((Property)board.getBoard().get(this.position-1));
					this.propertyString += board.getBoard().get(this.position-1).getName()+",";
					this.money -= board.getBoard().get(this.position-1).getCost();
					banker.changeMoney(board.getBoard().get(this.position-1).getCost());
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("bought " + board.getBoard().get(this.position-1).getName() + "\n");
				}
				else
				{
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("goes bankrupt\n");
				}
			}
			else if (!otherPlayer.properties.contains(board.getBoard().get(this.position-1)) && this.properties.contains(board.getBoard().get(this.position-1)))
			{
				noProcessingOutput(otherPlayer, dice, writer);
				writer.write("has " + board.getBoard().get(this.position-1).getName()+"\n");
			}
			else
			{
				int rent;
				if (board.getBoard().get(this.position-1).getType().equals("Land"))
				{
					if (board.getBoard().get(this.position-1).getCost() <= 2000)
					{
						rent = (board.getBoard().get(this.position-1).getCost() * 4) / 10;
					}
					else if (board.getBoard().get(this.position-1).getCost() > 3000)
					{
						rent = (board.getBoard().get(this.position-1).getCost() * 35) / 100;
					}
					else
					{
						rent = (board.getBoard().get(this.position-1).getCost() * 3) / 10;
					}
				}
				else if (board.getBoard().get(this.position-1).getType().equals("Company"))
				{
					rent = dice * 4;
				}
				else
				{
					rent = 25 * otherPlayer.numRailroads;
				}
				if (this.money >= rent)
				{
					this.money -= rent;
					otherPlayer.money += rent;
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("paid rent for " + board.getBoard().get(this.position-1).getName()+"\n");
				}
				else
				{
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("goes bankrupt\n");
				}
			}
		}
		//
		//This block executes if landed Square is a Utility(GO, Tax, Chance / Community Cards, Jail, Parking)
		else
		{
			if (board.getBoard().get(this.position-1).getName().equals("Go"))
			{
				noProcessingOutput(otherPlayer, dice, writer);
				writer.write("is in GO square\n");
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Tax"))
			{
				this.money -= 100;
				banker.changeMoney(100);
				noProcessingOutput(otherPlayer, dice, writer);
				writer.write("paid Tax\n");
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Chance"))
			{
				switch(chanceTurn%6)
				{
				case 1:
					this.moveTo(1);
					this.money += 200;
					banker.changeMoney(-200);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getChanceList().get(0) + "\n");
					break;
				case 2:
					if (board.getBoard().get(this.position-1).getIndex() > 27)
					{
						this.money += 200;
						banker.changeMoney(-200);
					}
					this.moveTo(27);
					if (otherPlayer.properties.contains(board.getBoard().get(this.position-1)))
					{
						int rent = (board.getBoard().get(this.position-1).getCost() * 3) / 10;
						if (this.money >= rent)
						{
							this.money -= rent;
							otherPlayer.money += rent;
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("draw " + listJson.getChanceList().get(1) + " " + this.name + " paid rent for " + board.getBoard().get(this.position-1).getName()+"\n");
						}
						else
						{
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("goes bankrupt\n");
						}
					}
					else if (this.properties.contains(board.getBoard().get(this.position-1)))
					{
						noProcessingOutput(otherPlayer, dice, writer);
						writer.write("draw " + listJson.getChanceList().get(1) + " " + this.name + " has " + board.getBoard().get(this.position-1).getName()+"\n");
					}
					else
					{
						if (this.money >= board.getBoard().get(this.position-1).getCost())
						{
							this.money -= board.getBoard().get(this.position-1).getCost();
							banker.changeMoney(board.getBoard().get(this.position-1).getCost());
							this.properties.add((Property)board.getBoard().get(this.position-1));
							this.propertyString += board.getBoard().get(this.position-1).getName()+",";
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("draw " + listJson.getChanceList().get(1) + " " + this.name + " bought " + board.getBoard().get(this.position-1).getName()+"\n");
						}
						else
						{
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("goes bankrupt\n");
						}
					}
					break;
				case 3:
					this.moveTo(this.position-3);
					if (otherPlayer.properties.contains(board.getBoard().get(this.position-1)))
					{
						int rent;
						if (board.getBoard().get(this.position-1).getCost() <= 2000)
						{
							rent = (board.getBoard().get(this.position-1).getCost() * 4) / 10;
						}
						else if (board.getBoard().get(this.position-1).getCost() > 3000)
						{
							rent = (board.getBoard().get(this.position-1).getCost() * 35) / 100;
						}
						else
						{
							rent = (board.getBoard().get(this.position-1).getCost() * 3) / 10;
						}
						if (this.money >= rent)
						{
							this.money -= rent;
							otherPlayer.money += rent;
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("draw " + listJson.getChanceList().get(2) + " " + this.name + " paid rent for " + board.getBoard().get(this.position-1).getName()+"\n");
						}
						else
						{
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("goes bankrupt\n");
						}
					}
					else if (this.properties.contains(board.getBoard().get(this.position-1)))
					{
						noProcessingOutput(otherPlayer, dice, writer);
						writer.write("draw " + listJson.getChanceList().get(2) + " " + this.name + " has " + board.getBoard().get(this.position-1).getName()+"\n");
					}
					else
					{
						if (this.money >= board.getBoard().get(this.position-1).getCost())
						{
							this.money -= board.getBoard().get(this.position-1).getCost();
							banker.changeMoney(board.getBoard().get(this.position-1).getCost());
							this.properties.add((Property)board.getBoard().get(this.position-1));
							this.propertyString += board.getBoard().get(this.position-1).getName()+",";
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("draw " + listJson.getChanceList().get(2) + " " + this.name + " bought " + board.getBoard().get(this.position-1).getName()+"\n");
						}
						else
						{
							noProcessingOutput(otherPlayer, dice, writer);
							writer.write("goes bankrupt\n");
						}
					}
					break;
				case 4:
					this.money -= 15;
					banker.changeMoney(15);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getChanceList().get(3)+"\n");
					break;
				case 5:
					this.money += 150;
					banker.changeMoney(-150);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getChanceList().get(4)+"\n");
					break;
				case 0:
					this.money += 100;
					banker.changeMoney(-100);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getChanceList().get(5)+"\n");
					break;
				}
				Game.increaseChanceTurn();
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Community"))
			{
				switch(communityTurn%11)
				{
				case 1:
					this.moveTo(1);
					this.money += 200;
					banker.changeMoney(-200);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(0)+"\n");
					break;
				case 2:
					this.money += 75;
					banker.changeMoney(-75);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(1)+"\n");
					break;
				case 3:
					this.money -= 50;
					banker.changeMoney(50);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(2)+"\n");
					break;
				case 4:
					this.money += 10;
					otherPlayer.money -= 10;
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(3)+"\n");
					break;
				case 5:
					this.money += 50;
					otherPlayer.money -= 50;
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(4)+"\n");
					break;
				case 6:
					this.money += 20;
					banker.changeMoney(-20);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(5)+"\n");
					break;
				case 7:
					this.money += 100;
					banker.changeMoney(-100);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(6)+"\n");
					break;
				case 8:
					this.money -= 100;
					banker.changeMoney(100);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(7)+"\n");
					break;
				case 9:
					this.money -= 50;
					banker.changeMoney(50);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(8)+"\n");
					break;
				case 10:
					this.money += 100;
					banker.changeMoney(-100);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(9)+"\n");
					break;
				case 0:
					this.money += 50;
					banker.changeMoney(-50);
					noProcessingOutput(otherPlayer, dice, writer);
					writer.write("draw " + listJson.getCommunityChestList().get(10)+"\n");
					break;
				}
				Game.increaseCommunityTurn();
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Jail") && this.inJail==false)
			{
				noProcessingOutput(otherPlayer, dice, writer);
				writer.write("went to jail\n");
				this.inJail = true;
				this.turnsInJail++;
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Jail") && this.inJail==true && this.turnsInJail != 3)
			{
				noProcessingOutput(otherPlayer, dice, writer);
				writer.write("in jail (count=" + Integer.toString(this.turnsInJail) +")\n");
				this.inJail = true;
				this.turnsInJail++;
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Jail") && this.inJail==true && this.turnsInJail == 3)
			{
				noProcessingOutput(otherPlayer, dice, writer);
				writer.write("in jail (count=" + Integer.toString(this.turnsInJail) +")\n");
				this.inJail = false;
				this.turnsInJail = 0;
			}
			else if (board.getBoard().get(this.position-1).getName().equals("GoToJail"))
			{
				this.position = 11;
				this.action(board, otherPlayer, banker, chanceTurn, communityTurn, dice, writer);
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Park") && this.isParking == false)
			{
				noProcessingOutput(otherPlayer, dice, writer);
				writer.write("is in Free Parking\n");
				this.isParking = true;
			}
			else if (board.getBoard().get(this.position-1).getName().equals("Park") && this.isParking == true)
			{
				this.isParking = false;
			}
		}
		//
		//Bankruptcy is a thing but this is just to be sure
		if (this.money < 0)
		{
			this.money = 0;
		}
		if (otherPlayer.money < 0)
		{
			otherPlayer.money = 0;
		}
		//
	}
	
	//This noProcessingOutput method is useful to avoid repetitive code, it outputs current situation of players without a Processing part
	public void noProcessingOutput(Player otherPlayer, int dice, FileWriter writer) throws IOException
	{
		if (this.name.equals("Player 1"))
		{
			writer.write(this.name + "\t" + dice + "\t" + this.position + "\t" + this.money + "\t" + otherPlayer.money + "\t" + this.name + " ");
		}
		else
		{
			writer.write(this.name + "\t" + dice + "\t" + this.position + "\t" + otherPlayer.money + "\t" + this.money + "\t" + this.name + " ");
		}
	}
	//
	
	//This Overrided toString method is used when Show() command executes
	@Override
	public String toString()
	{
		return this.name + "\t" + this.money + "\t" + this.propertyString.substring(0,this.propertyString.length()-1);
	}
	//
	
	//GETTER-SETTERS
	public String getName() {return name;}

	public int getMoney() {return money;}

	public void setMoney(int money) 
	{
		this.money = money;
	}
	
	public int getPosition() {return this.position;}
	
	public void setOldPosition(int oldPosition)
	{
		this.oldPosition = oldPosition;
	}
	//

}