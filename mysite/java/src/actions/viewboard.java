package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedHashSet;
// Клас для перегляду вмісту певної тематичної дошки.
public class viewboard {
	public static void processing (db.config siteconfig, 
	db.board board,
	HttpServletResponse response) 
	throws SQLException, IOException {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		out.println("<html><head><style>table { white-space: pre-wrap; }</style><title>" + siteconfig.GetString("nazvasaitu") + " / " + board.GetDescription() + "</title></head><body>");
		out.println("<a href=\"./\">Повернутись на головну</a>");
		if (board.IsBump()) out.println(" || <a href=\"./?action=createthread&board=" + board.GetPath() + "\">Створити новий тред</a>");
		out.println("<hr>");
		LinkedHashSet<db.thread> threads = board.GetThreads(); // Список трідів цієї бірди.
		Iterator<db.thread> it = threads.iterator();
		out.println("<table border=\"1\" bordercolor=\"blue\">");
		Formatter fmt; // Для виводу часу у зрозумілому вигляді.
		long TimeInMillis; // Тут зберігатимется часу у форматі мікросекунд.
		while(it.hasNext()) {
			db.thread thread = it.next();
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
			out.println("<tr><td>Час останнього підняття: " + fmt + "</td></tr>");
			out.println("<tr><td>Кількість дописів: " + thread.CountPosts() + "</td></tr>");
			out.println("<tr><td><a href=\"./?action=viewthread&board=" + thread.GetBoardName() + "&id=" + thread.GetId() + "\">Відкрити тред</a></td></tr>");
			out.println("</table></td>");
			out.println("<td VALIGN=TOP width=\"80%\">");
			out.println("<table width=\"100%\" border=\"1\" bordercolor=\"blue\"><tr><td>Заголовок треду: <b>" + thread.GetTitle() + "</b></td></tr>"); // Заголовок треду.
			out.println("<tr><td>Перше повідомлення треду:<br />" + thread.GetOp() + "</td></tr>");
			out.println("</table>");
			out.println("</td>");
			out.println("</tr>");
		}
		out.println("</table><body></html>");
	}
}