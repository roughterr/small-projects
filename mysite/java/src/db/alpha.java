package db;
import java.sql.SQLException;
// Усі інші класи в цьому пакеті мають унаслідуватися в� � цього.
abstract public class alpha {
	protected java.sql.Statement st;
	protected java.sql.Connection conn;
	public alpha (java.sql.Connection conn) throws SQLException {
		this.conn = conn;
		st = conn.createStatement();
	}
	protected void finalize () throws SQLException {
		st.close();
	}
}
