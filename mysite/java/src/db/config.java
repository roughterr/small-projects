package db;
import java.sql.*;
// Клас для роботи із налаштуваннями сайту.
public class config extends alpha {
	public config (Connection conn) throws SQLException {
		super (conn);
	}
	public String GetString (String parametr) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT znachen FROM configvarchar WHERE parametr='" + parametr + "'");
		if (rs.next()) return rs.getString("znachen"); // Якщо було знайдено такий параметр в базі даних.
		else return ""; // Якщо не знайдено такого параметру, то видача порожнього значення.
	}
	public long GetLong (String parametr) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT znachen FROM configbigint WHERE parametr='" + parametr + "'");
		if (rs.next()) return rs.getLong("znachen"); // Якщо було знайдено такий параметр в базі даних.
		else return Long.MIN_VALUE;
	}
	public boolean DoesExitString(String parametr) throws SQLException { // Метод для перевірки чи існує запис про параметр з рядковим значенням у базі даних.
		ResultSet rs = st.executeQuery("SELECT znachen FROM configvarchar WHERE parametr='" + parametr + "'");
		if (rs.next()) return true; // Якщо було знайдено такий параметр в базі даних.
		else return false;
	}
	public boolean DoesExitLong(String parametr) throws SQLException { // Метод для перевірки чи існує запис про параметр з числовим значенням у базі даних.
		ResultSet rs = st.executeQuery("SELECT znachen FROM configbigint WHERE parametr='" + parametr + "'");
		if (rs.next()) return true; // Якщо було знайдено такий параметр в базі даних.
		else return false;
	}
}