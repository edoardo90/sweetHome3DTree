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

import com.eteks.sweethome3d.adaptive.security.assets.Asset;
import com.eteks.sweethome3d.adaptive.security.assets.AssetType;
import com.eteks.sweethome3d.adaptive.security.assets.ObjectAbility;
import com.eteks.sweethome3d.adaptive.security.assets.attributes.BuildingObjectAttribute;
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
public class SavedConfigurationsLoader {



  protected SecurityNameAndMap namesConventionsSweetHome;
  private static UserPreferences preferences;
  
  
  
  private File sweetHomeLibraryObjectsFile;
  private File ifcWordsToLookForFile;
  private File rolesFile;
  private File attributesPossibleFile;
  private File abilityFile;
  
  private String securityCategoryName = "Security";

  private static SavedConfigurationsLoader instance = null;  //SINGLETON!!
  private Map<String, List<String>>  fileContentCache = new HashMap<String, List<String>>();
  private SecurityNameAndMap snm = null;
  private Map<AssetType, HomePieceOfFurniture> typeToFurniture = null;
  private Map<String, Set<BuildingObjectAttribute>>  attributesPossible = null; 
  private Map<String, Set<ObjectAbility>> objectsAbilities = null;
  private Set<String> availableRoles = new TreeSet<String>();
  
  
  
