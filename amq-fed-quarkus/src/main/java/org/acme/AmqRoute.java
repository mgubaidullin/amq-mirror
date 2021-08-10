package org.acme;

import io.vertx.core.Vertx;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
public class AmqRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:demo1").id("receiver")
                .log("${body}");
    }

    @Named("activemq")
    ActiveMQComponent createActiveMQComponent() {
        ActiveMQComponent activemq = new ActiveMQComponent();
        String namespace = Vertx.vertx().fileSystem().readFileBlocking("/var/run/secrets/kubernetes.io/serviceaccount/namespace").toString();
        activemq.setBrokerURL("tcp://" + namespace + "-hdls-svc:61616");
        return activemq;
    }
}
