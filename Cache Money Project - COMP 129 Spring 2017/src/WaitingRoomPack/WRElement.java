package WaitingRoomPack;

public class WRElement {
	private String name;
	private String msg;
	
	public WRElement(String name, String msg){
		setName(name);
		setMsg(msg);
	}
	public void setName(String name){
		this.name = name;
	}
	public void setMsg(String msg){
		this.msg = msg;
	}
	public String getName(){
		return name;
	}
	public String getMsg(){
		return msg;
	}
}
