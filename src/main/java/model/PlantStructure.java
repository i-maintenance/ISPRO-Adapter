package model;

public class PlantStructure {
	private String foreignKey1 = "";
	private String foreignKey2 = "";
	private int id = 0;
	private String structureClass;
	public PlantStructure() {
		// default
	}
	public PlantStructure(int id) {
		this.id = id;
	}
	public PlantStructure(int id, String key1, String key2) {
		this.id = id;
		this.foreignKey1 = key1;
		this.foreignKey2 = key2;
	}
	public String getForeignKey1() {
		return foreignKey1;
	}
	public void setForeignKey1(String foreignKey1) {
		this.foreignKey1 = foreignKey1;
	}
	public String getForeignKey2() {
		return foreignKey2;
	}
	public void setForeignKey2(String foreignKey2) {
		this.foreignKey2 = foreignKey2;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStructureClass() {
		return structureClass;
	}
	public void setStructureClass(String structureClass) {
		this.structureClass = structureClass;
	} 

}
