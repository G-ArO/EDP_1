package edp;

public class Diccionario{
    
    private Nodo[] tabla;
    private int tamanoOcupado;
    private Nodo primero;
    private Nodo ultimo;
    private final double factorDeCarga = 0.8;
    
    
    /**
     * Constructor Principal.
     * @param tamano capacidad inicial de la tabla hash.
     */
    private Diccionario(int tamano){
        this.tabla = new Nodo[tamano];
        this.primero = null;
        this.ultimo = null;        
    }

    /**
     * Constructor por defecto.
     * Llama al constructor principal con un tamaño predefinido de 16.
     */
    public Diccionario(){
        this(16);
        
    }
    
    
    /**
     * Añade un par clave-valor al diccionario.
     * * @param clave Clave única para identificar el valor.
     * @param valor Valor asociado a la clave.
     * @throws TypeError (O Exception según definición) si la clave no es válida.
     */
    public void add(Object clave, Object valor) throws TypeError{
        Nodo nodo;
        //Comprobamos que key es valida --> Devuelve un error.
        if (! isValidKey(clave)){
            throw new IllegalArgumentException("Clave nula o invalida");        
            
        }

        int indicePrimerLiberado = -1; // -1 == No encontrado
        boolean  creaNodo = true;
        int indice = 0;

        //Buscamos indice
        for(int intentos = 0; intentos < tabla.length; intentos++){
            indice = funcionDispersion(clave, intentos);
            if(tabla[indice] == null){
                indice = indicePrimerLiberado != -1 ?  indicePrimerLiberado : indice;
                break;
            }
            if(tabla[indice].getClave().equals(clave)){
                creaNodo = false;
                break;
            }
            if(indicePrimerLiberado == -1){
                if(tabla[indice].getLiberado()) {
                    indicePrimerLiberado = indice;
                }
            }            
        }

        // En caso de que el indice apunte a null/liberado: Creamos nodo
        if(creaNodo) {
            if (primero == null){
                nodo = new Nodo(clave, valor);
                primero = nodo;
                ultimo = nodo;
            } else {
                nodo = new Nodo(clave, valor, ultimo);
                // Añadimos el nodo a la linked list
                ultimo.ModificarSiguiente(nodo);
                ultimo = nodo;
            }
            tabla[indice] = nodo;
            tamanoOcupado++;
        } else {
            // En caso contrario el nodo está en la linked list -> solo modificamos el valor y en el array
            tabla[indice].setValor(valor);
        }
        
        // Si pasa del 80% -->  duplicar tamaño (función trasladar)
        if (tamanoOcupado >= factorDeCarga * tabla.length) {
            trasladar();
        }
    }
    

    /**
     * Elimina un elemento del diccionario dada su clave.
     * @param clave La clave del elemento a eliminar.
     * @return El valor del elemento eliminado.
     * @throws KeyError si la clave no es válida o no existe.
     */
    public Object del(Object clave) throws KeyError{
        //Comprobamos que key es valida --> metodo isValidKey(), lanza excepción si no encuentra
        if (!isValidKey(clave)) {
            throw new KeyError("unhashable type: '" + clave.getClass().getSimpleName() + "'");
        }
        // Buscar si la clave esta en el array --> get()
        Object valor = get(clave);
        if (valor == null) throw new KeyError("key not found"); // No se encuentra la clave
        
        int intentos = 0;
        int indice = funcionDispersion(clave, intentos);

        //Mientras no encontremos la clave seguimos iterando (ya sabemos que está en la tabla). 
        while(! tabla[indice].getClave().equals(clave)){
            intentos++;
            indice = funcionDispersion(clave, intentos);
        }

        // Borramos el nodo poniendo su atributo liberado a true (evita problemas con las colisiones)
        Nodo nodo = tabla[indice];
        nodo.elimina();
        tamanoOcupado--;
        // Eliminamos Nodo de la linked list
        if (clave.equals(primero.getClave())){
            primero = nodo.getSiguiente();
        } else if (clave.equals(ultimo.getClave())){
            ultimo = nodo.getAnterior();
        } else {
            nodo.getAnterior().ModificarSiguiente(nodo.getSiguiente());
            nodo.getSiguiente().ModificarAnterior(nodo.getAnterior());
        }        
        return valor;
    }
        
    /**
     * Recupera el valor asociado a una clave.
     * @param clave La clave a buscar.
     * @return El valor asociado o null si no se encuentra.
     * @throws TypeError si la clave no es válida para el sistema de dispersión.
     */
    public Object get(Object clave) throws TypeError{
        //Comprobamos que key es valida --> metodo isValidKey()
        if (! isValidKey(clave)){
            throw new TypeError("unhashable type: '" + clave.getClass().getSimpleName() + "'");
        }

        int intentos = 0;
        int indice = funcionDispersion(clave, intentos); // Solo cambia esto
        boolean encontrado = false;

        //Mientras no encontremos la clave seguimos iterando. 
        while(! encontrado && tabla[indice] != null){
            if (tabla[indice].getClave().equals(clave)  && !tabla[indice].getLiberado()) {
                encontrado = true;
            } else {
                intentos++;
                indice = funcionDispersion(clave, intentos);
            }
            
        }

        //Devolvemos y acabamos
        if (encontrado) {
            return tabla[indice].getValor();
        } else {
            return null;
        }
    }
    
    /**
     * Obtiene todas las claves presentes en el diccionario.
     * @return Array de objetos con las claves en orden de inserción.
    */
    public Object[] keys(){
        Nodo current = primero;
        Object[] claves = new Object[tamanoOcupado];
        for(int i = 0; i < tamanoOcupado; i++){
            Object clave = current.getClave();
            claves[i] = clave;
            current = current.getSiguiente();
        }
        return claves;
    }
    
