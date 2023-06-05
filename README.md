# Toko Komputer Microservices
## Fullstack Angular Springboot Microservices

### Overview

1. Project Architecture
![architecture](https://i.postimg.cc/jSRxxpDm/microservices-tokkom.png) 

2. Angular Landing Page
![landing1](https://i.postimg.cc/HLBCknq7/landing1.png)
![landing2](https://i.postimg.cc/QtCZ9sDY/landing2.png)
![landing3](https://i.postimg.cc/Jh1L8Z2L/landing3.png)

3. Angular Admin Panel Page
![home](https://i.postimg.cc/wBV9R0mM/home.png)
![product](https://i.postimg.cc/Hnfm1NfZ/product.png)
![productpreview](https://i.postimg.cc/sDhy4Vht/product-preview.png)
![orderpreview](https://i.postimg.cc/BZy4F8XT/order-preview.png)
![notification](https://i.postimg.cc/cCKWjW5t/notification.png)

### Technology Stack
1. Springboot
2. Spring Cloud Gateway
3. Eureka Server
4. Resilience4j Circuit Breaker
5. Rest API
6. Mysql
7. Postgre
8. MongoDB
9. Zipkin
10. ELK
11. Redis
12. Kafka
13. Docker

### Prerequisite

1. Check ng version
   ```
   ng version
   Angular CLI: 15.2.5
   ```
2. Check node version
   ```
   node -v
   v16.20.0
   ```
3. Check npm version
   ```
   npm -v
   8.19.4
   ```
4. Check docker version
   ```
   docker --version
   Docker version 20.10.23, build 7155243
   ```

### Installation Steps

1. Clone this repo
   ```
   git clone https://github.com/mademanik/toko-komputer-microservices.git
   ```
   
#### Running Springboot Backend Server
2. cd into toko-komputer-microservices
   ```
   cd toko-komputer-microservices
   ```
3. cd into tokkom-backend
   ```
   cd tokkom-backend
   ```
4. run docker compose command
   ```
   docker compose up
   ```
5. running server done

#### Running Angular Frontend Client
6. cd into tokkom-frontend
   ```
   cd tokkom-frontend
   ```
7. run npm install or yarn to download package dependency
   ```
   npm i
   ```
8. run npm start to run angular client frontend
   ```
   npm start
   ```
9. open to port http://localhost:8081 to open landing page
10. open to port http://localhost:8081/panel to open panel page
11. running client done
