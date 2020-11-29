package com.javaworm.configme;

import com.javaworm.configme.controllers.ConfigResourceController;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.javaoperatorsdk.operator.Operator;

public class Program {
    public static void main(String[] args) {
        final var k8sClient = new DefaultKubernetesClient();
        final var operator = new Operator(k8sClient);
        operator.registerController(new ConfigResourceController(k8sClient));
    }
}
