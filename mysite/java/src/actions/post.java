package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class post {
	public static void processing (HttpServletRequest request,
	HttpServletResponse response,
	Map<String,String> query,
	db.board board,
	db.config siteconfig)
	throws SQLException,IOException,exceptions.NotFound,exceptions.AccessDenied {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		if (!query.containsKey("thread")) { // Якщо не вказано тред, в який потрібно додати повідомлення.
			throw new exceptions.NotFound();
		}
		int ThreadId; // id треду.
		try { 
			ThreadId = Integer.valueOf(query.get("thread"));
		}
		catch (Exception e) { // Якщо id треду вказано у нечисловому форматі.
			throw new exceptions.AccessDenied();
		}
		db.thread thread = board.GetThread(ThreadId); // Отримання треду.
		if (!thread.IsReply()) { // Якщо користувач не має права створювати повідомлення.
			throw new exceptions.AccessDenied();
		}
		String text = ""; // Текст повідомлення.
		boolean hidden = false; // Чи приховати ім’я автора треду (для методу).
		String hidename = ""; // Чи приховати ім’я автора треду (для форми).
		boolean error = false;
		String errormsg = "";
		boolean sage = false;
		String sage4form = "";
		if (!query.containsKey("text")) {
			error = true; // Якщо користувач не заповнив форму.
		}
		else if (query.containsKey("hidename") && !query.get("hidename").equals("true")) {
			error = true;
			errormsg = "Параметр \"hidename\" не може мати такого значення";
		}
		else if (query.containsKey("text") && query.get("text").length() == 0) {
			error = true;
			errormsg = "Ви маєте ввести текст повідомлення.";
		}
		else if (query.containsKey("sage") && !query.get("sage").equals("true")) {
			error = true;
			errormsg = "Параметр \"sage\" не може мати такого значення";
		}
		if (query.containsKey("hiddename") && query.get("hiddename").equals("true")) {
			hidden = true; // Якщо користувач захотів приховати своє ім’я.
			hidename = "checked";
		}
		if (query.containsKey("text")) {
			text = query.get("text");
		}
		if (query.containsKey("sage") && query.get("sage").equals("true")) {
			sage = true;
			sage4form = "checked";
		}
		if (error) {
			out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Додати нове повідомлення</title></head><body>");
			out.println("<h3>" + errormsg + "</h3><form method=\"post\" action=\"./\">");
			out.println("<h3>Для того, щоб додати нове повідомлення в тред  \"" + thread.GetTitle() + "\" введіть:</h3>");
			out.println("<p>Текст повідомлення:</p><textarea rows=\"10\" cols=\"110\" name =\"text\">" + text + "</textarea>");
			out.println("<input name=\"action\" value=\"post\" type=\"hidden\">");
			out.println("<input name=\"thread\" value=\"" + thread.GetId() + "\" type=\"hidden\">");
			out.println("<input name=\"board\" value=\"" + board.GetPath() + "\" type=\"hidden\">");
			if (thread.IsBump()) { // Якщо користувач має вибір: піднімати чи не піднімати тред.
				out.println("<br />Не піднімати тред (sage)?<input type=\"checkbox\" name=\"sage\" value=\"true\" " + sage4form + "><br />");
			}
			out.println("Приховати ім’я?<input type=\"checkbox\" name=\"hiddename\" value=\"true\" " + hidename + "><br />");
			out.println("<br /><input type=\"submit\" value=\"ОК\"></form>");
			out.println("</body></html>"); // Кінець HTML сторінки.
		}
		else { // Якщо помилок немає, додати повідомлення.
			if (sage == false && !thread.IsBump()) {
				sage = true;
			}
			{ // Заміна знаків більше і менше на відповідні їм позначення у html.
				CharSequence c1 = "<";
				CharSequence c2 = "&lt;";
				CharSequence c3 = ">";
				CharSequence c4 = "&gt;";
				text = text.replace(c1, c2);
				text = text.replace(c3, c4);
			}
			thread.post(request.getRemoteAddr(), hidden, text, sage);
			out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Повідомлення додано</title></head><body>"
			+ "<h3>Повідомлення в тред \"<a href=\"./?action=viewthread&board=" + board.GetPath() + "&id=" + thread.GetId() + "\">" + thread.GetTitle() + "</a>\" успішно додано.</h3></body></html>");
		}
	}
}
