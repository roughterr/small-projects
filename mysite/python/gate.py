page['body'] += '<div class="osn" style="{ text-align: center; }">' # Початок основного блоку сторінки.
if user == "guest" and query['action'] == "logout": # Якщо користувач захотів вилогінитись, не залогінившись, то
	error('yet') # переадресувати його на головну сторінку сайту.
elif query['action'] == "logout": # Якщо користувач захотів вийти,
	out('Set-Cookie: login=\n') # Обнулити його коржик.
	sql = "update users set hash='no' where login = '" + user + "';" # І видалити з бази даних хеш паролю,
	cursor.execute(sql) # виконавши SQL запит.
	page['body'] += '<h2>Ви успішно вийшли.</h2><h2><a href="./index.py">Перейти на головну.</a></h2>' # Показати користувачу сторінку про те, що він успішно вийшов.
	ugroup = "guest" # І користувач стає гостем.
	pageout() # Вивід сторінки користувачу.
elif not user == "guest" and query['action'] == "login": # Якщо користувач захотів залогінитись, не вилогінившись, то
	error('yet') # переадресувати його на головну сторінку сайту.
page['title'] = "Вхід" # Заголовок сторінки буде такий.
loginform = """<form method="post" action="index.py">
	<h3>Для того, щоб увійти на сайт введіть свої:</h3>
	<p>Логін:</p><input name ="login">
	<p>Пароль:</p><input name ="password" type="password">
	<input name="action" value="login" type="hidden">
	<br /><br />
	<input type="checkbox" name="remember" value="yes" />запам'ятати<br />
	<br />
	<input type="submit" value="ОК">
</form>""" # Форма для входу.
if not 'login' in query: # Якщо у запиті немає логіну, то
	page['body'] += loginform # вивести знову форму для входу.
elif query['login'] == "": # Якщо введений логін порожній, то
	page['body'] += "Ви не ввели логін." + loginform # вивести, знову ж таки, форму для входу.
elif not 'password' in query: # Якщо у запиті немає паролю, то вивести 
	page['body'] += "Неправильний логін або пароль." + loginform # форму з повідомленням про помилку, для входу.
elif re.search(r'[^.a-z|0-9]',query['login']): # Якщо логін, введений користувачем, містить недопустимі символи,
	page['body'] += "Помилка. Логін має складатися лише з символів латинського алфавіту і цифр." + loginform # вивести форму з повідомленням про помилку, для входу.
elif re.search(r'[^.a-z|0-9]',query['password']):	# Якщо пароль, введений користувачем, містить недопустимі символи,
	page['body'] += "Неправильний логін або пароль." + loginform # вивести форму з повідомленням про помилку, для входу.
