package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//Клас для створення тредів.
public class CreateThread {
	public static void processing (HttpServletRequest request,
	HttpServletResponse response,
	Map<String,String> query,
	db.board board,
	db.config siteconfig)
	throws SQLException,IOException,exceptions.NotFound,exceptions.AccessDenied {
		if (!siteconfig.DoesExitLong("maxoplength")) { 
			response.sendError(500,"База даних не містить налаштування максимальної довжини першого повідомлення треду");
		}
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		String title = ""; // Заголовок треду.
		if (query.containsKey("title")) { // Якщо користувач заповнив це поле у формі.
			title = query.get("title");
		}
		String op = ""; // Перше повідомлення треду.
		if (query.containsKey("op")) {
			op = query.get("op");
		}
		boolean hidden = false; // Чи приховати ім’я автора треду (для методу).
		String hidename = ""; // Чи приховати ім’я автора треду (для форми).
		boolean error = false;
		String errormsg = "";
		if (!query.containsKey("title") && !query.containsKey("op")) {
			error = true; // Якщо користувач не заповнив форму.
		}
		else if (op.length() > siteconfig.GetLong("maxoplength")) { // Якщо довжина першого повідомлення треду перевищує максимально допустиму.
			error = true;
			errormsg = "Перевищено максимальну довжину повідомлення.";
			op = op.substring(0, (int) siteconfig.GetLong("maxoplength")); // Для недопущення зловживання трафіком.
		}
		else if (query.containsKey("hidename") && !query.get("hidename").equals("true")) {
			error = true;
			errormsg = "Параметр \"hidename\" не може мати такого значення";
		}
		else if (title.length() > 128) {
			title = title.substring(0, 128);
			error = true;
			errormsg = "Заголовок треду має бути не довший 128 символів.";
		}
		else if (query.containsKey("title") && query.get("title").length() == 0) {
			error = true;
			errormsg = "Ви маєте ввести текст заголовку.";
		}
		else if (query.containsKey("op") && query.get("op").length() == 0) {
			error = true;
			errormsg = "Ви маєте ввести текст повідомлення.";
		}
		if (query.containsKey("hidename") && query.get("hidename").equals("true")) {
			hidden = true; // Якщо користувач захотів приховати своє ім’я.
			hidename = "checked";
		}
		if (error) {
			out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Створити тред</title></head><body>"
			+ "<h3>" + errormsg + "</h3><form method=\"post\" action=\"./\">"
			+ "<h3>Для того, щоб створити тред на дошці \"" + board.GetDescription() + "\" введіть:</h3>"
			+ "<p>Заголовок:</p><input name =\"title\" maxlength=\"128\" value=\"" + title + "\">"
			+ "<p>Текст повідомлення (не більше " + siteconfig.GetLong("maxoplength") + " символів):</p><textarea rows=\"10\" cols=\"110\" name =\"op\">" + op + "</textarea>"
			+ "<input name=\"action\" value=\"createthread\" type=\"hidden\">"
			+ "<input name=\"board\" value=\"" + board.GetPath() + "\" type=\"hidden\">"
			+ "<br /><br />"
			+ "Приховати ім’я?<input type=\"checkbox\" name=\"hidename\" value=\"true\" " + hidename + "><br />"
			+ "<br /><input type=\"submit\" value=\"ОК\"></form>");					
			out.println("</body></html>"); // Кінець HTML сторінки.
			
		}
		else { // Якщо помилок немає, створити тред.
			{ // Заміна знаків більше і менше на відповідні їм позначення у html.
				CharSequence c1 = "<";
				CharSequence c2 = "&lt;";
				CharSequence c3 = ">";
				CharSequence c4 = "&gt;";
				op = op.replace(c1, c2);
				op = op.replace(c3, c4);
			}
			board.CreateThread(title, op, request.getRemoteAddr(), hidden);
			out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Тред створено</title></head><body>"
			+ "<h3>Тред в дошці \"<a href=\"./?action=viewboard&board=" + board.GetPath() + "\">" + board.GetDescription() + "</a>\" успішно створено.</h3></body></html>");
		}
	}
}