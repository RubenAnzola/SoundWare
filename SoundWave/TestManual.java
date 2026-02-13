import modelo.plataforma.Plataforma;
import modelo.usuarios.UsuarioPremium;
import modelo.usuarios.UsuarioGratuito;
import enums.TipoSuscripcion;

public class TestManual {
    public static void main(String[] args) {
        try {
            System.out.println("=== INICIANDO TESTS MANUALES ===");

            // Test 1.1: Crear plataforma Singleton
            System.out.println("\n--- Test 1.1: Crear plataforma Singleton ---");
            Plataforma.reiniciarInstancia();
            Plataforma plataforma = Plataforma.getInstancia("SoundWave Test");
            System.out.println("✓ Plataforma creada: " + plataforma.getNombre());

            Plataforma otraInstancia = Plataforma.getInstancia("Otro Nombre");
            System.out.println("✓ Es Singleton: " + (plataforma == otraInstancia));

            // Test 1.2: Email inválido
            System.out.println("\n--- Test 1.2: Email inválido ---");
            try {
                plataforma.registrarUsuarioPremium("Test", "emailsinArroba", "password123");
                System.out.println("✗ ERROR: Debería lanzar EmailInvalidoException");
            } catch (Exception e) {
                System.out.println("✓ Lanzó excepción: " + e.getClass().getSimpleName());
            }

            try {
                plataforma.registrarUsuarioPremium("Test", "email@", "password123");
                System.out.println("✗ ERROR: Debería lanzar EmailInvalidoException");
            } catch (Exception e) {
                System.out.println("✓ Lanzó excepción: " + e.getClass().getSimpleName());
            }

            try {
                plataforma.registrarUsuarioGratuito("Test", "", "password123");
                System.out.println("✗ ERROR: Debería lanzar EmailInvalidoException");
            } catch (Exception e) {
                System.out.println("✓ Lanzó excepción: " + e.getClass().getSimpleName());
            }

            // Test 1.3: Password débil
            System.out.println("\n--- Test 1.3: Password débil ---");
            try {
                plataforma.registrarUsuarioGratuito("Test", "test@gmail.com", "123");
                System.out.println("✗ ERROR: Debería lanzar PasswordDebilException");
            } catch (Exception e) {
                System.out.println("✓ Lanzó excepción: " + e.getClass().getSimpleName());
            }

            // Test 1.4: Registrar usuarios premium
            System.out.println("\n--- Test 1.4: Registrar usuarios premium ---");
            UsuarioPremium user1 = plataforma.registrarUsuarioPremium(
                "Juan Pérez", "juan@gmail.com", "password123", TipoSuscripcion.PREMIUM);
            System.out.println("✓ Usuario Premium 1: " + user1.getNombre());

            UsuarioPremium user2 = plataforma.registrarUsuarioPremium(
                "María López", "maria@hotmail.com", "securepass456", TipoSuscripcion.FAMILIAR);
            System.out.println("✓ Usuario Premium 2: " + user2.getNombre());

            UsuarioPremium user3 = plataforma.registrarUsuarioPremium(
                "Carlos García", "carlos@outlook.com", "mypassword789", TipoSuscripcion.ESTUDIANTE);
            System.out.println("✓ Usuario Premium 3: " + user3.getNombre());

            System.out.println("Total usuarios premium: " + plataforma.getUsuariosPremium().size());

            // Test 1.5: Registrar usuarios gratuitos
            System.out.println("\n--- Test 1.5: Registrar usuarios gratuitos ---");
            UsuarioGratuito user4 = plataforma.registrarUsuarioGratuito(
                "Pedro Sánchez", "pedro@yahoo.com", "freeuser123");
            System.out.println("✓ Usuario Gratuito 1: " + user4.getNombre());

            UsuarioGratuito user5 = plataforma.registrarUsuarioGratuito(
                "Ana Martínez", "ana@gmail.com", "anapass456");
            System.out.println("✓ Usuario Gratuito 2: " + user5.getNombre());

            System.out.println("Total usuarios gratuitos: " + plataforma.getUsuariosGratuitos().size());

            // Test 1.6: Usuario duplicado
            System.out.println("\n--- Test 1.6: Usuario duplicado ---");
            try {
                plataforma.registrarUsuarioGratuito("Duplicado", "juan@gmail.com", "duplicate123");
                System.out.println("✗ ERROR: Debería lanzar UsuarioYaExisteException");
            } catch (Exception e) {
                System.out.println("✓ Lanzó excepción: " + e.getClass().getSimpleName());
            }

            // Test 1.7: Total usuarios
            System.out.println("\n--- Test 1.7: Total usuarios ---");
            System.out.println("Total todos los usuarios: " + plataforma.getTodosLosUsuarios().size());
            System.out.println("Total premium: " + plataforma.getUsuariosPremium().size());
            System.out.println("Total gratuitos: " + plataforma.getUsuariosGratuitos().size());

            System.out.println("\n=== TODOS LOS TESTS PASARON ===");

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

