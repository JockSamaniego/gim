package org.gob.gim.cadaster.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import ec.gob.gim.cadaster.model.Block;
import ec.gob.gim.cadaster.model.BlockLimit;
import ec.gob.gim.cadaster.model.Neighborhood;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.common.model.Attachment;

@Name("blockHome")
public class BlockHome extends EntityHome<Block> {

	private static final long serialVersionUID = 1L;

	@Logger
	Log logger;
	
	@In(create = true)
	TerritorialDivisionHome territorialDivisionHome;
	
	@In
	FacesMessages facesMessages;
	
	TerritorialDivision parish;
	
	TerritorialDivision zone;
	
	TerritorialDivision sector;	
	
	private String streetName;	
	
	private Street currentStreet;
	
	private BlockLimit blockLimit;
	
	private boolean isFirstTime = true;

	public void setBlockId(Long id) {
		setId(id);
	}

	public Long getBlockId() {
		return (Long) getId();
	}
	
	/**
	 * Persiste o actualiza el Block
	 * 
	 */
	public String save(){
		this.getInstance().setSector(sector);
		if(isManaged()){
			return super.update();
		}else{
			return super.persist();
		}
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		TerritorialDivision sector = territorialDivisionHome.getDefinedInstance();
		if (sector != null) {
			getInstance().setSector(sector);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Block getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<BlockLimit> getLimits() {
		return getInstance() == null ? null : new ArrayList<BlockLimit>(
				getInstance().getLimits());
	}
	public List<Property> getProperties() {
		return getInstance() == null ? null : new ArrayList<Property>(
				getInstance().getProperties());
	}
	
	/**
	 * Se encarga de subir la imagen con el croquis de la manzana
	 * @param event evento de carga de archivo 
	 */
	public void sketchListener(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		logger.info("Sketch listener executed...  Data: "+item.getData());
	    if(item != null && item.getData() != null) {
	    	logger.info(item.getFileName()+" "+item.getFileSize());
	    	if(getInstance().getSketch() == null){
	    		getInstance().setSketch(new Attachment());
	    	}
            getInstance().getSketch().setData(item.getData());
            getInstance().getSketch().setName(item.getFileName());
            getInstance().getSketch().setContentType(item.getContentType());
            getInstance().getSketch().setSize(item.getFileSize());
	    }
	}
	
	/**
	 * Limpia la imagen del croquis de la manzana
	 *  
	 */
	public void clearSketch(){
		getInstance().getSketch().setData(null);
        getInstance().getSketch().setName(null);
        getInstance().getSketch().setContentType(null);
        getInstance().getSketch().setSize(0);
	}
	
	/**
	 * Asigna valores a variables antes de que aparezca el modalPanel para editar el BlockLimit
	 * @param blockLimit objeto que se va a editar 
	 */
	public void beforeEditLimit(BlockLimit blockLimit){
		currentStreet = blockLimit.getStreet();
		if (currentStreet.getPlace() == null){
			setStreetName(currentStreet.getName());
		}else{
			setStreetName(currentStreet.getName() + " - " + currentStreet.getPlace().getName());
		}
		this.blockLimit = blockLimit;
	}
	
	
	/**
	 * Crea un nuevo BlockLimit
	 *  
	 */
	public void createBlockLimit(){		
		this.blockLimit = new BlockLimit();		
		streetName=null;
	}
	
	/**
	 * Agrega this.blockLimit a los límites del Block
	 * 
	 */
	public void addBlockLimit(){
		if(currentStreet == null){
			facesMessages.addToControl("street", org.jboss.seam.international.StatusMessage.Severity.ERROR, "#{messages['cadaster.notAllBlockValuesChosen']}");
			setStreetName(null);
			return;
		}
		setStreetName(null);		
		this.blockLimit.setStreet(currentStreet);
		setCurrentStreet(null);
		this.getInstance().add(this.blockLimit);
		logger.info(this.getInstance().getLimits().size());
	}	
	
	/**
	 * Antes de salir del modalPanel de edición del BLockLimit, se limpia valores
	 * 
	 */
	public void beforeOut(){
		setCurrentStreet(null);		
		setStreetName(null);
	}
	
	/**
	 * Elimina un blockLimit de la lista de blockLimits de la instancia
	 * 
	 */
	public void removeBlockLimit(BlockLimit limit){
		this.getInstance().remove(limit);
	}

	public BlockLimit getBlockLimit() {
		return blockLimit;
	}
	
	/**
	 * Fija en el suggestionBox el nombre de la calle
	 * @param street calle que ha sido seleccionada
	 */
	public void beforeAutcomplete(Street street) {
		//value="#{_street.name} #{_street.place == null ? '' :  ' - '} #{_street.place == null ? '' :  _street.place}"
		setCurrentStreet(street);
		if(street == null){
			streetName = null;
		}else if (street.getPlace() == null){
			setStreetName(street.getName());
		}else{
			setStreetName(street.getName() + " - " + street.getPlace().getName());
		}
	}

	/**
	 * Cambia this.blockLimit para ser editado en el modalPanel
	 * @param blockLimit objeto que será editado
	 */
	public void setBlockLimit(BlockLimit blockLimit) {
		this.blockLimit = blockLimit;
	}
	
	/**
	 * Busca las calles según el criterio ingresado el suggestionBox
	 * @param suggestion criterio ingresado el suggestionBox
	 * @return List<Street>
	 */
	@SuppressWarnings("unchecked")
	public List<Street> findStreets(Object suggestion){
		String name = suggestion.toString();
		Query query = getPersistenceContext().createNamedQuery("Street.findByName");
		query.setParameter("name", name);
		return query.getResultList();
	}
	
	/**
	 * Busca las parroquias de un determinado cantón
	 * @param defaultCantonId id del cantón
	 * @return List<TerritorialDivision>
	 */
	public List<TerritorialDivision> findParishes(Long defaultCantonId){
		return findTerritorialDivisions(defaultCantonId);
	}
	
	/**
	 * Busca las zonas de una parroquia (parish)
	 * @return List<TerritorialDivision>
	 */
	public List<TerritorialDivision> findZones(){
		if(parish!=null){
			return findTerritorialDivisions(parish.getId());
		}
		return new ArrayList<TerritorialDivision>();
	}
	
	/**
	 * Busca los sectores de un determinada zona (zone)
	 * @return List<TerritorialDivision>
	 */
	public List<TerritorialDivision> findSectors(){
		if(zone!=null){
			return findTerritorialDivisions(zone.getId());
		}
		return new ArrayList<TerritorialDivision>();
	}
	
	/**
	 * Busca todos los barrios
	 * @return List<Neighborhood>
	 */
	public List<Neighborhood> findNeighborhoods(){		
		Query query = getPersistenceContext().createNamedQuery("Neighborhood.findAll");		
		return query.getResultList();	
	}
		
	/**
	 * Pone a null la zona, sector y el codigo catastral cuando hay un cambio en la parroquia
	 * 
	 */
	public void resetAll(){		
		isFirstTime = false;
		setZone(null);
		resetSectorAndCode();
	}
	
	/**
	 * Pone a null el sector y el codigo catastral cuando hay un cambio en la zona
	 * 
	 */
	public void resetSectorAndCode(){
		setSector(null);
		getInstance().setCadastralCode(null);
	}

	@SuppressWarnings("unchecked")
	private List<TerritorialDivision> findTerritorialDivisions(Long parentId){
		Query query = getPersistenceContext().createNamedQuery("TerritorialDivision.findByParent");
		query.setParameter("parentId", parentId);
		return query.getResultList();
	}
	
	/**
	 * Genera la clave catastral en base a: provincia, cantón, parroquia, zona y sector
	 * 
	 */
	public void generateBlockCode(){
		TerritorialDivision province = (TerritorialDivision) getComponentInstance("defaultProvince");
		TerritorialDivision canton = (TerritorialDivision) getComponentInstance("defaultCanton");
		logger.info("PROVINCIA OBTENIDA F ---->"+province);
		logger.info("CANTON OBTENIDO F---->"+canton);
		logger.info("PARROQUIA F---->"+parish);
		logger.info("ZONA F---->"+zone);
		logger.info("SECTOR F---->" + sector);
		
		if(province!=null && canton != null && parish!=null && zone!=null && sector!=null && this.getInstance().getCode()!=null){			
			if(this.getInstance().getCode().matches("[0-9]{1,3}")){				
				this.getInstance().setCode(getFormattedBlockCodeNumber());
				if(!this.getInstance().getCode().matches("[0-9]{3,3}")){
					showErrorMessages();
					return;
				}			
			}else{
				showErrorMessages();
				return;
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append(province.getCode());
			sb.append(canton.getCode());
			sb.append(parish.getCode());
			sb.append(zone.getCode());
			sb.append(sector.getCode());
			sb.append(this.getInstance().getCode());
			logger.info("CODIGO GENERADO ---->"+sb.toString());
			getInstance().setCadastralCode(sb.toString());
		}
		else{
			getInstance().setCadastralCode(null);
			logger.info("CODIGO GENERADO ----> Not all values were set");
			addFacesMessageFromResourceBundle("cadaster.notAllBlockValuesChosen");
		}
	}
	
	private void showErrorMessages(){
		logger.info("ERROR GENERADO ----> Codigo no valido");
		getInstance().setCadastralCode(null);
		getInstance().setCode(null);
		String message = Interpolator.instance().interpolate(
				"#{messages['block.invalidCode']}", new Object[0]);
		facesMessages.addToControl("rcode",org.jboss.seam.international.StatusMessage.Severity.ERROR, message);
		return;
	}
	
	/**
	 * Busca un territorialDivision en base a un codigo que se lo toma como substring de la clave catastral
	 * @param x posicion inicial para obtener el substring 
	 * @param y posicion final para obtener el substring
	 * @param territorialDivision es el padre del territorialDivision que se va a obtener
	 * @return TerritorialDivision
	 */
	private TerritorialDivision findTerritorialDivision(int x, int y, TerritorialDivision territorialDivision){
		if(this.getInstance().getCadastralCode().length() < y) return null;		
		
		String code = this.getInstance().getCadastralCode().substring(x,y);				
		Query query = getPersistenceContext().createNamedQuery("TerritorialDivision.findByCodeAndParent");
		query.setParameter("code", code);		
		query.setParameter("parent",territorialDivision);
		List<?>  result = query.getResultList();
		if(result != null && result.size() > 0) return (TerritorialDivision)result.get(0);
			
		return null;
	}
	
	private java.text.NumberFormat getNumberFormat() {
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("000");
		numberFormat.setMaximumIntegerDigits(3);
		return numberFormat;
	}

	public String getFormattedBlockCodeNumber() {		
		return getNumberFormat().format(Long.parseLong(this.getInstance().getCode()));
	}
	
	/**
	 * Carga los valores de los territorialDivision en base al código catastral
	 * 
	 */
	public void loadValues(){			
		if(!isFirstTime)return;		
		
		if(this.getInstance().getCadastralCode() == null) return;
		
		if(parish == null) parish = findTerritorialDivision(5,9,territorialDivisionHome.findDefaultCanton());
				
		if(zone == null) zone = findTerritorialDivision(9,11,parish);
				
		if(sector == null){ sector = findTerritorialDivision(11,13,zone);			
		}
	}
	
	public TerritorialDivision getParish() {		
		return parish;
	}

	public void setParish(TerritorialDivision parish) {
		this.parish = parish;
	}

	public TerritorialDivision getZone() {		
		return zone;
	}
	
	public void setZone(TerritorialDivision zone) {
		this.zone = zone;
	}

	public void setSector(TerritorialDivision sector) {
		this.sector = sector;
	}

	public TerritorialDivision getSector() {		
		return sector;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setCurrentStreet(Street currentStreet) {
		this.currentStreet = currentStreet;
	}

	public Street getCurrentStreet() {
		return currentStreet;
	}
	
}
