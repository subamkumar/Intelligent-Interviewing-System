using System;
using System.Threading;
using System.IO;
using System.IO.Ports;

namespace SMSSender
{
    public class GSMHandler
    {
        SerialPort port;

        public GSMHandler(String comPort)
        {
            //access the port and perform handshake
            port = new SerialPort(comPort, 9600, Parity.None, 8, StopBits.One);
            //open it and take its ownership
            port.Open();

            //AT command support check
            string cmd = "AT"; //tell the device : ATTENION HERE
            WriteLn(cmd);

            string res;
            res = ReadResponse();
            if (res.IndexOf("OK") == -1)//-1 means OK not found in res
            {
                port.Close();
                throw new Exception("Connected Device Doesn't Support AT Commands");
            }

            cmd = "AT+CMGF=1"; //tell the device : IO would be in text mode (ASCII)
            WriteLn(cmd);
            res = ReadResponse();

            if (res.IndexOf("OK") == -1)
            {
                port.Close();
                throw new Exception("Error Changing Device I/O Mode to Text");
            }
        }//constructor ends

        public bool SendMessage(string phNum, string msg)
        {
            try
            {
                string cmd;
                cmd = "AT+CMGS=\"" + phNum + "\"";
                WriteLn(cmd);
                cmd = msg + (char)(26); //(char) 26 is ctrl Z
                WriteLn(cmd);
                string res = ReadResponse();
                if (res.IndexOf("OK") != -1)
                    return true;
                else
                    return false;
            }
            catch
            {
                return false;
            }
        }


        void WriteLn(string value)
        {
            value = value + (char)13;
            //send the data
            port.Write(value);
            //delay for device processing
            Thread.Sleep(1000);
        }

        string ReadResponse()
        {
            Thread.Sleep(1000);
            int data;
            string response = "";
            
            while (port.BytesToRead > 0)
            {
                data = port.ReadByte();
                response = response + (char)data;
                Thread.Sleep(10);
            }

            return response;
        }

        public void ClosePort()
        {
            port.Close();
        }

        ~GSMHandler()
        {
            try
            {
                if (port.IsOpen == true)
                {
                    port.Close();
                }
            }
            catch (Exception ex)
            {
                Console.Write(ex.StackTrace);
                //do nothing
            }
        }
    }

}
