# managecate.py — скрипт для впорядковування категорій адміністратором.
if not ugroup == "admin": # Якщо користувач не має прав адміністратора,
	error('403') # то немає йому чого тут робити.
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn" style="{ text-align:center; }">' # Початок основного блоку сторінки.
if 'cate' in query: # Якщо користувач вибрав категорію, яку хоче перемістити.
	if query['cate'] == "": # Якщо значення параметру із назвою категорії порожнє,
		error('400') # то вивести повідомлення про помилковий ввід.
	if re.search(r'[^.a-z|0-9|-]',query['cate']): # Якщо назва категорії, введена користувачем, містить недопустимі символи,
		error('400') # то вивести повідомлення про помилковий ввід.
	if len(query['cate']) > 64: # Якщо назва категорії, введена користувачем, містить більше 64-х символів,
		error('400') # то вивести повідомлення про помилковий ввід.
	sql = "select name,position from cate where name =" + sqladopt(query['cate']) + ";" # Перевірити, чи категорія існує.
	cursor.execute(sql)
	data = cursor.fetchone()
	try:
		data[0]
	except:
		error ('404') # Якщо не існує, то вивести помилку 404.
	page['title'] = "Перемістити категорію " + query['cate'] # Заголовок повідомлення.
	if 'position' in query: # Якщо адміністратор вказав, на яку позицію перемістити повідомлення.
		try: # Перевірити, чи позиція вказана у числовому форматі.
			query['position'] = int(query['position'])
		except:
			page['body'] += '<p>Помилка. Позиція категорії, введена вами, введена у неправильному форматі.</p>'
			page['body'] += '<a href="index.py?action=managecate&cate=' + query['cate'] + '">Спробувати знову</a>.'
		else:
			if query['position'] > 99:
				page['body'] += '<p>Помилка. Позиція категорії, введена вами, не повинна перевищувати 99.</p>'
				page['body'] += '<a href="index.py?action=managecate&cate=' + query['cate'] + '">Спробувати знову</a>.'
			elif query['position'] == data[1]: # Якщо категорії і так знаходиться на заданій позиції.
				page['body'] += '<p>Помилка. Категорія ' + query['cate'] + ' уже і так знаходиться на вибраній позиції.</p>'
				page['body'] += '<a href="index.py?action=managecate&cate=' + query['cate'] + '">Спробувати знову</a>.'
			else:
				sql = "update cate set position='" + str(query['position']) + "' where name=" + sqladopt(query['cate']) + ";"
				cursor.execute(sql) # Переміщення категорії.
				page['body'] += '<p>Категорію <a href="index.py?action=catelist&cate=' + query['cate'] + '">' + query['cate'] + '</a> успішно переміщено.</p><p><a href="index.py?action=managecate">Впорядкувати інші категорії</a>.</p>'
	else: # Якщо користувач ще не вибрав позицію для категорії, то дати йому форму для вибору.
		page['body'] += '<p>Зараз категорія <a href=index.py?action=catelist&cate=' + query['cate'] + '">' + query['cate'] + '</a> знаходиться на позиції ' + str(data[1]) + ' у списку. Якщо бажаєте змінити позицію, то введіть її у форму, у форматі цілого числа від 0 до 99:</p>'
		page['body'] += '<form method="post" action="index.py"><input name="action" value="managecate" type="hidden">'
		page['body'] += '<input name="cate" value="' + query['cate'] + '" type="hidden">'
		page['body'] += '<input name="position" size="2">&#09;<input type="submit" value="ОК"></form>'
else:
	page['title'] = "Впорядкувати категорії" # Заголовок повідомлення.
	page['body'] += '<p aling="center">Впорядкувати категорії:</p>'
	sql = "select name,position from cate order by position;" # Вибірка списку категорій і їх позицій.
	cursor.execute(sql)
	data = cursor.fetchall()
	page['body'] += '<table border="1" style="color:gray;text-align:center;">' # Початок таблиці.
	page['body'] += '<tr><td>Категорія</td><td>її поточна позиція</td></tr>' # Шапка таблиці.
	for row in data: # Порядкове зчитування результату запиту.
		page['body'] += '<tr>'
		page['body'] += '<td><a href="index.py?action=managecate&cate=' + row[0] + '">' + row[0] + '</a></td>'
		page['body'] += '<td>' + str(row[1]) + '</td>'
		page['body'] += '</tr>'
	page['body'] += '</table>' # Кінець таблиці.
include('sidebar.py') # Бокова панель сторінки.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.