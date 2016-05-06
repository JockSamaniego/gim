-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
ALTER TABLE LotPosition ADD COLUMN factor numeric(19,6);
ALTER TABLE StreetMaterial ADD COLUMN factor numeric(19,6);

INSERT INTO FiscalPeriod (id, startDate, endDate, name) VALUES (1, '2012-01-01', '2012-12-31', 'Período Fiscal 2012');
INSERT INTO FiscalPeriod (id, startDate, endDate, name) VALUES (2, '2013-01-01', '2013-12-31', 'Período Fiscal 2013');

INSERT INTO CreditNoteType (id, name) VALUES ('1','Nota de crédito');
INSERT INTO CreditNoteType (id, name) VALUES ('2','Compensación');
INSERT INTO CreditNoteType (id, name) VALUES ('3','Embargo');

-- Valores de Catastro
INSERT INTO TerritorialDivisionType (id, name, priority) VALUES (1,'Provincia', 1);
INSERT INTO TerritorialDivisionType (id, name, priority) VALUES (2,'Cantón', 2);
INSERT INTO TerritorialDivisionType (id, name, priority) VALUES (3,'Parroquia', 3);
INSERT INTO TerritorialDivisionType (id, name, priority) VALUES (4,'Zona', 4);
INSERT INTO TerritorialDivisionType (id, name, priority) VALUES (5,'Sector', 5);

INSERT INTO money(id, denomination, moneytype, "value") VALUES (1, '1', 'BILL', 1.00);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (2, '2', 'BILL', 2.00);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (3, '5', 'BILL', 5.00);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (4, '10', 'BILL', 10.00);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (5, '20', 'BILL', 20.00);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (6, '50', 'BILL', 50.00);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (7, '100', 'BILL', 100.00);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (8, '1 CENT', 'COIN', 0.01);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (9, '5 CENT', 'COIN', 0.05);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (10, '10 CENT', 'COIN', 0.10);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (11, '25 CENT', 'COIN', 0.25);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (12, '50 CENT', 'COIN', 0.50);
INSERT INTO money(id, denomination, moneytype, "value") VALUES (13, '1 DOLAR', 'COIN', 1.00);

