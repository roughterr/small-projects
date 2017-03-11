# delete.py — за допомогою цього скрипта адмін може видалити повідомлення.
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn">' # Початок основного блоку сторінки.
if ugroup == "admin": # Якщо користувач має право адміністратора, то
	page['title'] = "Видалити повідомлення" # Заголовок сторінки.
	if not 'time' in query: # Якщо не вистачає параметру 'time', що його мав вказати користувач.
		page['body'] += "Помилковий ввід. Не уточнено, що треба видалити."
		pageout() # Виведення HTML сторінки.
	try: # Спробувати перетворити заданий час у числовий формат.
		query['time'] = int(query['time'])
	except: # Якщо не вийшло, то
		page['body'] += "Помилковий ввід. Час заданий у неправильному форматі." # вивести повідомлення про помилку.
		pageout() # Виведення HTML сторінки.
	sql = "select zagolovok from posts where time = '" + str(query['time']) + "';"
	cursor.execute(sql) # Виконання запиту.
	data = cursor.fetchone() # Скачування результатів SQL запиту.
	try: # Спробувати
		povidom = data[0] # взяти заголовок повідомлення, що треба видалити.
	except: # Якщо заголовоку повідомлення немає, то й немає самого повідомлення,
		page['body'] += "Помилка. Повідомлення не знайдено." # що треба видалити.
		pageout() # Виведення HTML сторінки.
	if 'certain' in query: # Якщо користувач впевнений, то
		sql = "delete from posts where time = '" + str(query['time']) + "';" # Запит на видалення повідомлення.
		cursor.execute(sql) # Виконання запиту.
		page['body'] += 'Повідомлення "' + povidom + '" успішно видалено.'
		page['title'] = "Повідомлення видалено" # Заголовок сторінки.
	else: # Якщо користувач не вневнений, то спитати його:
		page['body'] += 'Впевнені, що хочете видалити повідомлення "' + povidom +'"?<p align="center"><a href="index.py?action=delete&time=' + str(query['time']) + '&certain=yes">Так.</a></p>'
	del povidom # Якби змінна не була створена, то скрипт перестав би виконуватися раніше.
else: # Якщо користувач немає прав адміністратора, то
	page['body'] += "У вас немає прав на цю дію." # "Под звуком пи".
	page['title'] = "403" # Заголовок сторінки.
include('sidebar.py') # Бокова панель сторінки.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.