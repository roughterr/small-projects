# catelist — скрипт для виводу повідомлень лише з певної категорії.
try: # Перевірити, чи користувач вибрав категорію, з якої потрібно виводити повідомлення.
	a = query['cate']
	a = str(a)
except: # Якщо користувач не вибрав категорію або неправильний ввід, то вивід помилки 400.
	error('400')
a = len(a) # Визначення довжини значення параметру query['cate'], що вказаний у запиті.
if a == 0: # Якщо параметр query['cate'] запиту містить нуль символів, то
	error('400') # видати помилку вводу.
if a > 64: # Якщо параметр query['cate'] запиту містить більше 64-х символів, то це надто багато.
	error('413')
sql = "select name from cate where name =" + sqladopt(query['cate']) + " order by position;" # Перевірити, чи категорія існує.
cursor.execute(sql)
data = cursor.fetchone()
try:
	data[0]
except: # Якщо категорія, вказана у запиті, не існує.
	error('404')
cursor.execute("select znachen from config where parametr = 'maxlist';") # Запит до бази даних на видачу максимальної кількості повідомлень на сторінці.
data = cursor.fetchone() # Скачування результатів запиту
maxlist = int(data[0]) + 1 # І вибираємо перший і єдиний можливий результат. І збільшуємо його на одиницю.
maxlist = str(maxlist) # Перетворення у рядковий тип для того, щоб можна було вставити у SQL запит
include('userbar.py') # Панель користувача.
page['body'] += '<div class="osn">' # Початок основного блоку сторінки.
include('sidebar.py') # Бокова панель.
if ugroup == "admin": # Якщо користувач має права адміністратора, то вивести йому кнопку для видалення категорії.
	page['body'] += '<p align="center"><big>Категорія ' + query['cate'] + '. <a href="index.py?action=removecate&cate=' + query['cate'] + '" title="Видалити категорію" style="color:red">(x)</big></a>'
else: # Якщо звичайний користувач.
	page['body'] += '<p align="center"><big>Повідомлення з категорії ' + query['cate'] + '.</big></p>'
page['body'] += '<p align="right"><a type="application/rss+xml" href="index.py?action=rss&cate=' + query['cate'] + '" style="color:orange;">Підписатись по RSS на оновлення з цієї категорії</a></p>'
page['title'] = query['cate'] # Заголовок сторінки.
if 'vid' in query and 'do' in query: # Якщо вказано взаємовиключні параметри, то
	error('400') # видача повідомлення з помилкою користувачу.
elif not 'vid' in query and not 'do' in query:
	sql = "select time,text,zagolovok,cate from posts where cate=" + sqladopt(query['cate']) + " ORDER BY time DESC limit " + maxlist + ";" # Запит до БД на видачу повідомлень.
elif 'vid' in query:
	if not re.search(r'[^.0-9]',query['vid']): # Перевірити чи запит не містить SQL-ін'єкції.
		sql = "select time,text,zagolovok,cate from posts where time >= '" + query['vid'] + "' and cate =" + sqladopt(query['cate']) + " ORDER BY time limit " + maxlist + ";"
	else: # Якщо можливо містить,
		error('400') # то вивести помилку вводу.
elif 'do' in query:
	if not re.search(r'[^.0-9]',query['do']): # Перевірити чи запит не містить SQL-ін'єкції.
		sql = "select time,text,zagolovok,cate from posts where time <= '" + query['do'] + "' and cate=" + sqladopt(query['cate']) + " ORDER BY time DESC limit " + maxlist + ";"
	else: # Якщо можливо містить,
		error('400') # то вивести помилку вводу.
cursor.execute(sql) # Виконання SQL запиту.
data = cursor.fetchall() # Скачування результатів SQL запиту.
try: # Перевірити, чи
	data[0] # знайдено повідомлення.
except: # Якщо не знайдено, то виведення повідомлення про це і все.
	page['body'] += "<p align=center>Повідомлень не знайдено.</p>"
	pageout() # Виведення HTML сторінки.
