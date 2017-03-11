package db;
import java.sql.*;
import java.util.Date;
// Клас для роботи із списком забанених по IP користувачів.
public class banip extends alpha {
	public banip (Connection conn, String ip) throws SQLException,exceptions.AccessDenied {
		super (conn);
		ResultSet rs = st.executeQuery("SELECT time FROM banip WHERE ip = '" + ip + "';");
		if (rs.next()) { // Якщо в базі даних є запис про даний IP.
			Date date = new Date(); // Поточна дата.
			long timefromdb = rs.getLong("time"); // Отримання часу з БД.
			timefromdb *= 1000; // Час у БД зберігається у форматі секунд же.
			Date date2 = new Date(timefromdb); // Перетворення часу із БД у формат Date.
			if(date.before(date2)) { // Якщо час в БД є пізнішим за поточний, то заборонити користувачу будь-який доступ.
				throw new exceptions.AccessDenied();
			}
		}
		rs.close();
	}
}