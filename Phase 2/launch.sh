minikube --driver=docker start
eval $(minikube docker-env)

docker build -t h2db ./booking-db
kubectl apply -f ./booking-db/h2db-deployment.yaml
kubectl apply -f ./booking-db/h2db-service.yaml

docker build -t booking ./booking
docker build -t users ./users
docker build -t wallet ./wallet

kubectl apply -f ./booking/booking-deployment.yaml
kubectl apply -f ./users/users-deployment.yaml
kubectl apply -f ./wallet/wallet-deployment.yaml

kubectl apply -f ./booking/booking-service.yaml
kubectl apply -f ./users/users-service.yaml
kubectl apply -f ./wallet/wallet-service.yaml

kubectl port-forward service/booking 8081:8081
kubectl port-forward service/users 8080:8080
kubectl port-forward service/wallet 8082:8082

