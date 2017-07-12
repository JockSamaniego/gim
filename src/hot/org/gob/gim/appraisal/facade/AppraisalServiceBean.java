package org.gob.gim.appraisal.facade;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

//import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;

import ec.gob.gim.appraisal.model.AppraisalPeriod;
import ec.gob.gim.appraisal.model.AppraisalRossHeidecke;
import ec.gob.gim.appraisal.model.AppraisalTotalExternal;
import ec.gob.gim.appraisal.model.AppraisalTotalRoof;
import ec.gob.gim.appraisal.model.AppraisalTotalStructure;
import ec.gob.gim.appraisal.model.AppraisalTotalWall;
import ec.gob.gim.cadaster.model.Appraisal;
import ec.gob.gim.cadaster.model.Building;
import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.PreservationState;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.Sewerage;

/**
 * @author IML
 *
 */
@Stateless(name = "AppraisalService")
public class AppraisalServiceBean implements AppraisalService {

	@EJB
	SystemParameterService systemParameterService;

	@PersistenceContext
	EntityManager entityManager;

	// @EJB
	// CrudService crudService;

	private Map<String, AppraisalTotalStructure> mapTotalStructure = new HashMap<String, AppraisalTotalStructure>();
	private Map<String, AppraisalTotalWall> mapTotalWall = new HashMap<String, AppraisalTotalWall>();
	private Map<String, AppraisalTotalRoof> mapTotalRoof = new HashMap<String, AppraisalTotalRoof>();
	private Map<String, AppraisalTotalExternal> mapTotalExternal = new HashMap<String, AppraisalTotalExternal>();
	private Map<Long, AppraisalRossHeidecke> mapRossHeidecke = new HashMap<Long, AppraisalRossHeidecke>();

	public void getMaps(AppraisalPeriod appraisalPeriod) {
		mapTotalStructure.clear();
		for (AppraisalTotalStructure aTotalStructure : appraisalPeriod
				.getAppraisalTotalStructure()) {
			mapTotalStructure.put(
					aTotalStructure.getStructureMaterial().name(),
					aTotalStructure);
		}

		mapTotalWall.clear();
		for (AppraisalTotalWall aTotalWall : appraisalPeriod
				.getAppraisalTotalWall()) {
			mapTotalWall.put(aTotalWall.getWallMaterial().name(), aTotalWall);
		}

		mapTotalRoof.clear();
		for (AppraisalTotalRoof aTotalRoof : appraisalPeriod
				.getAppraisalTotalRoof()) {
			mapTotalRoof.put(aTotalRoof.getRoofMaterial().name(), aTotalRoof);
		}

		mapTotalExternal.clear();
		for (AppraisalTotalExternal aTotalExternal : appraisalPeriod
				.getAppraisalTotalExternal()) {
			mapTotalExternal.put(aTotalExternal.getExternalFinishing().name(),
					aTotalExternal);
		}

		mapRossHeidecke.clear();
		for (AppraisalRossHeidecke aRossHeidecke : findAppraisalRossHeideckeYears()) {
			mapRossHeidecke.put((long) aRossHeidecke.getYear(), aRossHeidecke);
		}

	}

	@SuppressWarnings("unchecked")
	public List<AppraisalRossHeidecke> findAppraisalRossHeideckeYears() {
		Query query = entityManager
				.createNamedQuery("AppraisalRossHeidecke.findAll");
		List<AppraisalRossHeidecke> appraisalRossHeidecke = query
				.getResultList();
		return appraisalRossHeidecke;
	}

	public void saveValueBySquareMeter(List<Property> properties,
			boolean temporalValues) throws Exception {
		for (Property property : properties) {
			setToValueBySquareMeter(property.getCurrentDomain(), temporalValues);
		}
	}

	private void setToValueBySquareMeter(Domain domain, boolean temporalValues)
			throws Exception {
		if (domain != null && domain.getId() != null) {
			if (!temporalValues) {
				Query query = entityManager
						.createNamedQuery("Domain.setLotValueM2");
				query.setParameter("valueBySquareMeter",
						domain.getValueBySquareMeter());
				query.setParameter("domainId", domain.getId());
				query.executeUpdate();
			} else {
				Query query = entityManager
						.createNamedQuery("Domain.setLotValueM2Tmp");
				query.setParameter("valueBySquareMeterTmp",
						domain.getValueBySquareMeterTmp());
				query.setParameter("domainId", domain.getId());
				query.executeUpdate();
			}
		}
	}