  /**
   * Map that stores the graphical representation associated to each {@link AssetType}
   * 
   * @return
   */
  private Map<AssetType, HomePieceOfFurniture> createTypeToFurnitureMap()
  {
    Map<AssetType, HomePieceOfFurniture> catalogFurniture =
        new HashMap<AssetType, HomePieceOfFurniture>();
  
    List<FurnitureCategory> categories= getUserPreferences().getFurnitureCatalog().getCategories();
  
    SecurityNameAndMap namesConventionsSweetHome = this.getCatalogNamesFromFile();
    Map<String, AssetType> catalog = namesConventionsSweetHome.sweetCatalogToType;
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
  
          AssetType typeOBJ = catalog.get(pieceName);
          catalogFurniture.put(typeOBJ, hopf);
        }
      }
    }    
    return catalogFurniture;
  }


  public static SavedConfigurationsLoader getInstance(UserPreferences preferences)
  {

    if(instance == null)
    {
      instance = new SavedConfigurationsLoader(preferences);
      return instance;
    }
    else
    {
      return instance;
    }
  }
  
  public static SavedConfigurationsLoader getInstance() {
    if (preferences == null)
      throw new IllegalStateException("savedConfigurationsLoader should have preferences set before asking it !");
    return getInstance(preferences);
 }
  

  /***
   * es.  CCTV -> Camera surveillance N090211
   * @param type
   * @return
   */
  public String getSweetHomeNameForType(AssetType type)
  {
    if(this.namesConventionsSweetHome != null)
        return this.namesConventionsSweetHome.TypeToSweetName.get(type);
    else
    {
      this.namesConventionsSweetHome = this.getNamesConventions();
      return this.getSweetHomeNameForType(type);
    }
  }
  
  public AssetType getTypeForSweetHomeName(String sweethomeName)
  {
    AssetType type = this.getNamesConventions().sweetCatalogToType.get(sweethomeName);
    if (type == null)
       type = AssetType.UNKNOWN_OBJECT;
    return type;
    
  }
  
  public Set<ObjectAbility> getObjectAbilities(String objectOriginalName)
  {
    if(this.objectsAbilities == null)
    {
      this.objectsAbilities = new HashMap<String, Set<ObjectAbility>>();
      initAbilities();
    }
    return this.objectsAbilities.get(objectOriginalName);
  }
  
  private void initAbilities()
  {
    List<String> abilityCont = this.getfileContent(this.abilityFile.getAbsolutePath());
    for(String abilityRow : abilityCont)
    {
       String[] abfields = abilityRow.split(",");
       String originalName = abfields[0];
       boolean storeFiles = Boolean.valueOf(abfields[1]);
       boolean connect = Boolean.valueOf(abfields[2]);
       Set<ObjectAbility> abilities = new TreeSet<ObjectAbility>();
       if(storeFiles)
          abilities.add(ObjectAbility.STORE_FILES);
       if(connect)
          abilities.add(ObjectAbility.CONNECT);
       this.objectsAbilities.put(originalName, abilities);
    }
  }
  
  
  public Map<AssetType, HomePieceOfFurniture> getMapTypeToFurniture()
  {
    if(typeToFurniture == null)
        typeToFurniture = this.createTypeToFurnitureMap();
    return this.typeToFurniture;
  }
  

  protected SavedConfigurationsLoader(UserPreferences preferences)
  {
    SavedConfigurationsLoader.preferences = preferences;
    this.sweetHomeLibraryObjectsFile = this.readSweetHomeLibraryObj();
    this.ifcWordsToLookForFile = this.readWordsToLook();
    this.attributesPossibleFile = this.readAttribute(); 
    this.setMapOfLibraryObjects(preferences);
    this.rolesFile = this.readRoles();
    this.abilityFile = this.readAbilities();
  }
  


  /**
   * Create a map from object type to homePieceOfFurniture, this map is used by the objects
   * This is used by objects of the class {@link Asset} to call getPieceOfForniture
   * @param preferences
   */
  private void setMapOfLibraryObjects(UserPreferences preferences)
  {
    Map<AssetType, HomePieceOfFurniture> map =   this.getMapTypeToFurniture();
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
  
  protected String getAbilitiesFileName()
  {
    return "ability.txt";
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
    
    Map<String, AssetType> catalog = new HashMap<String, AssetType>();
    Map<AssetType, String> catalogBack = new HashMap<AssetType, String>();
    
    List<String> fileSweetToType =  getfileContent(this.sweetHomeLibraryObjectsFile.getAbsolutePath());
    String categoryName = fileSweetToType.get(0);
    for(int i = 1; i<fileSweetToType.size(); i++)
    {
      String line = fileSweetToType.get(i);
      String[] coupleObjectNameType = line.split(",");
      String objectName = coupleObjectNameType[0];
      String objectType = coupleObjectNameType[1];
      AssetType buildingObjType = AssetType.valueOf(objectType);
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
    public Map<String, AssetType> sweetCatalogToType;
    public Map<AssetType, String> TypeToSweetName;
    public String securityCategoryName;

  }


  protected List<String> getfileContent(String filePath)
  {
    List<String> cachedCont = this.fileContentCache.get(filePath);
    if(cachedCont != null)
      return cachedCont;
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
  protected  List<String> stringToLookFor(AssetType objectType)
  {

    List<String> words = new ArrayList<String>();

    List<String> content = this.getfileContent(this.ifcWordsToLookForFile.getAbsolutePath());
    for(String line : content)
    {
     String [] parts = line.split(",");
      String object = parts[0];
      AssetType type = AssetType.valueOf(object);
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
    return SavedConfigurationsLoader.preferences;
  }
  
  private File readRoles()
  {
    return getFileFromName(""+  getReadRolesFileName());
  }
  
  private File readAbilities()
  {
    return getFileFromName("" + this.getAbilitiesFileName());
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




  public void setObjectAbilityStatus(String objectOriginalName, ObjectAbility ability, boolean active) {
    Set<ObjectAbility> abilities = this.getObjectAbilities(objectOriginalName);
    if(abilities == null)
    {
      Set<ObjectAbility> abilitiesNew = new TreeSet<ObjectAbility>();
      if(active)
          abilitiesNew.add(ability);
      this.objectsAbilities.put(objectOriginalName, abilitiesNew);
    }
    else
    {
      if(abilities.contains(ability))
      {
        if(active)
            return;
        else
          abilities.remove(ability);
      }
      else
      {
        if(active)
            abilities.add(ability);
        else
          return;
      }
    }
  }


  public boolean isARole(String roleStr) {
    return this.getAvailableRoles().contains(roleStr);
  }







}
