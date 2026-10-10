# Cowrie Integrated Systems Airtime API
High performance airtime/data topup API and network agnostic logical PINs for Nigerian networks Airtel, Glo, T2mobile and MTN

Cowrie Integrated Systems Limited is an NCC licensed Value Added Service provider of telecommunication products and services.
Our Airtime REST API enables developers and service providers to dispense airtime/data plans/logical PINs from their applications.
Send a request to [vas@cowriesys.com](mailto:vas@cowriesys.com) to signup for an account.

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
https://api.cowriesys.com/data/Credit?net=AIR&msisdn=2348124661601&amount=350&xref=7734c7da7687442&bundle=500mb1d

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
    amount: 350,  
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
T2mobile|ETI

## Data Plans
The following data plans are available

### Airtel
| Bundle | Price | Size | Validity | Name | Description |
|---|---|---|---|---|---|
| 250mb1d | 50 | 250MB | 1 night | Night 250MB | Night plan. Valid midnight to 5AM only |
| 75mb1d | 75 | 75MB | 1 day | Daily 75MB | Daily plan |
| 110mb1d | 100 | 110MB | 1 day | Daily 110MB | Daily plan |
| 230mb2d | 200 | 230MB | 2 days | 2-Day 230MB | 2-day plan |
| 300mb2d | 300 | 300MB | 2 days | 2-Day 300MB | 2-day plan |
| 500mb1d | 350 | 500MB | 1 day | Daily 500MB | Daily plan |
| 500mb7d | 500 | 500MB | 7 days | Weekly 500MB | Weekly plan |
| 1gb7d | 800 | 1GB | 7 days | Weekly 1GB | Weekly plan |
| 1.5gb7d | 1,000 | 1.5GB | 7 days | Weekly 1.5GB | Weekly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 4gb7d | 1,500 | 4GB | 7 days | Weekly 4GB | Weekly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 6gb7d | 2,000 | 6GB | 7 days | Weekly 6GB | Weekly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 8gb7d | 2,500 | 8GB | 7 days | Weekly 8GB | Weekly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 10gb7d | 3,000 | 10GB | 7 days | Weekly 10GB | Weekly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 20gb7d | 5,000 | 20GB | 7 days | Weekly 20GB | Weekly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 2gb30d | 1,500 | 2GB | 30 days | Monthly 2GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 3gb30d | 2,000 | 3GB | 30 days | Monthly 3GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 4gb30d | 2,500 | 4GB | 30 days | Monthly 4GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 8gb30d | 3,000 | 8GB | 30 days | Monthly 8GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 10gb30d | 4,000 | 10GB | 30 days | Monthly 10GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 13gb30d | 5,000 | 13GB | 30 days | Monthly 13GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 18gb30d | 6,000 | 18GB | 30 days | Monthly 18GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 25gb30d | 8,000 | 25GB | 30 days | Monthly 25GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 35gb30d | 10,000 | 35GB | 30 days | Monthly 35GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 60gb30d | 15,000 | 60GB | 30 days | Monthly 60GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 100gb30d | 20,000 | 100GB | 30 days | Monthly 100GB | Monthly plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 160gb30d | 30,000 | 160GB | 30 days | Monthly 160GB | Monthly plan |
| 210gb30d | 40,000 | 210GB | 30 days | Monthly 210GB | Monthly plan |
| 300gb3m | 50,000 | 300GB | 90 days | 3-Month 300GB | Long-term plan |
| 350gb4m | 60,000 | 350GB | 120 days | 4-Month 350GB | Long-term plan |
| 685gb1y | 100,000 | 685GB | 365 days | Yearly 685GB | Long-term plan |
| s200mb2d | 100 | 200MB | 2 days | Social 200MB | Social plan. WhatsApp, Facebook, Instagram, YouTube & TikTok only |
| s1gb3d | 300 | 1GB | 3 days | Social 1GB | Social plan. WhatsApp, Facebook, Instagram, YouTube & TikTok only |
| s1.5gb7d | 500 | 1.5GB | 7 days | Social 1.5GB | Social plan. WhatsApp, Facebook, Instagram, YouTube & TikTok only |
| 1gb1d | 500 | 1GB | 1 day | Binge 1GB | Binge plan |
| 2gb2d | 600 | 2GB | 2 days | Binge 2GB | Binge plan. Includes 1GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 3gb2d | 750 | 3GB | 2 days | Binge 3GB | Binge plan. Includes 1GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 4gb2d | 1,000 | 4GB | 2 days | Binge 4GB | Binge plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |
| 6gb2d | 1,500 | 6GB | 2 days | Binge 6GB | Binge plan. Includes 2GB YouTube night + 200MB for YouTube, Instagram & TikTok |

