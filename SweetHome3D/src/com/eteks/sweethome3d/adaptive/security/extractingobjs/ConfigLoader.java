package com.eteks.sweethome3d.adaptive.security.extractingobjs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;
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
  private static UserPreferences preferences;
  private File sweetHomeLibraryObjectsFile;
  private File ifcWordsToLookForFile;
  private File rolesFile;
  private File attributesPossibleFile;
  
  private String securityCategoryName = "Security";

  private static ConfigLoader instance = null;
  private Map<String, List<String>>  fileContentCache = new HashMap<String, List<String>>();
  private SecurityNameAndMap snm = null;
  private Map<BuildingObjectType, HomePieceOfFurniture> typeToFurniture = new HashMap<BuildingObjectType, HomePieceOfFurniture>();
  private Map<String, Set<BuildingObjectAttribute>>  attributesPossible = null; 
  private Set<String> availableRoles = new TreeSet<String>();
  
  
  
  /**
   * Map that stores the graphical representation associated to each {@link BuildingObjectType}
   * 
   * @return
   */
  private Map<BuildingObjectType, HomePieceOfFurniture> createTypeToFurnitureMap()
  {
    Map<BuildingObjectType, HomePieceOfFurniture> catalogFurniture =
        new HashMap<BuildingObjectType, HomePieceOfFurniture>();
  
    List<FurnitureCategory> categories= getUserPreferences().getFurnitureCatalog().getCategories();
  
    SecurityNameAndMap namesConventionsSweetHome = this.getCatalogNamesFromFile();
    Map<String, BuildingObjectType> catalog = namesConventionsSweetHome.sweetCatalogToType;
    this.securityCategoryName = namesConventionsSweetHome.securityCategoryName;
  
    for(FurnitureCategory category : categories )
    {
      String securityCategoryName = this.getSecurityCategoryName();
      if(category.getName().equals(securityCategoryName))
      {
        List<CatalogPieceOfFurniture> catalogObjs = category.getFurniture();
        for(PieceOfFurniture piece : catalogObjs)
        {
          HomePieceOfFurniture  hopf = new HomePieceOfFurniture(piece);
  
          String pieceName = hopf.getOriginalName();
  
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
    if(this.namesConventionsSweetHome != null)
        return this.namesConventionsSweetHome.TypeToSweetName.get(type);
    else
    {
      this.namesConventionsSweetHome = this.getNamesConventions();
      return this.getSweetHomeNameForType(type);
    }
  }
  
  public BuildingObjectType getTypeForSweetHomeName(String sweethomeName)
  {
    BuildingObjectType type = this.getNamesConventions().sweetCatalogToType.get(sweethomeName);
    if (type == null)
       type = BuildingObjectType.UNKNOWN_OBJECT;
    return type;
    
  }
  
  public Map<BuildingObjectType, HomePieceOfFurniture> getMapTypeToFurniture()
  {
    if(typeToFurniture == null)
        typeToFurniture = this.createTypeToFurnitureMap();
    return this.typeToFurniture;
  }
  

  protected ConfigLoader(UserPreferences preferences)
  {
    ConfigLoader.preferences = preferences;
    this.sweetHomeLibraryObjectsFile = this.readSweetHomeLibraryObj();
    this.ifcWordsToLookForFile = this.readWordsToLook();
    this.attributesPossibleFile = this.readAttribute(); 
    this.setMapOfLibraryObjects(preferences);
    this.rolesFile = this.readRoles();
  }
  


  /**
   * Create a map from object type to homePieceOfFurniture, this map is used by the objects
   * This is used by objects of the class {@link BuildingObjectContained} to call getPieceOfForniture
   * @param preferences
   */
  private void setMapOfLibraryObjects(UserPreferences preferences)
  {
    Map<BuildingObjectType, HomePieceOfFurniture> map =   this.getMapTypeToFurniture();
    preferences.setFornitureMap(map); 
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
  
  protected String getReadRolesFileName()
  {
    return "roles.txt";
  }

  protected String getReadWordsToLookFileName()
  {
    return "words.txt";
  }

  protected String getSweetLibFileName()
  {
    return "libsec.txt";
  }
  
  protected String getAttributesFileName()
  {
    return "attributes.txt";
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
    if(snm != null)
        return snm;
    
    Map<String, BuildingObjectType> catalog = new HashMap<String, BuildingObjectType>();
    Map<BuildingObjectType, String> catalogBack = new HashMap<BuildingObjectType, String>();
    
    List<String> fileSweetToType =  getfileContent(this.sweetHomeLibraryObjectsFile.getAbsolutePath());
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
    
    snm = new SecurityNameAndMap();
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
  
  public Set<BuildingObjectAttribute> getPossibleAttributesForObject(String buildingObjectOriginalName)
  {
    if(this.attributesPossible == null)
    {
      this.initPossibleAttributes();
    }
    return this.attributesPossible.get(buildingObjectOriginalName);
  }
  
  private void initPossibleAttributes()
  {
    List<String> possibleAttributesCont = this.getfileContent(this.attributesPossibleFile.getAbsolutePath());
    
    for(String fileRowAttribute : possibleAttributesCont)
    {
      //PIECE_ORIGINAL_NAME,ATTRIBUTE_NAME,ATTRIBUTE_TYPE,ATTRIBUTE_DEFAULT_VALUE
      String [] cols = fileRowAttribute.split(",");
      String originalName = cols[0];
      if(this.attributesPossible == null)
      {
        this.attributesPossible = new HashMap<String, Set<BuildingObjectAttribute>>();
      }
      Set<BuildingObjectAttribute> attrForName = this.attributesPossible.get(originalName);
      if(attrForName == null)
      {
        attrForName = new HashSet<BuildingObjectAttribute>();
        this.attributesPossible.put(originalName, attrForName);
      }
      
      BuildingObjectAttribute boa = new BuildingObjectAttribute(fileRowAttribute);
      
      attrForName.add(boa);
      
    }
  }
  
  public Set<String> getAvailableRoles()
  {
    if(this.availableRoles == null || this.availableRoles.size() == 0 )
    {
      List<String> cont = this.getfileContent(this.rolesFile.getAbsolutePath());
      this.availableRoles.addAll(cont); 
    }
    return this.availableRoles;
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

    List<String> content = this.getfileContent(this.ifcWordsToLookForFile.getAbsolutePath());
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
  
  private File readRoles()
  {
    return getFileFromName(""+  getReadRolesFileName());
  }

  private  File readWordsToLook() {
    return getFileFromName("" + getReadWordsToLookFileName());
  }


  private  File readSweetHomeLibraryObj() {
  
    return getFileFromName("" + getSweetLibFileName());
  }
  
  private File readAttribute()
  {
    return getFileFromName("" + this.getAttributesFileName());
  }

  private String getSecurityCategoryName() {

    return this.securityCategoryName;
  }


  public SecurityNameAndMap getNamesConventions() {
     if (this.snm != null)
     {
       return this.snm;
     }
     else
     {
       this.snm = this.getCatalogNamesFromFile();
       return this.snm;
     }
     
  }


  public static ConfigLoader getInstance() {
      if (preferences == null)
        throw new IllegalStateException("configLoader should have preferences set before asking it !");
      return new ConfigLoader(preferences);
  }


  public boolean isARole(String roleStr) {
    return this.getAvailableRoles().contains(roleStr);
  }












}
