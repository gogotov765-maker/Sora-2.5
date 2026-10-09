# Sora 2.5 — версия 1.2026.010

Нативное Android-приложение (Kotlin + Compose) и сервер генерации (Python).

## Что внутри
- `app/` — приложение: вход через Google (обои + облачко + «Sora 2.5»), лента, создание
  (текст / персонаж / картинка), профиль с черновиками и публикацией.
- `server/` — сервер: принимает запрос, генерирует видео, вшивает вотермарку
  (хромакей, 60%, каждые 5 с угол меняется), раздаёт файл.
- `firestore.rules` — правила базы. `.github/workflows/build.yml` — сборка APK в облаке.

## Что нужно сделать тебе
1. **Свежий `google-services.json`** положи в `app/` (старый — для `com.sora25.app`,
   без OAuth-клиентов; нужен для `com.sora25.app2` и скачанный ПОСЛЕ включения Google-входа).
2. **Обои входа**: замени `app/src/main/res/drawable/login_bg.webp` на свои
   (если другой формат — удали `login_bg.webp`, положи `login_bg.jpg` / `.png`).
3. **Firestore**: Firebase → Build → Firestore Database → Create (production mode),
   вкладка Rules → вставь `firestore.rules` → Publish.
4. **Сервер** (нужен ffmpeg): `cd server && pip install -r requirements.txt`,
   ключ сервисного аккаунта (Firebase → Project settings → Service accounts → Generate key)
   сохрани как `server/service-account.json`, затем задай переменные из `.env.example` и
   `uvicorn main:app --host 0.0.0.0 --port 8000`.
   Без `FAL_KEY` работает демо-режим (тестовое видео + вотермарка).
5. **API модели**: впиши `FAL_KEY` и проверь id моделей на fal.ai.
6. **Сборка APK без Android Studio**: залей папку в приватный репозиторий GitHub, в
   Settings → Secrets добавь `KEYSTORE_BASE64` (`certutil -encode sora25.keystore k.txt` на
   Windows, содержимое без строк BEGIN/END), `KEYSTORE_PASSWORD`, `BACKEND_URL`
   (адрес сервера с https). Actions → Build APK → Run workflow → скачай APK из Artifacts.

## Заметки
- Вес: универсальный APK (arm64-v8a, armeabi-v7a, x86_64) и отдельные по архитектурам;
  R8 и сжатие ресурсов включены. Тяжёлых нативных библиотек нет.
- Проект не собирался в этой среде (нет Android SDK) — первая сборка может потребовать
  мелких правок; присылай лог ошибок.
- В `AndroidManifest.xml` включён http (для теста). Для боевого сервера используй https
  и убери `usesCleartextTraffic`.