INSERT INTO SystemParameter (name, classname, value) VALUES ('MINIMUM_BOND_YEAR', 'java.lang.Integer','1993');
INSERT INTO SystemParameter (name, classname, value) VALUES ('CREDIT_NOTE_INITIAL_PORCENT', 'java.math.BigDecimal','20');
INSERT INTO SystemParameter (name, classname, value) VALUES ('INTEREST_ACCOUNT_ID', 'java.lang.Long','307');
INSERT INTO SystemParameter (name, classname, value) VALUES ('QUOTAS_ACCOUNT_ID', 'java.lang.Long','1002');
INSERT INTO SystemParameter (name, classname, value) VALUES ('SHOW_SHIELD', 'java.lang.Boolean','false');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PSEUDO_RESIDENT_INITIAL_VALUE', 'java.lang.Long','500');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DEFAULT_CITY', 'java.lang.String','LOJA');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DEFAULT_COUNTRY', 'java.lang.String','ECUADOR');
INSERT INTO SystemParameter (name, classname, value) VALUES ('NOTIFICATION_LEGAL_NOTE', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('NOTIFICATION_TASK_TYPE_CANCELLED', 'java.lang.Long','4');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PARKING_VALUE_FOR_HOUR', 'java.math.BigDecimal','0.35');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PARKING_EXTENDED_TIME', 'java.lang.Long','5');
INSERT INTO SystemParameter (name, classname, value) VALUES ('TERRITORIAL_DIVISION_CODE_PROVINCE', 'java.lang.String','11');
INSERT INTO SystemParameter (name, classname, value) VALUES ('TERRITORIAL_DIVISION_CODE_CANTON', 'java.lang.String','300');
INSERT INTO SystemParameter (name, classname, value) VALUES ('TERRITORIAL_DIVISION_TYPE_ID_PROVINCE', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('TERRITORIAL_DIVISION_TYPE_ID_CANTON', 'java.lang.Long','2');
INSERT INTO SystemParameter (name, classname, value) VALUES ('CONTRACT_TYPE_ID_WATERSUPLY', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('CONTRACT_TYPE_ID_DEATH', 'java.lang.Long','2');
INSERT INTO SystemParameter (name, classname, value) VALUES ('CONTRACT_TYPE_ID_RENTAL', 'java.lang.Long','3');
INSERT INTO SystemParameter (name, classname, value) VALUES ('CONTRACT_TYPE_ID_STAND', 'java.lang.Long','4');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_ADMINISTRATOR', 'java.lang.String','ROOT');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_EMISOR', 'java.lang.String','EMISOR');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_PREEMISOR', 'java.lang.String','PREEMISOR');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_CASHIER', 'java.lang.String','CAJERO');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_COMPENSATION_CASHIER', 'java.lang.String','CAJERO COMPENSACIONES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_REVENUE_BOSS', 'java.lang.String','JEFE RENTAS');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_REVENUE_CERTIFICATE', 'java.lang.String','CERTIFICADO DE RENTAS SYSTEM');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_INCOME_BOSS', 'java.lang.String','JEFE RECAUDACIONES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_PARKING_ADMINISTRATOR', 'java.lang.String','ADMINISTRADOR DE PARQUEADERO');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_DETAIL_CHECKER', 'java.lang.String','REVISAR DETALLE OBLIGACIONES MUNICIPALES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_MANAGER_BLOCKER', 'java.lang.String','ADMINISTRADOR BLOQUEO OBLIGACIONES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_MANAGER_INVALIDATOR', 'java.lang.String','ADMINISTRADOR BAJA OBLIGACIONES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_MANAGER_VOIDER', 'java.lang.String','ADMINISTRADOR ANULACION OBLIGACIONES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_MANAGER_PRINTER', 'java.lang.String','ADMINISTRADOR REIMPRESION OBLIGACIONES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_MANAGER_PREEMISSION', 'java.lang.String','ADMINISTRADOR PREEMISION OBLIGACIONES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_PRINTER_RESCUER', 'java.lang.String','ADMINISTRADOR REIMPRESION ORIGINALES');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ROLE_NAME_EDITION_READING', 'java.lang.String','EDICION DE LECTURA');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DEFAULT_INSTITUTION_ID', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PROPERTY_TYPE_ID_URBAN', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PROPERTY_TYPE_ID_RUSTIC', 'java.lang.Long','2');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_ALCABALA', 'java.lang.Long','58');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_UTILITY', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_URBAN_PROPERTY', 'java.lang.Long','56');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_RUSTIC_PROPERTY', 'java.lang.Long','57');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_RENT_ANTENNA', 'java.lang.Long','408');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_RENT_FENCE', 'java.lang.Long','120');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_RENT_LABEL_PALETTE', 'java.lang.Long','313');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_RENT_DEATH', 'java.lang.Long','24');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_WATER_SERVICE_TAX', 'java.lang.Long','76');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENTRY_ID_RENT_STAND', 'java.lang.Long','27');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_DRAFT', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_PREEMIT', 'java.lang.Long','2');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_PENDING', 'java.lang.Long','3');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT', 'java.lang.Long','4');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_BLOCKED', 'java.lang.Long','5');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_PAID', 'java.lang.Long','6');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_COMPENSATION', 'java.lang.Long','7');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_VOID', 'java.lang.Long','8');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_REVERSED', 'java.lang.Long','9');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_REJECTED', 'java.lang.Long','10');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL', 'java.lang.Long','11');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DELEGATE_ID_CADASTER', 'java.lang.Long','3');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DELEGATE_ID_REVENUE', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DELEGATE_ID_INCOME', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DELEGATE_ID_ADMINISTRATION', 'java.lang.Long','6');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DELEGATE_ID_PROCURATOR', 'java.lang.Long','4');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DELEGATE_ID_REGULATION_AND_CONTROL', 'java.lang.Long','5');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DELEGATE_ID_SANITATION', 'java.lang.Long','7');
INSERT INTO SystemParameter (name, classname, value) values ('DELEGATE_ID_FINANTIAL', 'java.lang.Long','10');
INSERT INTO SystemParameter (name, classname, value) VALUES ('ENABLE_RECEIPT_GENERATION', 'java.lang.Boolean','true');
INSERT INTO SystemParameter (name, classname, value) VALUES ('MINIMUM_EMISSION_YEAR', 'java.lang.Integer','2010');
INSERT INTO SystemParameter (name, classname, value) VALUES ('AMOUNT_BETWEEN_WATER_ROUTE', 'java.lang.Integer','5');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PARKING_ENTRY_DISCOUNTED', 'java.lang.Long','444');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PARKING_TAX_DISCOUNTED', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('INITIAL_RECEIPT_NUMBER', 'java.lang.Long','1');
INSERT INTO SystemParameter (name, classname, value) VALUES ('FINAL_RECEIPT_NUMBER', 'java.lang.Long','999999999');
INSERT INTO SystemParameter (name, classname, value) VALUES ('PASSWORD_RENEWAL_DAYS', 'java.lang.Integer','30');
INSERT INTO SystemParameter (name, classname, value) VALUES ('DAYS_TO_PAY_AFTER_NOTIFICATION', 'java.lang.Long','8');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SERVICE_REGISTER_ENABLED', 'java.lang.Boolean','TRUE');
insert into gimprod.SystemParameter (name, classname, value) values ('USER_NAME_ROOT', 'java.lang.String','ROOT');

INSERT INTO systemparameter (name, classname, description, value) VALUES ('CONSUMPTIONSTATE_ID_GENERATED', 'java.lang.Long', NULL, '1');
INSERT INTO systemparameter (name, classname, description, value) VALUES ('CONSUMPTIONSTATE_ID_ENTERED', 'java.lang.Long', NULL, '2');
INSERT INTO systemparameter (name, classname, description, value) VALUES ('CONSUMPTIONSTATE_ID_CHECKED', 'java.lang.Long', NULL, '3');
INSERT INTO systemparameter (name, classname, description, value) VALUES ('CONSUMPTIONSTATE_ID_PREEMITTED', 'java.lang.Long', NULL, '4');

