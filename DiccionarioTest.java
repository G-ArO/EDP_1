import edp.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DiccionarioTest {

    private Diccionario diccionario;

    @Before
    public void setUp() {
        diccionario = new Diccionario();
    }

    @Test
    public void testContenedorVacio() {
        Object[] claves = diccionario.keys();
        assertEquals("Un diccionario recien creado debe tener 0 claves", 0, claves.length);
    }

    @Test
    public void testAddYGetGenericos() {
        diccionario.add("puntos", 100);
        assertEquals("Deberia recuperar el entero 100", 100, diccionario.get("puntos"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExcepcionClaveInvalida() {
        diccionario.add(null, 50);
    }

    // IA-assisted: Prueba de estres para verificar el metodo trasladar()
    @Test
    public void testRedimensionamientoMasivo() {
        for (int i = 0; i < 500; i++) {
            diccionario.add("clave" + i, i);
        }
        assertEquals("El redimensionamiento deberia mantener los datos", 250, diccionario.get("clave250"));
    }

    @Test
    public void testPopItem() {
        diccionario.add("primero", 1);
        diccionario.add("segundo", 2);
        
        diccionario.popItem();
        
        assertNull("El elemento ya no debe existir", diccionario.get("segundo"));
        assertEquals("El tamaño debe haberse reducido a 1", 1, diccionario.keys().length);
    }

    @Test
    public void testCopy() {
        diccionario.add("original", 10);
        Diccionario copia = diccionario.copy();
        
        assertEquals("La copia debe tener los mismos datos", 10, copia.get("original"));
        
        copia.add("nuevo", 20);
        assertNull("Modificar la copia no debe afectar al original", diccionario.get("nuevo"));
    }
    
    @Test
    public void testListaEnlazadOrder() {
        diccionario.add("primero", 10);
        diccionario.add("segundo", 20);
        diccionario.add("tercero", 30);

        Object[] claves = diccionario.keys();
        assertEquals("El primer punter debe apuntar a 'primero'", "primero", claves[0]);
        assertEquals("El puntero 'siguiente' del primero debe apuntar a 'segundo'", "segundo", claves[1]);
        assertEquals("El último puntero (tercero) debe estar al final", "tercero", claves[2]);
        assertEquals("Deberia tener ele mismo tamaño la LinkedList con la tabla, tamaño:", 3, claves.length);
    }
    @Test
    public void testListaEnlazadaOrdenValores() {
        diccionario.add("primero", 10);
        diccionario.add("segundo", 20);
        diccionario.add("tercero", 30);
    
        Object[][] items = diccionario.items();
        assertEquals("El valor del primer nodo (primero) debe ser 10", 10, items[0][1]);
        assertEquals("El valor del nodo central (siguiente) debe ser 20", 20, items[1][1]);
        assertEquals("El valor del ultimo nodo debe ser 30", 30, items[2][1]);
        assertEquals("Deberia haber 3 pares de elementos en la lista", 3, items.length);
    }
    @Test
    public void testActualizarValorExistente() {
        diccionario.add("primero", 50);

        diccionario.add("primero", 150);

        assertEquals("El valor deberia haberse actualizado a 150", 150, diccionario.get("primero"));

        Object[] claves = diccionario.values();
        assertEquals("El tamano no deberia aumentar al sobreescribir", 1, claves.length);
}

}