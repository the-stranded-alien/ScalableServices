#!/bin/bash

set -e

NAMESPACE="hotel-management"
SERVICES=("api-gateway" "booking-service" "eureka-server" "hotel-service" "notification-service" "user-service")

echo "🟡 Setting Docker environment to Minikube..."
eval $(minikube docker-env)

echo "🛠️  Building services via docker-compose..."
docker-compose build

echo "🏷️  Tagging services for Kubernetes compatibility..."
for svc in "${SERVICES[@]}"; do
  docker tag scalableservices-$svc $svc:latest || echo "$svc image already properly tagged"
done

echo "🔁 Recreating namespace: $NAMESPACE"
kubectl delete namespace $NAMESPACE --ignore-not-found
kubectl create namespace $NAMESPACE

echo "📦 Applying namespace YAML (if exists)..."
[ -f k8s/namespace/namespace.yaml ] && kubectl apply -f k8s/namespace/namespace.yaml || true

echo "🧱 Applying infrastructure YAML (databases, message brokers, etc)..."
kubectl apply -f k8s/infra/infra.yaml

echo "🚀 Deploying services..."
find k8s -type f -name "*.yaml" ! -path "*/infra/*" ! -path "*/namespace/*" -exec kubectl apply -f {} \;

echo "✅ Done. Watching pods in $NAMESPACE..."
kubectl get pods -n $NAMESPACE -w