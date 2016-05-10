-- SQL statements which are executed at application startup if hibernate.hbm2ddl.auto is 'create' or 'create-drop'
insert into gimprod.FiscalPeriod (id, startDate, endDate, name) values (1, '2011-01-01', '2011-12-31', 'Período Fiscal 2011');
insert into gimprod.FiscalPeriod (id, startDate, endDate, name) values (2, '2012-01-01', '2012-12-31', 'Período Fiscal 2012');

insert into gimprod.CreditNoteType (id, name) values ('1','Nota de crédito');
insert into gimprod.CreditNoteType (id, name) values ('2','Compensación');
insert into gimprod.CreditNoteType (id, name) values ('3','Embargo');

-- Valores de Catastro
insert into gimprod.TerritorialDivisionType (id, name, priority) values (1,'Provincia', 1);
insert into gimprod.TerritorialDivisionType (id, name, priority) values (2,'Cantón', 2);
insert into gimprod.TerritorialDivisionType (id, name, priority) values (3,'Parroquia', 3);
insert into gimprod.TerritorialDivisionType (id, name, priority) values (4,'Zona', 4);
insert into gimprod.TerritorialDivisionType (id, name, priority) values (5,'Sector', 5);

INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (1, '1', 'BILL', 1.00);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (2, '2', 'BILL', 2.00);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (3, '5', 'BILL', 5.00);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (4, '10', 'BILL', 10.00);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (5, '20', 'BILL', 20.00);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (6, '50', 'BILL', 50.00);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (7, '100', 'BILL', 100.00);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (8, '1 CENT', 'COIN', 0.01);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (9, '5 CENT', 'COIN', 0.05);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (10, '10 CENT', 'COIN', 0.10);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (11, '25 CENT', 'COIN', 0.25);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (12, '50 CENT', 'COIN', 0.50);
INSERT INTO gimprod.money(id, denomination, moneytype, "value") VALUES (13, '1 DOLAR', 'COIN', 1.00);

insert into gimprod.SystemParameter (name, classname, value) values ('MINIMUM_BOND_YEAR', 'java.lang.Integer','1993');

insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SERVICE_REGISTER_ENABLED', 'java.lang.Boolean','TRUE');

insert into gimprod.SystemParameter (name, classname, value) values ('CREDIT_NOTE_INITIAL_PORCENT', 'java.lang.Long','20');

insert into gimprod.SystemParameter (name, classname, value) values ('INTEREST_ACCOUNT_ID', 'java.lang.Long','307');
insert into gimprod.SystemParameter (name, classname, value) values ('QUOTAS_ACCOUNT_ID', 'java.lang.Long','1002');


insert into gimprod.SystemParameter (name, classname, value) values ('SHOW_SHIELD', 'java.lang.Boolean','false');

insert into gimprod.SystemParameter (name, classname, value) values ('PSEUDO_RESIDENT_INITIAL_VALUE', 'java.lang.Long','500');

insert into gimprod.SystemParameter (name, classname, value) values ('DEFAULT_CITY', 'java.lang.String','LOJA');
insert into gimprod.SystemParameter (name, classname, value) values ('DEFAULT_COUNTRY', 'java.lang.String','ECUADOR');

