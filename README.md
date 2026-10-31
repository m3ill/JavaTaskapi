# Java Task API

Java 21 hedefli Spring Boot 4.1.2, Spring Web, Spring JDBC ve PostgreSQL ile görev takip API’si. IntelliJ IDEA ile geliştirildi.

## Kurulum

JDK 21 veya üzeri ve çalışan PostgreSQL gereklidir. Maven Wrapper depoya dahildir.

PostgreSQL kullanıcı adınızı kullanarak veritabanını bir kez oluşturun:

```bash
psql -U KULLANICI_ADI -d postgres -c 'CREATE DATABASE taskapi;'
psql -U KULLANICI_ADI -d taskapi -f schema.sql
```

Bağlantı ayarlarını ortam değişkenleriyle verebilirsiniz. IntelliJ’de Run Configuration → Environment variables alanını kullanın. Terminal örneği:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/taskapi'
export DB_USERNAME='KULLANICI_ADI'
# Sunucunuz parola istiyorsa DB_PASSWORD değişkenini yerel ortamınızda tanımlayın.
./mvnw spring-boot:run
```

Parola belirtilmezse boş kabul edilir; PostgreSQL erişim ayarlarınız buna izin vermelidir. Uygulama `.env` dosyasını kendiliğinden yüklemez. Parolaları kaynak koduna veya Git’e yazmayın.

IntelliJ’de projeyi Maven projesi olarak açıp `TaskapiApplication` sınıfını çalıştırabilirsiniz. Maven Wrapper ile reload sırasında `ArtifactHandlerManager` hatası görülürse Settings → Build Tools → Maven → Maven home path altında Bundled (Maven 3) seçin. Terminaldeki wrapper bundan bağımsızdır.

## Endpoint’ler

Varsayılan adres: `http://localhost:8080`.

| Metot | Yol | Başarılı yanıt |
| --- | --- | --- |
| GET | `/health` | 200, status JSON’u (veritabanı sağlığını ölçmez) |
| GET | `/tasks` | 200, görev listesi |
| GET | `/tasks/{id}` | 200, tek görev |
| POST | `/tasks` | 201, oluşturulan görev |
| PATCH | `/tasks/{id}` | 200, güncellenen görev |
| DELETE | `/tasks/{id}` | 204, gövdesiz |

POST gövdesi:

```json
{"title": "Spring JDBC öğren"}
```

PATCH gövdesi:

```json
{"completed": true}
```

POST için eksik/boş başlık ve PATCH için eksik/null `completed` 400 döndürür. Bulunamayan görevi getirme, güncelleme ve silme 404 döndürür. Alan türlerinin JSON dönüşüm davranışı ayrıca sıkılaştırılmadı.

## Yapı ve doğrulama

`Controller → Service → Repository → PostgreSQL`. Request record’ları gelen veriyi, `Task` record’u görev verisini temsil eder. Repository parametreli SQL kullanır. Veriler PostgreSQL’de saklanır; uygulamanın yeniden başlamasıyla silinmez.

```bash
./mvnw test
./mvnw package
```

Mevcut test yalnızca Spring context yüklenmesini kontrol eder ve yapılandırılmış PostgreSQL ortamı gerektirebilir; CRUD entegrasyon test paketi değildir. Geliştirme oturumunda kullanıcı CRUD, 400/404/204 ve yeniden başlatma sonrası kalıcılık kontrollerinin çalıştığını bildirdi.
