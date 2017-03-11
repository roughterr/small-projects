# addpost.py — скрипт, який допомагає користувачу публікувати повідомлення.
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn" style="{ text-align: center; }">' # Початок основного блоку сторінки.
page['title'] = "Опублікувати повідомлення" # Заголовок повідомлення.
if not ugroup == "admin": # Якщо користувач немає прав адміністратора, то це означає, що
	page['body'] += 'У вас немає прав на здійсення даної операції.</div>'
	pageout() # немає йому чого тут робити.
formu = "ni" # Ця змінна означатиме вивести користувачу форму для входу чи ні.
if not 'time' in query: # Якщо користувач не захотів автоматичного визначення часу.
	query['time'] = "manual" # то про це скаже змінна.
if formu == "ni": # Якщо є вірогідність, що користувач заповнив усі поля правильно.
	if query['time'] == "auto": # Якщо користувач захотів, щоб час був виставлений автоматично (поточний).
		chas = str(int(time.time())) # Беремо поточний час.
	else: # Якщо користувач захотів, щоб час був виставлений ним вручну.
		if not 'year' in query and not 'month' in query and not 'day' in query and not 'hour' in query and not 'minute' in query and not 'second' in query and not 'zagolovok' in query and not 'text' in query: # Якщо нічого не введено, то, скоріш за все, користувач тільки-що отримав форму і ще не вводив в неї нічого. Тому повідомлення про помилку зараз виводити не варто.
			formu = 'tak' # Змінна, яка означає, що потрібно видати форму.
		elif not 'year' in query: # Якщо користувач не вказав рік.
			formu = 'tak' # Значення 'tak' змінної 'formu' означатиме, що ще успіху немає і вивести форму для вводу.
			page['body'] += 'Помилка. Ви не вказали рік.'
		elif not 'month' in query: # Якщо користувач не вказав місяць.
			formu = 'tak'
			page['body'] += 'Помилка. Ви не вказали місяць.'
		elif not 'day' in query: # Якщо користувач не вказав день.
			formu = 'tak'
			page['body'] += 'Помилка. Ви не вказали день.'
		elif not 'hour' in query: # Якщо користувач не вказав години.
			formu = 'tak'
			page['body'] += 'Помилка. Ви не вказали години.'
		elif not 'minute' in query: # Якщо користувач не вказав хвилини.
			formu = 'tak'
			page['body'] += 'Помилка. Ви не вказали хвилини.'
		elif not 'second' in query: # Якщо користувач не вказав секунди.
			formu = 'tak'
			page['body'] += 'Помилка. Ви не вказали секунди.'
		else:
			try: # Спробувати перетворити рік, введений користувачем у числовий формат.
				query['year'] = int(query['year'])
			except: # Якщо не вийшло, то це неприйнятно.
				formu = 'tak'
		if formu == "ni": # Якщо поки-що все правильно, то йдемо далі.
			if query['year'] < 1970: # Так як час зберігається у форматі кількості секунд від 1970 року, то
				formu = 'tak' # заборонено вказувати час повідомлення до даної дати.
				page['body'] += 'Помилка. Заборонено до 1970 року.'
			if query['year'] > 9000: # До 9000 року ще надто довго.
				formu = 'tak'
				page['body'] += 'Помилка. Заборонено після 9000 року.'
			query['year'] = str(query['year']) # Обернення змінної назад у рядковий тип.
		if formu == "ni":
			try:
				query['month'] = int(query['month'])
			except:
				formu = 'tak'
				page['body'] += 'Помилка. Не заповнено чи неправильно заповнено поле "місяць".'
		if formu == "ni":
			if not 0 < query['month'] < 13:
				formu = 'tak'
				page['body'] += 'Помилка. Місяців 12 у році.'
			query['month'] = str(query['month']) # Обернення змінної назад у рядковий тип.
		if formu == "ni":
			try:
				query['day'] = int(query['day'])
			except:
				formu = 'tak'
				page['body'] += 'Помилка. Не заповнено чи неправильно заповнено поле "день".'
		if formu == "ni":
			if not 0 < query['day'] < 32:
				formu = 'tak'
				page['body'] += 'Помилка. Неправильна дата.'
			query['day'] = str(query['day']) # Обернення змінної назад у рядковий тип.
		if formu == "ni":
			try:
				query['hour'] = int(query['hour'])
			except:
				formu = 'tak'
				page['body'] += 'Помилка. Не заповнено чи неправильно заповнено поле "години".'
		if formu == "ni":
			if not query['hour'] < 25:
				formu = 'tak'
				page['body'] += 'Помилка. У добі 24 години.'
			query['hour'] = str(query['hour']) # Обернення змінної назад у рядковий тип.
		if formu == "ni":
			try:
				query['minute'] = int(query['minute'])
			except:
				formu = 'tak'
				page['body'] += 'Помилка. Не заповнено чи неправильно заповнено поле "хвилини".'
		if formu == "ni":
			if not query['minute'] < 60:
				formu = 'tak'
				page['body'] += 'Помилка. У годині 60 хвилин.'
			query['minute'] = str(query['minute']) # Обернення змінної назад у рядковий тип.
		if formu == "ni":
			try:
				query['second'] = int(query['second'])
			except:
				formu = 'tak'
				page['body'] += 'Помилка. Не заповнено чи неправильно заповнено поле "секунди".'
		if formu == "ni":
			if not query['second'] < 60:
				formu = 'tak'
				page['body'] += 'Помилка. У хвилині 60 секунд.'
			query['second'] = str(query['second']) # Обернення змінної назад у рядковий тип.
		if formu == "ni":
			try:
				execcode = "chas = str(int(time.mktime(time.strptime('" + query['year'] + '-' + query['month'] + '-' + query['day'] + '-' + query['hour'] + '-' + query['minute'] + '-' + query['second'] + "', '%Y-%m-%d-%H-%M-%S'))))"
				exec(execcode)
				del execcode
			except:
				formu = 'tak'
				page['body'] += 'Помилка. Неправильна дата.'
