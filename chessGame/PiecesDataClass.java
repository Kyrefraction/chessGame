package chessGame;

public class PiecesDataClass {
	private int id;
	private String name;
	private String value;
	private String movement;
	private String notation;
	public PiecesDataClass(String name, String value, String movement, String notation, int id) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.movement = movement;
		this.notation = notation;
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMovement() {
		return movement;
	}
	public void setMovement(String movement) {
		this.movement = movement;
	}
	public String getNotation() {
		return notation;
	}
	public void setNotation(String notation) {
		this.notation = notation;
	}
	public String toString() {
		return "name = " + getName() + " value = " + getValue() + " movement = " + getMovement() + " notation = " + getNotation();
	}
}
