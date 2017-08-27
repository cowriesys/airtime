using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Net;
using Newtonsoft.Json;
using System.Security.Cryptography;
using System.Configuration;

namespace Cisl
{
    public class AirtimeClient
    {
        private String URL = String.Empty;
        private String ClientId = String.Empty;
        private String ClientKey = String.Empty;
        private String TerminalId = "AirtimeClient";

        public AirtimeClient()
        {
            this.URL = ConfigurationManager.AppSettings["URL"];
            this.ClientId = ConfigurationManager.AppSettings["ClientId"];
            this.ClientKey = ConfigurationManager.AppSettings["ClientKey"];
        }

        public AirtimeClient(String URL, String ClientId, String ClientKey)
        {
            this.URL = URL;
            this.ClientId = ClientId;
            this.ClientKey = ClientKey;
        }

        public AirtimeClient(String ClientId, String ClientKey)
        {
            this.URL = ConfigurationManager.AppSettings["URL"];
            this.ClientId = ClientId;
            this.ClientKey = ClientKey;
        }

        public static Network Parse(String net)
        {
            switch (net.ToUpper())
            {
                case "AIR":
                case "AIRTEL":
                    return Network.AIR;

                case "ETI":
                case "ETISALAT":
                    return Network.ETI;

                case "GLO":
                    return Network.GLO;

                case "MTN":
                    return Network.MTN;

                default:
                    throw new Exception("Unknown Network");
            }
        }

        public BalanceResult Balance()
        {
            String Nonce = DateTime.Now.ToString("yyyyMMddHHmmssfff");

            WebClient http = new WebClient();
            String Signature = ComputeSignature(Nonce, http.QueryString);

            http.Headers.Add("ClientId", ClientId);
            http.Headers.Add("Signature", Signature);
            http.Headers.Add("Nonce", Nonce);

            String response = http.DownloadString(URL + "/airtime/Balance");
            BalanceResult result = JsonConvert.DeserializeObject<BalanceResult>(response);
            return result;
        }

        public BalanceResult AgentBalance()
        {
            String Nonce = DateTime.Now.ToString("yyyyMMddHHmmssfff");

            WebClient http = new WebClient();
            String Signature = ComputeSignature(Nonce, http.QueryString);

            http.Headers.Add("AgentId", ClientId);
            http.Headers.Add("Signature", Signature);
            http.Headers.Add("Nonce", Nonce);
            http.Headers.Add("TerminalId", TerminalId);

            String response = http.DownloadString(URL + "/terminal/Balance");
            BalanceResult result = JsonConvert.DeserializeObject<BalanceResult>(response);
            return result;
        }

        public AirtimeResult Credit(Network net, String msisdn, int amount, string xref)
        {
            AirtimeResult result = new AirtimeResult();
            result.ResultCode = "0";
            String Nonce = DateTime.Now.ToString("yyyyMMddHHmmssfff");
            msisdn = Regex.Replace(msisdn, @"(?<PREFIX>^0)(?<TAIL>\d*)", @"234${TAIL}");

            try
            {
                WebClient http = new WebClient();
                http.QueryString.Add("net", net.ToString());
                http.QueryString.Add("msisdn", msisdn);
                http.QueryString.Add("amount", amount.ToString());
                http.QueryString.Add("xref", xref);

                String Signature = ComputeSignature(Nonce, http.QueryString);               

                http.Headers.Add("ClientId", ClientId);
                http.Headers.Add("Signature", Signature);
                http.Headers.Add("Nonce", Nonce);

                String response = http.DownloadString(URL + "/airtime/Credit");
                result = JsonConvert.DeserializeObject<AirtimeResult>(response);
                result.ResultCode = "200";
            }
            catch (WebException wex)
            {
                if (wex.Status == WebExceptionStatus.ProtocolError)
                {
                    HttpStatusCode httpStatus = ((HttpWebResponse)wex.Response).StatusCode;
                    result.ResultCode = httpStatus.ToString();
                    result.message = wex.Message;
                    result.xref = xref;
                }
            }

            return result;
        }

        public AirtimeResult AgentCredit(Network net, String msisdn, int amount, string xref)
        {
            AirtimeResult result = new AirtimeResult();
            result.ResultCode = "0";
            String Nonce = DateTime.Now.ToString("yyyyMMddHHmmssfff");
            msisdn = Regex.Replace(msisdn, @"(?<PREFIX>^0)(?<TAIL>\d*)", @"234${TAIL}");

            try
            {
                WebClient http = new WebClient();
                http.QueryString.Add("net", net.ToString());
                http.QueryString.Add("msisdn", msisdn);
                http.QueryString.Add("amount", amount.ToString());
                http.QueryString.Add("xref", xref);

                String Signature = ComputeSignature(Nonce, http.QueryString);                

                http.Headers.Add("AgentId", ClientId);
                http.Headers.Add("Signature", Signature);
                http.Headers.Add("Nonce", Nonce);
                http.Headers.Add("TerminalId", TerminalId);

                String response = http.DownloadString(URL + "/terminal/Credit");
                result = JsonConvert.DeserializeObject<AirtimeResult>(response);
                result.ResultCode = "200";
            }
            catch (WebException wex)
            {
                if (wex.Status == WebExceptionStatus.ProtocolError)
                {
                    result.ResultCode = ((HttpWebResponse)wex.Response).StatusCode.ToString();
                }
            }

            return result;
        }

        public CheckResult Check(String reference)
        {
            String Nonce = DateTime.Now.ToString("yyyyMMddHHmmssfff");
            String Xref = Guid.NewGuid().ToString("N");

            WebClient http = new WebClient();
            http.QueryString.Add("reference", reference);

            String Signature = ComputeSignature(Nonce, http.QueryString);

            http.Headers.Add("ClientId", ClientId);
            http.Headers.Add("Signature", Signature);
            http.Headers.Add("Nonce", Nonce);

            String response = http.DownloadString(URL + "/airtime/Check");
            CheckResult result = JsonConvert.DeserializeObject<CheckResult>(response);
            return result;
        }

        private string ComputeSignature(String Nonce, NameValueCollection query)
        {
            String signature = String.Empty;
            StringBuilder queryString = new StringBuilder();
            if (query.Count > 0)
            {
                queryString.Append("?");
                queryString.Append(query.GetKey(0));
                queryString.Append("=");
                queryString.Append(query[0]);

                for (int i = 1; i < query.Count; i++)
                {
                    queryString.Append("&");
                    queryString.Append(query.GetKey(i));
                    queryString.Append("=");
                    queryString.Append(query[i]);
                }
            }

            byte[] data = Encoding.UTF8.GetBytes(Nonce + queryString.ToString());
            byte[] secretKeyBytes = Convert.FromBase64String(ClientKey);

            using (HMACSHA256 hmac = new HMACSHA256(secretKeyBytes))
            {
                byte[] signatureBytes = hmac.ComputeHash(data);
                signature = Convert.ToBase64String(signatureBytes);
            }

            return signature;
        }
    }

    public enum Network
    {
        MTN, AIR, GLO, ETI
    }

    public class AirtimeResult
    {
        public String id { get; set; }
        public String xref { get; set; }
        public int balance { get; set; }
        public int charge { get; set; }
        public String message { get; set; }
        public String ResultCode { get; set; }
    }

    public class BalanceResult
    {
        public int balance { get; set; }
        public int discount { get; set; }
    }

    public class CheckResult
    {
        public String id { get; set; }
        public String msisdn { get; set; }
        public String net { get; set; }
        public int amount { get; set; }
        public String xref { get; set; }
        public DateTime stamp { get; set; }
    }
}
