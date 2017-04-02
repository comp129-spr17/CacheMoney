package MultiplayerPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public final class SqlRelated {
	//private static SqlRelated sqlRelated = new SqlRelated();
	private static final String IP_ADDRESS = "www.cachemoney.com";
	private static final String PORT_NUM = "3306";
	private static final String DATABASE = "cachemoneydb";
	private static final String USER_ID = "devinlim";
	private static final String USER_PW = "1234";
//	private static final String IP_ADDRESS = "localhost";
//	private static final String PORT_NUM = "3306";
//	private static final String DATABASE = "cachemoneydb";
//	private static final String USER_ID = "root";
//	private static final String USER_PW = "";
	private Statement statement;
	private Connection connection;
	private static ArrayList<ResultSet> resultSets;
	public static SqlRelated getInstance() throws Exception{
		return new SqlRelated();
	}
	private SqlRelated() throws Exception{
		statement = null;
		resultSets = new ArrayList<>();
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"+IP_ADDRESS
					+ ":" + PORT_NUM 
					+ "/" + DATABASE 
					+ "?useSSL=false", USER_ID , USER_PW);
			initResultSets();	
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
}