--INSERT INTO "role"(id, description, "name") VALUES (2, 'Cajero', 'CAJERO');
--INSERT INTO "role"(id, description, "name") VALUES (3, 'Emisor', 'EMISOR');
--INSERT INTO "role"(id, description, "name") VALUES (4, 'Cajero compensaciones', 'CAJERO COMPENSACIONES');
--INSERT INTO "role"(id, description, "name") VALUES (5, 'Jefe rentas', 'JEFE RENTAS');
--INSERT INTO "role"(id, description, "name") VALUES (6, 'Jefe recaudaciones', 'JEFE RECAUDACIONES');
--INSERT INTO "role"(id, description, "name") VALUES (7, 'PreEmisor', 'PREEMISOR');
--INSERT INTO "role"(id, description, "name") VALUES (8, 'Administrador de Parqueadero', 'ADMINISTRADOR DE PARQUEADERO');
--INSERT INTO "role"(id, description, "name") VALUES (25, 'Revisar detalle de obligaciones municipales', 'REVISAR DETALLE OBLIGACIONES MUNICIPALES');
--INSERT INTO "role"(id, description, "name") VALUES (26, 'Revisor de obligaciones', 'ADMINISTRADOR BLOQUEO OBLIGACIONES');
--INSERT INTO "role"(id, description, "name") VALUES (27, 'Reversador de obligaciones emitidas', 'ADMINISTRADOR BAJA OBLIGACIONES');
--INSERT INTO "role"(id, description, "name") VALUES (28, 'Anulador de obligaciones', 'ADMINISTRADOR ANULACION OBLIGACIONES');
--INSERT INTO "role"(id, description, "name") VALUES (29, 'Reimpresor de obligaciones', 'ADMINISTRADOR REIMPRESION OBLIGACIONES');
--INSERT INTO "role"(id, description, "name") VALUES (30, 'Administrador de preemisiones', 'ADMINISTRADOR PREEMISION OBLIGACIONES');
--INSERT INTO "role"(id, description, "name") VALUES (39, 'Reimpresor de facturas originales', 'ADMINISTRADOR REIMPRESION ORIGINALES');

INSERT INTO MunicipalBondStatus (id, description, name) VALUES (1, 'Calculada para revisión sin ningún efecto legal','BORRADOR');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (2, 'Generada para su revisión y emisión en rentas','PRE EMITIDA');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (3, 'Emitida y adeudada por el contribuyente','PENDIENTE');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (4, 'A pagar por cuotas mediante un convenio','EN CONVENIO');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (5, 'Prohibida de cancelar por posible revisión','BLOQUEADA');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (6, 'El contribuyente ha cancelado los valores correspondientes','PAGADA');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (7, 'Factura generada en espera de pago por compensacion','EN COMPENSACION');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (8, 'Emitida y anulada en el mismo dia','ANULADA');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (9, 'Emitida y dada de baja luego de contabilizada','REVERSADA');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (10, 'Preemitida que no es aprobada para emision','RECHAZADA');
INSERT INTO MunicipalBondStatus (id, description, name) VALUES (11, 'El contribuyente ha cancelado los valores correspondientes usando una via electronica','PAGADA DESDE MEDIO EXTENO');


INSERT INTO ContractType (id, description, name, entry_id) VALUES (1, 'agua potable', 'agua potable', NULL);
INSERT INTO ContractType (id, description, name, entry_id) VALUES (2, 'arrendamiento de unidades de cementerios', 'cementerios', NULL);
INSERT INTO ContractType (id, description, name, entry_id) VALUES (3, 'Arrendar espacios para colocación de vallas, rótulos, antenas, etc.', 'Alquiler de espacios para espacios públicos', NULL);
INSERT INTO ContractType (id, description, name, entry_id) VALUES (4, 'Arrendamiento mercados', 'Arrendamiento mercados', NULL);
INSERT INTO ContractType (id, description, name, entry_id) VALUES (5, 'Arrendamiento estacionamientos', 'Arrendamiento estacionamientos',null );

INSERT INTO LotPosition (id, name, factor) VALUES (1, 'Esquinero', 1);
INSERT INTO LotPosition (id, name, factor) VALUES (2, 'Intermedio', 0.98);
INSERT INTO LotPosition (id, name, factor) VALUES (3, 'Cabecera', 1);
INSERT INTO LotPosition (id, name, factor) VALUES (4, 'Interior', 0.9);
INSERT INTO LotPosition (id, name, factor) VALUES (5, 'En Callejón', 0.97);


INSERT INTO SidewalkMaterial (id, code, name) VALUES (1, '1', 'No tiene');
INSERT INTO SidewalkMaterial (id, code, name) VALUES (2, '2', 'Hormigón');
INSERT INTO SidewalkMaterial (id, code, name) VALUES (3, '3', 'Baldosa/Gress');
INSERT INTO SidewalkMaterial (id, code, name) VALUES (4, '4', 'Adoquín');
INSERT INTO SidewalkMaterial (id, code, name) VALUES (5, '5', 'Solo bordillo');

