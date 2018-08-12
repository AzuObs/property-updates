# Run Dev

```bash
docker-compose build
docker-compose up
sbt run
```


# Run Tests

```bash
docker-compose build
docker-compose up
sbt test
```

# Application Logic

When a user updates the prices of a property, the application will output a notification on STDOUT. The notification would normally be sent by email to users who are watching that property rather than simply being outputted to STDOUT.

# HTTP Routes

```text
curl -X PUT localhost:8080/v1/properties/:id/prices/:value
```

# Prepopulated Data

Prepopulated `users`, `properties`, and `property_watchers` exist in MySQL. The pre-populated data can be viewed at `docker/mysql/init/001_create_db.sql`
