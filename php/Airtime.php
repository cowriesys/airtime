<?php
class Airtime {

	private $url ='https://api.cowriesys.com:2443';
	private $clientId = 'ClientId';
	private $clientKey = 'ClientKey';
	private $terminalId = '0002';

	function sign($message) {
		return base64_encode(hash_hmac('sha256', $message, base64_decode($this->clientKey), true));
	}

	public function balance() {
		$api = '/airtime/Balance';
		$nonce = uniqid();
		$queryString = '';

		$signature = $this->sign($nonce.$queryString);

		$request = curl_init($this->url.$api.$queryString);
		curl_setopt($request, CURLOPT_HTTPHEADER, array('ClientId: '.$this->clientId,
		                                                'Signature: '.$signature,
		                                                'Nonce: '.$nonce));
		$result = curl_exec($request);

		if (!$result) {
		    die('Error: "' . curl_error($request) . '" - Code: ' . curl_errno($request));
		}

		echo($result);
		curl_close($request);
	}

	public function credit($net, $msisdn, $amount) {
		$api = '/airtime/Credit';
		$nonce = uniqid();
		$queryString = '?net='.$net.'&msisdn='.$msisdn.'&amount='.$amount.'&xref='.$nonce;

		$signature = $this->sign($nonce.$queryString);

		$request = curl_init($this->url.$api.$queryString);
		curl_setopt($request, CURLOPT_HTTPHEADER, array('ClientId: '.$this->clientId,
		                                                'Signature: '.$signature,
		                                                'Nonce: '.$nonce));
		$result = curl_exec($request);

		if (!$result) {
		    die('Error: "' . curl_error($request) . '" - Code: ' . curl_errno($request));
		}

		echo($result);
		curl_close($request);
	}

	public function agent_balance() {
		$api = '/terminal/Balance';
		$nonce = uniqid();
		$queryString = '';

		$signature = $this->sign($nonce.$queryString);

		$request = curl_init($this->url.$api.$queryString);
		curl_setopt($request, CURLOPT_HTTPHEADER, array('AgentId: '.$this->clientId,
		                                                'Signature: '.$signature,
		                                                'Nonce: '.$nonce,
														'TerminalId: '.$this->terminalId));
		$result = curl_exec($request);

		if (!$result) {
		    die('Error: "' . curl_error($request) . '" - Code: ' . curl_errno($request));
		}

		echo($result);
		curl_close($request);
	}

	public function agent_credit($net, $msisdn, $amount) {
		$api = '/terminal/Credit';
		$nonce = uniqid();
		$queryString = '?net='.$net.'&msisdn='.$msisdn.'&amount='.$amount.'&xref='.$nonce;

		$signature = $this->sign($nonce.$queryString);

		$request = curl_init($this->url.$api.$queryString);
		curl_setopt($request, CURLOPT_HTTPHEADER, array('AgentId: '.$this->clientId,
		                                                'Signature: '.$signature,
		                                                'Nonce: '.$nonce,
														'TerminalId: '.$this->terminalId));
		$result = curl_exec($request);

		if (!$result) {
		    die('Error: "' . curl_error($request) . '" - Code: ' . curl_errno($request));
		}

		echo($result);
		curl_close($request);
	}
}

$airtime = new Airtime();
$airtime->agent_balance();
//$airtime->agent_credit('mtn', '2347038662037', 100);
?>