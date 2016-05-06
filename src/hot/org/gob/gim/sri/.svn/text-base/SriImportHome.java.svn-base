package org.gob.gim.sri;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import ec.gob.gim.common.model.Address; 
import ec.gob.gim.common.model.Person;
import ec.gob.loja.client.clients.UserClient;
import ec.gob.loja.client.model.Message;
import ec.gob.loja.client.model.UserWS;

@Name("sriImportHome")
// @Scope(ScopeType.CONVERSATION)
public class SriImportHome extends EntityHome<Object> implements Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;
	private boolean autoUpload = false;

//	private FileUpload fileupload;

	ArrayList<HashMap<String, String>> personsDataUpdate = null;
	private String totalDataExcel = "";
	private String totalUpdates = "";
	private String totalAddress = "";
	
	public final int CELL_TYPE_BLANK = 3;
	public final int CELL_TYPE_BOOLEAN = 4;
	public final int CELL_TYPE_ERROR = 5;
	public final int CELL_TYPE_FORMULA=2;
	public final int CELL_TYPE_NUMERIC = 0;
	public final int CELL_TYPE_STRING = 1;

	public void listener(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();
		System.out.println("===>" + item.getFile());
		readExcel(item.getData());
	}

	private HashMap<String, String> getLocations(String fullDirections) {

		fullDirections = remove1(fullDirections);

		// System.out.println("REEMPLAZADO=>" + fullDirections);
		String BARRIO = "BARRIO:";
		String CALLE = "CALLE:";
		String NUMBER = "NUMERO:";
		String REFERENCIA = "REFERENCIA:";
		String CARRETERO = " CARRETERO:";

		Boolean hasneighborhood = fullDirections.contains(BARRIO);
		Boolean hasStreet = fullDirections.contains(CALLE);
		Boolean hasNumber = fullDirections.contains(NUMBER);
		Boolean hasReference = fullDirections.contains(REFERENCIA);
		Boolean hasCarretero = fullDirections.contains(CARRETERO);

		String neighborhood = "", street = "", number = "";

		// si contiene callle y direccion separar
		if ((hasneighborhood && hasStreet)) {
			int positionStreet = fullDirections.indexOf(CALLE);
			neighborhood = fullDirections.substring(0, positionStreet);
			neighborhood = neighborhood.replace(BARRIO, "");

			street = fullDirections.substring(positionStreet,
					fullDirections.length());
			street = street.replace(CALLE, "");

		} else if (hasneighborhood && hasCarretero) {
			int positionCarretero = fullDirections.indexOf(CARRETERO);
			neighborhood = fullDirections.substring(0, positionCarretero);
			neighborhood = neighborhood.replace(BARRIO, "");

			street = fullDirections.substring(positionCarretero,
					fullDirections.length());
			street = street.replace(CARRETERO, "");

		} else if (hasneighborhood && hasReference) {

			int positionStreet = fullDirections.indexOf(REFERENCIA);
			neighborhood = fullDirections.substring(0, positionStreet);
			neighborhood = neighborhood.replace(BARRIO, "");

			street = fullDirections.substring(positionStreet,
					fullDirections.length());
			street = street.replace(REFERENCIA, "");

		} else if (hasneighborhood && !hasNumber && !hasReference) {

			int positionNumber = fullDirections.indexOf(NUMBER);
			System.out.println("POSNumber=>" + positionNumber);
			neighborhood = fullDirections.substring(0, positionNumber);
			neighborhood = neighborhood.replace(BARRIO, "");

			number = fullDirections.substring(positionNumber,
					fullDirections.length());
			number = street.replace(NUMBER, "");
			street = number;

		} else if (hasneighborhood && !hasStreet && !hasNumber) {
			neighborhood = fullDirections.replace(BARRIO, "");
		} else if (hasStreet && !hasneighborhood) {
			street = fullDirections.replace(CALLE, "");
		}

		HashMap<String, String> locations = new HashMap<String, String>();
		locations.put("neighborhood", neighborhood);
		locations.put("street", street);

		return locations;
	}

	/**
	 * Función que elimina acentos y caracteres especiales de una cadena de
	 * texto.
	 * 
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public static String remove1(String input) {
		// Cadena de caracteres original a sustituir.
		String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		// Cadena de caracteres ASCII que reemplazarán los originales.
		String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		String output = input;
		for (int i = 0; i < original.length(); i++) {
			// Reemplazamos los caracteres especiales.
			output = output.replace(original.charAt(i), ascii.charAt(i));
		}// for i
		return output;
	}// remove1

	/**
	 * 
	 * @param filename
	 * 
	 */

	public void readExcel(byte[] excelData) {
		InputStream file2 = null;
		int registerExcel = 0;
		try {
			file2 = new ByteArrayInputStream(excelData);
			Workbook wb = WorkbookFactory.create(file2);
			Sheet mySheet = wb.getSheetAt(0);

			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();

			String identificationNumber="", razonSocial = "", canton = "", direccionLarga = "", telefonoDomicilio = "", telefonoCelular = "", email = "";
 
			this.personsDataUpdate = new ArrayList<HashMap<String, String>>();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				razonSocial = canton = direccionLarga = telefonoDomicilio = telefonoCelular = email = "";

				identificationNumber = getTextValue(row, 0);
				
				if (identificationNumber.equals("")) {
					continue;
				}
				

				identificationNumber = identificationNumber.substring(0, 10);
				razonSocial = getValue(row, 1);
				canton = getValue(row, 3);
				direccionLarga = getValue(row, 4);
				telefonoDomicilio = getTextValue(row, 5);
				telefonoCelular = getTextValue(row, 6);
				email = getValue(row, 7);

				telefonoDomicilio = getOneNumber(telefonoDomicilio);
				telefonoCelular = getOneNumber(telefonoCelular);

				HashMap<String, String> data = new HashMap<String, String>();
				data.put("identificationNumber", identificationNumber);
				data.put("razonSocial", razonSocial);
				data.put("canton", canton);
				data.put("direccionLarga", direccionLarga);
				data.put("telefonoDomicilio", telefonoDomicilio);
				data.put("telefonoCelular", telefonoCelular);
				data.put("email", email);
				registerExcel++;
				this.personsDataUpdate.add(data);

			}
			file2.close();
			this.totalDataExcel = "Total datos archivo: " + registerExcel;
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFound" + e);
		} catch (IOException e) {

			System.out.println("IO" + e);
		} catch (InvalidFormatException e) {

			System.out.println("invalidFormat" + e);
		} finally {
			updateResidents();
		}
	}

	
	private String getTextValue(Row row, int pos){
		
		//determinar el tipo
		if(row.getCell(pos)==null){
			return "";
		}
		
		int type = row.getCell(pos).getCellType();
		String text ="";
		double number=0;
		
		switch (type) {
			case CELL_TYPE_BLANK:
				break;
			
			case CELL_TYPE_BOOLEAN:
				
				break;
	
			case CELL_TYPE_ERROR:
				
				break;
				
			case CELL_TYPE_FORMULA:
				
				break;
				
			case CELL_TYPE_NUMERIC:
				number = row.getCell(pos)
					.getNumericCellValue();
				text = ""+number;
				break;	
				
			case CELL_TYPE_STRING:
				text = row.getCell(pos).getStringCellValue();
				break;
		}
		
		return text;
	}
	
	private String getValue(Row row, int index) {
		return (row.getCell(index) != null) ? row.getCell(index)
				.getStringCellValue() : "";
	}
	
	
	private double getValueNumeric(Row row, int index) {
		return (row.getCell(index) != null) ? row.getCell(index)
				.getNumericCellValue() : 0.0	;
	}

	private String getOneNumber(String number) {
		int pos = number.length();
		if (number.contains(" ")) {
			pos = number.indexOf(" ");
		}

		return number.substring(0, pos);
	}

	public void updateResidents() {

		Query q = null;
		String findPerson = "Person.findByIdentificationNumber";
		int updateRegisters = 0;
		int newDirections = 0;
		int flushCount = 0;
		System.out.println("Ingresa al metodo de actualizar");
		System.out.println("tamano de lista " + personsDataUpdate.size());
		for (HashMap<String, String> dataPerson : this.personsDataUpdate) {

			try {

				q = getEntityManager().createNamedQuery(findPerson);
				q.setParameter("identificationNumber",
						dataPerson.get("identificationNumber"));
				Person person = (Person) q.getSingleResult();

				if (person.getCurrentAddress() == null) {
					// continue;
					Address currentAddress = new Address();
					person.setCurrentAddress(currentAddress);
					person.getCurrentAddress().setCountry("ECUADOR");
					person.getCurrentAddress().setStreet("");
					person.getCurrentAddress().setNeighborhood("");
					newDirections++;
				}

				// analizar la direccion y obtener
				// datos como calles y barrios
				HashMap<String, String> locations = getLocations(dataPerson
						.get("direccionLarga"));
				if (!locations.get("neighborhood").trim().equals("")) {
					person.getCurrentAddress().setNeighborhood2(
							locations.get("neighborhood"));
				}

				if (!locations.get("street").trim().equals("")) {
					person.getCurrentAddress().setStreet2(
							locations.get("street"));
				}

				if (!dataPerson.get("email").trim().equals("")) {
					if(person.getEmail()!=null && person.getEmail().isEmpty()){
						person.setEmail(dataPerson.get("email"));
					} 
				}

				if (!dataPerson.get("telefonoDomicilio").trim().equals("")) {
					person.getCurrentAddress().setPhoneNumber(
							dataPerson.get("telefonoDomicilio"));
				}

				if (!dataPerson.get("telefonoCelular").trim().equals("")) {
					person.getCurrentAddress().setMobileNumber(
							dataPerson.get("telefonoCelular"));
				}

				if (!dataPerson.get("canton").trim().equals("")) {
					person.getCurrentAddress()
							.setCity(dataPerson.get("canton"));
				}

				getEntityManager().merge(person);
				prepareToEBillingService(person);
				updateRegisters++;
			} catch (NoResultException e) {
			}
		}
		this.totalAddress = "Nro., contribuyentes actualizados: "
				+ updateRegisters;
		this.totalUpdates = "Nro, direcciones nuevas creadas: " + newDirections;

		System.out.println(this.totalAddress);
		System.out.println(this.totalUpdates);

	}

	/**
	 * 
	 * @param person
	 */
	private void prepareToEBillingService(Person person) {
		 
		UserWS userws = new UserWS();
		userws.setIdentification(person.getIdentificationNumber());
		userws.setName(person.getFirstName());
		userws.setSurname(person.getLastName());
		userws.setActive(Boolean.TRUE);
		userws.setEmail(person.getEmail());
		userws.setPassword(person.getIdentificationNumber());
		try {
			userws.setPhone(person.getCurrentAddress().getPhoneNumber());
		} catch (Exception ex) {
			System.out.println(" setPhone sri >>> error >>>>> <<<<<<");
			ex.printStackTrace();
			userws.setPhone("");
		}
		try {
			sendToService(userws);
		} catch (Exception e) {
			System.out.println("=>" + e);
			e.printStackTrace();
		}
	}

	private UserWS sendToService(UserWS input) throws Exception {

		String BASE_URI = ResourceBundle.instance().getString("BASE_URI_SRI");
		UserClient client = new UserClient(BASE_URI);
		UserWS response;
		response = client.saveUser_XML(input, UserWS.class);
		
		if (response.getMessageList() != null) {
			List<Message> mensajes = response.getMessageList();
			for (Message mensaje : mensajes) {
				System.out.println(mensaje.getType() + "\t"
						+ mensaje.getIdentifier() + "\t" + mensaje.getMessage()
						+ "\t" + mensaje.getAdditionalInformation());
			}
		}
		return response;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		this.totalAddress = "";
		this.totalUpdates = "";
		this.totalDataExcel = "";
	}

	public boolean isWired() {
		return true;
	}

	public Object getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
 

	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public ArrayList<HashMap<String, String>> getPersonsDataUpdate() {
		return personsDataUpdate;
	}

	public void setPersonsDataUpdate(
			ArrayList<HashMap<String, String>> personsDataUpdate) {
		this.personsDataUpdate = personsDataUpdate;
	}

	public String getTotalDataExcel() {
		return totalDataExcel;
	}

	public void setTotalDataExcel(String totalDataExcel) {
		this.totalDataExcel = totalDataExcel;
	}

	public String getTotalUpdates() {
		return totalUpdates;
	}

	public void setTotalUpdates(String totalUpdates) {
		this.totalUpdates = totalUpdates;
	}

	public String getTotalAddress() {
		return totalAddress;
	}

	public void setTotalAddress(String totalAddress) {
		this.totalAddress = totalAddress;
	}
}
