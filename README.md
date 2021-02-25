---
title: TLS and NIO  
author: vhulot  
date: 2021-02  
---

# TLS and NIO

Socket programming with Java socket IO & NIO.

Features :

- Connect & retrieve certificate from websites.
- Trying different cypher suites. 
- Ping/Pong server over NIO.

Exercise for University.

## Generate key with
```sh
keytool -genkey -alias maclef -keyalg RSA -storetype PKCS12 -keysize 2048 -keypass 123456 -keystore serveurstore.keys -storepass 123456
```

## Connect to SSL server with
```sh
openssl s_client -connect <your-ip>:9999
```
Exemples :
- openssl s_client -connect localhost:9999
- openssl s_client -connect 192.168.1.31:9999
