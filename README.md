## mySpring

### Introduction

mySpring is a self-implemented mini Spring framework. Both IOC and AOP are supported.

* Annotation based IOC(support circular dependency), supported annotations can be found [here](https://github.com/123RuiHuang/mySpring/tree/master/mySpring/src/main/java/com/ray/mySpring/annotation)
* Transactional management

### How it is implemented
* implement IOC using java reflection
* implement AOP using java dynamic proxy

### How to use it
* IOC: use annotation the same way of Spring annotation 
* Transactional: support customized transaction logic, all you need to do is implementing the interface TransactionManager and apply the annotation @Transactional to the implemention class
### Code Example
* Please refer to the project [online-payment](https://github.com/123RuiHuang/mySpring/tree/master/online-payment). This project is an online payment website to demonstrate
  the usage of mySpring
