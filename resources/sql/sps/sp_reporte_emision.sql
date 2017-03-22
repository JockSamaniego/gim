-- Function: reports.sp_reporte_emision(character varying, character varying, character varying, character varying)

-- DROP FUNCTION reports.sp_reporte_emision(character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION reports.sp_reporte_emision(
    IN fecha_inicial character varying,
    IN fecha_final character varying,
    IN estados_ids character varying,
    IN cuenta_id character varying)
  RETURNS TABLE(cuentaid bigint, accountcode character varying, accountname character varying, cantidad_emisiones bigint, valor_emision numeric, cantidad_bajas bigint, valor_bajas numeric, total_emision numeric) AS
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
	select * from
		((select 
		emision.account_id, 
		emision.accountCode, 
		emision.accountName, 
		coalesce(emision.cantidad,0) as cantidad_emisiones,
		coalesce(emision.total,0) - coalesce(bajas_current.total,0) as valor_emision,
		COALESCE(bajas_current.cantidad, 0) as cantidad_bajas,
		COALESCE(bajas_current.total,0.00) AS valor_bajas,
		coalesce(emision.total,0) as total_emision
	from 
		(
		    --reporte emision
		    SELECT 	a.id as account_id, 
				a.accountCode, 
				a.accountName, 
				sum(total) total, 
				count(*) cantidad
		    FROM item i, municipalBond mb, entry e, account a
		    WHERE i.municipalBond_id=mb.id 
		    AND i.entry_id = e.id 
		    AND e.account_id = a.id 
		    --AND a.accountCode like '1.1.3.11.01.02.003.001'
		    --AND a.accountCode like '%'
		    AND mb.emisionDate BETWEEN sdate and edate
		    AND mb.municipalBondStatus_id in (SELECT UNNEST(string_to_array(estados_ids, ',')::BIGINT[]))
				AND (cuentaid IS NULL or a.id= cuentaid)
		    GROUP BY a.id
		    ORDER BY a.accountCode 
		) as emision
		full join (
		    --reporte bajas periodo consulta
		SELECT 	a.id, 
			a.accountCode, 
			a.accountName, 
			sum(total) total, 
			count(*) cantidad
		    FROM item i, municipalBond mb, entry e, account a
		    WHERE i.municipalBond_id=mb.id 
		    AND i.entry_id = e.id 
		    AND e.account_id = a.id 
		    --AND a.accountCode like '1.1.3.11.01.02.003.001'
		    --AND a.accountCode like '%'
		    AND mb.emisionDate BETWEEN sdate and edate
		    AND mb.municipalBondStatus_id in (9)
		    and mb.reverseddate BETWEEN sdate and edate
		    AND (cuentaid IS NULL or a.id= cuentaid)
		    GROUP BY a.id
		    ORDER BY a.accountCode
		) as bajas_current on emision.accountcode = bajas_current.accountcode
		ORDER BY emision.accountCode ASC)
		UNION
		((SELECT  taxes.id, 
			taxes.accountCode, 
			taxes.description,
			taxes.cantidad as cantidad,
			taxes.valor_emision - COALESCE(taxes_bajas.valor,0.00) as valor_emision,
			COALESCE(taxes_bajas.cantidad,0.00)::integer as cantidad_bajas,
			COALESCE(taxes_bajas.valor,0.00) as valor_bajas,
			taxes.valor_emision as total_emision
		FROM
		(select  t.id, 
			ac.accountCode, 
			t.description,
			count(*) as cantidad,
			SUM(ti.value) as valor_emision
		from MunicipalBond mb 
		INNER join taxItem ti ON ti.municipalbond_id = mb.id
		left join tax t ON ti.tax_id = t.id
		left join Account ac  ON ac.id = t.taxaccount_id
		where mb.emisionDate Between sdate and edate
		AND mb.municipalBondStatus_id in (SELECT UNNEST(string_to_array(estados_ids, ',')::BIGINT[]))
		and t.id NOT IN (2)
		AND 1 = return_iva
		GROUP BY t.id, t.name, ac.accountCode ORDER BY ac.accountCode) as taxes
		left JOIN 
		(select  t.id, 
			count(*) AS cantidad, 
			SUM(ti.value) as valor
		from MunicipalBond mb 
		INNER join taxItem ti ON ti.municipalbond_id = mb.id
		left join tax t ON ti.tax_id = t.id
		left join Account ac  ON ac.id = t.taxaccount_id
		where mb.emisionDate BETWEEN sdate and edate
		and mb.reversedDate BETWEEN sdate and edate
		and t.id not in (2)
		AND mb.municipalBondStatus_id in (9) 
		AND 1 = return_iva
		GROUP BY t.id, t.description,ac.accountCode ORDER BY ac.accountCode)as taxes_bajas ON taxes.id = taxes_bajas.id)))final_result
		ORDER BY final_result.accountCode ;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION reports.sp_reporte_emision(character varying, character varying, character varying, character varying)
  OWNER TO rolgimloja;
