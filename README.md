# Cowrie Integrated Systems Airtime API
High performance airtime/data topup API for Nigerian networks Airtel, Glo, 9Mobile and MTN

Cowrie Integrated Systems Limited is an NCC licensed Value Added Service provider of telecommunication products and services.
Our Airtime REST API enables developers and service providers to dispense airtime/data plans from their applications.
This repository documents the Airtime REST API and contains bindings for the following languages/platforms
* C#
* Java
* Javascript
* PHP
* Python

## API Structure
The airtime API is based on a HTTP/REST architecture. API clients issue HTTP GET requests with parameters specified in the query string. API responses use standard HTTP response codes with messages encoded in JSON format.

## API Security
Security for the API is enforced through a combination of SSL and HMAC256 signatures. API requests are only accepted over HTTPS.

## Request Authentication
API client requests are authenticated and authorized using a supplied ClientId and ClientKey. For every request, the API client must include the ClientId and sign the request using the ClientKey.

## Computing the Signature
The algorithm used to compute the signature is described as follows 
1. Generate a nonce (it can be any unique string) 
2. Concatenate the nonce and the query string including the question mark "?"
`{nonce}?net={net}&msisdn={msisdn}&amount={amount}&channel={channel}&xref={xref}`
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
Request URL
https://api.cowriesys.com:2443/airtime/Balance

Request Headers 
ClientId: me@client.com 
Signature: TAP2kgjhhodYUcawFIwsn2GSxjoyVvWWQDZMhHuMFFM= 
Nonce: 20151110202513869
```

## Balance Response
A successful request will return the following JSON encoded response

**HTTP 200 OK**
```javascript
{
    balance: "1253CBF19F431784E610",
    discount: 4
}
```

## Credit Request
```
Request URL
https://api.cowriesys.com:2443/airtime/Credit?net=AIR&msisdn=2348124661601&amount=100&xref=7734c7da7687442

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
Request URL
https://api.cowriesys.com:2443/data/Credit?net=AIR&msisdn=2348124661601&amount=100&xref=7734c7da7687442

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

## Network Codes
The `net` request parameter for the Credit and Data API methods should be set according to this table

Network|Code
-------|----
Airtel|AIR
Glo|GLO
MTN|MTN
9Mobile|ETI

## Data Plans
The following data plans are available

### Airtel
Value|Size
-----|----
100|30MB
200|100MB
500|750MB
1,000|1.5GB
2,000|3.5GB
2,500|5GB
3,500|7GB
4,000|9GB
5,000|10GB
8,000|16GB
10,000|22GB
15,000|30GB
36,000|50GB
70,000|100GB
136,000|200GB

### Glo
100|70MB
200|200MB
500|1.6GB
1,000|3.2GB
2,000|7.5GB
2,500|10GB
3,000|12GB
4,000|16GB
5,000|24GB
8,000|32GB
10,000|46GB
15,000|60GB
18,000|90GB

###MTN
100|10MB
200|500MB
500|750MB
1,000|1GB
2000|5GB
5,000|10GB
10,000|22GB

###9Mobile
200|150MB
1,000|1GB
1,200|1.5GB
2,000|2.5GB
3,000|4GB
8,000|11.5GB
10,000|15GB
18,000|27.5GB
27,500|30GB
55,500|60GB
84,992|100GB
110,000|120GB

## Check Request
```
Request URL
https://api.cowriesys.com:2443/airtime/Check?reference=7734c7da7687442

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
