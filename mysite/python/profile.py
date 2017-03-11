# profile.py — скрипт для регування користувачами своїх особистих даних.
if user == "guest": # Якщо користувач не авторизований, то
	error('403') # у нього немає власного профілю, який він може редагувати.
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn" style="{ text-align: center; }">' # Початок основного блоку сторінки.
if 'newpass' in query: # Якщо користувач захотів зміниити пароль.
	page['title'] = "Зміна паролю"
	if not 'oldpass' in query: # Якщо користувач не ввів поточний пароль.
		formu = 'tak'
	elif not 'newpass2' in query: # Якщо користувач повторно не ввів пароль.
		page['body'] += '<p>Помилка. Ви не ввели новий пароль повторно.</p>'
		formu = 'tak'
	elif not query['newpass'] == query['newpass2']: # Якщо пароль не співпадає.
		page['body'] += '<p>Помилка. Пароль не співпадає.</p>'
		formu = 'tak'
	elif re.search(r'[^.a-z|0-9|-]',query['newpass']):
		page['body'] += '<p>Помилка. Новий пароль містить недопустимі символи.</p>'
		formu = 'tak'
	elif len(query['newpass']) > 32:
		page['body'] += '<p>Помилка. Новий пароль надто довгий.</p>'
		formu = 'tak'
	elif len(query['newpass']) < 2:
		page['body'] += '<p>Помилка. Новий пароль надто короткий.</p>'
		formu = 'tak'
	else:
		sql = "select pass from users where login='" + user + "';" # Дізнаємося пароль поточного користувача.
		cursor.execute(sql)
		data = cursor.fetchone()
		if not data[0] == query['oldpass']: # Якщо старий пароль не співпадає.
			page['body'] += '<p>Помилка. Старий пароль, введений вами, неправильний.</p>'
			formu = 'tak'
		elif query['newpass'] == query['oldpass']: # Якщо новий пароль, введений користувачем, такий самий як і старий.
			page['body'] += '<p>І чим же новий пароль відрізняється від старого?</p>'
			page['title'] = 'Пароль не змінено'
		else:
			sql = "update users set pass='" + query['newpass'] + "' where login='" + user + "';"
			cursor.execute(sql)
			page['body'] += '<p>Успіх! Пароль змінено.</p>'
			page['title'] = 'Пароль змінено'
	if 'formu' in locals(): # Якщо користувачу потрібно вивести форму для зміни паролю:
		del formu
		page['body'] += """<form method="post" action="index.py">
<p>Для зміни паролю:</p>
<input name="action" value="profile" type="hidden">
<p>Введіть старий пароль:</p>
<input name="oldpass" size="16"
"""
		if 'oldpass' in query:
			page['body'] += ' value="' + query['oldpass'] + '"'
		page['body'] += """>
<p>Введіть новий пароль (має складатися лише із маленьких літер латинського алфавіту, цифр і дефісів; мінімум 2, максимум 32 символи):</p>
<input name="newpass" size="16" type="password">
<p>Повторіть новий пароль:</p>
<input name="newpass2" size="16" type="password"><br /><br />
<input type="submit" value="ОК">
</form>"""
else:
	page['body'] += '<p><a href="index.py?action=profile&newpass=">Змінити пароль</a>.</p>'
	sql = "select email from users where login='" + user + "';"
	cursor.execute(sql)
	data = cursor.fetchone()
	page['body'] += '<p>Адреса електронної пошти — ' + data[0] + '</p>'
	page['title'] = "Профіль користувача " + user # Заголовок сторінки.
include('sidebar.py') # Бокова панель сторінки.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.