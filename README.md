# AMQ HA and DR on OpenShift

## Architecture

1. Two namespaces in Openshift
2. Two brokers AMQ cluster in each namespace
4. Topic `demo1` in each broker
5. Dual mirror (Disaster recovery) configuration of brokers [ActiveMQ Broker Connections](https://activemq.apache.org/components/artemis/documentation/latest/amqp-broker-connections.html#mirror)


## How to
### Prerequisites
OpenShift 4.10 up and running

### Deployment
1. Login to OpenShift
```bash
oc login https://host:port
```
2. Install operators
```
oc apply -k operators
```
3. Create namespaces `amq-mtl` and `amq-lvl`. Create brokers `amq-mtl` and `amq-lvl` with `demo1` topic address in corresponding namespaces 
```
oc apply -k brokers
```
### Deploy applications
```
cd client-amqp
```
1. Sender
```
mvn clean package -Dnamespace=amq-mtl -Dname=producer
```
2. Receiver
```
mvn clean package -Dnamespace=amq-mtl -Dname=consumer
```
