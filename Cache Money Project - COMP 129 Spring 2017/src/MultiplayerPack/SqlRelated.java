package MultiplayerPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.mysql.cj.api.xdevapi.Result;

public final class SqlRelated {
	private static SqlRelated sqlRelated = new SqlRelated();
	private static final String IP_ADDRESS = "10.15.17.88";
	private static final String PORT_NUM = "3306";
	private static final String DATABASE = "cachemoneydb";
	private static final String USER_ID = "devinlim";
	private static final String USER_PW = "1234";
//	private static final String IP_ADDRESS = "localhost";
//	private static final String PORT_NUM = "3306";
//	private static final String DATABASE = "cachemoneydb";
//	private static final String USER_ID = "root";
//	private static final String USER_PW = "";
	private static Statement statement;
	private static Connection connection;
	private static ArrayList<ResultSet> resultSets;
	private static ResultSet rSet;
	private static String saving_statement;
	public static SqlRelated getInstance(){
		return new SqlRelated();
	}
	private SqlRelated(){
		saving_statement = "";
		statement = null;
		resultSets = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"+IP_ADDRESS
					+ ":" + PORT_NUM 
					+ "/" + DATABASE 
					+ "?useSSL=false", USER_ID , USER_PW);
			
			initResultSets();	
		} catch (Exception e) {
			System.out.println("***********************\nCONNECTION TO SQL FAILED.\nCheck to see if you are connected to the VPN or PacificNet, and then try again.\nDisable SQL in Property.java to load the game from text files.\n***********************");
			e.printStackTrace();
		}
	}
	
	
	
	
	
	private void initResultSets() throws SQLException{
		statement = connection.createStatement();
		resultSets.add(statement.executeQuery("SELECT b.Name, b.Family_id, b.Buying_Price, b.Mortgage_value, p.rent_base, p.rent_house_1, p.rent_house_2, p.rent_house_3, p.rent_house_4, p.rent_hotel, p.build_house_price " +
						"FROM base_prop_data b, property_data p " +
						"WHERE b.Data_id = p.Data_id " +
						"ORDER BY b.Data_id ASC;"));
		statement = connection.createStatement();
		resultSets.add(statement.executeQuery("SELECT b.Name, b.Family_id, b.Buying_Price, b.Mortgage_value, r.Rent_owned_1, r.Rent_owned_2, r.Rent_owned_3, r.Rent_owned_4 " +
				"FROM base_prop_data b, railroad_data r " +
				"WHERE b.Data_id = r.Data_id " +
				"ORDER BY b.Data_id ASC;"));
		statement = connection.createStatement();
		resultSets.add(statement.executeQuery("SELECT b.Name, b.Family_id, b.Buying_Price, b.Mortgage_value, u.Rent_modifier_1, u.Rent_modifier_2 " +
				"FROM base_prop_data b, utility_data u " +
				"WHERE b.Data_id = u.Data_id " +
				"ORDER BY b.Data_id ASC;"));
	}
	/**
	 * 0 - property
	 * 1 - railroad
	 * 2 - utility
	 * @return
	 */
	public static void getNextP(int propType){
		try {
			resultSets.get(propType).next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 0 - property
	 * 1 - railroad
	 * 2 - utility
	 * @return
	 */
	public static String getPName(int propType){
		try {
			return resultSets.get(propType).getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 0 - property
	 * 1 - railroad
	 * 2 - utility
	 * @return
	 */
	public static int getPFamilyId(int propType){
		try {
			return resultSets.get(propType).getInt(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 0 - property
	 * 1 - railroad
	 * 2 - utility
	 * @return
	 */
	public static int getPBuyingPrice(int propType){
		try {
			return resultSets.get(propType).getInt(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 0 - property
	 * 1 - railroad
	 * 2 - utility
	 * @return
	 */
	public static int getPMortgage(int propType){
		try {
			return resultSets.get(propType).getInt(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static int getPropertyRentBase(){
		try {
			return resultSets.get(0).getInt(5);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static int getPropertyRentHouse(int ithHouse){
		try {
			return resultSets.get(0).getInt(6+ithHouse);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static int getPropertyRentHotel(){
		try {
			return resultSets.get(0).getInt(10);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static int getPropertyBuildHousePrice(){
		try {
			return resultSets.get(0).getInt(11);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static int getRailRoadRentOwned(int ithOwned){
		try {
			return resultSets.get(1).getInt(5+ithOwned);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static int getUtilityRentModifier(int ithModifier){
		try {
			return resultSets.get(2).getInt(5+ithModifier);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static void insertNewUser(String user_id, String user_pw, String f_name, String l_name){
		try {
			statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO user_info (user_id, user_pw, f_name, l_name)"
					+ "VALUES ('"+user_id+"','"+user_pw+"', '"+f_name+"', '"+l_name+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void updateWinOrLose(String user_id, boolean isWin){
		try{
			statement = connection.createStatement();
			statement.executeUpdate("UPDATE user_info "
					+ "SET " + getWinOrLose(isWin) + " = " + getWinOrLose(isWin) + " + 1 "
					+ "WHERE user_id = '" + user_id + "';");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	private static String getWinOrLose(boolean isWin){
		return isWin?"win" : "lose";
	}
	public static boolean isIdExisting(String user_id){
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT COUNT(user_id) AS num "
					+ "FROM user_info "
					+ "WHERE user_id='"+user_id+"';");
			rSet.next();
			if(rSet.getInt(1) > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void loginAndOutAction(String user_id, boolean isLogIn){
		try {
			statement = connection.createStatement();
			statement.executeUpdate("UPDATE user_info "
					+ "SET isOn=" + isLogIn +" " 
							+ "WHERE user_id='"+ user_id +"';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static boolean checkingLogin(String user_id, String user_pw){
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT COUNT(user_id) "
						+ "FROM user_info "
						+ "WHERE user_id='"+user_id+"' AND BINARY user_pw='"+user_pw+"';");
			rSet.next();
			if(rSet.getInt(1) > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean isLoggedInAlready(String user_id){
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT isOn "
						+ "FROM user_info "
						+ "WHERE user_id='"+user_id+"';");
			rSet.next();
			return rSet.getBoolean(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void generateUserInfo(String user_id){
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT CONCAT(f_name,\" \",l_name) AS full_name, win, lose "
						+ "FROM user_info "
						+ "WHERE user_id = '"+user_id+"';");
			rSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static String getUserName(){
		try {
			return rSet.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static int getWin(){
		try {
			return rSet.getInt(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public static int getLose(){
		try {
			return rSet.getInt(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public int saveGameBeginning(int current_state, int current_turn, int numPlayer){
		try {
			System.out.println(saving_statement);
			statement = connection.createStatement();
			statement.execute("INSERT INTO saved_game (current_state,current_turn, num_Player) " +
					"VALUES ("+current_state +","+current_turn+","+numPlayer+"); ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT @Game_Num := MAX(saved_num) " +
											"FROM saved_game; ");
			rSet.next();
			return rSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public void saveGameUser(int gameNum, String user_id, boolean isAlive, int player_num, boolean inJail, int posNum ,int totMoney, int jailFree, String tradeRequest){
		try {
			System.out.println(saving_statement);
			statement = connection.createStatement();
			statement.execute("INSERT INTO saved_game_user " +
					"VALUES ("+gameNum+",'"+user_id+"', "+isAlive+", "+player_num+", "+inJail+", "+posNum+", "+totMoney+", "+jailFree+", '"+tradeRequest+"', "+(player_num == 0)+"); ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		saving_statement += "INSERT INTO saved_game_user " +
//				"VALUES ("+gameNum+",'"+user_id+"', "+isAlive+", "+player_num+", "+inJail+", "+posNum+", "+totMoney+", "+jailFree+", '"+tradeRequest+"'); ";
	}
	public void saveProperty(int gameNum,String propName, int multiplier, boolean isMortgaged, int numHouse, int numHotel, int ownedBy){
		try {
			System.out.println(saving_statement);
			statement = connection.createStatement();
			statement.execute("INSERT INTO saved_game_property " +
					"VALUES ("+gameNum+",'"+propName+"', "+multiplier+", "+isMortgaged+", "+numHouse+", "+numHotel+", "+ownedBy+"); ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
//				saving_statement += "INSERT INTO saved_game_property " +
//				"VALUES ("+gameNum+",'"+propName+"', "+multiplier+", "+isMortgaged+", "+numHouse+", "+numHotel+", "+ownedBy+"); ";
	}
	public void updateGameSaved(int gameNum, int turn){
		try {
			statement = connection.createStatement();
			statement.execute("UPDATE saved_game " 
					+ "SET current_turn = "+turn+" "
					+ "WHERE saved_num = "+gameNum+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateGameUser(int gameNum, String user_id, boolean isAlive, boolean inJail, int posNum ,int totMoney, int jailFree, String tradeRequest){
		try {
			statement = connection.createStatement();
			statement.execute("UPDATE saved_game_user " 
					+ "SET is_alive = "+isAlive+", in_jail="+inJail+", pos_num = "+posNum+", tot_money = "+totMoney+", jail_free = "+jailFree+", trade_request = '"+tradeRequest+"' "
					+ "WHERE saved_num = "+gameNum+" AND user_id='"+user_id+"';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		saving_statement += "INSERT INTO saved_game_user " +
//				"VALUES ("+gameNum+",'"+user_id+"', "+isAlive+", "+player_num+", "+inJail+", "+posNum+", "+totMoney+", "+jailFree+", '"+tradeRequest+"'); ";
	}
	public void deleteAllProp(int gameNum){
		try {
			statement = connection.createStatement();
			statement.execute("DELETE FROM saved_game_property " 
					+ "WHERE saved_num = "+gameNum+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		saving_statement += "INSERT INTO saved_game_user " +
//				"VALUES ("+gameNum+",'"+user_id+"', "+isAlive+", "+player_num+", "+inJail+", "+posNum+", "+totMoney+", "+jailFree+", '"+tradeRequest+"'); ";
	}
	public void insertSavingGame(){
		try {
			System.out.println(saving_statement);
			statement = connection.createStatement();
			statement.execute(saving_statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int[] getLoadingBeginning(int savedNum){
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT current_turn,num_player "
						+ "FROM saved_game "
						+ "WHERE saved_num = "+ savedNum+";");
			rSet.next();
			return new int[] {rSet.getInt(1), rSet.getInt(2)};
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new int[]{0,0};
	}
	public ResultSet importUserInfo(int savedNum){
		try {
			statement = connection.createStatement();
			return statement.executeQuery("SELECT player_num, user_id,is_alive, jail_free, pos_num, tot_money, in_jail, trade_request  "
						+ "FROM saved_game_user "
						+ "WHERE saved_num = "+ savedNum+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ResultSet importPropInfo(int savedNum, int playerNo){
		try {
			statement = connection.createStatement();
			return statement.executeQuery("SELECT property_name, owned_by, num_house, num_hotel, multiplier, is_mortgaged "
						+ "FROM saved_game_property "
						+ "WHERE saved_num="+ savedNum+" AND owned_by="+playerNo+";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<String> getLoadingUserList(int savedNum){
		ArrayList<String> list = new ArrayList<>();
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT user_id "
						+ "FROM saved_game_user "
						+ "WHERE saved_num="+ savedNum+";");
			while(rSet.next()){
				list.add(rSet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<ArrayList<Integer>> getLoadingGameList(String userId){
		ArrayList<ArrayList<Integer>> list = new ArrayList<>();
		list.add(new ArrayList<>());
		list.add(new ArrayList<>());
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT u.saved_num, g.num_Player "
						+ "FROM saved_game g, saved_game_user u "
						+ "WHERE u.user_id='"+userId+"' AND u.is_host AND g.saved_num=u.saved_num;");
			while(rSet.next()){
				list.get(0).add(rSet.getInt(1));
				list.get(1).add(rSet.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void addFriend(String firstId, String secondId){
		try {
			statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO friend_list "
					+ "VALUES ('"+firstId+"','"+secondId+"', NOW()),"
							+ "('"+secondId+"','"+firstId+"', NOW());");
			
		} catch (SQLException e) {
//			e.printStackTrace();
		}
	}
	public static void removeFriend(String firstId, String secondId){
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM friend_list "
					+ "WHERE user_id = '"+firstId+"' AND friend_id='"+secondId+"'"+" OR user_id = '"+secondId+"' AND friend_id='"+firstId+"';");
			
		} catch (SQLException e) {
//			e.printStackTrace();
		}
	}
	public ResultSet getFriend(String myId){
		try {
			statement = connection.createStatement();
			return statement.executeQuery("SELECT friend_id "
					+ "FROM friend_list "
					+ "WHERE user_id = '"+myId+"';");
		} catch (SQLException e) {
//			e.printStackTrace();
		}
		return null;
	}
	public static boolean isFriend(String firstId, String secondId){
		try {
			statement = connection.createStatement();
			ResultSet rSet = statement.executeQuery("SELECT COUNT(user_id) "
					+ "FROM friend_list "
					+ "WHERE user_id = '"+firstId+"' AND friend_id='"+secondId+"';");
			rSet.next();
			return rSet.getInt(1)>0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void setPlayerStatus(int status, String user_id){
		(new UpdatePlayerStat(status, user_id)).start();
	}
	static class UpdatePlayerStat extends Thread{
		int status;
		String user_id;
		public UpdatePlayerStat(int status, String user_id){
			this.status = status;
			this.user_id = user_id;
		}
		public void run(){
			try {
				statement = connection.createStatement();
				statement.executeUpdate("UPDATE user_info "
									+ "SET online_stat= " + status
									+ " WHERE user_id = '"+user_id+"';");
				
			} catch (SQLException e) {
//				e.printStackTrace();
			}
		}
	}
	public static int getPlayerStatus(String user_id){
		try {
			statement = connection.createStatement();
			rSet = statement.executeQuery("SELECT online_stat "
									+ "FROM user_info "
									+ "WHERE user_id = '"+user_id+"';");
			rSet.next();
			return rSet.getInt(1);
		} catch (SQLException e) {
//			e.printStackTrace();
		}
		return 0;
	}
}