insert into gimprod.SystemParameter (name, classname, value) values ('NOTIFICATION_LEGAL_NOTE', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('NOTIFICATION_TASK_TYPE_CANCELLED', 'java.lang.Long','4');

insert into gimprod.SystemParameter (name, classname, value) values ('PARKING_VALUE_FOR_HOUR', 'java.math.BigDecimal','0.35');
insert into gimprod.SystemParameter (name, classname, value) values ('PARKING_EXTENDED_TIME', 'java.lang.Long','5');

insert into gimprod.SystemParameter (name, classname, value) values ('TERRITORIAL_DIVISION_CODE_PROVINCE', 'java.lang.String','11');
insert into gimprod.SystemParameter (name, classname, value) values ('TERRITORIAL_DIVISION_CODE_CANTON', 'java.lang.String','300');

insert into gimprod.SystemParameter (name, classname, value) values ('TERRITORIAL_DIVISION_TYPE_ID_PROVINCE', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('TERRITORIAL_DIVISION_TYPE_ID_CANTON', 'java.lang.Long','2');

insert into gimprod.SystemParameter (name, classname, value) values ('CONTRACT_TYPE_ID_WATERSUPLY', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('CONTRACT_TYPE_ID_DEATH', 'java.lang.Long','2');
insert into gimprod.SystemParameter (name, classname, value) values ('CONTRACT_TYPE_ID_RENTAL', 'java.lang.Long','3');
insert into gimprod.SystemParameter (name, classname, value) values ('CONTRACT_TYPE_ID_STAND', 'java.lang.Long','4');

insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_ADMINISTRATOR', 'java.lang.String','ROOT');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_EMISOR', 'java.lang.String','EMISOR');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_PREEMISOR', 'java.lang.String','PREEMISOR');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_CASHIER', 'java.lang.String','CAJERO');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_COMPENSATION_CASHIER', 'java.lang.String','CAJERO COMPENSACIONES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_REVENUE_BOSS', 'java.lang.String','JEFE RENTAS');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_REVENUE_CERTIFICATE', 'java.lang.String','CERTIFICADO DE RENTAS SYSTEM');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_INCOME_BOSS', 'java.lang.String','JEFE RECAUDACIONES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_PARKING_ADMINISTRATOR', 'java.lang.String','ADMINISTRADOR DE PARQUEADERO');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_DETAIL_CHECKER', 'java.lang.String','REVISAR DETALLE OBLIGACIONES MUNICIPALES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_MANAGER_BLOCKER', 'java.lang.String','ADMINISTRADOR BLOQUEO OBLIGACIONES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_MANAGER_INVALIDATOR', 'java.lang.String','ADMINISTRADOR BAJA OBLIGACIONES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_MANAGER_VOIDER', 'java.lang.String','ADMINISTRADOR ANULACION OBLIGACIONES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_MANAGER_PRINTER', 'java.lang.String','ADMINISTRADOR REIMPRESION OBLIGACIONES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_MANAGER_PREEMISSION', 'java.lang.String','ADMINISTRADOR PREEMISION OBLIGACIONES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_PRINTER_RESCUER', 'java.lang.String','ADMINISTRADOR REIMPRESION ORIGINALES');
insert into gimprod.SystemParameter (name, classname, value) values ('ROLE_NAME_EDITION_READING', 'java.lang.String','EDICION DE LECTURA');

insert into gimprod.SystemParameter (name, classname, value) values ('DEFAULT_INSTITUTION_ID', 'java.lang.Long','1');

insert into gimprod.SystemParameter (name, classname, value) values ('PROPERTY_TYPE_ID_URBAN', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('PROPERTY_TYPE_ID_RUSTIC', 'java.lang.Long','2');

insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_ALCABALA', 'java.lang.Long','58');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_UTILITY', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_URBAN_PROPERTY', 'java.lang.Long','56');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_RUSTIC_PROPERTY', 'java.lang.Long','57');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_RENT_ANTENNA', 'java.lang.Long','408');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_RENT_FENCE', 'java.lang.Long','120');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_RENT_LABEL_PALETTE', 'java.lang.Long','313');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_RENT_DEATH', 'java.lang.Long','24');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_WATER_SERVICE_TAX', 'java.lang.Long','76');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_RENT_STAND', 'java.lang.Long','27');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_RENT_UNBUILTLOT', 'java.lang.Long','61');

insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_SEWERAGE', 'java.lang.Long','459');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_MASTER_PLAN', 'java.lang.Long','460');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_SECURITY', 'java.lang.Long','464');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_MICRO_WATERSHEDS', 'java.lang.Long','450');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_BASIC_COST', 'java.lang.Long','448');
insert into gimprod.SystemParameter (name, classname, value) values ('ENTRY_ID_TRASH', 'java.lang.Long','43');


