package actions;
import java.io.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
// Для авторизації користувачів.
public class login {
	public static void processing (HttpServletRequest request,
	HttpServletResponse response,
	Map<String,String> query,
	db.user user,
	db.config siteconfig,
	db.trylogin trylogin)
	throws SQLException,IOException {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		if (!user.getlogin().equals("guest")) { // Якщо користувач уже залогінений.
			out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + "</title></head><body><h3>" + user.getlogin() + ", ви не можете увійти, бо уже залогінені.</h3><h3><a href=\"./?action=exit\">Вийти.</a></h3><h3><a href='javascript:history.go(-1)'>Повернутись на попередню сторінку.</a></h3></body></html>");
			return;
		}
		String error = null; // Повідомляє, що користувачу про помилку.
		if(query.containsKey("login") && query.containsKey("password")) { // Якщо повністю введені дані для авторизації.
			if (trylogin.minute(request.getRemoteAddr(), siteconfig)) { // Якщо не перевищено кількість спроб авторизації.
				if (user.login_with_password(query.get("login"),query.get("password"))) { // Якщо дані, уведені користувачем, правильні.
					Cookie c = new Cookie("login", user.get_string4login()); // Коржик, який треба буде видати.
					if (query.containsKey("remember") && query.get("remember").equals("yes")) { // Якщо користувач захотів коржик із довгим терміном зберігання,
						c.setMaxAge(10000000); // дати коржика з терміном дії 115,7 днів.
					}
					response.addCookie(c); // Видача коржика.
					out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Вхід</title></head><body><h3>" + user.getlogin() + ", ви успішно увійшли.</h3><h3><a href=\"./\">Перейти на головну.</a></h3></body></html>");
					return;
				}
				else  {
					error = "Логін або пароль невірні. Спробуйте ще раз.";
				}
			}
			else { // Якщо перевищено кількість спроб авторизації за хвилину.
				error = "Перевищено максимальну кількість спроб авторизації за хвилину. Спробуйте пізніше.";
			}
		}
		String login = ""; // Параметри для форми по замовчуванню.
		String password = "";
		String remember = "";
		if (query.containsKey("login")) login = query.get("login"); // Для видачі користувачу у форму даних, які він увів перед цим.
		if (query.containsKey("password")) password = query.get("password");
		if (query.containsKey("remember")) remember = "checked";
		out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Вхід</title></head><body>");
		if (error != null) { // Якщо потрібно вивести повідомлення про помилку.
			out.println("<h3>" + error + "</h3>\n");
		}
		out.println("<form method=\"post\" action=\"./\">" + // Форма для авторизації.
		"<h3>Для того, щоб увійти на сайт введіть свої:</h3>" +
		"<p>Логін:</p><input name =\"login\" value=\"" + login + "\">" +
		"<p>Пароль:</p><input type=\"password\" name =\"password\" value=\"" + password + "\">" +
		"<input name=\"action\" value=\"login\" type=\"hidden\">" +
		"<br /><br />" +
		"Запам’ятати<input type=\"checkbox\" name=\"remember\" value=\"yes\" " + remember + "><br />" +
		"<br /><input type=\"submit\" value=\"ОК\"></form>");					
		out.println("</body></html>"); // Кінець HTML сторінки.
	}
}