### Glo
| Bundle | Price | Size | Validity | Name | Description |
|---|---|---|---|---|---|
| 120mb1d | 100 | 120MB | 1 day | Daily N100 | Daily bundle. 120MB + 5MB night (125MB total) |
| 250mb2d | 200 | 250MB | 2 days | Daily N200 | Daily bundle. 250MB + 25MB night (275MB total) |
| 550mb7d | 500 | 550MB | 7 days | Weekly N500 | Weekly bundle. 550MB + 1GB night (1.5GB total) |
| 1.7gb7d | 1,000 | 1.7GB | 7 days | Weekly N1000 | Weekly bundle. 1.7GB + 2GB night (3.7GB total) |
| 4gb7d | 1,500 | 4GB | 7 days | Weekly N1500 | Weekly bundle. 4GB + 2GB night (6GB total) |
| 6.5gb7d | 2,000 | 6.5GB | 7 days | Weekly N2000 | Weekly bundle. 6.5GB + 2.5GB night (9GB total) |
| 22gb7d | 5,000 | 22GB | 7 days | Weekly N5000 | Weekly bundle. 22GB + 2.5GB night (24.5GB total) |
| 2.2gb30d | 1,500 | 2.2GB | 30 days | Monthly N1500 | Monthly bundle. 2.2GB + 3GB night (5.2GB total) |
| 3.25gb30d | 2,000 | 3.25GB | 30 days | Monthly N2000 | Monthly bundle. 3.25GB + 3GB night (6.25GB total) |
| 8.5gb30d | 3,000 | 8.5GB | 30 days | Monthly N3000 | Monthly bundle. 8.5GB + 2GB night (10.5GB total) |
| 14.5gb30d | 5,000 | 14.5GB | 30 days | Monthly N5000 | Monthly bundle. 14.5GB + 2GB night (16.5GB total) |
| 38gb30d | 10,000 | 38GB | 30 days | Monthly N10000 | Monthly bundle. 38GB + 4GB night (42GB total) |
| 240mb1d | 100 | 240MB | 1 day | Campus Booster N100 | Campus Booster. On campus: 240MB + 5MB night (245MB total). Off campus: 120MB + 5MB night (125MB total) |
| 500mb2d | 200 | 500MB | 2 days | Campus Booster N200 | Campus Booster. On campus: 500MB + 25MB night (525MB total). Off campus: 250MB + 25MB night (275MB total) |
| 1.1gb7d | 500 | 1.1GB | 7 days | Campus Booster N500 | Campus Booster. On campus: 1.1GB + 1GB night (2.1GB total). Off campus: 550MB + 1GB night (1.5GB total) |
| c2.2gb30d | 1,000 | 2.2GB | 30 days | Campus Booster N1000 | Campus Booster. On campus: 2.2GB + 2GB night (4.2GB total). Off campus: 1.1GB + 1.5GB night (2.6GB total) |
| 6.5gb30d | 2,000 | 6.5GB | 30 days | Campus Booster N2000 | Campus Booster. On campus: 6.5GB + 3.5GB night (10GB total). Off campus: 3.25GB + 3GB night (6.25GB total) |
| 29gb30d | 5,000 | 29GB | 30 days | Campus Booster N5000 | Campus Booster. On campus: 29GB + 3GB night (32GB total). Off campus: 14.5GB + 2GB night (16.5GB total) |

