package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import db.post;
public class ViewThread {
	public static void processing (db.config siteconfig, 
	db.board board, Map<String,String> query, HttpServletResponse response) 
	throws exceptions.AccessDenied,exceptions.NotFound, SQLException, IOException {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		if (!query.containsKey("id")) { // Якщо не вказано, який саме тред відкрити.
			throw new exceptions.NotFound();
		}
		int id; // id треду.
		try { 
			id = Integer.valueOf(query.get("id"));
		}
		catch (Exception e) { // Якщо id треду вказано у нечисловому форматі.
			throw new exceptions.AccessDenied();
		}
		db.thread thread = board.GetThread(id); // Отримання треду.
		out.println("<html><head><style>table { white-space: pre-wrap; }</style><title>" + siteconfig.GetString("nazvasaitu") + " / " + thread.GetTitle() + "</title></head><body>");
		out.println("<a href=\"./?action=viewboard&board=" + board.GetPath() + "\">Повернутись до списку тредів на дошці \"" + board.GetDescription() + "\"</a>");
		if (thread.IsReply()) out.println("|| <a href=\"./?action=post&board=" + board.GetPath() + "&thread=" + thread.GetId() + "\">Написати в тред</a>");
		out.println("<hr>");
		LinkedHashSet<post> posts = thread.GetPosts(); // Отримання списку повідомлень.
		Iterator<db.post> it = posts.iterator();
		out.println("<table border=\"1\" bordercolor=\"blue\">");
		Formatter fmt; // Для виводу часу у зрозумілому вигляді.
		long TimeInMillis; // Тут зберігатимется часу у форматі мікросекунд.
		{ // Перше повідомлення треду береться з іншої таблиці в БД.
			out.println("<tr>");
			out.println("<td VALIGN=TOP width=\"20%\"><table width=\"100%\" border=\"1\" bordercolor=\"blue\">");
			if (thread.IsAdmin()) {
				out.println("<tr><td>IP: " + thread.GetIp() + "</td></tr>");
			}
			fmt = new Formatter();
			TimeInMillis = thread.Founded()*1000;
			fmt.format("%tF %tT", TimeInMillis, TimeInMillis);
			out.println("<tr><td>Створено: " + fmt + "</td></tr>");
			if (!thread.IsHidden()) {
				out.println("<tr><td>Автор: " + thread.GetAuthor() + "</td></tr>");
			}
			if (thread.IsAdmin()) {
				out.println("<tr><td><a href=\"./?action=deletethread&board=" + thread.GetBoardName() + "&id=" + thread.GetId() + "\">Видалити тред</a></td></tr>");
			}
			fmt = new Formatter();
			TimeInMillis = thread.LastPost()*1000;
			fmt.format("%tF %tT", TimeInMillis, TimeInMillis);
			out.println("</table></td>");
			out.println("<td VALIGN=TOP width=\"80%\">");
			out.println("<table width=\"100%\" border=\"1\" bordercolor=\"blue\"><tr><td>Заголовок треду: <b>" + thread.GetTitle() + "</b></td></tr>"); // Заголовок треду.
			out.println("<tr><td>" + thread.GetOp() + "</td></tr>");
			out.println("</table>");
			out.println("</td>");
			out.println("</tr>");
		}
		while(it.hasNext()) {
			db.post post = it.next();
			out.println("<tr>");
			out.println("<td VALIGN=TOP width=\"20%\"><table width=\"100%\" border=\"1\" bordercolor=\"blue\">");
			if (thread.IsAdmin()) {
				out.println("<tr><td>IP: " + post.GetIp() + "</td></tr>");
			}
			fmt = new Formatter();
			TimeInMillis = post.GetTime()*1000;
			fmt.format("%tF %tT", TimeInMillis, TimeInMillis);
			out.println("<tr><td>Створено: " + fmt + "</td></tr>");
			if (!post.IsHidden()) {
				out.println("<tr><td>Автор: " + post.GetAuthor() + "</td></tr>");
			}
			if (thread.IsAdmin()) {
				out.println("<tr><td><a href=\"./?action=deletepost&board=" + thread.GetBoardName() + "&thread=" + thread.GetId() + "&time=" + post.GetTime() + "\">Видалити повідомлення.</a></td></tr>");
			}
			out.println("<tr><td>sage: " + post.IsSage() + "</td></tr>");
			out.println("</table></td>");
			out.println("<td VALIGN=TOP width=\"80%\">");
			out.println(post.GetText());
			out.println("</td>");
			out.println("</tr>");
		}
		out.println("</table><body></html>");
	}
}