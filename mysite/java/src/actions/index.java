package actions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
// Головна сторінка сайту.
public class index {
	public static void processing (HttpServletResponse response, db.user user, db.config siteconfig)
	throws SQLException,IOException {
		PrintWriter out = response.getWriter(); // Для виводу тексту.
		out.println("<html><head><title>" + siteconfig.GetString("nazvasaitu") + "</title></head><body>" +
		"<h3>Привіт, " + user.getlogin() + "!</h3><h3><a href=\"./?action=viewboard&board=b\">Перейти до /b</a></h3>");
		out.println("<br />");
		if (user.getlogin().equals("guest")) out.println("<a href=\"./?action=login\">Увійти</a>");
		else {
			out.println("<a href=\"./?action=exit\">Вийти</a><br /><br />");
			out.println("<a href=\"./?action=changepass\">Змінити пароль</a>");
		}
		out.println("</body></html>");
	}
}