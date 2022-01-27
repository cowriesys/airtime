const CryptoJS = require('crypto-js');
var fetch = require('node-fetch');

const URL = 'https://api.cowriesys.com';
const ClientId = 'ClientId';
const ClientKey = 'ClientKey';

module.exports = {

	sign: function(key, nonce, message) {
		var hmac = CryptoJS.HmacSHA256(nonce + message, CryptoJS.enc.Base64.parse(key));
		var digest = CryptoJS.enc.Base64.stringify(hmac);
		return digest;
	},

	nonce: function () {
		return new Date().getTime();
	},

	balance: function() {
		
		var api = URL + '/airtime/Balance';
		var nonce = this.nonce();
		var signature = this.sign(ClientKey, nonce, '');

		console.log('fetching: ' + api + ' ClientId: ' + ClientId + ' nonce: ' + nonce + ' signature: ' + signature);

		var response = fetch(api, {
			headers: {
				'Content-Type': 'application/json',
				'ClientId': ClientId,
				'Nonce': nonce,
				'Signature': signature
			}
		}).then(function (response) {

			if (!response.ok) {
				throw 'HTTP error: ' + response.status;
			}
			return response.json();

		}).then(function (result) {
			console.log('received: balance: ' + result.balance + ' discount: ' + result.discount);

		}).catch(function(error) {
			console.log(error);
		});
	},

	credit: function(net, msisdn, amount, xref) {
		
		var api = URL + '/airtime/Credit?net='+net
                                              +'&msisdn='+msisdn
                                              +'&amount='+amount
                                              +'&xref='+xref;
		var nonce = this.nonce();
		var signature = this.sign(ClientKey, nonce, api.substring(api.indexOf('?')));

		console.log('fetching: ' + api + ' ClientId: ' + ClientId + ' nonce: ' + nonce + ' signature: ' + signature);

		var response = fetch(api, {
			headers: {
				'Content-Type': 'application/json',
				'ClientId': ClientId,
				'Nonce': nonce,
				'Signature': signature
			}
		}).then(function (response) {

			if (!response.ok) {
				throw 'HTTP error: ' + response.status;
			}
			return response.json();

		}).then(function (result) {
			console.log('received: balance: ' + result.balance + ' charge: ' + result.charge);

		}).catch(function(error) {
			console.log(error);
		});
	},

	dataCredit: function(net, msisdn, amount, xref) {
		
		var api = URL + '/data/Credit?net='+net
                                              +'&msisdn='+msisdn
                                              +'&amount='+amount
                                              +'&xref='+xref;
		var nonce = this.nonce();
		var signature = this.sign(ClientKey, nonce, api.substring(api.indexOf('?')));

		console.log('fetching: ' + api + ' ClientId: ' + ClientId + ' nonce: ' + nonce + ' signature: ' + signature);

		var response = fetch(api, {
			headers: {
				'Content-Type': 'application/json',
				'ClientId': ClientId,
				'Nonce': nonce,
				'Signature': signature
			}
		}).then(function (response) {

			if (!response.ok) {
				throw 'HTTP error: ' + response.status;
			}
			return response.json();

		}).then(function (result) {
			console.log('received: balance: ' + result.balance + ' charge: ' + result.charge);

		}).catch(function(error) {
			console.log(error);
		});
	},

	check: function(xref) {
		
		var api = URL + '/airtime/Check?reference='+xref;
		var nonce = this.nonce();
		var signature = this.sign(ClientKey, nonce, api.substring(api.indexOf('?')));

		console.log('fetching: ' + api + ' ClientId: ' + ClientId + ' nonce: ' + nonce + ' signature: ' + signature);

		var response = fetch(api, {
			headers: {
				'Content-Type': 'application/json',
				'ClientId': ClientId,
				'Nonce': nonce,
				'Signature': signature
			}
		}).then(function (response) {

			if (!response.ok) {
				throw 'HTTP error: ' + response.status;
			}
			return response.json();

		}).then(function (result) {
			console.log('received: id: ' + result.id + ' xref: ' + result.xref + ' net: ' + result.net + ' msisdn: ' + result.msisdn + ' amount: ' + result.amount + ' stamp: ' + result.stamp);

		}).catch(function(error) {
			console.log(error);
		});
	},

	allocateSingle: function(unit, type, message, xref) {
		
		var api = URL + '/pin/AllocateSingle?unit='+unit
											+'&type='+type
											+'&message='+message
											+'&xref='+xref;
		var nonce = this.nonce();
		var signature = this.sign(ClientKey, nonce, api.substring(api.indexOf('?')));

		console.log('fetching: ' + api + ' ClientId: ' + ClientId + ' nonce: ' + nonce + ' signature: ' + signature);

		var response = fetch(api, {
			headers: {
				'Content-Type': 'application/json',
				'ClientId': ClientId,
				'Nonce': nonce,
				'Signature': signature
			}
		}).then(function (response) {

			if (!response.ok) {
				throw 'HTTP error: ' + response.status;
			}
			return response.json();

		}).then(function (result) {
			console.log('received: pin: ' + result.pin + ' serial: ' + result.serial + ' unit: ' + result.unit + ' type: ' + result.type + ' message: ' + result.message + ' xref: ' + result.xref);

		}).catch(function(error) {
			console.log(error);
		});
	}
}
