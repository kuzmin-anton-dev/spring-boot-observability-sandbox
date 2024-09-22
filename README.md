# spring-boot-observability-sandbox

#### Before you start

1. Run docker compose
```shell
docker compose -f infrastructure/docker-compose.yml up -d
```

### How to run the project

1. Run downstream and upstream services
2. Execute http requests from the `/requests` folder
3. Check observability tools

### Useful links

1. [Jaeger](http://localhost:16686/)
2. [Grafana](http://localhost:3000/)
3. [Kibana](http://localhost:5601/)
