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

2. Install operators
```
oc apply -k operators
```
3. Create namespaces `amq-mtl` and `amq-lvl`, create brokers `amq-mtl` and `amq-lvl` with `demo1` queue address in corresponding namespaces 
```
oc apply -k brokers
```
### Test

1. Open AMQ Broker Management console in `amq-lvl` and add message to `demo1` queue
2. Open AMQ Broker Management console in `amq-mtl` and check message in `demo1` queue
3. Make the same for `amq-mtl` to `amq-lvl`
