package com.eteks.sweethome3d.adaptive.security.ifcSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import com.eteks.sweethome3d.resources.Res;

public class ConfigLoader {

  

  private final UserPreferences preferences;
  private File sweetHomeLibraryObjects;
  private File ifcWordsToLookFor;

  private String securityCategoryName = "Security";

  private static ConfigLoader instance = null;
  
  public static ConfigLoader getInstance(UserPreferences preferences)
  {

    if(instance == null)
    {
      instance = new ConfigLoader(preferences);
      return instance;
    }
    else
    {
      return instance;
    }
  }

  
  protected ConfigLoader(UserPreferences preferences)
  {
    this.preferences = preferences;
    this.sweetHomeLibraryObjects = this.readSweetHomeLibraryObj();
    this.ifcWordsToLookFor = this.readWordsToLook();
    
  }


  private UserPreferences getUserPreferences() {
    return this.preferences;
  }

  private  File readWordsToLook() {
    return getFileFromName("" + getReadWordsToLookFileName());
  }

  private  File readSweetHomeLibraryObj() {
    
    return getFileFromName("" + getSweetLibFileName());
  }

  protected File getFileFromName(String name)
  {
    Class<Res> classe =Res.class;
    URL url = classe.getResource(name);
    URI uri=null;
    try {
      uri = url.toURI();
    } catch (URISyntaxException ex) {

      ex.printStackTrace();
    }
    File file = new File(uri);
    return file;

  }

  protected String getReadWordsToLookFileName()
  {
    return "words.txt";
  }

  protected String getSweetLibFileName()
  {
    return "libsec.txt";
  }


  /**
   * 
   * Building Object Type  -> SweetHome3D name
   * so for instance   
   * "CCTV"  is associated with Camera
   * @return
   */
  protected SecurityNameAndMap getCatalogNamesFromFile()
  {

    Map<String, BuildingObjectType> catalog = new HashMap<String, BuildingObjectType>();
    
    List<String> fileCont = getfileContent(this.sweetHomeLibraryObjects.getAbsolutePath());
    String categoryName = fileCont.get(0);
    for(int i = 1; i<fileCont.size(); i++)
    {
      String line = fileCont.get(i);
      String[] coupleObjectNameType = line.split(",");
      String objectName = coupleObjectNameType[0];
      String objectType = coupleObjectNameType[1];
      BuildingObjectType buildingObjType = BuildingObjectType.valueOf(objectType);
      catalog.put(objectName, buildingObjType);
      
    }
    
    SecurityNameAndMap snm = new SecurityNameAndMap();
    snm.catalog = catalog;
    snm.securityCategoryName = categoryName;
    return snm;
  }
  
  public class SecurityNameAndMap
  {
    public Map<String, BuildingObjectType> catalog;
    public String securityCategoryName;
    
  }
  

  protected List<String> getfileContent(String filePath)
  {
    List<String> fileCont = new ArrayList<String>();
    BufferedReader br= null;
    try {
      br = new BufferedReader(new FileReader(filePath));
      String line;
      while ((line = br.readLine()) != null) {
        // process the line.
        fileCont.add(line);
      }
      br.close();
    }
    catch(Exception e)
    {}
    
     return fileCont; 
  }


  protected  List<String> stringToLookFor(BuildingObjectType objectType)
  {
    
    List<String> words = new ArrayList<String>();

    List<String> content = this.getfileContent(this.ifcWordsToLookFor.getAbsolutePath());
    for(String line : content)
    {
      
      String [] parts = line.split(",");
      String object = parts[0];
      BuildingObjectType type = BuildingObjectType.valueOf(object);
      if(objectType.equals(type))
      {
        for(int i=1; i<parts.length; i++)
        {
          words.add(parts[i]);
        }
      }
      
    }
    
    return words;
    
  }

  public Map<BuildingObjectType, HomePieceOfFurniture> createFurnitureMap()
  {
    Map<BuildingObjectType, HomePieceOfFurniture> catalogFurniture =
        new HashMap<BuildingObjectType, HomePieceOfFurniture>();
     
    List<FurnitureCategory> categories= getUserPreferences().getFurnitureCatalog().getCategories();
    
    SecurityNameAndMap snm = this.getCatalogNamesFromFile();
    Map<String, BuildingObjectType> catalog = snm.catalog;
    securityCategoryName = snm.securityCategoryName;
    
    for(FurnitureCategory category : categories )
    {
      String securityCategoryName = this.getSecurityCategoryName();
      if(category.getName().equals(securityCategoryName))
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

          
          BuildingObjectType typeOBJ = catalog.get(pieceName);
          catalogFurniture.put(typeOBJ, hopf);
        }
      }
    }    


    return catalogFurniture;
  }

  private String getSecurityCategoryName() {
   
    return this.securityCategoryName;
  }












}