else: # Інакше.
	cursor.execute("select znachen from config where parametr = 'tryloginminute'") # Взяття із БД параметру tryloginminute, який означає максимальну кількість спроб авторизації в хвилину.
	tryloginminute = cursor.fetchone()
	tryloginminute = int(tryloginminute[0])
	sql = "select lastminute,sprob from tryloginminute where ip = '" + os.environ["REMOTE_ADDR"] + "';" # Вибірка з БД даних про раніше невдалу авторизацію з даного IP.
	cursor.execute(sql) # Виконання SQL-запиту.
	data = cursor.fetchone()
	try: # Спробувати зчитати дві змінні, отримані з БД, у окремі змінні.
		lastminute = int(data[0]) # Час з останньої хвилини, в яку було здійснено невдалу спробу авторизації з даного IP.
		sprob = int(data[1]) # Кількість невдалих спроб авторизації
	except: # Якщо у БД немає інформацію про авторизацію з цього IP.
		lastminute = 0 # Оголосити цю змінну в числовому типі, щоб можна було порівнювати.
	else:  # Якщо у БД є інформація про авторизацію з цього IP.
		if lastminute + 60 > int(time.time()) and sprob > tryloginminute: # Якщо хвилина з часу останньої невдалої авторизації не пройшла і кількість спроб в цю хвилину була перевищена, то
			page['body'] += '<h3>Ви перевищили допустиму кількість невдалих спроб авторизації з цього IP на одну хвилину.</h3><h3>Зачекайте трошки і <a href="index.py?action=login">спробуйте знову</a>.</h3></div>'
			pageout() # видача помилки користувачу і завершення роботи скрипта.
	del tryloginminute # Видалення використаної змінної, в якій зберігалася максимально допустима кількість невдалих спроб авторизації.
	if lastminute + 60 > int(time.time()): # Якщо ще не пройшла хвилина з часу останньої невдалої авторизації.
		yakshchotochas = str(lastminute) # то час в БД залишиться незмінним.
	else: # Якщо хвилина уже пройшла, то
		sprob = 0 # кількість попередніх невдалих спроб не має значення
		yakshchotochas = str(int(time.time())) # і якщо, то заносимо у БД поточний час.
	del lastminute
	sprob += 1 # Змінна буде використана у випадку якщо буде ще одна невдала спроба авторизації, тому заздалегідь збільшення її на одиницю.
	iferror = "replace into tryloginminute (ip,lastminute,sprob) values ('" + os.environ["REMOTE_ADDR"] + "','" + yakshchotochas + "','" + str(sprob) + "');" # Підготовка запиту, який буде виконаний якщо спроба авторизації виявиться невдалою.
	del yakshchotochas,sprob
	sql = "select pass from users where login =" + sqladopt(query['login']) + ";" # Підготовка SQL запиту на видачу паролю.
	cursor.execute(sql) # Виконання SQL-запиту.
	data = cursor.fetchall() # Скачування результатів SQL запиту.
	try: # Перевірити, чи 
		password = data[0][0] # знайдено у базі даних дані про користувача.
	except: # Якщо ні, то
		page['body'] += "Неправильний логін або пароль." + loginform # вивести форму із повідомленням помилку.
		cursor.execute(iferror) # Внесення у БД інформацію про невдалу авторизацію з даного IP.
	else: # Якщо знайдено,
		if query['password'] == password: # Якщо пароль правильний.
			hashofpass = password + str(time.time()) # Підготовка змінної для хешування.
			hashofpass = hashlib.md5(hashofpass.encode('UTF8')).hexdigest() # Хешування змінної.
			sql = "update users set hash='" + hashofpass + "' where login =" + sqladopt(query['login']) + ";" # Підготовка SQL запиту на зміну хешу паролю в таблиці.
			cursor.execute(sql) # Виконання SQL запиту.
			if 'remember' in query: # Якщо користувач захотів, щоб його коржик мав тривалий термін зберігання.
				corzhyk = time.strftime("%Y") # Визначення поточного року.
				corzhyk = int(corzhyk) + 2 # Термін придатності коржика — 2 роки.
				corzhyk = time.strftime("%a, %d-%b-") + str(corzhyk) + time.strftime(" %H:%M:%S GMT;") # Формування повних даних про час для коржика.
				corzhyk = 'Set-Cookie: login=' + query['login'] + "^" + hashofpass + "; expires=" + corzhyk + "\n" # Завершальний етап готування коржика.
			else: # Інакше коржик матиме термін зберігання
				corzhyk = 'Set-Cookie: login=' + query['login'] + "^" + hashofpass + "\n" # до кінця сеансу.
			out(corzhyk) # Видача коржика користувачу.
			del corzhyk,hashofpass # Видалення використаних змінних.
			page['body'] += '<h2>Ви успішно увійшли.</h2><h2><a href="./index.py">Перейти на головну.</a></h2>' # Повідомлення про успішне залогінення.
			page['title'] = "Привіт, " + query['login'] + "!" # Написати в заголовку сторінки: "привіт", користувачу.
		else: # Якщо пароль неправильний, то
			page['body'] += "Неправильний логін або пароль." + loginform # вивести форму із повідомленням помилку.
			cursor.execute(iferror) # Внесення у БД інформацію про невдалу авторизацію з даного IP.
		del password # Видалення використаної змінної.
del loginform # Видалення використаної (а може й ні) змінної.
include('sidebar.py') # Бокова панель.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.