	public BigDecimal getAppraisalRelationFactor(Property proper) {
		BigDecimal equivalencia = new BigDecimal(0);

		if (proper.getFront().compareTo(proper.getSide()) == -1) // menor que
			equivalencia = proper.getFront().divide(proper.getSide(), 4,
					RoundingMode.HALF_UP);
		else if (proper.getFront().compareTo(proper.getSide()) == 0) // igual
			return new BigDecimal(1);
		else if (proper.getFront().compareTo(proper.getSide()) == 1) { // mayor
																		// que
			equivalencia = proper.getSide().divide(proper.getFront(), 3,
					RoundingMode.HALF_UP);
		}
		System.out.println("Property Id:" + proper.getId());
		System.out.println("Valor equivalencia1:" + equivalencia.doubleValue());
		equivalencia = equivalencia.add(new BigDecimal(0.001)); // para comparar
																// solamente los
																// menores y no
																// los iguales
		System.out.println("Valor equivalencia1:" + equivalencia.doubleValue());

		if (equivalencia.compareTo(new BigDecimal(0.2500)) == 1)
			return new BigDecimal(1);
		else if (equivalencia.compareTo(new BigDecimal(0.2000)) == 1)
			return new BigDecimal(0.9925);
		else if (equivalencia.compareTo(new BigDecimal(0.1667)) == 1)
			return new BigDecimal(0.9850);
		else if (equivalencia.compareTo(new BigDecimal(0.1429)) == 1)
			return new BigDecimal(0.9775);
		else if (equivalencia.compareTo(new BigDecimal(0.1250)) == 1)
			return new BigDecimal(0.9700);
		else if (equivalencia.compareTo(new BigDecimal(0.1111)) == 1)
			return new BigDecimal(0.9625);
		else if (equivalencia.compareTo(new BigDecimal(0.1000)) == 1)
			return new BigDecimal(0.9550);
		else if (equivalencia.compareTo(new BigDecimal(0.0909)) == 1)
			return new BigDecimal(0.9475);
		else
			return new BigDecimal(0.9400);
//		 if (equivalencia.compareTo(new BigDecimal(0.251)) == 1)
//		 return new BigDecimal(1);
//		 else if (equivalencia.compareTo(new BigDecimal(0.201)) == 1)
//		 return new BigDecimal(0.9925);
//		 else if (equivalencia.compareTo(new BigDecimal(0.171)) == 1)
//		 return new BigDecimal(0.9850);
//		 else if (equivalencia.compareTo(new BigDecimal(0.141)) == 1)
//		 return new BigDecimal(0.9775);
//		 else if (equivalencia.compareTo(new BigDecimal(0.131)) == 1)
//		 return new BigDecimal(0.9700);
//		 else if (equivalencia.compareTo(new BigDecimal(0.111)) == 1)
//		 return new BigDecimal(0.9625);
//		 else if (equivalencia.compareTo(new BigDecimal(0.101)) == 1)
//		 return new BigDecimal(0.9550);
//		 else if (equivalencia.compareTo(new BigDecimal(0.091)) == 1)
//		 return new BigDecimal(0.9475);
//		 else return new BigDecimal(0.9400);
	}

	public BigDecimal getAppraisalAreaFactor(Property proper) {
		if (proper.getArea().compareTo(new BigDecimal(51)) == -1)
			return new BigDecimal(1);
		else if (proper.getArea().compareTo(new BigDecimal(251)) == -1)
			return new BigDecimal(0.99);
		else if (proper.getArea().compareTo(new BigDecimal(501)) == -1)
			return new BigDecimal(0.98);
		else if (proper.getArea().compareTo(new BigDecimal(1001)) == -1)
			return new BigDecimal(0.97);
		else if (proper.getArea().compareTo(new BigDecimal(2501)) == -1)
			return new BigDecimal(0.96);
		else if (proper.getArea().compareTo(new BigDecimal(5001)) == -1)
			return new BigDecimal(0.95);
		else
			return new BigDecimal(0.94);
		/*if (proper.getArea().compareTo(new BigDecimal(51)) == -1)
			return new BigDecimal(1);
		else if (proper.getArea().compareTo(new BigDecimal(251)) == -1)
			return new BigDecimal(1);
		else if (proper.getArea().compareTo(new BigDecimal(501)) == -1)
			return new BigDecimal(0.96);
		else if (proper.getArea().compareTo(new BigDecimal(1001)) == -1)
			return new BigDecimal(0.90);
		else if (proper.getArea().compareTo(new BigDecimal(2501)) == -1)
			return new BigDecimal(0.80);
		else if (proper.getArea().compareTo(new BigDecimal(5001)) == -1)
			return new BigDecimal(0.795);
		else
			return new BigDecimal(0.60);*/
	}

