-- Function: reports.sp_reporte_emision_other_details(character varying, character varying, character varying, character varying)

-- DROP FUNCTION reports.sp_reporte_emision_other_details(character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION reports.sp_reporte_emision_other_details(
    IN fecha_inicial character varying,
    IN fecha_final character varying,
    IN estados_ids character varying,
    IN cuenta_id character varying)
  RETURNS TABLE(cuentaid bigint, accountcode character varying, accountname character varying, cantidad_emisiones bigint, valor_emision numeric, cantidad_bajas bigint, valor_bajas numeric, total_emision numeric, tipo character varying) AS
$BODY$
DECLARE sdate date;
DECLARE edate date;
DECLARE cuentaid BIGINT:= NULL;
DECLARE return_iva INTEGER;
BEGIN
	sdate = to_date(fecha_inicial, 'yyyy-mm-dd');
	edate = to_date(fecha_final, 'yyyy-mm-dd');
	
	IF cuenta_id = 'null' then
		cuentaid = NULL;
		return_iva = 1;
	ELSE
		cuentaid = cuenta_id::BIGINT;
		return_iva = 0;
	end if;

	RETURN QUERY
	select 	final_result.id,
		final_result.accountCode::character varying,
		final_result.accountname::character varying,
		final_result.cantidad::BIGINT,
		final_result.valor_emision,
		final_result.cantidad_bajas::BIGINT,
		final_result.valor_bajas,
		final_result.total_emision,
		final_result.tipo::character varying
	FROM
	-----FUTURAS
	((select ac.id, 
		ac.parameterFutureEmission::json->>'accountFutureCode' as accountCode,
		ac.parameterFutureEmission::json->>'accountFutureName' as accountname,
		count(*) as cantidad,
		SUM(i.total) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(i.total) as total_emision,
		'FUTURA' AS tipo
	from municipalbond mb
	INNER join item i ON i.municipalbond_id = mb.id
	INNER join entry e ON i.entry_id = e.id
	left join account ac ON e.account_id = ac.id 
	where mb.creationdate between sdate AND edate
	and i.total > 0
	AND mb.municipalBondStatus_id = 13
	GROUP BY ac.id, e.name,ac.accountCode,ac.parameterFutureEmission 
	ORDER BY ac.accountCode)
	UNION
	(select ac.id,
		ac.parameterFutureEmission::json->>'accountFutureCode' as accountCode,
		ac.parameterFutureEmission::json->>'accountFutureName' as accountname,
		count(*) as cantidad,
		SUM(ti.value) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(ti.value) as total_emision,
		'FUTURA' AS tipo
	from MunicipalBond mb 
	INNER join taxItem ti ON ti.municipalbond_id = mb.id
	left join tax t ON ti.tax_id = t.id
	left join Account ac  ON ac.id = t.taxaccount_id
	where mb.creationDate Between sdate AND edate
	and ti.value > 0
	AND mb.municipalBondStatus_id =13
	GROUP BY ac.id, t.name,ac.accountCode, ac.parameterFutureEmission 
	ORDER BY ac.accountCode)
	UNION
	--"{"accountFutureCode" : "9.1.1.23.01.01","accountFutureName" : "Costo de Procesamiento de Datos CPD","accountFormalizeCode" : "9.2.1.23.01.01","accountFormalizeName" : "Costo de Procesamiento de Datos CPD"}"
	-------
	--PAGO ANTICIPADO DE FUTURAS
	(select	ac.id,
		ac.parameterFutureEmission::json->>'accountFormalizeCode' as accountCode,
		ac.parameterFutureEmission::json->>'accountFormalizeName' as accountname,
		count(*) as cantidad,
		SUM(i.total) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(i.total) as total_emision,
		'FORMALIZACIONES PAGO ANTICIPADO' AS tipo
	from StatusChange sch 
	INNER JOIN municipalbond mb ON mb.id = sch.municipalbond_id
	INNER join item i ON i.municipalbond_id = mb.id
	INNER join entry e ON i.entry_id = e.id
	left join account ac ON e.account_id = ac.id 
	where mb.emisionDate Between sdate AND edate
	and sch.date Between sdate AND edate
	and i.total > 0
	AND sch.explanation = (select value from systemparameter where name = 'STATUS_CHANGE_FUTURE_EMISSION_EXPLANATION')
	GROUP BY ac.id, e.name,ac.accountCode,ac.parameterFutureEmission 
	ORDER BY ac.accountCode)
	UNION
	(select	ac.id,
		ac.parameterFutureEmission::json->>'accountFormalizeCode' as accountCode,
		ac.parameterFutureEmission::json->>'accountFormalizeName' as accountname,
		count(*) as cantidad,
		SUM(ti.value) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(ti.value) as total_emision,
		'FORMALIZACIONES PAGO ANTICIPADO' AS tipo 
	from statuschange sch 
	INNER JOIN municipalbond mb ON mb.id = sch.municipalbond_id
	INNER join taxItem ti ON ti.municipalbond_id = mb.id
	left join tax t ON ti.tax_id = t.id
	left join Account ac  ON ac.id = t.taxaccount_id
	where mb.emisionDate Between sdate AND edate
	and sch.date Between sdate AND edate
	and t.id NOT IN (2)
	AND sch.explanation = (select value from systemparameter where name = 'STATUS_CHANGE_FUTURE_EMISSION_EXPLANATION')
	GROUP BY ac.id, t.name,ac.accountCode,ac.parameterFutureEmission 
	ORDER BY ac.accountCode)
	UNION
	---FORMALIZACIONES NORMALES
	(select	ac.id,
		ac.parameterFutureEmission::json->>'accountFormalizeCode' as accountCode,
		ac.parameterFutureEmission::json->>'accountFormalizeName' as accountname,
		count(*) as cantidad,
		SUM(i.total) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(i.total) as total_emision,
		'FORMALIZACIONES NORMALES' AS tipo
	from StatusChange sch 
	INNER JOIN municipalbond mb ON mb.id = sch.municipalbond_id
	INNER join item i ON i.municipalbond_id = mb.id
	INNER join entry e ON i.entry_id = e.id
	left join account ac ON e.account_id = ac.id 
	where mb.emisionDate Between sdate AND edate
	and sch.date Between sdate AND edate
	and i.total > 0
	AND sch.explanation = (select value from systemparameter where name = 'STATUS_CHANGE_FOMALIZE_EMISSION_EXPLANATION')
	GROUP BY ac.id, e.name,ac.accountCode,ac.parameterFutureEmission 
	ORDER BY ac.accountCode)
	UNION
	(select	ac.id,
		ac.parameterFutureEmission::json->>'accountFormalizeCode' as accountCode,
		ac.parameterFutureEmission::json->>'accountFormalizeName' as accountname,
		count(*) as cantidad,
		SUM(ti.value) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(ti.value) as total_emision,
		'FORMALIZACIONES NORMALES' AS tipo
	from statuschange sch 
	INNER JOIN municipalbond mb ON mb.id = sch.municipalbond_id
	INNER join taxItem ti ON ti.municipalbond_id = mb.id
	left join tax t ON ti.tax_id = t.id
	left join Account ac  ON ac.id = t.taxaccount_id
	where mb.emisionDate Between sdate AND edate
	and sch.date Between sdate AND edate
	and t.id NOT IN (2)
	AND sch.explanation = (select value from systemparameter where name = 'STATUS_CHANGE_FOMALIZE_EMISSION_EXPLANATION')
	GROUP BY ac.id, t.name,ac.accountCode,ac.parameterFutureEmission 
	ORDER BY ac.accountCode)
	UNION
	--ANULADAS
	(select ac.id,
		ac.accountCode, 
		e.name, 
		count(*) as cantidad, 
		SUM(i.total) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(i.total) as total_emision,
		'ANULADAS' AS tipo
	from municipalbond mb
	INNER join item i ON i.municipalbond_id = mb.id
	INNER join entry e ON i.entry_id = e.id
	left join account ac ON e.account_id = ac.id 
	where mb.emisionDate Between sdate AND edate
	--and mb.reverseddate Between sdate AND edate
	and i.total > 0 
	--AND e.id = :entryId 
	AND mb.municipalBondStatus_id in(select value::bigint from systemparameter where name = 'MUNICIPAL_BOND_STATUS_ID_VOID') 
	and i.value > 0
	GROUP BY ac.id, e.name, ac.accountCode 
	ORDER BY ac.accountCode)
	UNION
	(select ac.id,
		ac.accountCode, 
		t.name,
		count(*) as cantidad, 
		SUM(ti.value) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(ti.value) as total_emision,
		'ANULADAS' AS tipo
	from MunicipalBond mb 
	INNER join taxItem ti ON ti.municipalbond_id = mb.id
	left join tax t ON ti.tax_id = t.id
	left join Account ac  ON ac.id = t.taxaccount_id
	where mb.emisionDate Between sdate AND edate
	--and mb.reverseddate Between sdate AND edate
	and t.id NOT IN (2)
	AND mb.municipalBondStatus_id in(select value::bigint from systemparameter where name = 'MUNICIPAL_BOND_STATUS_ID_VOID')
	GROUP BY ac.id, t.name,ac.accountCode 
	ORDER BY ac.accountCode)
	UNION
	--REVERSADAS
	(select ac.id,
		ac.accountCode, 
		e.name, 
		count(*) as cantidad, 
		SUM(i.total) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(i.total) as total_emision,
		'REVERSADAS' AS tipo
	from municipalbond mb
	INNER join item i ON i.municipalbond_id = mb.id
	INNER join entry e ON i.entry_id = e.id
	left join account ac ON e.account_id = ac.id 
	where mb.emisionDate Between sdate AND edate
	and mb.reverseddate Between sdate AND edate
	and i.total > 0 
	--AND e.id = :entryId 
	AND mb.municipalBondStatus_id in(select value::bigint from systemparameter where name = 'MUNICIPAL_BOND_STATUS_ID_REVERSED') 
	and i.value > 0
	GROUP BY ac.id, e.name, ac.accountCode 
	ORDER BY ac.accountCode)
	UNION
	(select ac.id,
		ac.accountCode, 
		t.name,
		count(*) as cantidad, 
		SUM(ti.value) as valor_emision,
		0 as cantidad_bajas,
		0.00 as valor_bajas,
		SUM(ti.value) as total_emision,
		'REVERSADAS' AS tipo
	from MunicipalBond mb 
	INNER join taxItem ti ON ti.municipalbond_id = mb.id
	left join tax t ON ti.tax_id = t.id
	left join Account ac  ON ac.id = t.taxaccount_id
	where mb.emisionDate Between sdate AND edate
	and mb.reverseddate Between sdate AND edate
	and t.id NOT IN (2)
	AND mb.municipalBondStatus_id in(select value::bigint from systemparameter where name = 'MUNICIPAL_BOND_STATUS_ID_REVERSED')
	GROUP BY ac.id, t.name,ac.accountCode 
	ORDER BY ac.accountCode)) final_result
	WHERE cuentaid IS NULL OR final_result.id = cuentaid
	ORDER BY final_result.tipo, final_result.accountCode;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION reports.sp_reporte_emision_other_details(character varying, character varying, character varying, character varying)
  OWNER TO rolgimloja;
