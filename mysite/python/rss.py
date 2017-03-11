# rss.py — скрипт для видачі стрічки новин.
if 'cate' in query: # Якщо користувач захотів, щоб йому були видані повідомлення лише з певної категорії.
	if len(query['cate']) == 0: # Якщо параметр query['cate'] запиту містить нуль символів, то
		error('400') # видати помилку вводу.
	if len(query['cate']) > 64: # Якщо параметр query['cate'] запиту містить більше 64-х символів, то це надто багато.
		error('413')
	sql = "select name from cate where name =" + sqladopt(query['cate']) + " order by position;" # Перевірити, чи категорія існує.
	cursor.execute(sql)
	data = cursor.fetchone()
	try:
		data[0]
	except: # Якщо категорія, вказана у запиті, не існує.
		error('404')
out('Content-type: text/html;encoding=UTF-8\n\n')
out("""<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
<channel>
	<title>""")
out(nazvasaitu)
if 'cate' in query:
	out(' / ')
	out(query['cate']) # Ім'я категорії, з якої потрібно виводити повідомлення.
out('</title>\n\t<link>http://')
out(os.environ['HTTP_HOST']) # URL сайту.
out(os.environ['SCRIPT_NAME'])
if 'cate' in query:
	out('?action=catelist&amp;cate=')
	out(query['cate'])
out('</link>\n')
cursor.execute("select znachen from config where parametr ='opyssaitu';") # Витягування із БД опису сайту, який
data = cursor.fetchone() # потрібно помістити в голові сторінки.
try:
	data[0]
except: # Якщо відповідного рядочка у базі даних не знайдено.
	del data # Щоб було.
else: # Якщо все добре.
	out('\t<description>')
	out(data[0])
	out('</description>\n')
if 'cate' in query:
	sql = "select time,text,zagolovok from posts where cate=" + sqladopt(query['cate']) + " ORDER BY time DESC limit 50;"
else:	
	sql = "select time,text,zagolovok from posts ORDER BY time DESC limit 50;"
cursor.execute(sql)
data = cursor.fetchall() # Скачування результатів SQL запиту.
for row in data:
	out('\t<item>\n')
	out('\t\t<title>')
	title = row[2].replace('&','&amp;')
	title = title.replace('<','&lt;')
	title = title.replace('>','&gt;')
	out(title)
	del title
	out('</title>\n')
	out('\t\t<description>')
	description = row[1].replace('&','&amp;')
	description = description.replace('<','&lt;')
	description = description.replace('>','&gt;')
	out(description)
	del description
	out('</description>\n')
	out('\t\t<pubDate>')
	chas = time.strftime("%a, %d %b %Y %H:%M:%S GMT",time.localtime(int(row[0])))
	out(str(chas))
	del chas
	out('</pubDate>\n')
	out('\t\t<link>http://')
	out(os.environ['HTTP_HOST']) # URL сайту.
	out(os.environ['SCRIPT_NAME'])
	out('?action=list&amp;do=')
	out(str(row[0]))
	out('</link>\n')
	out('\t</item>\n')
out('</channel>\n')
out('</rss>')
mysql.close() # Закриття з'єдання із MySQL.
quit() # Завершення роботи скрипта після виводу сторінки.