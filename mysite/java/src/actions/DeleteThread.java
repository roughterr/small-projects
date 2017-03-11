package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
// Клас для видалення тредів адмінами.
public class DeleteThread {
	public static void processing (HttpServletResponse response,
	Map<String,String> query,
	db.board board,
	db.config siteconfig)
	throws SQLException,IOException,exceptions.NotFound,exceptions.AccessDenied {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		if (!query.containsKey("id")) { // Якщо не вказано який тред видаляти.
			response.sendError(400,"ID треду не вказано.");
			return;
		}
		board.DeleteThread(query.get("id"));
		out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Тред видалено</title></head><body>"
		+ "<h3>Тред з дошки \"<a href=\"./?action=viewboard&board=" + board.GetPath() + "\">" + board.GetDescription() + "</a>\" успішно видалено.</h3></body></html>");
	}
}