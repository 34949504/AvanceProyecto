package org.example.avanceproyecto.Controllers;
/*
Datos atomicos que permite una facil comunicacion entre dos clases como VerTareas y TaskDoer
 */

import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ArbolBinario;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter @Setter
public class SharedStates {
    private AtomicBoolean thread_active = new AtomicBoolean(true);
    private AtomicInteger speed = new AtomicInteger(1000);

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JSONObject departamentos_id_json;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JSONObject empleados_json;

    private HashMap<String,Integer> departamentos_id_hashmap = new HashMap<>();
    private HashMap<Integer,String> departamentos_id_name_hashmap = new HashMap<>();
    private ArrayList<String> departamentos_names = new ArrayList<>();
    private HashMap<Integer,Empleado> departamentos_binaryTrees = new HashMap<>(); //departamento id and Node
    private Stage stage;


 public SharedStates(JSONObject departamentos_id_json, JSONObject empleados_json) { this.departamentos_id_json = departamentos_id_json; this.empleados_json = empleados_json;


        fillDepartamentosId();//FIRST
//        fillEmpleados();
        createBinaryTrees();
    }

    private void fillDepartamentosId() {
        Iterator<String> keys = departamentos_id_json.keys();
        while (keys.hasNext()) {
            String departamento_name = keys.next();
            Integer id = departamentos_id_json.getInt(departamento_name);
            departamentos_id_hashmap.put(departamento_name,id);
            departamentos_names.add(departamento_name);
            departamentos_id_name_hashmap.put(id,departamento_name);
        }


    }

    public Integer getDepartamentoID(String departamento_name) {
        return  departamentos_id_hashmap.get(departamento_name);
    }
    public ArrayList<Empleado> getEmpleadosArray(String departamento_name) {
        int departamento_id = getDepartamentoID(departamento_name.toLowerCase());
        Empleado root = departamentos_binaryTrees.get(departamento_id);
        ArrayList<Empleado> empleadoArrayList = new ArrayList<>();
        ArbolBinario.inorder(root,empleadoArrayList);
        return empleadoArrayList;
    }
    public static class Colores {
        // Colores básicos como constantes
        public static final String BLANCO = "#FFFFFF"; // blanco
        public static final String NEGRO = "#000000";  // negro
        public static final String ROJO = "#FF0000";   // rojo
        public static final String VERDE = "#00FF00";  // verde
        public static final String AZUL = "#0000FF";   // azul
        public static final String AMARILLO = "#FFFF00"; // amarillo
        public static final String NARANJA = "#FFA500";  // naranja
        public static final String MORADO = "#800080";   // morado
        public static final String ROSA = "#FFC0CB";     // rosa
        public static final String CIAN = "#00FFFF";     // cian
    }

    private void createBinaryTrees() {

        for(String departamento:departamentos_names) {
            JSONObject dep_json = empleados_json.getJSONObject(departamento);
            JSONArray empleados_ordenados = dep_json.getJSONArray("empleados_ordenados");
            int departamento_id = departamentos_id_hashmap.get(departamento);

            Empleado root = null;
            for (int i = 0; i < empleados_ordenados.length(); i++) {
                int emp_key_int =  empleados_ordenados.getInt(i);
                String emp_key = Integer.toString(emp_key_int);

                JSONObject empleado_json = dep_json.getJSONObject(emp_key);
                String nombre = empleado_json.getString("nombre");
                String apellidos = empleado_json.getString("apellidos");
                String fecha_nacimiento = empleado_json.getString("fecha_nacimiento");

                Empleado empleado = new Empleado(nombre,apellidos,departamento_id,fecha_nacimiento);

                if (!departamentos_binaryTrees.containsKey(departamento_id)) {
                    departamentos_binaryTrees.put(departamento_id,empleado);
                    root = empleado;
                    continue;
                }
                ArbolBinario.insertNode(root,empleado);
            }
        }

    }
    public String getDepartamentoName(int id) {
     return departamentos_id_name_hashmap.get(id);

    }



}
