# AMQ HA and DR on OpenShift

## Architecture

1. Two namespaces in Openshift
2. Two brokers AMQ cluster in each namespace
4. Topic `demo1` in each broker
5. Dual mirror (Disaster recovery) configuration of brokers [ActiveMQ Broker Connections](https://activemq.apache.org/components/artemis/documentation/latest/amqp-broker-connections.html#mirror)


## How to
### Prerequisites
OpenShift 4.12 up and running

### Deployment
1. Login to OpenShift
```bash
oc login --token=XXX --server=https://XXX:6443
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
oc project amq-mtl
```
1. Consumer
Build and deploy consumer
```
mvn clean package -Dnamespace=amq-mtl -Dname=consumer -Dquarkus.openshift.env.vars.connection=amqp://amq-mtl-all-0-svc:61616
```
2. Producer
Build and deploy producer
```
mvn clean package -Dnamespace=amq-mtl -Dname=producer -Dquarkus.openshift.env.vars.connection=amqp://amq-mtl-all-0-svc:61616
```
### Verify results
Message sent
```
oc get pods --selector application=producer -o name | xargs oc logs
```
Result:
```
2023-04-13 18:40:13,181 INFO  [producer] (Camel (camel-1) thread #1 - timer://demo) 1681411211518 : V_1
2023-04-13 18:40:13,804 INFO  [producer] (Camel (camel-1) thread #1 - timer://demo) 1681411211518 : V_2
2023-04-13 18:40:14,800 INFO  [producer] (Camel (camel-1) thread #1 - timer://demo) 1681411211518 : V_3
```

Message received
```
oc get pods --selector application=consumer -o name | xargs oc logs
```
result
```
2023-04-13 18:40:13,162 INFO  [consumer] (Camel (camel-1) thread #1 - JmsConsumer[demo1]) 1681411211518 : V_1
2023-04-13 18:40:13,172 INFO  [consumer] (Camel (camel-1) thread #1 - JmsConsumer[demo1]) 1681411211518 : V_1
2023-04-13 18:40:13,802 INFO  [consumer] (Camel (camel-1) thread #1 - JmsConsumer[demo1]) 1681411211518 : V_2
2023-04-13 18:40:13,817 INFO  [consumer] (Camel (camel-1) thread #1 - JmsConsumer[demo1]) 1681411211518 : V_2
2023-04-13 18:40:14,798 INFO  [consumer] (Camel (camel-1) thread #1 - JmsConsumer[demo1]) 1681411211518 : V_3
2023-04-13 18:40:14,808 INFO  [consumer] (Camel (camel-1) thread #1 - JmsConsumer[demo1]) 1681411211518 : V_3
```
