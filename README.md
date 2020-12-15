ConfigME
========
A Kubernetes operator to load the configmap data from external services.
There are situations where services in a Kubernetes cluster may need data provided by different datasources 
like HTTP-API, files in a GitServer or kafka topic to name a few.

If the data is cacheable, mapping them to a ConfigMap would contribute to the resiliency of the service as it wouldn't be dependent to the availability of the other service.

## Alternatives:

### caching the data in every pod

Not to mention that while the external service is down your service can't scale or the pods being respawned. 

### A central Cache like Redis, Memcached

Well, this solution is already better than the previous one... 
but really? you want to spend your resoures to implement a thing identical to what K8s already provides?

 

## TODO

- [X] Handle http errors may happen
- [X] Native image build
- [x] Docker Image
- [ ] Different Authentications methods
  - [x] none
  - [x] bearer
  - [ ] basic
  - [ ] api-key
  - ...
- [ ] Schema support for fetched configuration
- [ ] How to use document


