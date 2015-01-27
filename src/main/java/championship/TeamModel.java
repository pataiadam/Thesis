package championship;

public class TeamModel {
	private int id;
	private String name;
	
	public TeamModel(String s, int i){
		this.name=s;
		this.id=i;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