### MTN
| Bundle | Price | Size | Validity | Name | Description |
|---|---|---|---|---|---|
| 110mb1d | 100 | 110MB | 1 day | Daily 110MB | Daily plan. SMS 104 to 312 |
| 230mb1d | 200 | 230MB | 1 day | Daily 230MB | Daily plan |
| 500mb1d | 350 | 500MB | 1 day | Daily 500MB | Daily plan. SMS 175 to 312 |
| 1gb1d | 500 | 1GB | 1 day | Daily 1GB + 1.5 Mins | Daily plan. Includes 1.5 minutes talk time. SMS 155 to 312 |
| 2.5gb1d | 750 | 2.5GB | 1 day | Daily 2.5GB | Daily plan. Dial *312*1*1# |
| 3.5gb1d | 1,000 | 3.5GB | 1 day | Daily 3.5GB | Daily plan. SMS 191 to 312 |
| 1.5gb2d | 600 | 1.5GB | 2 days | 2-Day 1.5GB | 2-day plan. Includes 100MB YouTube all day. SMS 199 to 312 |
| 2gb2d | 750 | 2GB | 2 days | 2-Day 2GB | 2-day plan. SMS 154 to 312 |
| 2.5gb2d | 900 | 2.5GB | 2 days | 2-Day 2.5GB | 2-day plan. SMS 146 to 312 |
| 3.2gb2d | 1,000 | 3.2GB | 2 days | 2-Day 3.2GB | 2-day plan. SMS 180 to 312 |
| 4gb2d | 1,200 | 4GB | 2 days | 2-Day 4GB | 2-day plan. SMS 192 to 312 |
| 5.5gb2d | 1,500 | 5.5GB | 2 days | 2-Day 5.5GB | 2-day plan. SMS 193 to 312 |
| 7gb2d | 1,800 | 7GB | 2 days | 2-Day 7GB | 2-day plan. SMS 183 to 312 |
| 500mb7d | 500 | 500MB | 7 days | Weekly 500MB | Weekly plan. Includes 100MB YouTube all day + 1GB YouTube night. SMS 103 to 312 |
| 1gb7d | 800 | 1GB | 7 days | Weekly 1GB | Weekly plan. Includes 100MB YouTube all day + 1GB YouTube night. SMS 142 to 312 |
| 1.5gb7d | 1,000 | 1.5GB | 7 days | Weekly 1.5GB | Weekly plan. Includes 100MB YouTube all day + 1GB YouTube night. SMS 105 to 312 |
| 3.5gb7d | 1,500 | 3.5GB | 7 days | Weekly 3.5GB | Weekly plan. SMS 176 to 312 |
| 6gb7d | 2,500 | 6GB | 7 days | Weekly 6GB | Weekly plan. SMS 143 to 312 |
| 11gb7d | 3,500 | 11GB | 7 days | Weekly 11GB | Weekly plan. SMS 181 to 312 |
| 15gb7d | 4,000 | 15GB | 7 days | Weekly 15GB | Weekly plan. Prepaid customers only. SMS 194 to 312 |
| 20gb7d | 5,000 | 20GB | 7 days | Weekly 20GB | Weekly plan. Digital channels only |
| 12.5gb14d | 4,500 | 12.5GB | 14 days | 2-Weeks 12.5GB | 2-week plan. SMS 195 to 312 |
| 18gb14d | 6,000 | 18GB | 14 days | 2-Weeks 18GB | 2-week plan. SMS 196 to 312 |
| 28gb14d | 8,000 | 28GB | 14 days | 2-Weeks 28GB | 2-week plan. SMS 197 to 312 |
| 40gb14d | 10,000 | 40GB | 14 days | 2-Weeks 40GB | 2-week plan. SMS 198 to 312 |
| 2gb30d | 1,500 | 2GB | 30 days | Monthly 2GB + 2 Mins | Monthly plan. Includes 2 mins talk time, 200MB YouTube all day, 2GB all-night streaming. SMS 106 to 312 |
| 2.7gb30d | 2,000 | 2.7GB | 30 days | Monthly 2.7GB + 2 Mins | Monthly plan. Includes 2 mins talk time, 200MB YouTube all day, 2GB all-night streaming. SMS 130 to 312 |
| 3.5gb30d | 2,500 | 3.5GB | 30 days | Monthly 3.5GB + 5 Mins | Monthly plan. Includes 5 mins talk time, 200MB YouTube all day, 2GB all-night streaming. SMS 131 to 312 |
| 7gb30d | 3,500 | 7GB | 30 days | Monthly 7GB | Monthly plan. Includes 2GB all-night streaming. SMS 110 to 312 |
| 10gb30d | 4,500 | 10GB | 30 days | Monthly 10GB + 10 Mins | Monthly plan. Includes 10 mins talk time, 200MB YouTube all day, 2GB all-night streaming. Roaming eligible. SMS 147 to 312 |
| 12.5gb30d | 5,500 | 12.5GB | 30 days | Monthly 12.5GB | Monthly plan. Includes 300MB YouTube all day, 2GB all-night streaming. Roaming eligible. SMS 148 to 312 |
| 16.5gb30d | 6,500 | 16.5GB | 30 days | Monthly 16.5GB + 10 Mins | Monthly plan. Includes 10 mins talk time, 300MB YouTube all day, 2GB all-night streaming. Roaming eligible. SMS 107 to 312 |
| 20gb30d | 7,500 | 20GB | 30 days | Monthly 20GB | Monthly plan. Includes 4GB all-night streaming. SMS 116 to 312 |
| 25gb30d | 9,000 | 25GB | 30 days | Monthly 25GB | Monthly plan. Includes 4GB all-night streaming. SMS 153 to 312 |
| 36gb30d | 11,000 | 36GB | 30 days | Monthly 36GB | Monthly plan. Roaming eligible. SMS 117 to 312 |
| 65gb30d | 16,000 | 65GB | 30 days | Monthly 65GB | Monthly plan. SMS 178 to 312 |
| 75gb30d | 18,000 | 75GB | 30 days | Monthly 75GB | Monthly plan. Roaming eligible. SMS 150 to 312 |
| 165gb30d | 35,000 | 165GB | 30 days | Monthly 165GB | Monthly plan. SMS 149 to 312 |
| 150gb2m | 40,000 | 150GB | 60 days | 2-Month 150GB | 2-month plan. Roaming eligible. SMS 118 to 312 |
| 480gb3m | 90,000 | 480GB | 90 days | 3-Month 480GB | 3-month plan. SMS 133 to 312 |

