package db;
import java.sql.*;
import java.util.LinkedHashSet;
public class thread extends alpha {
	protected final int id; // id треду.
	protected final boolean admin; // Чи можна адмініструвати.
	protected final boolean bump; // Чи можна бампати.
	protected final boolean reply; // Чи можна відписуватися.
	protected final String board; // Ім’я дошки, до якої відноситься тред.
	protected final String title; // Заголовок треду.
	protected final String op; // Текст першого повідомлення треду.
	protected final String author; // Автор треду.
	protected final String ip; // IP-адреса автора треду.
	protected final boolean hidden; // Чи захотів автор приховати ім’я.
	protected final long founded; // Час створення треду.
	protected long lastpost; // Час останнього бампу.
	protected final String username; // Логін користувача, який володіє об’єктом.
	public thread (Connection conn, int id, boolean admin, boolean bump, boolean reply, String username)
	throws SQLException,exceptions.NotFound  {
		super(conn);
		this.id = id;
		this.admin = admin;
		this.bump = bump;
		this.reply = reply;
		this.username = username;
		ResultSet rs = st.executeQuery("SELECT `board`,`title`,`op`,`author`,`ip`,`hidden`,`founded`,`lastpost` FROM threads WHERE id=" + id + "");
		if (!rs.next()) throw new exceptions.NotFound(); // Якщо тред не було знайдено.
		title = rs.getString("title");
		board = rs.getString("board");
		op = rs.getString("op");
		author = rs.getString("author");
		ip = rs.getString("ip");
		hidden = rs.getBoolean("hidden");
		founded = rs.getLong("founded");
		lastpost = rs.getLong("lastpost");
		rs.close();
	}
	public int GetId() { // Отримати ім’я дошки, до якої відноситься тред.
		return id; // Отримати id треду.
	}
	public boolean IsAdmin() { // Перевірити модерує користувач цей тред.
		return admin;
	}
	public boolean IsBump() { // Перевірити чи може користувач бампати цей тред.
		return bump;
	}
	public boolean IsReply() { // Перевірити чи може користувач відписуватись у треді.
		return reply;
	}
	public String GetBoardName() { // Отримати ім’я дошки, до якої відноситься тред.
		return board;
	}
	public String GetTitle() { // Отримати заголовок треду.
		return title;
	}
	public String GetOp() { // Отримати текст першого повідомлення треду.
		return op;
	}
	public String GetAuthor() { // Отримати логін автора треду.
		if (hidden) throw new exceptions.AccessDenied(); // Але тільки якщо він не прихований.
		return author;
	}
	public String GetIp() { // Отримати IP-адресу автора треду.
		if (!admin) throw new exceptions.AccessDenied(); // Але це можуть лише адміни.
		return ip;
	}
	public boolean IsHidden() { // Перевірити чи захотів автор приховати ім’я.
		return hidden;
	}
	public long Founded() { // Дізнатись час створення треду.
		return founded;
	}
	public long LastPost() { // Дізнатись час останнього підняття треду.
		return lastpost;
	}
	public int CountPosts() throws SQLException { // Дізнатись кількість повідомлень (без першого).
		ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM posts WHERE `thread`='"  + id + "'");
		if (!rs.next()) return 0;
		return rs.getInt(1);
	}
	public void post(String ip,boolean hidden,String text,boolean sage)
	throws SQLException,exceptions.AccessDenied {
		if (!reply) throw new exceptions.AccessDenied();
		if (!sage && !bump) throw new exceptions.AccessDenied(); // Якщо користувач не може бампанути, але пробує.
		long time = (long) System.currentTimeMillis()/1000; // Поточний час у кількості секунд від 1970 року.
		String sql = "INSERT INTO posts (`thread`,`time`,`author`,`sage`,`ip`,`hidden`,`text`) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement pst=conn.prepareStatement(sql);
		pst.setInt(1, id);
		pst.setLong(2, time);
		pst.setString(3, username);
		pst.setBoolean(4, sage);
		pst.setString(5, ip);
		pst.setBoolean(6, hidden);
		pst.setString(7, text);
		pst.executeUpdate();
		pst.close();
		if (!sage) { // Якщо потрібно підняти тред.
			sql = "UPDATE `threads` SET `lastpost`=? WHERE id=" + id;
			pst = conn.prepareStatement(sql);
			pst.setLong(1, time);
			pst.executeUpdate();
			pst.close();
		}
	}
	public void DeletePost(long time) // Видалити повідомлення.
	throws SQLException,exceptions.AccessDenied,exceptions.NotFound {
		if (!admin) throw new exceptions.AccessDenied();
		post post = new post(conn,id,time,admin); // Задля перевірки чи повідомлення ще існує.
		st.executeUpdate("DELETE FROM posts WHERE `time`=" + time + " AND thread='" + id + "'");
	}
	public LinkedHashSet<post> GetPosts()  // Отримати список повідомлень з треду.
	throws SQLException,exceptions.NotFound {	
		LinkedHashSet<post> posts = new LinkedHashSet<post>(); // Тут зберігатиметься список повідомлень.
		ResultSet rs = st.executeQuery("SELECT `time` FROM posts WHERE `thread`='" + id + "'");
		while(rs.next()) {
			long time = rs.getLong("time");
			post post = new post(conn,id,time,admin);
			posts.add(post);
		}
		rs.close();
		return posts;
	}
}