using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ARModelController : MonoBehaviour
{
    public static ARModelController instance;

    [SerializeField] private GameObject[] models;
    [SerializeField] private GameObject child;

    private void Awake()
    {
        if (instance != null)
        {
            Destroy(gameObject);
        }
        instance = this;
    }

    public void ChangeModel(int index)
    {
        if (index < 0 || index >= models.Length)
        {
            return;
        }

        if (child)
        {
            Destroy(child);
        }

        GameObject newModel = Instantiate(models[index], gameObject.transform);
        child = newModel;
    }

    public void RotateChild(int rotationFactor)
    {
        if (child != null)
        {
            child.transform.Rotate(Vector3.up, rotationFactor);
        }
    }
}
