# AMQ HA in OpenShift Demo

## How to
### Prerequisites
OpenShift 4.7 up and running


### Deployment
Login to OpenShift
```bash
oc login https://host:port
```

Create namespace and install operator
```
oc apply -k .
```
