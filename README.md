# Repsy Package Manager

Minimal bir package repository REST API sistemi. .rep (zip) ve meta.json dosyalarının yüklenip indirilebildiği Spring Boot tabanlı bir uygulama.

---

## 🚀 Özellikler

- REST API üzerinden .rep (binary zip) ve meta.json dosyası yükleme
- PostgreSQL veritabanı ile kayıt takibi
- Dosyaları file-system'e kaydetme
- Docker ile container yapısı
- Storage stratejisi seçilebilir (file-system / object-storage)

---

## 🛠 Geliştirme ve Çalıştırma

### Projeyi başlatmak için:

```bash
docker-compose up --build
```

## Get ve Post istekleri
### GET /{packageName} /{version} /{fileName}
- İstemci tarafından gönderilen packageName ve version bilgisi ile meta.json dosyasını döner.

### POST /{packageName}/{version}
- İstemci tarafından gönderilen packageName ve version bilgisi ile meta.json dosyasını alır ve veritabanına kaydeder.

## Depolama Stratejileri
- FileSystemStorage: Dosyaları yerel dosya sistemine kaydeder.

## Gereksinimler
- Java 17
- PostgreSQL
- Docker
- Docker Compose
- Spring Boot


