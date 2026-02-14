# Rotation

Bu proje Spring Boot + PostgreSQL + Flyway + JWT auth içerir ve Docker ile
kolayca ayağa kalkar. Bu doküman en basit kullanım akışını anlatır.

## Gereksinimler

- Docker Desktop
- Docker Compose

## Hızlı Başlangıç (Docker)

```bash
docker-compose up --build
```

Arka planda çalıştırmak için:

```bash
docker-compose up -d --build
```

Durdurmak için:

```bash
docker-compose down
```

Not: `docker-compose down -v` komutu veritabanı verilerini de siler.

## Servisler

- Uygulama: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- pgAdmin: `http://localhost:5050`

## Veritabanını Görme/Yönetme (pgAdmin)

1. Tarayıcıdan pgAdmin'e girin: `http://localhost:5050`
2. Giriş:
   - Email: `admin@admin.com`
   - Password: `admin`
3. Sunucu ekleyin:
   - Name: `Rotation DB`
   - Host name/address: `database` (Docker içinden) veya `localhost` (host makineden)
   - Port: `5432`
   - Maintenance database: `rotation`
   - Username: `postgres`
   - Password: `secret123`
4. Tabloları görmek için:
   - `Servers` → `Rotation DB` → `Databases` → `rotation` → `Schemas` → `public` → `Tables`

## CLI ile Veritabanı Görme

```bash
docker-compose exec database psql -U postgres -d rotation
```

Örnek tablo listesi:

```sql
\dt
```

## Kalıcılık (Volume)

Veritabanı verileri `database-data` volume'unda saklanır. Bu yüzden:

- `docker-compose down` → veriler korunur
- `docker-compose down -v` → veriler silinir

pgAdmin ayarları `pgadmin-data` volume'unda saklanır.

## Projede Eklenen Dosyalar ve Amaçları

Aşağıdaki bölümde bu projede eklenen/her biri güncellenen dosyaların amacı
kısa ve net şekilde anlatılmıştır. Bu liste özellikle JWT auth ve OpenAPI
akışını anlamak için rehber niteliğindedir.

### Kimlik Doğrulama (JWT) ile İlgili Dosyalar

- `src/main/java/com/buyukozkan/rotation/service/JwtService.java`  
  JWT üretimi, doğrulama ve claim çıkarma işlemleri.
- `src/main/java/com/buyukozkan/rotation/security/JwtAuthenticationFilter.java`  
  Her istekte `Authorization: Bearer ...` header’ını okuyup token doğrular.
- `src/main/java/com/buyukozkan/rotation/service/UserDetailsServiceImpl.java`  
  Email ile kullanıcıyı yükler ve Spring Security’ye `UserDetails` döner.
- `src/main/java/com/buyukozkan/rotation/config/SecurityConfig.java`  
  Stateless security, JWT filter, Swagger izinleri ve auth provider ayarları.
- `src/main/java/com/buyukozkan/rotation/controller/AuthController.java`  
  `/api/auth/register`, `/api/auth/login`, `/api/auth/refresh` endpoint’leri.
- `src/main/java/com/buyukozkan/rotation/dto/LoginRequest.java`  
  Login request DTO’su.
- `src/main/java/com/buyukozkan/rotation/dto/RegisterRequest.java`  
  Register request DTO’su.
- `src/main/java/com/buyukozkan/rotation/dto/RefreshTokenRequest.java`  
  Refresh token request DTO’su.
- `src/main/java/com/buyukozkan/rotation/dto/AuthResponse.java`  
  Access + refresh token response DTO’su.

### Kullanıcı Modeli ile İlgili Dosyalar

- `src/main/java/com/buyukozkan/rotation/entity/User.java`  
  Kullanıcı entity’si (email, password, role vb).
- `src/main/java/com/buyukozkan/rotation/entity/Role.java`  
  Kullanıcı rol enum’u (`USER`, `ADMIN`).
- `src/main/java/com/buyukozkan/rotation/repository/UserRepository.java`  
  `User` için JPA repository.

### OpenAPI (Swagger) ile İlgili Dosyalar

- `src/main/java/com/buyukozkan/rotation/config/OpenApiConfig.java`  
  Swagger/OpenAPI tanımı ve JWT bearer ayarı.

### Veritabanı ve Flyway

- `src/main/resources/db/migration/V1__create_users.sql`  
  İlk kullanıcı tablosu migration’ı.
- `src/main/resources/db/migration/V2__add_role_to_users.sql`  
  Kullanıcıya `role` alanı ekleyen migration.
- `src/main/resources/application.yml`  
  DB bağlantısı, Flyway, JWT ve Swagger ayarları.

### Docker

- `docker-compose.yml`  
  Uygulama + PostgreSQL + pgAdmin servisleri.
- `Dockerfile`  
  Maven ile build ve runtime image.

## Olmazsa Olmaz Dosyalar

Bu dosyalar olmadan proje ayağa kalkmaz veya auth çalışmaz:

- `pom.xml` (tüm bağımlılıklar)
- `Dockerfile`
- `docker-compose.yml`
- `src/main/resources/application.yml`
- `src/main/resources/db/migration/V1__create_users.sql`
- `src/main/java/com/buyukozkan/rotation/entity/User.java`
- `src/main/java/com/buyukozkan/rotation/entity/Role.java`
- `src/main/java/com/buyukozkan/rotation/repository/UserRepository.java`
- `src/main/java/com/buyukozkan/rotation/service/JwtService.java`
- `src/main/java/com/buyukozkan/rotation/security/JwtAuthenticationFilter.java`
- `src/main/java/com/buyukozkan/rotation/service/UserDetailsServiceImpl.java`
- `src/main/java/com/buyukozkan/rotation/config/SecurityConfig.java`
- `src/main/java/com/buyukozkan/rotation/controller/AuthController.java`

Swagger kullanımı için gerekli (isteğe bağlı):

- `src/main/java/com/buyukozkan/rotation/config/OpenApiConfig.java`
