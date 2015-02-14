package com.eteks.sweethome3d.adaptive.security.extractingobjs;

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
/**
 * This class gather some basic methods for reading parameters from config files
 * 
 * @author Edoardo Pasi
 */
public class ConfigLoader {



  protected SecurityNameAndMap namesConventionsSweetHome;
  private final UserPreferences preferences;
  private File sweetHomeLibraryObjects;
  private File ifcWordsToLookFor;

  private String securityCategoryName = "Security";

  private static ConfigLoader instance = null;
  private Map<String, List<String>>  fileContentCache = new HashMap<String, List<String>>(); 
  
  /**
   * Map that stores the graphical representation associated to each {@link BuildingObjectType}
   * 
   * @return
   */
  public Map<BuildingObjectType, HomePieceOfFurniture> createTypeToFurnitureMap()
  {
    Map<BuildingObjectType, HomePieceOfFurniture> catalogFurniture =
        new HashMap<BuildingObjectType, HomePieceOfFurniture>();
  
    List<FurnitureCategory> categories= getUserPreferences().getFurnitureCatalog().getCategories();
  
    namesConventionsSweetHome = this.getCatalogNamesFromFile();
    Map<String, BuildingObjectType> catalog = namesConventionsSweetHome.sweetCatalogToType;
    securityCategoryName = namesConventionsSweetHome.securityCategoryName;
  
    for(FurnitureCategory category : categories )
    {
      String securityCategoryName = this.getSecurityCategoryName();
      if(category.getName().equals(securityCategoryName))
      {
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

  /***
   * es.  CCTV -> Camera surveillance N090211
   * @param type
   * @return
   */
  public String getSweetHomeNameForType(BuildingObjectType type)
  {
    return this.namesConventionsSweetHome.TypeToSweetName.get(type);
  }
  
  public BuildingObjectType getTypeForSweetHomeName(String sweethomeName)
  {
    return this.namesConventionsSweetHome.sweetCatalogToType.get(sweethomeName);
  }
  
  
  

  protected ConfigLoader(UserPreferences preferences)
  {
    this.preferences = preferences;
    this.sweetHomeLibraryObjects = this.readSweetHomeLibraryObj();
    this.ifcWordsToLookFor = this.readWordsToLook();

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
   * Return {@link SecurityNameAndMap} that wraps maps of conversions
   * SweetHome3D name -> Building Object Type 
   * so for instance   
   * Camera surveillance N090211  is associated with CCTV
   * @return
   */
  protected SecurityNameAndMap getCatalogNamesFromFile()
  {

    Map<String, BuildingObjectType> catalog = new HashMap<String, BuildingObjectType>();
    Map<BuildingObjectType, String> catalogBack = new HashMap<BuildingObjectType, String>();
    
    List<String> fileSweetToType =  getfileContent(this.sweetHomeLibraryObjects.getAbsolutePath());
    String categoryName = fileSweetToType.get(0);
    for(int i = 1; i<fileSweetToType.size(); i++)
    {
      String line = fileSweetToType.get(i);
      String[] coupleObjectNameType = line.split(",");
      String objectName = coupleObjectNameType[0];
      String objectType = coupleObjectNameType[1];
      BuildingObjectType buildingObjType = BuildingObjectType.valueOf(objectType);
      catalog.put(objectName, buildingObjType);
      catalogBack.put(buildingObjType, objectName);

    }

    SecurityNameAndMap snm = new SecurityNameAndMap();
    snm.sweetCatalogToType = catalog;
    snm.TypeToSweetName = catalogBack;
    snm.securityCategoryName = categoryName;
    return snm;
  }

  public class SecurityNameAndMap
  {
    public Map<String, BuildingObjectType> sweetCatalogToType;
    public Map<BuildingObjectType, String> TypeToSweetName;
    public String securityCategoryName;

  }


  protected List<String> getfileContent(String filePath)
  {
    List<String> cachedCont = this.fileContentCache.get(filePath);
    if(cachedCont != null)
      return cachedCont;
    if(cachedCont == null)
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

      this.fileContentCache.put(filePath, fileCont);
      return fileCont;
    }

    return null;

  }

  /**
   * <pre>
   * Building Object Type -> IFC possible Names 
   * CCTV ->  camera, CCTV, videocamera, surevelliance
   * </pre>
   * @param objectType
   * @return
   */
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

  private UserPreferences getUserPreferences() {
    return this.preferences;
  }


  private  File readWordsToLook() {
    return getFileFromName("" + getReadWordsToLookFileName());
  }


  private  File readSweetHomeLibraryObj() {
  
    return getFileFromName("" + getSweetLibFileName());
  }


  private String getSecurityCategoryName() {

    return this.securityCategoryName;
  }












}
