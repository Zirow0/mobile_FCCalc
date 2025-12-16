# Інструкція з компіляції та очищення Android проекту

## Передумови

- Java JDK 11 або вище
- Android SDK
- Змінні середовища `JAVA_HOME` та `ANDROID_HOME` налаштовані

## Створення нового проекту з шаблону

```powershell
.\rename-project.ps1 MyProjectName
```

## Компіляція

### Debug збірка

```bash
./gradlew assembleDebug
```

APK файл буде створено в: `app/build/outputs/apk/debug/app-debug.apk`

### Release збірка

```bash
./gradlew assembleRelease
```

APK файл буде створено в: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Збірка без демона (рекомендовано для CI/CD)

```bash
./gradlew assembleDebug --no-daemon
```

## Очищення проекту

### Повне очищення

```bash
./gradlew clean
```

Видаляє директорію `build/` та всі скомпільовані файли.

### Очищення з пересборкою

```bash
./gradlew clean assembleDebug
```

### Ручне очищення кешу Gradle

```powershell
Remove-Item -Recurse -Force .gradle
Remove-Item -Recurse -Force app\build
Remove-Item -Recurse -Force build
```

Або в bash:

```bash
rm -rf .gradle app/build build
```

## Корисні команди

| Команда | Опис |
|---------|------|
| `./gradlew tasks` | Список всіх доступних задач |
| `./gradlew dependencies` | Показати залежності проекту |
| `./gradlew build` | Повна збірка (debug + release) |
| `./gradlew test` | Запуск unit-тестів |
| `./gradlew connectedAndroidTest` | Запуск інструментальних тестів |
| `./gradlew lint` | Перевірка коду на помилки |

## Вирішення проблем

### Помилка "Could not determine Java version"

Переконайтесь, що `JAVA_HOME` вказує на JDK 11+:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
```

### Помилка "SDK location not found"

Створіть файл `local.properties` в корені проекту:

```properties
sdk.dir=C\:\\Users\\USERNAME\\AppData\\Local\\Android\\Sdk
```

### Очищення кешу Gradle глобально

```powershell
Remove-Item -Recurse -Force $env:USERPROFILE\.gradle\caches
```
