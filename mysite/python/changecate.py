# changecate.py — скрипт для зміни категорій повідомлень, адміністратором.
if not ugroup == "admin": # Якщо користувач не має прав адміністратора.
	error('403')
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn" style="{ text-align: center; }">' # Початок основного блоку сторінки.
page['title'] = "Змінити категорію повідомлення" # Заголовок повідомлення.
include('sidebar.py') # Бокова панель сторінки.
if not 'time' in query: # Якщо не вказано параметр 'time', то
	error('400') # скрипт не може далі продовжувати роботу.
try: # Перевірити
	query['time'] = int(query['time']) # чи час повідомлення вказаний у правильному форматі.
except: # Якщо ні.
	page['body'] += 'Неправильний ввід. Час повідомлення вказаний у неправильному форматі.'
	page['title'] = '400'
	page['body'] += '</div>' # Кінець основного блоку сторінки.
	pageout() # Виведення HTML сторінки.
sql = "select cate,zagolovok from posts where time='" + str(query['time']) + "';" # Перевірити чи повідомлення існує.
cursor.execute(sql)
data = cursor.fetchone()
zagolovok = data[1]
try:
	cate = data[0]
except: # Якщо повідомлення не знайдено, то
	error('404') # вивести помилку 404.
page['title'] = 'Змінити категорію повідомлення'
if 'cate' in query: # Якщо користувач вибрав категорію, на яку хоче змінити.
	sql = "select name from cate where name=" + sqladopt(query['cate']) + ";"
	cursor.execute(sql)
	data = cursor.fetchone()
	try: # Перевірити, чи
		data[0] # категорія існує.
	except:
		error('404')
	else:
		sql = "update posts set cate=" + sqladopt(query['cate']) + " where time='" + str(query['time']) + "';"
		cursor.execute(sql)
		page['body'] += 'Категорію повідомлення успішно змінено.<br />'
		cate = query['cate'] # Змінна, що показує, який до якої категорії відноситься повідомлення.
page['body'] += 'Для того, щоб змінити категорію <a href="index.py?action=list&do=' + str(query['time']) + '">повідомлення "' + zagolovok + '"</a> виберіть її зі списку:<br /><br />'
del zagolovok
sql = "select name from cate order by position;" # Запит до БД на видачу списку категорій.
cursor.execute(sql)
data = cursor.fetchall()
for row in data: # Вивід категорій по одному.
	if row[0] == cate: # Якщо це категорія, яка зараз є у повідомлення.
		page['body'] += row[0] + ' (поточна категорія)'
	else: # Якщо не поточна категорія.
		page['body'] += '<a href="index.py?action=changecate&time='+ str(query['time']) + '&cate=' + row[0] + '">' + row[0] + '</a>'
	page['body'] += '<br />' # Відділення категорій одної від іншої переносами рядків.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.