insert into gimprod.SystemParameter (name, classname, value) values ('CONSUMPTIONSTATE_ID_GENERATED', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('CONSUMPTIONSTATE_ID_ENTERED', 'java.lang.Long','2');
insert into gimprod.SystemParameter (name, classname, value) values ('CONSUMPTIONSTATE_ID_CHECKED', 'java.lang.Long','3');
insert into gimprod.SystemParameter (name, classname, value) values ('CONSUMPTIONSTATE_ID_PREEMITTED', 'java.lang.Long','4');

insert into gimprod.SystemParameter (name, classname, value) values ('WATER_METER_STATUS_ID_WORKING', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_METER_STATUS_ID_WITHOUT', 'java.lang.Long','2');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_METER_STATUS_ID_DAMAGED', 'java.lang.Long','3');

insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_RESIDENTIAL', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_COMMERCIAL', 'java.lang.Long','2');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_OFFICIAL', 'java.lang.Long','3');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_HALF_OFFICIAL', 'java.lang.Long','4');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_INDUSTRIAL', 'java.lang.Long','5');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_OLD_PEOPLE', 'java.lang.Long','6');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_SPECIAL', 'java.lang.Long','7');
insert into gimprod.SystemParameter (name, classname, value) values ('WATER_SUPPLY_CATEGORY_ID_ZERO_CATEGORY', 'java.lang.Long','8');

insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_DRAFT', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_PREEMIT', 'java.lang.Long','2');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_PENDING', 'java.lang.Long','3');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT', 'java.lang.Long','4');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_BLOCKED', 'java.lang.Long','5');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_PAID', 'java.lang.Long','6');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_COMPENSATION', 'java.lang.Long','7');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_VOID', 'java.lang.Long','8');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_REVERSED', 'java.lang.Long','9');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_REJECTED', 'java.lang.Long','10');
insert into gimprod.SystemParameter (name, classname, value) values ('MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL', 'java.lang.Long','11');

insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_CADASTER', 'java.lang.Long','3');
insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_REVENUE', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_INCOME', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_ADMINISTRATION', 'java.lang.Long','6');
insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_PROCURATOR', 'java.lang.Long','4');
insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_REGULATION_AND_CONTROL', 'java.lang.Long','5');
insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_SANITATION', 'java.lang.Long','7');
insert into gimprod.SystemParameter (name, classname, value) values ('DELEGATE_ID_FINANTIAL', 'java.lang.Long','10');

insert into gimprod.SystemParameter (name, classname, value) values ('ENABLE_RECEIPT_GENERATION', 'java.lang.Boolean','true');
insert into gimprod.SystemParameter (name, classname, value) values ('MINIMUM_EMISSION_YEAR', 'java.lang.Integer','2010');

insert into gimprod.SystemParameter (name, classname, value) values ('AMOUNT_BETWEEN_WATER_ROUTE', 'java.lang.Integer','5');