rez = [] # Перетворення списку data в список rez для того, бо список "data" є 'tuple' типом
rez += data # і видає помилку: "TypeError: 'tuple' object doesn't support item deletion" при
del data # спробі видалити елемент списку.
maxlist = int(maxlist) - 1 # Зменшення  на одиницю змінної із кількістю знайдених результатів. Бо список починається з нуля.
if 'vid' in query: # Якщо було задано дату, до якої варто виводити повідомлення, то пересортувати список у
	rez.reverse() # оберненому порядку, так як запит у БД йшов без "DESC".
	try: # Перевірити чи ще є повідомлення.
		rez[maxlist] = rez[maxlist] 
	except: # Якщо немає, то
		del maxlist # видалити змінну із кількістю повідомлення і поки-що більш нічого.
	else: # Якщо є, то
		vid = str(rez[0][0]) # піде у посилання.
		del rez[0],maxlist # видалити перший результат, бо він не потрібен.
	sql = "select time from posts where time > '" + str(rez[-1][0]) + "' and cate=" + sqladopt(query['cate']) + " order by time;" # Спитати базу даних чи є старіші повідомлення.
	cursor.execute(sql) # Виконання SQL запиту.
	data = cursor.fetchone() # Скачування результатів SQL запиту.
	try: # Перевірити 
		do = rez[-1][0] # чи є старіші повідомлення.
	except: # Якщо старіших повідомлень немає, то просто
		del data # видалення змінної дата
	else: # Якщо є, то беремо найстаріше знайдене на цій сторінці повідомлення
		do = int(do) - 1 # і зменшуємо на одиницю, щоб на сторінки не дублювали повідомлення.
		do = str(do) # І зменшена на одну секунду дата піде у посилання.
else: # Якщо потрібно виводити повідомлення, старіші якоїсь дати.
	try: # Перевірити 
		rez[maxlist] = rez[maxlist] # чи ще є повідомлення.
	except: # Якщо немає, то
		del maxlist # видалити змінну із кількістю повідомлення і поки-що більш нічого.
	else: # Якщо є, то
		do = str(rez[maxlist][0]) # Піде у посилання.
		del rez[maxlist],maxlist # видалити перший результат, бо він не потрібен.
	sql = "select time from posts where time > '" + str(rez[0][0]) + "' and cate=" + sqladopt(query['cate']) + " order by time;" # Спитати базу даних чи є новіші повідомлення.
	cursor.execute(sql) # Виконання SQL запиту.
	data = cursor.fetchone() # Скачування результатів SQL запиту.
	try: # Перевірити чи є новіші повідомлення.
		vid = str(data[0])
	except:
		del data
for row in rez: # Перебір результатів запиту по одному.
	page['body'] += '<h3><a href="index.py?action=showpost&time=' + str(row[0]) + '">' + row[2] + "</a></h3>" # Заголовок повідомлення.
	page['body'] += row[1] # Текст повідомлення.
	page['body'] += '<br /><br />'
	if ugroup == "admin": # Якщо користувач має права адміністратора, то вивести йому кнопку для зміни категорії.
		page['body'] += '<span style="float:left"><a href="index.py?action=changecate&time=' + str(row[0]) + '">Змінити категорію</a></span>'
	page['body'] += '<span style="float:right">Опубліковано о ' + time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(int(row[0]))) + '</span><br />' # Тут час переводиться із формату кількості секунд від 1970 року у формат "рік-місяць-день години:хвилини:секунди".
	if ugroup == "admin": # Якщо користувач — адміністратор, то
		page['body'] += '<a href="index.py?action=delete&time=' + str(row[0]) + '" style="color:red">Видалити це повідомлення</a> || <a href="index.py?action=edit&time=' + str(row[0]) + '" style="color:lime">Редагувати це повідомлення</a>'
	page['body'] += "<hr>" # Кожне повідомлення буде відділено горизонтальною лінією від іншого.
if 'do' in locals():
	page['body'] += '<span style="float:right"><a href="index.py?action=catelist&cate=' + query['cate'] + '&do=' + do + '">Старіші повідомлення</a></span>'
	del do # Видалення використаної змінної.
if 'vid' in locals():
	page['body'] += '<span style="float:left"><a href="index.py?action=catelist&cate=' + query['cate'] + '&vid=' + vid + '">Новіші повідомлення</a></span>'
	del vid # Видалення використаної змінної.
page['body'] += "<br />" # Для того, щоб текст із параметром "float" не вилазив за межі елементу.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.