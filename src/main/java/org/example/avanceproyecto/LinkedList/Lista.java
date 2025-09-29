package org.example.avanceproyecto.LinkedList;

import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Tarea.TareaNodo;

import java.util.ArrayList;

/**
 * Solamente agrega las
 */
public class Lista   {
    ArrayList<TareaNodo> tareaNodoArrayList = new ArrayList<>();

    public Lista(ArrayList<Observer> observers) {
    }


    public void insert(TareaNodo tareaNodo) {
        tareaNodoArrayList.add(tareaNodo);
    }
    public void delete(int index) {
        if (index <0 && index >= tareaNodoArrayList.size()) {
            return;
        }
        tareaNodoArrayList.remove(index);
    }
    public void find() {

    }
    public ArrayList<TareaNodo> getAllNodes(){
        return  tareaNodoArrayList;
    }

}