    /**
     * Obtiene todos los valores presentes en el diccionario.
     * @return Array de objetos con los valores en orden de inserción.
    */
    public Object[] values(){
        Nodo current = primero;
        Object[] valores = new Object[tamanoOcupado];
        for(int i = 0; i < tamanoOcupado; i++){
            Object valor = current.getValor();
            valores[i] = valor;
            current = current.getSiguiente();
        }
        return valores;
    }
    
    /**
     * Obtiene los pares clave-valor (entradas) del diccionario.
     * @return Un array donde cada elemento es un Array [clave, valor].
     */
    public Object[][] items(){
        Nodo current = primero;
        Object[][] items = new Object[tamanoOcupado][2];
        for(int i = 0; i < tamanoOcupado; i++){
            Object valor = current.getValor();
            Object clave = current.getClave();
            items[i] = new Object[]{clave,valor};
            current = current.getSiguiente();
        }
        return items;
    }

    /**
     * Calcula el índice en la tabla para una clave dada, considerando el número de intentos.
     * @param clave Objeto clave para calcular el hash.
     * @param intentos Número de colisiones previas encontradas.
     * @return Índice válido dentro del rango [0, tamano - 1].
     */
    private int funcionDispersion(Object clave, int intentos) {
        return (clave.hashCode() + funcionColisiones(intentos)) & (tabla.length - 1);
    }

    /**
     * Define la estrategia de resolución de colisiones (Probiado Lineal).
     * @param intentos Número de intentos realizados.
     * @return Desplazamiento a sumar al índice original.
     */
    private int funcionColisiones(int intentos){
        return intentos;
    }
    
    /**
     * Reestructura la tabla hash duplicando su tamaño y reinsertando los elementos.
     * Se dispara automáticamente cuando se supera el factor de carga (80%).
     */
    private void trasladar() {
        int nuevoTamano = 2 * tabla.length;
        tabla = new Nodo[nuevoTamano];
        Nodo current = primero;
        // Vaciamos la linked list ya que la funcion add la modifica
        primero = null;
        ultimo = null;
        
        while (current != null){
            add(current.getClave(), current.getValor());
            current = current.getSiguiente();
        }
    }
    
    /**
     * Valida si una clave es apta para el diccionario (debe ser no nula e inmutable).
     * @param key La clave a validar.
     * @return true si la clase de la clave está en la lista de permitidas.
     */
    private boolean isValidKey(Object key){
        Object[] clasesInmutables = {String.class, Integer.class, Float.class,
        Double.class, Boolean.class, Long.class, Short.class, Byte.class, Character.class};
        if (key == null) return false;
        Class<?> keyClass = key.getClass();
        for (Object claseInmutable : clasesInmutables) {
            if (claseInmutable == keyClass) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Reinicia el diccionario a su estado inicial.
     */
    public void clear(){
        tabla = new Nodo[16];
        primero = null;
        ultimo = null;
    }
    
    /**
     * Crea una copia superficial (shallow copy) del diccionario actual.
     * @return Una nueva instancia de Diccionario con los mismos elementos.
     */
    public Diccionario copy(){
        Diccionario nuevo = new Diccionario(tabla.length);
        Nodo current = primero;
        while(current != null) {
            nuevo.add(current.getClave(), current.getValor());
            current = current.getSiguiente();
        }
        return nuevo;
    }


    /**
     * Extrae y elimina el último elemento insertado (comportamiento LIFO).
     * Implementa del
     * @return Un array con [clave, valor].
     */
    public Object[] popItem(){
        Object[] resultado =  new Object[]{ultimo.getClave(), ultimo.getValor()};
        del(ultimo.getClave());
        return resultado;
    }

    /**
     * Elimina una clave y devuelve su valor. Si no existe, devuelve un valor por defecto.
     * Implementa del
     * @param clave Clave a eliminar.
     * @param valorPorDefecto Valor a retornar si la clave no existe.
     * @return El valor eliminado o el valor por defecto.
     * @throws KeyError Si la clave no existe y valorPorDefecto es null.
    */
    public Object pop(Object clave, Object valorPorDefecto) throws KeyError{
        try{
            return del(clave);
        } catch (KeyError e) {
            if(valorPorDefecto == null) {
                throw new KeyError("unhashable type: '" + clave.getClass().getSimpleName() + "'");
            } else {
                return valorPorDefecto;
            }
        }
        
    }

    
    /**
     * Actualiza el diccionario actual con los elementos de otro diccionario.
     * @param dicci El otro diccionario del cual copiar los datos.
     */
    public void update(Diccionario dicci){
        for(Object key_otro : dicci.keys()){
            this.add(key_otro, dicci.get(key_otro));
        }
    }

    /**
     * Sobrecarga de setDefault para usar null como valor predefinido.
     */
    public Object setdefault(Object clave){
        return setDefault(clave, null);
    }

    /**
     * Si la clave existe, devuelve su valor. Si no, la inserta con el valor proporcionado.
     * @param clave Clave a buscar o insertar.
     * @param valor Valor a asignar si no existe.
     * @return El valor actual o el recién insertado.
    */
    public Object setDefault(Object clave, Object valor){
        Object valorEncontrado = get(clave);
        if (valorEncontrado != null){
            return valorEncontrado;
        } else {
            add(clave, valor);
            return valor;
        }
    }

    /**
     * Devuelve la capacidad actual de la tabla hash.
     */
    private int size(){
        return tabla.length;
    }


}