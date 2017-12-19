#!/bin/bash

pod_name=$(kubectl get pods --namespace=playground | grep -v NAME | awk {'print $1'})
kubectl exec -it --namespace=playground ${pod_name} -- bash