	public List<Property> calculateUrbanAppraisal(
			AppraisalPeriod appraisalPeriod, int anioAppraisal,
			List<Property> properties, boolean temporalValues) {
		BigDecimal affectationFactorLot = new BigDecimal(0);
		BigDecimal lotAppraisal = new BigDecimal(0);

		BigDecimal affectationFactorBuilding = new BigDecimal(0);
		BigDecimal buildingAppraisal = new BigDecimal(0);
		BigDecimal totalBuildingAppraisal = new BigDecimal(0);
		BigDecimal cienBigD = new BigDecimal(100);
		// BigDecimal ceroBigD = BigDecimal.ZERO;

		getMaps(appraisalPeriod);
		for (Property property : properties) {
			// Inicia Calculo de Avaluo de Terreno
			System.out.println("======= CadastralCode: "
					+ property.getCadastralCode());
			lotAppraisal = BigDecimal.ZERO;
			affectationFactorLot = BigDecimal.ZERO;
			property.setAppraisalRelationFactor(getAppraisalRelationFactor(property));
			// System.out.println("======= RelationFactor: "+property.getAppraisalRelationFactor());

			property.setAppraisalAreaFactor(getAppraisalAreaFactor(property));
			// System.out.println("======= AreaFactor: "+property.getAppraisalAreaFactor());

			affectationFactorLot = property.getAppraisalRelationFactor()
					.multiply(property.getAppraisalAreaFactor());
			// System.out.println("======= affectationFactorLot: "+affectationFactorLot);
			// System.out.println("======= LotPosition: "+property.getLotPosition().getFactor());
			affectationFactorLot = affectationFactorLot.multiply(property
					.getLotPosition().getFactor());
			// System.out.println("======= affectationFactorLot: "+affectationFactorLot);
			// System.out.println("======= Lottopography: "+property.getLotTopography().getFactor());
			affectationFactorLot = affectationFactorLot.multiply(property
					.getLotTopography().getFactor());
			// System.out.println("======= affectationFactorLot: "+affectationFactorLot);
			// System.out.println("======= StreetMaterial: "+property.getStreetMaterial().getFactor());
			affectationFactorLot = affectationFactorLot.multiply(property
					.getStreetMaterial().getFactor());
			// System.out.println("======= affectationFactorLot: "+affectationFactorLot);

			// INFRAESTRUCTURA
			if (property.getHasWaterService())
				affectationFactorLot = affectationFactorLot
						.multiply(appraisalPeriod.getFactorHasWater());
			else
				affectationFactorLot = affectationFactorLot
						.multiply(appraisalPeriod.getFactorHasntWater());

			if (property.getSewerage().compareTo(Sewerage.NOT_AVAILABLE) == 0)
				affectationFactorLot = affectationFactorLot
						.multiply(appraisalPeriod.getFactorHasntSewerage());
			else
				affectationFactorLot = affectationFactorLot
						.multiply(appraisalPeriod.getFactorHasSewerage());

			if (property.getHasElectricity())
				affectationFactorLot = affectationFactorLot
						.multiply(appraisalPeriod.getFactorHasEnergy());
			else
				affectationFactorLot = affectationFactorLot
						.multiply(appraisalPeriod.getFactorHasntEnergy());

			// System.out.println("======= affectationFactorLot: "+affectationFactorLot);
			affectationFactorLot = affectationFactorLot
					.round(new MathContext(4));
			property.setAffectationFactorLot(affectationFactorLot);

			if (!temporalValues) {
				lotAppraisal = property.getCurrentDomain()
						.getValueBySquareMeter()
						.multiply(property.getAffectationFactorLot());
				lotAppraisal = lotAppraisal.multiply(property.getArea());
				lotAppraisal = lotAppraisal.setScale(2, RoundingMode.HALF_UP);
			} else {
				lotAppraisal = property.getCurrentDomain()
						.getValueBySquareMeterTmp()
						.multiply(property.getAffectationFactorLot());
				lotAppraisal = lotAppraisal.multiply(property.getArea());
				lotAppraisal = lotAppraisal.setScale(2, RoundingMode.HALF_UP);
			}

			System.out.println("======= totalLotAppraisal: " + lotAppraisal);
			if ((property.getLotAliquot().floatValue() > 0)
					&& (property.getLotAliquot().floatValue() < 100)) {
				lotAppraisal = lotAppraisal.multiply(property.getLotAliquot())
						.divide(cienBigD);
				lotAppraisal = lotAppraisal.setScale(2, RoundingMode.HALF_UP);
				System.out.println("======= Lot Aliquot: "
						+ property.getLotAliquot());
				System.out.println("======= totalLotAppraisal Aliquot: "
						+ lotAppraisal);
			}

			if (temporalValues) {
				property.getCurrentDomain().setLotAppraisalTmp(lotAppraisal);
			} else {
				property.getCurrentDomain().setLotAppraisal(lotAppraisal);
			}

			System.out.println("======= lotAppraisal : " + lotAppraisal);

			// Inicia Calculo de Avaluo de Construccion
			totalBuildingAppraisal = BigDecimal.ZERO;
			for (Building building : property.getBuildings()) {
				// System.out.println("======= Construction: "+building.getId());
				buildingAppraisal = BigDecimal.ZERO;
				affectationFactorBuilding = BigDecimal.ZERO;
				affectationFactorBuilding = affectationFactorBuilding
						.add(mapTotalStructure.get(
								building.getStructureMaterial().name())
								.getTotal());
				affectationFactorBuilding = affectationFactorBuilding
						.add(mapTotalWall
								.get(building.getWallMaterial().name())
								.getTotal());
				affectationFactorBuilding = affectationFactorBuilding
						.add(mapTotalRoof
								.get(building.getRoofMaterial().name())
								.getTotal());
				affectationFactorBuilding = affectationFactorBuilding
						.add(mapTotalExternal.get(
								building.getExternalFinishing().name())
								.getTotal());
				// System.out.println("======= Structure: "+mapTotalStructure.get(building.getStructureMaterial().name()));
				// System.out.println("======= Structure: "+mapTotalStructure.get(building.getStructureMaterial().name()).getTotal());
				// System.out.println("======= Wall: "+mapTotalWall.get(building.getWallMaterial().name()));
				// System.out.println("======= Wall: "+mapTotalWall.get(building.getWallMaterial().name()).getTotal());
				// System.out.println("======= Roof: "+mapTotalRoof.get(building.getRoofMaterial().name()));
				// System.out.println("======= Roof: "+mapTotalRoof.get(building.getRoofMaterial().name()).getTotal());
				// System.out.println("======= External: "+mapTotalExternal.get(building.getExternalFinishing().name()));
				// System.out.println("======= External: "+mapTotalExternal.get(building.getExternalFinishing().name()).getTotal());
				//
				// System.out.println("======= affectationFactorBuilding: "+affectationFactorBuilding);

				// Equipment
				if (building.getHasEquipment()) {
					affectationFactorBuilding = affectationFactorBuilding
							.multiply(appraisalPeriod.getFactorHasEquipment());
					// System.out.println("======= appraisalPeriod.getFactorHasEquipment(): "+appraisalPeriod.getFactorHasEquipment());
				} else {
					affectationFactorBuilding = affectationFactorBuilding
							.multiply(appraisalPeriod.getFactorHasntEquipment());
					// System.out.println("======= appraisalPeriod.getFactorHasntEquipment(): "+appraisalPeriod.getFactorHasntEquipment());
				}

				// System.out.println("======= affectationFactorBuilding: "+affectationFactorBuilding);

				// preservationState
				Long anioConst = anioAppraisal
						- building.getBuildingYear().longValue();// building.getAnioConst().longValue();
				if (anioConst > 99)
					anioConst = Long.valueOf(99);
				BigDecimal factorRoss = new BigDecimal(1);
				// System.out.println("======= Años: "+anioConst);
				// System.out.println("======= PreservationState: "+building.getPreservationState().name());
				if (building.getPreservationState().compareTo(
						PreservationState.GOOD) == 0) {
					factorRoss = mapRossHeidecke.get(anioConst).getGoodState();
				} else if (building.getPreservationState().compareTo(
						PreservationState.BAD) == 0) {
		factorRoss = mapRossHeidecke.get(anioConst).getBadState();
				} else if (building.getPreservationState().compareTo(
						PreservationState.REGULAR) == 0) {
					factorRoss = mapRossHeidecke.get(anioConst)
							.getRegularState();
				}

				// System.out.println("======= factorRoss : "+factorRoss );

				factorRoss = factorRoss.divide(new BigDecimal(100));

				// System.out.println("======= factorRoss : "+factorRoss );

				BigDecimal substract = new BigDecimal(0);
				// equivale al 80% --> (VA-Vr) --> Vr = 20% de VA
				substract = affectationFactorBuilding.multiply(new BigDecimal(
						0.8));
				substract = substract.multiply(factorRoss);

				// System.out.println("======= substract: "+substract);

				// valor por m2 de edificacion para esa porcion de la
				// construccion
				affectationFactorBuilding = affectationFactorBuilding
						.subtract(substract);

				buildingAppraisal = building.getArea().multiply(
						affectationFactorBuilding);
				buildingAppraisal = buildingAppraisal.multiply(BigDecimal
						.valueOf(building.getFloorsNumber()));

				// System.out.println("======= Area: "+building.getArea());
				// System.out.println("======= Pisos: "+building.getFloorsNumber());
				// System.out.println("======= affectationFactorBuilding: "+affectationFactorBuilding);

				totalBuildingAppraisal = totalBuildingAppraisal
						.add(buildingAppraisal);
				System.out.println("======= totalBuildingAppraisal : "
						+ totalBuildingAppraisal);
			}

			if ((property.getBuildingAliquot().floatValue() >= 0)
					&& (property.getBuildingAliquot().floatValue() < 100)) {
				totalBuildingAppraisal = totalBuildingAppraisal.multiply(
						property.getBuildingAliquot()).divide(cienBigD);
				System.out.println("======= Building Aliquot: "
						+ property.getBuildingAliquot());
				System.out.println("======= totalBuildingAppraisal Aliquot: "
						+ totalBuildingAppraisal);
			}

			totalBuildingAppraisal = totalBuildingAppraisal.setScale(2,
					RoundingMode.HALF_UP);

			if (temporalValues) {
				// BigDecimal aditionalValue = BigDecimal.ZERO;
				// if (anioAppraisal == 2015){
				// aditionalValue = totalBuildingAppraisal.multiply(new
				// BigDecimal(18.8)).divide(cienBigD, RoundingMode.HALF_UP);
				// }
				// totalBuildingAppraisal =
				// totalBuildingAppraisal.add(aditionalValue);
				property.getCurrentDomain().setBuildingAppraisalTmp(
						totalBuildingAppraisal);
				property.getCurrentDomain().setCommercialAppraisalTmp(
						totalBuildingAppraisal.add(lotAppraisal));
			} else {
				// BigDecimal aditionalValue = BigDecimal.ZERO;
				// if (anioAppraisal == 2015){
				// aditionalValue = totalBuildingAppraisal.multiply(new
				// BigDecimal(18.8)).divide(cienBigD, RoundingMode.HALF_UP);
				// }
				// totalBuildingAppraisal =
				// totalBuildingAppraisal.add(aditionalValue);
				property.getCurrentDomain().setBuildingAppraisal(
						totalBuildingAppraisal);
				property.getCurrentDomain().setCommercialAppraisal(
						totalBuildingAppraisal.add(lotAppraisal));
			}
		}

		return properties;

	}