INSERT INTO StreetMaterial (id, code, name, factor) VALUES (1, '1', 'No tiene vía', 0.8);
INSERT INTO StreetMaterial (id, code, name, factor) VALUES (2, '2', 'Tierra', 0.88);
INSERT INTO StreetMaterial (id, code, name, factor) VALUES (3, '3', 'Lastre', 0.9);
INSERT INTO StreetMaterial (id, code, name, factor) VALUES (4, '4', 'Asfalto', 0.95);
INSERT INTO StreetMaterial (id, code, name, factor) VALUES (5, '5', 'Adoquín', 1);
INSERT INTO StreetMaterial (id, code, name, factor) VALUES (6, '6', 'Hormigón', 1);

INSERT INTO StreetType (id, code, name) VALUES (1, '1', 'Avenida');
INSERT INTO StreetType (id, code, name) VALUES (2, '2', 'Calle Local');
INSERT INTO StreetType (id, code, name) VALUES (3, '3', 'Peatonal');
INSERT INTO StreetType (id, code, name) VALUES (4, '4', 'Escalinata');
INSERT INTO StreetType (id, code, name) VALUES (5, '5', 'Camino Vecinal');
INSERT INTO StreetType (id, code, name) VALUES (6, '6', 'Sendero');

INSERT INTO fencematerial (id, name) VALUES (1, 'NINGUNO');
INSERT INTO fencematerial (id, name) VALUES (2, 'LADRILLO');
INSERT INTO fencematerial (id, name) VALUES (3, 'VERJA/HIERRO');
INSERT INTO fencematerial (id, name) VALUES (4, 'MALLA');
INSERT INTO fencematerial (id, name) VALUES (5, 'PIEDRA');

INSERT INTO purchasetype (id, name) VALUES (1, 'COMPRA VENTA');
INSERT INTO purchasetype (id, name) VALUES (2, 'DONACION');
INSERT INTO purchasetype (id, name) VALUES (3, 'SUCESION');
INSERT INTO purchasetype (id, name) VALUES (4, 'COMODATO');
INSERT INTO purchasetype (id, name) VALUES (5, 'COPROPIEDAD');
INSERT INTO purchasetype (id, name) VALUES (6, 'PERMUTA');
INSERT INTO purchasetype (id, name) VALUES (7, 'REMATE');


--Valores de Agua 
INSERT INTO WaterMeterStatus (id, name) VALUES (1, 'Funcionando');
INSERT INTO WaterMeterStatus (id, name) VALUES (2, 'Sin Medidor');
INSERT INTO WaterMeterStatus (id, name) VALUES (3, 'Dañado');
INSERT INTO WaterMeterStatus (id, name) VALUES (4, 'Estimado');
INSERT INTO WaterMeterStatus (id, name) VALUES (5, 'Fuera de Servicio');

INSERT INTO WaterSupplyCategory (id, name) VALUES (1, 'Residencial');
INSERT INTO WaterSupplyCategory (id, name) VALUES (2, 'Comercial');
INSERT INTO WaterSupplyCategory (id, name) VALUES (3, 'Oficial');
INSERT INTO WaterSupplyCategory (id, name) VALUES (4, 'Oficial Medio');
INSERT INTO WaterSupplyCategory (id, name) VALUES (5, 'Industrial');
INSERT INTO WaterSupplyCategory (id, name) VALUES (6, 'Tercera Edad');
INSERT INTO WaterSupplyCategory (id, name) VALUES (7, 'Especial');
INSERT INTO WaterSupplyCategory (id, name) VALUES (8, 'Categoría 0');

INSERT INTO ConsumptionState (id, name) VALUES (1, 'GENERADO');
INSERT INTO ConsumptionState (id, name) VALUES (2, 'INGRESADO');
INSERT INTO ConsumptionState (id, name) VALUES (3, 'CHEQUEADO');
INSERT INTO ConsumptionState (id, name) VALUES (4, 'PREEMITIDO');

INSERT INTO SpaceType (id, name, reason) VALUES (1, 'ANTENA','Art. 10');
INSERT INTO SpaceType (id, name, reason) VALUES (2, 'VALLA','Art. 11');
INSERT INTO SpaceType (id, name, reason) VALUES (3, 'ROTULO','Art. 12');
INSERT INTO SpaceType (id, name, reason) VALUES (4, 'PALETA','Art. 13');

--INSERT INTO Account (id, code, name, parent_id) VALUES (1, '0','Principal',NULL);

INSERT INTO TaxpayerRecord (id, address, fax, name, phone, number, slogan, isDefault) VALUES (1, 'Ecuador','555 5555', 'GAD', '999 9999', 1160000240001, 'People is the target', true);

