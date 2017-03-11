package actions;
import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;
// Для виходу користувачів із сайту.
public class exit {
	public static void processing (HttpServletResponse response, db.config siteconfig) 
	throws IOException,SQLException {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		response.addCookie(new Cookie("login", "")); // Знищити коржик користувача.
		out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + "</title></head><body><h3>Ви вилогінилися.</h3><h3><a href=\"./\">Перейти на головну.</a></h3></body></html>");
		return;
	}
}