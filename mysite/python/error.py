# error.py — скрипт для виводу повідомлень про помилку.
page['body'] = '<div class="osn" style="{ text-align: center; }">' # Початок основного блоку сторінки.
if error == "400": # Якщо помилка 400.
	page['title'] = "400" # То заголовок сторінки буде такий.
	page['body'] += "Неправильний ввід." # І повідомлення на сторінці буде таке.
elif error == "404": # Якщо помилка 404.
	page['title'] = "404"
	page['body'] += "Сторінку не знайдено."
elif error == "403": # Якщо помилка 403.
	page['title'] = "403"
	page['body'] += "Доступ заборонено."
elif error == "413": # Якщо помилка 413.
	page['title'] = "413"
	page['body'] += "Запит надто довгий."
elif error == 'yet': # Якщо потрібно перенаправити користувача на головну сторінку сайту.	
	page['body'] = 'Redirecting to a <a href="./index.py">home page</a>.'
	page['meta'] = '<meta http-equiv="refresh" content="1; URL=./index.py">' # Тег <meta> у шапці сторінки.
	page['style'] = ""
else: # Якщо функцію викликано з інакшим кодом, то
	page['title'] = "failure" # все ж напевно варто вивести повідомлення
	page['body'] += "Невідома помилка." # про невідому помилку.
page['body'] += '</div>' # Кінець основного блоку сторінки.
includefile.close() # Закриття файлу.
pageout() # Вивід сторінки користувачу.