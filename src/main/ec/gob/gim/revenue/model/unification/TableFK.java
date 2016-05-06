package ec.gob.gim.revenue.model.unification;

/**
 * solo es una vista para presentarla las relaciones de una tabla dada
 * 
 * @author richard
 *
 */
public class TableFK {

	private String tc_table_schema;
	private String tc_constraint_name;
	private String tc_table_name;
	private String kcu_column_name;
	private String ccu_table_name;
	
	private Long resident_id;
	private Double count;

	public String getTc_table_schema() {
		return tc_table_schema;
	}

	public void setTc_table_schema(String tc_table_schema) {
		this.tc_table_schema = tc_table_schema;
	}

	public String getTc_constraint_name() {
		return tc_constraint_name;
	}

	public void setTc_constraint_name(String tc_constraint_name) {
		this.tc_constraint_name = tc_constraint_name;
	}

	public String getTc_table_name() {
		return tc_table_name;
	}

	public void setTc_table_name(String tc_table_name) {
		this.tc_table_name = tc_table_name;
	}

	public String getKcu_column_name() {
		return kcu_column_name;
	}

	public void setKcu_column_name(String kcu_column_name) {
		this.kcu_column_name = kcu_column_name;
	}

	public String getCcu_table_name() {
		return ccu_table_name;
	}

	public void setCcu_table_name(String ccu_table_name) {
		this.ccu_table_name = ccu_table_name;
	}

	public Long getResident_id() {
		return resident_id;
	}

	public void setResident_id(Long resident_id) {
		this.resident_id = resident_id;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

}
