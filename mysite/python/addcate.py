# addcate.py — скрипт для створення нової категорії адміністратором.
if not ugroup == "admin": # Якщо користувач не має прав адміністратора,
	error('403') # то немає йому чого тут робити.
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn" style="{ text-align:center; }">' # Початок основного блоку сторінки.
page['title'] = "Додати категорію" # Заголовок повідомлення.
def form(): # Функція для виводу форми якщо потрібно.
	page['body'] += '<form method="post" action="index.py"><input name="action" value="addcate" type="hidden">'
	page['body'] += '<p>Введіть назву категорії, яку хочете створити<br />'
	page['body'] += '(до 64-х символів; лише маленькі літери латинського алфавіту, цифри і дефіси):<br /><br />'
	page['body'] += '<input name="name" size="60"'
	if 'name' in query:
		page['body'] += ' value="' + query['name']
	page['body'] += '"></p>'
	page['body'] += "<p>Введіть позицію категорії, яку хочете створити, в форматі цілого числа "
	page['body'] += 'від 0 до 99: <input name="position" size="2" value="'
	if 'position' in query:
		page['body'] += str(query['position'])
	else: # Якщо користувач не ввів позиції категорії.
		page['body'] += '1'
	page['body'] += '"></p><input type="submit" value="ОК"></form>'
	include('sidebar.py') # Бокова панель сторінки.
	page['body'] += '</div>' # Кінець основного блоку сторінки.
	pageout() # Виведення HTML сторінки.
if not 'name' in query: # Якщо користувач не вибрав категорію, то
	form() # вивести форму.
if query['name'] == "": # Якщо значення параметру із назвою категорії порожнє.
	form()
if re.search(r'[^.a-z|0-9]',query['name']): # Якщо назва категорії, введена користувачем, містить недопустимі символи.
	page['body'] += '<p>Помилка. Назва категорії, введена вами, містить недопустимі символи.</p>'
	form()
if len(query['name']) > 64: # Якщо назва категорії, введена користувачем, містить більше 64-х символів.
	page['body'] += '<p>Помилка. Назва категорії, введена вами, надто довга.</p>'
	form()
try:
	query['position'] = int(query['position'])
except:
	page['body'] += '<p>Помилка. Позиція категорії, введена вами, має неправильний формат.</p>'
	form()
if query['position'] > 99:
	page['body'] += '<p>Помилка. Позиція категорії, введена вами, не повинна перевищувати 99.</p>'
	form()
sql = "select name from cate where name =" + sqladopt(query['name']) + ";" # Перевірити, чи категорія існує.
cursor.execute(sql)
data = cursor.fetchone()
try: # Перевірити, чи категорія уже бува не існує.
	data[0]
except: # Якщо категорія не існує, то це нормально.
	sql = "insert into cate (name,position) values ('" + query['name'] + "','" + str(query['position']) + "');"
else:
	page['body'] += '<p>Помилка. Категорія, введена вами, уже існує.</p>'
	form()
del form # Після цього моменту функція form уже не потрібна.
cursor.execute(sql)
page['body'] += 'Категорію <a href="index.py?action=catelist&cate=' + query['name'] + '">' + query['name'] + '</a> успішно створено.'
page['title'] = 'Категорію створено.' # Заголовок сторінки.
include('sidebar.py') # Бокова панель сторінки.
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.