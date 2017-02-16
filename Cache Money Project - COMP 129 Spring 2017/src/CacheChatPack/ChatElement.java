package CacheChatPack;

public class ChatElement {
	private String name;
	private String msg;
	
	public ChatElement(String name, String msg){
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
