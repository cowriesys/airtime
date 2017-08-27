import sys
import datetime
import requests
import base64
import hashlib
import hmac
import urllib
import uuid
import time

url = "https://api.cowriesys.com:2443"
clientId = "ClientId"
clientKey = "ClientKey"

def Nonce():
    return datetime.datetime.now().strftime('%Y%m%d%H%M%S%f')

def Sign(nonce, query = None):
    queryString = ""

    if(query != None):
        queryString += "?" + urllib.urlencode(query)

    data = bytes(str(nonce) + queryString).encode()
    secretKeyBytes = base64.b64decode(clientKey)
    return  base64.b64encode(hmac.new(secretKeyBytes, data, digestmod = hashlib.sha256).digest())

def Balance():
    nonce = Nonce()
    signature = Sign(nonce)
    headers = {'ClientId' : clientId, 'Signature' : signature, 'Nonce' : nonce}
    balance = requests.get(url + "/airtime/Balance", headers = headers)
    balance.raise_for_status()
    return balance.text

def Credit(net, msisdn, amount, xref):
    parameters = {'net' : net, 'msisdn' : msisdn, 'amount' : amount, 'xref' : xref }
    nonce = Nonce()
    signature = Sign(nonce, parameters)
    headers = {'ClientId' : clientId, 'Signature' : signature, 'Nonce' : nonce}
    credit = requests.get(url + "/airtime/Credit", headers = headers, params = parameters)
    credit.raise_for_status()
    return credit.text

def Check(reference):
    parameters = {'reference' : reference}
    nonce = Nonce()
    signature = Sign(nonce, parameters)
    headers = {'ClientId' : clientId, 'Signature' : signature, 'Nonce' : nonce}
    check = requests.get(url + "/airtime/Check", headers = headers, params = parameters)
    check.raise_for_status()
    return check.text

net = str(sys.argv[1]) # net can be AIR, ETI, GLO, MTN
msisdn = str(sys.argv[2]) # msisdn in international format 234 prefix
amount =  sys.argv[3] # minimum varies across networks
xref = str(uuid.uuid1()) # any unique reference

print(Balance())
#print(Credit(net, msisdn, amount, xref))
print(Check(xref))