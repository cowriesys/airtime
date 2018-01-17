# Cowrie Integrated Systems Airtime API
High performance airtime/data topup API and network agnostic logical PINs for Nigerian networks Airtel, Glo, 9Mobile and MTN

Cowrie Integrated Systems Limited is an NCC licensed Value Added Service provider of telecommunication products and services.
Our Airtime REST API enables developers and service providers to dispense airtime/data plans/logical PINs from their applications.
Send a request to [info@cowriesys.com](mailto:info@cowriesys.com) to signup for an account.

This repository documents the Airtime REST API and contains bindings for the following languages/platforms
* [C#](https://github.com/cowriesys/airtime/tree/master/cs)
* [Java](https://github.com/cowriesys/airtime/tree/master/java)
* [Javascript](https://github.com/cowriesys/airtime/tree/master/js)
* [PHP](https://github.com/cowriesys/airtime/tree/master/php)
* [Python](https://github.com/cowriesys/airtime/tree/master/python)

To start using the API immeadiately, download the code samples for your chosen platform.
The remainder of this document provides the specification for the API and describes how it is implemented.

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
[Balance](#balance-request)|Get airtime account balance and discount 
[Credit](#credit-request)|Credit an amount airtime to a phone number on network 
[Data](#data-request)|Credit a data plan to a phone number on network
[Check](#check-request)|Get the details of an airtime transaction using a unique identifier
[AllocateSingle](#allocatesingle-pin-request)|Allocate a single voucher worth an amount

## Balance Request
```
Request URL
https://api.cowriesys.com/airtime/Balance

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
    balance: 100000,
    discount: 4
}
```

## Credit Request
```
Request URL
https://api.cowriesys.com/airtime/Credit?net=AIR&msisdn=2348124661601&amount=100&xref=7734c7da7687442

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
https://api.cowriesys.com/data/Credit?net=AIR&msisdn=2348124661601&amount=100&xref=7734c7da7687442

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
Value|Size|Validity
-----|----|--------
100|30MB|1 day
200|100MB|3 days
500|750MB|2 weeks
1,000|1.5GB|1 month
2,000|3.5GB|1 month
2,500|5GB|1 month
3,500|7GB|1 month
4,000|9GB|1 month
5,000|10GB|1 month
8,000|16GB|1 month
10,000|22GB|1 month
15,000|30GB|2 months
36,000|50GB|6 months
70,000|100GB|1 year
136,000|200GB|1 year

### Glo
Value|Size|Validity
-----|----|--------
100|70MB|1 day
200|200MB|5 days
500|1.6GB|10 days
1,000|3.2GB|1 month
2,000|7.5GB|1 month
2,500|10GB|1 month
3,000|12GB|1 month
4,000|16GB|1 month
5,000|24GB|1 month
8,000|32GB|1 month
10,000|46GB|1 month
15,000|60GB|1 month
18,000|90GB|1 month

### MTN
Value|Size|Validity
-----|----|--------
100|10MB|1 day
200|500MB|1 day
500|750MB|1 week
1,000|1GB|1 month
2000|5GB|1 month
5,000|10GB|1 month
10,000|22GB|1 month

### 9Mobile
Value|Size|Validity
-----|----|--------
200|150MB|1 week
1,000|1GB|1 month
1,200|1.5GB|1 month
2,000|2.5GB|1 month
3,000|4GB|1 month
8,000|11.5GB|1 month
10,000|15GB|1 month
18,000|27.5GB|1 month
27,500|30GB|3 months
55,500|60GB|6 months
84,992|100GB|6 months
110,000|120GB|1 year


## Check Request
```
Request URL
https://api.cowriesys.com/airtime/Check?reference=7734c7da7687442

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
    xref: "7734c7da7687442",
    status: "OK"
}
```

## AllocateSingle Pin Request
```
Request URL
https://api.cowriesys.com/pin/AllocateSingle?unit=110&type=AIRTIME&message=HappyNewYear&xref=20180101215933289
```

## AllocateSingle Pin Response
```javascript
{
    "serial": "97054",
    "pin": "101436786399",
    "unit": 110,
    "fee": 0,
    "type": "AIRTIME",
    "xref": "20180101215933289",
    "message": "HappyNewYear"
}
```

## Response Error Codes
A failed request will result in one of the following HTTP response error codes.

HTTP Code|HTTP Status|Description
---------|-----------|------------
400|Bad Request|Signature does not match or one or more query parameters is incorrect 
402|Payment Required|Insufficient balance, client account requires payment 
403|Forbidden|One or more required headers are missing
404|Not Found|Network and/or MSISDN are incorrect
409|Conflict|Nonce has been used before 
500|Server Error|The server encountered an error 
