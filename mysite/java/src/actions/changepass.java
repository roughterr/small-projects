package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.*;
// Якщо користувач захотів змінити свій пароль.
public class changepass {
	public static void processing (HttpServletResponse response, Map<String,String> query,
	db.user user,db.config siteconfig) throws SQLException,IOException {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		if (user.getlogin().equals("guest")) { // Якщо користувач не авторизований, то 
			response.sendError(401,"Неавторизований"); // про яку зміну пароля може
			return; // йти мова?
		}
		long minpass = 1; // Мінімальна довжина паролю за замовчуванням.
		if (siteconfig.DoesExitLong("minpass")) minpass = siteconfig.GetLong("minpass");
		long maxpass = 64; // Максимальна довжина паролю за замовчуванням.
		if (siteconfig.DoesExitLong("maxpass")) maxpass = siteconfig.GetLong("maxpass");
		long changepass = 5; // Мінімальний час між змінами паролю.
		if (siteconfig.DoesExitLong("changepass")) maxpass = siteconfig.GetLong("changepass");
		String error = null;
		long time = System.currentTimeMillis()/1000; // Поточний час.
		if (!query.containsKey("newpass1") || !query.containsKey("newpass2") || !query.containsKey("oldpass")) error = ""; // Якщо користувач не заповнив поля, то повідомлення про помилку можнай й не видавати.
		else if (!query.get("newpass1").equals(query.get("newpass2"))) error = "Введені паролі не співпадають";
		else if (query.get("newpass1").length()  < minpass) error = "Пароль має бути не менше " + minpass + " символів.";
		else if (query.get("newpass1").length()  > maxpass) error = "Пароль має бути не більше " + maxpass + " символів.";
		else if (!java.util.regex.Pattern.matches("[a-z0-9A-Z]+",query.get("newpass1"))) error = "Пароль має складатися лише із символів латинського алфавіту і цифр.";
		else if (time - user.GetLastChangePass() < siteconfig.GetLong("changepass")) {
			out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Зміна паролю</title></head><body><h3>Пароль можна змінювати не частіше, ніж раз в " + changepass + " секунд. Спробуйте пізніше.</h3><h3><a href='javascript:history.go(-1)'>Повернутись на попередню сторінку.</a></h3></body></html>");
			return;
		}
		else if (!user.login_with_password(user.getlogin(),query.get("oldpass"))) error = "Введений старий пароль невірний.";
		else { // Якщо все вірно.
			if (user.changepass(query.get("newpass1"))) { // Якщо вдалось змінити пароль.
				response.addCookie(new Cookie("login",user.get_string4login())); // Видача нового коржика користувачу.
				out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Зміна паролю</title></head><body><h3>Пароль успішно змінено.</h3><h3><a href=\"./\">Перейти на головну.</a></h3><h3><a href='javascript:history.go(-1)'>Повернутись на попередню сторінку.</a></h3></body></html>");
				return;
			}
			else error = "Гість не може змінювати пароль.";
		} // Якщо програма дойшла до наступного рядочка, то це означає, що пароль не вдалось змінити
		out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + " / Зміна паролю</title></head><body><h3>"
		+ error + "</h3>" +
		"<form method=\"post\" action=\"./\">" +
		"<h3>Для зміни паролю введіть свої:</h3>" +
		"<p>Поточний пароль:</p><input type=\"password\" name =\"oldpass\">" +
		"<p>Новий пароль:</p><input type=\"password\" name =\"newpass1\">" +
		"<p>Новий пароль (повторіть):</p><input type=\"password\" name =\"newpass2\">" +
		"<input name=\"action\" value=\"changepass\" type=\"hidden\"><br /><br />" +
		"<br /><input type=\"submit\" value=\"ОК\"></form>" +
		"</body></html>");
	}
}