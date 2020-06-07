using System;
using System.Threading;
using System.IO;
using System.Collections.Generic;

namespace SMSSender
{
    public class MessageProcessor
    {
        string fName;
        bool sendingFlag;
        Thread t1;
        GSMHandler gsm;

        public MessageProcessor(string comPort)
        {
            fName = "d:\\IGS\\messages\\messages.txt";

            gsm = new GSMHandler(comPort);
            sendingFlag = true;
            t1 = new Thread(keepWriting);
            t1.Start();
        }

        void keepWriting()
        {
            List<string[]> messages;
            while (sendingFlag)
            {
                try
                {
                    messages = getMessagesToSend();
                    if (messages.Count > 0)
                    {
                        foreach (string[] msg in messages)
                            gsm.SendMessage(msg[0], msg[1]);
                    }
                    messages.Clear();//empty it
                }
                catch
                { }
                //delay
                Thread.Sleep(30 * 1000);
            }

        }

        List<string[]> getMessagesToSend()
        {
            List<string[]> messages = new List<string[]>();

            try
            {
                StreamReader srdr = new StreamReader(fName);
                string str = "";
                int x = 0;
                int data;
                string[] contents;
                char[] delimiter = {'~'};

                //reading the file byte by byte
                while ((data = srdr.Read()) != -1)
                {
                    str = str + (char)data;
                    
                    if (data == '~')
                    {
                        x++;
                    }

                    if (x == 2)
                    {
                        contents = str.Split(delimiter);
                        if (contents[0].Length > 0 && contents[1].Length > 0)
                        {
                            messages.Add(contents);
                        }
                        x = 0;
                        str = "";
                    }
                }//while

                srdr.Close();
                File.Delete(fName);
            }
            catch
            { }

            return messages;
        }

        ~MessageProcessor()
        {
            try
            {
                sendingFlag = false;
                gsm.ClosePort();
            }
            catch
            { }
        }
    }

}