INSERT INTO Charge (id, department, name, institution_id) VALUES (1, 'JEFATURA DE RENTAS','JEFE DE RENTAS',1);
INSERT INTO Charge (id, department, name, institution_id) VALUES (2, 'JEFATURA DE RECAUDACIONES','JEFE DE RECAUDACIONES',1);
INSERT INTO Charge (id, department, name, institution_id) VALUES (3, 'JEFATURA DE AVALÚOS Y CATASTROS','JEFE DE AVALÚOS Y CATASTROS',1);
INSERT INTO Charge (id, department, name, institution_id) VALUES (4, 'PROCURADURÍA SÍNDICA','PROCURADOR SÍNDICO',1);
INSERT INTO Charge (id, department, name, institution_id) VALUES (5, 'JEFATURA DE REGULACIÓN Y CONTROL','JEFE DE REGULACION Y CONTROL',1);
INSERT INTO Charge (id, department, name, institution_id) VALUES (6, 'DIRECCION ADMINISTRATIVA','DIRECTOR ADMINISTRATIVO MUNICIPAL',1);
INSERT INTO Charge (id, department, name, institution_id) VALUES (7, 'DIRECCION DE HIGIENE','JEFE DE HIGIENE DEL GADM-LOJA',1);

INSERT INTO Delegate (id, endDate, isActive, name, startDate, charge_id) VALUES (1, '2013-01-01', TRUE, 'Dra. Alicia Parra', '2012-01-01',1);
INSERT INTO Delegate (id, endDate, isActive, name, startDate, charge_id) VALUES (2, '2013-01-01', TRUE, 'Dra. María Augusta Solano', '2012-01-01',2);
INSERT INTO Delegate (id, endDate, isActive, name, startDate, charge_id) VALUES (3, '2013-01-01', TRUE, 'Arq. Wilson Carrión Escudero', '2012-01-01',3);
INSERT INTO Delegate (id, endDate, isActive, name, startDate, charge_id) VALUES (4, '2013-01-01', TRUE, 'Dra. María Alejandra Cueva', '2012-01-01',4);
INSERT INTO Delegate (id, endDate, isActive, name, startDate, charge_id) VALUES (5, '2013-01-01', TRUE, 'Arq. Sixto Lomas Bozmediano', '2012-01-01',5);
INSERT INTO Delegate (id, endDate, isActive, name, startDate, charge_id) VALUES (6, '2013-01-01', TRUE, 'Ing. Fausto Maldonado', '2012-01-01',6);
INSERT INTO Delegate (id, endDate, isActive, name, startDate, charge_id) VALUES (7, '2013-01-01', TRUE, 'Dra. Alba Mogrovejo Garrido', '2012-01-01',7);

INSERT INTO PropertyType (id, name, entry_id) VALUES (1, 'Urbano', NULL);
INSERT INTO PropertyType (id, name, entry_id) VALUES (2, 'Rústico', NULL);

INSERT INTO TimePeriod (id, daysNumber, name) VALUES (1, 0, 'A la vista');
INSERT INTO TimePeriod (id, daysNumber, name) VALUES (2, 1, 'Diario');
INSERT INTO TimePeriod (id, daysNumber, name) VALUES (3, 30, 'Mensual');
INSERT INTO TimePeriod (id, daysNumber, name) VALUES (4, 60, 'Bimensual');
INSERT INTO TimePeriod (id, daysNumber, name) VALUES (5, 39, 'Trimestral');
INSERT INTO TimePeriod (id, daysNumber, name) VALUES (6, 180, 'Semestral');
INSERT INTO TimePeriod (id, daysNumber, name) VALUES (7, 365, 'Anual');

INSERT INTO EntryTypeIncome(id, name) VALUES (1, 'Tributario');
INSERT INTO EntryTypeIncome(id, name) VALUES (2, 'No Tributario');

INSERT INTO Cementery(id, address, code, name, property, manager_id) VALUES (1, 'Daniel Alvarez - La Tebaida', '001', 'GENERAL', 'PUBLIC', null);
INSERT INTO Cementery(id, address, code, name, property, manager_id) VALUES (2, 'Yanacocha', '002', 'YANACOCHA', 'PUBLIC', null);
INSERT INTO Cementery(id, address, code, name, property, manager_id) VALUES (3, 'Obrapía', '003', 'OBRAPIA', 'PUBLIC', null);

INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (1, 'CREADO', 'CREADO', null, '1');
INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (2, 'NOTIFICADO', 'NOTIFICADO', null, '2');
INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (3, 'FACILIDADES DE PAGO', 'FACILIDADES DE PAGO', null, '3');
INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (4, 'CANCELADO', 'CANCELADO', null, '4');
INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (5, 'JUICIO', 'JUICIO', null, '5');
INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (6, 'RECLAMO ADMINISTRATIVO', 'RECLAMO ADMINISTRATIVO', null, '6');
INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (7, 'DEMANDA CONTENCIOSA TRIBUTARIA', 'DEMANDA CONTENCIOSA TRIBUTARIA', null, '7');
INSERT INTO notificationtasktype(id, description, "name", reports, "sequence")VALUES (8, 'BAJA', 'BAJA', null, '8');

