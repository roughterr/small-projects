package db;
import java.sql.*;

// Клас для обмеження частоти авторизації користувачів. Перевірка іде по IP.
public class trylogin extends alpha {
	public trylogin (Connection conn) throws SQLException {
		super (conn);
	}
	public boolean minute(String ip, config siteconfig) throws SQLException { // Разів у хвилину.
		ResultSet rs = st.executeQuery("SELECT lastminute,sprob FROM tryloginminute WHERE ip = '" + ip + "'");
		long time = System.currentTimeMillis()/1000; // Поточний час.
		if (rs.next()) { // Якщо було знайдено такий IP в базі даних.
			int sprob = rs.getInt("sprob");
			long lastminute = rs.getLong("lastminute");
			long diff = time - lastminute; // Різниця між поточним часом і часом останньої спроби користувача.
			if (diff > 60) { // Якщо різниця більше 60 секунд.
				st.executeUpdate("REPLACE INTO tryloginminute (ip,lastminute,sprob) VALUES ('" +
				ip + "','" + time + "','1')");
				return true;
			}
			if (siteconfig.GetLong("tryloginminute") < sprob) { // Якщо кількість спроб перевищена, то
				return false; // заборонити доступ.
			}
			sprob++;
			st.executeUpdate("REPLACE INTO tryloginminute (ip,lastminute,sprob) VALUES ('" +
			ip + "','" + time + "','" + sprob + "')");
			return true;
		}
		else {
			st.executeUpdate("INSERT INTO tryloginminute (ip,lastminute,sprob) VALUES ('" +
			ip + "','" + time + "','1')");
			return true; // Доступ дозволено.
		}
	}
}