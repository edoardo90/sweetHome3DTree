package com.eteks.sweethome3d.adaptive.security.ifcSecurity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.PieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;

public class ConfigLoader {
  
  private final UserPreferences preferences;
  
  public ConfigLoader(UserPreferences preferences)
  {
    this.preferences = preferences;
  }
  
  private UserPreferences getUserPreferences() {
    return this.preferences;
  }
  
  

  /**
   * 
   * Building Object Type  -> SweetHome3D name
   * so for instance   
   * "CCTV"  is associated with Camera
   * @return
   */
  protected static Map<String, BuildingObjectType> getCatalogNamesFromFile()
  {
    Map<String, BuildingObjectType> catalog = new HashMap<String, BuildingObjectType>();
    catalog.put("Camera surveillance N090211", BuildingObjectType.CCTV);
    catalog.put ("pc-21", BuildingObjectType.PC);
    catalog.put("Printer N120614", BuildingObjectType.PRINTER);
    catalog.put("Torchere N160914", BuildingObjectType.LIGHT);
    catalog.put("Conditioner LG N240211", BuildingObjectType.HVAC);
    catalog.put("Man N090512", BuildingObjectType.MAN);
    catalog.put("Woman N170408", BuildingObjectType.WOMAN);
    
    return catalog;
  }
  
  protected static List<String> stringToLookFor(BuildingObjectType objectType)
  {
    //TODO: conventions file
    //PC, desktop, computer, laptop

    List<String> words = new ArrayList<String>();
    switch(objectType)
    {
      case ACTOR:
      {
        words.add("actor");  //TODO  remove "actor"?
        break;
      }
      case CCTV :
      {
        words.add("camera");
        words.add("CCTV");
        break;
      }
      case LIGHT:
      {
        words.add("light");
        words.add("luminaire");
        words.add("lamp");
        break;
      }
      case PC:
      {
        words.add("desktop");
        words.add("computer");
        words.add("laptop");
        break;
      }
      case PRINTER:
      {
        words.add("printer");
        break;
      }
      case HVAC:
      {
        words.add("hvac");
        words.add("conditioner");
        words.add("heater");
        break;
      }
    }
    return words;
  }

  protected Map<BuildingObjectType, HomePieceOfFurniture> createFurnitureMap()
  {
    Map<BuildingObjectType, HomePieceOfFurniture> catalogFurniture =
        new HashMap<BuildingObjectType, HomePieceOfFurniture>();
    //TODO: config file for conventions sweetHome 
    List<FurnitureCategory> categories= getUserPreferences().getFurnitureCatalog().getCategories();
    for(FurnitureCategory category : categories )
    {
      if(category.getName().equals("Security"))
      {

        /**
         *  "Security"  is the name of the library that have to be imported,
         *  this maybe could be written inside an xml file  or preference file or something
         *  instead of hard coded
         *  
         *  The same is with the objects name
         */

        List<CatalogPieceOfFurniture> catalogObjs = category.getFurniture();
        for(PieceOfFurniture piece : catalogObjs)
        {
          HomePieceOfFurniture  hopf = new HomePieceOfFurniture(piece);
          
          String pieceName = piece.getName();
          Map<String, BuildingObjectType> catalog = ConfigLoader.getCatalogNamesFromFile();
          BuildingObjectType typeOBJ = catalog.get(pieceName);
          catalogFurniture.put(typeOBJ, hopf);
        }
      }
    }    


    return catalogFurniture;
  }

  
}