INSERT INTO unittype ("id", "name") VALUES (1, 'Boveda');
INSERT INTO unittype ("id", "name") VALUES (2, 'Tumba');
INSERT INTO unittype ("id", "name") VALUES (3, 'Tumba Ilustres');
INSERT INTO unittype ("id", "name") VALUES (4, 'Nicho');
INSERT INTO unittype ("id", "name") VALUES (5, 'Osario');

-- Cambiar los valores correspondientes de IdentityGenerator 
INSERT INTO IdentityGenerator (name, value) VALUES ('Account',344);
INSERT INTO IdentityGenerator (name, value) VALUES ('Attachment',100009);
INSERT INTO IdentityGenerator (name, value) VALUES ('Block', 3013);
INSERT INTO IdentityGenerator (name, value) VALUES ('BlockLimit', 11232);
INSERT INTO IdentityGenerator (name, value) VALUES ('Building', 73712);
INSERT INTO IdentityGenerator (name, value) VALUES ('Cementery', 4);
INSERT INTO IdentityGenerator (name, value) VALUES ('Charge', 8);
INSERT INTO IdentityGenerator (name, value) VALUES ('ConsumptionState', 5);
INSERT INTO IdentityGenerator (name, value) VALUES ('Contract',739877);
INSERT INTO IdentityGenerator (name, value) VALUES ('ContractType',4);
INSERT INTO IdentityGenerator (name, value) VALUES ('Delegate', 8);
INSERT INTO IdentityGenerator (name, value) VALUES ('Domain', 48203);
INSERT INTO IdentityGenerator (name, value) VALUES ('Entry', 436);
INSERT INTO IdentityGenerator (name, value) VALUES ('EntryDefinition', 9);
INSERT INTO IdentityGenerator (name, value) VALUES ('EntryStructure', 5);
INSERT INTO IdentityGenerator (name, value) VALUES ('EntryTypeIncome', 3);
INSERT INTO IdentityGenerator (name, value) VALUES ('FenceMaterial',6);
INSERT INTO IdentityGenerator (name, value) VALUES ('FiscalPeriod',3);
INSERT INTO IdentityGenerator (name, value) VALUES ('Institution', 2);
INSERT INTO IdentityGenerator (name, value) VALUES ('InterestRate', 94);
INSERT INTO IdentityGenerator (name, value) VALUES ('LandUse', 962);
INSERT INTO IdentityGenerator (name, value) VALUES ('Location', 48205);
INSERT INTO IdentityGenerator (name, value) VALUES ('LotPosition', 6);
INSERT INTO IdentityGenerator (name, value) VALUES ('MunicipalBondStatus',10);
INSERT INTO IdentityGenerator (name, value) VALUES ('Neighborhood',61);
INSERT INTO IdentityGenerator (name, value) VALUES ('NotificationTaskType',5);
INSERT INTO IdentityGenerator (name, value) VALUES ('Property', 53202);
INSERT INTO IdentityGenerator (name, value) VALUES ('PropertyLandUse', 55382);
INSERT INTO IdentityGenerator (name, value) VALUES ('PropertyType', 3);
INSERT INTO IdentityGenerator (name, value) VALUES ('PurchaseType',8);
INSERT INTO IdentityGenerator (name, value) VALUES ('Resident',212256);
INSERT INTO IdentityGenerator (name, value) VALUES ('Role',9);
INSERT INTO IdentityGenerator (name, value) VALUES ('Route',991);
INSERT INTO IdentityGenerator (name, value) VALUES ('SidewalkMaterial', 6);
INSERT INTO IdentityGenerator (name, value) VALUES ('SpaceType',5);
INSERT INTO IdentityGenerator (name, value) VALUES ('Street', 2533);
INSERT INTO IdentityGenerator (name, value) VALUES ('StreetMaterial', 7);
INSERT INTO IdentityGenerator (name, value) VALUES ('StreetType', 7);
INSERT INTO IdentityGenerator (name, value) VALUES ('TerritorialDivision',20348);
INSERT INTO IdentityGenerator (name, value) VALUES ('TerritorialDivisionType',6);
INSERT INTO IdentityGenerator (name, value) VALUES ('TimePeriod', 8);
INSERT INTO IdentityGenerator (name, value) VALUES ('UnitType', 6);
INSERT INTO IdentityGenerator (name, value) VALUES ('WaterMeter', 739878);
INSERT INTO IdentityGenerator (name, value) VALUES ('WaterMeterStatus', 6);
INSERT INTO IdentityGenerator (name, value) VALUES ('WaterSupply', 739878);
INSERT INTO IdentityGenerator (name, value) VALUES ('WaterSupplyCategory', 9);
INSERT INTO IdentityGenerator (name, value) VALUES ('Money', 14);
INSERT INTO IdentityGenerator (name, value) VALUES ('Action', 111);
INSERT INTO identitygenerator(name) VALUES ('ProductType');
INSERT INTO identitygenerator(name) VALUES ('RightsTransfer');
INSERT INTO identitygenerator(name) VALUES ('Till');
INSERT INTO identitygenerator(name) VALUES ('Deposit');
INSERT INTO identitygenerator(name) VALUES ('Death');
INSERT INTO identitygenerator(name) VALUES ('MarketEmission');
INSERT INTO identitygenerator(name) VALUES ('VehicleType');
INSERT INTO identitygenerator(name) VALUES ('ParkingLot');
INSERT INTO identitygenerator(name) VALUES ('ReceiptAuthorization');
INSERT INTO identitygenerator(name) VALUES ('Concession');
INSERT INTO identitygenerator(name) VALUES ('BudgetEntryType');
INSERT INTO identitygenerator(name) VALUES ('RoutePeriod');
INSERT INTO identitygenerator(name) VALUES ('User');
INSERT INTO identitygenerator(name) VALUES ('GarbageCollectionType');
INSERT INTO identitygenerator(name) VALUES ('Item');
INSERT INTO identitygenerator(name) VALUES ('Mortgage');
INSERT INTO identitygenerator(name) VALUES ('PaymentAgreement');
INSERT INTO identitygenerator(name) VALUES ('Appraisal');
INSERT INTO identitygenerator(name) VALUES ('Business');
INSERT INTO identitygenerator(name) VALUES ('TaxpayerRecord');
INSERT INTO identitygenerator(name) VALUES ('WorkdealFraction');
INSERT INTO identitygenerator(name) VALUES ('Tariff');
INSERT INTO identitygenerator(name) VALUES ('AssessmentType');
INSERT INTO identitygenerator(name) VALUES ('LandExemption');
INSERT INTO identitygenerator(name) VALUES ('Dividend');
INSERT INTO identitygenerator(name) VALUES ('Unit');
INSERT INTO identitygenerator(name) VALUES ('Section');
INSERT INTO identitygenerator(name) VALUES ('AuditRequest');
INSERT INTO identitygenerator(name) VALUES ('BusinessLocalReference');
INSERT INTO identitygenerator(name) VALUES ('Tax');
INSERT INTO identitygenerator(name) VALUES ('UnbuiltLot');
INSERT INTO identitygenerator(name) VALUES ('TaxRate');
INSERT INTO identitygenerator(name) VALUES ('Assessment');
INSERT INTO identitygenerator(name) VALUES ('Receipt');
INSERT INTO identitygenerator(name) VALUES ('DispatchReason');
INSERT INTO identitygenerator(name) VALUES ('Permission');
INSERT INTO identitygenerator(name) VALUES ('BusinessLocal');
INSERT INTO identitygenerator(name) VALUES ('StandType');
INSERT INTO identitygenerator(name) VALUES ('Workday');
INSERT INTO identitygenerator(name) VALUES ('RoutePreviewEmission');
INSERT INTO identitygenerator(name) VALUES ('Budget');
INSERT INTO identitygenerator(name) VALUES ('EmissionOrder');
INSERT INTO identitygenerator(name) VALUES ('Consumption');
INSERT INTO identitygenerator(name) VALUES ('Ticket');
INSERT INTO identitygenerator(name) VALUES ('MunicipalBond');
INSERT INTO identitygenerator(name) VALUES ('Issuer');
INSERT INTO identitygenerator(name) VALUES ('CashRecord');
INSERT INTO identitygenerator(name) VALUES ('CheckingRecord');
INSERT INTO identitygenerator(name) VALUES ('ConnectionSite');
INSERT INTO identitygenerator(name) VALUES ('Notification');
INSERT INTO identitygenerator(name) VALUES ('Journal');
INSERT INTO identitygenerator(name) VALUES ('Workdeal');
INSERT INTO identitygenerator(name) VALUES ('PreissuerPermission');
INSERT INTO identitygenerator(name) VALUES ('StatusChange');
INSERT INTO identitygenerator(name) VALUES ('TillPermission');
INSERT INTO identitygenerator(name) VALUES ('CreditNoteType');
INSERT INTO identitygenerator(name) VALUES ('Vehicle');
INSERT INTO identitygenerator(name) VALUES ('BudgetItem');
INSERT INTO identitygenerator(name) VALUES ('EconomicActivity');
INSERT INTO identitygenerator(name) VALUES ('ParkingRent');
INSERT INTO identitygenerator(name) VALUES ('BudgetEntry');
INSERT INTO identitygenerator(name) VALUES ('Payment');
INSERT INTO identitygenerator(name) VALUES ('PaymentFraction');
INSERT INTO identitygenerator(name) VALUES ('ReceiptType');
INSERT INTO identitygenerator(name) VALUES ('TaxItem');
INSERT INTO identitygenerator(name) VALUES ('Branch');
INSERT INTO identitygenerator(name) VALUES ('BuildingMaterialValue');
INSERT INTO identitygenerator(name) VALUES ('MaintenanceEntry');
INSERT INTO identitygenerator(name) VALUES ('Stand');
INSERT INTO identitygenerator(name) VALUES ('Address');
INSERT INTO identitygenerator(name) VALUES ('FinancialInstitution');
INSERT INTO identitygenerator(name) VALUES ('Exemption');
INSERT INTO identitygenerator(name) VALUES ('Place');
INSERT INTO identitygenerator(name) VALUES ('Boundary');
INSERT INTO identitygenerator(name) VALUES ('BudgetReference');
INSERT INTO identitygenerator(name) VALUES ('Local');
INSERT INTO identitygenerator(name) VALUES ('NotificationTask');
INSERT INTO identitygenerator(name) VALUES ('SessionRecord');
INSERT INTO identitygenerator(name) VALUES ('Adjunct');
INSERT INTO identitygenerator(name) VALUES ('VehicleMaker');
INSERT INTO identitygenerator(name) VALUES ('Space');
INSERT INTO identitygenerator(name) VALUES ('Market');
INSERT INTO identitygenerator(name) VALUES ('CreditNote');