insert into gimprod.SystemParameter (name, classname, value) values ('PARKING_ENTRY_DISCOUNTED', 'java.lang.Long','444');
insert into gimprod.SystemParameter (name, classname, value) values ('PARKING_TAX_DISCOUNTED', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('INITIAL_RECEIPT_NUMBER', 'java.lang.Long','1');
insert into gimprod.SystemParameter (name, classname, value) values ('FINAL_RECEIPT_NUMBER', 'java.lang.Long','999999999');

insert into gimprod.SystemParameter (name, classname, value) values ('PASSWORD_RENEWAL_DAYS', 'java.lang.Integer','30');

insert into gimprod.SystemParameter (name, classname, value) values ('DAYS_TO_PAY_AFTER_NOTIFICATION', 'java.lang.Long','8');
insert into gimprod.SystemParameter (name, classname, value) values ('USER_NAME_ROOT', 'java.lang.String','ROOT');

INSERT INTO gimprod."role"(id, description, "name") VALUES (1, 'Administrador general', 'ROOT');
INSERT INTO gimprod."role"(id, description, "name") VALUES (2, 'Cajero', 'CAJERO');
INSERT INTO gimprod."role"(id, description, "name") VALUES (3, 'Emisor', 'EMISOR');
INSERT INTO gimprod."role"(id, description, "name") VALUES (4, 'Cajero compensaciones', 'CAJERO COMPENSACIONES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (5, 'Jefe rentas', 'JEFE RENTAS');
INSERT INTO gimprod."role"(id, description, "name") VALUES (6, 'Jefe recaudaciones', 'JEFE RECAUDACIONES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (7, 'PreEmisor', 'PREEMISOR');
INSERT INTO gimprod."role"(id, description, "name") VALUES (8, 'Administrador de Parqueadero', 'ADMINISTRADOR DE PARQUEADERO');
INSERT INTO gimprod."role"(id, description, "name") VALUES (25, 'Revisar detalle de obligaciones municipales', 'REVISAR DETALLE OBLIGACIONES MUNICIPALES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (26, 'Revisor de obligaciones', 'ADMINISTRADOR BLOQUEO OBLIGACIONES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (27, 'Reversador de obligaciones emitidas', 'ADMINISTRADOR BAJA OBLIGACIONES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (28, 'Anulador de obligaciones', 'ADMINISTRADOR ANULACION OBLIGACIONES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (29, 'Reimpresor de obligaciones', 'ADMINISTRADOR REIMPRESION OBLIGACIONES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (30, 'Administrador de preemisiones', 'ADMINISTRADOR PREEMISION OBLIGACIONES');
INSERT INTO gimprod."role"(id, description, "name") VALUES (39, 'Reimpresor de facturas originales', 'ADMINISTRADOR REIMPRESION ORIGINALES');

insert into gimprod.MunicipalBondStatus (id, description, name) values (1, 'Calculada para revisión sin ningún efecto legal','BORRADOR');
insert into gimprod.MunicipalBondStatus (id, description, name) values (2, 'Generada para su revisión y emisión en rentas','PRE EMITIDA');
insert into gimprod.MunicipalBondStatus (id, description, name) values (3, 'Emitida y adeudada por el contribuyente','PENDIENTE');
insert into gimprod.MunicipalBondStatus (id, description, name) values (4, 'A pagar por cuotas mediante un convenio','EN CONVENIO');
insert into gimprod.MunicipalBondStatus (id, description, name) values (5, 'Prohibida de cancelar por posible revisión','BLOQUEADA');
insert into gimprod.MunicipalBondStatus (id, description, name) values (6, 'El contribuyente ha cancelado los valores correspondientes','PAGADA');
insert into gimprod.MunicipalBondStatus (id, description, name) values (7, 'Factura generada en espera de pago por compensacion','EN COMPENSACION');
insert into gimprod.MunicipalBondStatus (id, description, name) values (8, 'Emitida y anulada en el mismo dia','ANULADA');
insert into gimprod.MunicipalBondStatus (id, description, name) values (9, 'Emitida y dada de baja luego de contabilizada','REVERSADA');
insert into gimprod.MunicipalBondStatus (id, description, name) values (10, 'Preemitida que no es aprobada para emision','RECHAZADA');
insert into gimprod.MunicipalBondStatus (id, description, name) values (11, 'El contribuyente ha cancelado los valores correspondientes usando una via electronica','PAGADA DESDE MEDIO EXTENO');


insert into gimprod.ContractType (id, description, name, entry_id) values (1, 'agua potable', 'agua potable', NULL);
insert into gimprod.ContractType (id, description, name, entry_id) values (2, 'arrendamiento de unidades de cementerios', 'cementerios', NULL);
insert into gimprod.ContractType (id, description, name, entry_id) values (3, 'Arrendar espacios para colocación de vallas, rótulos, antenas, etc.', 'Alquiler de espacios para espacios públicos', NULL);
insert into gimprod.ContractType (id, description, name, entry_id) values (4, 'Arrendamiento mercados', 'Arrendamiento mercados', NULL);
insert into gimprod.ContractType (id, description, name, entry_id) values (5, 'Arrendamiento estacionamientos', 'Arrendamiento estacionamientos',30 );

insert into gimprod.LotPosition (id, name, factor) values (1, 'Esquinero', 1);
insert into gimprod.LotPosition (id, name, factor) values (2, 'Intermedio', 0.98);
insert into gimprod.LotPosition (id, name, factor) values (3, 'Cabecera', 1);
insert into gimprod.LotPosition (id, name, factor) values (4, 'Interior', 0.9);
insert into gimprod.LotPosition (id, name, factor) values (5, 'En Callejón', 0.97);


insert into gimprod.SidewalkMaterial (id, code, name) values (1, '1', 'No tiene');
insert into gimprod.SidewalkMaterial (id, code, name) values (2, '2', 'Hormigón');
insert into gimprod.SidewalkMaterial (id, code, name) values (3, '3', 'Baldosa/Gress');
insert into gimprod.SidewalkMaterial (id, code, name) values (4, '4', 'Adoquín');
insert into gimprod.SidewalkMaterial (id, code, name) values (5, '5', 'Solo bordillo');

insert into gimprod.StreetMaterial (id, code, name, factor) values (1, '1', 'No tiene vía', 0.8);
insert into gimprod.StreetMaterial (id, code, name, factor) values (2, '2', 'Tierra', 0.88);
insert into gimprod.StreetMaterial (id, code, name, factor) values (3, '3', 'Lastre', 0.9);
insert into gimprod.StreetMaterial (id, code, name, factor) values (4, '4', 'Asfalto', 0.95);
insert into gimprod.StreetMaterial (id, code, name, factor) values (5, '5', 'Adoquín', 1);
insert into gimprod.StreetMaterial (id, code, name, factor) values (6, '6', 'Hormigón', 1);

insert into gimprod.StreetType (id, code, name) values (1, '1', 'Avenida');
insert into gimprod.StreetType (id, code, name) values (2, '2', 'Calle Local');
insert into gimprod.StreetType (id, code, name) values (3, '3', 'Peatonal');
insert into gimprod.StreetType (id, code, name) values (4, '4', 'Escalinata');
insert into gimprod.StreetType (id, code, name) values (5, '5', 'Camino Vecinal');
insert into gimprod.StreetType (id, code, name) values (6, '6', 'Sendero');

insert into gimprod.fencematerial (id, name) VALUES (1, 'NINGUNO');
insert into gimprod.fencematerial (id, name) VALUES (2, 'LADRILLO');
insert into gimprod.fencematerial (id, name) VALUES (3, 'VERJA/HIERRO');
insert into gimprod.fencematerial (id, name) VALUES (4, 'MALLA');
insert into gimprod.fencematerial (id, name) VALUES (5, 'PIEDRA');

insert into gimprod.purchasetype (id, name) VALUES (1, 'COMPRA VENTA');
insert into gimprod.purchasetype (id, name) VALUES (2, 'DONACION');
insert into gimprod.purchasetype (id, name) VALUES (3, 'PARTICION');
insert into gimprod.purchasetype (id, name) VALUES (4, 'ADJUDICACION');
insert into gimprod.purchasetype (id, name) VALUES (5, 'COPROPIEDAD');
insert into gimprod.purchasetype (id, name) VALUES (6, 'PERMUTA');
insert into gimprod.purchasetype (id, name) VALUES (7, 'REMATE');
insert into gimprod.purchasetype (id, name) VALUES (8, 'GANANCIALES');

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
INSERT INTO identitygenerator(name) VALUES ('Municipalbond');
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

--Valores de Agua 
insert into gimprod.WaterMeterStatus (id, name) values (1, 'Funcionando');
insert into gimprod.WaterMeterStatus (id, name) values (2, 'Sin Medidor');
insert into gimprod.WaterMeterStatus (id, name) values (3, 'Dañado');
insert into gimprod.WaterMeterStatus (id, name) values (4, 'Estimado');
insert into gimprod.WaterMeterStatus (id, name) values (5, 'Fuera de Servicio');

insert into gimprod.WaterSupplyCategory (id, name) values (1, 'Residencial');
insert into gimprod.WaterSupplyCategory (id, name) values (2, 'Comercial');
insert into gimprod.WaterSupplyCategory (id, name) values (3, 'Oficial');
insert into gimprod.WaterSupplyCategory (id, name) values (4, 'Oficial Medio');
insert into gimprod.WaterSupplyCategory (id, name) values (5, 'Industrial');
insert into gimprod.WaterSupplyCategory (id, name) values (6, 'Tercera Edad');
insert into gimprod.WaterSupplyCategory (id, name) values (7, 'Especial');
insert into gimprod.WaterSupplyCategory (id, name) values (8, 'Categoría 0');

insert into gimprod.ConsumptionState (id, name) values (1, 'GENERADO');
insert into gimprod.ConsumptionState (id, name) values (2, 'INGRESADO');
insert into gimprod.ConsumptionState (id, name) values (3, 'CHEQUEADO');
insert into gimprod.ConsumptionState (id, name) values (4, 'PREEMITIDO');

insert into gimprod.SpaceType (id, name, reason) values (1, 'ANTENA','Art. 10');
insert into gimprod.SpaceType (id, name, reason) values (2, 'VALLA','Art. 11');
insert into gimprod.SpaceType (id, name, reason) values (3, 'ROTULO','Art. 12');
insert into gimprod.SpaceType (id, name, reason) values (4, 'PALETA','Art. 13');

--insert into gimprod.Account (id, code, name, parent_id) values (1, '0','Principal',NULL);

insert into gimprod.Institution (id, address, fax, name, phone, ruc, slogan) values (1, 'Bolívar y J. A. Eguiguren','2570407', 'Gobierno Autónomo Descentralizado Municipal de Loja', '2570407', 1160000240001, 'Loja para todos');




insert into gimprod.Charge (id, department, name, institution_id) values (1, 'JEFATURA DE RENTAS','JEFE DE RENTAS',1);
insert into gimprod.Charge (id, department, name, institution_id) values (2, 'JEFATURA DE RECAUDACIONES','JEFE DE RECAUDACIONES',1);
insert into gimprod.Charge (id, department, name, institution_id) values (3, 'JEFATURA DE AVALÚOS Y CATASTROS','JEFE DE AVALÚOS Y CATASTROS',1);
insert into gimprod.Charge (id, department, name, institution_id) values (4, 'PROCURADURÍA SÍNDICA','PROCURADOR SÍNDICO',1);
insert into gimprod.Charge (id, department, name, institution_id) values (5, 'JEFATURA DE REGULACIÓN Y CONTROL','JEFE DE REGULACION Y CONTROL',1);
insert into gimprod.Charge (id, department, name, institution_id) values (6, 'DIRECCION ADMINISTRATIVA','DIRECTOR ADMINISTRATIVO MUNICIPAL',1);
insert into gimprod.Charge (id, department, name, institution_id) values (7, 'DIRECCION DE HIGIENE','JEFE DE HIGIENE DEL GADM-LOJA',1);

insert into gimprod.Delegate (id, endDate, isActive, name, startDate, charge_id) values (1, '2013-01-01', TRUE, 'Dra. Alicia Parra', '2012-01-01',1);
insert into gimprod.Delegate (id, endDate, isActive, name, startDate, charge_id) values (2, '2013-01-01', TRUE, 'Dra. María Augusta Solano', '2012-01-01',2);
insert into gimprod.Delegate (id, endDate, isActive, name, startDate, charge_id) values (3, '2013-01-01', TRUE, 'Arq. Wilson Carrión Escudero', '2012-01-01',3);
insert into gimprod.Delegate (id, endDate, isActive, name, startDate, charge_id) values (4, '2013-01-01', TRUE, 'Dra. María Alejandra Cueva', '2012-01-01',4);
insert into gimprod.Delegate (id, endDate, isActive, name, startDate, charge_id) values (5, '2013-01-01', TRUE, 'Arq. Sixto Lomas Bozmediano', '2012-01-01',5);
insert into gimprod.Delegate (id, endDate, isActive, name, startDate, charge_id) values (6, '2013-01-01', TRUE, 'Ing. Fausto Maldonado', '2012-01-01',6);
insert into gimprod.Delegate (id, endDate, isActive, name, startDate, charge_id) values (7, '2013-01-01', TRUE, 'Dra. Alba Mogrovejo Garrido', '2012-01-01',7);

insert into gimprod.PropertyType (id, name, entry_id) values (1, 'Urbano', NULL);
insert into gimprod.PropertyType (id, name, entry_id) values (2, 'Rústico', NULL);

insert into gimprod.TimePeriod (id, daysNumber, name) values (1, 0, 'A la vista');
insert into gimprod.TimePeriod (id, daysNumber, name) values (2, 1, 'Diario');
insert into gimprod.TimePeriod (id, daysNumber, name) values (3, 30, 'Mensual');
insert into gimprod.TimePeriod (id, daysNumber, name) values (4, 60, 'Bimensual');
insert into gimprod.TimePeriod (id, daysNumber, name) values (5, 39, 'Trimestral');
insert into gimprod.TimePeriod (id, daysNumber, name) values (6, 180, 'Semestral');
insert into gimprod.TimePeriod (id, daysNumber, name) values (7, 365, 'Anual');

insert into gimprod.EntryTypeIncome(id, name) values (1, 'Tributario');
insert into gimprod.EntryTypeIncome(id, name) values (2, 'No Tributario');


insert into gimprod.Cementery(id, address, code, name, property, manager_id) values (1, 'Daniel Alvarez - La Tebaida', '001', 'GENERAL', 'PUBLIC', null);
insert into gimprod.Cementery(id, address, code, name, property, manager_id) values (2, 'Yanacocha', '002', 'YANACOCHA', 'PUBLIC', null);
insert into gimprod.Cementery(id, address, code, name, property, manager_id) values (3, 'Obrapía', '003', 'OBRAPIA', 'PUBLIC', null);

INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (1, 'CREADO', 'CREADO', null, '1');
INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (2, 'NOTIFICADO', 'NOTIFICADO', null, '2');
INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (3, 'FACILIDADES DE PAGO', 'FACILIDADES DE PAGO', null, '3');
INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (4, 'CANCELADO', 'CANCELADO', null, '4');
INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (5, 'JUICIO', 'JUICIO', null, '5');
INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (6, 'RECLAMO ADMINISTRATIVO', 'RECLAMO ADMINISTRATIVO', null, '6');
INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (7, 'DEMANDA CONTENCIOSA TRIBUTARIA', 'DEMANDA CONTENCIOSA TRIBUTARIA', null, '7');
INSERT INTO gimprod.notificationtasktype(id, description, "name", reports, "sequence")VALUES (8, 'BAJA', 'BAJA', null, '8');




INSERT INTO gimprod.unittype ("id", "name") VALUES (1, 'Boveda');
INSERT INTO gimprod.unittype ("id", "name") VALUES (2, 'Tumba');
INSERT INTO gimprod.unittype ("id", "name") VALUES (3, 'Tumba Ilustres');
INSERT INTO gimprod.unittype ("id", "name") VALUES (4, 'Nicho');
INSERT INTO gimprod.unittype ("id", "name") VALUES (5, 'Osario');

--Hashed Password AdmiN
INSERT INTO gimprod._User (id, name, password, isActive, isBlocked) 
	   VALUES (1, 'ROOT', 'c6f7591bfcc396b4c8ab468b0307b2c0', true, false);

INSERT INTO gimprod.Resident (id, residentType, identificationType, name, lastName, firstName, identificationNumber, user_id)
	   VALUES (1, 'N', 'NATIONAL_IDENTITY_DOCUMENT' ,'ADMINISTRADOR DEL SISTEMA', 'ADMINISTRADOR', 'DEL SISTEMA', '9999999999', 1);


INSERT INTO gimprod.Permission (id, permissionType, action_id, role_id, visibleFromMenu) 
	   VALUES (1, 'WRITE', 1, null, false);
INSERT INTO gimprod.Permission (id, permissionType, action_id, role_id, visibleFromMenu) 
	   VALUES (2, 'WRITE', 2, null, false);	   
	   
CREATE SEQUENCE gimprod.municipalBondNumber
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1;
  
CREATE SEQUENCE gimprod.residentNumber
  INCREMENT 1
  MINVALUE 9900000000
  MAXVALUE 9223372036854775807
  START 9900000000;
  
  
--Para insertar comentarios en los campos de las tablas para auditoria
COMMENT ON COLUMN gimprod._user.id
IS 'Código del Sistema';
COMMENT ON COLUMN gimprod._user.name
IS 'Nombre de Usuario';
COMMENT ON COLUMN gimprod._user.password
IS 'Contraseña';
COMMENT ON COLUMN gimprod._user.expirationdate
IS 'Fecha de Expiración de la Contraseña';
COMMENT ON COLUMN gimprod._user.isactive
IS 'Usuario Activo/Inactivo';




COMMENT ON COLUMN gimprod.sessionrecord.username
IS 'Nombre de Usuario';
COMMENT ON COLUMN gimprod.sessionrecord.date
IS 'Fecha';
COMMENT ON COLUMN gimprod.sessionrecord.time
IS 'Hora';
COMMENT ON COLUMN gimprod.sessionrecord.ipaddress
IS 'Dirección IP';
COMMENT ON COLUMN gimprod.sessionrecord.sessionrecordtype
IS 'Tipo de Registro';


COMMENT ON COLUMN gimprod.receipt.authorizationnumber
IS 'Autorización';
COMMENT ON COLUMN gimprod.receipt.branch
IS 'Establecimiento';
COMMENT ON COLUMN gimprod.receipt.sequential
IS 'Nro. de Comprobante';
COMMENT ON COLUMN gimprod.receipt.status
IS 'Estado';
COMMENT ON COLUMN gimprod.receipt.till
IS 'Caja';
COMMENT ON COLUMN gimprod.receipt.municipalbond_id
IS 'Obligación Municipal';
COMMENT ON COLUMN gimprod.receipt.receipttype_id
IS 'Tipo de Comprobante';


COMMENT ON COLUMN gimprod.receiptauthorization.authorizationnumber
IS 'Número Autorización';
COMMENT ON COLUMN gimprod.receiptauthorization.enddate
IS 'Fecha Hasta';
COMMENT ON COLUMN gimprod.receiptauthorization.startdate
IS 'Fecha Inicio';
COMMENT ON COLUMN gimprod.receiptauthorization.taxpayerrecord_id
IS 'Código Institución';


COMMENT ON COLUMN gimprod.municipalbond.id
IS 'Código Obligación';
COMMENT ON COLUMN gimprod.municipalbond.address
IS 'Dirección';
COMMENT ON COLUMN gimprod.municipalbond.base
IS 'Base Imponible';
COMMENT ON COLUMN gimprod.municipalbond.creationdate
IS 'Fecha Creación';
COMMENT ON COLUMN gimprod.municipalbond.description
IS 'Descripción';
COMMENT ON COLUMN gimprod.municipalbond.discount
IS 'Descuento';
COMMENT ON COLUMN gimprod.municipalbond.emisiondate
IS 'Fecha Emisión';
COMMENT ON COLUMN gimprod.municipalbond.expirationdate
IS 'Fecha Vencimiento';
COMMENT ON COLUMN gimprod.municipalbond.groupingcode
IS 'Código Agrupación';
COMMENT ON COLUMN gimprod.municipalbond.interest
IS 'Interés';
COMMENT ON COLUMN gimprod.municipalbond.isnopasivesubject
IS 'No es Sujeto Pasivo';
COMMENT ON COLUMN gimprod.municipalbond.legalstatus
IS 'Estado Legal';
COMMENT ON COLUMN gimprod.municipalbond.number
IS 'Número de Obligación';
COMMENT ON COLUMN gimprod.municipalbond.paidtotal
IS 'Pago Total';
COMMENT ON COLUMN gimprod.municipalbond.printingsnumber
IS 'Número Impresiones';
COMMENT ON COLUMN gimprod.municipalbond.reference
IS 'Referencia';
COMMENT ON COLUMN gimprod.municipalbond.servicedate
IS 'Fecha del Servicio';
COMMENT ON COLUMN gimprod.municipalbond.surcharge
IS 'Recargo';
COMMENT ON COLUMN gimprod.municipalbond.taxabletotal
IS 'Base Impuestos';
COMMENT ON COLUMN gimprod.municipalbond.taxestotal
IS 'Total Impuestos';
COMMENT ON COLUMN gimprod.municipalbond.resident_id
IS 'Código Contribuyente';



COMMENT ON COLUMN gimprod.branch.address
IS 'Dirección';
COMMENT ON COLUMN gimprod.branch.name
IS 'Nombre de Establecimiento';
COMMENT ON COLUMN gimprod.branch.number
IS 'Número';



COMMENT ON COLUMN gimprod.tax.description
IS 'Descripción';
COMMENT ON COLUMN gimprod.tax.name
IS 'name';



COMMENT ON COLUMN gimprod.taxrate.enddate
IS 'Fecha Final';
--COMMENT ON COLUMN gimprod.taxrate.is active
--IS 'Activo';
COMMENT ON COLUMN gimprod.taxrate.rate
IS 'Porcentaje';
COMMENT ON COLUMN gimprod.taxrate.startdate
IS 'Fecha Inicial';
COMMENT ON COLUMN gimprod.taxrate.tax_id
IS 'Código Impuesto';

  
