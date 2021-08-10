# AMQ Mirror with Broker connections in OpenShift

Demo of AMQ mirror option to capture events from the broker and pass them over the wire to another broker. This enables you to capture multiple asynchronous replicas. More info: [ActiveMQ Broker Connections](https://activemq.apache.org/components/artemis/documentation/latest/amqp-broker-connections.html#mirror)

```
+------------------------+         +-------------------------+
|                        |         |                         |
|  namespace: AMQ-MTL    |         |  namespace: AMQ-LVL     |
|     broker: AMQ-MTL    |         |     broker: AMQ-LVL     |
|                        |         |                         |
|                        |         |                         |
|                        |         |                         |
|                        |         |                         |
|                        |         |                         |
|                        |         |                         |
| +--------------------+ |         | +---------------------+ |
| |                    | |         | |                     | |
| |  queue: demo1      |<+---------+-+    queue: demo1     | |
| |                    | |         | |                     | |
| +--------------------+ |         | +---------------------+ |
|                        |         |                         |
+------------------------+         +-------------------------+
```

## How to
### Prerequisites
OpenShift 4.7 up and running


### Deployment
Login to OpenShift
```bash
oc login https://host:port
```

Create namespaces `amq-mtl` and `amq-lvl` and install operators
```
oc apply -k operators
```
Create brokers `amq-mtl` and `amq-lvl` with `demo1` queue address in corresponding namespaces 
```
oc apply -k brokers
```

### Build custom init container (optional)
If required build your own AMQ Init Container image:
```bash
cd amq-init-container
docker build -t quay.io/REPOSITORY/PROJECT:TAG --progress=plain .
docker push quay.io/REPOSITORY/PROJECT:TAG
```
Change `broker/amq-lvl-broker.yaml` , set `initImage` to your `quay.io/REPOSITORY/PROJECT:TAG`

### Build consumer app
```bash
cd amq-fed-quarkus
mvn package
docker build -f src/main/docker/Dockerfile.jvm -t quay.io/REPOSITORY/APP:tag .
docker docker push quay.io/REPOSITORY/APP:tag
```
Change `application/amq-lvl-app.yaml` and `application/amq-mtl-app.yaml` , set `image` to your `quay.io/REPOSITORY/APP:TAG`