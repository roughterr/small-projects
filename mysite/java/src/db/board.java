package db;
import java.sql.*;
import java.util.*;
// Клас тематичних дошок.
public class board extends alpha { // Екземпляри цього класу створюються з позиції відвідувача сайту.
	final private String path; // Ім’я дошки не змінюється.
	final private boolean admin; // Чи може користувач модерувати дошку.
	final private boolean bump; // Чи може бампати і створювати нові треди.
	final private boolean reply; // Чи може відписуватись у треди.
	final private String description; // Опис дошки.
	private user user; // Для отримання даних про користувача.
	public String GetPath() {
		return path;
	}
	public board (Connection conn, String path, user user) 
	throws SQLException,exceptions.NotFound,exceptions.AccessDenied {
		super (conn);
		this.path = path;
		this.user = user;
		ResultSet rs = super.st.executeQuery("SELECT `description`,`admingroup`,`readgroup`,`members`,`guests` FROM boards WHERE `path`='"  + path + "'");
		if (!rs.next()) throw new exceptions.NotFound(); // Якщо не знайдено такої дошки.
		description = rs.getString("description");
		if (user.checkgroup(rs.getString("admingroup"))) {
			admin = true;
			bump = true;
			reply = true;
		}
		else if (user.checkgroup(rs.getString("members"))) {
			admin = false;
			bump = true;
			reply = true;
		}
		else if (user.checkgroup(rs.getString("guests"))) {
			admin = false;
			bump = false;
			reply = true;
		}
		else if (user.checkgroup(rs.getString("readgroup"))) {
			admin = false;
			bump = false;
			reply = false;
		}
		else { // Якщо користувач навіть читати дошку не може, то відмовити у створенні
			throw new exceptions.AccessDenied(); // екземпляру цього класу,
		} // із видачею виключення.
	}
	public int CountTreads() throws SQLException { // Полічити треди.
		ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM threads WHERE `board`='"  + path + "'");
		return rs.getInt(1);
	}
	public LinkedHashSet<thread> GetThreads (int limit1, int limit2) throws SQLException {
		LinkedHashSet<thread> threads = new LinkedHashSet<thread>(); // Тут зберігатиметься список тредів.
		ResultSet rs = st.executeQuery("SELECT `id` FROM threads WHERE `board`='"  + path + "' ORDER BY `lastpost` DESC LIMIT " + limit1 + ", " + limit2);
		while(rs.next()) {
			int id = rs.getInt("id");
			thread thread = new thread(conn,id,admin,bump,reply,user.getlogin());
			threads.add(thread);
		}
		return threads;
	}
	public LinkedHashSet<thread> GetThreads () throws SQLException { // Видати усі треди з дошки.
		LinkedHashSet<thread> threads = new LinkedHashSet<thread>(); // Тут зберігатиметься список тредів.
		ResultSet rs = st.executeQuery("SELECT `id` FROM threads WHERE `board`='" + path + "' ORDER BY `lastpost` DESC");
		while(rs.next()) {
			int id = rs.getInt("id");
			thread thread = new thread(conn,id,admin,bump,reply,user.getlogin());
			threads.add(thread);
		}
		return threads;
	}
	public String GetDescription() { // Отримати опис дошки.
		return description;
	}
	public void DeleteThread(String id) throws SQLException,exceptions.NotFound { // Видалити тред поточної дошки.
		if (admin == false) throw new exceptions.AccessDenied(); // Видаляти можуть лише адміни.
		ResultSet rs = st.executeQuery("SELECT `id` FROM threads WHERE `board`='" + path + "' AND `id`='" + id + "'");
		if (!rs.next()) throw new exceptions.NotFound(); // Якщо трід не знайдено.
		rs.close();
		String sql = "DELETE FROM threads WHERE `board`='" + path + "' AND `id`=?";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, id);
		pst.executeUpdate();
		pst.executeUpdate();
		sql = "DELETE FROM posts WHERE `thread`=?";
		pst = conn.prepareStatement(sql);
		pst.setString(1, id);
		pst.executeUpdate();
		pst.executeUpdate();
	}
	public void CreateThread (String title, String op, String ip, boolean hidden) throws SQLException { // Створити тред.
		if (bump == false) throw new exceptions.AccessDenied(); // Якщо користувач не має права створювати треди.
		long time = (long) System.currentTimeMillis()/1000; // Поточний час у кількості секунд від 1970 року.
		String sql = "INSERT INTO threads (`board`,`lastpost`,`title`,`op`,`author`,`ip`,`hidden`,`founded`) "
		+ "VALUES ('" +  path + "',?,?,?,'" + user.getlogin() + "','" + ip + "'," + Boolean.toString(hidden) + ",?)";
		PreparedStatement pst=conn.prepareStatement(sql);
		long time1 = 2;
		String ash = Long.toString(time1);
		pst.setLong(1, time);
		pst.setString(2, title);
		pst.setString(3, op);
		pst.setLong(4, time);
		pst.executeUpdate();
		pst.close();
	}
	public boolean IsBump () { // Чи може користувач створювати нові треди.
		return bump;
	}
	public thread GetThread(int id) throws SQLException,exceptions.NotFound { // Отримати один тред, знаючи його id.
		String sql = ("SELECT `id` FROM threads WHERE `board`='" + path + "' AND `id`=?");
		PreparedStatement pst=conn.prepareStatement(sql);
		pst.setInt(1, id);
		ResultSet rs = pst.executeQuery();
		if (!rs.next()) throw new exceptions.NotFound();
		rs.close();
		pst.close();
		thread thread = new thread(conn,id,admin,bump,reply,user.getlogin());
		return thread;
	}
}