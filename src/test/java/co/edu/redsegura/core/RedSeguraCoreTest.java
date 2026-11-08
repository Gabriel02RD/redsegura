package co.edu.redsegura.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.redsegura.core.RedSeguraCore.SearchComparison;
import co.edu.redsegura.exception.NotFoundException;
import org.junit.jupiter.api.Test;

class RedSeguraCoreTest {

    @Test
    void q1HospitalUniversitarioElAvlNecesitaMenosPasosQueElAbb() {
        RedSeguraCore core = RedSeguraCore.withDefaultData();
        SearchComparison r = core.compareSearchByLabel("Hospital Universitario");
        assertTrue(r.found());
        assertEquals("S05", r.key());
        assertEquals(5, r.bstSteps());
        assertEquals(4, r.avlSteps());
        assertEquals(12, r.bstHeight());
        assertEquals(4, r.avlHeight());
    }

    @Test
    void etiquetaInexistenteLanzaExcepcion() {
        RedSeguraCore core = RedSeguraCore.withDefaultData();
        assertThrows(NotFoundException.class, () -> core.findVertexByLabel("Lugar que no existe"));
    }

    @Test
    void q2LaDemostracionIncluyeLasCuatroRotaciones() {
        String text = RedSeguraCore.withDefaultData().demonstrateRotations();
        for (String type : new String[]{"LL", "RR", "LR", "RL"}) {
            assertTrue(text.contains("Rotación " + type), "Falta " + type);
        }
    }

    @Test
    void q5RutaDeControl() {
        RedSeguraCore core = RedSeguraCore.withDefaultData();
        assertEquals(24, core.shortestRoute("S02", "S08").totalMinutes());
    }

    @Test
    void q6BloqueoYRestauracion() {
        RedSeguraCore core = RedSeguraCore.withDefaultData();
        core.blockConnection("R16");
        assertEquals(5, core.shortestRoute("S02", "S08").path().size());
        core.unblockConnection("R16");
        assertEquals(4, core.shortestRoute("S02", "S08").path().size());
    }

    @Test
    void q7VerticeInexistenteNoTerminaElPrograma() {
        RedSeguraCore core = RedSeguraCore.withDefaultData();
        assertThrows(NotFoundException.class, () -> core.neighborsOf("S99"));
        // El objeto sigue siendo utilizable después del error
        assertEquals(24, core.shortestRoute("S02", "S08").totalMinutes());
    }

    @Test
    void describePathIncluyeEtiquetas() {
        RedSeguraCore core = RedSeguraCore.withDefaultData();
        String text = core.describePath(core.shortestRoute("S02", "S08"));
        assertTrue(text.contains("UAJS Sede E"));
        assertTrue(text.contains("Terminal de Transporte"));
    }
}
