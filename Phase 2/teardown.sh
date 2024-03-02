# Stop and remove all containers
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

# Delete deployments and services
minikube delete --all
kubectl delete deployments --all
kubectl delete services --all

# Remove Docker images
docker rmi $(docker images -q)
