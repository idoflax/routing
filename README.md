# Routing Service

- This uses information from <https://raw.githubusercontent.com/mledoze/countries/master/countries.json> to determine the best land route between two given points
- The service runs on <http://localhost:8080>

### Technologies

- Kotlin 1.5 / Java 11
- Spring boot 2.5
- Redis
- A* Pathfinding Algorithm

# Run instructions

- Run `RUNME.sh`, it will build the jar with maven and use docker-compose to run the service and the dependant cache
- Go to <http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config> and try it out