CREATE SEQUENCE municipalBondNumber INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1;
  
--Para insertar comentarios en los campos de las tablas para auditoria
COMMENT ON COLUMN _user.id IS 'Código del Sistema';
COMMENT ON COLUMN _user.name IS 'Nombre de Usuario';
COMMENT ON COLUMN _user.password IS 'Contraseña';
COMMENT ON COLUMN _user.expirationdate IS 'Fecha de Expiración de la Contraseña';
COMMENT ON COLUMN _user.isactive IS 'Usuario Activo/Inactivo';

COMMENT ON COLUMN sessionrecord.username IS 'Nombre de Usuario';
COMMENT ON COLUMN sessionrecord.date IS 'Fecha';
COMMENT ON COLUMN sessionrecord.time IS 'Hora';
COMMENT ON COLUMN sessionrecord.ipaddress IS 'Dirección IP';
COMMENT ON COLUMN sessionrecord.sessionrecordtype IS 'Tipo de Registro';

COMMENT ON COLUMN receipt.authorizationnumber IS 'Autorización';
COMMENT ON COLUMN receipt.branch IS 'Establecimiento';
COMMENT ON COLUMN receipt.sequential IS 'Nro. de Comprobante';
COMMENT ON COLUMN receipt.status IS 'Estado';
COMMENT ON COLUMN receipt.till IS 'Caja';
COMMENT ON COLUMN receipt.municipalbond_id IS 'Obligación Municipal';
COMMENT ON COLUMN receipt.receipttype_id IS 'Tipo de Comprobante';

