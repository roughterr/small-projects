package db;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
// Клас для зберігання повідомлень.
public class post extends alpha {
	protected final String author;
	protected final boolean sage;
	protected final String ip;
	protected final boolean hidden;
	protected final String text;
	protected final boolean admin; // Для перевірки чи можна показувати IP.
	protected final long time;
	public post (Connection conn, int thread, long time, boolean admin) 
	throws SQLException,exceptions.NotFound {
		super (conn);
		ResultSet rs = st.executeQuery("SELECT `author`,`sage`,`ip`,`hidden`,`text`,`time` " +
		"FROM `posts` WHERE `thread`='" + thread + "' AND `time`=" + time);
		if (!rs.next()) throw new exceptions.NotFound();
		this.author = rs.getString("author");
		this.sage = rs.getBoolean("sage");
		this.ip = rs.getString("ip");
		this.hidden = rs.getBoolean("hidden");
		this.text = rs.getString("text");
		this.admin = admin;
		this.time = rs.getLong("time");
	}
	public String GetAuthor() throws exceptions.AccessDenied {
		if (hidden) throw new exceptions.AccessDenied();
		return author;
	}
	public boolean IsSage() {
		return sage;
	}
	public String GetIp() {
		if (!admin) throw new exceptions.AccessDenied();
		return ip;
	}
	public boolean IsHidden() {
		return hidden;
	}
	public String GetText() {
		return text;
	}
	public boolean IsAdmin() { // Для того, щоб знайти чи можна дізнатись IP.
		return admin;
	}
	public long GetTime() {
		return time;
	}
}
