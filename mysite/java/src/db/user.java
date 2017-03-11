package db;
import java.sql.*;
import java.util.*;
import java.security.*;
// Клас для роботи з відомостями про користувача.
public class user extends alpha {
	private String login = "guest"; // За замовчуванням користувач — гість.
	public user(Connection conn) throws SQLException {
		super (conn);
	}
	public boolean login_with_hash(String s) throws SQLException { // Має отримати рядок у форматі логін^хеш.
		StringTokenizer stt = new StringTokenizer(s,"^");
		if (stt.countTokens() > 1) { // Якщо розбито більш як на 1 елемент.
			String login1 = stt.nextToken(); // Перший елемент — логін.
			String hash1 = stt.nextToken(); // Другий елемент — хеш.
			ResultSet rs = st.executeQuery("SELECT hash FROM users WHERE login ='" + login1 + "'");
			if (rs.next()) { // Якщо було знайдено такий логін в базі даних.
				if(hash1.equals(rs.getString("hash"))) { // І якщо хеші з коржика і в БД співпадають.
					login = login1; // Якщо хеші співпали, то присвоєння логіну.
					return true; // Повідомлення про те, що авторизація пройшла успішно.
				}
			}
		}
		return false; // Повідомлення про те, що не не вийшло залогінитися.
	}
	public boolean login_with_password (String login, String password) throws SQLException { // Метод для авторизації, використовуючи логін і пароль.
		ResultSet rs = st.executeQuery("SELECT pass FROM users WHERE login ='" + login + "'");
		if (rs.next()) { // Якщо було знайдено такий логін в базі даних.
			if (password.equals(rs.getString("pass"))) {
				this.login = login;
				return true;
			}
		}
		return false; // Якщо не було знайдено такого логіна, то видача значення "false", яке означає, що авторизація не вдалась.
	}
	public String getlogin() { // Дізнатись логін поточного користувача.
		return login;
	}
	public String get_string4login() throws SQLException { // Метод для видачі рядка, який використовуватиметься користувачем для авторизації по коржику.
		ResultSet rs = st.executeQuery("SELECT hash FROM users WHERE login ='" + login + "'");
		if (rs.next()) { // Якщо було знайдено такий логін в базі даних.
			return login + "^" + rs.getString("hash"); // Видача рядка у форматі логін^хеш.
		}
		return ""; // Якщо користувач не зареєстрований, то видача порожнього значення.
	}
	public boolean checkgroup (String group) throws SQLException { // Метод для перевірки чи користувач входить в певну групу.
		if (login.equals("guest")) {
			if (group.equals("guest")) return true; // Так, гість входить в групу "гість".
			else return false; // Це єдина група, в яку він входить.
		}
		ResultSet rs = st.executeQuery("SELECT `group` FROM `groupmap` WHERE user='" + login +"' AND `group`='" + group + "'");
		if (rs.next()) return true;
		else return false;
	}
	public boolean changepass(String newpassword) { // Метод для зміни паролю.
		Random r = new Random();
		String s = r.nextInt(12000) + newpassword + r.nextInt(9000); // Генерація випадкового рядка, який, все ж, містить пароль.
		String md5;
		try {
			md5 = make_md5(s);
		} catch (NoSuchAlgorithmException e) {
			return false; // Не удалось.
		}
		long time = System.currentTimeMillis()/1000; // Поточний час.
		try {
			st.executeUpdate("UPDATE users SET pass='" + newpassword + "',hash='" +
			md5  + // При зміні паролю також перегенеровується хеш.
			"',lastchangepass='" + time + // І також час останнього зміни паролю.
			"' WHERE login ='" + login + "'");
		}
		catch (SQLException e) {
			return false; // Не удалось.
		}
		return true;
	}
	public static String make_md5(String s) throws NoSuchAlgorithmException { // Метод для генерації md5-хешу.
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(s.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		}
		return sb.toString();
	}
	public long GetLastChangePass() throws SQLException { // Метод для отримання часу останньої зміни паролю користувачем, у форматі кількості секунд від 1970 року.
		ResultSet rs = st.executeQuery("SELECT lastchangepass FROM users WHERE login ='" + login + "'");
		if (rs.next()) { // Якщо було знайдено такий логін в базі даних.
			return rs.getLong("lastchangepass"); // Видача рядка у форматі логін^хеш.
		}
		return 0L; // Якщо користувач не зареєстрований, то видача нульового значення.
	}
}
