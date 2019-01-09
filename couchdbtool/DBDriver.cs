using Newtonsoft.Json;
using System.Net.Http;
using System.Net.Http.Headers;
using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace couchdbtool
{
    class DBDriver
    {
        private static String dburl = "http://localhost:27017/";
        private static String dbname = "library/iter1";
        private HttpClientHandler httphandler = null;
        private HttpClient httpclient = null;
        private String testfile = null;
        private static Byte[] rawdata = null;

        public DBDriver(String file)
        {
            httphandler = new HttpClientHandler();
            httpclient = new HttpClient(httphandler);
            testfile = file;
            if (null == rawdata)
            {
                rawdata = File.ReadAllBytes(file);
            }
        }

        public void GetDBInfo()
        {
            var result = httpclient.GetAsync(dburl + dbname).Result;
            if (result.IsSuccessStatusCode)
            {
                Console.WriteLine(result.Content.ReadAsStringAsync().Result);
            }
            else
            {
                Console.WriteLine(result.ReasonPhrase);
            }
        }

        public void InserFile(Object o)
        {
            String url = dburl + dbname;
            String id = o as String;

            DBJSONContent jcontent = new DBJSONContent { Id = id };
            jcontent.Data = rawdata;

            var str = JsonConvert.SerializeObject(jcontent);

            HttpContent content = new StringContent(str, Encoding.UTF8, "application/json");
            var result = httpclient.PostAsync(url, content).Result;
            if (result.IsSuccessStatusCode)
            {
                Console.WriteLine(result.Content.ReadAsStringAsync().Result);
            }
            else
            {
                Console.WriteLine(result.ReasonPhrase);
            }

            str = null;
        }
    }
}
