using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;

namespace Cisl
{
    class Program
    {
        static void Main(string[] args)
        {
            String URL = ConfigurationManager.AppSettings["URL"];
            String ClientId = ConfigurationManager.AppSettings["ClientId"];
            String ClienKey = ConfigurationManager.AppSettings["ClientKey"];

            AirtimeClient airtime = new AirtimeClient(URL, ClientId, ClienKey);
            Network net = AirtimeClient.Parse(args[0]);
            String msisdn = args[1];
            int amount = int.Parse(args[2]);

            try
            {
                BalanceResult balance = airtime.Balance();
                Console.WriteLine("balance: {0} discount: {1}%", balance.balance, balance.discount);

                String xref = Guid.NewGuid().ToString("N");
                AirtimeResult credit = airtime.Credit(net, msisdn, amount, xref);
                Console.WriteLine("credit id: {0} xref: {1} message: {2}", credit.id, credit.xref, credit.message);

                // Gice a few seconds for the databases to sync
                // System.Threading.Thread.Sleep(1000);

                CheckResult check = airtime.Check(xref);
                if (check != null)
                {
                    Console.WriteLine("check id: {0} net: {1} msisdn: {2} amount: {3} xref: {4}", check.id, check.net, check.msisdn, check.amount, check.xref);
                }
            }
            catch (System.Net.WebException wex)
            {
                Console.WriteLine("HTTP Status: {0} {1}", wex.Status, wex.Message);
            }
        }
    }
}
