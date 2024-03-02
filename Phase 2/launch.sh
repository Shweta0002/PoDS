minikube --driver=docker start

minikube start

eval $(minikube docker-env)

docker build -t bhavya-choudhary/booking -f booking/Dockerfile .
docker build -t bhavya-choudhary/users -f users/Dockerfile .
docker build -t bhavya-choudhary/wallet -f wallet/Dockerfile .
docker build -t bhavya-choudhary/db -f booking-db/Dockerfile .

minikube kubectl -- create deployment db-microservice --image=bhavya-choudhary/db
minikube kubectl -- create deployment booking-microservice --image=bhavya-choudhary/booking
minikube kubectl -- create deployment users-microservice --image=bhavya-choudhary/users
minikube kubectl -- create deployment wallet-microservice --image=bhavya-choudhary/wallet

minikube kubectl -- expose deployment db-microservice --type=LoadBalancer --port=1521
minikube kubectl -- expose deployment booking-microservice --type=LoadBalancer --port=8081
minikube kubectl -- expose deployment users-microservice --type=LoadBalancer --port=8080
minikube kubectl -- expose deployment wallet-microservice --type=LoadBalancer --port=8082