if formu == "ni":
	if not 'text' in query: # Якщо користувач не ввів текст повідомлення.
		page['body'] += 'Помилка. Ви не ввели текст повідомлення.'
		formu = 'tak'
	elif query['text'] == "": # Якщо користувач не ввів текст повідомлення невперше.
		formu = 'tak'
		page['body'] += 'Помилка. Ви не ввели текст повідомлення.'
if formu == "ni":
	if not 'zagolovok' in query:
		formu = 'tak'
		page['body'] += 'Помилка. Ви не ввели заголовок повідомлення.'
	elif len(query['zagolovok']) > 64:
		formu = 'tak'
		page['body'] += 'Помилка. Заголовок повідомлення має бути не більше 64-х символів.'
if formu == "ni": # Якщо ще все правильно, то перевірка чи бува уже не існує повідомлення з цією датою.
	sql = "select time from posts where time = '" + chas + "';" 
	cursor.execute(sql) # Виконання SQL запиту.
	data = cursor.fetchone() # Скачування результатів SQL-запиту.
	try:
		a = data[0]
	except:
		del sql # Аби щось було.
	else: # Якщо існує, то запропонувати користувачу вибрати іншу дату.
		page['body'] += 'Помилка. Повідомлення з цією датою уже існує.'
		del a
		formu = 'tak'
if formu == "tak": # Якщо треба вивести форму для публікації повідомлення.
	page['body'] += '<form method="post" action="index.py">' # Початок форми для публікації повідомлення.
	page['body'] += '<h3>Для публікації повідомлення введіть:</h3><p>Заголовок:</p><input size="60" name="zagolovok"' # Поле для вводу заголовка.
	if 'zagolovok' in query:
		page['body'] += ' VALUE="' + query['zagolovok'] + '"'
	page['body'] += '>\n'
	page['body'] += '\t<input name="action" value="addpost" type="hidden">\n'
	page['body'] += '<p>Текст:</p><textarea rows="10" cols="110" name ="text">' # Поле для вводу тексту.
	if 'text' in query:
		page['body'] += query['text']
	page['body'] += '</textarea>\n<br /><br />'	
	page['body'] += '<input type="checkbox" name="time" value="auto"'
	if 'time' in query:
		if query['time'] == 'auto':
			page['body'] += ' checked'
	page['body'] += ' />Автоматично виставити час повідомлення (якщо, то далі поля не заповнювати).<br /><br />'
	page['body'] += 'Рік: <input name="year" size="4"'
	if not 'year' in query:
		query['year'] = time.strftime("%Y", time.localtime())
	page['body'] += ' VALUE="' + query['year'] + '">\n'
	page['body'] += 'Місяць: <input name="month" size="2"'
	if not 'month' in query:
		query['month'] = time.strftime("%m", time.localtime())
	page['body'] += ' VALUE="' + query['month'] + '">\n'
	page['body'] += 'День: <input name="day" size="2"'
	if not 'day' in query:
		query['day'] = time.strftime("%d", time.localtime())
	page['body'] += ' VALUE="' + query['day'] + '">\n'
	page['body'] += 'Години: <input name="hour" size="2"'
	if not 'hour' in query:
		query['hour'] = time.strftime("%H", time.localtime())
	page['body'] += ' VALUE="' + query['hour'] + '">\n'
	page['body'] += 'Хвилини: <input name="minute" size="2"'
	if not 'minute' in query:
		query['minute'] = time.strftime("%M", time.localtime())
	page['body'] += ' VALUE="' + query['minute'] + '">\n'
	page['body'] += 'Секунди: <input name="second" size="2"'
	if not 'second' in query:
		query['second'] = time.strftime("%S", time.localtime())
	page['body'] += ' VALUE="' + query['second'] + '">\n<br /><br />'
	page['body'] += '<input type="submit" value="ОК">'
	page['body'] += '</form>' # Кінець форми.
else: # Якщо форму виводити не потрібно бо користувач усе ввів правильно.
	sql = "insert into posts (zagolovok,time,text) values (" + sqladopt(query['zagolovok']) + ",'" + chas + "'," + sqladopt(query['text']) + ");"
	cursor.execute(sql) # Виконання SQL запиту.
	sql = "select time from posts where time = '" + chas + "';"
	cursor.execute(sql) # Виконання SQL запиту.
	data = cursor.fetchone()
	try:
		a = data[0]
	except:
		page['body'] += 'Внутрішня помилка сервера.'
	else:
		page['body'] += '<a href="index.py?action=showpost&time=' + chas + '">Повідомлення</a> успішно додано.'
		del chas,a
del formu # Видалення використаної змінної formu.
include('sidebar.py') # Бокова панель сторінки.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.