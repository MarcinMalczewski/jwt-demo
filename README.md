## How to generate keys?
```console 
foo@bar:~$ ssh-keygen -t rsa -P "" -b 2048 -m PEM -f jwtRS256-key.pem
foo@bar:~$ openssl rsa -in jwtRS256-key.pem -pubout -outform PEM -out jwtRS256-key.pub
foo@bar:~$ openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in jwtRS256-key.pem -out jwtRS256-key.priv
```
as a result the following files are generated: 

jwtRS256-key.priv (private key)

jwtRS256-key.pub (public key)


