using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace SMSSender
{
    public class SMSSender
    {
        MessageProcessor mProcessor = null;

        public static void Main(string[] args)
        {
            new SMSSender().process();
        }

        public void process()
        {
            try
            {
                if (!Directory.Exists("d:\\IGS\\messages"))
                    Directory.CreateDirectory("d:\\IGS\\messages");

                //opening a file for reading in text mode
                StreamReader sr = new StreamReader("d:\\IGS\\messages\\ports.txt");
                
                //file is open
                string port;
                while ((port = sr.ReadLine()) != null)
                {
                    try
                    {
                        mProcessor = new MessageProcessor(port);
                        break;
                    }
                    catch (Exception ex)
                    {
                        StreamWriter sw = new StreamWriter("d:\\hic\\messages\\err.txt", true);
                        sw.WriteLine("Error Detecting GSM Modem");
                        sw.WriteLine(ex.Message);
                        sw.Flush();
                        sw.Close();
                    }
                }//while
                sr.Close();
            }
            catch (Exception ex)
            {
                //create or append
                StreamWriter sw = new StreamWriter("d:\\IGS\\messages\\err.txt", true);
                sw.WriteLine("Error in SMS Manager");
                sw.WriteLine(ex.Message);
                sw.Flush();
                sw.Close();
            }

        }//process

    }

}
