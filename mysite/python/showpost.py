# showpost.py — скрипт для видачі одного повідомлення на сторінку.
include('userbar.py') # Панель користувача.
page['body'] += '<div class="osn">' # Початок основного блоку сторінки.
if not 'time' in query: # Якщо користувач не вказав час повідомлення, яке потрібно видати.
	error('400') # Видача повідомлення про помилковий ввід.
try: # Спробувати конвертувати час повідомлення у числовий формат.
	a = int(query['time'])
except: # Якщо не вдалося, то видати помилку вводу.
	error('400')
else: # Якщо вдалося, то видалити змінну, яка використовувалася для перевірки того, чи час повідомлення, введений
	del a # користувачем, був у числовому форматі.
sql = "select text,zagolovok,cate from posts WHERE time ='" + query['time'] + "' ORDER BY time DESC;"
cursor.execute(sql) # Скачування повідомлення із БД.
data = cursor.fetchone() # Скачування результату запиту.
try:
	data[0]
except: # Якщо повідомлення не знайдено.
	error('404')
page['title'] = data[1] # Заголовок повідомлення в заголовок сторінки.
page['body'] += "<h3>" + data[1] + "</h3>" # Заголовок повідомлення.
page['body'] += data[0] # Текст повідомлення.
if data[2] == None: # Якщо повідомлення не категоризовано.
	page['body'] += '<br /><br /><span style="float:left">Некатегоризовано'
else:
	page['body'] += '<br /><br /><span style="float:left">Категорія: <a href="index.py?action=catelist&cate=' + data[2] + '">' + data[2] + '</a>' # Вивести категорію, до якої відноситься це повідомлення.
if ugroup == "admin": # Якщо користувач має права адміністратора, то показати йому кнопку для зміни категорії.
	page['body'] += '  (<a href="index.py?action=changecate&time=' + query['time'] + '">' + 'змінити</a>)'
page['body'] += '</span>'
page['body'] += '<span style="float:right">Опубліковано о ' + time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(int(query['time']))) + '</span><br />' # Тут час переводиться із формату кількості секунд від 1970 року у формат "рік-місяць-день години:хвилини:секунди".
if ugroup == "admin": # Якщо користувач — адміністратор, то
	page['body'] += '<a href="index.py?action=delete&time=' + query['time'] + '" style="color:red">Видалити це повідомлення</a> || <a href="index.py?action=edit&time=' + query['time'] + '" style="color:lime">Редагувати це повідомлення</a>'
include('sidebar.py') # Бокова панель.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.