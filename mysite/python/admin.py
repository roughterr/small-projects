# admin.py — скрипт, за допомогою якого адміністратор може налаштувати сайт.
# managecate.py — скрипт для впорядковування категорій адміністратором.
if not ugroup == "admin": # Якщо користувач не має прав адміністратора,
	error('403') # то немає йому чого тут робити.
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn" style="{ text-align:center; }">' # Початок основного блоку сторінки.
page['title'] = 'Налаштування сайту'
if 'parametr' in query: # Якщо адміністратор хоче змінити якийсь параметр.
	if not 'znachen' in query:
		page['body'] += '<p>Порожнє значення параметру є неприйнятним.</p>'
	elif len(query['parametr']) > 16:
		page['body'] += 'Назва параметру надто довга. Максимум 16 символів.'
	elif len(query['znachen']) > 128:
		page['body'] += 'Значення параметру надто довге. Максимум 128 символів.'
	else:
		sql = "select parametr,znachen from config where parametr =" + sqladopt(query['parametr']) + ";"
		cursor.execute(sql)
		data = cursor.fetchone()
		try:
			data[0]
		except: # Якщо параметр не знайдено.
			error('404')
		if not query['znachen'] == data[1]: # Якщо значення параметру потрібно змінити.
			sql = "update config set znachen=" + sqladopt(query['znachen']) + " where parametr=" + sqladopt(query['parametr']) + ";"
			cursor.execute(sql)
			page['body'] += '<p> Значення параметру ' + query['parametr'] + ' успішно змінено.</p>'
sql = 'select parametr,znachen from config;'
cursor.execute(sql)
data = cursor.fetchall()
page['body'] += '<table border="2" style="color:gray;text-align:center;">' # Початок таблиці.
page['body'] += '<tr><td>Параметр</td><td>Значення</td></tr>' # Шапка таблиці.
for row in data:
	page['body'] += '<tr>'
	page['body'] += '<p><form method="post" action="index.py"><input name="action" value="admin" type="hidden">\n'
	page['body'] += '<input name="parametr" value="' + row[0] + '" type="hidden">\n'
	page['body'] += '<td>'
	page['body'] += row[0] # Назва параметру.
	page['body'] += '</td>'
	page['body'] += '<td>'
	page['body'] += '<input name="znachen" size="64" value="' + row[1] + '">\n' # row[1] — значення параметру.
	page['body'] += '</td>'
	page['body'] += '<td>'
	page['body'] += '<input type="submit" value="ОК"></form>\n'
	page['body'] += '</td>'
	page['body'] += '</tr>'
page['body'] += '</table>' # Кінець таблиці.
include('sidebar.py') # Бокова панель сторінки.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.