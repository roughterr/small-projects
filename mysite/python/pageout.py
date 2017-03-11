# pageout.py — скрипт для виводу сторінки.
out('Content-type: text/html;encoding=UTF-8\n\n') # Показати клієнту, що вивід HTML сторінки.
out('<html>\n\t<head>\n\t\t') # Початок html сторінки.
if 'meta' in page: # Якщо треба вставити незвичний тег <meta>, то
	out(page['meta']) # видача його користувачу.
	out('\n\t\t') # Один перенос рядку і два горизонтальні відступи.
out('<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >') # він виглядає так.
favicon = open('favicon.txt',"r",encoding='utf-8').read() # Скачування картинки, яка збережена у форматі base64.
favicon = '\n\t\t<link rel="shortcut icon" href="data:image/x-icon;base64,' + favicon + '"type="image/png">'
out(favicon) # Видача значку сайта користувачу.
del favicon # Видалення використаної змінної.
out('\n\t\t<title>') # Початок заголовку сторінки.
out (nazvasaitu) # Вивід назви сайту у заголовку сторінки.
if 'title' in page: # Якщо є дані про заголовок сторінки.
	out(' / ') # Розділювач в заголовку сторінки між назвою сайту і заголовком конкретної сторінки.
	out(page['title']) # Видача заголовку сторінки.
out('</title>\n') # Кінець заголовку сторінки.
out('\t\t<style>') # Відкриття тегу, який означає CSS.
if not 'style' in page: # Якщо не вказано специфічний стиль сторінки.
	infile = open('style.css',"r",encoding='utf-8') # Відкриття файлу зі стилем сторінки.
	out(infile.read()) # Вивід стилю сторінки.
	infile.close()# Закриття файлу зі стилем сторінки.
	del infile # Видалення змінної для файлу зі стилем сторінки.
else: # Якщо вказано специфічний стиль сторінки, то
	out(page['style']) # його видача користувачу.
out('</style>\n') # Закриття тегу, який означає CSS.
out('<link rel="alternate" type="application/rss+xml" href="index.py?action=rss">') # Стрічка новин.
out('\t</head>\n\t<body>\n') # Кінець шапки сторінки і початок її тіла.
out('<div class="header"><h2><a href="./index.py">')  # Голова сторінки.
out(nazvasaitu)
out('</a></h2><h4 style="color:006666;">')
cursor.execute("select znachen from config where parametr ='opyssaitu';") # Витягування із БД опису сайту, який
data = cursor.fetchone() # потрібно помістити в голові сторінки.
try:
	data[0]
except: # Якщо відповідного рядочка у базі даних не знайдено.
	out('тут міг би бути опис сайту.')
else: # Якщо все добре.
	out(data[0])
out('</h4>')
out('</div>')
out(page['body']) # Видача тіла сторінки.
out('\n\t</body>\n</html>') # Кінець html сторінки.
mysql.close() # Закриття з'єдання із MySQL.
quit() # Завершення роботи скрипта після виводу сторінки.