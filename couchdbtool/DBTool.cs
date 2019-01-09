using System;
using System.Diagnostics;
using System.Threading;

namespace couchdbtool
{
    class DBTool
    {
        private static String file_10K  = @"D:\tuneathon\xlcs_console_10K.LOG";
        private static String file_100K = @"D:\tuneathon\xlcs_console_10K.LOG";
        private static String file_1M   = @"D:\tuneathon\xlcs_console_1M.LOG";
        private static int testoffset = 0;

        static void Main(string[] args)
        {
            DBDriver driver = new DBDriver(file_10K);
            driver.GetDBInfo();

            testoffset = 10;
            RunTests(file_1M, 250);
        }
        
        static void RunTests(String file, int iterations)
        {
            DBDriver[] drivers = new DBDriver[iterations];
            Thread[] thr = new Thread[iterations];

            for (int i = 0; i < iterations; i++)
            {
                drivers[i] = new DBDriver(file);
                thr[i] = new Thread(drivers[i].InserFile);
            }

            var stopwatch = new Stopwatch();
            stopwatch.Start();
            for (int i = 0; i < iterations; i++)
            {
                thr[i].Start(Convert.ToString(i));
            }

            for (int i = 0; i < iterations; i++)
            {
                thr[i].Join();
            }
            stopwatch.Stop();

            Console.WriteLine("Elapsed time for " + iterations
                + " concurrent insertions: {0}", stopwatch.ElapsedMilliseconds);

            Console.WriteLine("Finished waiting for all threads");
        }       
    }
}
