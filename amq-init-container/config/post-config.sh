#!/bin/bash
echo "#### Custom config start. ####"
diverts=""
diverts="      ${diverts}<broker-connections>\n"
diverts="      ${diverts}<amqp-connection uri=\"tcp://amq-mtl-hdls-svc.amq-mtl.svc.cluster.local:61616\" name=\"amq-mtl\">\n"
diverts="      ${diverts}<mirror  queue-removal=\"true\" queue-creation=\"true\" message-acknowledgements=\"true\" source-mirror-address=\"myLocalSNFMirrorQueue\"/>\n"
diverts="      ${diverts}</amqp-connection>\n"
diverts="      ${diverts}</broker-connections>\n"
sed -i "s|  <addresses>|${diverts}  <addresses> ${address}|g" ${CONFIG_INSTANCE_DIR}/etc/broker.xml
echo "#### Custom config done. ####"


