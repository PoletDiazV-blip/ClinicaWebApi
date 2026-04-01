import com.presentation.controllers.*;
import com.data.repositories.*;
import com.domain.repositories.*;
import com.data.persistence.DatabaseConfig;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 1. Instancias de Repositorios y Controladores (Sin cambios)
        ICitaRepository citaRepo = new CitaRepositoryImpl();
        CitaController citaController = new CitaController(citaRepo);

        IHorarioRepository horarioRepo = new HorarioRepositoryImpl();
        HorarioController horarioController = new HorarioController(horarioRepo);

        DoctorRepositoryImpl doctorRepo = new DoctorRepositoryImpl();
        DoctorController doctorController = new DoctorController(doctorRepo);

        // 2. Configuración de Javalin
        var app = Javalin.create(config -> {
                    // Habilitar CORS para que Vercel pueda conectar con el Backend
                    config.bundledPlugins.enableCors(cors -> {
                        cors.addRule(it -> it.anyHost());
                    });

                    config.router.mount(router -> {
                        router.get("/", ctx -> ctx.result("Servidor corriendo: Bienvenido a ClinicaWeb"));

                        // --- MÓDULO DE AUTENTICACIÓN ---
                        router.post("/auth/login", AuthController::login);
                        router.post("/auth/register", UsuarioController::crear);

                        // --- MÓDULO DE CITAS (DOCTOR) ---
                        router.get("/doctores/{id}/stats", UsuarioController::obtenerStats);
                        router.get("/doctores/{id}/citas", citaController::getCitasPorDoctor);
                        router.put("/citas/{id}/estado", citaController::actualizarEstado);

                        // --- MÓDULO DE HORARIOS ---
                        router.get("/doctores/{id}/horarios", horarioController::listar);
                        router.post("/horarios", horarioController::crear);
                        router.delete("/horarios/{id}", horarioController::eliminar);

                        // --- MÓDULO DE PERFIL DOCTOR ---
                        router.get("/doctores/{id}/perfil", doctorController::obtenerPerfil);
                        router.put("/doctores/{id}/perfil", doctorController::actualizarPerfil);

                        // --- MÓDULO DE PACIENTES ---
                        router.get("/pacientes/doctores", doctorController::listarDoctores);
                        router.post("/pacientes/agendar", citaController::agendarCita);
                        router.get("/pacientes/{id}/citas", citaController::getCitasPorPaciente);
                        router.delete("/citas/{id}", citaController::eliminarCita);
                        router.get("/pacientes/{id}/stats", citaController::getStatsPaciente);

                        // --- MÓDULO DE PERFIL PACIENTE ---
                        router.get("/usuarios/{id}/perfil", UsuarioController::obtenerPerfil);
                        router.put("/usuarios/{id}", UsuarioController::actualizar);

                        // --- MÓDULO CRUD DE USUARIOS ---
                        router.get("/usuarios", UsuarioController::listar);
                        router.delete("/usuarios/{id}", UsuarioController::eliminar);
                    });

                })
                .exception(Exception.class, (e, ctx) -> {
                    logger.error("Error no controlado: {}", e.getMessage());
                    ctx.status(500).json(Map.of("error", "Ocurrio un error interno en el servidor"));
                })
                .error(404, ctx -> {
                    ctx.json(Map.of("error", "La ruta solicitada no existe en ClinicaWeb"));
                });

        // --- CONFIGURACIÓN DE PUERTO PARA DESPLIEGUE (Railway/Render) ---
        // Lee el puerto del sistema, si no existe usa el 8080 (Local)
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        app.start(port);

        logger.info("Servidor ClinicaWeb encendido correctamente en el puerto: " + port);

        // Gancho de apagado para cerrar el pool de conexiones de Aiven
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Apagando servidor...");
            DatabaseConfig.closePool();
        }));
    }
}