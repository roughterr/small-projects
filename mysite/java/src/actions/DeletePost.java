package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
public class DeletePost {
	public static void processing (HttpServletResponse response,
	Map<String,String> query,
	db.board board,
	db.config siteconfig)
	throws SQLException,IOException,exceptions.NotFound,exceptions.AccessDenied {
		if (!query.containsKey("thread")) { // Якщо не вказано з якого треду видаляти.
			response.sendError(400,"ID треду не вказано.");
			return;
		}
		if (!query.containsKey("time")) { // Якщо не вказано з якого треду видаляти.
			response.sendError(400,"Час повідомлення не вказано.");
			return;
		}
		long time; // Час повідомлення.
		int ThreadId;
		try {
			time = Long.decode(query.get("time"));
			ThreadId = Integer.decode(query.get("thread"));
		}
		catch (NumberFormatException e) {
			throw new exceptions.NotFound();
		}
		db.thread thread = board.GetThread(ThreadId);
		thread.DeletePost(time);
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Повідомлення видалено</title></head><body>"
		+ "<h3>Повідомлення з треду \"<a href=\"./?action=viewthread&board=" + board.GetPath() + "&id=" + thread.GetId() + "\">" + thread.GetTitle() + "</a>\" успішно видалено.</h3></body></html>");
	}
}
