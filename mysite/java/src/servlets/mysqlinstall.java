package servlets;
// Клас для створення конфігурації і заповнення бази даних таблицями. Виконується як сервлет. Не залежить від інших класів даного проекту.
public class mysqlinstall extends javax.servlet.http.HttpServlet {
	protected void service(javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) 
	throws javax.servlet.ServletException, java.io.IOException {
		response.setContentType("text/html; charset=UTF-8"); // Якщо цього не зробити, то замість українських літер будуть знаки запитання.
		java.io.PrintWriter out = response.getWriter(); // Для виводу тексту.
		String where = getServletContext().getRealPath("WEB-INF/config.xml"); // Визначення можливого шляху до цільового файлу.
		out.println("<html><body>"); // Початок HTML-сторінки.
		if (checkfile(where)) { // Якщо файл існує.
			out.println("<h2>Configuration file 'config.xml' has been created.</h2>");
		}
		else {
			String host="localhost"; // Параметри, які мають бути введені.
			String login="root";
			String pass="",dbname="";
			String port = "3306";
			if (request.getMethod().equals("GET")) { // В даній програмі методом GET ніякі парамети не приймаються.
				out.println(outform(host,login,pass,dbname,port));
			}
			else if (request.getMethod().equals("POST")) { 
				if(request.getParameter("host") == null || 
				request.getParameter("login") == null ||
				request.getParameter("pass") == null ||
				request.getParameter("dbname") == null ||
				request.getParameter("port") == null) {
					response.sendError(400,"Bad query.");
				}
				else {
					host = request.getParameter("host");
					login = request.getParameter("login");
					pass = request.getParameter("pass");
					dbname = request.getParameter("dbname");
					port = request.getParameter("port");
					if (host.equals("") || login.equals("") || pass.equals("") || dbname.equals("") || port.equals("")) {
						out.println("<h3>Усі поля мають бути заповнені.</h3>");
						out.println(outform(host,login,pass,dbname,port));
					}
					else {
						boolean error = false; // Змінна, яка означає, що сталася помилка.
						java.sql.Connection conn = null;
						try { // Спроба підключитися до бази даних.
							Class.forName("com.mysql.jdbc.Driver"); 
							conn = java.sql.DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, login, pass);
						}
						catch (java.sql.SQLException e) {
							out.println("<h3>Не вдалося підключитися до бази даних. Спробуйте ще раз.</h3>");
							out.println(outform(host,login,pass,dbname,port));
							error = true;
						}
						catch (ClassNotFoundException e) {
							response.sendError(500,"Internal server error: MySQL driver wasn't found.");
							error = true;
						}
						if(!error) { // Якщо з’єднання із базою даних пройшло успішно.
							org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument(); // Підготовка документу, в який буде записано конфігураційний файл.
							org.dom4j.Element mysqlconfig = document.addElement("mysqlconfig");
							mysqlconfig.addElement("host").addText(host);
							mysqlconfig.addElement("user").addText(login);
							mysqlconfig.addElement("password").addText(pass);
							mysqlconfig.addElement("database").addText(dbname);
							mysqlconfig.addElement("port").addText(port); // Документ підготовлено.
							try {
								java.io.PrintWriter pwriter = new java.io.PrintWriter(new java.io.FileOutputStream(where));
								pwriter.write(document.asXML()); // Запис XML файлу.
								pwriter.close(); // Якщо це не зробити, вміст файлу не збережеться.
								try {
									fill(conn); // Наповнення бази даних таблицями.
									out.println("<h2>Базу даних успішно підлючено і заповнено порожніми таблицями.</h2>");
									out.println("<h2><a href=\"./\">Перейти на головну.</a></h2>");
								}
								catch (java.sql.SQLException e) {
									response.sendError(500,"Internal server error: creating tables failed.");
								}
							}
							catch (java.io.FileNotFoundException e) {
								response.sendError(500,"Internal server error: cannot write to file. Maybe you must correct directory permissions.");
							}
						}
					}
				}
			}
			else { // Якщо дані були передані і не GET і не POST методом, то це щось не те. Явно.
				out.println("Cannot determine the request method.");
			}
		}
		out.println("</body></html>"); // Кінець HTML-сторінки.
	}
	protected static boolean checkfile(String where) { // Метод для перевірки існування файлу.
		try {
			new java.io.FileReader(where);
		}
		catch (java.io.FileNotFoundException e) {
			return false;
		} 
		return true; // Якщо при відкритті файлу не кинуло виключення, то це означає, що файл існує.
	}
	protected static String outform(String host, String login, String pass, String dbname, String port) { // Метод для виведення форми.
		String form = "<form method=\"post\" action=\"mysqlinstall\"><h2>Введіть:</h2>" +
	"\n<p>Хост бази даних MySQL:<input type=\"text\" name=\"host\" size=\"19\" value=\"" + host + "\"></p>" +
	"\n<p>Логін користувача MySQL:<input type=\"text\" name=\"login\" size=\"19\" value=\"" + login + "\"></p>" +
	"\n<p>Пароль користувача MySQL:<input type=\"password\" name=\"pass\" size=\"19\" value=\"" + pass + "\"></p>" +
	"\n<p>Ім’я бази даних:<input type=\"text\" name=\"dbname\" size=\"19\" value=\"" + dbname + "\"></p>" +
	"\n<p>Порт серверу баз даних:<input type=\"text\" name=\"port\" size=\"19\" value=\"" + port + "\"></p>" +
	"\n<p><input type=\"submit\" value=\"Встановити\"></p>";
		return form;
	}
	protected static void fill(java.sql.Connection conn) throws java.sql.SQLException { // Метод для заповнення бази даних таблицями.
		java.sql.Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS banip");
		stmt.executeUpdate("CREATE TABLE banip (`ip` VARCHAR(15), `time` BIGINT, UNIQUE KEY(`ip`))"); // Створення таблиці, в якій зберігатимуться дані про забанених по IP користувачів. В полі "ip" зберігатимуться IP-адреси; "time" — до якого часу забанено користувача, в кількості секунд від 1970 року.
		stmt.executeUpdate("DROP TABLE IF EXISTS users");
		stmt.executeUpdate("CREATE TABLE users (login VARCHAR (32) PRIMARY KEY, pass VARCHAR (32), hash VARCHAR (32), lastchangepass BIGINT)"); // Створення таблиці, в якій зберігатимуться дані про користувачів. У полі "login" — логін користувача, "pass" — пароль користувача, "hash" — хеш паролю, "lastchangepass" — час останньої зміни пароля.
		stmt.executeUpdate("INSERT INTO users (login,pass,hash,lastchangepass) VALUES ('a','a','1af1474c3d4909bdf63724f53f6274aa',1)"); // Внесення у БД запиту про акаунт адміністратора.
		stmt.executeUpdate("DROP TABLE IF EXISTS `groupmap`"); // Тут зберігатимуться групи користувачів.
		stmt.executeUpdate("CREATE TABLE `groupmap` (`user` VARCHAR (32), `group` VARCHAR (32), INDEX(`user`))");
		stmt.executeUpdate("REPLACE INTO `groupmap` (`user`,`group`) VALUES ('a','admin')");
		stmt.executeUpdate("DROP TABLE IF EXISTS configvarchar");
		stmt.executeUpdate("CREATE TABLE configvarchar (parametr VARCHAR (32) PRIMARY KEY, znachen VARCHAR (128))"); // Створення таблиці, в якій зберігатимуться налаштування сайту. У полі "parametr" — назва параметру, "znachen" — його значення.
		stmt.executeUpdate("INSERT INTO configvarchar (parametr,znachen) VALUES ('nazvasaitu','Мій сайт')"); // Внесення у таблицю з налаштуваннями сайту запису про назву сайту.
		stmt.executeUpdate("DROP TABLE IF EXISTS configbigint");
		stmt.executeUpdate("CREATE TABLE configbigint (parametr VARCHAR (16) PRIMARY KEY, znachen BIGINT)");
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('tryloginminute',8)"); // Внесення у таблицю з налаштуваннями сайту запису про максимальну кількість запитів на авторизацію, в хвилину.
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('changepass',10)"); // Внесення у таблицю з налаштуваннями сайту запису про мінімальний час між змінами свого паролю користувачем.
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('minpass',2)"); // Мінімальна довжина паролю.
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('maxpass',64)"); // Максимальна довжина паролю.
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('bumplimit',500)"); // Бампліміт.
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('threadsonpage',20)"); // Максимальна кількість тредів на одній сторінці.
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('lastmsgs',3)"); // Кількість останніх повідомлень з трідів, які відображаються в списку трідів.
		stmt.executeUpdate("INSERT INTO configbigint (parametr,znachen) VALUES ('maxoplength',256)"); // Максимальна довжина першого повідомлення треду.
		stmt.executeUpdate("DROP TABLE IF EXISTS tryloginminute");
		stmt.executeUpdate("CREATE TABLE tryloginminute (ip VARCHAR (15) PRIMARY KEY, lastminute BIGINT, sprob INT (3))"); // Створення таблиці, в якій зберігатимуться кількість невдалих спроб спроб авторизації користувача за хвилину, з IP.  полі "ip" — IP-адреса користувача, "lastminute" — остання хвилина з авторизацією, "sprob" — кількість здійснених спроб за ту хвилину.
		stmt.executeUpdate("DROP TABLE IF EXISTS `boards`"); // Тут зберігатимуться тематичні дошки.
		stmt.executeUpdate("CREATE TABLE boards (`path` VARCHAR (4), INDEX(`path`), " + // "path" — ідентифікатор дошки.
		"`description` VARCHAR (128), " + // "description" — опис дошки.
		"`admingroup` VARCHAR (32)," + // "admingroup" — група користувачів, які можуть робити усе із дошкою — читати, створювати, бампати, видаляти як свої, так і чужі повідомлення і треди, переглядати IP.
		"`readgroup` VARCHAR (32)," + // "readgroup" — група користувачів, які можуть лише читати.
		"`members` VARCHAR (32)," + // "members" — група користувачів, які можуть читати, писати, бампати.
		"`guests` VARCHAR (32))"); // "guests" — група користувачів, які можуть читати, відписуватись в тредах, але не можуть їх бампати і створювати нові.
		stmt.executeUpdate("INSERT INTO boards (`path`,`description`,`admingroup`,`readgroup`,`members`,`guests`) VALUES ('b','Безлад','admin','guest','guest','guest')");
		stmt.executeUpdate("DROP TABLE IF EXISTS `threads`");
		stmt.executeUpdate("CREATE TABLE threads (board VARCHAR (4), INDEX(`board`)," + // "board" — дошка, до якої відноситься тред.
		"id INT AUTO_INCREMENT PRIMARY KEY," + // "id" — унікальний номер кожного треду.
		"`lastpost` BIGINT, INDEX(`lastpost`)," + // "lastpost" — час останнього повідомлення.
		"`founded` BIGINT," + // "founded" — час створення треду.
		"title VARCHAR(128)," + // "title" — заголовок треду.
		"op TEXT," + // "op" — перше повідомлення треду.
		"ip VARCHAR(15)," + // "ip" — IP-адреса автора треду.
		"author VARCHAR (32)," + // "author" — автор треду.
		"hidden BOOLEAN)"); // "hidden" — чи приховувати ім’я автора треду.
		stmt.executeUpdate("INSERT INTO threads (`board`,`id`,`lastpost`,`title`,`op`,`author`,`ip`,`hidden`,`founded`) VALUES ('b',1,1,'Заголовок першого тріду','Перше повідомлення першого треду','author','111.111.111.111',FALSE,1)");
		stmt.executeUpdate("DROP TABLE IF EXISTS `posts`"); // Таблиця для зберігання повідомлень.
		stmt.executeUpdate("CREATE TABLE `posts` (`thread` INT, INDEX(`thread`)," // "thread" — ід треду, до якого відноситься повідомлення.
		+ "`time` BIGINT, INDEX(`thread`,`time`), UNIQUE KEY(`time`)," // "time" — час створення повідомлення.
		+ "`author` VARCHAR (32)," // "author" — автор повідомлення.
		+ "`sage` BOOLEAN,"
		+ "`text` TEXT," // "text" — текст повідомлення.
		+ "`ip` VARCHAR(15)," // "ip" — IP-адреса автора повідомлення.
		+ "`hidden` BOOLEAN)"); // "hidden" — чи приховувати ім’я автора повідомлення.
		stmt.close();
	}
}