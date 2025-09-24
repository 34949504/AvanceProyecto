package org.example.avanceproyecto;

import lombok.Getter;
import org.example.avanceproyecto.ControllerUtils.Empleado;

import java.util.ArrayList;


/*
Se encarga de hacer operaciones en el arbol binario como insertar, buscar, y eliminar
 */
public class ArbolBinario {


    public static Empleado insertNode(Empleado root, Empleado nuevo_empleado) {
        if (root == null) {
            return nuevo_empleado;
        }

        if (root.id == nuevo_empleado.id) {
            nuevo_empleado.id = resolve_same_key(nuevo_empleado.id);
            return insertNode(root, nuevo_empleado);
        }

        if (nuevo_empleado.id < root.id) {
            root.left = insertNode(root.left, nuevo_empleado);
        } else {
            root.right = insertNode(root.right, nuevo_empleado);
        }

        return root;
    }
    public static Integer resolve_same_key(Integer id) {
        return  id + 7;
    }
    public static void inorder(Empleado root, ArrayList<Empleado> empleadoArrayList) {
        if (root != null) {
            inorder(root.left, empleadoArrayList);
            empleadoArrayList.add(root);
            inorder(root.right, empleadoArrayList);
        }
    }
    public static EmpleadoInfo buscarEmpleado(Integer id,Empleado root) {

        if (rootIsNull(root)) {
            return new EmpleadoInfo(null, ErrorReason.RootNull);
        }
        Empleado pointer = root;
        while (true) {
            if (pointer == null) {
                return new EmpleadoInfo(null, ErrorReason.IDNotFOund);
            }
            int current_id = pointer.id;
            if (id > current_id) {
                pointer = pointer.right;
            }
            else if (id <current_id) {
                pointer = pointer.left;
            }
            else if (id == current_id) {
                return new EmpleadoInfo(pointer, ErrorReason.None);
            }
        }
    }
    private static boolean rootIsNull(Empleado empleadoNodo) {
        return empleadoNodo == null;
    }

    public static enum ErrorReason{
        RootNull,
        IDNotFOund,
        None;
    }


    @Getter
    public static class EmpleadoInfo {
        private Empleado empleadoNodo;
        private ErrorReason errorReason;

        public EmpleadoInfo(Empleado empleadoNodo,ErrorReason errorReason) {
            this.empleadoNodo = empleadoNodo;
            this.errorReason = errorReason;
        }
    }



    /**
     *
     * @return
     */
    public static Empleado eliminarEmpleado(Empleado root, Integer id) {
        if (root == null) return null;

        if (id < root.id) {
            root.left = eliminarEmpleado(root.left, id);
        } else if (id > root.id) {
            root.right = eliminarEmpleado(root.right, id);
        } else { // Found the node to delete
            // Case 1: Leaf
            if (root.left == null && root.right == null) {
                return null;
            }
            // Case 2: One child
            else if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            // Case 3: Two children
            else {
                Empleado successor = findMin(root.right);
                root.id = successor.id;
                root.right = eliminarEmpleado(root.right, successor.id);
            }
        }

        return root;
    }

    // Helper to find the minimum in a subtree
    private static Empleado findMin(Empleado node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }



}
