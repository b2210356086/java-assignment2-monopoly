import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ListJsonReader
{
	
	private JSONArray chanceList;
	private JSONArray communityChestList;
	
	@SuppressWarnings("unchecked")
	public ListJsonReader()
    {
		//Adds Chance and Community Cards to Two separate lists for easier usage later
		JSONParser processor = new JSONParser();
        try (Reader file = new FileReader("list.json"))
        {
            JSONObject jsonfile = (JSONObject) processor.parse(file);
            
            JSONArray tempChanceList = (JSONArray) jsonfile.get("chanceList");
            chanceList = new JSONArray();
            for(Object i:tempChanceList)
            {
            	chanceList.add(((JSONObject)i).get("item"));
            }
            
            JSONArray tempCommunityChestList = (JSONArray) jsonfile.get("communityChestList");
            communityChestList = new JSONArray();
            for(Object i:tempCommunityChestList)
            {
            	communityChestList.add(((JSONObject)i).get("item"));	
            }
        }
        //
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
     }
	 
	 //GETTER-SETTERS
     public JSONArray getChanceList() {return chanceList;}

     public JSONArray getCommunityChestList() {return communityChestList;}
     //

}