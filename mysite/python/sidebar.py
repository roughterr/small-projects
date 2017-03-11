# userbar.py — скрипт для формування у тілі сторінки бокового блоку.
page['body'] += '\n<div class="sidebar">' # Бокова панель.
if ugroup == "admin": # Якщо користувач має права адміністратора, то вивести йому посилання на сторінку, з якої можна додати нове повідомлення.
	page['body'] += '<a href="index.py?action=addpost" style="color:green;font-size:100%;" title="опублікувати нове повідомлення">Нове повідомлення.</a><hr />'
page['body'] += 'Категорії'
if ugroup == "admin": # Якщо користувач має права адміністратора, то вивести йому кнопку для впорядкування категорій.
	page['body'] += ' (<a href="index.py?action=managecate">впорядкувати</a>)'
page['body'] += ':<br />'
sql = "select name from cate order by position;"
cursor.execute(sql)
data = cursor.fetchall()
for row in data:
	page['body'] += '<a href="index.py?action=catelist&cate=' + row[0] + '">' + row[0] + '</a><br />'
if ugroup == "admin": # Якщо користувач має права адміністратора, то вивести йому кнопку для додавання нової категорії.
	page['body'] += '<a href="index.py?action=addcate" style="color:green;" title="Додати нову категорію">Додати категорію.</a>'
page['body'] += '<hr />'
if ugroup == "admin":
	page['body'] += '<a href="index.py?action=admin">Адмініструвати сайт</a><br />'
page['body'] += '<a href="index.py?action=rss" style="color:orange;" title="Підписатись по RSS на нові публікації">Підписатись по RSS</a>'
page['body'] += '<hr /><a title="Powered by Python-3" href="http://python.org/"><b><span style="font-style:italic;color:33CC99;">Powered by Python</span></b></a>' # Вивести логотин Пайтона.
page['body'] += '</div>' # Кінець бокового блоку сторінки.