COMMENT ON COLUMN receiptauthorization.authorizationnumber IS 'Número Autorización';
COMMENT ON COLUMN receiptauthorization.enddate IS 'Fecha Hasta';
COMMENT ON COLUMN receiptauthorization.startdate IS 'Fecha Inicio';
COMMENT ON COLUMN receiptauthorization.taxpayerrecord_id IS 'Código Institución';

COMMENT ON COLUMN municipalbond.id IS 'Código Obligación';
COMMENT ON COLUMN municipalbond.address IS 'Dirección';
COMMENT ON COLUMN municipalbond.base IS 'Base Imponible';
COMMENT ON COLUMN municipalbond.creationdate IS 'Fecha Creación';
COMMENT ON COLUMN municipalbond.description IS 'Descripción';
COMMENT ON COLUMN municipalbond.discount IS 'Descuento';
COMMENT ON COLUMN municipalbond.emisiondate IS 'Fecha Emisión';
COMMENT ON COLUMN municipalbond.expirationdate IS 'Fecha Vencimiento';
COMMENT ON COLUMN municipalbond.groupingcode IS 'Código Agrupación';
COMMENT ON COLUMN municipalbond.interest IS 'Interés';
COMMENT ON COLUMN municipalbond.isnopasivesubject IS 'No es Sujeto Pasivo';
COMMENT ON COLUMN municipalbond.legalstatus IS 'Estado Legal';
COMMENT ON COLUMN municipalbond.number IS 'Número de Obligación';
COMMENT ON COLUMN municipalbond.paidtotal IS 'Pago Total';
COMMENT ON COLUMN municipalbond.printingsnumber IS 'Número Impresiones';
COMMENT ON COLUMN municipalbond.reference IS 'Referencia';
COMMENT ON COLUMN municipalbond.servicedate IS 'Fecha del Servicio';
COMMENT ON COLUMN municipalbond.surcharge IS 'Recargo';
COMMENT ON COLUMN municipalbond.taxabletotal IS 'Base Impuestos';
COMMENT ON COLUMN municipalbond.taxestotal IS 'Total Impuestos';
COMMENT ON COLUMN municipalbond.resident_id IS 'Código Contribuyente';

COMMENT ON COLUMN branch.address IS 'Dirección';
COMMENT ON COLUMN branch.name IS 'Nombre de Establecimiento';
COMMENT ON COLUMN branch.number IS 'Número';

COMMENT ON COLUMN tax.description IS 'Descripción';
COMMENT ON COLUMN tax.name IS 'name';

COMMENT ON COLUMN taxrate.enddate IS 'Fecha Final';
COMMENT ON COLUMN taxrate.rate IS 'Porcentaje';
COMMENT ON COLUMN taxrate.startdate IS 'Fecha Inicial';
COMMENT ON COLUMN taxrate.tax_id IS 'Código Impuesto';
