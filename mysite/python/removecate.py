# removecate.py — скрипт для видалення категорій адміністратором.
if not ugroup == "admin": # Якщо користувач не має прав адміністратора.
	error('403')
include('userbar.py') # Блок із виведенням користувачу даних про його авторизацію.
page['body'] += '<div class="osn" style="{ text-align:left; }">' # Початок основного блоку сторінки.
page['title'] = "Видалити категорію" # Заголовок повідомлення.
try: # Перевірити, чи адміністратор вибрав категорію, яку хоче видалити.
	a = query['cate']
	a = str(a)
except: # Якщо користувач не вибрав категорію або неправильний ввід, то вивід помилки 400.
	error('400')
a = len(a) # Визначення довжини значення параметру query['cate'], що вказаний у запиті.
if 0 > a: # Якщо параметр query['cate'] запиту містить нуль символів, то
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
if not 'how' in query: # Якщо користувач в запиті не вказав як саме видаляти категорію, то спитати його.
	page['body'] += '<p style="{ text-align:center; }">Як ви хочете видалити категорію ' + query['cate'] + '?<br />Вибравши варіант видалення, підтвердження не буде запитано — категорію буде видалено відразу.</p>'
	page['body'] += '<p><a style="color:pink;" href="index.py?action=removecate&cate=' + query['cate'] + '&how=withoutposts">Лише саму категорію, повідомлення в ній зробити некатегоризованими.</a></p>'
	page['body'] += '<p><a style="color:red;" href="index.py?action=removecate&cate=' + query['cate'] + '&how=withinposts">Разом з повідомленнями в ній.</a></p>'
	include('sidebar.py') # Бокова панель сторінки.
elif query['how'] == "withoutposts": # Якщо користувач захотів видалити лише саму категорію.
	sql = "delete from cate where name=" + sqladopt(query['cate']) + ";"
	sql += "update posts set cate=null where cate='" + query['cate'] + "';"
	cursor.execute(sql)
	del sql
	page['body'] += '<p style="{ text-align:center; }">Категорію "' + query['cate'] + '" успішно видалено. Повідомлення, що були якщо, то в ній, стали некатегоризованими.</p>'
	include('mysql-connect.py') # Реконект до MySQL.
elif query['how'] == "withinposts": # Якщо користувач захотів видалити категорію разом із повідомленнями в ній.
	sql = "delete from cate where name=" + sqladopt(query['cate']) + ";"
	sql += "delete from posts where cate=" + sqladopt(query['cate']) + ";"
	cursor.execute(sql)
	del sql
	page['body'] += '<p style="{ text-align:center; }">Категорію "' + query['cate'] + '" успішно видалено разом із повідомленнями в ній якщо вони були.</p>'
	include('mysql-connect.py') # Реконект до MySQL.
else: # Якщо невідома дія, то вивести повідомлення про помилку користувачу.
	error('400') #
page['body'] += '</div>' # Кінець основного блоку сторінки.
pageout() # Виведення HTML сторінки.