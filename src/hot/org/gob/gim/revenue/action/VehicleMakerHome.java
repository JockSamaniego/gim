package org.gob.gim.revenue.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.CoipDTO;
import ec.gob.gim.revenue.model.DTO.VehicleGeneralDataDTO;
import ec.gob.gim.revenue.model.DTO.VehicleUniqueDataDTO;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleMaker;

@Name("vehicleMakerHome")
public class VehicleMakerHome extends EntityHome<VehicleMaker> {

	private static final long serialVersionUID = 1L;

	@In
	FacesMessages facesMessages;
	
	public void setVehicleMakerId(Long id) {
		setId(id);
	}

	public Long getVehicleMakerId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public VehicleMaker getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	
	@Override
	public String persist() {
		// comprobar q no exista el nombre
		Query q = this.getEntityManager().createNamedQuery("VehicleMaker.findByName");
		q.setParameter("name", this.getInstance().getName());
		if (q.getResultList().size() == 0) {
			return super.persist();
		} else {
			facesMessages.addToControl("resident",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"#{messages['vehicleMaker.nameExist']}");
			return null;
		}
	}
	
	//para consultar informacion de vehiculos
	
	private String vehicleCriteriaSearch;
	private List<VehicleUniqueDataDTO> vUniqueDataList;
	private List<VehicleGeneralDataDTO> vGeneralDataList;

	public String getVehicleCriteriaSearch() {
		return vehicleCriteriaSearch;
	}

	public void setVehicleCriteriaSearch(String vehicleCriteriaSearch) {
		this.vehicleCriteriaSearch = vehicleCriteriaSearch;
	}
	
	public List<VehicleUniqueDataDTO> getvUniqueDataList() {
		return vUniqueDataList;
	}

	public void setvUniqueDataList(List<VehicleUniqueDataDTO> vUniqueDataList) {
		this.vUniqueDataList = vUniqueDataList;
	}

	public List<VehicleGeneralDataDTO> getvGeneralDataList() {
		return vGeneralDataList;
	}

	public void setvGeneralDataList(List<VehicleGeneralDataDTO> vGeneralDataList) {
		this.vGeneralDataList = vGeneralDataList;
	}

	public void searchVehicleInformation(){
		vUniqueDataList = new ArrayList<VehicleUniqueDataDTO>();
		vGeneralDataList = new ArrayList<VehicleGeneralDataDTO>();
		String query = "SELECT * "
						+"FROM gimprod.vehicle_unique_data vud "
						+"WHERE UPPER(vud.numero_placa) = UPPER('"+this.vehicleCriteriaSearch+"')";
		Query q = this.getEntityManager().createNativeQuery(query);
		vUniqueDataList = NativeQueryResultsMapper.map(q.getResultList(), VehicleUniqueDataDTO.class);
		if (vUniqueDataList.size()>0){
			String query2 = "SELECT * "
					+"FROM gimprod.vehicle_general_data vgd "
					+"WHERE vgd.codigo_sub_categoria = '"+vUniqueDataList.get(0).getCODIGO_SUB_CATEGORIA()+"' "
					+"ORDER BY vgd.anio_desde DESC";
			Query q2 = this.getEntityManager().createNativeQuery(query2);
			vGeneralDataList = NativeQueryResultsMapper.map(q2.getResultList(), VehicleGeneralDataDTO.class);
		}
		
	}
}
