package edp;

public class Nodo{
    private Object valor;
    private Object clave;
    private Nodo anterior;
    private Nodo siguiente;
    private boolean liberado;
    
    public Nodo(Object clave, Object valor, Nodo anterior){
        this.valor = valor;
        this.clave = clave;
        this.anterior = anterior;
        this.siguiente = null;
        this.liberado = false;
    }

    public Nodo(Object clave, Object valor){
        this.valor = valor;
        this.clave = clave;
        this.anterior = null;
        this.siguiente = null;
        this.liberado = false;
    }
    
    public Nodo getAnterior(){
        return this.anterior;
    }
    
    public void ModificarAnterior(Nodo nuevoAnterior){
        this.anterior = nuevoAnterior;
        return;
    }
    
    public Nodo getSiguiente(){
        return this.siguiente;
    }
    
    public void ModificarSiguiente(Nodo nuevoSiguiente){
        this.siguiente = nuevoSiguiente;
        return;
    }
    
    public Object getClave(){
        return this.clave;
    }
    
    public void setClave(Object c){
        this.clave = c;
        return;
    }
    
    public Object getValor(){
        return this.valor;
    }
    
    public void setValor(Object v){
        this.valor = v;
        return;
    }
    
    public void elimina(){
        this.liberado = true;
    }
    
    public boolean getLiberado(){
        return this.liberado;
    }
}