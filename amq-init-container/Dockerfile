FROM registry.redhat.io/amq7/amq-broker-init-rhel7:latest
ADD config /amq/scripts
RUN ["chmod", "+x", "/amq/scripts/post-config.sh"]