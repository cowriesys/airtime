# Cowrie Integrated Systems Airtime API
High performance airtime/data topup API for Nigerian networks Airtel, Glo, 9Mobile and MTN

Cowrie Integrated Systems Limited is an NCC licensed Value Added Service provider of telecommunication products and services.
Our Airtime REST API enables developers and service providers to dispense airtime/data plans from their applications.
This repository documents the Airtime REST API and contains bindings for the following languages/platforms
* C#
* Java
* Javascript
* PHP
* Phython

## API Structure
The airtime API is based on a HTTP/REST architecture. API clients issue HTTP GET requests with parameters specified in the query string. API responses use standard HTTP response codes with messages encoded in JSON format.

## API Security
Security for the API is enforced through a combination of SSL and HMAC256 signatures. API requests are only accepted over HTTPS.

## Request Authentication
API client requests are authenticated and authorized using a supplied ClientId and ClientKey. For every request, the API client must include the ClientId and sign the request using the ClientKey.

## Computing the Signature
The algorithm used to compute the signature is described as follows 
1. Generate a nonce (it can be any unique string) 
2. Concatenate the nonce and the query string including the question mark “?” like this nonce?net={net}&msisdn={msisdn}&amount={amount}&channel={channel}&xref={xref} 
3. Convert the base64 encoded ClientKey to bytes 
4. Instantiate a SHA256 object from the ClientKey bytes 
5. Compute the SHA256 hash of the concatenated nonce and query string. The result yields the signature. Convert the signature to base64 format 
6. Apply the ClientId, Signature and Nonce as HTTP headers

## API Methods
Name|Description
----|-----------
Balance|Get airtime account balance and discount 
Credit|Credit an amount airtime to a phone number on network 
Data|Credit a data plan to a phone number on network
Check|Get the details of an airtime transaction using a unique identifier

## Balance Request
```
Request URL https://api.cowriesys.com:2443/airtime/Balance 
Request Headers 
ClientId: me@client.com 
Signature: TAP2kgjhhodYUcawFIwsn2GSxjoyVvWWQDZMhHuMFFM= 
Nonce: 20151110202513869
```
## Balance Response
**HTTP 200 OK**
```javascript
{
    balance: "1253CBF19F431784E610",
    discount: 4
}
```

## Credit Request
```
Request URL https://api.cowriesys.com:2443/airtime/Credit?net=AIR&msisdn=2348124661601&amount=100&xref=7734c7da7687442
Request Headers 
ClientId: me@client.com 
Signature: TAP2kgjhhodYUcawFIwsn2GSxjoyVvWWQDZMhHuMFFM= 
Nonce: 20151110202513869
```

## Credit Response
A successful request will return the following JSON encoded response
**HTTP 200 OK**
```javascript
{
    id: "1253CBF19F431784E610",
    xref: "7734c7da7687442",
    message: "OK"
}
```

## Data Request
```
Request URL https://api.cowriesys.com:2443/airtime/Balance 
Request Headers 
ClientId: me@client.com 
Signature: TAP2kgjhhodYUcawFIwsn2GSxjoyVvWWQDZMhHuMFFM= 
Nonce: 20151110202513869
```

## Data Response
A successful request will return the following JSON encoded response
**HTTP 200 OK**
```javascript
{
    id: "1253CBF19F431784E610",
    net: "AIR",
    msisdn: "2348124661601",  
    amount: 100,  
    xref: "7734c7da7687442"
}
```

## Data Plans
Network|Value|Size
-------|-----|----
Airtel|100|30MB
Airtel|200|100MB
Airtel|500|750MB
Airtel|1000|1.5GB
Airtel|2000|3.5GB
Airtel|2500|5GB
Airtel|3500|7GB
Airtel|4000|9GB
Airtel|10000|10GB
Airtel|8000|16GB
Airtel|10000|22GB
Airtel|15000|30GB
Airtel|36000|50GB
Airtel|70000|100GB
Airtel|136000|200GB
Glo|100|70MB
Glo|200|200MB
Glo|500|1.6GB
Glo|1000|3.2GB
Glo|2000|7.5GB
Glo|2500|10GB
Glo|3000|12GB
Glo|4000|16GB
Glo|5000|24GB
Glo|8000|32GB
Glo|10000|46GB
Glo|15000|60GB
Glo|18000|90GB
MTN|100|10MB
MTN|200|500MB
MTN|500|750MB
MTN|1000|1GB
MTN|2000|5GB
MTN|5000|10GB
MTN|10000|22GB
9Mobile|200|150MB
9Mobile|1000|1GB
9Mobile|1200|1.5GB
9Mobile|2000|2.5GB
9Mobile|3000|4GB
9Mobile|8000|11.5GB
9Mobile|10000|15GB
9Mobile|18000|27.5GB
9Mobile|27500|30GB
9Mobile|5500|60GB
9Mobile|84992|100GB
9Mobile|110000|120GB

## Check Request
```
Request URL https://api.cowriesys.com:2443/airtime/Check?reference=7734c7da7687442
Request Headers 
ClientId: me@client.com 
Signature: TAP2kgjhhodYUcawFIwsn2GSxjoyVvWWQDZMhHuMFFM= 
Nonce: 20151110202513869
```

## Check Response
A successful request will return the following JSON encoded response
**HTTP 200 OK**
```javascript
{
    id: "1253CBF19F431784E610",
    net: "AIR",
    msisdn: "2348124661601",  
    amount: 100,  
    xref: "7734c7da7687442"
}
```

## Response Error Codes
A failed request will result in one of the following HTTP response error codes.

HTTP Code|HTTP Status|Description
---------|-----------|------------
400|Bad Request|Signature does not match or one or more query parameters is incorrect 
402|Payment Required|Client account requires payment 
403|Forbidden|One or more required headers are missing
404|Not Found|Network and/or MSISDN are incorrect
409|Conflict|Nonce has been used before 
500|Server Error|The server encountered an error 
