package servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import java.sql.*;
// Головний клас сайту.
public class site extends javax.servlet.http.HttpServlet {
	private boolean initialized = false; // Змінна, яка показує чи була виконана початкова ініціалізація.
	private Connection conn; // Тут зберігатиметься з’єднання із базою даних.
	protected void service(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8"); // Якщо цього не зробити, то замість українських літер будуть знаки запитання.
		try {
			if (initialized) {
				db.banip banip = new db.banip(conn,request.getRemoteAddr()); // Створення об’єкту, який перевіряє чи користувача не забанено по IP.
				LinkedHashMap<String,String> query = new LinkedHashMap<String,String>(); // В цьому хеші зберігатимуться параметри, передані користувачем.
				if (request.getMethod().equals("GET")) { // Якщо користувач передав дані методом GET.
					if (request.getQueryString() == null || request.getQueryString().equals("")) { } // Якщо не введено ніяких параметрів.
					else {
						String[] url1 = request.getQueryString().split("&"); // Робиття рядку запиту на частини.
						for (String s: url1) {
							String[] url2 = s.split("=", 2);
							if (url2.length == 2) {
								query.put(url2[0], url2[1]); // Нагадування: ключі в Map не дублюються.
							}
							else { // Якщо елемент не вдалося розбити, то зупинка обробки даного запиту.
								response.sendError(400,"Bad URL."); // З видачею помилки.
								return;
							}
						}
					}
				}
				else if (request.getMethod().equals("POST")) { // Якщо користувач передав дані методом GET.
					Enumeration<String> enu = request.getParameterNames(); // Тут лише ключі.
					while (enu.hasMoreElements()) {
						String key = enu.nextElement(); // Ключ.
						String value = new String(request.getParameter(key).getBytes("ISO8859-1"), "UTF-8"); // Значення.
						query.put(key, value);
					}
				}
				else { // Якщо якийсь інший метод, то невідомо що з ним робити.
					response.sendError(405,"Метод не підтримується.");
					return;
				}
				LinkedHashMap<String,Cookie> cookies = new LinkedHashMap<String,Cookie>(); // Коржики!
				{
					Cookie[] cookies1 = request.getCookies();
					for (Cookie cookie: cookies1) {
						cookies.put(cookie.getName(), cookie);
					}
				}
				db.user user = new db.user(conn); // Створення об’єкту для роботи з даними користувачів.
				if (cookies.containsKey("login")) user.login_with_hash(cookies.get("login").getValue()); // Авторизація користувача по коржику.
				if (query.containsKey("action")) {
					if (query.get("action").equals("exit")) {
						actions.exit.processing(response,new db.config(conn));
					}
					else if (query.get("action").equals("login")) {
						actions.login.processing(request, response, query, user, new db.config(conn), new db.trylogin(conn));
					}
					else if (query.get("action").equals("changepass")) {
						actions.changepass.processing(response, query, user, new db.config(conn));
					}
					else if (query.get("action").equals("deletethread")) {
						actions.DeleteThread.processing(response, query, new db.board(conn, query.get("board"), user), new db.config(conn));
					}
					else if (query.get("action").equals("createthread")) {
						actions.CreateThread.processing(request, response, query, new db.board(conn, query.get("board"), user), new db.config(conn));
					}
					else if (query.get("action").equals("viewboard")) {
						actions.viewboard.processing(new db.config(conn), new db.board(conn, query.get("board"), user), response);
					}
					else if (query.get("action").equals("viewthread")) {
						actions.ViewThread.processing(new db.config(conn), new db.board(conn, query.get("board"), user), query, response);
					}
					else if (query.get("action").equals("post")) {
						actions.post.processing(request, response, query, new db.board(conn, query.get("board"), user), new db.config(conn));
					}
					else if (query.get("action").equals("deletepost")) {
						actions.DeletePost.processing(response, query, new db.board(conn, query.get("board"), user), new db.config(conn));
					}
					else {
						response.sendError(404,"Сторінку не знайдено (дія не підтримується).");
					}
				}
				else { // Головна сторінка сайту.
					actions.index.processing(response, user, new db.config(conn));
				}
			}
			else { // Якщо ініціалізація ще не була виконана.
				init_v2(response);
			}
		}
		catch (SQLException e) { // Якщо був збій під час роботи з базою даних, то видача помилки.
			PrintWriter out = response.getWriter(); e.printStackTrace(out);
		//	response.sendError(500,"Внутрішня помилка серверу: проблеми у роботі з базою даних.");
		}
		catch (exceptions.AccessDenied e1) {
			response.sendError(403,"Доступ заборонено");
		}
		catch (exceptions.NotFound e2) {
			response.sendError(404,"Сторінку не знайдено.");
		}
	}
	protected synchronized void init_v2(HttpServletResponse response) // "synchronized" тут для того, щоб користувачі раптово не проініціалізували декілька разів цей сервлет.
	throws SQLException, IOException, ServletException { 
		if (initialized) { // Якщо ініціалізація уже виконана, то
			response.getWriter().println("<html><head><meta http-equiv=\"refresh\" content=\"3\"></head></html>"); // перезавантаження сторінки через 3 секунди.
			return;
		}
		String host=null,user=null,password=null,database=null,port=null; // Ту зберігатимуться параметри для підлючення до бази даних.
		try {
			String where = getServletContext().getRealPath("WEB-INF/config.xml"); // Визначення можливого шляху до конфігураційного файлу.
			File file = new File(where);
			SAXReader reader = new SAXReader(); // За допомогою цього читатиметься xml файл.
			Document document = reader.read(file);
			Element root = document.getRootElement(); // Отримання кореневого елементу xml документа.
			host = root.element("host").getText(); // Отримання тексту з елемента "host", який є елементом кореневого елемента.
			user = root.element("user").getText();
			password = root.element("password").getText();
			database = root.element("database").getText();
			port = root.element("port").getText();
		}
		catch (Exception e) {
			response.sendError(500,"Внутрішня помилка сервера: проблеми з читанням конфігураційного файлу config.xml.");
			return;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception e) {
			response.sendError(500,"Внутрішня помилка сервера: MySQL драйвер не був знайдений.");
			return;
		}
		conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
		initialized = true; // Якщо метод дойшов до даного етапу, то це означає, що ініціалізація пройшла успішно.
		response.getWriter().println("<html><head><meta http-equiv=\"refresh\" content=\"1\"></head></html>"); // Якщо все пройшло успішно, то перезавантаження сторінки через 1 секунду.
	}
	public void destroy() { // Виконується при завершенні роботи сервлета.
		try {
			conn.close(); // Закриття з’єднання із базою даних.
		}
		catch (SQLException e) { } // Ігнорування виключення. Воно вже не має значення.
	}
}