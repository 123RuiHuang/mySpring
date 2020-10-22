## mySpring

### Introduction

mySpring is a self-implemented mini Spring framework. Both IOC and AOP are supported.

* Annotation based IOC(e.g. @Component, @Service, @Autowired)
* Transactional management(@Transactional)

### How it is implemented
* implement IOC using java reflection
* implement AOP using java dynamic proxy

### How to use it
* IOC: use annotation the same way of Spring annotation 
* Transactional: support customized transaction logic, all you need to do is implementing the interface com.ray.payment.service.TransferService  
### Code Example
* Please refer to the project online-payment:https://github.com/123RuiHuang/mySpring/tree/master/online-payment. This project is a dummy online payment demo to demonstrate
  the usage of myspring
