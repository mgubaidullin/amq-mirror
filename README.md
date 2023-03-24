# AMQ Mirror with Broker connections on OpenShift

Demo of AMQ mirror option to capture events from the broker and pass them over the wire to another broker. 

More info: [ActiveMQ Broker Connections](https://activemq.apache.org/components/artemis/documentation/latest/amqp-broker-connections.html#mirror)

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
| |  queue: demo1      |<+---------+>|    queue: demo1     | |
| |                    | |         | |                     | |
| +--------------------+ |         | +---------------------+ |
|                        |         |                         |
+------------------------+         +-------------------------+
```

## How to
### Prerequisites
OpenShift 4.10 up and running

### Deployment
1. Login to OpenShift
```bash
oc login https://host:port
```
2. Create namespaces `amq-mtl` and `amq-lvl`. Install operators
```
oc apply -k operators
```
3. Create brokers `amq-mtl` and `amq-lvl` with `demo1` topic address in corresponding namespaces 
```
oc apply -k brokers
```
### Deploy applications
1. Sender
```
cd client-amqp
mvn clean package -Dnamespace=amq-mtl -Dname=amqp-sender
```
2. Receiver
```
cd client-amqp
mvn clean package -Dnamespace=amq-mtl -Dname=amqp-receiver
```