	public void saveAppraisals(List<Property> properties, boolean temporalValues)
			throws Exception {
		for (Property property : properties) {
			setAppraisal(property.getCurrentDomain(), temporalValues);
		}
	}

	private void setAppraisal(Domain domain, boolean temporalValues)
			throws Exception {
		if (domain != null && domain.getId() != null) {
			if (!temporalValues) {
				Query query = entityManager
						.createNamedQuery("Domain.setAppraisals");
				query.setParameter("lotAppraisal", domain.getLotAppraisal());
				query.setParameter("buildingAppraisal",
						domain.getBuildingAppraisal());
				query.setParameter("commercialAppraisal",
						domain.getCommercialAppraisal());
				query.setParameter("domainId", domain.getId());
				query.executeUpdate();
				Appraisal appraisal = new Appraisal();
				appraisal.setLot(domain.getLotAppraisal());
				appraisal.setBuilding(domain.getBuildingAppraisal());
				appraisal.setCommercialAppraisal(domain
						.getCommercialAppraisal());
				appraisal.setValueBySquareMeter(domain.getValueBySquareMeter());
				appraisal.setProperty(domain.getCurrentProperty());
				// crudService.create(appraisal);
				entityManager.persist(appraisal);
			} else {
				Query query = entityManager
						.createNamedQuery("Domain.setAppraisalsTmp");
				query.setParameter("lotAppraisalTmp",
						domain.getLotAppraisalTmp());
				query.setParameter("buildingAppraisalTmp",
						domain.getBuildingAppraisalTmp());
				query.setParameter("commercialAppraisalTmp",
						domain.getCommercialAppraisalTmp());
				query.setParameter("domainId", domain.getId());
				query.executeUpdate();
			}
		}
	}

}