### T2mobile
| Bundle | Price | Size | Validity | Name | Description |
|---|---|---|---|---|---|
| 40mb1d | 50 | 40MB | 1 day | Daily 40MB | 40MB Daily @ N50. Dial *312# |
| 83mb1d | 100 | 83MB | 1 day | Daily 83MB + 50MB Social | 83MB + 50MB Social Daily @ N100. Dial *312# |
| 150mb1d | 150 | 150MB | 1 day | Daily 150MB + 100MB Night Data | 150MB + 100MB Night Daily @ N150. Dial *312# |
| 250mb1d | 200 | 250MB | 1 day | Daily 250MB | 250MB Anytime Data Plan Daily @ N200. Dial *312# |
| 650mb7d | 500 | 650MB | 7 days | Weekly 650MB + 100MB Socials | 650MB +100MB Social Weekly @ N500. Dial *312# |
| 2gb30d | 1,000 | 2GB | 30 days | Monthly 2GB | 2GB Anytime Data Plan Monthly @ N1000. Dial *312# |
| 2.3gb30d | 1,200 | 2.3GB | 30 days | Monthly 2.3GB | 2.3GB Anytime Data Plan Monthly @ N1200. Dial *312# |
| 3.4gb7d | 1,500 | 3.4GB | 7 days | Weekly 3.4GB | 3.4GB Special Data Plan Weekly @ N1500. Dial *312# |
| 4.5gb30d | 2,000 | 4.5GB | 30 days | Monthly 4.5GB | 4.5GB Anytime Data Plan Monthly @ N2000. Dial *312# |
| 5.2gb30d | 2,500 | 5.2GB | 30 days | Monthly 5.2GB | 5.2GB Anytime Data Plan Monthly @ N2500. Dial *312# |
| 6.2gb30d | 3,000 | 6.2GB | 30 days | Monthly 6.2GB | 6.2GB Anytime Data Plan Monthly @ N3000. Dial *312# |
| 8.4gb30d | 4,000 | 8.4GB | 30 days | Monthly 8.4GB | 8.4GB Anytime Data Plan Monthly @ N4000. Dial *312# |
| 11.4gb30d | 5,000 | 11.4GB | 30 days | Monthly 11.4GB | 11.4GB Anytime Data Plan Monthly @ N5000. Dial *312# |
| 23gb30d | 10,000 | 23GB | 30 days | Monthly 23GB | 23GB Monthly @ N10,000. Dial *312# |
| 35gb30d | 15,000 | 35GB | 30 days | Monthly 35GB | 35GB Monthly @ N15,000. Dial *312# |
| 47gb30d | 20,000 | 47GB | 30 days | Monthly 47GB | 47GB Monthly @ N20,000. Dial *312# |
| 118gb30d | 50,000 | 118GB | 30 days | Monthly 118GB | 118GB Monthly @ N50,000. Dial *312# |
| 25mb7d | 25 | 25MB | 7 days | Facebook Weekly | Social bundle. Facebook only. Dial *229*1*2# |
| 80mb30d | 80 | 80MB | 30 days | Facebook Monthly | Social bundle. Facebook only. Dial *229*1*3# |

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
https://api.cowriesys.com/pin/AllocateSingle?unit=110&type=AIRTIME&message=HappyNewYear&xref=25247c969d8046e5b8554da08a4d0fd7
```

## AllocateSingle Pin Response
A successful request will return the following JSON encoded response

**HTTP 200 OK**
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

## AllocateBatch Pin Request
```
Request URL
https://api.cowriesys.com/pin/Allocate?unit=210&count=2&type=AIRTIME&message=HappyNewYear&xref=42e54bb1241d4c31a8bc6745ab5fedad
```

## AllocateBatch Pin Response
A successful request will return the following JSON encoded response

**HTTP 200 OK**
```javascript
[
    {
        "serial": "97055",
        "pin": "101436786399",
        "unit": 210,
        "fee": 0,
        "type": "AIRTIME",
        "xref": "42e54bb1241d4c31a8bc6745ab5fedad",
        "message": "HappyNewYear"
    },
    {
        "serial": "97056",
        "pin": "2135096597208",
        "unit": 210,
        "fee": 0,
        "type": "AIRTIME",
        "xref": "42e54bb1241d4c31a8bc6745ab5fedad",
        "message": "HappyNewYear"
    }
]
```

## Redeem Pin Request
```
Request URL
https://api.cowriesys.com/pin/Redeem?net=AIR&msisdn=2348124661601&pin=101436786399&xref=f85c362677ba4c8fa1e6613190ee8c69

Request Headers 
ClientId: me@client.com 
Signature: TAP2kgjhhodYUcawFIwsn2GSxjoyVvWWQDZMhHuMFFM= 
Nonce: 20151110202513869
```

## Redeem Pin Response
A successful request will return the following JSON encoded response

**HTTP 200 OK**
```javascript
{
    message: "Topup complete"
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
