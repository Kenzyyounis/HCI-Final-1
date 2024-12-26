using System.Collections;
using UnityEngine;
using System.IO;
using System.Net.Sockets;

public class CSocketClient : MonoBehaviour
{
    private TcpClient client;
    private NetworkStream stream;
    private StreamReader reader;

    public string host = "127.0.0.1";
    public int port = 4200;

    private void Awake()
    {
        // Start the connection and JSON receiving in a coroutine
        StartCoroutine(HandleConnectionCoroutine());
    }

    private IEnumerator HandleConnectionCoroutine()
    {
        yield return CreateConnectionCoroutine();
        yield return ReceiveAndPrintJsonCoroutine();
    }

    private IEnumerator CreateConnectionCoroutine()
    {
        client = new TcpClient();

        var connectTask = client.ConnectAsync(host, port);
        while (!connectTask.IsCompleted)
        {
            yield return null;
        }

        if (connectTask.Exception != null)
        {
            Debug.LogError("Connection failed: " + connectTask.Exception.GetBaseException().Message);
            yield break;
        }

        stream = client.GetStream();
        reader = new StreamReader(stream);
        Debug.Log("Connected to the server.");
    }

    private IEnumerator ReceiveAndPrintJsonCoroutine()
    {
        while (true)
        {
            string data = null;
            var readTask = reader.ReadLineAsync();
            while (!readTask.IsCompleted)
            {
                yield return null; // Wait for the data to be received
            }

            if (readTask.Exception != null)
            {
                Debug.LogError("Error reading data: " + readTask.Exception.GetBaseException().Message);
                // break;
            }

            data = readTask.Result;
            // if (string.IsNullOrEmpty(data))
            // {
            //     Debug.Log("No data received, closing connection.");
            //     break;
            // }

            JsonData jsonData = JsonUtility.FromJson<JsonData>(data);
            Debug.Log("Received Data:");
            Debug.Log($"Action: {jsonData.Action}, Value: {jsonData.Value}");

            if (ARModelController.instance)
            {
                if (jsonData.Action == "ChangeModel")
                {
                    ARModelController.instance.ChangeModel(jsonData.Value);
                }

                else if (jsonData.Action == "RotateModel")
                {
                    ARModelController.instance.RotateChild(jsonData.Value);
                }
            }
        }
    }

    public void CloseConnection()
    {
        if (reader != null)
            reader.Close();
        if (stream != null)
            stream.Close();
        if (client != null)
            client.Close();
        Debug.Log("Connection closed.");
    }

    private void OnApplicationQuit()
    {
        CloseConnection();
    }
}

[System.Serializable]
public class JsonData
{
    public string Action;
    public int Value;
}
