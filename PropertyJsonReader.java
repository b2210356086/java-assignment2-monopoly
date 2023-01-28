import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class PropertyJsonReader 
{
	
	private ArrayList<Property> properties = new ArrayList<Property>();
	
	public PropertyJsonReader()
    {
		 //Builds Property Objects and Adds these to an ArrayList
		 JSONParser processor = new JSONParser();
         try (Reader file = new FileReader("property.json"))
         {
             JSONObject jsonfile = (JSONObject) processor.parse(file);
             
             JSONArray Land = (JSONArray) jsonfile.get("1");
             for(Object i:Land)
             {
				 properties.add(new Property((String)((JSONObject)i).get("name"),
						 "Land",Integer.parseInt((String)((JSONObject)i).get("id")),
						 Integer.parseInt((String)((JSONObject)i).get("cost"))));
             }
             
             JSONArray RailRoad = (JSONArray) jsonfile.get("2");
             for(Object i:RailRoad)
             {
            	 properties.add(new Property((String)((JSONObject)i).get("name"),
						 "RailRoad",Integer.parseInt((String)((JSONObject)i).get("id")),
						 Integer.parseInt((String)((JSONObject)i).get("cost"))));
             }
			 
             JSONArray Company = (JSONArray) jsonfile.get("3");
             for(Object i:Company)
             {
            	 properties.add(new Property((String)((JSONObject)i).get("name"),
						 "Company",Integer.parseInt((String)((JSONObject)i).get("id")),
						 Integer.parseInt((String)((JSONObject)i).get("cost"))));
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
	 public ArrayList<Property> getProperties() {return properties;}
	 //

}