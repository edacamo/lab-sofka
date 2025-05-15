# lab-sofka
Laboratorio de microservicios con eventos Kafka

# INSTRUCCIONES DE CONFIGURACIÓN Y EJECUCIÓN

## INDICACIONES GENERALES
El proyecto ha sido compilado y probado en un sistema operativo Mac OS con JDK 17.
### La base datos que se usa es PostgreSQL, una imagen en docker.

## REQUISITOS
1. Descargar e instalar Docker (para Mac/Windows/Linux).
2. Instalar JDK 17.
3. Microservicios creados con Spring Boot versión 3.4.3.
4. Instalar y configurar Maven.

## CLONAR REPOSITORIO
```bash
git clone https://github.com/edacamo/lab-sofka.git
```

## PRUEBAS UNITARIAS E INTEGRALES

### Para revisar la prueba unitaria (en `microservice-persons`):
```bash
cd microservice-persons
mvn clean install
```

###  Para revisarla prueba integral (en `microservice-accounts`):
```bash
cd microservice-accounts
```

### Modificar la línea 26 de "kafka:9092" a "localhost:9092" en la clase KafkaConsumerConfig.java
### Después, ejecutar el siguiente comando:
```bash
mvn clean install
```

## EJECUTAR DOCKER-COMPOSE PARA DESPLEGAR LOS CONTENEDORES EN DOCKER

Antes de ejecutar `docker-compose`, revertir el cambio de "localhost:9092" a "kafka:9092" en la clase KafkaConsumerConfig.java de `ms-accounts`.
 Luego, ejecutar el siguiente comando para construir y desplegar los contenedores:
```bash
docker-compose -f docker-compose-sofka.yml up --build
```

## LOGS EN CONTENEDORES
### Para monitorear los logs de los contenedores, ejecutar los siguientes comandos:
```bash
docker logs -f kafka
docker logs -f zookeeper
docker logs -f microservice-persons
docker logs -f microservice-accounts
```


